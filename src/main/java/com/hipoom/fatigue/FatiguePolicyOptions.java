package com.hipoom.fatigue;

import com.hipoom.fatigue.database.DataStore;
import com.hipoom.fatigue.entity.BusinessVO;

/**
 * @author ZhengHaiPeng
 * @since 2024/5/18 12:09
 */
public abstract class FatiguePolicyOptions {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    protected int times;

    protected int period;



    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    /**
     * 请使用 FatiguePolicy 创建。
     */
    public FatiguePolicyOptions(int times, int period) {
        this.times = times;
        this.period = period;
    }



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 是否满足触发条件。
     */
    public abstract boolean canTrigger(long timestamp, String business);




    /* ======================================================= */
    /* Protect Methods                                         */
    /* ======================================================= */

    /**
     * @return 不会是 null。
     */
    protected BusinessVO loadVO(String business) {
        return DataStore.loadOrInitial(business);
    }
}
