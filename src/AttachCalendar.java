
/**
 * Class that merges CalendarMock and CalendarView
 * Runs the Calendar Project
 */
public class AttachCalendar 
{
	public static void main(String[] args) 
	{
		CalendarMock cal = new CalendarMock();
		CalendarView cal2 = new CalendarView(cal);
		cal.attach(cal2);
	}
}

