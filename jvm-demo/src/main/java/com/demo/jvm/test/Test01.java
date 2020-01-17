package com.demo.jvm.test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

/**
 * @author weiqiang
 * @date 2020/1/2 14:47
 * @decription
 * @updateInformaion
 */
public class Test01 {
    public static void main(String[] args) {

//        LocalDateTime dateByYearAndWeekNumAndDayOfWeek = getDateByYearAndWeekNumAndDayOfWeek(2020, 2, DayOfWeek.MONDAY);
//        System.err.println(dateByYearAndWeekNumAndDayOfWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

//        for (int i = 1; i < 100; i++) {
//            String code = getCode(i++, 10, "0");
//            System.err.println(i + "--"    +code);
//        }

//        LocalDateTime startTime = getDateByYearAndWeekNumAndDayOfWeek(2020, LocalDate.of(2020, 1,6)
//                .get(ChronoField.ALIGNED_WEEK_OF_YEAR), DayOfWeek.MONDAY);
//        // 当前周的最后一天
//        LocalDateTime endTime = getDateByYearAndWeekNumAndDayOfWeek(2020, LocalDate.of(2020, 1,6)
//                .get(ChronoField.ALIGNED_WEEK_OF_YEAR), DayOfWeek.SUNDAY);
//        System.err.println(startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        System.err.println(endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


        String s1 = "2019-09-09 00:00:00";
        String s2 = "2019-09-09 00:00:02";
        System.err.println(s1.hashCode());
        System.err.println(s2.hashCode());
    }


    private static LocalDateTime getDateByYearAndWeekNumAndDayOfWeek(Integer year, Integer num, DayOfWeek dayOfWeek) {
        String numStr = num < 10 ? "0" + String.valueOf(num) : String.valueOf(num);
        //2019-W01-01获取第一周的周一日期，2019-W02-07获取第二周的周日日期
        String weekDate = String.format("%s-W%s-%s", year, numStr, dayOfWeek.getValue());
        LocalDate date = LocalDate.parse(weekDate, DateTimeFormatter.ISO_WEEK_DATE);
        String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String dateTimeString = dateString.concat(" 00:00:00");

        int day = LocalDate.of(LocalDate.now().getYear(), 1, 1).getDayOfWeek().getValue();
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static String getCode(int num, int length, String replaceString) {
        String numString = String.valueOf(num);
        if (numString.length() >= length) {
            return numString;
        }
        int differLength = length - numString.length();
        StringBuilder appendBeforeSb = new StringBuilder();
        for (int i = 0; i < differLength; i++) {
            appendBeforeSb.append(replaceString);
        }
        appendBeforeSb.append(numString);
        return String.valueOf(appendBeforeSb);
    }

    private static int getWeekOfYear(int year, LocalDate date) {
        LocalDate nowFirstDay = LocalDate.of(year, 1, 1);
        int differ = nowFirstDay.getDayOfWeek().getValue() - 1;
        int dayOfYear = date.getDayOfYear();
        int diffAllDay = dayOfYear + differ;
        return diffAllDay % 7 == 0 ? diffAllDay / 7 : diffAllDay / 7 + 1;
    }

}
