package com.wither.dwm.model.mapper;

import com.wither.dwm.common.constants.CommonCodes;
import com.wither.dwm.common.utils.ArrayStringHandler;
import com.wither.dwm.model.bean.DmMetric;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wither.dwm.model.bean.DmTable;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 指标 Mapper 接口
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Mapper
public interface DmMetricMapper extends BaseMapper<DmMetric> {


    @Select("select t.*,dm.model_name,dd.name_chn as domain_name,cc.code_name as metric_type_name  " +
            " from  dm_metric t " +
            " left join dp_data_warehouse_model dm   on t.model_id =dm.id  " +
            " left join dp_data_domain dd on t.model_id =dd.id" +
            " left join common_code cc on t.metric_type =cc.code_no" +

            " where t.is_deleted='0' and metric_type in ('"+ CommonCodes.METRIC_TYPE_ATOMIC +"','"+CommonCodes.METRIC_TYPE_DERIVED+"','"+CommonCodes.METRIC_TYPE_COMPOSITE+"') ${modelCondition} ${limitSQL}")
    public List<DmMetric> selectListForQuery(@Param("modelCondition") String modelCondition, @Param("limitSQL") String limitSQL);

    @Select("select  count(*)  "+
            " from  dm_metric t " +
            " left join dp_data_warehouse_model dm   " +
            " on t.model_id =dm.id  " +
            " left join common_code cc on t.model_id =cc.code_no" +
            " where t.is_deleted='0'  ${modelCondition}  ")
    public Integer selectCountForQuery(String modelCondition );


    @Select("select distinct t.id, t.table_name from dm_metric dm join dm_table t on dm.link_table_id = t.id #{conditions}")
    List<Map> selectLinkTableList(String conditions);

    @Select("select tc.id as  link_column_id, tc.column_name ,tc.column_comment,t.model_id, t.domain_id,t.id as link_table_id,m.* from  dm_table_column  tc\n" +
            "            join dm_table t on t.id = tc.table_id\n" +
            "            left join   dm_metric  m on tc.id = m.link_column_id\n" +
            "            where tc.is_deleted='0' and  t.id=#{tableId}")
    @Results(id = "DmMetricResultMap", value = {
            @Result(column = "link_dim_ids", property = "linkDimIds", typeHandler = ArrayStringHandler.class),
            @Result(column = "link_atomic_metric_ids", property = "linkAtomicMetricIds", typeHandler = ArrayStringHandler.class),
            @Result(column = "link_modifier_ids", property = "linkModifierIds", typeHandler = ArrayStringHandler.class)
    })
    public List<DmMetric> selectMetricListForLink(@Param("tableId") Long tableId);
}
