package com.wither.dwm.model.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wither.dwm.model.bean.DmDimension;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 维度 Mapper 接口
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Mapper
@DS("dwm")
public interface DmDimensionMapper extends BaseMapper<DmDimension> {


    @Select("select t.*,dm.model_name " +
            " from  dm_dimension t " +
            " left join dp_data_warehouse_model dm   " +
            " on t.model_id =dm.id  " +
            " where t.is_deleted='0' ${condition} ${limitSQL}")
    public List<DmDimension> selectListForQuery(@Param("condition") String condition, @Param("limitSQL") String limitSQL);


    @Select("select  count(*) " +
            " from  dm_dimension t " +
            " left join dp_data_warehouse_model dm   " +
            " on dim.model_id =dm.id  " +
            " where t.is_deleted='0'  ${condition}")
    public Integer selectTotalForQuery(String condition);
}