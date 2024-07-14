package com.hipoom.fatigue;

import com.hipoom.fatigue.policy.DaysPolicy;
import com.hipoom.fatigue.policy.HoursPolicy;
import com.hipoom.fatigue.policy.MonthPolicy;
import com.hipoom.fatigue.policy.TotalPolicy;
import com.hipoom.fatigue.policy.WeekPolicy;

/**
 * @author ZhengHaiPeng
 * @since 2024/5/18 12:10
 */
public class FatigueTimes {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    private final int times;


    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    FatigueTimes(int times) {
        this.times = times;
    }



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 每 hour 个自然时内，最多执行 times 次。
     * 使用示例：
     * FatiguePolicy.maxTimes(1).inHours(1).
     * <br></br>
     * 表示 1 个自然时内，最多执行 1 次。
     * 注意，这里判断的是自然时，
     * 例如当 times 是 1 时: 如果 10:59 触发了一次，在 11:00 时会判断可以触发。
     */
    public FatiguePolicyOptions inHours(int hour) {
        return new HoursPolicy(this.times, hour);
    }

    /**
     * 每 day 自然天内，最多执行 times 次。
     * 使用示例：
     * FatiguePolicy.maxTimes(1).inDays(1).
     * <br></br>
     * 这个方法和 inHours(24)  不等价。例如：
     * 如果 1 号晚上 23 点触发过一次，在 2 号凌晨 1 点判断，则 inHours(24) 不满足，但 inDays(1) 满足。
     */
    public FatiguePolicyOptions inDays(int day) {
        return new DaysPolicy(this.times, day);
    }

    /**
     * 每 week 自然周内，最多执行 times 次。
     * 使用示例：
     * FatiguePolicy.maxTimes(1).inWeeks(1).
     * <br></br>
     * 这个方法和 inDays(7)  不等价。例如：
     * 如果 周日 触发过一次，在 下周一 时判断，则 inDays(7) 不满足，但 inWeek(1) 满足。
     * 同理，inWeeks(7) 、 inDays(7)、 inHours(7x24) 都不等价。
     */
    public FatiguePolicyOptions inWeeks(int week) {
        return new WeekPolicy(this.times, week);
    }

    /**
     * 每 month 自然月内，最多执行 times 次。
     * 使用示例：
     * FatiguePolicy.maxTimes(1).inWeeks(1).
     * <br></br>
     * 这个方法和 inDays(30)  不等价。例如：
     * 如果 30号 触发过一次，在下个月 1 号时判断，则 inDays(30) 不满足，但 inMonth(1) 满足。
     * 同理，inDays(30)、 inHours(30x24)、 inMonths(1) 都不等价。
     */
    public FatiguePolicyOptions inMonths(int month) {
        return new MonthPolicy(this.times, month);
    }

    /**
     * 累计总共触发 max 及其以内。
     *
     * @return nonnull.
     */
    public FatiguePolicyOptions inTotal(int max) {
        return new TotalPolicy(this.times);
    }

}
