package com.wither.dwm.common.constants;

/**
 * @Classname CommonCodes
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/8/1 14:23
 * @Created by 97952
 */
public class CommonCodes {

    // 分层名称
    public static final String DW_LEVEL_ODS = "0101";   // ODS层
    public static final String DW_LEVEL_DWD = "0102";   // DWD层
    public static final String DW_LEVEL_DIM = "0103";   // DIM层
    public static final String DW_LEVEL_DWS = "0104";   // DWS层
    public static final String DW_LEVEL_ADS = "0105";   // ADS层

    // 规则类型
    public static final String RULE_TYPE_TABLE_NAME = "0201";   // 表名规则
    public static final String RULE_TYPE_METRIC_NAME = "0202";   // 指标名规则

    // 安全级别
    public static final String SECURITY_LEVEL_PUBLIC = "0301";   // 公开
    public static final String SECURITY_LEVEL_INTERNAL = "0302";   // 内部
    public static final String SECURITY_LEVEL_SECRET = "0303";   // 保密
    public static final String SECURITY_LEVEL_TOP_SECRET = "0304";   // 高度机密

    // 存储策略
    public static final String STORAGE_MODE_FD = "0401";   // 每日全量
    public static final String STORAGE_MODE_INC = "0402";   // 每日增量
    public static final String STORAGE_MODE_ZIP = "0403";   // 拉链表

    // 表状态
    public static final String TABLE_STATUS_EDITING = "0501";   // 编辑中
    public static final String TABLE_STATUS_PUBLISHED = "0502";   // 已发布
    public static final String TABLE_STATUS_TO_ADD = "0503";   // 待补充
    public static final String TABLE_STATUS_ADDED = "0504";   // 已经补充



    // 关联类型
    public static final String ASSOCIATION_TYPE_INDEX = "0701";   // 指标
    public static final String ASSOCIATION_TYPE_DIMENSION = "0702";   // 维度
    public static final String ASSOCIATION_TYPE_TIME = "0703";   // 时间

    // 命名选项
    public static final String NAMING_OPTION_DOMAIN = "0802";   // 数据域
    public static final String NAMING_OPTION_BUSINESS_PROCESS = "0804";   // 业务过程
    public static final String NAMING_OPTION_STORAGE_STRATEGY = "0806";   // 存储策略
    public static final String NAMING_OPTION_CUSTOM = "0808";   // 自定义
    public static final String NAMING_OPTION_ATOMIC_METRIC = "0810";   // 原子指标
    public static final String NAMING_OPTION_STATISTICAL_PERIOD = "0812";   // 统计周期
    public static final String NAMING_OPTION_GRANULARITY = "0814";   // 粒度
    public static final String NAMING_OPTION_MODIFIER = "0816";   // 修饰词

    // 统计周期
    public static final String STATISTICAL_PERIOD_1D = "0901";   // 1天
    public static final String STATISTICAL_PERIOD_ND = "0902";   // n天

    // 数据类型
    public static final String DATA_TYPE_STRING = "1001";   // 字符串string
    public static final String DATA_TYPE_DECIMAL_16_2 = "1002";   // 数字decimal(16,2)
    public static final String DATA_TYPE_DECIMAL = "1003";   // 数字decimal
    public static final String DATA_TYPE_BIGINT = "1004";   // 数字bigint

    // 存储格式
    public static final String STORAGE_FORMAT_TEXT_TAB = "1101";   // 文本格式( \t分隔)
    public static final String STORAGE_FORMAT_TEXT_JSON = "1102";   // 文本格式json
    public static final String STORAGE_FORMAT_ORC = "1103";   // orc
    public static final String STORAGE_FORMAT_PARQUET = "1104";   // parquet

    // 压缩类型
    public static final String COMPRESS_TYPE_NONE = "1201";   // 不压缩
    public static final String COMPRESS_TYPE_SNAPPY = "1202";   // snappy
    public static final String COMPRESS_TYPE_GZIP = "1203";   // gzip


    // 根据数据生成 指标类型的常量
    //    ('1301','原子指标','','13',9999,now()),
    //    ('1302','派生指标','','13',9999,now()),
    //    ('1303','衍生指标','','13',9999,now()),
    //    ('1304','时间','','13',9999,now()),
    //    ('1305','粒度','','13',9999,now());
    public static final String METRIC_TYPE_ATOMIC = "1301";
    public static final String METRIC_TYPE_DERIVED = "1302";
    public static final String METRIC_TYPE_COMPOSITE = "1303";
    public static final String METRIC_TYPE_TIME = "1304";
    public static final String METRIC_TYPE_DIM = "1305";

}
