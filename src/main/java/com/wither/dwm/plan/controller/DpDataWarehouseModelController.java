package com.wither.dwm.plan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wither.dwm.common.bean.Result;
import com.wither.dwm.common.component.TableHiveProcessor;
import com.wither.dwm.plan.bean.DpDataWarehouseModel;
import com.wither.dwm.plan.service.DpDataWarehouseModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数仓模型 前端控制器
 * </p>
 *
 * @author wither
 * @since 2024-07-31
 */
@RestController
@RequestMapping("/data-plan/model")
public class DpDataWarehouseModelController {

    @Autowired
    private TableHiveProcessor tableHiveProcessor;
    @Autowired
    private DpDataWarehouseModelService dpDataWarehouseModelService;

    @PostMapping("/detail")
    public Result save(@RequestBody DpDataWarehouseModel dpDataWarehouseModel){
        dpDataWarehouseModel.setLastUpdateTime(new Date());
        dpDataWarehouseModel.setLastUpdateUserId(999L);
        dpDataWarehouseModelService.saveOrUpdate(dpDataWarehouseModel);
        return Result.ok(dpDataWarehouseModel);
    }

    @GetMapping("/list")
    public Result getList(){
        return Result.ok(dpDataWarehouseModelService.list()) ;
    }

    @GetMapping("/detail/{id}")
    public Result getDetail(@PathVariable("id") Long id){
        DpDataWarehouseModel warehouseModel = dpDataWarehouseModelService.getById(id);
        return Result.ok(warehouseModel) ;
    }

    @GetMapping("/options")
    public Result getOptions(){
        List<Map<String, Object>> list = dpDataWarehouseModelService.listMaps((new QueryWrapper<DpDataWarehouseModel>().select("id", "model_name as name", "schema_name as schemaName").eq("is_deleted", "0")));
        return  Result.ok(list);
    }

    @PostMapping("/detail/hive")
    public Result submitToHive(@RequestBody DpDataWarehouseModel dpDataWarehouseModel){
        boolean isNew = tableHiveProcessor.submitToHiveSchema(dpDataWarehouseModel.getSchemaName());
        if(!isNew) {
            return Result.error("已存在数据库：" + dpDataWarehouseModel.getSchemaName());
        }
        return Result.ok(dpDataWarehouseModel);
    }
}
