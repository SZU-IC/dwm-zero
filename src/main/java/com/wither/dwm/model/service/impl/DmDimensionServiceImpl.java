package com.wither.dwm.model.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.model.bean.DmDimension;
import com.wither.dwm.model.mapper.DmDimensionMapper;
import com.wither.dwm.model.service.DmDimensionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 维度 服务实现类
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@DS("dwm")
@Service
public class DmDimensionServiceImpl extends ServiceImpl<DmDimensionMapper, DmDimension> implements DmDimensionService {
    public List getListForQuery(QueryInfo queryInfo){
        String condition= "";
        if(queryInfo.getModelId() !=null){
            condition +=" and t.model_id="+queryInfo.getModelId();
        }

        String limit = queryInfo.getLimitSQL();
        List  list=baseMapper.selectListForQuery(condition,limit);
        return list;
    }



    public Integer getTotalForQuery(QueryInfo queryInfo){
        String condition= "";
        if(queryInfo.getModelId() !=null){
            condition +=" and t.model_id="+queryInfo.getModelId();
        }
        Integer total=  baseMapper.selectTotalForQuery(condition);
        return total;
    }

}
