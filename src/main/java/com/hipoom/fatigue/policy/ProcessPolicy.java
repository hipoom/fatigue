package com.hipoom.fatigue.policy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hipoom.fatigue.FatiguePolicyOptions;
import com.hipoom.fatigue.entity.BusinessVO;
import com.hipoom.fatigue.entity.BusinessVO.Record;

/**
 * @author ZhengHaiPeng
 * @since 2024/7/14 18:11
 */
public class ProcessPolicy extends FatiguePolicyOptions {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    private static final Map<String, Integer> business2Count = new HashMap<>();



    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    public ProcessPolicy(int times) {
        super(times, Integer.MAX_VALUE);
    }



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    @Override
    public boolean canTrigger(long timestamp, String business) {
        boolean isContains = business2Count.containsKey(business);

        // 如果没有触发过，那么只要 times 大于 0 即可触发。
        if (!isContains) {
            return times > 0;
        }

        // 如果有曾经触发过
        Integer countBox = business2Count.get(business);
        if (countBox == null) {
            return times > 0;
        }

        int count = countBox;
        return count < times;
    }

}
