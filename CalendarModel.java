
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;


public class CalendarModel
{
	private String textToDisplay;
	private boolean changeDisplayText;
	private boolean ChangeInSelectedDay;
	private boolean newMonth;
	private int current;
	private int DaysInTheMonth;
	private List<List<Event>> events;
	private List<LocalDate> dates;
	private ArrayList<ChangeListener> changeListeners;
	private HashMap<LocalDate, ArrayList<Event>> calendar;
	private GregorianCalendar gregCal;

	/**
	 * Ctor of MVC
	 */
	public CalendarModel()
	{
		textToDisplay = "";
		newMonth = false;
		ChangeInSelectedDay = false;
		changeListeners = new ArrayList<>();
		dates = new ArrayList<>();
		events = new ArrayList<>();
		gregCal = new GregorianCalendar();
		DaysInTheMonth = gregCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		current = gregCal.get(Calendar.DATE);
		calendar = new HashMap<LocalDate, ArrayList<Event>>();
	}

	/**
	 * Method that gets events of the day
	 * @param dt	 	LocalDate
	 */
	public void getEvents(LocalDate dt)
	{
		// all data must be clear
		dates.clear();
		events.clear();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");
		changeDisplayText = true;
		String eventsForTheDay = "";
		if( !calendar.containsKey(dt) )
			eventsForTheDay = formatter.format(dt) + "\n" + "Nothing to show today";
		else {
			ArrayList<Event> eventList = calendar.get(dt);
			dates.add(dt);
			events.add(eventList);
			for( Event event : eventList ) 
			{
				eventsForTheDay = eventsForTheDay + 
										"                                         " + 
											formatter.format(dt) + "\n" + 
												event.getStartTime() + " - " + 
													event.getEndTime() + "   " + 
														event.getName() + "\n";
			}
		}
		textToDisplay = eventsForTheDay;
		update();
	}

	/**
	 * Method that gets events for a given time frame
	 * @param d1 LocalDate  start date of time frame to search
	 * @param d2 LocalDate  end date of time frame to search
	 */
	public void getEvents(LocalDate d1, LocalDate d2)
	{
		// all data must be clear
		dates.clear();
		events.clear();
		changeDisplayText = true;
		String listOfAllEvents = new String();
		LocalDate current = d1;
		while( !current.equals(d2) ) 
		{
			if( calendar.containsKey(current) ) 
			{
				List<Event> eventsList = getEventsHelper(current);
				dates.add(current);
				events.add(eventsList);
				listOfAllEvents = listOfAllEvents + "      " + "\n" + textToDisplay;
			}
			current = current.plusDays(1);
		}
		if( !d1.equals(d2) && calendar.containsKey(d2) ) 
		{
			List<Event> eventsList = getEventsHelper(d2);
			dates.add(d2);
			events.add(eventsList);
			listOfAllEvents = listOfAllEvents + "      " + "\n" + textToDisplay + "\n";
		}
		textToDisplay = listOfAllEvents;
		update();
	}

	/**
	 * Method that returns list of events 
	 * @return list of events
	 */
	public List<List<Event>> getEvents() { return events; }

	/**
	 * Adds event into calendar 
	 * @param d	date of event
	 * @param e	event
	 */
	public void addEvent(LocalDate d, Event e)
	{
		if( calendar.containsKey(d) ) 
		{
			ArrayList<Event> list = calendar.get(d);
			list.add(e);
			calendar.put(d, list);
		} else 
		{
			ArrayList<Event> list = new ArrayList<Event>();
			list.add(e);
			calendar.put(d, list);
		}
	}

	/**
	 * Method that creates an event and adds it to calendar
	 * @param name  String name of event
	 * @param start  int of start time of event
	 * @param end   int of end time of event 
	 */
	public void createEvent(String name, int start, int end)
	{
		LocalDate day = LocalDate.of(getCurrentYear(), 
												getCurrentMonth() + 1, current);
		Event e = new Event(name, start, end);
		if( calendar.containsKey(day) ) 
		{
			ArrayList<Event> list = calendar.get(day);
			list.add(e);
			calendar.put(day, list);
		} else 
		{
			ArrayList<Event> list = new ArrayList<Event>();
			list.add(e);
			calendar.put(day, list);
		}
	}

	/**
	 * Method that formats the events to be displayed in order in the given date
	 * @param dt LocalDate
	 * @return List of events
	 */
	public List<Event> getEventsHelper(LocalDate dt)
	{
		List<Event> eventList = new ArrayList<Event>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");
		changeDisplayText = true;
		String eventsForTheDay = "";
		if( !calendar.containsKey(dt) )
			textToDisplay = "\n" + "Nothing to show today";
		else 
		{
			sortDay(calendar.get(dt));
			eventList = calendar.get(dt);
			for( Event event : eventList ) 
			{
				// Formats to display events 
				eventsForTheDay = eventsForTheDay + 
										"                                           " + 
											formatter.format(dt) + "\n" + 
												event.getStartTime() + " - " + 
													event.getEndTime() + "  " + 
														event.getName() + "\n";
			}
		}
		textToDisplay = eventsForTheDay;
		return eventList;
	}

	/**
	 * Method that returns true if an event overlap with existing event
	 * @param date 		LocalDate
	 * @param e	   		Event
	 * @return boolean 	
	 */
	public boolean overlap(LocalDate date, Event e)
	{
		if( calendar.containsKey(date) ) 
		{
			ArrayList<Event> list = calendar.get(date);
			for( Event current : list ) 
			{
				int currentStart = current.getStartTime();
				int checkStart = e.getStartTime();
				int currentEnd = current.getEndTime();
				int checkEnd = e.getEndTime();
				if( currentStart == checkStart || checkEnd == currentEnd 
						|| currentStart == checkEnd || checkStart == currentEnd )
					return true;
				else if( currentStart >= checkStart && checkEnd >= currentEnd )
					return true;
				
				else if( checkEnd >= currentStart && currentEnd >= checkStart )
					return true;
			}
		}
		return false;
	}

	/**
	 * Method that sorts events by starting time
	 * @param events List of events
	 */
	public void sortDay(ArrayList<Event> events)
	{
		class Sorting implements Comparator<Event>
		{
			public int compare(Event first, Event second)
			{
				return Integer.compare(first.getStartTime(), second.getStartTime());
			}
		}
		Collections.sort(events, new Sorting());
	}

	/**
	 * Method that checks which month is active
	 * @param 	i			int
	 * @return 	String		active month
	 */
	public String getStringMonth(int i)
	{
		switch( i ) {
		case 1:
			return "January";
		case 2:
			return "Febuary";
		case 3:
			return "March";
		case 4:
			return "April";
		case 5:
			return "May";
		case 6:
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December";
		}
		return "incorrect month input";
	}

	/**
	 * Method that gets text to display on view
	 * @return String
	 */
	public String getTextDisplay(){ return textToDisplay; }

	/**
	 * Method that resets changeDisplayText boolean to false
	 */
	public void resetChangeInTestDisplay(){ changeDisplayText = false; }

	/**
	 * Method that attaches the listeners
	 * @param listener
	 */
	public void attach(ChangeListener listener){ changeListeners.add(listener); }

	/**
	 * Method that updates the Changelisteners
	 */
	public void update()
	{
		for( ChangeListener l : changeListeners ) 
		{
			l.stateChanged(new ChangeEvent(this));
		}
	}

	/**
	 * Method that gets boolean that indicates change in display
	 * @return boolean
	 */
	public boolean getChangeInDisplay(){ return changeDisplayText; }

	/**
	 * Method that gets boolean that indicates if day is selected
	 * @return
	 */
	public boolean getChangeInSelectedDay(){ return ChangeInSelectedDay; }

	/**
	 * Method that resets ChangeInSelectedDay boolean to false
	 */
	public void resetChangeInSelectedDay(){ ChangeInSelectedDay = false; }

	/**
	 * Method that gets boolean that indicates if current day is selected
	 * @return 
	 */
	public int getCurrentlySelectedDay(){ return current; }

	/**
	 * Method that allows users to select days
	 * @param date int
	 */
	public void setCurrentlySelectedDay(int date)
	{
		ChangeInSelectedDay = true;
		current = date;
		update();
	}

	/**
	 * Method that gets int of current Month
	 * @return int
	 */
	public int getCurrentMonth(){return gregCal.get(Calendar.MONTH);}

	/**
	 * Method that gets int of current Year
	 * @return
	 */
	public int getCurrentYear(){return gregCal.get(Calendar.YEAR);}

	/**
	 * Method that returns LocalDate in Year, Month, day formath
	 * @return
	 */
	public LocalDate getCurrentLocalDate()
	{
		return LocalDate.of(getCurrentYear(), 
									getCurrentMonth() + 1, 
											getCurrentlySelectedDay());
	}

	/**
	 * Method that returns day in week 
	 * @param day int of month
	 * @return int from 1-7
	 */
	public int getDayInWeek(int day)
	{
		// Week starts at 1
		gregCal.set(Calendar.DAY_OF_MONTH, day);	
		return gregCal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Method that returns number of days in given month
	 * @return int
	 */
	public int getNumberOfDaysInMonth(){return DaysInTheMonth;}

	/**
	 * Method that adds month + 1, updates changelisteners
	 */
	public void nextMonth()
	{
		gregCal.add(Calendar.MONTH, 1);
		newMonth = true;
		DaysInTheMonth = gregCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		update();
	}

	/**
	 * Method that subtracts month - 1, updates changelisteners
	 */
	public void previousMonth()
	{
		gregCal.add(Calendar.MONTH, -1);
		newMonth = true;
		DaysInTheMonth = gregCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		update();
	}

	/**
	 * Method that adds day + 1, updates changelisteners
	 */
	public void nextDay()
	{ 
		if( current + 1 > gregCal.getActualMaximum(Calendar.DAY_OF_MONTH) ) 
		{
			nextMonth();
			current = 1;
		} else
			current++;
		update();
	}

	/**
	 * Method that subtracts month - 1, updates changelisteners
	 */
	public void previousDay()
	{
		if( current == 1 ) 
		{
			previousMonth();
			current = gregCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		} else
			current--;
		update();
	}

	/**
	 * Method that returns boolean that indicates month state changed
	 * @return
	 */
	public boolean monthStateChanged(){return newMonth;}

	/**
	 * Method that resets status of change of month to false
	 */
	public void resetChangeOfMonth(){newMonth = false;}

	/**
	 * Method that reads String with path to file that contains
	 * recurring events
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void fileReader(String fileName) throws FileNotFoundException
	{
		// Path will be given
		FileInputStream file = new FileInputStream(fileName);
		Scanner input = new Scanner(file);
		String name;
		int start;
		int end;
		int startMonth;
		int endMonth;
		int year;
		String[] days;
		int dayInt = 0;
		while( input.hasNextLine() ) {
			String current = input.nextLine();
			String[] values = current.split(";");
			name = values[0];
			year = Integer.parseInt(values[1]);
			startMonth = Integer.parseInt(values[2]);
			endMonth = Integer.parseInt(values[3]);
			days = values[4].split("");
			start = Integer.parseInt(values[5]);
			end = Integer.parseInt(values[6]);
			LocalDate startDay = LocalDate.of(year, startMonth, 1);
			LocalDate endDay = LocalDate.of(year, endMonth + 1, 1);
			Event event = new Event(name, start, end);
			while( !startDay.equals(endDay) ) {
				for( int i = 0; i < days.length; i++ ) {
					switch( days[i] ) {
					case "M":
						dayInt = 1;
						break;
					case "T":
						dayInt = 2;
						break;
					case "W":
						dayInt = 3;
						break;
					case "H":
						dayInt = 4;
						break;
					case "F":
						dayInt = 5;
						break;
					case "A":
						dayInt = 6;
						break;
					case "S":
						dayInt = 7;
						break;
					}
					if( startDay.getDayOfWeek().getValue() 
							== dayInt && !(overlap(startDay, event)) )
						addEvent(startDay, event);
				}
				startDay = startDay.plusDays(1);
			}
		}
		input.close();
	}

	/**
	 * Method that return list of dates
	 * @return list of dates
	 */
	public List<LocalDate> getDates()
	{return dates;}

}

