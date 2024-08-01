package com.wither.dwm.model.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.model.bean.DmModifier;
import com.wither.dwm.model.mapper.DmModifierMapper;
import com.wither.dwm.model.service.DmModifierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 限定词表 服务实现类
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Service
@DS("dwm")
public class DmModifierServiceImpl extends ServiceImpl<DmModifierMapper, DmModifier> implements DmModifierService {
    public List getListForQuery(QueryInfo queryInfo){
        String condition= "";
        if(queryInfo.getModelId() !=null){
            condition +=" and t.model_id="+queryInfo.getModelId();
        }

        String limit = queryInfo.getLimitSQL();
        List list=baseMapper.selectListForQuery(condition,limit);
        return list;
    }



    public Integer getTotalForQuery(QueryInfo queryInfo){
        String condition= "";
        if(queryInfo.getModelId() !=null){
            condition +=" and t.model_id="+queryInfo.getModelId();
        }
        Integer total=  baseMapper.selectCountForQuery(condition);
        return total;
    }
}
