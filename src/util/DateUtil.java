package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * 时间工具类
 *
 * @since JDK version: 1.8, Created by yuFan on 2018/5/17 0:24
 */
public class DateUtil {
    /**
     * 创建uuid唯一码
     *
     * @return java.lang.String uuid码(唯一码)
     */
    public synchronized static String createGUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * DATETIME(格式:yyyy-MM-dd HH:mm:ss) 用于表示 年-月-日 时:分:秒
     *
     * @return java.lang.String
     */
    public static String getAteTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String atetime = dateFormat.format(date.getTime());
        return atetime;
    }

    /**
     * DATE(格式:yyyy-MM-dd) 用于表示 年-月-日
     *
     * @return java.lang.String
     */
    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowdate = dateFormat.format(date.getTime());
        return nowdate;
    }

    /**
     * TIME(格式:HH:mm:ss) 用于表示 时:分:秒
     *
     * @return java.lang.String
     */
    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String nowtime = dateFormat.format(date.getTime());
        return nowtime;
    }

    /**
     * YEAR(格式:yyyy) 用于表示 年份
     *
     * @param
     * @return java.lang.String
     */
    public static String getYear() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String year = dateFormat.format(date.getTime());
        return year;
    }

    /**
     * ShortAteTime(格式:yyyyMMddHHmmss) 用于表示 年月日时分秒
     *
     * @return java.lang.String
     */
    public static String getShortAteTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String short_atetime = dateFormat.format(date.getTime());
        return short_atetime;
    }

    /**
     * ShortDate(格式:yyyyMMdd) 用于表示 年月日
     *
     * @return java.lang.String
     */
    public static String getShortDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String short_date = dateFormat.format(date.getTime());
        return short_date;
    }

    /**
     * ShortTime (格式:HHmmss) 用于表示 时分秒
     *
     * @return java.lang.String
     */
    public static String getShortTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss");
        String short_time = dateFormat.format(date.getTime());
        return short_time;
    }

    /**
     * 计算日期
     *
     * @param timeUnit 时间单位
     * @param n        数量
     * @return java.lang.String
     */
    public static String calculatingTime(String timeUnit, int n) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        if (timeUnit == null || timeUnit.equals("")) {
            return "0";
        }

        if (timeUnit.equals("year")) {
            cal.add(GregorianCalendar.YEAR, n);// 在日期上加n年
        } else if (timeUnit.equals("month")) {
            cal.add(GregorianCalendar.MONTH, n);// 在日期上加n月
        } else if (timeUnit.equals("day")) {
            cal.add(GregorianCalendar.DATE, n);// 在日期上加n天
        }

        return dateFormat.format(cal.getTime());
    }

}
