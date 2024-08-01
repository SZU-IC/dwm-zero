package com.wither.dwm.common.scheduler;

import com.wither.dwm.model.service.DmTableSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Classname DwmScheduler
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/8/1 15:34
 * @Created by 97952
 */
@Component
public class DwmScheduler {
    @Autowired
    DmTableSyncService tableSyncService;
    @Scheduled(cron = "10 * * * * *")
    public void everyDaySyncTableDataInfo() {
        tableSyncService.syncDataInfoForScheduler();
    }
}
