
/**
 * Class that merges CalendarModel and CalendarView
 * Runs the Calendar Project
 */
public class CalendarTester 
{
	public static void main(String[] args) 
	{
		CalendarModel model = new CalendarModel();
		CalendarView view = new CalendarView(model);
		model.attach(view);
	}
}

