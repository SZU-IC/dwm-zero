package com.wither.dwm.common.bean;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * <p>
 * 标准编码
 * </p>
 *
 * @author wither
 * @since 2024-07-31
 */
@Data
@TableName("common_code")
public class CommonCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    private String codeNo;

    /**
     * 编码名称
     */
    private String codeName;

    /**
     * 额外名称
     */
    private String extName;

    /**
     * 上级编码
     */
    private String parentCodeNo;

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
