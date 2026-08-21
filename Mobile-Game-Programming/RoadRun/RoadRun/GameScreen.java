//File:GameScreen.java
//Team:Cudjoe
//Date:10/04/2005
import javax.microedition.lcdui.*;
import java.util.Vector;
import java.util.Timer;
/**
 * The main drawing and control class for the game Road Run.
 * @author Cudjoe
 */
public class GameScreen extends Canvas implements Runnable
{
   private RoadRun theMidlet;
   private int cps;
   private int cyclesThisSecond;
   private long lastCPSTime = 0;
   private long lastCycleTime;
   private Vector actorList;
   private WombatActor wombat;

   // Rendering setup
   private static int statusLineHeight = 10;
   private int laneHeight = 0;
   private static final int topMargin = 3;

   // Game state
   private Image offScreenBuffer;
   private boolean running=true;
   private static int score=0;
   private CountDown countdown = new CountDown(120);
   /**
    * Constructor for a GameScreen.
    * @param midlet A reference to the MIDlit controlling the game.
    */
   public GameScreen(RoadRun midlet)
   {
      theMidlet = midlet;

      // create the game thread
      Thread t = new Thread(this);
      t.start();

      new Timer().schedule(countdown, 0, 1000);
      initResources();
   }

   /**
    * convenience method to return the y position of a lane number
    * @param lane The lane position
    * @return The y postiion of the given lane
    */
   public int getLaneYPos(int lane)
   {
      return (lane * laneHeight) + 1 + topMargin;
   }

   /**
    * Initialize the resources for the game. Should be called to setup the game
    * for play.
    */
   private void initResources()
   {
      offScreenBuffer = Image.createImage(getWidth(), getHeight());
      int heightMinusStatus = getHeight() - statusLineHeight;
      laneHeight = ((heightMinusStatus) / 9);

      actorList = new Vector();

      // add the actors
      wombat = new WombatActor(this, getWidth() / 2, getLaneYPos(8));
      actorList.addElement( wombat );

      // add a car
      for (int i=1; i < 4; i++)
      {
         actorList.addElement(new TruckActor(this, 1, getLaneYPos(i), (i * 2) + 1));
         actorList.addElement(new CarActor(this,getWidth()/2, getLaneYPos(i), (i*2)+1));
      }

      for (int i = 5; i < 8; i++)
      {
         actorList.addElement(new TruckActor(this, 0, getLaneYPos(i), i-3));
         actorList.addElement(new CarActor(this, getWidth() / 2, getLaneYPos(i),i-3 ));
      }

   }

   /**
    * Number of cycles we want to have per second.
    */
   private static final int MAX_CPS = 50;

   /**
    * The number of milliseconds allowed per cycle/frame.
    */
   private static final int MS_PER_FRAME = 1000 / MAX_CPS;

   /**
    * Called when thread is started. Controls the main game loop including the
    * framerate based on the timing set in MS_PER_FRAME. On every cycle it
    * calls the cycle and repaint methods.
    */
   public void run()
   {
      while(running)
      {
         // remember the starting time
         long cycleStartTime = System.currentTimeMillis();

         // run the cycle
         cycle();
         repaint();
        //to stop the game if the clock completes 120 seconds 
         if(countdown.getTime() == 0) {
         running = false;

      }

         // update the CPS
         if (System.currentTimeMillis() - lastCPSTime > 1000)
         {
            lastCPSTime=System.currentTimeMillis();
            cps = cyclesThisSecond;
            cyclesThisSecond = 0;
         } else
            cyclesThisSecond++;

         // Here we calculate how much time has been used so far this cycle. If
         // it is less than the amount of time we should have spent then we
         // sleep a little to let the MIDlet get on with other work.
         long timeSinceStart = (cycleStartTime - System.currentTimeMillis());
         if (timeSinceStart < MS_PER_FRAME)
         {
            try
            {
               Thread.sleep(MS_PER_FRAME - timeSinceStart);
            }
            catch(java.lang.InterruptedException e)
            { }
         }
      }

      // If we've reached this point then the running boolean has been set to
      // false by something (such as a quit command) and it's time to fall back
      // to the menu system. The gameOver method displays an alert telling the
      // user their time has come and then returns to the menu.
       //if(score<highscore)
      theMidlet.gameOver();


}

   /**
    * Handles the cycling of all the Actors in the world by calculating the
    * elapsed time and then call the cycle method on all Actors in the local
    * Vector. At the end this method also checks to see if any Actor struck
    * the Wombat.
    */
   protected void cycle()
   {
      if (lastCycleTime > 0)
      {
         long msSinceLastCycle = System.currentTimeMillis() - lastCycleTime;

         // cycle all the actors
         for (int i = 0; i < actorList.size(); i++)
         {
            Actor a = (Actor) actorList.elementAt(i);
            a.cycle((int)msSinceLastCycle);

            // check if any hit the wombat
            if (a.isCollidingWith(wombat) && a != wombat)
               running = false;
         }

      }
      lastCycleTime = System.currentTimeMillis();
   }

   /**
    * Draws the background graphics for the game using rudimentary drawing
    * tools. Note that we draw to the offscreenBuffer graphics (osg) not the
    * screen. The offscreenBuffer is an image the size of the screen we render
    * to and then later "flip" (draw) onto the display in one go (see the paint
    * method).
    */
   private void renderWorld()
   {
      // grab our off-screen graphics context
      Graphics osg = offScreenBuffer.getGraphics();
      int y=0;

      // draw the top roadside
      osg.setColor(0x00209020);
      y += (laneHeight)+topMargin;
      osg.fillRect(0, 0, getWidth(), y);


      // kirb edge
      osg.setColor(0x00808080);
      osg.drawLine(0, y-2, getWidth(), y-2);
      osg.setColor(0x00000000);
      osg.drawLine(0, y-1, getWidth(), y-1);

      // draw the first three lanes
      osg.setColor(0x00000000);
      osg.fillRect(0, y, getWidth(), laneHeight * 3);

      // draw the line markings on the road
      osg.setStrokeStyle(Graphics.DOTTED);
      osg.setColor(0x00AAAAAA);
      y += laneHeight; osg.drawLine(0, y, getWidth(), y);
      y += laneHeight; osg.drawLine(0, y, getWidth(), y);
      y += laneHeight; osg.drawLine(0, y, getWidth(), y);

      // draw the middle safety strip
      osg.setColor(0x00666666);
      osg.fillRect(0, y-2, getWidth(), 2);
      osg.setColor(0x00aaaaaa);
      osg.fillRect(0, y, getWidth(), laneHeight); y+= laneHeight;
      osg.setColor(0x00666666);
      osg.fillRect(0, y - 2, getWidth(), 2);

      // draw the next three lanes
      osg.setColor(0x00000000);
      osg.fillRect(0, y, getWidth(), laneHeight * 3);

      // draw the line markings on the road
      osg.setStrokeStyle(Graphics.DOTTED);
      osg.setColor(0x00AAAAAA);
      y += laneHeight; osg.drawLine(0, y, getWidth(), y);
      y += laneHeight; osg.drawLine(0, y, getWidth(), y);
      y += laneHeight; osg.drawLine(0, y, getWidth(), y);

      // curb edge
      osg.setStrokeStyle(Graphics.SOLID);
      osg.setColor(0x00808080);
      osg.drawLine(0, y, getWidth(), y);
      y++;
      osg.setColor(0x00000000);
      osg.drawLine(0, y, getWidth(), y);

      // draw the bottom roadside
      osg.setColor(0x00209020);
      osg.fillRect(0, y, getWidth(), y + (laneHeight * 2));
      y += laneHeight * 2;

      // draw the status bar along the bottom
      osg.setColor(0, 0, 128);
      osg.fillRect(0, getHeight() - statusLineHeight, getWidth(), getHeight());
      osg.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL));
      osg.setColor(0x00ffffff);

      osg.setColor(0x00ffffff);
      //osg.drawString("" + cps + " cps", 5, getHeight() - statusLineHeight + 1,
       //              Graphics.LEFT | Graphics.TOP);
     osg.drawString("" +"HighScore"+ theMidlet.getHighScore(),110 , getHeight() - statusLineHeight + 1,
              Graphics.LEFT | Graphics.TOP);

     osg.drawString("" +"time"+ countdown.getTime()  , 60, getHeight() - statusLineHeight + 1,
              Graphics.LEFT | Graphics.TOP);
      osg.drawString(""+"CurrScore" + score , 5, getHeight() - statusLineHeight + 1,
              Graphics.LEFT | Graphics.TOP);
      // now draw all the actors
      for (int i=0; i < actorList.size(); i++)
      {
         Actor a = (Actor)actorList.elementAt(i);
         a.render(osg);
      }
   }

   /**
    * Canvas overridden paint method that calls the renderWorld method (which
    * draws to the offscreenBuffer) and then draws the offscreenBuffer image
    * onto the display.
    * @param graphics The graphics context of the Canvas.
    */
   protected void paint(Graphics graphics)
   {
      renderWorld();
      graphics.drawImage(offScreenBuffer, 0, 0, Graphics.LEFT | Graphics.TOP);

   }

   /**
    * It returns the height of the lane.
    * @return The height of a lane.
    */
   public int getLaneHeight()
   {
      return laneHeight;
   }

   /**
    * Called when a key is pressed. Based on which key they hit it moves the
    * WombatActor.
    * @param keyCode The key that was pressed.
    */
   protected void keyPressed(int keyCode)
   {
      switch (getGameAction(keyCode))
      {
         case UP:
            if(wombat.presentCheck(wombat.getX())&& wombat.getY()==22){
            }else
             wombat.setY( wombat.getY() - laneHeight );
            break;
         case DOWN:
            wombat.setY(wombat.getY() + laneHeight);
            if(wombat.cyclecheck==1 && wombat.getY()==148)
            {
             wombat.cyclecheck=0;
             scorecount();
            }
            break;
         case RIGHT:
            wombat.setX(wombat.getX() + laneHeight);
            break;
         case LEFT:
            wombat.setX(wombat.getX() - laneHeight);
            break;
      }
   }
   /**
    * It calculates the score for the player,such that the player who completes the
   * roundtrip in shortest time gets the most benefit.
   * 
   */
   
   public void scorecount(){
      score+=countdown.getTime();
      countdown.countime=120;
   }
   /**
    * It returns the current score of the player.
    * 
    * @return score ,the current score of player
    */
   public static int getScore(){
      return score;
   }
   /**
    * It resets the score after the game gets over.
    *
    */
   public static void resetScore(){
     score=0;
   }
}
