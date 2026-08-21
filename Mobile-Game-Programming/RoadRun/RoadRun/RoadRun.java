//File:RoadRun.java
//Team:Cudjoe
//Date:10/04/2005
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * The application class for the game RoadRun.
 * @author Cudjoe
 */

public class RoadRun extends MIDlet implements CommandListener
{
   private MainMenu menu;
   private RMSGameScores rmsgscore;
   private Command save;
   private TextField playername;
   private Form playerform;
   private int highscore;
   /**
    * MIDlet constructor instantiates the main menu.
    */
   public RoadRun()
   {
      menu = new MainMenu(this);
      rmsgscore = new RMSGameScores();

   }

   /**
    * Handles Application Manager notification the MIDlet is starting (or
    * resuning from a pause). In this case we set the menu as the current
    * display screen.
    * @throws MIDletStateChangeException
    */
   public void startApp() throws MIDletStateChangeException
   {
      Display.getDisplay(this).setCurrent(menu);


   }

   /**
    * Handles Application Manager notification the MIDlet is about to be paused.
    * We don't bother doing anything for this case.
    */
   public void pauseApp()
   {
   }

   /**
    * Handles Application Manager notification the MIDlet is about to be
    * destroyed. We don't bother doing anything for this case.
    */
   public void destroyApp(boolean unconditional) throws MIDletStateChangeException
   {
   }

   /**
    * Called by the game classes to indicate to the user that the current game
    * is over.
    */
   public void gameOver()
   {
     /*A form is generated to ask the player for name if the score is higher than any
	  score in recordstore*/
         if(GameScreen.getScore() >0){
         if(rmsgscore.getNumRecords()<3 || GameScreen.getScore() >= rmsgscore.lowScore()){
	        playerform = new Form("Login");
	        playername = new TextField("Name","" , 32, 0);
		    playerform.append(playername);
		    playerform.append("Score");
		    playerform.append(""+GameScreen.getScore()+"");

		    // create and add the commands
		    int priority = 1;
	        save = new Command("SAVE", Command.OK, priority++);
    	    playerform.addCommand(save);
            playerform.setCommandListener(this);
	       Display.getDisplay(this).setCurrent(playerform);
        }
      }else{ // Display an alert (the device will take care of implementing how to
      // show it to the user.
       Alert alert = new Alert("", "Splat! Game Over", null, AlertType.INFO);
      // Then set the menu to be current.
      Display.getDisplay(this).setCurrent(alert, menu);
    }
 }


   /**
    * A utility method to shutdown the application.
    */
   public void close()
   {


      try
      {
         destroyApp(true);
         notifyDestroyed();
      }

      catch (MIDletStateChangeException e)
      { }
   }

   /**
    * A utility method to create and then activate a new GameScreen.
    */
   public void activateGameScreen()
   {
      GameScreen gameScreen = new GameScreen(this);
      Display.getDisplay(this).setCurrent(gameScreen);
    }
  /**
   * It handles the saving of the player score and name in the recordstore.If playername
   * is less than 6 characters or blank it displays a alert message and asks for playername
   * again.
   */
   public void commandAction(Command command, Displayable displayable)
    {
      if (command == save){
         if(playername.size()==0){
			  // Display an alert (the device will take care of implementing how to
			       // show it to the user.
			      Alert alertnull = new Alert("", "Player name cannot be blank", null, AlertType.INFO);
			       // Then set the menu to be current.
                  Display.getDisplay(this).setCurrent(alertnull, playerform);
		 }else {
			 if(playername.size()<6){
			 // Display an alert (the device will take care of implementing how to
	        // show it to the user.
  		      Alert alertnull = new Alert("", "Player name should be atleast 6 characters ", null, AlertType.INFO);
	       // Then set the menu to be current.
               Display.getDisplay(this).setCurrent(alertnull, playerform);
			 }else{
         if(rmsgscore.getNumRecords()<3){
            rmsgscore.addScore(GameScreen.getScore(),playername.getString());
		}else {
			if(GameScreen.getScore() >=rmsgscore.lowScore()){
					rmsgscore.deletelowest();
					rmsgscore.addScore(GameScreen.getScore(),playername.getString());
				}
	         }//end of else
   		  GameScreen.resetScore();
          Display.getDisplay(this).setCurrent(menu);
	 }
  //end of else for checking 6 characters
  }
  //end of else for null character
  }
   }
   /**
    * It returns the highest score from the recordstore.
    * @return highscore
    */
   public int getHighScore(){
     return rmsgscore.getHighScore();
   }

}




