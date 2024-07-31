package com.wither.dwm.common.bean;

import lombok.Data;

import java.util.List;

/**
 * @Classname Result
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/7/31 14:30
 * @Created by 97952
 */
@Data
public class Result {
    /***
      * 总数，用于表示数据的总量
      */
    Integer total;

    /**
     * 状态码，默认值是200，表示成功
     */
    Integer status = 200;

    /**
     * 错误信息，用于描述错误原因
     */
    String errorMsg;

    /**
     * 数据对象，存储实际返回的数
     */
    Object data;

    /***
      * @MethodName error
      * @Description
     * @param: msg 错误信息
     * @return: com.wither.dwm.common.bean.Result 表示错误对象
      * @Author Wither
      * @Date 2024/7/31 14:39
      */
    public static Result error(String msg){
        Result result = new Result();
        result.setStatus(600);
        result.setErrorMsg(msg);
        return result;
    }

    /***
      * @MethodName ok
      * @Description
     * @param: msg
     * @return: com.wither.dwm.common.bean.Result
      * @Author Wither
      * @Date 2024/7/31 14:40
      */
    public static Result ok(Object data){
        Result result = new Result();
        result.setData(data);
        return result;
    }

    /***
     * @MethodName ok
     * @Description
     * @param: msg
     * @return: com.wither.dwm.common.bean.Result
     * @Author Wither
     * @Date 2024/7/31 14:40
     */
    public static Result ok(){
        Result result = new Result();
        return result;
    }

    public static Result ok(Object data, int total){
        Result result = new Result();
        result.setData(data);
        result.setTotal(total);
        return result;
    }
}
