package com.hipoom.fatigue;

/**
 * @author ZhengHaiPeng
 * @since 2024/5/18 12:08
 */
public class FatiguePolicy {

    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 在指定时间周期内，最多能触发多少次。
     */
    public static FatigueTimes maxTimes(int times) {
        return new FatigueTimes(times);
    }

}
