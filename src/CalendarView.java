/**
 * CalendarView.java
 * @author Anonymous
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Font;
import java.time.DayOfWeek;

/*
 * This class is for following the MVC pattern, which this class is for the view method.
 * In this class, we set up the view of our button, panel and frame for our calendar.
 */
public class CalendarView implements ChangeListener {
	private CalendarMock mock;
	private MONTHS[] months = MONTHS.values();
	private int currentlyPickedDay = -1;
	private int numOfDaysinMonth;
	private JFrame frameView = new JFrame();
	private JPanel monthPanel = new JPanel();
	private JLabel nameOfMonth = new JLabel();
	private JButton create = new JButton("Create an Event");
	private JButton file = new JButton("Add File");
	private JButton agenda = new JButton("Agenda");
	private JTextArea textArea = new JTextArea();
	private ArrayList<JButton> dayButtons = new ArrayList<JButton>();
	private JButton monthButton = new JButton("Month");
	private JButton weekButton = new JButton("Week");
	private JButton dayButton = new JButton("Day");
	private JButton prev = new JButton("<");
	private JButton next = new JButton(">");
	private JButton today = new JButton("Today");
	boolean onDayView;
	boolean onMonthView;
	boolean onWeekView;
	LocalDate curerntDayOfView; /// NOTE: this doesn't have to be the same as the currentlySelectedDay
	//view layout that shows different format based on day, week or month
    private ViewLayout viewLayout;
	// CONTROLLER
	/**
	 * Creates buttons representing all the days in the current month and adds them
	 * to an array list and adds them to the month panel.
	 */
	private void createDayButtons() {
		for (int x = 0; x < mock.getDayInWeek(1) - 1; x++) { // this adds blank buttons to make sure the days are in the correct spots
			JButton filling = new JButton();
				filling.setEnabled(false);
				monthPanel.add(filling);
			}
			for (int i = 1; i <= numOfDaysinMonth; i++) {
				final int j = i;
				JButton day = new JButton(Integer.toString(i));
				day.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						onDayView = false;
                    	onMonthView = false;
                    	onWeekView = false;
						prev.setEnabled(false);
						next.setEnabled(false);
						pickDay(j - 1); // Local variable i defined in an enclosing scope must be final or effectively final
						displayDate(j);   // Local variable i defined in an enclosing scope must be final or effectively final
						create.setEnabled(true);
					}
				});
				monthPanel.add(day);  // add each button to month panel
				dayButtons.add(day); // add each button to arraylist containing the month's day buttons
			}
		}
	/**
	 * Constructs the calendar.
	 * @param mock the  mock that stores and manipulates calendar data
	 */
	//CONTROLLER
	public CalendarView(CalendarMock m) { 
		this.mock = m;
		numOfDaysinMonth = mock.getNumberOfDaysInMonth();
		monthPanel.setLayout(new GridLayout(0, 7));
		textArea = new JTextArea(30, 50);
		textArea.setEditable(false);
		textArea.setFont(new Font("monospaced", Font.PLAIN, 18));
		createDayButtons();
		displayDate(mock.getCurrentlySelectedDay());  
		pickDay(mock.getCurrentlySelectedDay() - 1);   
		prev.setEnabled(false);
		next.setEnabled(false);
		onDayView = true;
		next.addActionListener(new ActionListener()  {
			@Override
				public void actionPerformed(ActionEvent e) {
				if (onMonthView){
                    curerntDayOfView = curerntDayOfView.plusMonths(1).withDayOfMonth(1);
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
		today.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						LocalDate current = LocalDate.now();
						curerntDayOfView = current;
						if (onMonthView)
                            monthView(current);
                        else if (onWeekView)
                            weekView(current);
                        else { // on day view
                            dayView(current);
                        }
                    }
		});
		
		prev.addActionListener(new ActionListener()  {
					@Override
						public void actionPerformed(ActionEvent e) {
						prev.setEnabled(true);
						next.setEnabled(true);
						if (onMonthView) {
                            curerntDayOfView = curerntDayOfView.minusMonths(1).withDayOfMonth(1);       
                            monthView(curerntDayOfView);
                        }
                        else if (onWeekView) {
                            curerntDayOfView = curerntDayOfView.minusDays(7);
                            weekView(curerntDayOfView);
                        }
                        else { // on day view
                            curerntDayOfView = curerntDayOfView.minusDays(1);
                            dayView(curerntDayOfView);
                        }
					}
		});
		monthButton.addActionListener(new ActionListener() {
					@Override
						public void actionPerformed(ActionEvent e) {
						monthView(LocalDate.of(mock.getCurrentYear(),  mock.getCurrentMonth() + 1, mock.getCurrentlySelectedDay()));
                        prev.setEnabled(true);
                        next.setEnabled(true);
						}
		});
		weekButton.addActionListener(new ActionListener() {
					@Override
						public void actionPerformed(ActionEvent e) {
                        prev.setEnabled(true);
                        next.setEnabled(true);
                        weekView(LocalDate.of(mock.getCurrentYear(),  mock.getCurrentMonth() + 1, mock.getCurrentlySelectedDay()));
                    }
		});
		dayButton.addActionListener(new ActionListener()  {
					@Override
						public void actionPerformed(ActionEvent e) {
						prev.setEnabled(true);
                        next.setEnabled(true);
                        dayView(LocalDate.of(mock.getCurrentYear(),  mock.getCurrentMonth() + 1, mock.getCurrentlySelectedDay()));
                    }
		});
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createEventDialog("");
			}
		});
		JButton prevMonth = new JButton("<");
		prevMonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mock.previousMonth();
			}
		});
		JButton nextMonth = new JButton(">");
		nextMonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mock.nextMonth();
			}
		});
		agenda.addActionListener(new  ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JDialog dialog = new JDialog();
				dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				final JTextField startDay = new JTextField(15);
				final JTextField endDay = new JTextField(15);
				JButton submit = new JButton("Submit");
				submit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (startDay.getText().isEmpty() || endDay.getText().isEmpty()) 
							return;
						else {
							dialog.dispose();							
							String[] start = startDay.getText().split("/");
							String[] end = endDay.getText().split("/");
							if (start[2].length() == 2) {
								start[2] = "20" + start[2];
							}
							if (end[2].length() == 2) {
								end[2] = "20" + end[2];
							}
							int[] startArray = new int[] {0,0,0};
							for (int i = 0; i < start.length; i++) {
								startArray[i] = Integer.parseInt(start[i]);
							}
							int[] endArray = new int[] {0,0,0};
							for (int i = 0; i < end.length; i++) {
								endArray[i] = Integer.parseInt(end[i]);
							}
							LocalDate d1 = LocalDate.of(startArray[2], startArray[0], startArray[1]);
							LocalDate d2 = LocalDate.of(endArray[2], endArray[0], endArray[1]);
							mock.getEvents(d1,d2);	
						}
					}
				});
				dialog.setLayout(new GridBagLayout());
				GridBagConstraints gridContraints = new GridBagConstraints();
				dialog.add(new JLabel("start day mm/dd/yyyy"), gridContraints);
				dialog.add(new JLabel("End day mm/dd/yyyy"), gridContraints);
				gridContraints.gridy = 2;
				dialog.add(startDay, gridContraints);
				dialog.add(endDay, gridContraints);
				dialog.add(submit, gridContraints);
				dialog.pack();
				dialog.setVisible(true);
			}
		});
		file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JDialog dialog = new JDialog();
				dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				final JTextField fileNameInput = new JTextField(30);
				JButton submit = new JButton("Submit");
				submit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (fileNameInput.getText().isEmpty()) {
							return;
						}
						else {
							dialog.dispose();
							String fileName = fileNameInput.getText();
							try {
								mock.fileReader(fileName);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
							}
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
		}); // END OF CONTROLLER
		JPanel titleContainer = new JPanel();
		titleContainer.setLayout(new FlowLayout());
		JPanel containsMonth = new JPanel();
		containsMonth.setLayout(new BorderLayout());
		JPanel finalContainer = new JPanel();
		JPanel btnsPanel5 = new JPanel((new FlowLayout(FlowLayout.CENTER, 0, 0)));
		finalContainer.setLayout(new BorderLayout());
		nameOfMonth.setText("" + months[mock.getCurrentMonth()] + " " + mock.getCurrentYear());
		titleContainer.add(prevMonth);
		titleContainer.add(nameOfMonth);
		titleContainer.add(nextMonth);
		btnsPanel5.add(create);
		containsMonth.add(titleContainer, BorderLayout.NORTH);
		containsMonth.add(new JLabel("      S                M              T              W                T                 F                 S"), BorderLayout.CENTER);
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
		// btnsPanel.add(file);
		btnsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 30)); 
		FlowLayout layout = (FlowLayout)btnsPanel2.getLayout();
		layout.setVgap(0);
		btnsPanel.add(create);
		
		//Agenda, create, file view
		constraints.gridx = 0;
		constraints.gridy = 1;
		dayViewPanel.add(btnsPanel, constraints);
		//today, day, week, month view
		constraints.gridx = 0;
		constraints.gridy = 2;
		dayViewPanel.add(btnsPanel2, constraints);
		//Event view
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 3;
		JScrollPane dayScrollPane = new JScrollPane(textArea);
		dayScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		dayViewPanel.add(dayScrollPane, constraints);
		finalContainer.add(containsMonth, BorderLayout.NORTH);
		frameView.add(finalContainer);
		frameView.add(dayViewPanel);
		frameView.setLayout(new FlowLayout());
		frameView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameView.pack();
		frameView.setVisible(true);
	}
	// CONTROLLER HELPER METHODS
	/**
	 * creates a border around selected date and puts a border around current date 
	 * when the program first starts up
	 * @param dayToSelect day to select
	 */
	private void pickDay(int dayToSelect) {  
		if (currentlyPickedDay != -1)  // DONT REMOVE
			dayButtons.get(currentlyPickedDay).setBorder(new JButton().getBorder());
		currentlyPickedDay = dayToSelect;
		dayButtons.get(dayToSelect).setBorder(new LineBorder(Color.RED, 1));
	}	
	/**
	 * Creates an event on the selected date through user input.
	 * @param txt String
	 */
	private void createEventDialog(String txt) {
		final JDialog dialog = new JDialog();
		dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		final JTextField date = new JTextField(30);
		final JTextField eventText = new JTextField(30);
		final JTextField timeStart = new JTextField(10);
		final JTextField timeEnd = new JTextField(10);
		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				if (date.getText().split("/").length != 3 ||eventText.getText().isEmpty() || timeStart.getText().isEmpty() || timeEnd.getText().isEmpty() || date.getText().isEmpty()) {
					return;
				} else {
					String start = timeStart.getText();
					String end = timeEnd.getText();
					String[] dateArray = date.getText().split("/");
					int[] dayOfEvent = new int[] {0,0,0};
					if (dateArray[2].length() == 2)
						dateArray[2] = "20" + dateArray[2];
					for (int i = 0; i < dayOfEvent.length; i++) {
						dayOfEvent[i] = Integer.parseInt(dateArray[i]);
					}
					Event ev = new Event(eventText.getText(), Integer.valueOf(start), Integer.valueOf(end));
					LocalDate d = LocalDate.of(dayOfEvent[2], dayOfEvent[0], dayOfEvent[1]);
					boolean timeConflict = mock.overlap(d, ev);
					if (!timeConflict)
						mock.addEvent(d, ev);
					else {
						createEventDialog("An error occured. Try again!!: ");
					}
					if (!timeConflict && onDayView && d.equals(mock.getCurrentLocalDate())) {
                        mock.getEvents(d);
                    }
                    else if (!timeConflict && onMonthView && dayOfEvent[0] == mock.getCurrentMonth() + 1) {
                        LocalDate last = d.plusMonths(1).withDayOfMonth(1).minusDays(1);
                        mock.getEvents(LocalDate.of(dayOfEvent[2], dayOfEvent[0], 1), last);
                    }
                    else if (!timeConflict && onWeekView) {
                        weekView(LocalDate.of(mock.getCurrentYear(), mock.getCurrentMonth() + 1, mock.getCurrentlySelectedDay()));
                    }
                    else if (!timeConflict && mock.getCurrentYear() == dayOfEvent[2] && mock.getCurrentMonth() + 1 == dayOfEvent[0] && mock.getCurrentlySelectedDay() == dayOfEvent[1])
                        displayDate(dayOfEvent[1]);
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
	    JLabel l4 = new JLabel(txt + "date of event in following format: mm/dd/yyyy");
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
     * to get adds a day's events to the GUI's text area
     * @param d LocalDate of day's events 
     *  
     */
    public void dayView(LocalDate d) { 
        //set layout for week view
        setViewLayout(new DayLayout());   
        onWeekView = false;
        onMonthView = false;
        onDayView = true;
        mock.getEvents(d); 
    }
    /**
     *  @param date LocalDate in a week to get the entire week's events adds a week's 
     *  events to the GUI's text area
     */
    public void weekView(LocalDate date) {
        //set layout for week view
        setViewLayout(new WeekLayout());
        onWeekView = true;
        onMonthView = false;
        onDayView = false;
        LocalDate start = date;
        LocalDate end = date;
        DayOfWeek day = date.getDayOfWeek();
        int d = day.getValue();
        if (d == 7) 
            d = 0;   
        start = start.minusDays(d); 
        end = end.plusDays(7 - d - 1);
        mock.getEvents(start,end);
    }
    /**
     * @param date LocalDate in a month to get the entire month's events
     * adds a month's events to the GUI's text area
     */
    public void monthView(LocalDate date) {
        //set layout for week view
        setViewLayout(new MonthLayout());
        onWeekView = false;
        onMonthView = true;
        onDayView = false;
        LocalDate start = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate end = date.plusMonths(1).withDayOfMonth(1).minusDays(1);
        mock.getEvents(start, end);
    }
	/**
	 * Shows the selected date and events on that date.
	 * @param i the selected date
	 */
	private void displayDate(int i) {  
		LocalDate d = LocalDate.of(mock.getCurrentYear(), mock.getCurrentMonth() + 1, i);
		curerntDayOfView = d;
		mock.setCurrentlySelectedDay(i);
        mock.getEvents(d);
    }
	@Override
	public void stateChanged(ChangeEvent e) {
		if (mock.monthStateChanged()|| mock.getChangeInDisplay() || mock.getChangeInSelectedDay()) {
			textArea.setText(mock.getTextDisplay());
			//format the layout
            if (viewLayout != null){
                viewLayout.format(textArea, mock.getDates(), mock.getEvents());
            }
            mock.resetChangeInTestDisplay();
            mock.resetChangeInSelectedDay();
			numOfDaysinMonth = mock.getNumberOfDaysInMonth();
			dayButtons.clear();
			monthPanel.removeAll();
			nameOfMonth.setText("" + months[mock.getCurrentMonth()] + " " + mock.getCurrentYear());
			createDayButtons();
			currentlyPickedDay = -1;
			mock.resetChangeOfMonth();
			frameView.pack();
			frameView.repaint();
			pickDay(mock.getCurrentlySelectedDay() - 1);
		} else {
			displayDate(mock.getCurrentlySelectedDay());
			pickDay(mock.getCurrentlySelectedDay() - 1);
		}
	}
	/**
     * sets view layout for day, week, and month views
     * @param vl Viewlayout 
     */
    public void setViewLayout(ViewLayout vl){
        this.viewLayout = vl;
    }
    /**
     * get text area 
     * @return text area
     */
    public JTextArea getTextArea() {
        return textArea;
    }
}