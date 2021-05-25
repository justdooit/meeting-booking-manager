
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CompareLocalDateTime {

	public static void main(String[] args) throws ParseException {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

//      LocalDateTime date1 = LocalDateTime.parse("2020-01-31 11:44:43", dtf);
//      LocalDateTime date2 = LocalDateTime.parse("2020-01-31 11:44:44", dtf);

		String dateTime1 = "2011-03-21 09:00";
		String dateTime2 = "2011-03-21 11:00";
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date1 = formatter.parse(dateTime1);
		Date date2 = formatter.parse(dateTime2);

		Date date3 = addHoursToJavaUtilDate(date1, 2);
		Date date4 = addHoursToJavaUtilDate(date2, 2);

		System.out.println("date1 : " + date1);
		System.out.println("date2 : " + date2);
		System.out.println("date1 : " + date3);
		System.out.println("date2 : " + date4);
		
		System.out.println(hasOverlap(date1, addOneMinuteHoursToJavaUtilDate(date2)));

		if (date1.equals(date2)) {
			System.out.println("Date1 is equal Date2");
		}

		if (date1.before(date2)) {
			System.out.println("Date1 is before Date2");
		}

		if (date1.after(date2)) {
			System.out.println("Date1 is after Date2");
		}

	}
	
	/**
	 * (StartA <= EndB)  and  (EndA >= StartB) (StartA <= EndB)  and  (StartB <= EndA)

NOTE3. Thanks to @tomosius, a shorter version reads:
DateRangesOverlap = max(start1, start2) < min(end1, end2)
	 */

	private static boolean hasOverlap(Date date1Start, Date date2Start) {
		Date date1End = addHoursToJavaUtilDate(date1Start, 2);
		Date date2End = addHoursToJavaUtilDate(date2Start, 2);
		return !date1End.before(date2Start) && !date1Start.after(date2End);
	
	}

	public static Date addHoursToJavaUtilDate(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}
	
	public static Date addOneMinuteHoursToJavaUtilDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, 1);
		return calendar.getTime();
	}
}