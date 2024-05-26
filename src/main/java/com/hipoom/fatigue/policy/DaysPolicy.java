package com.hipoom.fatigue.policy;

import java.util.List;
import java.util.TimeZone;

import com.hipoom.calendar.TimeInfo;
import com.hipoom.fatigue.FatiguePolicyOptions;
import com.hipoom.fatigue.entity.BusinessVO;
import com.hipoom.fatigue.entity.BusinessVO.Record;

/**
 * @author ZhengHaiPeng
 * @since 2024/5/25 22:38
 */
public class DaysPolicy extends FatiguePolicyOptions {

    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    public DaysPolicy(int times, int period) {
        super(times, period);
    }



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    @Override
    public boolean canTrigger(long timestamp, String business) {
        BusinessVO vo    = loadVO(business);
        int        count = findCount(timestamp, vo.records, period);
        return count < times;
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    /**
     * 遍历 records 记录，计算 period 小时内，有多少次触发。
     */
    private int findCount(long now, List<Record> records, int period) {
        // 计算当前的时间信息
        TimeInfo info = new TimeInfo(now, TimeZone.getDefault());

        int count = 0;

        // 遍历
        for (Record record : records) {
            // 此记录的时间信息
            TimeInfo time = new TimeInfo(record.timestamp, TimeZone.getDefault());
            // 计算天数差距
            long daysDiff = info.dayIndex - time.dayIndex;
            if (daysDiff < period) {
                count++;
            }
        }

        return count;
    }
}
