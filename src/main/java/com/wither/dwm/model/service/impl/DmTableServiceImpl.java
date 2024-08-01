package com.wither.dwm.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.common.component.TableHiveProcessor;
import com.wither.dwm.common.constants.CommonCodes;
import com.wither.dwm.common.utils.SqlUtil;
import com.wither.dwm.model.bean.DmTable;
import com.wither.dwm.model.bean.DmTableColumn;
import com.wither.dwm.model.mapper.DmTableMapper;
import com.wither.dwm.model.service.DmTableColumnService;
import com.wither.dwm.model.service.DmTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据表 服务实现类
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Service
public class DmTableServiceImpl extends ServiceImpl<DmTableMapper, DmTable> implements DmTableService {
    @Autowired
    private DmTableColumnService dmTableColumnService;
    @Override
    public void saveTableAll(DmTable dmTable) {
        //保存主表
        saveOrUpdate(dmTable);
        //保存字段表
        List<DmTableColumn> dmTableColumnList = dmTable.getTableColumns();
        int seq=1;  //字段排序
        for (DmTableColumn dmTableColumn : dmTableColumnList) {
            dmTableColumn.setTableId(dmTable.getId());
            dmTableColumn.setSeq(seq++);
            dmTableColumn.setLastUpdateTime(dmTable.getLastUpdateTime() );
            dmTableColumn.setLastUpdateUserId(dmTable.getLastUpdateUserId());
            dmTableColumn.setLastUpdateUserId(dmTable.getLastUpdateUserId());
            dmTableColumn.setIsPartitionCol("0");
        }
        dmTableColumnService.saveOrUpdateBatch(dmTableColumnList);
        //保存分区字段表
        seq=1; //字段排序
        List<DmTableColumn> partitionColumnList = dmTable.getPartitionColumns();
        for (DmTableColumn partitionColumn : partitionColumnList) {
            partitionColumn.setTableId(dmTable.getId());
            partitionColumn.setSeq(seq++);
            partitionColumn.setLastUpdateTime(dmTable.getLastUpdateTime() );
            partitionColumn.setLastUpdateUserId(dmTable.getLastUpdateUserId());
            partitionColumn.setLastUpdateUserId(dmTable.getLastUpdateUserId());
            partitionColumn.setIsPartitionCol("1");
        }
        dmTableColumnService.saveOrUpdateBatch(partitionColumnList);
        //以前有但是现在不存在的字段
        List<Long> dmTableColumnIds = dmTableColumnList.stream().map(dmTableColumn -> dmTableColumn.getId()).collect(Collectors.toList());
        List<Long> dmTablePartitionColumnIds = partitionColumnList.stream().map(dmTableColumn -> dmTableColumn.getId()).collect(Collectors.toList());
        dmTableColumnIds.addAll(dmTablePartitionColumnIds);
        UpdateWrapper<DmTableColumn> updateWrapper = new UpdateWrapper<DmTableColumn>().set("is_deleted", "1").eq("table_id", dmTable.getId()).notIn("id", dmTableColumnIds);
        dmTableColumnService.update(updateWrapper);
    }

    public List getListForQuery(  QueryInfo queryInfo){
        String condition= "";
        if(queryInfo.getModelId() !=null){
            condition +=" and t.model_id="+queryInfo.getModelId();
        }
        if(queryInfo.getTableNameQuery() !=null){
            condition +=" and (t.table_name like '%"+ SqlUtil.filterUnsafeSql( queryInfo.getTableNameQuery())+"%' or t.table_name_chn like '%"+SqlUtil.filterUnsafeSql( queryInfo.getTableNameQuery())+"%' )";
        }
        String limit = queryInfo.getLimitSQL();
        List  list=baseMapper.selectListForQuery(condition,limit);
        return list;
    }


    public Integer getTotalForQuery(QueryInfo queryInfo){
        String condition= "";
        if(queryInfo.getModelId() !=null){
            condition +=" and t.model_id="+queryInfo.getModelId();
        }
        if(queryInfo.getTableNameQuery() !=null){
            condition +=" and (t.table_name like '%"+ SqlUtil.filterUnsafeSql( queryInfo.getTableNameQuery())+"%' or t.table_name_chn like '%"+SqlUtil.filterUnsafeSql( queryInfo.getTableNameQuery())+"%' )";
        }
        Integer total=  baseMapper.selectTotalForQuery(condition);
        return total;
    }

    public DmTable getTableAll(Long tableId)     {
        DmTable dmTable = this.getById(tableId);
        dmTable.setTableColumns(dmTableColumnService.list(
                new QueryWrapper<DmTableColumn>()
                        .eq("table_id", tableId)
                        .eq("is_deleted","0")
                        .eq("is_partition_col", "0")
                        .orderByAsc("seq")
        ));
        dmTable.setPartitionColumns(dmTableColumnService.list(
                new QueryWrapper<DmTableColumn>()
                        .eq("table_id", tableId)
                        .eq("is_deleted","0")
                        .eq("is_partition_col", "1")
                        .orderByAsc("seq")));
        return dmTable;
    }

    @Autowired
    private TableHiveProcessor tableHiveProcessor;
    @Override
    public void submitToHive(Long tableId)     {
        DmTable dmTable = getById(tableId);
        //先做一次保存
        tableHiveProcessor.submitToHive(dmTable);
        dmTable.setLastUpdateTime(new Date());
        dmTable.setLastUpdateUserId(9999L);
        dmTable.setTableStatus(CommonCodes.TABLE_STATUS_PUBLISHED);
        saveOrUpdate(dmTable);
    }
}
