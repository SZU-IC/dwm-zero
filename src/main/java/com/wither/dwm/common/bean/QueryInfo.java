package com.wither.dwm.common.bean;

import lombok.Data;

/**
 * @Classname QueryInfo
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/8/1 11:16
 * @Created by 97952
 */
@Data
public class QueryInfo  {

    Long modelId;

    String tableNameQuery;

    String metricName;

    int pageSize;

    int pageNo;

    //在方法中拼接SQL的limit语句
    public String getLimitSQL(){
        return " limit "+(pageNo-1)*pageSize+","+pageSize;
    }
}
