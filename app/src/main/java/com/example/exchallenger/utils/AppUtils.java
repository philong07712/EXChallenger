package com.example.exchallenger.utils;

import java.util.Arrays;

public class AppUtils {
    public static String getScheduleText(int[] repeat) {
        int sum = 0;

        for (int value : repeat) {
            sum += value;

        }
        if (sum == 0) {
            return "Once";
        }
        if (sum == 7) {
            return "Everyday";
        }
        if (sum == repeat[0] + repeat[6]) {
            return "Weekend";
        }
        if (sum == 5 && (repeat[0] + repeat[6] == 0)) {
            return "Weekdays";
        }
        StringBuilder concat = new StringBuilder();
        if (repeat[0] == 1) {
            concat.append("SU, ");
        }
        if (repeat[1] == 1) {
            concat.append("MO, ");
        }
        if (repeat[2] == 1) {
            concat.append("TU, ");
        }
        if (repeat[3] == 1) {
            concat.append("WE, ");
        }
        if (repeat[4] == 1) {
            concat.append("TH, ");
        }
        if (repeat[5] == 1) {
            concat.append("FR, ");
        }
        if (repeat[6] == 1) {
            concat.append("SA, ");
        }
        int lastComma = concat.lastIndexOf(", ");
        if (lastComma > 0) {
            concat.delete(lastComma, lastComma + 2);
        }
        return concat.toString();
    }

    public static String getTimeFromHourAndUnit(int hour, int minute) {
        String hourStr = hour > 9 ? (hour + "") : ("0" + hour);
        String minStr = minute > 9 ? (minute + "") : ("0" + minute);

        return hourStr + ":" + minStr;
    }
}