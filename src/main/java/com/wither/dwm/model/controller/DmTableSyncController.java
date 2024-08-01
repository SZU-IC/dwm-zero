package com.wither.dwm.model.controller;

import com.wither.dwm.common.bean.Result;
import com.wither.dwm.common.component.TableHiveProcessor;
import com.wither.dwm.model.bean.DmTableSync;
import com.wither.dwm.model.service.DmTableSyncService;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 数据同步信息表 前端控制器
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@RestController
@RequestMapping("/data-model/sync")
public class DmTableSyncController {

    @Autowired
    DmTableSyncService dmTableSyncService;

    @Autowired
    TableHiveProcessor tableHiveProcessor;


    @GetMapping("/schema/options")
    public Result schemaOption() throws TException {
        List<String> schemaNameList = tableHiveProcessor.getMetaClient().getAllDatabases();
        return Result.ok(schemaNameList);
    }

    @GetMapping("/list")
    public Result showSyncTableList(@RequestParam("schemaName")  String schemaName){
        List<DmTableSync> syncTableListFromHive = dmTableSyncService.getSyncTableListFromHive(schemaName);
        return Result.ok(syncTableListFromHive);
    }

    @PostMapping("/meta")
    public Result syncTableMeta(@RequestBody List<DmTableSync> dmTableSyncList){
        dmTableSyncService.syncTableMeta(dmTableSyncList);
        return Result.ok(dmTableSyncList);
    }

//    @PostMapping("/datainfo")
//    public Result syncTableDataInfo(@RequestBody List<DmTableSync> dmTableSyncList){
//        dmTableSyncService.syncTableDataInfo(dmTableSyncList);
//        return Result.ok(dmTableSyncList);
//    }
}
