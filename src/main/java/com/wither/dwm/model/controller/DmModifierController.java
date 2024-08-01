package com.wither.dwm.model.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.common.bean.Result;
import com.wither.dwm.model.bean.DmModifier;
import com.wither.dwm.model.service.DmModifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 限定词表 前端控制器
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@RestController
@CrossOrigin
@RequestMapping("/data-model/modifier")
public class DmModifierController {
    @Autowired
    DmModifierService dmModifierService;

    @PostMapping("/detail")
    public Result save(@RequestBody DmModifier dmModifier) {
        dmModifier.setLastUpdateTime(new Date());
        dmModifier.setLastUpdateUserId(9999L);
        dmModifierService.saveOrUpdate(dmModifier);
        return Result.ok(dmModifier);

    }

    @GetMapping("/list")
    public Result getList(QueryInfo queryInfo) {
        //这个部分的查询放在服务层组织后面
        List list = dmModifierService.getListForQuery(queryInfo);
        Integer total = dmModifierService.getTotalForQuery(queryInfo);

        return Result.ok(list, total);
    }

    @GetMapping("/detail/{id}")
    public Result getDetail(@PathVariable("id") Long id) {
        DmModifier dmModifier = dmModifierService.getById(id);
        return Result.ok(dmModifier);

    }

    @GetMapping("/options")
    public Result getOptions(@RequestParam(value = "modelId", required = false) Long modelId) {
        QueryWrapper<DmModifier> queryWrapper = new QueryWrapper<DmModifier>()
                .select("id", "name_chn as name")
                .eq("is_deleted", "0")
                .eq(modelId != null, "model_id", modelId);
        List list = dmModifierService.listMaps(queryWrapper);
        return Result.ok(list);
    }

}
