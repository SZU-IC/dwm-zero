package com.wither.dwm.model.service;

import com.wither.dwm.model.bean.DmTableSync;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据同步信息表 服务类
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
public interface DmTableSyncService extends IService<DmTableSync> {
    public List<DmTableSync> getSyncTableListFromHive(String schemaName);

    public void syncTableMeta(List<DmTableSync> dmTableSyncList) ;

    void syncTableDataInfo(List<DmTableSync> dmTableSyncList);

    public void syncDataInfoForScheduler();
}
