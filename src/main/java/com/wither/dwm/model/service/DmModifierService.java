package com.wither.dwm.model.service;

import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.model.bean.DmModifier;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 限定词表 服务类
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
public interface DmModifierService extends IService<DmModifier> {
    public List getListForQuery(QueryInfo queryInfo);

    public Integer getTotalForQuery(QueryInfo queryInfo );
}
