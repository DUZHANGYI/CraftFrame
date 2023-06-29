import com.craft.frame.commons.lang.utils.DateUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author DURR
 * @desc 类描述
 * @date 2023/6/29 12:04
 */
public class UtilsTest {

	@Test
	public void getDateRangeTest() throws Exception {
		/*LocalDate start = LocalDate.of(2023, 6, 1);
		LocalDate end = LocalDate.now();*/

		Date start = DateUtils.parseDate("2023-06-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Date end = new Date();
		List<LocalDate> dates = DateUtils.getDateRange(start, end);
		dates.forEach(System.err::println);
	}

	@Test
	public void getDayBoundaryTest() {
		// LocalDate
		System.err.println("--------------------------------LocalDate--------------------------------");
		LocalDate now = LocalDate.now();

		LocalDateTime[] dateTimes = DateUtils.getDayBoundary(now);
		for (LocalDateTime dateTime : dateTimes) {
			System.err.println(dateTime);
		}

		String[] dateStrings = DateUtils.getDayBoundaryToStr(now);
		for (String dateString : dateStrings) {
			System.err.println(dateString);
		}

		Date[] times = DateUtils.getDayBoundaryToDate(now);
		for (Date time : times) {
			System.err.println(time);
		}

		System.err.println("--------------------------------Date--------------------------------");

		// Date
		Date date = new Date();
		LocalDateTime[] dateTimes2 = DateUtils.getDayBoundary(date);
		for (LocalDateTime dateTime : dateTimes2) {
			System.err.println(dateTime);
		}

		String[] dateStrings2 = DateUtils.getDayBoundaryToStr(date);
		for (String dateString : dateStrings2) {
			System.err.println(dateString);
		}

		Date[] times2 = DateUtils.getDayBoundaryToDate(date);
		for (Date time : times2) {
			System.err.println(time);
		}
	}

}
