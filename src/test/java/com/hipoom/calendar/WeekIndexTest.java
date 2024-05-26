package com.hipoom.calendar;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

/**
 * @author ZhengHaiPeng
 * @since 2024/5/25 19:46
 */
public class WeekIndexTest {

    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    @Test
    public void test() {
        testNeg288000000();
        testNeg259200000();
        testNeg28800000();
        test0();
        test230399999();
        test244800000();
        test259199999();
        test259200000();
        test446400000();
        test863999999();
        test864000000();
        test1716652799999();
        test1716652800000();
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    /**
     * 时间戳: 0
     * UTC+0, 1970-01-01 00:00:00.000
     * UTC+8, 1970-01-01 08:00:00.000
     */
    private void test0() {
        long mills = 0;
        test(mills, 0, 0);
    }

    /**
     * 时间戳: -288000000
     * UTC+0, 1969-12-28 16:00:00.000, index = -1
     * UTC+8, 1969-12-29 00:00:00.000, index = 0
     */
    private void testNeg288000000() {
        long mills = -288000000;
        test(mills, -1, 0);
    }

    /**
     * 时间戳: -259200000
     * UTC+0, 1969-12-29 00:00:00.000
     * UTC+8, 1969-12-29 08:00:00.000
     */
    private void testNeg259200000() {
        long mills = -259200000;
        test(mills, 0, 0);
    }

    /**
     * 时间戳: -28800000
     * UTC+0, 1969-12-31 16:00:00.000
     * UTC+8, 1970-01-01 00:00:00.000
     */
    private void testNeg28800000() {
        long mills = -28800000;
        test(mills, 0, 0);
    }

    /**
     * 时间戳: 244800000
     * UTC+0, 1970-01-03 20:00:00
     * UTC+8, 1970-01-04 04:00:00
     */
    private void test244800000() {
        long mills = 244800000;
        test(mills, 0, 1);
    }

    /**
     * 时间戳: 259199999
     * UTC+0, 1970-01-03 23:59:59.999
     * UTC+8, 1970-01-04 07:59:59.999
     */
    private void test259199999() {
        long mills = 259199999;
        test(mills, 0, 1);
    }

    /**
     * 时间戳: 259200000
     * UTC+0, 1970-01-04 00:00:00.000
     * UTC+8, 1970-01-04 08:00:00.000
     */
    private void test259200000() {
        long mills = 259200000;
        test(mills, 1, 1);
    }

    /**
     * 时间戳: 446400000
     * UTC+0, 1970-01-06 04:00:00.000, index = 1
     * UTC+8, 1970-01-06 12:00:00.000, index = 1
     */
    private void test446400000() {
        long mills = 446400000;
        test(mills, 1, 1);
    }

    /**
     * 时间戳: 864000000
     * UTC+0, 1970-01-11 00:00:00.000, index = 2
     * UTC+8, 1970-01-11 08:00:00.000, index = 2
     */
    private void test864000000() {
        long mills = 864000000;
        test(mills, 2, 2);
    }

    /**
     * 时间戳: 1716652799999L
     * UTC+0, 2024-05-25 15:59:59.999, index = 2838
     * UTC+8, 2024-05-25 23:59:59.999, index = 2838
     */
    private void test1716652799999() {
        long mills = 1716652799999L;
        test(mills, 2838, 2838);
    }

    /**
     * 时间戳: 1716652800000L
     * UTC+0, 2024-05-25 16:00:00.000, index = 2838
     * UTC+8, 2024-05-26 00:00:00.000, index = 2839
     */
    private void test1716652800000() {
        long mills = 1716652800000L;
        test(mills, 2838, 2839);
    }




    /**
     * 时间戳: 863999999
     * UTC+0, 1970-01-10 23:59:59.999, index = 1
     * UTC+8, 1970-01-11 07:59:59.999, index = 2
     */
    private void test863999999() {
        long mills = 863999999;
        test(mills, 1, 2);
    }



    /**
     * 时间戳: 230399999
     * UTC+0, 1970-01-03 15:59:59.999
     * UTC+8, 1970-01-03 23:59:59.999
     */
    private void test230399999() {
        long mills = 230399999;
        test(mills, 0, 0);
    }

    /**
     * @param mills 待测试毫秒数
     * @param expectUTC0Index 预期的 UTC+0 周数
     * @param expectUTC8Index 预期的 UTC+8 周数
     */
    private void test(long mills, long expectUTC0Index, long expectUTC8Index) {
        // UTC+8
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        long index = WeekIndex.get(mills, zone);
        assert index == expectUTC8Index;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(zone);
        System.out.println("测试通过: UTC+8, " + mills + ", " + sdf.format(mills) + ", index = " + index);

        // UTC+0
        zone = TimeZone.getTimeZone("GMT-0:00");
        index = WeekIndex.get(mills, zone);
        assert index == expectUTC0Index;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(zone);
        System.out.println("测试通过: UTC+0, " + mills + ", " + sdf.format(mills) + ", index = " + index);
    }

}