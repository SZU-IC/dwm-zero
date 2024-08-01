package com.wither.dwm.common.utils;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @Classname ArrayStringHandler
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/8/1 14:03
 * @Created by 97952
 */
public class ArrayStringHandler extends BaseTypeHandler<Long[]>{

    //把String[]转为String写入preparedStatement
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Long[] longs, JdbcType jdbcType) throws SQLException {
        String value = Arrays.stream(longs).map(String::valueOf).collect(Collectors.joining(","));
        preparedStatement.setString(i, String.join(",", value   ));
    }

    //把字段结果集转为 Long[]
    @Override
    public Long[] getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        String value = resultSet.getString(columnName);
        if (value != null&&value.length() > 0) {
            return Arrays.stream(value.split(",")).map(Long::valueOf).toArray(Long[]::new);
        } else {
            return null;
        }
    }

    @Override
    public Long[] getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String value = resultSet.getString(i);
        if (value != null&&value.length() > 0) {
            return Arrays.stream(value.split(",")).map(Long::valueOf).toArray(Long[]::new);
        } else {
            return null;
        }
    }

    @Override
    public Long[] getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String value = callableStatement.getString(i);
        if (value != null&&value.length() > 0) {
            return Arrays.stream(value.split(",")).map(Long::valueOf).toArray(Long[]::new);
        } else {
            return null;
        }
    }
}
