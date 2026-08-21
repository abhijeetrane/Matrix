//File:VehicleActor.java
//Team:Cudjoe
//Date:10/04/2005
/**
 * An Actor that serves as a base class for the vehicles (such as CarActor and
 * TruckActor). The common functionality is the movement in the cycle method.
 * @author Cudjoe
 */
abstract public class VehicleActor extends Actor
{
	/**
	 *  speed (along x axis only)
	 */
   protected int speed;         
   private long fluff = 0;

   /**
    * Constructs a new Vehicle setting the GameScreen, starting position (x, y)
    * and the speed at which it should move.
    * @param gsArg GameScreen Actor is associated with.
    * @param xArg The starting x position.
    * @param yArg The starting y position.
    * @param speedArg The speed of the vehicle.
    */
   public VehicleActor(GameScreen gsArg, int xArg, int yArg, int speedArg)
   {
      super(gsArg, xArg, yArg);
      speed = speedArg;
   }

   /**
    * A cycle method that moves the Actor a distance relative to it's current
    * speed (the value of the speed int) and the amount of time that has passed
    * since the last call to cycle (deltaMS). This code uses a fluff value in
    * order to remember values too small to handle (below the tick level).
    * @param deltaMS The number of milliseconds that have passed since the last
    * call to cycle.
    */
   public void cycle(long deltaMS)
   {
      long ticks = (deltaMS + fluff) / 100;

      // remember the bit we missed
      fluff += (deltaMS - (ticks * 100));

      // move based on our speed in pixels per ticks
      if(getY()< 59){
      if (ticks > 0){
    	   setX( getX() - (int) (speed * ticks) );
         
      }
      }else {
    	  if (ticks > 0){
    		  		  setX( (getX()+(int) (speed * ticks)) );
    	        }
    	  }
      
      
   }

}
