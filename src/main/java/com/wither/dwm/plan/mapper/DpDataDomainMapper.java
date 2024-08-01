package com.wither.dwm.plan.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wither.dwm.plan.bean.DpDataDomain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 数据域 Mapper 接口
 * </p>
 *
 * @author wither
 * @since 2024-07-31
 */
@Mapper
@DS("dwm")
public interface DpDataDomainMapper extends BaseMapper<DpDataDomain> {

    @Select("select t.*,dm.model_name from  dp_data_domain t left join dp_data_warehouse_model dm   " +
            " on t.model_id =dm.id  " +
            " where t.is_deleted='0'  ${modelCondition}")
    public List<DpDataDomain> selectListForQuery(String modelCondition);

}