/**
 * View.java
 * @author 
 */

import java.time.LocalDate;
import java.util.List;
import javax.swing.JTextArea;

public interface ViewLayout{
	/**
	 * format the layout for day, week,and month display
	 * @param output
	 * @param dates
	 * @param events
	 */
	public void format(JTextArea output, List<LocalDate> dates, List< List<Event>> events);
}
