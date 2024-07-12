package com.hipoom.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

import com.hipoom.calendar.TimeInfo;
import org.junit.jupiter.api.Test;

/**
 * @author ZhengHaiPeng
 * @since 2024/5/19 21:56
 */
class TimeInfoTest {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    public static final long MILLS_PER_HOUR = 60 * 60 * 1000;

    public static final long MILLS_PER_DAY = MILLS_PER_HOUR * 24;



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    @Test
    public void test() {
        TimeInfo.isSupportJava8Api = true;
        testOrigin();
        test19700102();
        test19700104();
        test19700104100000000();
        test19700110100000000();
        test19700210100000000();
        test20240525100000000();
        test20240526000000000();
    }

    @Test
    public void testWithoutJava8Api() {
        // 用于计算周数
        final LocalDate origin = LocalDate.of(1970, 1, 1);
        final LocalDate now = LocalDate.of(1970, 1, 1);

        // 累计天数
        long dayIndex = ChronoUnit.DAYS.between(origin, now);
        System.out.println("dayIndex: " + dayIndex);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse("1970-01-01");
            long timestamp = date.getTime();
            long dayIndex2 = timestamp / 1000 / 60 / 60 / 24;
            System.out.println("dayIndex2: " + dayIndex2);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        TimeInfo.isSupportJava8Api = false;
        testOrigin();
        test19700102();
        test19700104();
        test19700104100000000();
        test19700110100000000();
        test19700210100000000();
        test20240525100000000();
        test20240526000000000();
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    /**
     * 测试原点是否有异常。
     */
    private static void testOrigin() {
        TimeZone zone = TimeZone.getDefault();
        TimeInfo info = new TimeInfo(0, TimeZone.getDefault());
        assert info.millsFrom1970 == 0;
        assert info.yearOfAD == 1970;
        assert info.monthOfYear == 1;
        assert info.monthIndex == 0;
        assert info.weekIndex == 0;
        assert info.dayOfMonth == 1;
        assert info.dayIndex == 0;
        assert info.hourOfDay == zone.getRawOffset() / 1000 / 60 / 60;
        assert info.hourIndex == 0;
        assert info.minuteOfHour == 0;
        assert info.secondOfMinute == 0;
        assert info.millsOfSecond == 0;
        System.out.println("原点测试成功.");
    }

    /**
     * 测试 北京时间 1970-01-02 20:00:00 的数据。
     */
    private static void test19700102() {
        long mills = MILLS_PER_DAY + MILLS_PER_HOUR * 12;
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        TimeInfo info = new TimeInfo(mills, zone);
        assert info.millsFrom1970 == mills;
        assert info.yearOfAD == 1970;
        assert info.monthOfYear == 1;
        assert info.monthIndex == 0;
        assert info.weekIndex == 0;
        assert info.dayOfMonth == 2;
        assert info.dayIndex == 1;
        assert info.hourOfDay == 20;
        assert info.hourIndex == 36;
        assert info.minuteOfHour == 0;
        assert info.secondOfMinute == 0;
        assert info.millsOfSecond == 0;
        System.out.println("北京时间 1970-01-02 20:00:00 测试成功.");
    }

    /**
     * 测试 北京时间 1970-01-04 10:00:00 的数据。
     */
    private static void test19700104() {
        long mills = 3 * MILLS_PER_DAY + MILLS_PER_HOUR * 2;
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        TimeInfo info = new TimeInfo(mills, zone);
        assert info.millsFrom1970 == mills;
        assert info.yearOfAD == 1970;
        assert info.monthOfYear == 1;
        assert info.monthIndex == 0;
        assert info.weekIndex == 1;
        assert info.dayOfMonth == 4;
        assert info.dayIndex == 3;
        assert info.hourOfDay == 10;
        assert info.hourIndex == (3 * 24 + 2);
        assert info.minuteOfHour == 0;
        assert info.secondOfMinute == 0;
        assert info.millsOfSecond == 0;
        System.out.println("北京时间 1970-01-04 10:00:00 测试成功.");
    }


    /**
     * 测试 北京时间 1970-01-04 10:00:00 的数据。
     */
    private void test19700104100000000() {
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        final long mills = getTimestamp("1970-01-04 10:00:00.000", zone);

        TimeInfo info = new TimeInfo(mills, zone);
        assert info.millsFrom1970 == mills;
        assert info.yearOfAD == 1970;
        assert info.monthOfYear == 1;
        assert info.monthIndex == 0;
        assert info.weekIndex == 1;
        assert info.dayOfMonth == 4;
        assert info.dayIndex == 3;
        assert info.hourOfDay == 10;
        assert info.hourIndex == (3 * 24 + 2);
        assert info.minuteOfHour == 0;
        assert info.secondOfMinute == 0;
        assert info.millsOfSecond == 0;
        System.out.println("北京时间 1970-01-04 10:00:00 测试成功.");
    }

    /**
     * 测试 北京时间 1970-01-10 10:00:00 的数据。
     */
    private void test19700110100000000() {
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        final long mills = getTimestamp("1970-01-10 10:00:00.000", zone);

        TimeInfo info = new TimeInfo(mills, zone);
        assert info.millsFrom1970 == mills;
        assert info.yearOfAD == 1970;
        assert info.monthOfYear == 1;
        assert info.monthIndex == 0;
        assert info.weekIndex == 1;
        assert info.dayOfMonth == 10;
        assert info.dayIndex == 9;
        assert info.hourOfDay == 10;
        assert info.hourIndex == (9 * 24 + (10 - 8));
        assert info.minuteOfHour == 0;
        assert info.secondOfMinute == 0;
        assert info.millsOfSecond == 0;
        System.out.println("北京时间 1970-01-10 10:00:00.000 测试成功.");
    }

    /**
     * 测试 北京时间 1970-02-10 10:00:00.000 的数据。
     */
    private void test19700210100000000() {
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        final long mills = getTimestamp("1970-02-10 10:00:00.000", zone);

        TimeInfo info = new TimeInfo(mills, zone);
        assert info.millsFrom1970 == mills;
        assert info.yearOfAD == 1970;
        assert info.monthOfYear == 2;
        assert info.monthIndex == 1;
        assert info.weekIndex == 6;
        assert info.dayOfMonth == 10;
        assert info.dayIndex == 40;
        assert info.hourOfDay == 10;
        assert info.hourIndex == ((31 + 9) * 24 + (10 - 8));
        assert info.minuteOfHour == 0;
        assert info.secondOfMinute == 0;
        assert info.millsOfSecond == 0;
        System.out.println("北京时间 1970-01-10 10:00:00.000 测试成功.");
    }

    /**
     * 测试 北京时间 1970-02-10 10:00:00.000 的数据。
     */
    private void test20240525100000000() {
        String des = "2024-05-25 10:00:00.000";
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        final long mills = getTimestamp(des, zone);

        TimeInfo info = new TimeInfo(mills, zone);
        assert info.millsFrom1970 == mills;
        assert info.yearOfAD == 2024;
        assert info.monthOfYear == 5;
        assert info.monthIndex == 652;
        assert info.weekIndex == 2838;
        assert info.dayOfMonth == 25;
        assert info.dayIndex == 19868;
        assert info.hourOfDay == 10;
        assert info.hourIndex == 476834;
        assert info.minuteOfHour == 0;
        assert info.secondOfMinute == 0;
        assert info.millsOfSecond == 0;
        System.out.println("北京时间 " + des + " 测试成功.");
    }

    /**
     * 测试 北京时间 1970-02-10 10:00:00.000 的数据。
     */
    private void test20240526000000000() {
        String des = "2024-05-26 00:00:00.000";
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        final long mills = getTimestamp(des, zone);

        TimeInfo info = new TimeInfo(mills, zone);
        assert info.millsFrom1970 == mills;
        assert info.yearOfAD == 2024;
        assert info.monthOfYear == 5;
        assert info.monthIndex == 652;
        assert info.weekIndex == 2839;
        assert info.dayOfMonth == 26;
        assert info.dayIndex == 19869;
        assert info.hourOfDay == 0;
        assert info.hourIndex == 476848;
        assert info.minuteOfHour == 0;
        assert info.secondOfMinute == 0;
        assert info.millsOfSecond == 0;
        System.out.println("北京时间 " + des + " 测试成功.");
    }


    private static long getTimestamp(String format, TimeZone zone) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(zone);
        try {
            Date date = sdf.parse(format);
            return date.getTime();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
