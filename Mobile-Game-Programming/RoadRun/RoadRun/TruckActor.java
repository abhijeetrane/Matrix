//File:TruckActor.java
//Team:Cudjoe
//Date:10/04/2005
import javax.microedition.lcdui.Graphics;

/**
 * An VehicleActor that represents a truck (the only customisation is the
 * size and the drawing code).
 * @author Cudjoe
 */
public class TruckActor extends VehicleActor
{
 private final static int laneone=22;
 private final static int lanetwo=40;
 private final static int lanethree=58;
 private final static int lanefour=94;
 private final static int lanefive=112;
 private final static int lanesix=130;
 
 
   /**
    * Constructs a new truck setting the GameScreen, starting position (x, y)
    * and the speed at which it should move.
    * @param gsArg GameScreen Actor is associated with.
    * @param xArg The starting x position.
    * @param yArg The starting y position.
    * @param speedArg The speed of the vehicle.
    */
   public TruckActor(GameScreen gsArg, int xArg, int yArg, int speedArg)
   {
      super(gsArg, xArg, yArg, speedArg);
   }

   /**
    * Get the Actor width (overriden to set the width properly).
    * @return The width of the truck.
    */
   public int getActorWidth()
   {
      return 28;
   }

   /**
    * Draws a truck using rectangles.
    * @param graphics The graphics context on which to draw the car.
    */
   public void render(Graphics graphics)
   {
      int u = getActorHeight();
      switch(getY()){
      
      case(laneone):// the front
               graphics.setColor(0x00aa9922);
               graphics.fillRect(getX(), getY(), 4, u);
               // the trailer
               graphics.setColor(0x00ff0000);
               graphics.fillRect(getX()+9, getY(), 18, u);
               // the cab
               graphics.setColor(0x0066CCFF);
               graphics.fillRect(getX() + 4, getY(), u-3, u);
               break;
      case(lanetwo):// the front
               graphics.setColor(0x00aa9922);
               graphics.fillRect(getX(), getY(), 4, u);
               // the trailer
               graphics.setColor(0x00ff0000);
               graphics.fillRect(getX()+9, getY(), 18, u);
               // the cab
                graphics.setColor(0x00FEED54);
                graphics.fillRect(getX() + 4, getY(), u-3, u);
               break;
      case(lanethree):// the front
               graphics.setColor(0x00aa9922);
               graphics.fillRect(getX(), getY(), 4, u);
               // the trailer
               graphics.setColor(0x00ff0000);
               graphics.fillRect(getX()+9, getY(), 18, u);
               // the cab
               graphics.setColor(0x000000ff);
               graphics.fillRect(getX() + 4, getY(), u-3, u);
               break;
      case(lanefour):      //    the front
              graphics.setColor(0x00aa9922);
              graphics.fillRect(getX()+23, getY(), 4, u);
              // the trailer
              graphics.setColor(0x00ff0000);
              graphics.fillRect(getX(), getY(), 18, u);
              // the cab
              graphics.setColor(0x000000ff);
              graphics.fillRect(getX()+10, getY(), u-3, u);
               break;
      case(lanefive)://     the front
              graphics.setColor(0x00aa9922);
              graphics.fillRect(getX()+23, getY(), 4, u);
              // the trailer
              graphics.setColor(0x00ff0000);
              graphics.fillRect(getX(), getY(), 18, u);
              // the cab
              graphics.setColor(0x00FEED54);
              graphics.fillRect(getX()+10, getY(), u-3, u);
              break;
      case(lanesix)://     the front
             graphics.setColor(0x00aa9922);
             graphics.fillRect(getX()+23, getY(), 4, u);
             // the trailer
             graphics.setColor(0x00ff0000);
             graphics.fillRect(getX(), getY(), 18, u);
             // the cab
             graphics.setColor(0x0066CCFF);
             graphics.fillRect(getX()+10, getY(), u-3, u);
             break;       
      default:{}

   }
      
      
   }
}
