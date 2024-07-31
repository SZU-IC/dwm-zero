package com.wither.dwm.plan.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 命名规则
 * </p>
 *
 * @author wither
 * @since 2024-07-31
 */
@Data
@TableName("dp_naming_rule")
public class DpNamingRule implements Serializable {

private static final long serialVersionUID = 1L;

/**
 * 主键
 */
@TableId(value = "id", type = IdType.AUTO)
private Long id;

/**
 * 规则名称
 */
private String ruleName;

/**
 * 规则名称固定前缀
 */
private String rulePrefix;

/**
 * 规则主体(json)
 */
private String ruleBody;

/**
 * 规则描述
 */
private String ruleDesc;

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
