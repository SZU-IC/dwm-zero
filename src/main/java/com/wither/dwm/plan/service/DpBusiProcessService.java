package com.wither.dwm.plan.service;

import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.plan.bean.DpBusiProcess;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 业务过程 服务类
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
public interface DpBusiProcessService extends IService<DpBusiProcess> {
    public List getListForQuery(QueryInfo queryInfo);

    public Integer getCountForQuery(QueryInfo queryInfo );

}
