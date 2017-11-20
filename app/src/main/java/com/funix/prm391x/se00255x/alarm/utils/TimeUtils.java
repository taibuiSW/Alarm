package com.funix.prm391x.se00255x.alarm.utils;

import java.util.Calendar;

public class TimeUtils {
    public static long convertTimeToMillis(int hourOfDay, int minute, int second) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        return c.getTimeInMillis();
    }
}
