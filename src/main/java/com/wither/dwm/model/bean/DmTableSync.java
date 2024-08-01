package com.wither.dwm.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 数据同步信息表
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Data
@TableName("dm_table_sync")
public class DmTableSync implements Serializable {

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
 * 数仓模型id
 */
private Long modelId;

/**
 * 数据表名
 */
private String tableName;

/**
 * schema名
 */
private String schemaName;

/**
 * 是否每日同步数据信息
 */
private String isSyncInfo;

/**
 * 最后同步表结构时间
 */
private Date lastSyncMetaTime;

/**
 * 最后同步数据用户
 */
private Long lastSyncMetaUserId;

/**
 * 最后同步数据信息时间
 */
private Date lastSyncInfoTime;
        }
