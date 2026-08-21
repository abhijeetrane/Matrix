//File:Actor.java
//Team:Cudjoe
//Date:10/04/2005

import javax.microedition.lcdui.Graphics;

/**
 * An abstract base class for higher level Actors. This class handles basic
 * position as well as maintaining a link to the GameScreen. A class extending
 * this needs to implement the cycle and render methods.
 * @author Cudjoe
 */
abstract public class Actor
{
	/**
	 * It is an object of the GameScreen class.
	 */
   protected GameScreen gameScreen;
   private int x, y;

   /**
    * Constructs a new Actor.
    * @param gsArg The GameScreen this Actor belongs to.
    * @param xArg The starting x position.
    * @param yArg The starting y position.
    */
   public Actor(GameScreen gsArg, int xArg, int yArg)
   {
      gameScreen = gsArg;
      x = xArg;
      y = yArg;
   }

   /**
    * Called by the main game loop to let the Actor have a life. Typically an
    * implementation should use the deltaMS (the number of milliseconds that
    * have passed since the cycle method was last called) to carry out movement
    * or other actions relative to the amount of time that has passed. The
    * default implementation in the base class does nothing.
    * @param deltaMS The number of milliseconds that have passed since the last
    * call to cycle.
    */
   public void cycle(long deltaMS) { }

   /**
    * Called by the main game loop to draw this Actor to the screen. It is
    * intended that a child class override this implementation in order to
    * draw a representation of the actor (in a way it sees fit).
    * @param g The Graphics context upon which to draw the actor.
    */
   public void render(Graphics g) { }

   /**
    * Returns the width of the Actor (can be overriden by a child class to
    * return a different size).
    * @return Width of the actor.
    */
   public int getActorWidth()
   {
      // square by default
      return getActorHeight();
   }

   /**
    * Returns the height of the Actor (can be overriden by a child class to
    * return a different size). The default implementation (the most common
    * case) is to use a value slightly smaller than the lane height.
    * @return Height of the actor.
    */
   public int getActorHeight()
   {
      return gameScreen.getLaneHeight() - 2;
   }

   /**
    * It returns the current x position of the actor.
    * 
    * @return The current x position of the actor.
    */
   public int getX() { return x; }

   /**
    * Sets the current x position of the actor. We also check to see if the
    * actor has moved off the edge of the screen and wrap it around.
    */
   public void setX(int newX)
   {
      x = newX;
      // we wrap on the x-axis on a constant number to maintain an
      // equal distance between all vehicles
     if(getY() < 59){
      if (x < -32){
    	     	  x = gameScreen.getWidth();
    	       }
      if (x > gameScreen.getWidth()){
    	        x = -getActorWidth();
                }
     }else{
    	 if (x < 0){
       	  x = -getActorWidth();
         }
         if (x > gameScreen.getWidth()+4 ){
       	         x = 0;
                   }
     }



   }

   /**
    * It returns the current  y position of the actor.
    * 
    * @return The current y position of the actor.
    */
   public int getY() { return y; }

   /**
    * Sets the current y position of the actor. We also check to see if the
    * actor has moved off the edge of the screen and wrap it around.
    */
   public void setY(int newY)
   {
      y = newY;

      // we don't wrap on the y-axis
      if (y < gameScreen.getLaneYPos(0))
         y = gameScreen.getLaneYPos(0);

      if (y > gameScreen.getLaneYPos(8))
         y = gameScreen.getLaneYPos(8);
   }

   /**
    * Simple collision detection checks if a given point is in the Actor's
    * bounding rectangle.
    * @param px The x position of the point to check against.
    * @param py The y position of the point to check against.
    * @return true if the point px, py is within this Actor's bounding rectangle
    */
   public boolean isCollidingWith(int px, int py)
   {
      if (px >= getX() && px <= (getX() + getActorWidth()) &&
          py >= getY() && py <= (getY() + getActorHeight()) )
         return true;
      return false;
   }

   /**
    * Determins if another Actor has collided with this one. We do this by
    * checking if any of the four points in the passed in Actor's bounding
    * rectangle are within the bounds of this Actor's (using the isCollidingWith
    * point method above)
    * @param another The other Actor we're checking against.
    * @return true if the other Actor's bounding box collides with this one.
    */
   public boolean isCollidingWith(Actor another)
   {
      // check if any of our corners lie inside the other actor's
      // bounding rectangle
      if (isCollidingWith(another.getX(), another.getY()) ||
          isCollidingWith(another.getX() + another.getActorWidth(), another.getY()) ||
          isCollidingWith(another.getX(), another.getY() + another.getActorHeight()) ||
          isCollidingWith(another.getX() + another.getActorWidth(),
                          another.getY()+ another.getActorHeight()))
         return true;
      else
         return false;
   }

}


















