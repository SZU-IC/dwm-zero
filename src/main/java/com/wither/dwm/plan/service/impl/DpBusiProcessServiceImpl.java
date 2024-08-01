package com.wither.dwm.plan.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.plan.bean.DpBusiProcess;
import com.wither.dwm.plan.mapper.DpBusiProcessMapper;
import com.wither.dwm.plan.service.DpBusiProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业务过程 服务实现类
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@DS("dwm")
@Service
public class DpBusiProcessServiceImpl extends ServiceImpl<DpBusiProcessMapper, DpBusiProcess> implements DpBusiProcessService {
    //查询列表
    public List getListForQuery(QueryInfo queryInfo){
        String modelCondition= "";
        if(queryInfo.getModelId() !=null){
            modelCondition =" and t.model_id="+queryInfo.getModelId();
        }
        String limit = queryInfo.getLimitSQL();
        List  list=baseMapper.selectListForQuery(modelCondition,limit);
        return list;
    }

    //计算总数
    public Integer getCountForQuery(QueryInfo queryInfo ){
        String modelCondition= "";
        if(queryInfo.getModelId() !=null){
            modelCondition =" and t.model_id="+queryInfo.getModelId();
        }
        Integer total=  baseMapper.selectCountForQuery(modelCondition);
        return total;
    }

}
