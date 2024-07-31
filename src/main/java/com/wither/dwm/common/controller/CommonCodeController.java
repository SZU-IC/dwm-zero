package com.wither.dwm.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wither.dwm.common.bean.CommonCode;
import com.wither.dwm.common.bean.Result;
import com.wither.dwm.common.service.CommonCodeService;
import org.apache.derby.impl.sql.execute.rts.RealSortStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标准编码 前端控制器
 * </p>
 *
 * @author wither
 * @since 2024-07-31
 */
@RestController
@CrossOrigin
@RequestMapping("/data-common/code")
public class CommonCodeController {
    @Autowired
    private CommonCodeService commonCodeService;

    @GetMapping("/options")
    public Result getListByParent(@RequestParam("pCode") String pCode) {
        QueryWrapper<CommonCode> queryWrapper = new QueryWrapper<CommonCode>()
                .select("code_no as id", "code_name as name", "ext_name as extName")
                .eq("parent_code_no", pCode)
                .eq("is_deleted", "0");

        List mapList = commonCodeService.listMaps(queryWrapper);

        return Result.ok(mapList);
    }
}
