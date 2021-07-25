/**
 * CalendarMock.java
 * 
 * @author
 */


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.List;


public class CalendarMock {
	private String textToDisplay;
	private boolean changeDisplayText;
	private int current;
	private boolean newMonth;
	private int DaysInTheMonth;
	private ArrayList<ChangeListener> changeListeners;
	private HashMap<LocalDate, ArrayList<Event>> calendar;
	private GregorianCalendar gregCal;
	private boolean ChangeInSelectedDay;
	private List<List<Event>> events;
	private List<LocalDate> dates;

	/**
	 * Constructor for the model in the MVC
	 */
	public CalendarMock() {
		textToDisplay = "";
//		changetextToDisplay = false;
		newMonth = false;
		changeListeners = new ArrayList<>();
		gregCal = new GregorianCalendar();
		ChangeInSelectedDay = false;
		DaysInTheMonth = gregCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		current = gregCal.get(Calendar.DATE);
		calendar = new HashMap<LocalDate, ArrayList<Event>>();
		dates = new ArrayList<>();
		events = new ArrayList<>();
	}

	/**
	 * @return true if there has been a change in text display
	 */
	public boolean getChangeInDisplay() {
		return changeDisplayText;
	}

	/**
	 * @return String of events to be displayed to the view
	 */

	// textToDisplay holds the string on the view
	public String getTextDisplay() {
		return textToDisplay;
	}


	// LocalDate dt to get  events of the day
	public void getEvents(LocalDate dt) {
		// make sure to clear data
		dates.clear();
		events.clear();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");
		changeDisplayText = true;
		String eventsForTheDay = "";
		if (!calendar.containsKey(dt))
			eventsForTheDay = formatter.format(dt) + "\n" + "Nothing to show today";
		else {
			ArrayList<Event> eventList = calendar.get(dt);
			dates.add(dt);
			events.add(eventList);
			for (Event event : eventList) {
				eventsForTheDay = eventsForTheDay + "                                           " + formatter.format(dt)
						+ "\n" + event.getStartTime() + ":00" + " - " + event.getEndTime() + ":00  " + event.getName()
						+ "\n";
			}
		}
		textToDisplay = eventsForTheDay;
		update();
	}

	
	 // getStartTime() Gets the date and time at which this calendar event begins
	 // getEndTime() the date and time at which this calendar event ends
	 // This gets the events in the order they start with
	public List<Event> getEventsHelper(LocalDate dt) {
		List<Event> eventList = new ArrayList<Event>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");
		changeDisplayText = true;
		String eventsForTheDay = "";
		if (!calendar.containsKey(dt))
			textToDisplay = "\n" + "Nothing to show today";
		else {
			sortDay(calendar.get(dt));
			eventList = calendar.get(dt);
			for (Event event : eventList) {
				// If input = 12, we wanna add ":00" to make it 12:00
				// Also, we'll use - to separate start time and end time
				eventsForTheDay = eventsForTheDay + "                                           " + formatter.format(dt)
						+ "\n" + event.getStartTime() + ":00" + " - " + event.getEndTime() + ":00  " + event.getName()
						+ "\n";
			}
		}
		textToDisplay = eventsForTheDay;
		return eventList;
	}

	/**
	 * Gets events for a given time frame
	 * 
	 * @param d1 LocalDate - start date
	 * @param d2 LocalDate - end date
	 */
	public void getEvents(LocalDate d1, LocalDate d2) {
		// clear the data
		dates.clear();
		events.clear();
		changeDisplayText = true;
		String listOfAllEvents = new String();
		LocalDate current = d1;
		while (!current.equals(d2)) {
			if (calendar.containsKey(current)) {
				List<Event> eventsList = getEventsHelper(current);
				dates.add(current);
				events.add(eventsList);
				listOfAllEvents = listOfAllEvents + "      " + "\n" + textToDisplay;
			}
			current = current.plusDays(1);
		}
		if (!d1.equals(d2) && calendar.containsKey(d2)) {
			List<Event> eventsList = getEventsHelper(d2);
			dates.add(d2);
			events.add(eventsList);
			listOfAllEvents = listOfAllEvents + "      " + "\n" + textToDisplay + "\n";
		}
		textToDisplay = listOfAllEvents;
		update();
	}

	
	public boolean overlap(LocalDate date, Event e) {
		if (calendar.containsKey(date)) {
			ArrayList<Event> list = calendar.get(date);
			for (Event current : list) {
				int currentStart = current.getStartTime();
				int checkStart = e.getStartTime();
				int currentEnd = current.getEndTime();
				int checkEnd = e.getEndTime();
				if (currentStart == checkStart || checkEnd == currentEnd || currentStart == checkEnd
						|| checkStart == currentEnd)
					return true;
				else if (currentStart >= checkStart && checkEnd >= currentEnd)
					return true;

				else if (checkEnd >= currentStart && currentEnd >= checkStart)
					return true;
			}
		}
		return false;
	}

	// We will use the switch to see which month is active
	public String getStringMonth(int i) { 
		
		// Remember the months are declared in the MONTHS.java file
		switch (i) {
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

	
	 // Because we have removed overlapping in the overlap class, sort events on starting time
	public void sortDay(ArrayList<Event> events) {
		class order implements Comparator<Event> {
			public int compare(Event first, Event second) {
				return Integer.compare(first.getStartTime(), second.getStartTime());
			}
		}
		Collections.sort(events, new order());
	}

	public void resetChangeInTestDisplay() {
		changeDisplayText = false;
	}

	// Merge the listeners
	public void attach(ChangeListener listener) {
		changeListeners.add(listener);
	}

	/**
	 * update the ChangeListeners by calling statechanged() for each listener
	 */
	public void update() {
		for (ChangeListener l : changeListeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}

	//Allow user to select day
	public void setCurrentlySelectedDay(int date) {
		ChangeInSelectedDay = true;
		current = date;
		update();
	}

	// Checks if day is selected
	public boolean getChangeInSelectedDay() {
		return ChangeInSelectedDay;
	}

	
	public void resetChangeInSelectedDay() {
		ChangeInSelectedDay = false;
	}

	
	public int getCurrentlySelectedDay() {
		return current;
	}

	
	public int getCurrentYear() {
		return gregCal.get(Calendar.YEAR);
	}

	
	public int getCurrentMonth() {
		return gregCal.get(Calendar.MONTH);
	}

	
	public int getDayInWeek(int day) { // note: starts at 1
		gregCal.set(Calendar.DAY_OF_MONTH, day);
		return gregCal.get(Calendar.DAY_OF_WEEK);
	}

	
	public int getNumberOfDaysInMonth() {
		return DaysInTheMonth;
	}

	// Month +1
	// Remember to call update
	public void nextMonth() { // edit
		gregCal.add(Calendar.MONTH, 1);
		newMonth = true;
		DaysInTheMonth = gregCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		update();
	}

	// Month -1
	// Remember to call update
	public void previousMonth() { // edit
		gregCal.add(Calendar.MONTH, -1);
		newMonth = true;
		DaysInTheMonth = gregCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		update();
	}

	public LocalDate getCurrentLocalDate() {
		return LocalDate.of(getCurrentYear(), getCurrentMonth() + 1, getCurrentlySelectedDay());
	}

	// day +1
	// Remember to call update
	public void nextDay() { // edit
		if (current + 1 > gregCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			nextMonth();
			current = 1;
		} else
			current++;
		update();
	}

	//day -1
	// Remember to call update
	public void previousDay() { // edit
		if (current == 1) {
			previousMonth();
			current = gregCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		} else
			current--;
		update();
	}


	public boolean monthStateChanged() {
		return newMonth;
	}

	// After change in month, we set back newMonth back to false
	public void resetChangeOfMonth() {
		newMonth = false;
	}

	// Set event button to create event 
	public void createEvent(String name, int start, int end) { 
		LocalDate day = LocalDate.of(getCurrentYear(), getCurrentMonth() + 1, current);
		Event e = new Event(name, start, end);
		if (calendar.containsKey(day)) {
			ArrayList<Event> list = calendar.get(day);
			list.add(e);
			calendar.put(day, list);
		} else {
			ArrayList<Event> list = new ArrayList<Event>();
			list.add(e);
			calendar.put(day, list);
		}
	}

	
	public void addEvent(LocalDate d, Event e) {
		if (calendar.containsKey(d)) {
			ArrayList<Event> list = calendar.get(d);
			list.add(e);
			calendar.put(d, list);
		} else {
			ArrayList<Event> list = new ArrayList<Event>();
			list.add(e);
			calendar.put(d, list);
		}
	}

	
	public void fileReader(String fileName) throws FileNotFoundException { // rename // this only inputs regular events
		FileInputStream file = new FileInputStream(fileName); // let the user specify the file path
		Scanner input = new Scanner(file);
		String name;
		int start;
		int end;
		int startMonth;
		int endMonth;
		int year;
		String[] days;
		int dayInt = 0;
		while (input.hasNextLine()) {
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
			while (!startDay.equals(endDay)) {
				for (int i = 0; i < days.length; i++) {
					switch (days[i]) {
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
					if (startDay.getDayOfWeek().getValue() == dayInt && !(overlap(startDay, event)))
						addEvent(startDay, event);
				}
				startDay = startDay.plusDays(1);
			}
		}
		input.close();
	}

	/**
	 * get events (loaded by getEvents(...) method)
	 * 
	 * @return a list that has list of events
	 */
	public List<List<Event>> getEvents() {
		return events;
	}

	/**
	 * get dates (loaded by getEvents(...) method)
	 * 
	 * @return a list of LocalDate
	 */
	public List<LocalDate> getDates() {
		return dates;
	}

}
