package com.wither.dwm.common.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Classname HiveMapper
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/8/1 15:15
 * @Created by 97952
 */
@Mapper
@DS("hive")
public interface HiveMapper {

    //对hive进行统计
    @Insert("analyze table  ${schemaName}.${tableName} compute statistics")
    public void anlyzeTable(@Param("schemaName") String schemaName, @Param("tableName") String tableName);
    //从hive中获取表的描述信息
    @Select("describe formatted   ${schemaName}.${tableName}")
    public List<Map> getTableDesc(@Param("schemaName") String schemaName, @Param("tableName") String tableName);

}