/*
 * Utilaclock -- Simple fullscreen clock app Jesse Morgan <jesse@jesterpm.net>
 */

package net.jesterpm.utilaclock;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * Clock display.
 * 
 * @author jesse
 * @version 25 Oct 2009
 */
public class UtilaclockClockDisplay extends JFrame {
  // Private Constants
  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -2788602417853170710L;

  /**
   * How often do we update the clock.
   */
  private static final int UPDATE_FREQ = 500;
  
  /**
   * Clock Color.
   */
  private static final int CLOCK_COLOR = 0xFFFFFFFF;
  
  /**
   * Countdown Color.
   */
  private static final int COUNT_DOWN_COLOR = 0xFF00FF00;
  
  /**
   * Count up Color.
   */
  private static final int COUNT_UP_COLOR = 0xFF0000FF;
  
  /**
   * String representing GMT.
   */
  private static final String TIMEZONE_GMT = "GMT";
  
  // Instance fields
  /**
   * The label containing the time.
   */
  private JLabel my_label;
  
  /**
   * Snowman.
   */
  private JLabel my_snowman;

  /**
   * End time for countdowns.
   */
  private Date my_final_time;

  /**
   * Our timer.
   */
  private Timer my_timer;

  /**
   * Constructor to setup a clock.
   */
  public UtilaclockClockDisplay() {
    my_final_time = null;
    setupClock();
  }

  /**
   * Constructor to setup a countdown clock.
   * 
   * @param the_final_time The end time for this countdown.
   */
  public UtilaclockClockDisplay(final Date the_final_time) {
    my_final_time = the_final_time;
    setupClock();
  }
  
  /**
   * Change the final countdown time.
   * 
   * @param the_final_time The end time.
   */
  public void setFinalTime(final Date the_final_time) {
    my_final_time = the_final_time;
    updateClock();
  }
  
  /**
   * Enable the countdown.
   */
  public void start() {
    my_timer.start();
  }
  
  /**
   * Disable the countdown.
   */
  public void stop() {
    my_timer.stop(); 
  }
  
  /**
   * Set the size of the font on the clock.
   * @param the_size The font size.
   */
  public void setSize(final float the_size) {
    final Font font = my_label.getFont().deriveFont(the_size);
    my_label.setFont(font);
    pack();
  }

  /**
   * Utility to setup the clock window and start it going.
   */
  private void setupClock() {
    // Default close opperation and background color
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setBackground(new Color(0));
    setLayout(new FlowLayout());

    addWindowListener(new WindowAdapter() {
      public void windowClosed(final WindowEvent the_event) {
        my_timer.stop();
      }
    });
    
    addMouseListener(new MouseAdapter() {
      public void mouseClicked(final MouseEvent the_event) {
        if (the_event.getClickCount() == 2) {
          my_snowman.setVisible(!my_snowman.isVisible());
        }
      }
    });

    // Create our label
    my_label = new JLabel();

    // Set the font and color and such
    final Font font = new Font("Arial Black", Font.PLAIN, 200);
    my_label.setForeground(new Color(CLOCK_COLOR));
    my_label.setFont(font);
    my_label.setHorizontalAlignment(SwingConstants.CENTER);
    my_label.setVerticalAlignment(SwingConstants.CENTER);

    // Add the label to the window
    add(my_label);
    
    // Snowman?
    my_snowman = new JLabel(
              new ImageIcon(UtilaclockClockDisplay.class.getResource("/resources/snowman.gif")));
    
    add(my_snowman);
    
    my_snowman.setVisible(false);

    // Update the clock before packing
    updateClock();

    // Schedule Updates
    my_timer = new Timer(UPDATE_FREQ, new ActionListener() {
      public void actionPerformed(final ActionEvent the_event) {
        updateClock();
      }
    });

    // and pack.
    pack();
  }

  /**
   * Update the clock to the correct time.
   */
  private void updateClock() {
    final Calendar cal = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");

    // If this is a count down, we do some extra work
    if (my_final_time != null) {
      df = new SimpleDateFormat("HH:mm:ss");
      
      // First off, we switch to GMT to solve timezone problems.
      df.setTimeZone(TimeZone.getTimeZone(TIMEZONE_GMT));
      cal.setTimeZone(TimeZone.getTimeZone(TIMEZONE_GMT));

      // Get the difference between the final time and now
      final long timediff = my_final_time.getTime() - cal.getTimeInMillis();

      if (timediff > 0) {
        // We're counting down, set the time
        cal.setTimeInMillis(timediff);
        // and make the color green
        my_label.setForeground(new Color(COUNT_DOWN_COLOR));

      } else {
        // We're counting up, so set the time to the negative of the time
        // difference
        cal.setTimeInMillis(-timediff);
        // And color it blue
        my_label.setForeground(new Color(COUNT_UP_COLOR));
      }
    }

    // Set the label as needed
    my_label.setText(df.format(cal.getTime()));
  }
}
