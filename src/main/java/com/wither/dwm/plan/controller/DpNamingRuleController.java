package com.wither.dwm.plan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wither.dwm.common.bean.Result;
import com.wither.dwm.plan.bean.DpNamingRule;
import com.wither.dwm.plan.service.DpNamingRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 命名规则 前端控制器
 * </p>
 *
 * @author wither
 * @since 2024-07-31
 */
@RestController
@RequestMapping("/data-plan/naming")
public class DpNamingRuleController {
    @Autowired
    private DpNamingRuleService dpNamingRuleService;

    /***
      * @MethodName saveRule 保存命名接口
      * @Description
     * @param: dpNamingRule
     * @return: com.wither.dwm.common.bean.Result
      * @Author Wither
      * @Date 2024/7/31 15:28
      */
    @PostMapping("/detail")
    public Result saveRule(@RequestBody DpNamingRule dpNamingRule) {
        dpNamingRule.setLastUpdateTime(new Date());
        dpNamingRule.setLastUpdateUserId(999L);
        dpNamingRuleService.saveOrUpdate(dpNamingRule);
        return Result.ok(dpNamingRule);
    }

    @GetMapping("/list")
    public Result getRuleList(){
        QueryWrapper<DpNamingRule> queryWrapper = new QueryWrapper<DpNamingRule>()
                .eq("is_deleted", "0");

        List<Map<String, Object>> maps = dpNamingRuleService.listMaps(queryWrapper);

        return Result.ok(maps);
    }

    @GetMapping("/detail/{id}")
    public Result getRule(@PathVariable("id") Long id){
        return Result.ok(dpNamingRuleService.getById(id));
    }



}
