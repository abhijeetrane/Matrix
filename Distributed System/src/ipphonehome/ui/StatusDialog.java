//Team: Lanikai
//Date: 2005/10/24
//Vers: 1.0
//File: StatusDialog.java

package ipphonehome.ui;
import javax.swing.*;
import java.awt.*;

/** Presents a modal "Please Wait" type dialog while processing continues.

    CAVEAT: Does not work as expected if invoked from the event dispatch thread.
    (The dialog will not be presented until the current event dispatch completes)
    To make work correctly, <em>show</em> must be called from within a different thread.
 */
public class StatusDialog extends JDialog {
    /** Constructs a new StatusDialog

      @param frame Parent frame.
      @param message the string to be displayed
   */
  public StatusDialog(JFrame frame, String message) {
    super(frame,"IP Phone Home Logon",true);

    Container contentPane = getContentPane();
    contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.X_AXIS));
   // contentPane.setBackground(Color.yellow);

    
    JLabel msgLabel = new JLabel(message);
    msgLabel.setForeground(Color.black);

    
    contentPane.add(Box.createHorizontalStrut(5));
    contentPane.add(msgLabel);

    setBounds(125,125,410,100);
  }

  /** Presents the modal dialog in a new thread.
      WARNING: This will not work correctly if called from the Event Dispatch thread!
      Get yourself into a new thread before calling <em>show</em>.
  */
  public void show() {
    // A new thread, from which we can safely (?) invoke (blocking) show
    Thread thread = new Thread() {
      public void run() {
        StatusDialog.super.show();
      }
    };

    thread.start();

    // Give up the processor so the show thread can execute.
    Thread.yield();
  }

  /** Disposes the dialog after Swing event pipeline is empty, ie, as a low priority.
  */
  public void dispose() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        StatusDialog.super.dispose();
      }
    });
  }
}
