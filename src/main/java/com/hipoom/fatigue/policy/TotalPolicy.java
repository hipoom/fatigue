package com.hipoom.fatigue.policy;

import java.util.List;

import com.hipoom.fatigue.FatiguePolicyOptions;
import com.hipoom.fatigue.entity.BusinessVO;
import com.hipoom.fatigue.entity.BusinessVO.Record;

/**
 * @author ZhengHaiPeng
 * @since 2024/7/14 18:11
 */
public class TotalPolicy extends FatiguePolicyOptions {

    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    public TotalPolicy(int times) {
        super(times, Integer.MAX_VALUE);
    }



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    @Override
    public boolean canTrigger(long timestamp, String business) {
        BusinessVO vo    = loadVO(business);
        List<Record> tCatch = vo.records;
        int count = 0;
        if (tCatch != null) {
            count = tCatch.size();
        }
        return count < times;
    }

}
