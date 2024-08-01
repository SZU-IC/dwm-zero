package com.wither.dwm.model.service;

import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.model.bean.DmMetric;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 指标 服务类
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
public interface DmMetricService extends IService<DmMetric> {
    public List getListForQuery(QueryInfo queryInfo);

    public Integer getTotalForQuery(QueryInfo queryInfo );

    public List<Map> getLinkTableList(DmMetric  dmMetric) ;

    public List getMetricListForLink(Long tableId);

}
