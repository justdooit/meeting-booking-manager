package service.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final DateFormat REQEST_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final DateFormat BOOKING_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final DateFormat OUTPUT_DATE_FORMATTER = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat DATE_PART_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	public static Date BOOKING_START_TIME;
	public static Date BOKKING_END_TIME;
	public static String BOOKING_START_TIME_STR = "09:00";
	public static String BOKKING_END_TIME_STR= "17:30";
	
	static {
		try {
			BOOKING_START_TIME = OUTPUT_DATE_FORMATTER.parse(BOOKING_START_TIME_STR);
			BOKKING_END_TIME = OUTPUT_DATE_FORMATTER.parse(BOKKING_END_TIME_STR);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static boolean hasOverlap(Date date1Start, Date date2Start) {
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
