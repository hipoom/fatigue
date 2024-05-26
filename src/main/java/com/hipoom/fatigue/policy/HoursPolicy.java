package com.hipoom.fatigue.policy;

import java.util.List;

import com.hipoom.fatigue.FatiguePolicyOptions;
import com.hipoom.fatigue.entity.BusinessVO;
import com.hipoom.fatigue.entity.BusinessVO.Record;

/**
 * 小时级别的策略。
 *
 * @author ZhengHaiPeng
 * @since 2024/5/18 12:35
 */
public class HoursPolicy extends FatiguePolicyOptions {

    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    public HoursPolicy(int times, int period) {
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
        final long millsPerHour = 60 * 60 * 1000;
        // 当前的小时数索引
        long currentHourIndex = now / millsPerHour;

        int count = 0;

        // 遍历
        for (Record record : records) {
            long recordHourIndex = record.timestamp / millsPerHour;
            // 计算这条记录，距离此时此刻的小时数差距
            // 默认记录的都是过去的，如果系统调整过时间，则大于当前时刻的记录也会被参与判断
            long hoursDiff = currentHourIndex - recordHourIndex;
            if (hoursDiff < period) {
                count++;
            }
        }

        return count;
    }

}
