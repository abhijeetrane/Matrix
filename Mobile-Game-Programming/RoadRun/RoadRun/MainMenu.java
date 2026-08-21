//File:MainMenu.java
//Team:Cudjoe
//Date:10/04/2005
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;

/**
 * A simple menu system for Road Run using commmands.
 * @author Cudjoe
 */
public class MainMenu extends Form implements CommandListener
{
   private RoadRun theMidlet;
   private Command start;
   private Command exit;

   /**
    * Creates a new menu object (whilst retaining a reference to it's parent
    * midlet).
    * @param midlet The MIDlet this menu belongs to (used to later call back
    * to activate the game screen as well as exit.
    */
   protected MainMenu(RoadRun midlet)
   {
      // the Form constructor
      super("");

      // a connection to the midlet
      theMidlet = midlet;

      // add the welcome text
      append("\nWelcome to\n");
      append("R-O-A-D R-U-N");

      // create and add the commands
      int priority = 1;
      start = new Command("Start", Command.SCREEN, priority++);
      exit = new Command("Exit", Command.EXIT, priority++);

      addCommand(start);
      addCommand(exit);

      setCommandListener(this);
   }

   /**
    * Handles the start (activate game screen) and exit commands. All the work
    * is done by the RoadRun class.
    * @param command the command that was triggered
    * @param displayable the displayable on which the event occurred
    */
   public void commandAction(Command command, Displayable displayable)
   {
      if (command == start)
         theMidlet.activateGameScreen();
      if (command == exit){
        theMidlet.close();

}
   }
}
