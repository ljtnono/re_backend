package cn.lingjiatong.re.common.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 *
 * @author Wang, Haoyue
 * Date: 2020/7/18 3:18 下午
 */
@Slf4j
public class DateUtil {

    /**
     * 获取当前时间字符串
     *
     * @param pattern 样式
     * @return 时间字符串
     */
    public static String getNowString(String pattern) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return simpleDateFormat.format(date);
    }

    /**
     * 获取当前时间的LocalDateTime对象，默认为Asia/Shanghai时区
     *
     * @return LocalDateTime对象
     */
    public static LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
    }


    /**
     * 根据时间获取时间字符串
     *
     * @param date    日期
     * @param pattern 样式
     * @return 时间字符串
     */
    public static String getDateString(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return simpleDateFormat.format(date);
    }

    /**
     * 获取N天后的时间
     *
     * @param day N天后
     * @return 时间
     */
    public static Date getDateAfterNDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + day);
        return calendar.getTime();
    }

    /**
     * 获取N天前的时间
     *
     * @param day N天前
     * @return 时间
     */
    public static Date getDateBeforeNDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - day);
        return calendar.getTime();
    }

    /**
     * 获取当前unix时间戳
     *
     * @return unix时间戳
     */
    public static long getNowUnix() {
        ZoneOffset zoneOffset = ZoneOffset.ofHours(8);
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.toEpochSecond(zoneOffset);
    }

    /**
     * 时间字符串格式化为时间对象，转为Asia/Shanghai时间
     *
     * @param dateStr 时间字符串
     * @param pattern 时间格式化规则
     * @return Date 格式化后的时间对象
     * @author Ling, Jiatong
     */
    public static Date strToDate(String dateStr, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            log.error(e.toString(), e);
            return null;
        }
    }

    /**
     * 计算检测时长（不校验参数）
     * 检测时长s1 = 检测完成时间 - 检测开始时间
     * 1、1s <= s1 <= 60s，结果以x秒格式展示
     * 2、s1 > 60s，结果以x分y秒格式展示
     *
     * @param startDate 检测开始时间
     * @param finishDate 检测结束时间
     * @author Ling, Jiatong
     */
    public static String computeDetectDuration(Date startDate, Date finishDate) {
        long startTimeMillis = startDate.getTime();
        long endTimeMillis = finishDate.getTime();
        if (endTimeMillis - startTimeMillis < (60 * 1000)) {
            return ((endTimeMillis - startTimeMillis) / 1000) + "秒";
        } else {
            long millisToSecond = (endTimeMillis - startTimeMillis) / 1000;
            long minute = millisToSecond / 60;
            long second = millisToSecond % 60;
            return minute + "分" + second + "秒";
        }
    }

    public static void main(String[] args) {
        Date startDate = new Date(2021, 12, 20, 0, 0, 0);
        Date endDate = new Date(2021, 12, 21, 1, 12, 44);
        System.out.println(computeDetectDuration(startDate, endDate));
    }
}
