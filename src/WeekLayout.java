
import java.util.List;
import javax.swing.JTextArea;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WeekLayout implements ViewLayout
{
	/**
	 * Method that formats layout for the week display
	 */
	public void format(JTextArea output, List<LocalDate> dates, List< List<Event>> events)
	{
		if (dates.isEmpty()){ return; }			// if no dates, do not format
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");    		
		final int length = 50;									// length of each line
		StringBuilder content = new StringBuilder();			// content of output
		
		
		for (int i = 0; i < length; i++){ content.append("="); }
		content.append('\n');
		content.append(String.format("|%" + (length - 2) + "s|\n", "Events"));
		
		
		for (int i = 0; i < length; i++){ content.append("="); }
		content.append('\n');
		for (int i = 0; i < dates.size(); i++)
		{
			content.append(String.format("|%" + (length - 2) + 
								"s|\n", formatter.format(dates.get(i))));
			for(Event event : events.get(i)) 
			{
				content.append(String.format("|%-" + (length - 2) + 
									"s|\n", event.getStartTime() + " " + 
									   " - " + event.getEndTime() + "  " + event.getName()));
	        }
			
			
			for (int j = 0; j < length; j++){ content.append("-");	 }
			content.append('\n');
		}
		
		output.setText(content.toString());
	}
	
}

