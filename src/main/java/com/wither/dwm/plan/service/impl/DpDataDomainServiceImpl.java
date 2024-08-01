package com.wither.dwm.plan.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wither.dwm.plan.bean.DpDataDomain;
import com.wither.dwm.plan.mapper.DpDataDomainMapper;
import com.wither.dwm.plan.service.DpDataDomainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据域 服务实现类
 * </p>
 *
 * @author wither
 * @since 2024-07-31
 */
@Service
@DS("dwm")
public class DpDataDomainServiceImpl extends ServiceImpl<DpDataDomainMapper, DpDataDomain> implements DpDataDomainService {

    public List getListForQuery(Long modelId  ){
        String modelCondition= "";
        if(modelId !=null){
            modelCondition =" and t.model_id="+modelId;
        }
        return baseMapper.selectListForQuery(modelCondition);

    }

}
