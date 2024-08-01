package com.wither.dwm.plan.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wither.dwm.plan.bean.DpBusiProcess;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 业务过程 Mapper 接口
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@DS("dwm")
@Mapper
public interface DpBusiProcessMapper extends BaseMapper<DpBusiProcess> {
    @Select("select bp.*,dm.model_name,dd.name_chn as domain_name" +
            " from  dp_busi_process bp " +
            " left join dp_data_warehouse_model dm   " +
            " on bp.model_id =dm.id  " +
            " left join dp_data_domain dd on bp.model_id =dd.id" +
            " where dd.is_deleted='0'  ${modelCondition} ${limitSQL}")
    public List<DpBusiProcess> selectListForQuery(@Param("modelCondition") String modelCondition, @Param("limitSQL") String limitSQL);

    @Select("select  count(*)  "+
            " from  dp_busi_process bp " +
            " left join dp_data_warehouse_model dm   " +
            " on bp.model_id =dm.id  " +
            " left join dp_data_domain dd on bp.model_id =dd.id" +
            " where dd.is_deleted='0'  ${modelCondition}  ")
    public Integer selectCountForQuery(String modelCondition );
}
