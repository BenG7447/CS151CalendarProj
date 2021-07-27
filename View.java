
import java.util.List;
import javax.swing.JTextArea;
import java.time.LocalDate;


public interface View
{
	/**
	 * Method that formats the layout for day, week, and month display
	 * @param output	JTextArea
	 * @param dates		List<LocalDate>
	 * @param events	List<Event>
	 */
	public void format(JTextArea output, List<LocalDate> dates, List< List<Event>> events);
}

