package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking {
	
	private Date bookingDate;
	private Date requestDate;
	private String empID;
	private int duration;
	
	public Booking(Date requestDate, String empID, Date bookingDate,  int duration) {
		super();
		this.bookingDate = bookingDate;
		this.requestDate = requestDate;
		this.empID = empID;
		this.duration = duration;
	}
	
	public Booking() {
	}
	
	public Date getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public String getEmpID() {
		return empID;
	}
	public void setEmpID(String empID) {
		this.empID = empID;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	@Override
	public String toString() {
		return "Booking [bookingDate=" + bookingDate + ", RequestDate=" + requestDate + ", EmpID=" + empID
				+ ", duration=" + duration + "]";
	}

}
