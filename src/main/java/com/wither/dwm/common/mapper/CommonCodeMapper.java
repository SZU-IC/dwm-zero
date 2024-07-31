package com.wither.dwm.common.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wither.dwm.common.bean.CommonCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 标准编码 Mapper 接口
 * </p>
 *
 * @author wither
 * @since 2024-07-31
 */
@Mapper
@DS("dwm")
public interface CommonCodeMapper extends BaseMapper<CommonCode> {

}
