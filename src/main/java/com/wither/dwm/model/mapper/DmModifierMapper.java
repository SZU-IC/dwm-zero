package com.wither.dwm.model.mapper;

import com.wither.dwm.model.bean.DmModifier;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wither.dwm.plan.bean.DpBusiProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 限定词表 Mapper 接口
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Mapper
public interface DmModifierMapper extends BaseMapper<DmModifier> {
    @Select("select t.*,dm.model_name" +
            " from  dm_modifier t " +
            " left join dp_data_warehouse_model dm   " +
            " on t.model_id =dm.id  " +
            " where t.is_deleted='0'  ${modelCondition} ${limitSQL}")
    public List<DpBusiProcess> selectListForQuery(@Param("modelCondition") String modelCondition, @Param("limitSQL") String limitSQL);

    @Select("select  count(*)  "+
            " from  dm_modifier t " +
            " left join dp_data_warehouse_model dm   " +
            " on t.model_id =dm.id  " +
            " where t.is_deleted='0'  ${modelCondition}  ")
    public Integer selectCountForQuery(String modelCondition );

}
