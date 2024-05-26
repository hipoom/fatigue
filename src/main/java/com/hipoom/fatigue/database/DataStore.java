package com.hipoom.fatigue.database;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.hipoom.Files;
import com.hipoom.Files.DstFileExistPolicy;
import com.hipoom.fatigue.entity.BusinessVO;
import com.hipoom.fatigue.entity.BusinessVO.Record;
import com.hipoom.file.catalogue.Catalogue;

/**
 * @author ZhengHaiPeng
 * @since 2024/5/18 12:39
 */
public class DataStore {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    public static File workspace;



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 加载 business 对应的 VO，如果没有该业务，则新建业务。
     *
     * @return 不会为 null.
     */
    public synchronized static BusinessVO loadOrInitial(String business) {
        String json = loadJson(business);

        BusinessVO vo;

        // 如果加载的内容是 null, 说明是首次调用该方法。
        if (json == null) {
            Catalogue.addBusiness(workspace.getAbsolutePath(), business);
            vo = BusinessVO.createDefault();
        }
        else if (json.isEmpty()) {
            vo = BusinessVO.createDefault();
        } else {
            Gson gson = new Gson();
            try {
                vo = gson.fromJson(json, BusinessVO.class);
            } catch (Exception e) {
                vo = BusinessVO.createDefault();
            }
            if (vo.records == null) {
                vo.records = new LinkedList<>();
            }
        }
        return vo;
    }

    /**
     * 标记一个业务刚执行了。 并且清理旧的数据。
     *
     * @param business 业务名称。
     * @param daysNeedRemove 删除距离当前时刻 daysNeedRemove 天前的数据。
     */
    public synchronized static void markTriggerOnce(String business, long timestamp, int daysNeedRemove) {
        BusinessVO vo = loadOrInitial(business);
        vo.records.add(new Record(timestamp));
        removeRecordBefore(vo, daysNeedRemove);
        File file = Catalogue.getBusinessFile(workspace.getAbsolutePath(), business);
        Files.writeText(file, new Gson().toJson(vo), DstFileExistPolicy.Overwrite);
    }

    /**
     * 清理 vo 中的 records, 移除 days 前的记录。
     */
    public synchronized static void removeRecordBefore(BusinessVO vo, int days) {
        long now = System.currentTimeMillis();
        long gap = days * 24 * 60 * 60 * 1000L;
        long temp = now - gap;
        List<Record> records = new ArrayList<>(vo.records);
        List<Record> needRemoveRecords = new LinkedList<>();
        for(Record record : records) {
            if (record.timestamp < temp) {
                needRemoveRecords.add(record);
            }
        }
        vo.records.removeAll(needRemoveRecords);
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    /**
     * 读取 business 对应的业务数据。
     * 如果文件不存在，返回 null.
     * 可能存在返回空字符串 "" 的情况。
     */
    private synchronized static String loadJson(String business) {
        File dir = workspace;

        // 判断是否设置了工作目录
        if (dir == null) {
            throw new IllegalStateException("没有设置工作目录，请使用 Fatigue.setDataStoreDirectory() 设置");
        }

        // 获取业务文件
        File file = Catalogue.getBusinessFile(dir.getAbsolutePath(), business);
        if (file == null) {
            return null;
        }
        return Files.readText(file);
    }

}
