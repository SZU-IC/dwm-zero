package com.wither.dwm.plan.service;

import com.wither.dwm.plan.bean.DpDataDomain;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据域 服务类
 * </p>
 *
 * @author wither
 * @since 2024-07-31
 */
public interface DpDataDomainService extends IService<DpDataDomain> {

    public List getListForQuery(Long modelId  );
}
