package com.wither.dwm.model.service;

import com.wither.dwm.common.bean.QueryInfo;
import com.wither.dwm.model.bean.DmTable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据表 服务类
 * </p>
 *
 * @author wither
 * @since 2024-08-01
 */
public interface DmTableService extends IService<DmTable> {
    public void saveTableAll(DmTable dmTable);

    public List getListForQuery(QueryInfo queryInfo);

    //public Integer getCountForQuery(QueryInfo queryInfo );

    Integer getTotalForQuery(QueryInfo queryInfo);

    public DmTable getTableAll(Long tableId);
    public void submitToHive (Long tableId);
}
