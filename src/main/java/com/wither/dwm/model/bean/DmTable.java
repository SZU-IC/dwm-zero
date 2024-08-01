package com.wither.dwm.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wither.dwm.common.utils.ArrayStringHandler;
import lombok.Data;

/**
 * <p>
 * 数据表
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Data
@TableName("dm_table")
public class DmTable implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模型id
     */
    private Long modelId;

    /**
     * 数据域id
     */
    private Long domainId;

    /**
     * 业务过程id
     */
    private Long busiProcessId;

    /**
     * 维度id
     */
    private Long dimId;

    /**
     * 统计粒度_维度id
     */
    @TableField(typeHandler = ArrayStringHandler.class  )
    private Long[] statDimIds;

    /**
     * 统计周期
     */
    private String statPeriod;

    /**
     * 数仓层级
     */
    private String dwLevel;

    /**
     * 存储策略
     */
    private String storageMode;

    /**
     * 数据表名
     */
    private String tableName;

    /**
     * 数据表中文名
     */
    private String tableNameChn;

    /**
     * schema表名
     */
    private String schemaName;

    /**
     * 规则编码
     */
    private Long ruleId;

    /**
     * 生命周期(天)
     */
    private Long lifecycleDays;

    /**
     * 安全级别
     */
    private String securityLevel;

    /**
     * 技术负责人
     */
    private String tecOwner;

    /**
     * 业务负责人
     */
    private String busiOwner;

    /**
     * 状态
     */
    private String tableStatus;

    /**
     * 存储格式
     */
    private String storageFormat;

    /**
     * 压缩类型
     */
    private String compressType;

    /**
     * 空值替换
     */
    private String nullDefined;

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

        /**
         * 数仓模型名称
         */
        @TableField(exist = false)
        String modelName;

        /**
         * 表的所有列信息，不映射到数据库表字段。
         */
        @TableField(exist = false)
        List<DmTableColumn> tableColumns;

        /**
         * 表的分区列信息，不映射到数据库表字段。
         */
        @TableField(exist = false)
        List<DmTableColumn> partitionColumns;

        /**
         * 表的状态名称，不映射到数据库表字段。
         */
        @TableField(exist = false)
        String tableStatusName;

        /**
         * 表的行数，不映射到数据库表字段。
         */
        @TableField(exist = false)
        Long numRows;
}
