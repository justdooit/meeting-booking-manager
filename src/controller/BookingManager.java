package controller;

import static service.util.DateUtil.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.Booking;
import service.FileService;

public class BookingManager {

	public static void main(String[] args) throws ParseException {

		List<Booking> rawBookingsList = BookingManager.extractBookings();
		
		Map<String, List<Booking>> finalOutput = BookingManager.processBokkings(rawBookingsList);
		
		BookingManager.printOutput(finalOutput);

	}

	public static Map<String, List<Booking>> processBokkings(List<Booking> rawBookingsList) {
		
		Map<String, List<Booking>> finalOutput = new HashMap<String, List<Booking>>();
		List<Booking> bookingsList;

		for (Booking booking : rawBookingsList) {

			// for every new booking date go on adding in the map if it passes business
			// validation
			String key = DATE_PART_FORMATTER.format(booking.getBookingDate());
			boolean hasOverlap = false;
			if (finalOutput.containsKey(key)) {
				bookingsList = finalOutput.get(key);
				// if overlapping with any one of the bookings
				for (Booking insertedBooking : bookingsList) {
					if (hasOverlap(insertedBooking.getBookingDate(), addOneMinuteHoursToJavaUtilDate(booking.getBookingDate()))) {
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
		return finalOutput;
	}

	public static List<Booking> extractBookings() {
		
		List<Booking> bookings = null;
		
		try {

			Stream<String> records = FileService.readBookingsFile();

//			records.forEach(System.out::println);

			bookings = 
					records
					.map(x -> x.split(","))
					.filter(x -> {
						Date bookingStartTime = null;
						String bookingStartTimeStr = null;
						try {
							bookingStartTimeStr = OUTPUT_DATE_FORMATTER.format(BOOKING_DATE_FORMATTER.parse(x[2].trim()));
							bookingStartTime = OUTPUT_DATE_FORMATTER.parse(bookingStartTimeStr);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						return !bookingStartTime.before(BOOKING_START_TIME);
						})
					.filter(x -> {
						Date bookingEndTime = null;
						String bookingEndTimeStr = null;
						try {
							bookingEndTimeStr = OUTPUT_DATE_FORMATTER.format(BOOKING_DATE_FORMATTER.parse(x[2].trim()));
							bookingEndTime = addHoursToJavaUtilDate(OUTPUT_DATE_FORMATTER.parse(bookingEndTimeStr),
									Integer.parseInt(x[3].trim()));
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
		
						return !bookingEndTime.after(BOKKING_END_TIME);
					})
					.map(x -> {
						Booking booking = new Booking();
						try {
							booking = new Booking(REQEST_DATE_FORMATTER.parse(x[0].trim()), x[1].trim(),
									BOOKING_DATE_FORMATTER.parse(x[2].trim()), Integer.parseInt(x[3].trim()));
						} catch (NumberFormatException | ParseException e) {
							e.printStackTrace();
						}
						return booking;
					})
					.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bookings;
	}

	public static void printOutput(Map<String, List<Booking>> finalOutput) {
		for (Map.Entry<String, List<Booking>> entry : finalOutput.entrySet()) {
			System.out.println(entry.getKey());
			for (Booking booking : entry.getValue()) {
				System.out.println(OUTPUT_DATE_FORMATTER.format(booking.getBookingDate()) + " "
						+ OUTPUT_DATE_FORMATTER
								.format(addHoursToJavaUtilDate(booking.getBookingDate(), booking.getDuration()))
						+ " " + booking.getEmpID());
			}
		}
	}

}
