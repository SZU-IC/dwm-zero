package com.wither.dwm.model.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.common.bean.Result;
import com.wither.dwm.model.bean.DmDimension;
import com.wither.dwm.model.bean.DmModifier;
import com.wither.dwm.model.service.DmDimensionService;
import com.wither.dwm.model.service.DmModifierService;
import com.wither.dwm.plan.bean.DpBusiProcess;
import com.wither.dwm.plan.service.DpBusiProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 维度 前端控制器
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@RestController
@CrossOrigin
@RequestMapping("/data-model/dim")
public class DmDimensionController {

    @Autowired
    DmDimensionService dmDimensionService;

    @GetMapping("/list")
    public Result getList(  QueryInfo queryInfo){
        //这个部分的查询放在服务层
        List list = dmDimensionService.getListForQuery( queryInfo);
        Integer total = dmDimensionService.getTotalForQuery(queryInfo);

        return Result.ok(list,total);
    }

    @GetMapping("/detail/{id}")
    public Result getDetail(@PathVariable("id") Long id){
        DmDimension dmDimension = dmDimensionService.getById(id);
        return Result.ok(dmDimension);
    }

    @GetMapping("/options")
    public Result getOptions(@RequestParam(value = "modelId",required = false) Long modelId){
        QueryWrapper<DmDimension> queryWrapper = new QueryWrapper<DmDimension>()
                .select("id", "name_chn as name","name_eng as nameEng")
                .eq("is_deleted", "0")
                .eq(modelId !=null ,"model_id",modelId);
        List list = dmDimensionService.listMaps(queryWrapper);
        return Result.ok(list);
    }

//    @Autowired
//    private DpBusiProcessService dpBusiProcessService;
//
//    @PostMapping("/detail")
//    public Result save(@RequestBody DpBusiProcess dpBusiProcess){
//        dpBusiProcess.setLastUpdateTime(new Date());
//        dpBusiProcess.setLastUpdateUserId(9999L);
//        dpBusiProcessService.saveOrUpdate(dpBusiProcess);
//        return Result.ok(dpBusiProcess);
//    }
}
