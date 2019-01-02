package com.wbx.merchant.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/3/12 0012.
 */

public class DateUtil {
    public static String formatDateAndTime2(long date) {
        if (String.valueOf(date).length() == 10) {
            date = date * 1000;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(date));
    }

    public static String formatDateAndTime(long date) {
        if (String.valueOf(date).length() == 10) {
            date = date * 1000;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(new Date(date));
    }

    public static String formatDate(long date) {
        if (String.valueOf(date).length() == 10) {
            date = date * 1000;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date(date));
    }

    public static String getHourAndMinute(long date) {
        if (String.valueOf(date).length() == 10) {
            date = date * 1000;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(date));
    }

    public static String getHour(int seconds) {
        int hour = (seconds / (60 * 60)) % 24;
        String s = String.valueOf(hour);
        if (s.length() < 2) {
            s = "0" + s;
        }
        return s;
    }

    public static String getMinute(int seconds) {
        int minite = (seconds / 60) % 60;
        String s = String.valueOf(minite);
        if (s.length() < 2) {
            s = "0" + s;
        }
        return s;
    }

    public static String getSecond(int seconds) {
        int second = seconds % 60;
        String s = String.valueOf(second);
        if (s.length() < 2) {
            s = "0" + s;
        }
        return s;
    }
}
