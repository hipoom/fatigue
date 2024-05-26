package com.hipoom.fatigue.entity;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author ZhengHaiPeng
 * @since 2024/5/18 13:33
 */
public class BusinessVO {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    @SerializedName("records")
    public List<Record> records;



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * @return 不会为 null.
     */
    public static BusinessVO createDefault() {
        BusinessVO vo = new BusinessVO();
        vo.records = new LinkedList<>();
        return vo;
    }



    /* ======================================================= */
    /* Inner Class                                             */
    /* ======================================================= */

    public static class Record {

        @SerializedName("timestamp")
        public long timestamp;

        public Record(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
