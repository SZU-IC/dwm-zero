package com.wither.dwm.model.controller;

import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.common.bean.Result;
import com.wither.dwm.model.bean.DmTable;
import com.wither.dwm.model.service.DmTableColumnService;
import com.wither.dwm.model.service.DmTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数据表 前端控制器
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@RestController
@RequestMapping("/data-model/table")
public class DmTableController {

    @Autowired
    DmTableService dmTableService;

    @Autowired
    DmTableColumnService dmTableColumnService;


    @PostMapping("/detail")
    public Result save(@RequestBody DmTable dmTable){
        dmTable.setLastUpdateTime(new Date());
        dmTable.setLastUpdateUserId(9999L);
        dmTableService.saveTableAll(dmTable);
        return Result.ok(dmTable);
    }

    //{
    //        modelId: '',
    //        total: 0, // 数据库中的总记录数
    //        pageNo: 1, // 默认页码
    //        pageSize: 20 // 每页记录数
    //      }
    @GetMapping("/list")
    public Result getList(QueryInfo queryInfo){
        //这个部分的查询放在服务层组织后面
        List list = dmTableService.getListForQuery( queryInfo);
        Integer total = dmTableService.getTotalForQuery(queryInfo);
        return Result.ok(list,total );
    }

    @GetMapping("/detail/{id}")
    public Result getDetail(@PathVariable("id") Long id){
        DmTable dmTable = dmTableService.getTableAll(id);
        return Result.ok(dmTable);
    }

    @PostMapping("/hive/{id}")
    public Result submitToHive(Long tableId){
        dmTableService.submitToHive(tableId);
        return Result.ok();
    }
}
