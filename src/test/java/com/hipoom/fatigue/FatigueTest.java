package com.hipoom.fatigue;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.hipoom.Files;
import org.junit.jupiter.api.Test;

/**
 * @author ZhengHaiPeng
 * @since 2024/5/25 23:01
 */
class FatigueTest {


    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    @Test
    public void test() {
        URL url = getClass().getResource(".");
        if (url == null) {
            throw new RuntimeException("url 不应该是 null.");
        }
        String path = url.getPath();
        File workspace = new File(path, "unit-test");

        // 清理单元测试目录
        Files.delete(workspace);

        testInHours(workspace);
        testInDays(workspace);
        testInWeeks(workspace);
        testInMonths(workspace);
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private void testInHours(File workspace) {
        // 测试 maxTimes(1).inHours(1)
        test1Times1Hour(workspace);

        // 测试 maxTimes(2).inHours(1)
        test2Times1Hour(workspace);

        // 测试 maxTimes(2).inHours(2)
        test2Times2Hour(workspace);
    }

    /**
     * 验证 1 小时内最多 1 次。
     */
    private void test1Times1Hour(File workspace) {
        Fatigue.init(workspace.getAbsolutePath());
        Business business = Fatigue.of("测试1小时内最多1次");
        boolean canTrigger = business.canTrigger(
            FatiguePolicy.maxTimes(1).inHours(1)
        );
        assert canTrigger;

        // 触发时刻
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        final long triggerTimestamp = getTimestamp("2024-05-25 10:00:00.000", zone);

        // 读取时刻
        long judgeTimestamp = getTimestamp("2024-05-25 10:30:00.000", zone);

        // 模拟半小时内读取，不能触发
        business.markTriggerOnce(triggerTimestamp, 30);
        canTrigger = business.canTrigger(judgeTimestamp, FatiguePolicy.maxTimes(1).inHours(1));
        assert !canTrigger;

        // 模拟隔了1小时读取，能够触发
        judgeTimestamp = getTimestamp("2024-05-25 11:30:00.000", zone);
        canTrigger = business.canTrigger(judgeTimestamp, FatiguePolicy.maxTimes(1).inHours(1));
        assert canTrigger;

        System.out.println("测试通过: maxTimes(1).inHours(1)");
    }

    private void test2Times1Hour(File workspace) {
        Fatigue.init(workspace.getAbsolutePath());
        Business business = Fatigue.of("maxTimes(2).inHours(1)");

        // 在没有触发过的时候，能够触发
        boolean canTrigger = business.canTrigger(
            FatiguePolicy.maxTimes(2).inHours(1)
        );
        assert canTrigger;

        // 触发时刻
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        final long triggerTimestamp = getTimestamp("2024-05-25 10:00:00.000", zone);

        // 读取时刻
        long judgeTimestamp = getTimestamp("2024-05-25 10:30:00.000", zone);

        // 标记触发（第一次）
        business.markTriggerOnce(triggerTimestamp, 30);

        // 模拟半小时内读取，能触发
        canTrigger = business.canTrigger(judgeTimestamp, FatiguePolicy.maxTimes(2).inHours(1));
        assert canTrigger;

        // 标记触发（第2次）
        business.markTriggerOnce(triggerTimestamp, 30);

        // 模拟半小时内读取，不能触发
        canTrigger = business.canTrigger(judgeTimestamp, FatiguePolicy.maxTimes(2).inHours(1));
        assert !canTrigger;

        // 模拟隔了1小时读取，能够触发
        judgeTimestamp = getTimestamp("2024-05-25 11:30:00.000", zone);
        canTrigger = business.canTrigger(judgeTimestamp, FatiguePolicy.maxTimes(2).inHours(1));
        assert canTrigger;

        System.out.println("测试通过: maxTimes(2).inHours(1)");
    }

    private void test2Times2Hour(File workspace) {
        Fatigue.init(workspace.getAbsolutePath());
        Business business = Fatigue.of("maxTimes(2).inHours(2)");

        // 在没有触发过的时候，能够触发
        boolean canTrigger = business.canTrigger(
            FatiguePolicy.maxTimes(2).inHours(1)
        );
        assert canTrigger;

        // 触发时刻
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");

        // 标记触发（第一次）
        long triggerTimestamp = getTimestamp("2024-05-25 10:00:00.000", zone);
        business.markTriggerOnce(triggerTimestamp, 30);

        // 模拟半小时内读取，能触发
        long judgeTimestamp = getTimestamp("2024-05-25 10:30:00.000", zone);
        canTrigger = business.canTrigger(judgeTimestamp, FatiguePolicy.maxTimes(2).inHours(2));
        assert canTrigger;

        // 标记触发（第二次）
        triggerTimestamp = getTimestamp("2024-05-25 10:50:00.000", zone);
        business.markTriggerOnce(triggerTimestamp, 30);

        // 模拟 1 小时内再次读取，不能触发
        judgeTimestamp = getTimestamp("2024-05-25 10:51:00.000", zone);
        canTrigger = business.canTrigger(judgeTimestamp, FatiguePolicy.maxTimes(2).inHours(2));
        assert !canTrigger;

        // 模拟 2 小时内再次读取，也不能触发
        judgeTimestamp = getTimestamp("2024-05-25 11:00:00.000", zone);
        canTrigger = business.canTrigger(judgeTimestamp, FatiguePolicy.maxTimes(2).inHours(2));
        assert !canTrigger;

        System.out.println("测试通过: maxTimes(2).inHours(2)");
    }



    private void testInWeeks(File workspace) {
        // maxTimes(1).inWeeks(1)
        test1Times1Week(workspace);

        // maxTimes(2).inWeeks(1)
        test2Times1Week(workspace);
    }

    private void test1Times1Week(File workspace) {
        Fatigue.init(workspace.getAbsolutePath());
        Business business = Fatigue.of("maxTimes(1).inWeeks(1)");

        FatiguePolicyOptions options = FatiguePolicy.maxTimes(1).inWeeks(1);

        // 在没有触发过的时候，能够触发
        boolean canTrigger = business.canTrigger(options);
        assert canTrigger;

        // 触发时刻，模拟周日触发
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        final long triggerTimestamp = getTimestamp("2024-06-02 10:00:00.000", zone);
        business.markTriggerOnce(triggerTimestamp, 30);

        // 模拟周日到周六，不能触发
        for (int i = 2; i <= 8; i++) {
            String des = "2024-06-0" + i + " 10:30:00.000";
            System.out.println("测试: " + des);
            long judgeTimestamp = getTimestamp(des, zone);
            canTrigger = business.canTrigger(judgeTimestamp, options);
            assert !canTrigger;
        }

        // 模拟下个周日，可以触发
        long judgeTimestamp = getTimestamp("2024-06-09 10:30:00.000", zone);
        canTrigger = business.canTrigger(judgeTimestamp, options);
        assert canTrigger;

        // 模拟下个月 2 号，能触发
        judgeTimestamp = getTimestamp("2024-07-02 10:30:00.000", zone);
        canTrigger = business.canTrigger(judgeTimestamp, options);
        assert canTrigger;

        System.out.println("测试通过: maxTimes(1).inWeeks(1)");
    }

    private void test2Times1Week(File workspace) {
        Fatigue.init(workspace.getAbsolutePath());
        Business business = Fatigue.of("maxTimes(2).inWeeks(1)");

        FatiguePolicyOptions options = FatiguePolicy.maxTimes(2).inWeeks(1);

        // 在没有触发过的时候，能够触发
        boolean canTrigger = business.canTrigger(options);
        assert canTrigger;

        // 触发时刻，模拟周日触发
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        long triggerTimestamp = getTimestamp("2024-06-02 10:00:00.000", zone);
        business.markTriggerOnce(triggerTimestamp, 30);

        // 模拟周日到周六，能再触发
        for (int i = 2; i <= 8; i++) {
            String des = "2024-06-0" + i + " 10:30:00.000";
            System.out.println("测试: " + des);
            long judgeTimestamp = getTimestamp(des, zone);
            canTrigger = business.canTrigger(judgeTimestamp, options);
            assert canTrigger;
        }

        // 周日再触发一次
        triggerTimestamp = getTimestamp("2024-06-02 10:30:00.000", zone);
        business.markTriggerOnce(triggerTimestamp, 30);

        // 模拟周日到周六，不能再触发
        for (int i = 2; i <= 8; i++) {
            String des = "2024-06-0" + i + " 11:30:00.000";
            System.out.println("测试: " + des);
            long judgeTimestamp = getTimestamp(des, zone);
            canTrigger = business.canTrigger(judgeTimestamp, options);
            assert !canTrigger;
        }

        // 模拟下个周日，可以触发
        long judgeTimestamp = getTimestamp("2024-06-09 10:30:00.000", zone);
        canTrigger = business.canTrigger(judgeTimestamp, options);
        assert canTrigger;

        // 模拟下个月 2 号，能触发
        judgeTimestamp = getTimestamp("2024-07-02 10:30:00.000", zone);
        canTrigger = business.canTrigger(judgeTimestamp, options);
        assert canTrigger;

        System.out.println("测试通过: maxTimes(2).inWeeks(1)");
    }


    private void testInDays(File workspace) {
        // maxTimes(1).inWeeks(1)
        test1Times1Day(workspace);

        // maxTimes(2).inDays(3)
        test2Times3Day(workspace);
    }

    private void test1Times1Day(File workspace) {
        Fatigue.init(workspace.getAbsolutePath());
        Business business = Fatigue.of("maxTimes(1).inDays(1)");

        FatiguePolicyOptions options = FatiguePolicy.maxTimes(1).inDays(1);

        // 在没有触发过的时候，能够触发
        boolean canTrigger = business.canTrigger(options);
        assert canTrigger;

        // 触发时刻，模拟 2 号触发
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        long triggerTimestamp = getTimestamp("2024-06-02 10:00:00.000", zone);
        business.markTriggerOnce(triggerTimestamp, 30);

        // 2 号不能再次触发
        long judgeTimestamp = getTimestamp("2024-06-02 11:00:00.000", zone);
        canTrigger = business.canTrigger(judgeTimestamp, options);
        assert !canTrigger;

        // 3 号到 19 号，跨天了，能触发
        for (int i = 3; i <= 19; i++) {
            String des = "2024-06-0" + i + " 10:30:00.000";
            System.out.println("测试: " + des);
            judgeTimestamp = getTimestamp(des, zone);
            canTrigger = business.canTrigger(judgeTimestamp, options);
            assert canTrigger;
        }

        System.out.println("测试通过: maxTimes(1).inDays(1)");
    }

    private void test2Times3Day(File workspace) {
        Fatigue.init(workspace.getAbsolutePath());
        Business business = Fatigue.of("maxTimes(2).inDays(3)");

        FatiguePolicyOptions options = FatiguePolicy.maxTimes(2).inDays(3);

        // 在没有触发过的时候，能够触发
        boolean canTrigger = business.canTrigger(options);
        assert canTrigger;

        // 触发时刻，模拟 2 号触发
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        long triggerTimestamp = getTimestamp("2024-06-02 10:00:00.000", zone);
        business.markTriggerOnce(triggerTimestamp, 30);

        // 3 号到 19 号，跨天了，能触发
        for (int i = 2; i <= 19; i++) {
            String des = "2024-06-0" + i + " 10:30:00.000";
            System.out.println("测试: " + des);
            long judgeTimestamp = getTimestamp(des, zone);
            canTrigger = business.canTrigger(judgeTimestamp, options);
            assert canTrigger;
        }

        // 2 号再次触发一次
        triggerTimestamp = getTimestamp("2024-06-02 11:00:00.000", zone);
        business.markTriggerOnce(triggerTimestamp, 30);

        // 2、3、4 三天内不能再次出发
        for (int i = 2; i <= 4; i++) {
            String des = "2024-06-0" + i + " 10:30:00.000";
            System.out.println("测试: " + des);
            long judgeTimestamp = getTimestamp(des, zone);
            canTrigger = business.canTrigger(judgeTimestamp, options);
            assert !canTrigger;
        }

        // 3 天后，能再次触发
        for (int i = 5; i <= 10; i++) {
            String des = "2024-06-0" + i + " 10:30:00.000";
            System.out.println("测试: " + des);
            long judgeTimestamp = getTimestamp(des, zone);
            canTrigger = business.canTrigger(judgeTimestamp, options);
            assert canTrigger;
        }

        System.out.println("测试通过: maxTimes(2).inDays(3)");
    }



    private void testInMonths(File workspace) {
        // maxTimes(1).inMonths(1)
        test1Times1Month(workspace);

        // maxTimes(1).inMonths(1)
        test2Times1Month(workspace);
    }

    private void test1Times1Month(File workspace) {
        Fatigue.init(workspace.getAbsolutePath());
        Business business = Fatigue.of("maxTimes(1).inMonths(1)");

        FatiguePolicyOptions options = FatiguePolicy.maxTimes(1).inMonths(1);

        // 在没有触发过的时候，能够触发
        boolean canTrigger = business.canTrigger(options);
        assert canTrigger;

        // 触发时刻，模拟 1 号触发
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        long triggerTimestamp = getTimestamp("2024-06-01 10:00:00.000", zone);
        business.markTriggerOnce(triggerTimestamp, 30);

        // 1 号到 30 号，同月，不能触发
        for (int i = 1; i <= 30; i++) {
            String des = "2024-06-0" + i + " 10:30:00.000";
            System.out.println("测试: " + des);
            long judgeTimestamp = getTimestamp(des, zone);
            canTrigger = business.canTrigger(judgeTimestamp, options);
            assert !canTrigger;
        }

        System.out.println("测试通过: maxTimes(1).inMonths(1)");
    }

    private void test2Times1Month(File workspace) {
        Fatigue.init(workspace.getAbsolutePath());
        Business business = Fatigue.of("maxTimes(2).inMonths(1)");

        FatiguePolicyOptions options = FatiguePolicy.maxTimes(2).inMonths(1);

        // 在没有触发过的时候，能够触发
        boolean canTrigger = business.canTrigger(options);
        assert canTrigger;

        // 触发时刻，模拟 1 号触发
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        long triggerTimestamp = getTimestamp("2024-06-01 10:00:00.000", zone);
        business.markTriggerOnce(triggerTimestamp, 30);

        // 1 号到 30 号，同月，能触发
        for (int i = 1; i <= 30; i++) {
            String des = "2024-06-0" + i + " 10:30:00.000";
            System.out.println("测试: " + des);
            long judgeTimestamp = getTimestamp(des, zone);
            canTrigger = business.canTrigger(judgeTimestamp, options);
            assert canTrigger;
        }

        // 再触发一次
        triggerTimestamp = getTimestamp("2024-06-01 11:00:00.000", zone);
        business.markTriggerOnce(triggerTimestamp, 30);

        // 1 号到 30 号，同月，不能触发
        for (int i = 1; i <= 30; i++) {
            String des = "2024-06-0" + i + " 11:30:00.000";
            System.out.println("测试: " + des);
            long judgeTimestamp = getTimestamp(des, zone);
            canTrigger = business.canTrigger(judgeTimestamp, options);
            assert !canTrigger;
        }

        System.out.println("测试通过: maxTimes(2).inMonths(1)");
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