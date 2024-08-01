package com.wither.dwm.plan.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wither.dwm.common.bean.Result;
import com.wither.dwm.plan.bean.DpDataDomain;
import com.wither.dwm.plan.mapper.DpDataDomainMapper;
import com.wither.dwm.plan.service.DpDataDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据域 前端控制器
 * </p>
 *
 * @author wither
 * @since 2024-07-31
 */
@RestController
@RequestMapping("/data-plan/domain")
public class DpDataDomainController {
    @Autowired
    private DpDataDomainService dpDataDomainService;

    @PostMapping("/detail")
    public Result save(@RequestBody DpDataDomain dpDataDomain) {
        dpDataDomain.setLastUpdateTime(new Date());
        dpDataDomain.setLastUpdateUserId(999L);
        dpDataDomainService.save(dpDataDomain);
        return Result.ok(dpDataDomain);
    }

    @GetMapping("/detail/{id}")
    public Result getDetail(@PathVariable("id") Long id){
        DpDataDomain dpDataDomain = dpDataDomainService.getById(id);
        return  Result.ok(dpDataDomain);
    }

    @GetMapping("/options")
    public Result getOptions(@RequestParam(value = "modelId",required = false) Long modelId){
        QueryWrapper<DpDataDomain> queryWrapper = new QueryWrapper<DpDataDomain>()
                .select("id", "name_chn as name","name_eng as nameEng")
                .eq("is_deleted", "0")
                .eq(modelId !=null ,"model_id",modelId);
        List<Map<String, Object>> list = dpDataDomainService.listMaps(queryWrapper);
        return  Result.ok(list);
    }


}
