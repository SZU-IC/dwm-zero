package com.wither.dwm.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 字段表
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Data
@TableName("dm_table_column")
public class DmTableColumn implements Serializable {

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
 * 字段名称
 */
private String columnName;

/**
 * 字段中文名称
 */
private String columnComment;

/**
 * 是否是分区字段
 */
private String isPartitionCol;

/**
 * 数据类型
 */
private String dataType;

/**
 * 次序
 */
private Integer seq;

/**
 * 备注
 */
private String remark;

/**
 * 最后修改人id
 */
private Long lastUpdateUserId;

/**
 * 最后修改时间
 */
private Date lastUpdateTime;

/**
 * 是否删除
 */
private String isDeleted;
        }
