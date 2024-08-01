package com.wither.dwm.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * <p>
 * 指标
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Data
@TableName("dm_metric")
public class DmMetric implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 指标名称
     */
    private String metricName;

    /**
     * 指标类型
     */
    private String metricType;

    /**
     * 模型id
     */
    private Long modelId;

    /**
     * 数据域id
     */
    private Long domainId;

    /**
     * 数据表id
     */
    private Long linkTableId;

    /**
     * 字段id
     */
    private Long linkColumnId;

    /**
     * 粒度_维度id
     */
    private String linkDimIds;

    /**
     * 关联原子指标
     */
    private String linkAtomicMetricIds;

    /**
     * 统计周期(派生)
     */
    private String linkStatPeriod;

    /**
     * 修饰词id(派生)
     */
    private String linkModifierIds;

    /**
     * 计算口径(衍生)
     */
    private String linkCalcDesc;

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

        // 以下在指标列表展示时使用
        @TableField(exist = false)
        private String metricTypeName;

        @TableField(exist = false)
        private String modelName;

        @TableField(exist = false)
        private String domainName;
        @TableField(exist = false)
        private String columnName;
        @TableField(exist = false)
        private String columnComment;

        @TableField(exist = false)
        private String tableName;


        @TableField(exist = false)
        private List<Map> linkTableList;
}
