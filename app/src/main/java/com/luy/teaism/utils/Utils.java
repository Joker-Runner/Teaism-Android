package com.luy.teaism.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by joker on 7/9 0009.
 */

public class Utils {
    /**
     * IP 和端口号
     */
//    private static final String IP = "123.206.201.169";
    private static final String IP = "192.168.0.2";
    private static final String PORT = "8080";

    // 获取URL(获取到项目根目录)
    public static String getURL() {
        return "http://" + IP + ":" + PORT + "/Teaism";
    }

    // 日期格式
    private static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期格式转换
     */
    public static String parseToDate(long timeStamp) {
        Timestamp ts = new Timestamp(timeStamp);
        DateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        return dateFormat.format(ts);
    }

    /**
     * 只能日期转换
     *
     * @param timeStamp
     * @return
     */
    public static String parseToTime(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        Calendar calendarCurrent = Calendar.getInstance();
        calendarCurrent.setTimeInMillis(System.currentTimeMillis());
        if (calendar.get(Calendar.YEAR) == calendarCurrent.get(Calendar.YEAR)) {
            if (calendar.get(Calendar.MONTH) == calendarCurrent.get(Calendar.MONTH)) {
//                if (calendar.get(Calendar.WEEK_OF_MONTH) == calendarCurrent.get(Calendar.WEEK_OF_MONTH)) {
//                if (calendarCurrent.get(Calendar.DAY_OF_MONTH)-calendar.get(Calendar.DAY_OF_MONTH)>7){
//
//                }
                /**
                 * 8：12  7：47
                 * 8：53  7：25
                 */
                if (calendar.get(Calendar.DAY_OF_MONTH) == calendarCurrent.get(Calendar.DAY_OF_MONTH)) {
                    if (calendar.get(Calendar.HOUR_OF_DAY) == calendarCurrent.get(Calendar.HOUR_OF_DAY)) {
                        if (calendar.get(Calendar.MINUTE) == calendarCurrent.get(Calendar.MINUTE)) {
                            return "刚刚";
                        } else {
                            return calendarCurrent.get(Calendar.MINUTE) - calendar.get(Calendar.MINUTE) + "分钟前";
                        }
                    } else if (calendarCurrent.get(Calendar.HOUR_OF_DAY) - calendar.get(Calendar.HOUR_OF_DAY) == 1) {
                        if (calendarCurrent.get(Calendar.MINUTE) >= calendar.get(Calendar.MINUTE)) {
                            return "1小时前";
                        } else {
                            return calendarCurrent.get(Calendar.MINUTE) + 60 - calendar.get(Calendar.MINUTE) + "分钟前";
                        }
//                        return calendarCurrent.get(Calendar.HOUR_OF_DAY) - calendar.get(Calendar.HOUR_OF_DAY) + "小时前";
                    } else {
                        return calendarCurrent.get(Calendar.HOUR_OF_DAY) - calendar.get(Calendar.HOUR_OF_DAY) + "小时前";
                    }
//                    return calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
                } else if (calendarCurrent.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH) == 1) {
                    return "昨天";
                } else {
                    return calendarCurrent.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH) + "天前";
                }
//                } else {
//                    switch (calendarCurrent.get(Calendar.WEEK_OF_MONTH) - calendar.get(Calendar.WEEK_OF_MONTH)) {
//                        case 0
//                    }
//                }
            } else {
                return calendar.get(Calendar.MONTH) + 1 + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
            }
        } else {
            return calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月"
                    + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        }
    }
}
