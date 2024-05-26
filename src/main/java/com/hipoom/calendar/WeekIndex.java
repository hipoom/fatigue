package com.hipoom.calendar;

import java.util.TimeZone;

/**
 * @author ZhengHaiPeng
 * @since 2024/5/25 19:39
 */
public class WeekIndex {

    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    public static long get(long mills, TimeZone zone) {
        final long millsPerHour = 60 * 60 * 1000;
        final long millsPerDay = millsPerHour * 24;
        final long millsPerWeek = millsPerDay * 7;

        // 根据时区，修正偏移量
        long fixOffset = mills + zone.getRawOffset();

        // 3 天的毫秒数
        long mills3Day = millsPerDay * 3;

        // 用于计算周数的毫秒数
        long temp = (mills + zone.getRawOffset()) - mills3Day;

        // <--------->(-259200000)<---->(0)<---->(259199999)<------------->
        //           |<------------------0---------------->|

        // 如果差值 >0，说明 mills 代表的时刻在 1970-01-04 00:00:00 以后
        if (fixOffset > 259199999) {
            // 周索引就是毫秒数除以 7 天对应的毫秒数，再 +1
            return (temp / millsPerWeek) + 1;
        }

        // 是负值，且小于等于 3 天内的毫秒数，
        // 说明 mills 代表的时刻在 [1969-12-29 00:00:00.000, 1970-01-03 23:59:59.999] 区间内.
        if (fixOffset >= -259200000) {
            return 0;
        }

        // 说明 mills 代表的时刻在 1969-12-28 23:59:59.999 以前
        long abs = Math.abs(temp);
        return - abs / millsPerWeek -1;
    }

}
