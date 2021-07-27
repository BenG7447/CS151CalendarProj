
/**
 * Class of Events that contain a name, starttime, and endtime
 */
class Event 
{
	private String name;
	private int startTime;
	private int endTime;
	
	/**
	 * Ctor that creates an event with a name, starttime, and endtime
	 * @param name			String
	 * @param startTime		int
	 * @param endTime		int
	 */
	public Event(String name, int startTime, int endTime) 
	{
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * Method that gets the name of event
	 * @return String
	 */
	public String getName() { return name; }
	
	/**
	 * Method that gets the event start time
	 * @return int
	 */
	public int getStartTime() { return startTime; }
	
	/**
	 * Method that gets the event end time
	 * @return int
	 */
	public int getEndTime() { return endTime; }
	
}

