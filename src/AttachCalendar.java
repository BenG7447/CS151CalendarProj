/**
 * AttachCalendar.java
 * @author 
 */


public class AttachCalendar {
	// Merge CalendarMock and CalendarView
	public static void main(String[] args) {
		CalendarMock cal = new CalendarMock();
		CalendarView cal2 = new CalendarView(cal);
		cal.attach(cal2);
	}

}