package com.wither.dwm.plan.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 数仓模型
 * </p>
 *
 * @author wither
 * @since 2024-07-31
 */
@Data
@TableName("dp_data_warehouse_model")
public class DpDataWarehouseModel implements Serializable {

private static final long serialVersionUID = 1L;

/**
 * 主键
 */
@TableId(value = "id", type = IdType.AUTO)
private Long id;

/**
 * 数仓模型名称
 */
private String modelName;

/**
 * schema名称
 */
private String schemaName;

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
