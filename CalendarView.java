
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import java.awt.Font;
import java.time.DayOfWeek;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Class that follows MVC pattern View
 * Class that sets up buttons, panels, and frames of calendar program
 *
 */
public class CalendarView implements ChangeListener 
{
	private MONTHS[] months = MONTHS.values();
	private CalendarModel model;
	boolean onDayView;
	boolean onMonthView;
	boolean onWeekView;
	private int currentlyPickedDay = -1;
	private int numOfDaysinMonth;
	private JFrame frameView = new JFrame();
	private JPanel monthPanel = new JPanel();
	private JLabel nameOfMonth = new JLabel();
	private JButton monthButton = new JButton("Month");
	private JButton weekButton = new JButton("Week");
	private JButton dayButton = new JButton("Day");
	private JButton prev = new JButton("<");
	private JButton next = new JButton(">");
	private JButton today = new JButton("Today");
	private JButton create = new JButton("Create an Event");
	private JButton file = new JButton("Add File");
	private JButton agenda = new JButton("Agenda");
	private JTextArea textArea = new JTextArea();
	private ArrayList<JButton> dayButtons = new ArrayList<JButton>();
	LocalDate curerntDayOfView;  //Does not have to be same as currentlySelectedDay
    private View viewLayout; //shows different formats based on day, week, month
    
    
    /**
     * 
     * Start of Controller (Contains create and action listeners) -------------------
     * 
     */
    
    
    /**
     * Controller
     * Method that creates buttons representing all the days in the current month
     * then adds them to an arraylist and adds them to the month panel
     */
	private void createDayButtons() 
	{
		for (int x = 0; x < model.getDayInWeek(1) - 1; x++) 
		{
			// adds blank buttons to allow days to be in correct location
			JButton filling = new JButton();
			filling.setEnabled(false);
			monthPanel.add(filling);	
		}
		
		for (int i = 1; i <= numOfDaysinMonth; i++) 
		{	
			final int j = i;
			JButton day = new JButton(Integer.toString(i));
			
			day.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent event) 
				{
					onDayView = false;
					onMonthView = false;
					onWeekView = false;
					prev.setEnabled(false);
					next.setEnabled(false);
					pickDay(j - 1); 		// i must be final
					displayDate(j);   		// i must be final
					create.setEnabled(true);
				}
			});
			
			monthPanel.add(day); //adds each button to month panel
			dayButtons.add(day); //adds each button to list containing month's day button
		}
	}
	
	
	/**
     * 
     * Start of Ctor --------------------------------------------------------
     * 
     */
	
	
	/**
	 * Controller
	 * Ctor that creates calendar
	 * @param m CalendarModel model that stores and manipulates data of calendar
	 */
	public CalendarView(CalendarModel m) 
	{ 
		this.model = m;
		numOfDaysinMonth = model.getNumberOfDaysInMonth();
		monthPanel.setLayout(new GridLayout(0, 7));
		textArea = new JTextArea(30, 50);
		textArea.setEditable(false);
		textArea.setFont(new Font("monospaced", Font.PLAIN, 18));
		createDayButtons();
		displayDate(model.getCurrentlySelectedDay());  
		pickDay(model.getCurrentlySelectedDay() - 1);   
		prev.setEnabled(false);
		next.setEnabled(false);
		onDayView = true;
		
		next.addActionListener( new ActionListener()  
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (onMonthView)
				{
                    curerntDayOfView = 
                    		curerntDayOfView.plusMonths(1).withDayOfMonth(1);
                    monthView(curerntDayOfView);
                }
                else if (onWeekView){
                    curerntDayOfView = curerntDayOfView.plusDays(7);
                    weekView(curerntDayOfView);
                }
                else { // on day view
                    curerntDayOfView = curerntDayOfView.plusDays(1);
                    dayView(curerntDayOfView);
                }
			}
		});
		
		today.addActionListener( new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				LocalDate current = LocalDate.now();
				curerntDayOfView = current;	
				if (onMonthView)
					monthView(current);   
				else if (onWeekView)
					weekView(current);
				else // on day view
				{
					dayView(current);
				}
			}
		});
		
		prev.addActionListener( new ActionListener()  
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				prev.setEnabled(true);	
				next.setEnabled(true);
				if (onMonthView) 
				{
					curerntDayOfView = 
							curerntDayOfView.minusMonths(1).withDayOfMonth(1);
					monthView(curerntDayOfView); 
				}    
				else if (onWeekView) 
				{
					curerntDayOfView = curerntDayOfView.minusDays(7);   
					weekView(curerntDayOfView);
				}
				else { // on day view
					curerntDayOfView = curerntDayOfView.minusDays(1);
					dayView(curerntDayOfView);
				}
			}
		});
		
		monthButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				monthView(LocalDate.of(model.getCurrentYear(), 
												model.getCurrentMonth() + 1, 
														model.getCurrentlySelectedDay()));
				prev.setEnabled(true);
				next.setEnabled(true);
			}
		});

		weekButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				prev.setEnabled(true);
				next.setEnabled(true);
				weekView(LocalDate.of(model.getCurrentYear(), 
													model.getCurrentMonth() + 1, 
														model.getCurrentlySelectedDay()));
			}
		});

		dayButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				prev.setEnabled(true);
				next.setEnabled(true);
				dayView(LocalDate.of(model.getCurrentYear(), 
											model.getCurrentMonth() + 1, 
												model.getCurrentlySelectedDay()));
			}
		});

		create.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e){ createEventDialog(""); }
		});

		JButton prevMonth = new JButton("<");
		prevMonth.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e){ model.previousMonth(); }
		});

		JButton nextMonth = new JButton(">");
		nextMonth.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e){ model.nextMonth(); }
		});

		agenda.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				final JDialog dialog = new JDialog();
				dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				final JTextField startDay = new JTextField(15);
				final JTextField endDay = new JTextField(15);
				
				JButton submit = new JButton("Submit");
				submit.addActionListener( new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if( startDay.getText().isEmpty() || endDay.getText().isEmpty() )
							return;
						else 
						{
							dialog.dispose();
							String[] start = startDay.getText().split("/");
							String[] end = endDay.getText().split("/");
							
							if( start[2].length() == 2 ) { start[2] = "20" + start[2]; }
							if( end[2].length() == 2 ) { end[2] = "20" + end[2]; }
							
							int[] startArray = new int[] { 0, 0, 0 };
							for( int i = 0; i < start.length; i++ ) {
								startArray[i] = Integer.parseInt(start[i]);
							}
							
							int[] endArray = new int[] { 0, 0, 0 };
							for( int i = 0; i < end.length; i++ ) {
								endArray[i] = Integer.parseInt(end[i]);
							}
							
							LocalDate d1 = LocalDate.of(startArray[2], 
																	startArray[0], startArray[1]);
							LocalDate d2 = LocalDate.of(endArray[2], 
																	endArray[0], endArray[1]);
							model.getEvents(d1, d2);
						}
					}
				});
				dialog.setLayout(new GridBagLayout());
				
				GridBagConstraints gridContraints = new GridBagConstraints();
				dialog.add(new JLabel("Start Day mm/dd/yyyy"), gridContraints);
				dialog.add(new JLabel("End Day mm/dd/yyyy"), gridContraints);
				gridContraints.gridy = 2;
				
				dialog.add(startDay, gridContraints);
				dialog.add(endDay, gridContraints);
				dialog.add(submit, gridContraints);
				
				dialog.pack();
				dialog.setVisible(true);
			}
		});
		
		file.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				final JDialog dialog = new JDialog();
				dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				final JTextField fileNameInput = new JTextField(30);
				JButton submit = new JButton("Submit");
				
				submit.addActionListener( new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if( fileNameInput.getText().isEmpty() ) { return; } 
						else 
						{
							dialog.dispose();
							String fileName = fileNameInput.getText();
							try { model.fileReader(fileName); } 
							catch( FileNotFoundException e1 ) { }
						}
					}
				});

				dialog.setLayout(new GridBagLayout());
				GridBagConstraints gridContraints = new GridBagConstraints();
				
				gridContraints.weightx = 1.0;
				gridContraints.anchor = GridBagConstraints.LINE_START;
				
				dialog.add(fileNameInput, gridContraints);
				gridContraints.gridy = 3;
				gridContraints.weightx = 0.0;
				gridContraints.anchor = GridBagConstraints.LINE_START;
				
				dialog.add(new JLabel("File Path"), gridContraints);
				gridContraints.anchor = GridBagConstraints.CENTER;
				gridContraints.gridy = 4;
				
				dialog.add(submit, gridContraints);
				dialog.pack();
				dialog.setVisible(true);
			}
		});
		
		
		/**
	     * 
	     * End of Controller (Contains create and action listeners) --------------
	     * 
	     */
		
		
		JPanel titleContainer = new JPanel();
		titleContainer.setLayout(new FlowLayout());
		
		JPanel containsMonth = new JPanel();
		containsMonth.setLayout(new BorderLayout());
		
		JPanel finalContainer = new JPanel();
		JPanel btnsPanel5 = new JPanel((new FlowLayout(FlowLayout.CENTER, 0, 0)));
		
		finalContainer.setLayout(new BorderLayout());
		nameOfMonth.setText("" + months[model.getCurrentMonth()] + 
														" " + model.getCurrentYear());
		titleContainer.add(prevMonth);
		titleContainer.add(nameOfMonth);
		titleContainer.add(nextMonth);
		btnsPanel5.add(create);
		
		containsMonth.add(titleContainer, BorderLayout.NORTH);
		containsMonth.add( new JLabel("      S                M              T              W                T                 F                 S") 
									,BorderLayout.CENTER);
		containsMonth.add(monthPanel, BorderLayout.SOUTH);
		
		GridBagConstraints constraints = new GridBagConstraints();
		JPanel dayViewPanel = new JPanel();
		dayViewPanel.setLayout(new GridBagLayout());
		
		JPanel btnsPanel = new JPanel((new FlowLayout(FlowLayout.CENTER, 0, 0)));
		btnsPanel.setBackground(Color.BLACK);
		btnsPanel.setForeground(Color.RED);
		
		JPanel btnsPanel2 = new JPanel((new FlowLayout(FlowLayout.CENTER, 0, 0 )));
		btnsPanel2.setBackground(Color.BLACK);
		btnsPanel2.setForeground(Color.RED);
		btnsPanel2.add(prev);
		btnsPanel2.add(today);
		btnsPanel2.add(next);
		btnsPanel2.add(dayButton);
		btnsPanel2.add(weekButton);
		btnsPanel2.add(monthButton);
		btnsPanel2.add(agenda);
		
		btnsPanel.add(file);
		btnsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 30)); 
		
		FlowLayout layout = (FlowLayout)btnsPanel2.getLayout();
		layout.setVgap(0);
		btnsPanel.add(create);
		
		// Agenda, create, and file views
		constraints.gridx = 0;
		constraints.gridy = 1;
		dayViewPanel.add(btnsPanel, constraints);
		
		// Today, day, week, and month views
		constraints.gridx = 0;
		constraints.gridy = 2;
		dayViewPanel.add(btnsPanel2, constraints);
		
		// Event view
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 3;
		
		JScrollPane dayScrollPane = new JScrollPane(textArea);
		dayScrollPane.setVerticalScrollBarPolicy(
											JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		dayViewPanel.add(dayScrollPane, constraints);
		finalContainer.add(containsMonth, BorderLayout.NORTH);
		frameView.add(finalContainer);
		frameView.add(dayViewPanel);
		
		frameView.setLayout(new FlowLayout());
		frameView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameView.pack();
		frameView.setVisible(true);
	}
	
	
	/**
     * 
     * End of Ctor -------------------------------------------------------------
     * 
     */
	
	
	
	
	/**
     * 
     * Start of Controller Method Helpers -------------------------------------
     * 
     */
	
	
	/**
	 * Method that creates a border around selected date
	 * and puts a border around current date
	 * @param int dayToSelect
	 */
	private void pickDay(int dayToSelect) 
	{  
		if (currentlyPickedDay != -1)
			dayButtons.get(currentlyPickedDay).setBorder(new JButton().getBorder());
		currentlyPickedDay = dayToSelect;
		dayButtons.get(dayToSelect).setBorder(new LineBorder(Color.RED, 1));
	}	
	
	/**
	 * Method that creates an event on the selected date 
	 * @param String txt
	 */
	private void createEventDialog(String txt) 
	{
		final JDialog dialog = new JDialog();
		dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		final JTextField date = new JTextField(30);
		final JTextField eventText = new JTextField(30);
		final JTextField timeStart = new JTextField(10);
		final JTextField timeEnd = new JTextField(10);
		JButton submit = new JButton("Submit");
		
		submit.addActionListener( new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				dialog.dispose();
				if ( date.getText().split("/").length != 3 
						|| eventText.getText().isEmpty() 
							|| timeStart.getText().isEmpty() 
								|| timeEnd.getText().isEmpty() 
									|| date.getText().isEmpty()) 
				{ return; } 
				else 
				{
					String start = timeStart.getText();
					String end = timeEnd.getText();
					String[] dateArray = date.getText().split("/");
					
					int[] dayOfEvent = new int[] {0,0,0};
					if (dateArray[2].length() == 2) { dateArray[2] = "20" + dateArray[2]; }
					
					for (int i = 0; i < dayOfEvent.length; i++) 
					{
						dayOfEvent[i] = Integer.parseInt(dateArray[i]);
					}
					
					Event ev = new Event(eventText.getText(), 
												Integer.valueOf(start), 
														Integer.valueOf(end));
					LocalDate d = LocalDate.of(dayOfEvent[2], 
														dayOfEvent[0], 
																dayOfEvent[1]);
					boolean timeConflict = model.overlap(d, ev);
					
					if (!timeConflict) { model.addEvent(d, ev); }
					else { createEventDialog("An error occured. Try again!!: "); }
					
					if (!timeConflict && onDayView 
							&& d.equals(model.getCurrentLocalDate())) 
					{ model.getEvents(d); }
                    else if (!timeConflict && onMonthView && dayOfEvent[0] 
                    				== model.getCurrentMonth() + 1) 
                    {
                        LocalDate last = d.plusMonths(1).withDayOfMonth(1).minusDays(1);
                        model.getEvents(LocalDate.of(dayOfEvent[2], dayOfEvent[0], 1), last);
                    }
                    else if (!timeConflict && onWeekView) 
                    {
                        weekView(LocalDate.of(model.getCurrentYear(), 
                        								model.getCurrentMonth() + 1, 
                        									model.getCurrentlySelectedDay()));
                    }
                    else if (!timeConflict && model.getCurrentYear() 
                    			== dayOfEvent[2] && model.getCurrentMonth() + 1 
                    				== dayOfEvent[0] && model.getCurrentlySelectedDay() 
                    					== dayOfEvent[1])
                    { displayDate(dayOfEvent[1]); }
				}
			}
		});
		
		dialog.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.weightx = 1.0;
		c.gridy = 1;
	    c.anchor = GridBagConstraints.LINE_START;
	    
	    JLabel l4 = new JLabel(txt + "Date of Event in following format: mm/dd/yyyy");
	    if (!txt.contentEquals(""))
	    	l4.setForeground(Color.RED);
		dialog.add(l4, c);
	    c.anchor = GridBagConstraints.CENTER;
		c.gridy = 2;
	    c.anchor = GridBagConstraints.LINE_START;
		dialog.add(date, c);
		c.gridy = 4;
		c.weightx = 1.0;
	    c.anchor = GridBagConstraints.LINE_START;
	    
	    JLabel l1 = new JLabel("Name of Event");
	    if (!txt.contentEquals(""))
	    	l1.setForeground(Color.RED);
		dialog.add(l1, c);
		c.gridy = 5;
		c.weightx = 1.0;
		dialog.add(eventText, c);
	    c.anchor = GridBagConstraints.CENTER;
	    c.anchor = GridBagConstraints.CENTER;
		c.gridy = 7;
		c.weightx = 0.0;
	    c.anchor = GridBagConstraints.LINE_START;
	    
	    JLabel l2 = new JLabel(txt + "In 24hr clock system");
	    if (!txt.contentEquals(""))
	    	l2.setForeground(Color.RED);
		dialog.add(l2, c);
	    c.gridy = 8;
		dialog.add(timeStart, c);
		c.gridy = 10;
		
		JLabel l3 = new JLabel(txt + "In 24hr clock system");
	    if (!txt.contentEquals(""))
	    	l3.setForeground(Color.RED);
		dialog.add(l3, c);
		c.gridy = 11;
	    c.anchor = GridBagConstraints.LINE_START;
		dialog.add(timeEnd, c);
		c.anchor = GridBagConstraints.LINE_END;
		
		dialog.add(submit, c);
		dialog.pack();
		dialog.setVisible(true);
	}
	
	/**
	 * Method that adds a day's event to the GUI text area
	 * @param d LocalDate of day's event
	 */
    public void dayView(LocalDate d) 
    { 
        // set layout for week view
        setViewLayout(new DayView());   
        onWeekView = false;
        onMonthView = false;
        onDayView = true;
        model.getEvents(d); 
    }
    
    /**
     * Method that adds a week's event to the GUI text area
     * @param date LocalDate of week's event
     */
    public void weekView(LocalDate date) 
    {
        // set layout for week view
        setViewLayout(new WeekView());
        onWeekView = true;
        onMonthView = false;
        onDayView = false;
        LocalDate start = date;
        LocalDate end = date;
        DayOfWeek day = date.getDayOfWeek();
        int d = day.getValue();
        if (d == 7) { d = 0; }   
        start = start.minusDays(d); 
        end = end.plusDays(7 - d - 1);
        model.getEvents(start,end);
    }
    
    /**
     * Method that adds a month's event to GUI text area
     * @param date LocalDate of month's event
     */
    public void monthView(LocalDate date) 
    {
        // set layout for week view
        setViewLayout(new MonthView());
        onWeekView = false;
        onMonthView = true;
        onDayView = false;
        LocalDate start = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate end = date.plusMonths(1).withDayOfMonth(1).minusDays(1);
        model.getEvents(start, end);
    }
    
    /**
     * Method that shows the selected date and events on that date
     * @param int i
     */
	private void displayDate(int i) 
	{  
		LocalDate d = LocalDate.of(model.getCurrentYear(), 
											model.getCurrentMonth() + 1, i);
		curerntDayOfView = d;
		model.setCurrentlySelectedDay(i);
        model.getEvents(d);
    }
	
	/**
	 * 
	 * @Override
	 */
	public void stateChanged(ChangeEvent e) 
	{
		if ( model.monthStateChanged() 
				|| model.getChangeInDisplay() 
					|| model.getChangeInSelectedDay()) 
		{ 
			textArea.setText(model.getTextDisplay());
			
            if (viewLayout != null)		// Format the layout
            {
                viewLayout.format(textArea, model.getDates(), model.getEvents());
            }
            
            model.resetChangeInTestDisplay();
            model.resetChangeInSelectedDay();
			numOfDaysinMonth = model.getNumberOfDaysInMonth();
			dayButtons.clear();
			monthPanel.removeAll();
			nameOfMonth.setText("" + months[model.getCurrentMonth()] + 
										" " + model.getCurrentYear());
			createDayButtons();
			currentlyPickedDay = -1;
			model.resetChangeOfMonth();
			frameView.pack();
			frameView.repaint();
			pickDay(model.getCurrentlySelectedDay() - 1);
		} else 
		{
			displayDate(model.getCurrentlySelectedDay());
			pickDay(model.getCurrentlySelectedDay() - 1);
		}
	}
	
	/**
	 * Method that sets view layout for day, week, and month views
	 * @param vl View
	 */
    public void setViewLayout(View vl){ this.viewLayout = vl; }
    
    /**
     * Method that gets text area
     * @return text area
     */
    public JTextArea getTextArea() { return textArea; }
    
    
    /**
     * 
     * End of Controller Method Helpers ---------------------------------
     * 
     */
    
    
}

