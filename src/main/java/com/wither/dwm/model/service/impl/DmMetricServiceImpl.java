package com.wither.dwm.model.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.common.constants.CommonCodes;
import com.wither.dwm.common.utils.SqlUtil;
import com.wither.dwm.model.bean.DmMetric;
import com.wither.dwm.model.mapper.DmMetricMapper;
import com.wither.dwm.model.service.DmMetricService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 指标 服务实现类
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Service
public class DmMetricServiceImpl extends ServiceImpl<DmMetricMapper, DmMetric> implements DmMetricService {

    public List getListForQuery(QueryInfo queryInfo){
        String condition= "";
        if(queryInfo.getModelId() !=null){
            condition +=" and t.model_id="+queryInfo.getModelId();
        }
        if(queryInfo.getMetricName() !=null){
            condition +=" and t.metric_name like '%"+ SqlUtil.filterUnsafeSql(queryInfo.getMetricName())+"%'";
        }

        String limit = queryInfo.getLimitSQL();
        List  list=baseMapper.selectListForQuery(condition,limit);
        return list;
    }



    public Integer getTotalForQuery(QueryInfo queryInfo) {
        String condition = "";
        if (queryInfo.getModelId() != null) {
            condition += " and t.model_id=" + queryInfo.getModelId();
        }
        Integer total = baseMapper.selectCountForQuery(condition);
        return total;
    }

    public List<Map> getLinkTableList(DmMetric dmMetric) {
        String condition=null;
        if(dmMetric.getMetricType().equals(CommonCodes.METRIC_TYPE_ATOMIC)){
            condition = " where  FIND_IN_SET(  "+dmMetric.getId()+", m.link_atomic_metric_ids) ";
        }else{
            condition = "  where  m.id=  "+dmMetric.getId();
        }
        List<Map> list = baseMapper.selectLinkTableList(condition);
        return list;
    }

    public List getMetricListForLink(Long tableId) {
        List list = baseMapper.selectMetricListForLink(tableId);
        return list;
    }

}
