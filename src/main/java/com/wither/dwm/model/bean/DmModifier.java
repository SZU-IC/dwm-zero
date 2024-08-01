package com.wither.dwm.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 限定词表
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Data
@TableName("dm_modifier")
public class DmModifier implements Serializable {

private static final long serialVersionUID = 1L;

/**
 * 主键
 */
@TableId(value = "id", type = IdType.AUTO)
private Long id;

/**
 * 中文名称
 */
private String nameChn;

/**
 * 英文名称
 */
private String nameEng;

/**
 * 模型id
 */
private Long modelId;

/**
 * 业务口径
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
