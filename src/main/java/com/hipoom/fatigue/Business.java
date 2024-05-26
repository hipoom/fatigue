package com.hipoom.fatigue;

import com.hipoom.fatigue.database.DataStore;

/**
 * @author ZhengHaiPeng
 * @since 2024/5/18 11:53
 */
public class Business {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    private final String business;



    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    Business(String business) {
        this.business = business;
    }



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 标记执行了一次，并且清理 daysNeedRemove 前的数据。
     */
    public void markTriggerOnce(long timestamp, int daysNeedRemove) {
        DataStore.markTriggerOnce(business, timestamp, daysNeedRemove);
    }

    /**
     * 满足所有 policy 的时候，返回 true.
     *
     * @param options 需要满足的策略。
     */
    public boolean canTrigger(FatiguePolicyOptions... options) {
        return canTrigger(System.currentTimeMillis(), options);
    }

    public boolean canTrigger(long timestamp, FatiguePolicyOptions... options) {
        for (FatiguePolicyOptions option : options) {
            if (option.canTrigger(timestamp, business)) {
                continue;
            }
            return false;
        }
        return true;
    }
}
