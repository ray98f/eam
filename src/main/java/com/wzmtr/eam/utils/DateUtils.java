package com.wzmtr.eam.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/12
 */
@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDD = "yyyyMMdd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String[] PARSE_PATTERNS = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    /**
     * 返回当前时间的格式为YYYY_MM_DD_HH_MM_SS
     * @return 当前时间
     */
    public static String getCurrentTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 返回当前时间的格式为YYYYMMDD
     * @return 当前时间
     */
    public static String getNoDate() {
        return dateTimeNow(YYYYMMDD);
    }

    public static String getNoCurrentTime() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String dateTime() {
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
            return parseDate(str.toString(), PARSE_PATTERNS);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 校验日期是否是符合指定格式的合法日期
     * @param date 日期
     * @param length 日期的长度
     * @param format 日期的格式
     * @return 是否符合
     */
    public static boolean isLegalDate(String date, int length, String format) {
        if (date == null || date.length() != length) {
            return false;
        }
        try {
            DateFormat formatter = new SimpleDateFormat(format);
            // 设置lenient为false
            formatter.setLenient(false);
            Date date1 = formatter.parse(date);
            log.info("入参：" + date + ";转换后日期：" + formatter.format(date1));
            return date.equals(formatter.format(date1));
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 比较两个相同格式的字符串时间
     * @param firstDate  第一个时间
     * @param secondDate 第二个时间
     * @param pattern    时间格式
     * @return 时间大小状态
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
     * 获取当前时间加减月份后的日期
     * @return 日期
     */
    public static String getDayByMonth(int monthNum) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, monthNum);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 给时间加上几个小时
     * @param time 当前时间 格式：yyyy-MM-dd HH:mm:ss
     * @param hour 需要加的时间
     * @return 更新后的时间
     */
    public static String addDateHour(String time, int hour) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (date == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 24小时制
        cal.add(Calendar.HOUR, hour);
        date = cal.getTime();
        return sdf.format(date);
    }

    /**
     * 给日期加上几个月
     * @param day   当前时间 格式：yyyy-MM-dd
     * @param month 需要加的时间
     * @return 更新后的时间
     */
    public static String addMonthDay(String day, int month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(day);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (date == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);
        date = cal.getTime();
        return sdf.format(date);
    }

    /**
     * 获取两个日期之间的所有月份 (年月)
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 月份
     */
    public static List<String> getMonthBetweenDate(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // 声明保存日期集合
        List<String> list = new ArrayList<>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()) {
                // 把日期添加到集合
                list.add(sdf.format(startDate));
                // 设置日期
                calendar.setTime(startDate);
                //把日期增加一天
                calendar.add(Calendar.MONTH, 1);
                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (ParseException e) {
            log.error("exception message", e);
        }
        return list;
    }

    /**
     * 获取两个日期之间的所有日期
     * @param startDateStr 开始日期
     * @param endDateStr 结束日期
     * @return 之间的所有日期
     */
    public static List<String> getDatesBetween(String startDateStr, String endDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);
        List<String> dates = new ArrayList<>();
        //这里是判断开始日期是否在结束日期之后或者=结束日期
        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            String add = startDate.format(formatter);
            dates.add(add);
            startDate = startDate.plusDays(1);
        }
        dates.add(endDateStr);
        return dates;
    }

    /**
     * 获取日期范围内，指定日期的固定步长日期的所有日期
     * @param startStr 开始时间字符串
     * @param endStr 结束时间字符串
     * @param intervalDays 步长日期
     * @return 日期列表
     */
    public static List<String> getAllTimesWithinRange(String startStr, String endStr, int intervalDays) {
        List<String> times = new ArrayList<>();
        LocalDate start = string2LocalDate(startStr);
        LocalDate end = string2LocalDate(endStr);
        LocalDate current = start;
        while (current.isBefore(end)) {
            times.add(localDate2String(current));
            current = current.plusDays(intervalDays);
        }
        return times;
    }

    /**
     * LocalDateTime转String
     * @param time LocalDate
     * @return String
     */
    public static String localDate2String(LocalDate time) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return time.format(df);
    }

    /**
     * String转LocalDate
     * @param dateStr 日期字符串
     * @return LocalDate
     */
    public static LocalDate string2LocalDate(String dateStr) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, df);
    }

    /**
     * 指定时间往前或往后移动x天
     * @param specifiedDay 指定时间
     * @param x 指定天数
     * @return 移动后的日期
     * @throws ParseException 异常
     */
    public static String getDayBefore(String specifiedDay, int x) throws ParseException {
        Calendar c = Calendar.getInstance();
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        // 移动x天
        c.set(Calendar.DATE, day + x);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    /**
     * 日期增加指定天数
     * @param day 日期
     * @param i 天数
     * @return 修改后的日期
     * @throws ParseException 异常
     */
    public static String addDayDay(String day, int i) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(sdf.parse(day).getTime() + 24L * 3600L * 1000L * i);
        return sdf.format(d);
    }

    /**
     * 获取两个日期之间的天数间隔
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return 天数间隔
     */
    public static Long getDayBetweenDays(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(startTime, formatter);
        LocalDate date2 = LocalDate.parse(endTime, formatter);
        return Duration.between(date1.atStartOfDay(), date2.atStartOfDay()).toDays();
    }

    /**
     * 判断一个日期是否在两个日期之间（包含开始时间，不包含结束时间）
     * @param date 检测日期
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 是否
     * @throws ParseException 异常
     */
    public static boolean getDateBetweenContainStartExcludeEnd(String date, String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        Date dateToCheck = sdf.parse(date);
        return (dateToCheck.after(start) && dateToCheck.before(end)) || dateToCheck.equals(start);
    }

    /**
     * 获取入参所在季度的第一天
     * @param date 日期
     * @return 季度第一天
     */
    public static Date getQuarterStart(Date date) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        startCalendar.set(Calendar.MONTH, ((startCalendar.get(Calendar.MONTH)) / 3) * 3);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return startCalendar.getTime();
    }

    /**
     * 获取入参所在季度的最后一天
     * @param date 日期
     * @return 季度最后一天
     */
    public static Date getQuarterEnd(Date date) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(date);
        endCalendar.set(Calendar.MONTH, ((endCalendar.get(Calendar.MONTH)) / 3) * 3 + 2);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return endCalendar.getTime();
    }

    /**
     * 	获取当日最小时间（0时0分0秒）
     * 	@return 当日最小时间
     */
    public static LocalDateTime getCurrentDayStart() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    /**
     * 	获取当日最大时间（23时59分59秒）
     * 	@return 当日最大时间
     */
    public static LocalDateTime getCurrentDayEnd() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

}
