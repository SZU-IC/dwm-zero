package com.wither.dwm.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 数据信息表
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Data
@TableName("dm_table_data_info")
public class DmTableDataInfo implements Serializable {

private static final long serialVersionUID = 1L;

/**
 * 主键
 */
@TableId(value = "id", type = IdType.AUTO)
private Long id;

/**
 * 数据表id
 */
private Long tableId;

/**
 * 数据表名
 */
private String tableName;

/**
 * schema名
 */
private String schemaName;

/**
 * 文件数
 */
private Long numFiles;

/**
 * 行数
 */
private Long numRows;

/**
 * 分区数
 */
private Long numPartitions;

/**
 * 平均文件大小
 */
private Long fileSizeAvg;

/**
 * 压缩比
 */
private BigDecimal compressRatio;

/**
 * 未压缩数据大小(字节)
 */
private Long rawDataSize;

/**
 * 数据大小(字节)
 */
private Long dataSize;

/**
 * 数据大小(字节)含副本数
 */
private Long dataSizeAllRep;

/**
 * 最后修改时间
 */
private Date dataLastModifyTime;

/**
 * 最后访问时间
 */
private Date dataLastAccessTime;

/**
 * 最后同步数据信息时间
 */
private Date lastSyncInfoTime;
        }
