package com.wither.dwm.common.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wither.dwm.common.constants.CommonCodes;
import com.wither.dwm.common.constants.TableParams;
import com.wither.dwm.common.mapper.HiveMapper;
import com.wither.dwm.model.bean.DmTable;
import com.wither.dwm.model.bean.DmTableColumn;
import com.wither.dwm.model.bean.DmTableDataInfo;
import com.wither.dwm.model.bean.DmTableSync;
import com.wither.dwm.model.service.DmTableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.*;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Classname TableHiveProcessor
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/7/31 18:36
 * @Created by 97952
 */
@Component
@Slf4j
public class TableHiveProcessor {
    @Autowired
    private DmTableService dmTableService;
    @Value("${warehouse.base-path}")
    String basePath;

    @Value("${hadoop.username}")
    String  hadoopUser;

    @Value("${metastore.url}")  //顺序1
    String metaUrl;

    IMetaStoreClient iMetaStoreClient =null;// initClient();

    @Autowired
   // HiveMapper hiveMapper;

    @PostConstruct   //顺序2
    public    void  initClient(){
        try{
            System.setProperty("HADOOP_USER_NAME",hadoopUser);
            HiveConf hiveConf=new HiveConf();
            hiveConf.setVar(HiveConf.ConfVars.METASTOREURIS,metaUrl );

            iMetaStoreClient=  new HiveMetaStoreClient(hiveConf);
        }catch (Exception e){
            // e.printStackTrace();
            log.warn("初始化hive客户端异常");
        }
    }

    public IMetaStoreClient getMetaClient(){
        return iMetaStoreClient;
    }

    public   boolean submitToHiveSchema(String schemaName){
        Database database = new Database();
        database.setName(schemaName);
        try {
            getMetaClient().createDatabase(database);
        } catch (TException e) {
            if (e.getLocalizedMessage().indexOf("exists") >= 0) {
                return false;
            }
        }
        return  true;
    }

    /**
     * 将数据模型表提交到Hive。
     * 该方法首先根据数据模型表生成Hive表的定义，然后尝试性删除已存在的同名表，最后创建新的Hive表。
     * 如果在操作过程中遇到异常，将打印异常堆栈跟踪并抛出一个运行时异常，指示提交Hive表过程中发生异常。
     *
     * @param dmTable 数据模型表，用于生成Hive表的定义。
     */
    public void submitToHive(DmTable dmTable) {
        try {
            // 根据数据模型表生成Hive表定义
            Table table = generateHiveTable(dmTable);

            // 删除已存在的同名表
            iMetaStoreClient.dropTable(table.getDbName(), table.getTableName());

            // 创建新的Hive表
            iMetaStoreClient.createTable(table);
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();

            // 抛出运行时异常，指示提交Hive表异常
            throw new RuntimeException("提交hive表异常");
        }
    }


//生成hive表对象

    public Table generateHiveTable(DmTable  dmTable){

        // 1 创建hive元数据对象
        Table table = new Table();

        // 2 根据dmTable中的信息创建hive的表对象table
        table.setDbName(dmTable.getSchemaName());
        table.setTableName(dmTable.getTableName());
        table.setTableType("EXTERNAL_TABLE");

        StorageDescriptor storageDescriptor = new StorageDescriptor();
        storageDescriptor.setLocation(basePath+"/"+dmTable.getSchemaName()+"/"+dmTable.getTableName());
        //依据dmtable中的存储格式创建存储描述符
        storageDescriptor.setInputFormat(getInputFormat(dmTable.getStorageFormat()));

        storageDescriptor.setOutputFormat(getOutputFormat(dmTable.getStorageFormat()));
        storageDescriptor.setCols(getFieldSchemaList(dmTable.getTableColumns()));

        storageDescriptor.setSerdeInfo(getSerdeInfo(dmTable));

        table.setSd(storageDescriptor);

        //依据dmTable中的压缩类型设置表的压缩属性
        String compressionCodec = getCompressionCodec(dmTable.getCompressType());
        if(compressionCodec!=null){
            table.putToParameters("compression.codec",compressionCodec);
        }

        //设置表的分区键
        if(dmTable.getPartitionColumns()!=null && dmTable.getPartitionColumns().size()>0){
            table.setPartitionKeys(getPartitionFieldSchemaList(dmTable.getPartitionColumns()));
        }

        //设置表的注释
        table.putToParameters("comment",dmTable.getTableNameChn());

        return table;

    }


    /**
     * 根据存储格式获取输入格式参数。
     *
     * 此函数根据给定的存储格式返回相应的输入格式参数。支持的存储格式包括PARQUET、TEXT(JSON)和ORC。
     * 对于不同的存储格式，系统预定义了相应的输入格式参数。
     *
     * @param storageFormat 存储格式代码，用于确定输入格式。取值来自CommonCodes类中定义的存储格式常量。
     * @return 对应于给定存储格式的输入格式参数。如果存储格式不受支持，则返回null。
     */
    private String getInputFormat(String storageFormat){
        //利用switch 映射storageFormat的值为  TableParams中的INPUT_FORMAT
        switch (storageFormat){
            case CommonCodes.STORAGE_FORMAT_PARQUET:
                return TableParams.PARQUET_INPUT_FORMAT;
            case CommonCodes.STORAGE_FORMAT_TEXT_JSON:
                return TableParams.TEXT_INPUT_FORMAT;
            case CommonCodes.STORAGE_FORMAT_TEXT_TAB:
                return TableParams.TEXT_INPUT_FORMAT;
            case CommonCodes.STORAGE_FORMAT_ORC:
                return TableParams.ORC_INPUT_FORMAT;
        }
        return null;
    }

    /**
     * 根据存储格式获取相应的输出格式参数值。
     *
     * 此函数根据输入的存储格式字符串，返回对应的数据输出格式参数值。支持的存储格式包括PARQUET、TEXT(JSON)和TEXT(TAB)、ORC。
     * 对于不支持的存储格式，函数将返回null。
     *
     * @param storageFormat 存储格式的字符串表示。取值来自CommonCodes类中定义的常量。
     * @return 对应的输出格式参数值字符串。如果输入的存储格式不受支持，则返回null。
     */
    private String getOutputFormat(String storageFormat){
        switch (storageFormat){
            case CommonCodes.STORAGE_FORMAT_PARQUET:
                return TableParams.PARQUET_OUTPUT_FORMAT;
            case CommonCodes.STORAGE_FORMAT_TEXT_JSON:
                return TableParams.TEXT_OUTPUT_FORMAT;
            case CommonCodes.STORAGE_FORMAT_TEXT_TAB:
                return TableParams.TEXT_OUTPUT_FORMAT;
            case CommonCodes.STORAGE_FORMAT_ORC:
                return TableParams.ORC_OUTPUT_FORMAT;
        }
        return null;
    }

    //把dmTable中的tableColumns提取到List<FieldSchema>中
    private List<FieldSchema> getFieldSchemaList(List<DmTableColumn> dmTableColumns){
        List<FieldSchema> fieldSchemaList = new ArrayList<>();
        for (DmTableColumn dmTableColumn : dmTableColumns) {
            FieldSchema fieldSchema = new FieldSchema();
            fieldSchema.setName(dmTableColumn.getColumnName());
            fieldSchema.setType(getHiveDataType(dmTableColumn.getDataType()));
            fieldSchema.setComment(dmTableColumn.getColumnComment());
            fieldSchemaList.add(fieldSchema);
        }
        return fieldSchemaList;
    }

    //根据storageFormat 创建serdeInfo
    private SerDeInfo getSerdeInfo(DmTable dmTable) {
        SerDeInfo serDeInfo = new SerDeInfo();
        serDeInfo.setParameters(new HashMap<>());
        switch (dmTable.getStorageFormat()) {
            case CommonCodes.STORAGE_FORMAT_PARQUET:
                serDeInfo.setSerializationLib(TableParams.SERDE_CLASS_PARQUET);
                break;
            case CommonCodes.STORAGE_FORMAT_TEXT_JSON:
                serDeInfo.setSerializationLib(TableParams.SERDE_CLASS_JSON);
                break;
            case CommonCodes.STORAGE_FORMAT_TEXT_TAB:
                serDeInfo.setSerializationLib(TableParams.SERDE_CLASS_TEXT);
                serDeInfo.getParameters().put("field.delim", "\t");//输入
                serDeInfo.getParameters().put("serialization.format", "\t");//输出
                break;
            case CommonCodes.STORAGE_FORMAT_ORC:
                serDeInfo.setSerializationLib(TableParams.SERDE_CLASS_ORC);
                break;
        }
        //根据dmTable中的null对serde进行赋值
        if(dmTable.getNullDefined()!=null&&dmTable.getNullDefined().trim().length()>0){
            serDeInfo.getParameters().put("serialization.null.format", dmTable.getNullDefined());
        }
        return  serDeInfo;
    }

    /**
     * 根据压缩类型获取对应的压缩编码。
     *
     * 此函数根据输入的压缩类型字符串，返回相应的压缩编码字符串。
     * 支持的压缩类型包括GZIP和SNAPPY，如果不进行压缩，则返回null。
     *
     * @param compressionType 压缩类型的字符串表示，取值来自CommonCodes类。
     * @return 对应的压缩编码字符串，如果不需要压缩则返回null。
     */
    private String getCompressionCodec(String compressionType){
        switch (compressionType){
            case CommonCodes.COMPRESS_TYPE_GZIP:
                // 对应GZIP压缩类型的压缩编码
                return TableParams.COMPRESS_TYPE_GZIP;
            case CommonCodes.COMPRESS_TYPE_SNAPPY:
                // 对应SNAPPY压缩类型的压缩编码
                return TableParams.COMPRESS_TYPE_SNAPPY;
            case CommonCodes.COMPRESS_TYPE_NONE:
                // 不进行压缩时，返回null
                return null;
        }
        // 未识别的压缩类型，返回null
        return null;
    }

    //把dmTable中的partitionColumns提取到中 List<FieldSchema>
    private List<FieldSchema> getPartitionFieldSchemaList(List<DmTableColumn> partitionColumns){
        List<FieldSchema> fieldSchemaList = new ArrayList<>();
        for (DmTableColumn dmTableColumn : partitionColumns) {
            FieldSchema fieldSchema = new FieldSchema();
            fieldSchema.setName(dmTableColumn.getColumnName());
            fieldSchema.setType(getHiveDataType(dmTableColumn.getDataType()));
            fieldSchemaList.add(fieldSchema);
        }
        return fieldSchemaList;
    }


    //根据dmTableColumn中的dataType 生成hive字段的数据类型
    public static String getHiveDataType(String dataType){
        switch (dataType){
            case CommonCodes.DATA_TYPE_STRING:
                return "string";
            case CommonCodes.DATA_TYPE_DECIMAL:
                return "decimall";
            case CommonCodes.DATA_TYPE_DECIMAL_16_2:
                return "decimal(16,2)";
            case CommonCodes.DATA_TYPE_BIGINT:
                return "bigint";

        }
        return  "string";
    }

    /******************************************************************************************************************************************************
     ******************************************************************************************************************************************************
     * 根据dmTableSync对象从Hive中获取表的元数据并转换成DmTable对象
     * @param dmTableSync 包含要查询的表名和schema的同步信息对象
     * @return 转换后的DmTable对象
     * @throws Exception 如果从Hive获取信息时出现异常
     ******************************************************************************************************************************************************
     ****************************************************************************************************************************************************** */
    public DmTable generateDmTableFromHive(DmTableSync dmTableSync) throws Exception {
        Table hiveTable;
        try {
            hiveTable = iMetaStoreClient.getTable(dmTableSync.getSchemaName(), dmTableSync.getTableName());
        } catch (Exception e) {
            log.error("无法从Hive获取表元数据", e);
            throw new RuntimeException("获取Hive表元数据失败", e);
        }
        //根据id获取DmTable对象
        List<DmTable> dmTableList = dmTableService.list(new QueryWrapper<DmTable>().eq("is_deleted", "0"));
        Map<Long, DmTable> dmTableMap = dmTableList.stream().collect(Collectors.toMap(DmTable::getId, Function.identity()));
        DmTable dmTable  = dmTableMap.get(dmTableSync.getTableId());
        //如果没有找到对应的DmTable对象，则创建一个新对象
        if(dmTable==null){
            dmTable = new DmTable();
        }
        dmTable.setId(dmTableSync.getTableId());
        dmTable.setModelId(dmTableSync.getModelId());
        dmTable.setTableName(hiveTable.getTableName());
        dmTable.setSchemaName(hiveTable.getDbName());
        dmTable.setTableNameChn(hiveTable.getParameters().get("comment"));
        dmTable.setStorageFormat(parseStorageFormat(hiveTable.getSd()));
        dmTable.setCompressType(parseCompressType(hiveTable.getParameters().get("compression.codec")));
        dmTable.setDwLevel(getDwLevelByTableNamePrefix(hiveTable.getTableName()));
        dmTable.setNullDefined(hiveTable.getSd().getParameters().get("serialization.null.format"));

        // 获取字段信息
        List<DmTableColumn> columns = hiveTable.getSd().getCols().stream().map(fs -> {
            DmTableColumn column = new DmTableColumn();
            column.setColumnName(fs.getName());
            column.setDataType(mapHiveTypeToCommonCode(fs.getType()));
            column.setColumnComment(fs.getComment());
            return column;
        }).collect(Collectors.toList());
        dmTable.setTableColumns(columns);

        // 获取分区信息
        List<DmTableColumn> partitionColumns = hiveTable.getPartitionKeys().stream().map(pk -> {
            DmTableColumn column = new DmTableColumn();
            column.setColumnName(pk.getName());
            column.setDataType(mapHiveTypeToCommonCode(pk.getType()));
            column.setIsPartitionCol("true");
            return column;
        }).collect(Collectors.toList());
        dmTable.setPartitionColumns(partitionColumns);

        return dmTable;
    }

    // 辅助方法: 解析存储格式
    private String parseStorageFormat(StorageDescriptor sd) {
        if (sd.getInputFormat().equals(TableParams.PARQUET_INPUT_FORMAT)){
            return CommonCodes.STORAGE_FORMAT_PARQUET;
        } else if (sd.getInputFormat().equals(TableParams.ORC_INPUT_FORMAT)){
            return CommonCodes.STORAGE_FORMAT_ORC;
        }  else if (sd.getInputFormat().equals(TableParams.TEXT_INPUT_FORMAT)){
            if (sd.getSerdeInfo().getSerializationLib().equals(TableParams.SERDE_CLASS_JSON)){
                return CommonCodes.STORAGE_FORMAT_TEXT_JSON;
            } else {
                return CommonCodes.STORAGE_FORMAT_TEXT_TAB;
            }
        }
        return null;
    }

    // 辅助方法: 解析压缩类型
    private String parseCompressType(String codec) {
        if (codec == null) return CommonCodes.COMPRESS_TYPE_NONE;
        if (codec.contains("snappy")) return CommonCodes.COMPRESS_TYPE_SNAPPY;
        else if (codec.contains("gzip")) return CommonCodes.COMPRESS_TYPE_GZIP;
        return CommonCodes.COMPRESS_TYPE_NONE;
    }

    // 辅助方法: 将Hive数据类型映射为通用代码
    private String mapHiveTypeToCommonCode(String hiveType) {
        switch (hiveType.toLowerCase()) {
            case "string":
                return CommonCodes.DATA_TYPE_STRING;
            case "decimal":
                return CommonCodes.DATA_TYPE_DECIMAL;
            case "decimal(16,2)":
                return CommonCodes.DATA_TYPE_DECIMAL_16_2;
            case "bigint":
                return CommonCodes.DATA_TYPE_BIGINT;
            default:
                return "string"; // 默认返回string
        }
    }

    //根据表名前缀获得 分层
    public String getDwLevelByTableNamePrefix(String tableName){
        if (tableName.startsWith("ods_")){
            return CommonCodes.DW_LEVEL_ODS;
        } else if (tableName.startsWith("dws_")){
            return CommonCodes.DW_LEVEL_DWS;
        } else if (tableName.startsWith("dwd_")){
            return CommonCodes.DW_LEVEL_DWD;
        } else if(tableName.startsWith("ads_")){
            return CommonCodes.DW_LEVEL_ADS;
        } else if(tableName.startsWith("dim_")){
            return CommonCodes.DW_LEVEL_DIM;
        }
        return null;
    }


    @Autowired
    HiveMapper hiveMapper;
    /**
     * 从Hive中提取表的数据信息。
     * 此方法通过与Hive的元数据服务交互，获取指定表的详细描述信息，包括行数、分区数、文件数、数据大小等。
     * 并根据这些信息计算出压缩比和平均文件大小，更新到DmTableDataInfo对象中。
     *
     * @param dmTableDataInfo 包含表名和模式名的实体类，用于存储从Hive获取的数据信息。
     */
    public void extractDataInfoFromHive(DmTableDataInfo dmTableDataInfo) {


        // 通过HiveMapper获取表的描述信息列表
        List<Map> descList = hiveMapper.getTableDesc(dmTableDataInfo.getSchemaName(), dmTableDataInfo.getTableName());

        // 遍历描述信息列表，提取并设置表的统计信息
        for (Map map : descList) {
            if (map.get("data_type") == null) {
                continue;
            }
            String dataType = map.get("data_type").toString().trim();
            if (dataType.equals("numRows")) {
                dmTableDataInfo.setNumRows(Long.parseLong(map.get("comment").toString().trim()));
            } else if (dataType.equals("numPartitions")) {
                dmTableDataInfo.setNumPartitions(Long.parseLong(map.get("comment").toString().trim()));
            } else if (dataType.equals("numFiles")) {
                dmTableDataInfo.setNumFiles(Long.parseLong(map.get("comment").toString().trim()));
            } else if (dataType.equals("rawDataSize")) {
                dmTableDataInfo.setRawDataSize(Long.parseLong(map.get("comment").toString().trim()));
            } else if (dataType.equals("totalSize")) {
                dmTableDataInfo.setDataSize(Long.parseLong(map.get("comment").toString().trim()));
            }
        }

        // 计算并设置压缩比
        if (dmTableDataInfo.getRawDataSize() > 0L && dmTableDataInfo.getDataSize() > 0L) {
            BigDecimal compressRatio = BigDecimal.valueOf(dmTableDataInfo.getRawDataSize()).divide(BigDecimal.valueOf(dmTableDataInfo.getDataSize()), 2, BigDecimal.ROUND_HALF_UP);
            dmTableDataInfo.setCompressRatio(compressRatio);
        }

        // 计算并设置平均文件大小
        if (dmTableDataInfo.getDataSize() > 0L && dmTableDataInfo.getNumFiles() > 0L) {
            Long fileSizeAvg = dmTableDataInfo.getDataSize() / dmTableDataInfo.getNumFiles();
            dmTableDataInfo.setFileSizeAvg(fileSizeAvg);
        }
    }
}
