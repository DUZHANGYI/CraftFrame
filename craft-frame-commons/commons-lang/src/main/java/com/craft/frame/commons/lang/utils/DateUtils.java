package com.craft.frame.commons.lang.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author DURR
 * @desc 日期工具类
 * @date 2023/6/28 22:54
 */
@SuppressWarnings("unused")
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取传入日期的开始时间和结束时间
	 *
	 * @param date 日期
	 * @return 传入 2023-6-28 返回[2023-6-28 00:00:00,2023-6-28 23:59:59]
	 */
	public static LocalDateTime[] getDayBoundary(LocalDate date) {
		assert date != null : "date cannot be null";
		LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.MIN);
		LocalDateTime endDateTime = LocalDateTime.of(date, LocalTime.MAX);
		return new LocalDateTime[]{startDateTime, endDateTime};
	}

	public static LocalDateTime[] getDayBoundary(Date date){
		LocalDate localDate = convertToLocalDate(date);
		return getDayBoundary(localDate);
	}

	public static Date[] getDayBoundaryToDate(LocalDate date) {
		LocalDateTime[] dates = getDayBoundary(date);
		Date startDate = convertToDate(dates[0]);
		Date endDate = convertToDate(dates[1]);
		return new Date[]{startDate, endDate};
	}

	public static Date[] getDayBoundaryToDate(Date date){
		LocalDate localDate = convertToLocalDate(date);
		return getDayBoundaryToDate(localDate);
	}

	public static String[] getDayBoundaryToStr(LocalDate date) {
		LocalDateTime[] times = getDayBoundary(date);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
		String startBoundary = times[0].format(formatter);
		String endBoundary = times[1].format(formatter);
		return new String[]{startBoundary, endBoundary};
	}

	public static String[] getDayBoundaryToStr(Date date){
		LocalDate localDate = convertToLocalDate(date);
		return getDayBoundaryToStr(localDate);
	}

	/**
	 * 获取两个时间段之间的所有时间
	 *
	 * @param startDate 起始时间
	 * @param endDate   结束时间
	 * @return 时间间隙
	 */
	public static List<LocalDate> getDateRange(LocalDate startDate, LocalDate endDate) {
		assert startDate != null : "startDate cannot be null";
		assert endDate != null : "endDate cannot be null";
		List<LocalDate> datesInRange = new ArrayList<>();
		LocalDate currentDate = startDate;
		while (!currentDate.isAfter(endDate)) {
			datesInRange.add(currentDate);
			currentDate = currentDate.plusDays(1);
		}
		return datesInRange;
	}

	public static List<LocalDate> getDateRange(Date startDate, Date endDate) {
		assert startDate != null : "startDate cannot be null";
		assert endDate != null : "endDate cannot be null";
		LocalDate start = convertToLocalDate(startDate);
		LocalDate end = convertToLocalDate(endDate);
		return getDateRange(start, end);
	}

	public static LocalDate convertToLocalDate(Date date) {
		assert date != null : "date cannot be null";
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime convertToLocalDateTime(Date date) {
		assert date != null : "date cannot be null";
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static Date convertToDate(LocalDate date) {
		assert date != null : "date cannot be null";
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static Date convertToDate(LocalDateTime date) {
		assert date != null : "date cannot be null";
		return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	}

}
