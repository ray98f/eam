package com.wzmtr.eam.utils;

import com.wzmtr.eam.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Li.Wang
 * Date: 2023/8/7 16:57
 */
@Slf4j
public class DateUtil extends org.apache.commons.lang3.time.DateUtils {

    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    /**
     * 返回当前时间的格式为YYYY_MM_DD_HH_MM_SS
     * @return
     */
    public static final String getCurrentTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    public static Date getMonthNewDate(Date oldDate, Integer recordDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String data = format.format(oldDate);
        String[] dataStr = data.split("-");
        int year = (Integer.parseInt(dataStr[1]) + recordDate) / 12;
        int month = (Integer.parseInt(dataStr[1]) + recordDate) % 12;
        String a;
        if (month < CommonConstants.TEN) {
            if (month < 1) {
                a = "12";
            } else {
                a = "0" + month;
            }
        } else {
            a = month + "";
        }
        dataStr[0] = String.valueOf(Integer.parseInt(dataStr[0]) + year);
        dataStr[1] = a;
        String newData = dataStr[0] + "-" + dataStr[1] + "-" + dataStr[2];
        return format.parse(newData);
    }

    /**
     * -1时，firstDate<secondDate
     * 0,二者相等
     * 1,firstDate>secondDate
     * -99，异常情况
     *
     * @param firstDate
     * @param secondDate
     * @param pattern
     * @return
     */
    public static Integer dateCompare(String firstDate, String secondDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date d1 = sdf.parse(firstDate);
            Date d2 = sdf.parse(secondDate);
            return Long.compare(d1.getTime(), d2.getTime());
        } catch (Exception e) {
            log.error("compare err", e);
            return -99;
        }
    }

    /**
     * 获取系统当前时间戳(秒)
     */
    public static final long current() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * @param format
     * @return
     */
    public static final String current(String format) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(format);
        return dateTimeFormat.format(new Date());
    }


    public static void main(String[] args) throws ParseException {
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Date d1 = sdf.parse("2023-08-07 19:29:27");
        // Date d2 = sdf.parse("2024-08-07 19:29:27");
        // System.out.println(d1.getTime());
        // System.out.println(d2.getTime());
        // int compare = Long.compare(d1.getTime(), d2.getTime());
        // System.out.println(compare);
        System.out.println(current("yyyy-MM-dd HH:mm:ss"));
    }
}
