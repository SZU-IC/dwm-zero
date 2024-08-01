package com.wither.dwm.model.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wither.dwm.model.bean.DmTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 数据表 Mapper 接口
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Mapper
@DS("dwm")
public interface DmTableMapper extends BaseMapper<DmTable> {

    @Select("select t.*,dm.model_name,c.code_name as table_status_name " +
            " from  dm_table t " +
            " left join dp_data_warehouse_model dm  on t.model_id =dm.id   " +
            " left join common_code c  on c.code_no=t.table_status  " +
            " where t.is_deleted='0' ${condition} ${limitSQL}")
    public List<DmTable> selectListForQuery(@Param("condition") String condition, @Param("limitSQL") String limitSQL);


    @Select("select count(*) " +
            " from  dm_table t " +
            " left join dp_data_warehouse_model dm   " +
            " on t.model_id =dm.id  " +
            " where t.is_deleted='0'  ${condition}")
    public Integer selectTotalForQuery(String condition);

}
