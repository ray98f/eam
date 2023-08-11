package com.wzmtr.eam.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Li.Wang
 * Date: 2023/8/7 16:57
 */
@Slf4j
public class __DateUtil {

    /**
     * -1时，firstDate<secondDate
     * 0,二者相等
     * 1,firstDate>secondDate
     * -99，异常情况
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
