package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.Booking;

public class BookingManager {

	public static void main(String[] args) throws ParseException {

		DateFormat requetDateformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, List<Booking>> finalOutput = new HashMap<String, List<Booking>>();
		DateFormat bookingDateformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat outputTimeformatter = new SimpleDateFormat("HH:mm");
		Date startTime = outputTimeformatter.parse("09:00");
		Date endTime = outputTimeformatter.parse("17:30");
		try {

			Stream<String> records = readBookingsFile();

//			records.forEach(System.out::println);

			List<Booking> bookinigs = 
					records
					.map(x -> x.split(","))
					.filter(x -> {
						Date bookingStartTime = null;
						String bookingStartTimeStr = null;
						try {
							bookingStartTimeStr = outputTimeformatter.format(bookingDateformatter.parse(x[2].trim()));
							bookingStartTime =  outputTimeformatter.parse(bookingStartTimeStr);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						
						return !bookingStartTime.before(startTime);
					})
					.filter(x -> {
						Date bookingEndTime = null;
						String bookingEndTimeStr = null;
						try {
							bookingEndTimeStr = outputTimeformatter.format(bookingDateformatter.parse(x[2].trim()));
							bookingEndTime = addHoursToJavaUtilDate(outputTimeformatter.parse(bookingEndTimeStr), Integer.parseInt(x[3].trim()));
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						
						return !bookingEndTime.after(endTime);
					})
					.map(x -> {
						Booking booking = new Booking();
						try {
							booking = new Booking(requetDateformatter.parse(x[0].trim()), x[1].trim(),
									bookingDateformatter.parse(x[2].trim()), Integer.parseInt(x[3].trim()));
						} catch (NumberFormatException | ParseException e) {
							e.printStackTrace();
						}
						return booking;
						})
					.collect(Collectors.toList());

			SimpleDateFormat datePartFormatter = new SimpleDateFormat("yyyy-MM-dd");
			List<Booking> bookingsList;

			for (Booking booking : bookinigs) {

				// for every new booking date go on adding in the map if it passes business
				// validation
				String key = datePartFormatter.format(booking.getBookingDate());
				boolean hasOverlap = false;
				if (finalOutput.containsKey(key)) {
					bookingsList = finalOutput.get(key);
					// if overlapping with any one of the bookings
					for (Booking insertedBooking : bookingsList) {
						if (hasOverlap(insertedBooking.getBookingDate(),
								addOneMinuteHoursToJavaUtilDate(booking.getBookingDate()))) {
							hasOverlap = true;
							if (booking.getRequestDate().before(insertedBooking.getRequestDate())) {
								int index = bookingsList.indexOf(insertedBooking);
								bookingsList.set(index, booking);
							}
							break;
						}
					}
					if (!hasOverlap) {
						bookingsList.add(booking);
					}
					finalOutput.put(key, bookingsList);
				} else {
					bookingsList = new ArrayList<Booking>();
					bookingsList.add(booking);
					finalOutput.put(key, bookingsList);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Map.Entry<String, List<Booking>> entry : finalOutput.entrySet()) {
			System.out.println(entry.getKey());
			for (Booking booking : entry.getValue()) {
				System.out.println(outputTimeformatter.format(booking.getBookingDate()) + " "
						+ outputTimeformatter.format(addHoursToJavaUtilDate(booking.getBookingDate(), booking.getDuration()))
						+ " " + booking.getEmpID());
			}
		}

	}

	public static Stream<String> readBookingsFile() throws IOException {
		Stream<String> records = Files.lines(Paths.get("C:\\tmp\\bookings.txt"));
		return records;
	}

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
