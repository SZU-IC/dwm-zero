package com.wither.dwm.model.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.common.bean.Result;
import com.wither.dwm.common.constants.CommonCodes;
import com.wither.dwm.model.bean.DmMetric;
import com.wither.dwm.model.service.DmMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 指标 前端控制器
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@RestController
@RequestMapping("/data-model/metric")
public class DmMetricController {
    @Autowired
    DmMetricService dmMetricService;


    @PostMapping("/detail")
    public Result save(@RequestBody DmMetric dmMetric) {
        dmMetric.setLastUpdateTime(new Date());
        dmMetric.setLastUpdateUserId(9999L);
        dmMetricService.saveOrUpdate(dmMetric);
        return Result.ok(dmMetric);

    }

    @GetMapping("/list")
    public Result getList(QueryInfo queryInfo) {
        //这个部分的查询放在服务层组织后面
        List list = dmMetricService.getListForQuery(queryInfo);
        Integer total = dmMetricService.getTotalForQuery(queryInfo);

        return Result.ok(list, total);
    }

    @GetMapping("/detail/{id}")
    public Result getDetail(@PathVariable("id") Long id) {
        DmMetric dmMetric = dmMetricService.getById(id);
        dmMetric.setLinkTableList(dmMetricService.getLinkTableList(dmMetric));
        return Result.ok(dmMetric);

    }

    @GetMapping("/options")
    public Result getOptions(@RequestParam(value = "modelId", required = false) Long modelId) {
        QueryWrapper<DmMetric> queryWrapper = new QueryWrapper<DmMetric>()
                .select("id", "metric_name as name")
                .eq("is_deleted", "0")
                .eq(modelId != null, "model_id", modelId)
                .eq("metric_type", CommonCodes.METRIC_TYPE_ATOMIC);
        List list = dmMetricService.listMaps(queryWrapper);
        return Result.ok(list);
    }

    @PostMapping("/link")
    public Result submitLink(@RequestBody List<DmMetric> dmMetricList){
        for (DmMetric dmMetric : dmMetricList) {
            dmMetric.setLastUpdateTime(new Date());
            dmMetric.setLastUpdateUserId(9999L);
        }
        dmMetricService.saveOrUpdateBatch(dmMetricList);
        return Result.ok( dmMetricList);

    }

    @GetMapping("/link/{tableId}")
    public Result link(@PathVariable("tableId") Long tableId){
        List metricListForLink = dmMetricService.getMetricListForLink(tableId);
        return Result.ok(metricListForLink);
    }
}
