package com.hipoom.calendar;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author ZhengHaiPeng
 * @since 7/1/24 11:48 PM
 */
class WithoutJava8ApiTest {


   /* ======================================================= */
   /* Public Methods                                          */
   /* ======================================================= */

   @Test
   public void test() {
      long endTimestamp = getTimestamp("2030-12-31 23:59:59.000", TimeZone.getDefault());
      long nextTimestamp = nextTimestamp();
      while(nextTimestamp <= endTimestamp) {
         TimeInfo.isSupportJava8Api = true;
         TimeInfo info1 = new TimeInfo(nextTimestamp, TimeZone.getDefault());
         TimeInfo.isSupportJava8Api = false;
         TimeInfo info2 = new TimeInfo(nextTimestamp, TimeZone.getDefault());

         // 仅 debug 开启下方日志
         // System.out.println(info1.yearOfAD + "-" + info1.monthOfYear + "-" + info1.dayOfMonth + " " + info1.hourOfDay + ", dayIndex1 = " + info1.dayIndex + ", dayIndex2 = " + info2.dayIndex);
         if (info1.dayIndex != info2.dayIndex) {
            System.out.println();
         }
         if (info1.monthIndex != info2.monthIndex) {
            System.out.println();
         }
         nextTimestamp = nextTimestamp();
      }
   }

   static long now = getTimestamp("1992-04-05 00:00:00.000", TimeZone.getDefault());
   public static long nextTimestamp() {
      long temp = now;
      // 每次递增一小时
      now = now + 1000 * 60 * 60;
      return temp;
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
