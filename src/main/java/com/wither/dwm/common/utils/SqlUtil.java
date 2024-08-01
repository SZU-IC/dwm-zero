package com.wither.dwm.common.utils;

/**
 * @Classname SqlUtil
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/8/1 14:16
 * @Created by 97952
 */
public class SqlUtil {

    public static String filterUnsafeSql(String input) {
        if (input == null) {
            return null;
        }

        // 替换 MySQL 中可能导致 SQL 注入的特殊字符
        return input.replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\u001A", "\\Z")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
