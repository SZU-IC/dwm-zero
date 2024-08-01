package com.wither.dwm.model.service;

import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.model.bean.DmDimension;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 维度 服务类
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
public interface DmDimensionService extends IService<DmDimension> {
    public List getListForQuery(QueryInfo queryInfo);

    public Integer getTotalForQuery(QueryInfo queryInfo );

}
