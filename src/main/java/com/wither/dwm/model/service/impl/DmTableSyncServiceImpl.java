package com.wither.dwm.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wither.dwm.common.component.TableHdfsProcessor;
import com.wither.dwm.common.component.TableHiveProcessor;
import com.wither.dwm.common.constants.CommonCodes;
import com.wither.dwm.common.mapper.HiveMapper;
import com.wither.dwm.model.bean.DmTable;
import com.wither.dwm.model.bean.DmTableDataInfo;
import com.wither.dwm.model.bean.DmTableSync;
import com.wither.dwm.model.mapper.DmTableSyncMapper;
import com.wither.dwm.model.service.DmTableDataInfoService;
import com.wither.dwm.model.service.DmTableService;
import com.wither.dwm.model.service.DmTableSyncService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wither.dwm.plan.bean.DpDataWarehouseModel;
import com.wither.dwm.plan.service.DpDataWarehouseModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jdo.annotations.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据同步信息表 服务实现类
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Service
public class DmTableSyncServiceImpl extends ServiceImpl<DmTableSyncMapper, DmTableSync> implements DmTableSyncService {
    @Autowired
    private TableHiveProcessor tableHiveProcessor;

    @Autowired
    private DmTableService dmTableService;

    @Autowired
    private DpDataWarehouseModelService dpDataWarehouseModelService;

    @Autowired
    private DmTableDataInfoService dmTableDataInfoService;
    /**
     * 1 从Hive获取需要同步的表列表。
     * 2 为这个列表补充信息做准备
     * 2.1 准备已存在的同步信息列表，map,key为表名，value为DmTableSync对象
     * 2.2 准备模型数据表列表，map,key为表名，value为DmTable对象
     * 2.3 准备数仓模型列表，map,key为数据库名，value为DpDataWarehouseModel对象
     * 3 循环 hive数据表列表
     *    有数据同步 信息则补充，没有则模型数据表列表，进行补充 再没有查数仓模型补充数仓模型信息
     *    最终装配为 DmTableSync对象列表
     * @param schemaName Hive中的模式名，用于查询表列表。
     * @return 包含需要同步的表的配置信息的列表。
     * @throws RuntimeException 如果查询Hive时发生异常，则抛出运行时异常。
     */
    @Override
    public List<DmTableSync> getSyncTableListFromHive(String schemaName) {
        try {
            //1 从Hive获取需要同步的表列表。
            List<String> tableNameList = tableHiveProcessor.getMetaClient().getAllTables(schemaName);
            //2为这个列表补充信息做准备
            //2.1 准备已存在的同步信息列表，map,key为表名，value为DmTableSync对象
            QueryWrapper<DmTableSync> queryWrapper = new QueryWrapper<DmTableSync>().eq("schema_name", schemaName).in("table_name", tableNameList);
            List<DmTableSync> existsTableSyncList = list(queryWrapper);

            // 将已存在的同步配置按表名映射到一个Map中，方便后续查找
            Map<String, DmTableSync> tableSyncMap = existsTableSyncList.stream().collect(Collectors.toMap(dmTable -> dmTable.getTableName(), Function.identity()));

            //2.2 准备模型数据表列表，map,key为表名，value为DmTable对象
            List<DmTable> dmTableList = dmTableService.list(new QueryWrapper<DmTable>().eq("is_deleted", "0").eq("schema_name", schemaName));

            // 将数据管理表中的表信息按表名映射到一个Map中，方便后续查找表ID
            Map<String, DmTable> tableMap = dmTableList.stream().collect(Collectors.toMap(dmTable -> dmTable.getTableName(), Function.identity()));

            //2.3 准备数仓模型列表，map,key为数据库名，value为DpDataWarehouseModel对象
            List<DpDataWarehouseModel> modelList = dpDataWarehouseModelService.list(new QueryWrapper<DpDataWarehouseModel>().eq("is_deleted", "0"));
            Map<String, DpDataWarehouseModel> modelMap = modelList.stream().collect(Collectors.toMap(DpDataWarehouseModel::getSchemaName, Function.identity()));


            //3 循环 hive数据表列表
            //     *    有数据同步 信息则补充，没有则模型数据表列表，进行补充 再没有查数仓模型补充数仓模型信息
            //     *    最终装配为 DmTableSync对象列表
            // 初始化一个列表用于存储需要同步但尚未配置的表的同步信息
            List<DmTableSync> toSyncTableList = new ArrayList<>();
            //
            // 遍历Hive中的所有表名
            for (String tableName : tableNameList) {
                // 如果表已存在同步配置，则将其添加到待返回的列表中
                DmTableSync existsDmTableSync = tableSyncMap.get(tableName);
                if (existsDmTableSync != null) {
                    toSyncTableList.add(existsDmTableSync);
                } else { // 如果表不存在同步配置，则根据表名查找表ID，创建新的同步配置并添加到待返回的列表中
                    DmTableSync dmTableSync = new DmTableSync();
                    DmTable dmTable = tableMap.get(tableName);
                    if(dmTable!=null){
                        dmTableSync.setTableId(dmTable.getId());
                        dmTableSync.setModelId(dmTable.getModelId());
                    }
                    dmTableSync.setTableName(tableName);
                    dmTableSync.setSchemaName(schemaName);
                    // 根据模式名从数据仓库模型表中获取模型ID
                    if(dmTableSync.getModelId()==null){
                        DpDataWarehouseModel model = modelMap.get(schemaName);
                        if(model!=null){
                            dmTableSync.setModelId(model.getId());
                        }
                    }
                    toSyncTableList.add(dmTableSync);
                }
            }
            return toSyncTableList;
        } catch (Exception e) {
            throw new RuntimeException("查询hive异常", e);
        }
    }

    /**
     * 同步数据仓库表元数据。
     * 本方法通过解析Hive表的元数据信息，并匹配相应的数据模型，将这些信息同步到数据仓库的表管理模块中。
     *
     * @param dmTableSyncList 待同步的Hive表元数据列表。
     * @throws RuntimeException 如果同步过程中发生异常，则抛出运行时异常。
     */
    @Override
    public void syncTableMeta(List<DmTableSync> dmTableSyncList) {
        // 遍历待同步的Hive表元数据列表
        for (DmTableSync dmTableSync : dmTableSyncList) {
            try {
                // 根据Hive表元数据生成DmTable对象
                DmTable dmTable = tableHiveProcessor.generateDmTableFromHive(dmTableSync);
                // 设置表的状态为待处理，并保存到数据库
                dmTable.setTableStatus(CommonCodes.TABLE_STATUS_TO_ADD);
                dmTableService.saveTableAll(dmTable );
                // 更新同步信息，包括表ID、最后一次元数据同步时间和用户ID
                dmTableSync.setTableId(dmTable.getId());
                dmTableSync.setLastSyncMetaTime(new Date());
                dmTableSync.setLastSyncMetaUserId(9999L);

                saveOrUpdate(dmTableSync);

            } catch (Exception e) {
                // 如果同步过程中发生异常，则抛出运行时异常，并包含原始异常信息
                throw new RuntimeException("同步hive异常", e);
            }
        }
    }

    //每个表去同步数据
    @Override
    public void syncTableDataInfo(List<DmTableSync> dmTableSyncList) {
        for (DmTableSync dmTableSync : dmTableSyncList) {
            syncDataInfo( dmTableSync);
        }
    }


    @Autowired
    HiveMapper hiveMapper;

    @Autowired
    TableHdfsProcessor tableHdfsProcessor;

    /**
     * 同步数据信息方法。
     * 该方法用于根据传入的dmTableSync对象，同步数据库和Hive、HDFS中的表数据信息。
     * 如果表数据信息不存在，则创建新的表数据信息；否则，更新现有的表数据信息。
     * @param dmTableSync 同步任务对象，包含表的ID、名称和模式名等信息。
     */
    @Transactional
    public void syncDataInfo(DmTableSync dmTableSync) {
        // 根据表ID查询已存在的表数据信息
        DmTableDataInfo tableDataInfo = dmTableDataInfoService.getOne(new QueryWrapper<DmTableDataInfo>().eq("table_id", dmTableSync.getTableId()));

        // 如果表数据信息不存在，则创建新的表数据信息
        if(tableDataInfo == null) {
            tableDataInfo = new DmTableDataInfo();
            tableDataInfo.setTableId(dmTableSync.getTableId());
            tableDataInfo.setTableName(dmTableSync.getTableName());
            tableDataInfo.setSchemaName(dmTableSync.getSchemaName());
        }

        // 强行要求hive对某个进行统计
        hiveMapper.anlyzeTable(dmTableSync.getSchemaName(), dmTableSync.getTableName());

        // 从Hive中提取表的数据信息
        tableHiveProcessor.extractDataInfoFromHive(tableDataInfo);
        // 从HDFS中提取表的数据信息
        tableHdfsProcessor.extractDataInfoFromHdfs(tableDataInfo);

        // 更新表数据信息的最后一次同步时间，并保存或更新表数据信息
        tableDataInfo.setLastSyncInfoTime(new Date());
        dmTableDataInfoService.saveOrUpdate(tableDataInfo);

        // 更新同步任务的最后一次同步时间，并保存或更新同步任务信息
        dmTableSync.setLastSyncInfoTime(new Date());
        saveOrUpdate(dmTableSync);
    }

    public void syncDataInfoForScheduler() {
        QueryWrapper<DmTableSync> queryWrapper = new QueryWrapper<DmTableSync>().eq("is_sync_info", "1");
        List<DmTableSync> dmTableSyncList = list(queryWrapper);
        for (DmTableSync dmTableSync : dmTableSyncList) {
            syncDataInfo( dmTableSync);
        }
    }

}
