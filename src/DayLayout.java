
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.LocalDate;
import javax.swing.JTextArea;


public class DayLayout implements ViewLayout
{
	/**
	 * Method that formats layout for the day display
	 */
    public void format(JTextArea output, List<LocalDate> dates, List<List<Event>> events)
    {
        if (dates.isEmpty()){ return; }						// if no dates, do not format
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");          
        final int length = 50;								// length of each line
        StringBuilder content = new StringBuilder();		// content of output
        
       
        for (int i = 0; i < length; i++){ content.append("-"); }
        content.append('\n');
        content.append(String.format("|%" + (length - 2) + "s|\n", "Events"));
        
     
        for (int i = 0; i < length; i++){ content.append("-"); }
        content.append('\n');
        content.append(String.format("|%" + (length - 2) + "s|\n", 
        						formatter.format(dates.get(0))));
        

        for (int i = 0; i < length; i++){ content.append("-"); }
        content.append('\n');
        for(Event event : events.get(0)) 
        {
            content.append(String.format("|%-" + (length - 2) + 
            					"s|\n", event.getStartTime() + "  " + 
            					    " - " + event.getEndTime() + "   " + 
            							event.getName()));
        }
        
        output.setText(content.toString());
    }
}

