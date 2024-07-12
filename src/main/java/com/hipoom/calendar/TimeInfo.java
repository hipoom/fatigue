package com.hipoom.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author ZhengHaiPeng
 * @since 2024/5/25 17:13
 */
public class TimeInfo {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    /**
     * 是否支持 java8 的 api.
     * 例如在 Android 中，如果操作系统版本是 6.0 以下，会找不到 LocalDate 类。
     * 所以有部分实现方式就会降级。
     */
    public static boolean isSupportJava8Api = true;

    /**
     * 毫秒时间戳。
     * the current time in milliseconds.
     */
    public final long millsFrom1970;

    /**
     * 自然年。 当 millsFrom1970 是 0 时，year 取值是 1970 年.
     * Calendar Year. When the millsFrom1970 value is 0, it represents 1970.
     */
    public final int yearOfAD;

    /**
     * 相对于 year 的自然月，取值范围是  1 - 12 月.
     * The month of the year in the Gregorian and Julian calendars.
     * The first month is JANUARY which is 1.
     */
    public final int monthOfYear;

    /**
     * 以 1970-01 为起点，monthIndex 取值是 0.
     * monthIndex 记录了从 1970-01 开始的月份数。
     * 例如 1970-02 对应的 monthIndex 是 1.
     */
    public final long monthIndex;

    /**
     * 相对于 1970-01-01 00:00 经历了多少周。
     * 其中 1970-01-01 到 1970-01-03 属于第 0 周。
     * 1970-01-04 到 1970-01-10 属于第 1 周。
     * 以此类推。
     */
    public final long weekIndex;

    /**
     * 相对于 month 的自然天，取值是 1 - 31.
     * The day of the month. The first day of the month has value 1.
     */
    public final int dayOfMonth;

    /**
     * 相对于 1970-01-01 经历了多少天。
     * 其中 1970-01-01 属于第 0 天。
     * 1970-01-02 属于第 1 天。
     * 以此类推。
     */
    public final long dayIndex;

    /**
     * 相对于 day 的自然小时，取值是 0 - 23.
     * The hour of the day. This field is used for the 24-hour clock.
     * E.g., at 10:04:15.250 PM the hour is 22.
     */
    public final int hourOfDay;

    /**
     * 相对于 1970-01-01 00:00 经历了多少小时。
     */
    public final long hourIndex;

    /**
     * 相对于 hour 的自然分钟数，取值是 0 - 59.
     * The minute of the hour.
     */
    public final int minuteOfHour;

    /**
     * 相对于 minute 的自然秒数，取值是 0 - 59.
     * The second of the minute.
     */
    public final int secondOfMinute;

    /**
     * 相对于 second 的自然毫秒数，取值是 0 - 999.
     * The mills of the second.
     */
    public final int millsOfSecond;



    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    public TimeInfo(long millsFrom1970, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTimeInMillis(millsFrom1970);

        // 原始时间戳
        this.millsFrom1970 = millsFrom1970;

        // 自然年月日时分秒
        this.yearOfAD       = calendar.get(Calendar.YEAR);
        this.monthOfYear    = calendar.get(Calendar.MONTH) + 1;
        this.dayOfMonth     = calendar.get(Calendar.DATE);
        this.hourOfDay      = calendar.get(Calendar.HOUR_OF_DAY);
        this.minuteOfHour   = calendar.get(Calendar.MINUTE);
        this.secondOfMinute = calendar.get(Calendar.SECOND);
        this.millsOfSecond  = calendar.get(Calendar.MILLISECOND);

        // 累计小时数
        final long millsPerHour = 60 * 60 * 1000;
        this.hourIndex = millsFrom1970 / millsPerHour;

        // 累计周数，注意周数不能用 ChronoUnit.WEEKS.between 计算
        this.weekIndex = WeekIndex.get(millsFrom1970, timeZone);

        if (isSupportJava8Api) {
            // 用于计算周数
            final LocalDate origin = LocalDate.of(1970, 1, 1);
            final LocalDate now = LocalDate.of(yearOfAD, monthOfYear, dayOfMonth);

            // 累计天数
            this.dayIndex = ChronoUnit.DAYS.between(origin, now);

            // 累计月数
            this.monthIndex = ChronoUnit.MONTHS.between(origin, now);
        } else {
            /*
             * 1986年至1991年，中华人民共和国在全国范围实行了六年夏令时，每年从4月中旬的第一个星期日2时整(北京时间)到
             * 9月中旬第一个星期日的凌晨2时整(北京夏令时)。除1986年因是实行夏令时的第一年，从5月4日开始到9月14日结束外，
             * 其它年份均按规定的时段施行。夏令时实施期间，将时间向后调快一小时。1992年4月5日后不再实行。
             *
             * 所以 1986-5-5 到 1992-4-5 这期间的 dayIndex 可能不准确。
             */
            // 累计天数
            this.dayIndex = (millsFrom1970 + timeZone.getRawOffset()) / millsPerHour / 24;
            // 累计月数
            this.monthIndex = (this.yearOfAD - 1970) * 12L + (this.monthOfYear - 1);
        }
    }
}
