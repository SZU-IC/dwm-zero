package com.wither.dwm.plan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.common.bean.Result;
import com.wither.dwm.plan.bean.DpBusiProcess;
import com.wither.dwm.plan.service.DpBusiProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 业务过程 前端控制器
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@RestController
@RequestMapping("/data-plan/busi")
public class DpBusiProcessController {
    @Autowired
    private DpBusiProcessService dpBusiProcessService;

    @PostMapping("/detail")
    public Result save(@RequestBody DpBusiProcess dpBusiProcess){
        dpBusiProcess.setLastUpdateTime(new Date());
        dpBusiProcess.setLastUpdateUserId(9999L);
        dpBusiProcessService.saveOrUpdate(dpBusiProcess);
        return Result.ok(dpBusiProcess);
    }

    @GetMapping("/list")
    public Result getList( QueryInfo queryInfo){
        //这个部分的查询放在服务层组织后面
        List list = dpBusiProcessService.getListForQuery(  queryInfo);
        Integer total = dpBusiProcessService.getCountForQuery(queryInfo);
        return Result.ok(list,total );
    }

    @GetMapping("/detail/{id}")
    public Result getDetail(@PathVariable("id") Long id){
        DpBusiProcess busiProcess = dpBusiProcessService.getById(id);
        return Result.ok(busiProcess);
    }

    @GetMapping("/options")
    public Result getOptions(@RequestParam(value = "modelId",required = false) Long modelId){
        QueryWrapper<DpBusiProcess> queryWrapper = new QueryWrapper<DpBusiProcess>()
                .select("id", "name_chn as name","name_eng as nameEng")
                .eq("is_deleted", "0")
                .eq(modelId !=null ,"model_id",modelId);
        List  list = dpBusiProcessService.listMaps (queryWrapper);
        return Result.ok(list);
    }
}
