package com.wither.dwm.model.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wither.dwm.common.bean.Result;
import com.wither.dwm.model.bean.DmTableDataInfo;
import com.wither.dwm.model.bean.DmTableSync;
import com.wither.dwm.model.service.DmTableDataInfoService;
import com.wither.dwm.model.service.DmTableSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 数据信息表 前端控制器
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@RestController
@RequestMapping("/data-model/datainfo")
public class DmTableDataInfoController {
    @Autowired
    DmTableDataInfoService dmTableDataInfoService;

    @Autowired
    DmTableSyncService dmTableSyncService;

    @GetMapping("/detail/{id}")
    public Result getDetail(@PathVariable("id") Long tableId){
        QueryWrapper<DmTableDataInfo> queryWrapper = new QueryWrapper<DmTableDataInfo>().eq("table_id", tableId);
        DmTableDataInfo tableDataInfo = dmTableDataInfoService.getOne(queryWrapper);

        return Result.ok(tableDataInfo);

    }

    // 保存同步信息列表
    @PostMapping("/list")
    public Result  saveSyncList(@RequestBody List<DmTableSync> tableSyncList){

        dmTableSyncService.saveOrUpdateBatch(tableSyncList);
        return Result.ok(tableSyncList);

    }
}
