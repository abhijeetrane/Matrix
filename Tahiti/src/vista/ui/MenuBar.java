 // Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: MenuBar.java

package vista.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** This class implements the pulldown menu on the menu bar.<p>

    Currently, it's mostly a place holder for Vista specifications.

    @author Ron Coleman, PhD
*/
public class MenuBar extends JMenuBar {
  /** Set this to false to suppress debug activity. */
  protected final static boolean DEBUG = false;

  public MenuBar(JFrame owner ) {

    super( );

    /** Create the menus */
    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic(KeyEvent.VK_F);   // alt-F to select menu
    add(fileMenu);

    JMenuItem  Save = new JMenuItem("Save", KeyEvent.VK_S);
    Save.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_1, ActionEvent.ALT_MASK));
    Save.getAccessibleContext().setAccessibleDescription(
            "This doesn't really do anything");
    fileMenu.add(Save);
    Save.addActionListener(new SaveListener()); 




    JMenuItem  Exit = new JMenuItem("Exit", KeyEvent.VK_E);
    Exit.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_1, ActionEvent.ALT_MASK));
    Exit.getAccessibleContext().setAccessibleDescription(
            "This doesn't really do anything");
    fileMenu.add(Exit);
    Exit.addActionListener(new ExitListener(owner ));




    JMenu deleteMenu = new JMenu("Delete");
    deleteMenu.setMnemonic(KeyEvent.VK_D); // alt-D to select menu
    add(deleteMenu);




   JMenuItem  DeleteByClid = new JMenuItem("Delete by Clid", KeyEvent.VK_C);
    DeleteByClid.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_1, ActionEvent.ALT_MASK));
    DeleteByClid.getAccessibleContext().setAccessibleDescription(
            "This doesn't really do anything");
    deleteMenu.add(DeleteByClid);
    DeleteByClid.addActionListener(new DeleteByClidListener(owner ));



   JMenuItem  DeleteBySpid = new JMenuItem("Delete by Spid", KeyEvent.VK_S);
    DeleteBySpid.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_1, ActionEvent.ALT_MASK));
    DeleteBySpid.getAccessibleContext().setAccessibleDescription(
            "This doesn't really do anything");
    deleteMenu.add(DeleteBySpid);
    DeleteBySpid.addActionListener(new DeleteBySpidListener(owner ));


    JMenu exitMenu = new JMenu("Exit");
    exitMenu.setMnemonic(KeyEvent.VK_X);  // alt-X to select menu
    add(exitMenu);
    




   JMenuItem  ExitSubMenu = new JMenuItem("Exit", KeyEvent.VK_E);
    ExitSubMenu.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_1, ActionEvent.ALT_MASK));
    ExitSubMenu.getAccessibleContext().setAccessibleDescription(
            "This doesn't really do anything");
    exitMenu.add(ExitSubMenu);
    ExitSubMenu.addActionListener(new ExitSubListener(owner ));
  }
}
  /** This class handles OK clicks on the inspect dialog. */
  class DeleteByClidListener implements ActionListener {
  /** Dialog pop-up (we'll dispose) */
   private JFrame owner;

  /** Constructor.
     @param dialog Dialog pop-up. */
  public DeleteByClidListener(JFrame owner) {
this.owner=owner;

}

  /** Invoked on button click.
     @param e Action event. */
  public void actionPerformed(ActionEvent e) {
   
   new DeleteDialog(owner);
}
}
  /** This class handles OK clicks on the inspect dialog. */
  class DeleteBySpidListener implements ActionListener {
  /** Dialog pop-up (we'll dispose) */
   private JFrame owner;

  /** Constructor.
     @param dialog Dialog pop-up. */
  public DeleteBySpidListener(JFrame owner) {
this.owner=owner;

}

  /** Invoked on button click.
     @param e Action event. */
  public void actionPerformed(ActionEvent e) {
   
   new DeleteSpidDialog(owner);
}
 
  } 
  
  /** This class handles OK clicks on the inspect dialog. */
  class ExitListener implements ActionListener {
  /** Dialog pop-up (we'll dispose) */
   private JFrame owner;

  /** Constructor.
     @param dialog Dialog pop-up. */
  public ExitListener(JFrame owner) {
this.owner=owner;

}
 
  /** Invoked on button click.
     @param e Action event. */
  public void actionPerformed(ActionEvent e) {
   
   new ExitDialog(owner);
} 
  } 
  
  
  
  /** This class handles OK clicks on the inspect dialog. */
  class ExitSubListener implements ActionListener {
  /** Dialog pop-up (we'll dispose) */
   private JFrame owner;

  /** Constructor.
     @param dialog Dialog pop-up. */
  public ExitSubListener(JFrame owner) {
this.owner=owner;

}

  /** Invoked on button click.
     @param e Action event. */
  public void actionPerformed(ActionEvent e) {
   
   new ExitDialog(owner);
} 
}
  /** This class handles OK clicks on the inspect dialog. */
  class SaveListener implements ActionListener {
  /** Dialog pop-up (we'll dispose) */
   
  	private JFrame owner;
  /** Constructor.
     @param dialog Dialog pop-up. */
  public SaveListener() {
  	this.owner=owner;

}

  /** Invoked on button click.
     @param e Action event. */
  public void actionPerformed(ActionEvent e) {
   new SaveDialog(owner);
  
} 
}
   




