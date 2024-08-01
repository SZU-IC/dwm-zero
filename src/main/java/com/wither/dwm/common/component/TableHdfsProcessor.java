package com.wither.dwm.common.component;

import com.wither.dwm.model.bean.DmTableDataInfo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.metastore.api.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

/**
 * @Classname TableHdfsProcessor
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/8/1 15:24
 * @Created by 97952
 */
@Component
public class TableHdfsProcessor {


    @Autowired
    private TableHiveProcessor tableHiveProcessor;

    //1  准备阶段
    //         1.1  访问工具   (hdfs)
    //         1.2   递归起点
    //         1.3   收集数据的容器
    public void extractDataInfoFromHdfs(DmTableDataInfo dmTableDataInfo)  {

        Table hiveTable = null;
        try {
            hiveTable = tableHiveProcessor.getMetaClient().getTable(dmTableDataInfo.getSchemaName(), dmTableDataInfo.getTableName());


            //1.1  访问工具   (hdfs)
            FileSystem fileSystem = FileSystem.get(new URI(hiveTable.getSd().getLocation()),new Configuration(),hiveTable.getOwner() );
            //         1.2   递归起点  1级子目录的清单
            FileStatus[] fileStatuses = fileSystem.listStatus(new Path(hiveTable.getSd().getLocation()));
            // 1.3   收集数据的容器 tableMetaInfo
            //  进行递归操作
            dmTableDataInfo.setDataSizeAllRep(0L);
            getMetaFromHdfsRec(fileSystem, fileStatuses, dmTableDataInfo);

            System.out.println(dmTableDataInfo.getTableName()+  "：：：tableMetaInfo.getTableSize() = " + dmTableDataInfo.getDataSize());

            //1.5 关闭
            fileSystem.close();
        } catch ( Exception e) {
            e.printStackTrace();
            throw new RuntimeException("访问hdfs失败",e);
        }

    }


    // 递归过程
    //         1       遍历到的节点是中间节点
    //处理计算     收集数据    下探展开    递归的回调
    //          2     还是 叶子结点
    //                         处理计算     收集数据  返回
    private void getMetaFromHdfsRec(FileSystem fileSystem, FileStatus[] fileStatuses, DmTableDataInfo dmTableDataInfo) throws IOException {
        //0 遍历递归对象集合
        for (FileStatus fileStatus : fileStatuses) {
            if(  fileStatus.isDirectory()){
                //         1       遍历到的节点是中间节点 目录
                //处理计算     收集数据    下探展开    递归的回调
                FileStatus[] subFileStatus = fileSystem.listStatus(fileStatus.getPath());
                getMetaFromHdfsRec(fileSystem,subFileStatus,dmTableDataInfo);
            }else {
                //          2     还是 叶子结点    文件

                // 统计大小
                long len = fileStatus.getLen();
                // 结合副本数统计大小
                dmTableDataInfo.setDataSizeAllRep( dmTableDataInfo.getDataSizeAllRep()+ len* fileStatus.getReplication());
                // 最后修改时间
                Date lastModifyDate = new Date(fileStatus.getModificationTime());
                if( dmTableDataInfo.getDataLastModifyTime() ==null )       {
                    dmTableDataInfo.setDataLastModifyTime(lastModifyDate);
                } else if (dmTableDataInfo.getDataLastModifyTime().compareTo(lastModifyDate)<0) { //前小后大
                    dmTableDataInfo.setDataLastModifyTime(lastModifyDate);
                };
                // 最后访问时间
                Date lastAccessDate = new Date(fileStatus.getAccessTime());
                if( dmTableDataInfo.getDataLastAccessTime()==null )       {
                    dmTableDataInfo.setDataLastAccessTime(lastAccessDate);
                } else if (dmTableDataInfo.getDataLastAccessTime().compareTo(lastAccessDate)<0) { //前小后大
                    dmTableDataInfo.setDataLastAccessTime(lastAccessDate);
                }
            }
        }
    }

}
