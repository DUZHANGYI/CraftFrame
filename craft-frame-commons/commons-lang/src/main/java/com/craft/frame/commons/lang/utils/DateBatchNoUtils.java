package com.craft.frame.commons.lang.utils;

import com.craft.frame.commons.lang.utils.exception.DateParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DURR
 * @desc 日期批次号工具类
 * @date 2023/6/29 13:15
 */

@SuppressWarnings("unused")
public class DateBatchNoUtils {

	public enum TimeDiff {
		YEAR(Calendar.YEAR),
		MONTH(Calendar.MONTH),
		DATE(Calendar.DATE),
		HOUR(Calendar.HOUR),
		MINUTE(Calendar.MINUTE),
		SECOND(Calendar.SECOND),
		MILLISECOND(Calendar.MILLISECOND),
		;
		private final int calVal;

		TimeDiff(int calVal) {
			this.calVal = calVal;
		}
	}

	public enum BatchNoType {
		MON("yyyy%sMM"),
		DAY("yyyy%sMM%sdd"),
		HOUR("yyyy%sMM%sdd%sHH"),
		;

		private final String dateFmt;

		BatchNoType(String dateFmt) {
			this.dateFmt = dateFmt;
		}

		public String toFmt() {
			return this.toFmt(null);
		}

		public String toFmt(String split) {
			if (split == null) {
				split = "";
			}
			return StringUtils.replace(this.dateFmt, "%s", split);
		}

		public SimpleDateFormat toSimpleDateFmt(String split) {
			return new SimpleDateFormat(toFmt(split));
		}
	}

	private final static long UNIT_MILL_DAY = 24 * 60 * 60 * 1000;

	private static final Map<String, SimpleDateFormat> fmtMap = new HashMap<>();

	private final static String UNDERLINE = "_";

	private static SimpleDateFormat getFmt(BatchNoType type, String split) {
		String fmt = type.toFmt(split);
		SimpleDateFormat sdf = fmtMap.get(fmt);
		if (sdf != null) {
			return sdf;
		}
		sdf = new SimpleDateFormat(fmt);
		fmtMap.put(fmt, sdf);
		return sdf;
	}

	public static int dateYear(Date date) {
		if (date == null) {
			return 1970;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public static String dateToBatchNo(BatchNoType type, String split, Date date) {
		if (type == null || date == null) {
			return null;
		}
		SimpleDateFormat sdf = getFmt(type, split);
		return sdf.format(date);
	}

	public static int dateToBatchNum(BatchNoType type, Date date) {
		return NumberUtils.toInt(dateToBatchNo(type, "", date));
	}

	private static Date dateDiff(Date date, TimeDiff diffType, int diff) {
		if (diff == 0) {
			return date;
		}
		Calendar calendar = Calendar.getInstance();
		if (date == null) {
			date = new Date();
		}
		calendar.setTime(date);
		if (diffType != null) {
			calendar.add(diffType.calVal, diff);
		}
		return calendar.getTime();
	}

	public static Date parseFromBatchNo(BatchNoType type, int batchNum) {
		return parseFromBatchNo(type, null, String.valueOf(batchNum));
	}

	public static Date parseFromBatchNo(BatchNoType type, String split, String batchNo) {
		SimpleDateFormat sdf = getFmt(type, split);
		try {
			return sdf.parse(batchNo);
		} catch (ParseException e) {
			throw new DateParseException(e);
		}
	}

	public static int batchNumDiff(BatchNoType type, TimeDiff diffType, int batchNum, int diff) {
		if (type == null || diffType == null || diff == 0) {
			return batchNum;
		}
		Date date = dateDiff(parseFromBatchNo(type, batchNum), diffType, diff);
		return dateToBatchNum(type, date);
	}

	public static int BatchDiffDays(BatchNoType type, int start, int end) {
		Date startDate = parseFromBatchNo(type, start);
		Date endDate = parseFromBatchNo(type, end);
		return (int) ((endDate.getTime() - startDate.getTime()) / UNIT_MILL_DAY);
	}

	/**
	 * @return yyyy_MM
	 */
	public static String underlineMon() {
		return underlineMon(null, null, 0);
	}

	public static String underlineMon(Date date) {
		return underlineMon(date, null, 0);
	}

	public static String underlineMon(TimeDiff diffType, int diff) {
		return underlineMon(null, diffType, diff);
	}

	public static String underlineMon(Date date, int diff) {
		return underlineMon(date, null, diff);
	}

	public static String underlineMon(Date date, TimeDiff diffType, int diff) {
		if (date == null) {
			date = new Date();
		}
		if (diffType == null) {
			diffType = TimeDiff.MONTH;
		}
		return dateToBatchNo(BatchNoType.MON, UNDERLINE, dateDiff(date, diffType, diff));
	}

	public static Date parseUnderlineMon(String batchNo) {
		return parseFromBatchNo(BatchNoType.MON, UNDERLINE, batchNo);
	}

	/**
	 * @return yyyyMM
	 */
	public static int batchNumMon() {
		return batchNumMon(null, null, 0);
	}

	public static int batchNumMon(Date date) {
		return batchNumMon(date, null, 0);
	}

	public static int batchNumMon(TimeDiff diffType, int diff) {
		return batchNumMon(null, diffType, diff);
	}

	public static int batchNumMon(Date date, TimeDiff diffType, int diff) {
		if (date == null) {
			date = new Date();
		}
		if (diffType == null) {
			diffType = TimeDiff.MONTH;
		}
		return dateToBatchNum(BatchNoType.MON, dateDiff(date, diffType, diff));
	}

	public static int batchNumMon(int batchNum, int diff) {
		return batchNumMon(batchNum, TimeDiff.MONTH, diff);
	}

	public static int batchNumMon(int batchNum, TimeDiff diffType, int diff) {
		return batchNumDiff(BatchNoType.MON, diffType, batchNum, diff);
	}

	public static Date parsebatchNumMon(int batchNum) {
		return parseFromBatchNo(BatchNoType.MON, batchNum);
	}

	/**
	 * @return yyyy_MM_dd
	 */
	public static String underlineDay() {
		return underlineDay(null, null, 0);
	}

	public static String underlineDay(Date date) {
		return underlineDay(date, null, 0);
	}

	public static String underlineDay(TimeDiff diffType, int diff) {
		return underlineDay(null, diffType, diff);
	}

	public static String underlineDay(Date date, int diff) {
		return underlineDay(date, null, diff);
	}

	public static String underlineDay(Date date, TimeDiff diffType, int diff) {
		if (date == null) {
			date = new Date();
		}
		if (diffType == null) {
			diffType = TimeDiff.DATE;
		}
		return dateToBatchNo(BatchNoType.DAY, UNDERLINE, dateDiff(date, diffType, diff));
	}

	public static Date parseUnderlineDay(String batchNo) {
		return parseFromBatchNo(BatchNoType.DAY, UNDERLINE, batchNo);
	}

	/**
	 * @return yyyyMMdd
	 */
	public static int batchNumDay() {
		return batchNumDay(null, null, 0);
	}

	public static int batchNumDay(Date date) {
		return batchNumDay(date, null, 0);
	}

	public static int batchNumDay(TimeDiff diffType, int diff) {
		return batchNumDay(null, diffType, diff);
	}

	public static int batchNumDay(Date date, TimeDiff diffType, int diff) {
		if (date == null) {
			date = new Date();
		}
		if (diffType == null) {
			diffType = TimeDiff.DATE;
		}
		return dateToBatchNum(BatchNoType.DAY, dateDiff(date, diffType, diff));
	}

	public static int batchNumDay(int batchNum, int diff) {
		return batchNumDay(batchNum, TimeDiff.DATE, diff);
	}

	public static int batchNumDay(int batchNum, TimeDiff diffType, int diff) {
		return batchNumDiff(BatchNoType.DAY, diffType, batchNum, diff);
	}

	public static Date parseBatchNumDay(int batchNum) {
		return parseFromBatchNo(BatchNoType.DAY, batchNum);
	}

	/**
	 * @return yyyy_MM_dd_HH
	 */
	public static String underlineHour() {
		return underlineHour(null, null, 0);
	}

	public static String underlineHour(Date date) {
		return underlineHour(date, null, 0);
	}

	public static String underlineHour(TimeDiff diffType, int diff) {
		return underlineHour(null, diffType, diff);
	}

	public static String underlineHour(Date date, int diff) {
		return underlineHour(date, null, diff);
	}

	public static String underlineHour(Date date, TimeDiff diffType, int diff) {
		if (date == null) {
			date = new Date();
		}
		if (diffType == null) {
			diffType = TimeDiff.HOUR;
		}
		return dateToBatchNo(BatchNoType.HOUR, UNDERLINE, dateDiff(date, diffType, diff));
	}

	public static Date parseUnderlineHour(String batchNo) {
		return parseFromBatchNo(BatchNoType.HOUR, UNDERLINE, batchNo);
	}

	/**
	 * @return yyyyMMdd
	 */
	public static int batchNumHour() {
		return batchNumHour(null, null, 0);
	}

	public static int batchNumHour(Date date) {
		return batchNumHour(date, null, 0);
	}

	public static int batchNumHour(TimeDiff diffType, int diff) {
		return batchNumHour(null, diffType, diff);
	}

	public static int batchNumHour(Date date, TimeDiff diffType, int diff) {
		if (date == null) {
			date = new Date();
		}
		if (diffType == null) {
			diffType = TimeDiff.HOUR;
		}
		return dateToBatchNum(BatchNoType.HOUR, dateDiff(date, diffType, diff));
	}

	public static int batchNumHour(int batchNum, int diff) {
		return batchNumHour(batchNum, TimeDiff.HOUR, diff);
	}

	public static int batchNumHour(int batchNum, TimeDiff diffType, int diff) {
		return batchNumDiff(BatchNoType.HOUR, diffType, batchNum, diff);
	}

	public static Date parseBatchNumHour(int batchNum) {
		return parseFromBatchNo(BatchNoType.HOUR, batchNum);
	}

}
