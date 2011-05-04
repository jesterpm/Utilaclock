/*
 * Utilaclock -- Simple fullscreen clock app Jesse Morgan <jesse@jesterpm.net>
 */

package net.jesterpm.utilaclock;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Management GUI.
 * 
 * @author Jesse Morgan <jesse@jesterpm.net>
 * @version 25 Oct 2009
 */
public class UtilaClockManager extends JFrame {
  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 8838459061020873829L;

  /**
   * Font size text field.
   */
  private JTextField my_font_size;

  /**
   * End time for countdown.
   */
  private JTextField my_end_time;
  
  /**
   * Amount of time for the timer.
   */
  private JTextField my_timer_time;
  
  private final UtilaclockClockDisplay my_timer_window;

  /**
   * Clock Manager Constructor.
   */
  public UtilaClockManager() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle("Utilaclock");
    setResizable(false);
    setLayout(new GridLayout(0, 1));

    addWindowListener(new WindowAdapter() {
      public void windowClosed(final WindowEvent the_event) {
        for (Frame f : getFrames()) {
          f.dispose();
        }
      }
    });
    
    my_timer_window = new UtilaclockClockDisplay();
    
    setupButtons();

    add(getCountdownUI());

    add(getOptionsUI());

    pack();
  }
  
  /**
   * Creates the row of buttons and their listeners.
   * 
   * @return JPanel with buttons.
   */
  public void setupButtons() {
    final JPanel buttons = new JPanel(new FlowLayout());

    final JButton clockButton = new JButton("Create Clock");
    clockButton.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent the_event) {
        try {
          final float size = Float.parseFloat(my_font_size.getText());
          
          final UtilaclockClockDisplay clock = new UtilaclockClockDisplay();
          clock.setSize(size);
          clock.setVisible(true);
          clock.start();
        
        } catch (final NumberFormatException nf_exception) {
          JOptionPane.showMessageDialog(null, "Invalid Font Size.");
        }
      }
    });

    buttons.add(clockButton);

    final JButton countup_button = new JButton("Create Timer");
    countup_button.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent the_event) {
        try {
          final float size = Float.parseFloat(my_font_size.getText());
          
          final UtilaclockClockDisplay clock = new UtilaclockClockDisplay(new Date());
          clock.setSize(size);
          clock.setVisible(true);
          clock.start();
          
        } catch (final NumberFormatException nf_exception) {
          JOptionPane.showMessageDialog(null, "Invalid Font Size.");
        }
      }
    });

    final JButton countdown_button = new JButton("Create Countdown");
    countdown_button.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent the_event) {
        final DateFormat df = DateFormat.getDateTimeInstance();

        try {
          final Date d = df.parse(my_end_time.getText());
          final float size = Float.parseFloat(my_font_size.getText());
          
          final UtilaclockClockDisplay clock = new UtilaclockClockDisplay(d);
          clock.setSize(size);
          clock.setVisible(true);
          clock.start();

        } catch (final ParseException exception) {
          JOptionPane.showMessageDialog(null, "Invalid Date.");
        
        } catch (final NumberFormatException nf_exception) {
          JOptionPane.showMessageDialog(null, "Invalid Font Size.");
        }
      }
    });
    
    final JButton about = new JButton("About");
    about.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent the_event) {
        JOptionPane.showMessageDialog(UtilaClockManager.this, "Created by Jesse Morgan <jesse@jesterpm.net>.\n Version: $Id: UtilaClockManager.java 57 2010-10-17 05:40:34Z jesse $");
      }
    });

    
    
    buttons.add(countdown_button);

    buttons.add(countup_button);
    
    buttons.add(about);

    add(buttons);
    
    final JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    row2.add(new JLabel("Time Time (seconds): "));
    
    my_timer_time = new JTextField("90");
    row2.add(my_timer_time);
    
    final JCheckBox cbox = new JCheckBox("Enable Timer");
    cbox.addActionListener(new ActionListener() {
      
      public void actionPerformed(final ActionEvent the_event) {
        if (((JCheckBox) the_event.getSource()).isSelected()) {
          try {
            final int time = Integer.parseInt(my_timer_time.getText());
            
            Date d = new Date(System.currentTimeMillis() + 1000 * time);
            
            my_timer_window.setFinalTime(d);
            my_timer_window.start();
            my_timer_window.setVisible(true);
          
          } catch (final NumberFormatException the_e) {
            JOptionPane.showMessageDialog(null, "You must enter an integer.");
          }
          
        } else {
          my_timer_window.stop();
        }
        
      }
    });
    
    row2.add(cbox);
    
    final JButton timer_button = new JButton("Show Timer");
    timer_button.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent the_event) {
        try {
          final int time = Integer.parseInt(my_timer_time.getText());
          my_timer_window.setFinalTime(new Date(System.currentTimeMillis() + 1000 * time));
          my_timer_window.setVisible(true);
        
        } catch (final NumberFormatException the_e) {
          JOptionPane.showMessageDialog(null, "You must enter an integer.");
        }
      }
    });
    
    row2.add(timer_button);
    
    add(row2);
  }

  /**
   * Creates the Countdown Configuration UI.
   * 
   * @return JPanel with Countdown configuration UI
   */
  private JPanel getCountdownUI() {
    final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.setBorder(BorderFactory.createTitledBorder("Count Down"));

    final DateFormat df = DateFormat.getDateTimeInstance();

    panel.add(new JLabel("Count down ends at:"));

    my_end_time = new JTextField();
    my_end_time.setText(df.format(new Date()));
    panel.add(my_end_time);

    return panel;
  }

  /**
   * Creates the general options UI.
   * 
   * @return JPanel with general options.
   */
  private JPanel getOptionsUI() {
    final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.setBorder(BorderFactory.createTitledBorder("Options"));

    panel.add(new JLabel("Font size:"));

    my_font_size = new JTextField();
    my_font_size.setText("200");
    panel.add(my_font_size);

    panel.add(new JLabel("pt"));

    return panel;
  }
  
  
  
  /**
   * Program Entry-point.
   * 
   * @param the_args Command line arguments
   */
  public static void main(final String[] the_args) {
    final UtilaClockManager manager = new UtilaClockManager();

    manager.setVisible(true);
  }
}
