package com.ben.common.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import static com.ben.common.constant.CommonConstant.UTILITY_CLASS;

/**
 * @author lomofu
 * @date 2020/2/14 19:53
 */
public class TimeHelper {

  public static final ZoneId zoneId = ZoneId.systemDefault();

  public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  public static final DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private TimeHelper() {
    throw new IllegalStateException(UTILITY_CLASS);
  }

  public static LocalDateTime converseDateToLocalDateTime(Date date) {
    return date.toInstant().atZone(zoneId).toLocalDateTime();
  }

  public static Date converseLocalDateTimeToDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(zoneId).toInstant());
  }

  public static String converseDateToString(Date date) {
    return formatter.format(converseDateToLocalDateTime(date));
  }

  public static String converseSimpleDateToString(Date date) {
    return simpleFormatter.format(converseDateToLocalDateTime(date));
  }

  public static Date converseStringToDate(String date) {
    return converseLocalDateTimeToDate(LocalDateTime.parse(date, formatter));
  }

  public static boolean compareTwoDateIsEquals(Date date1, Date date2) {
    LocalDateTime localDateTime = converseDateToLocalDateTime(date1);
    LocalDateTime localDateTime1 = converseDateToLocalDateTime(date2);
    return localDateTime.isEqual(localDateTime1);
  }

  public static Date addDay(Date date, long days) {
    LocalDateTime localDateTime = converseDateToLocalDateTime(date);
    return converseLocalDateTimeToDate(localDateTime.plusDays(days));
  }

  public static Date addHours(Date date, long hours) {
    LocalDateTime localDateTime = converseDateToLocalDateTime(date);
    return converseLocalDateTimeToDate(localDateTime.plusHours(hours));
  }

  public static Date addMinutes(Date date, long minutes) {
    LocalDateTime localDateTime = converseDateToLocalDateTime(date);
    return converseLocalDateTimeToDate(localDateTime.plusMinutes(minutes));
  }

  public static Date nowWeekStart() {
    LocalDate now = LocalDate.now();
    String week = String.valueOf(now.getDayOfWeek());
    if (week.equals("SUNDAY")) {
      return converseLocalDateTimeToDate(now.atStartOfDay());
    }
    LocalDateTime localDateTime =
        now.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).atStartOfDay();
    return converseLocalDateTimeToDate(localDateTime);
  }

  public static Date nowWeekEnd() {
    LocalDate now = LocalDate.now();
    String week = String.valueOf(now.getDayOfWeek());
    if (week.equals("SATURDAY")) {
      return converseLocalDateTimeToDate(now.atTime(23, 59, 59));
    }
    LocalDateTime localDateTime =
        now.with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).atTime(23, 59, 59);
    return converseLocalDateTimeToDate(localDateTime);
  }
}
