package com.wither.dwm.model.mapper;

import com.wither.dwm.model.bean.DmTableSync;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 数据同步信息表 Mapper 接口
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
@Mapper
public interface DmTableSyncMapper extends BaseMapper<DmTableSync> {

}
