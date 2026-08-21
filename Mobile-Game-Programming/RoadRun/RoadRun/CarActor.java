//File:CarActor.java
//Team:Cudjoe
//Date:10/04/2005

import javax.microedition.lcdui.Graphics;

/**
 * An VehicleActor that represents a little car (the only customisation is the
 * size and the drawing code).
 * @author Cudjoe
 */
public class CarActor extends VehicleActor
{
 private final static int laneone=22;
 private final static int lanetwo=40;
 private final static int lanethree=58;
 private final static int lanefour=94;
 private final static int lanefive=112;
 private final static int lanesix=130;
 
 /**
    * Constructs a new car setting the GameScreen, starting position (x, y)
    * and the speed at which it should move.
    * @param gsArg GameScreen Actor is associated with.
    * @param xArg The starting x position.
    * @param yArg The starting y position.
    * @param speedArg The speed of the vehicle.
    */
   public CarActor(GameScreen gsArg, int xArg, int yArg, int speedArg)
   {
      super(gsArg, xArg, yArg, speedArg);
   }

   /**
    * Get the Actor width (overriden to set the width properly).
    * @return The width of the car.
    */
   public int getActorWidth()
   {
      return 12;
   }

   /**
    * Draws a car using rectangles.
    * @param graphics The graphics context on which to draw the car.
    */
   public void render(Graphics graphics)
   {
      int u = getActorHeight();
           
       switch(getY()){
       
       case(laneone):graphics.setColor(0x0066CCFF);
                graphics.fillRect(getX(), getY(), u, u);
                graphics.fillRect(getX() + (u / 2) + 5, getY(), u, u);
                graphics.setColor(0x00FEED54);
                graphics.fillRect(getX() + u - 2, getY(), u, u);
                break;
       case(lanetwo):graphics.setColor(0x0000FF00);
                graphics.fillRect(getX(), getY(), u, u);
                graphics.fillRect(getX() + (u / 2) + 5, getY(), u, u);
                graphics.setColor(0x00FF66FF);
                graphics.fillRect(getX() + u - 2, getY(), u, u);
                break;
       case(lanethree):graphics.setColor(0x00FF3366);
                graphics.fillRect(getX(), getY(), u, u);
                graphics.fillRect(getX() + (u / 2) + 5, getY(), u, u);
                graphics.setColor(0x00aa9922);
                graphics.fillRect(getX() + u - 2, getY(), u, u);
                break;
       case(lanefour):
                graphics.setColor(0x00FEED54);
                graphics.fillRect(getX(), getY(), u, u);
                graphics.fillRect(getX() + (u / 2) + 5, getY(), u, u);
                graphics.setColor(0x0066CCFF);
                graphics.fillRect(getX() + u - 2, getY(), u, u);
                break;
       case(lanefive):
                graphics.setColor(0x00FF66FF);
                graphics.fillRect(getX(), getY(), u, u);
                graphics.fillRect(getX() + (u / 2) + 5, getY(), u, u);
                graphics.setColor(0x0000FF00);
                graphics.fillRect(getX() + u - 2, getY(), u, u);
                break;
       case(lanesix):
                graphics.setColor(0x00aa9922);
                graphics.fillRect(getX(), getY(), u, u);
                graphics.fillRect(getX() + (u / 2) + 5, getY(), u, u);
                graphics.setColor(0x00FF3366);
                graphics.fillRect(getX() + u - 2, getY(), u, u);
                break;       
       default:{}

   }


}

}


