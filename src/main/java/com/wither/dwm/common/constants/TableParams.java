package com.wither.dwm.common.constants;

/**
 * @Classname TableParams
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/8/1 14:22
 * @Created by 97952
 */
public class TableParams {
    // hive的输入输出格式
    public static final String TEXT_INPUT_FORMAT = "org.apache.hadoop.mapred.TextInputFormat";
    public static final String TEXT_OUTPUT_FORMAT = "org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat";

    public static final String ORC_INPUT_FORMAT = "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat";
    public static final String ORC_OUTPUT_FORMAT = "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat";
    public static final String PARQUET_INPUT_FORMAT = "org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat";
    public static final String PARQUET_OUTPUT_FORMAT = "org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat";



    //序列化Java类
    public static final String SERDE_CLASS_TEXT = "org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe";
    public static final String SERDE_CLASS_ORC = "org.apache.hadoop.hive.ql.io.orc.OrcSerde";

    public static final String SERDE_CLASS_PARQUET = "org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe";
    public static final String SERDE_CLASS_JSON = "org.apache.hadoop.hive.serde2.JsonSerDe";


    //产生压缩Java类
    public static final String COMPRESS_TYPE_SNAPPY = "org.apache.hadoop.io.compress.SnappyCodec";
    public static final String COMPRESS_TYPE_GZIP = "org.apache.hadoop.io.compress.GzipCodec";
    public static final String COMPRESS_TYPE_LZO = "com.hadoop.compression.lzo.LzopCodec";
}
