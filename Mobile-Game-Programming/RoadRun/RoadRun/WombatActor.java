//File:WombatActor.java
//Team:Cudjoe
//Date:10/04/2005
import javax.microedition.lcdui.Graphics;

/**
 * The main player actor, the Wombat.
 * @author Cudjoe
 */
public class WombatActor extends Actor
{
   /**
    * This array keeps track of the positions of wombat.
    */
	protected int[] wombatposition=new int[11];
	/**
	 * This variable keeps track of the number  of landing positions reached.
	 */
	protected int count=0;
	/**
	 * This variable keeps track of the roundtrips completed.
	 */
	protected int cyclecheck=0;
	private int flag;
   /**
    * Constructs a new WombatActor object using the specified GameScreen and
    * position.
    * @param gsArg The GameScreen this Actor is associated with.
    * @param xArg The x starting position.
    * @param yArg The y starting position.
    */
   public WombatActor(GameScreen gsArg, int xArg, int yArg)
   {
      super(gsArg, xArg, yArg);
   }

   /**
    * Renders the actor to the screen by drawing a rectangle.
    * @param graphics The graphics context to draw to.
    */
   public void render(Graphics graphics)  {
	     
	   if (cyclecheck==0){
      	 if(getY()==4)
      	 positionCheck(getX());
	   }
	   if(count > 0){
		      graphics.setColor(0x00FF0000);
	      for(int i=1;i<= count;i++){
	    	  graphics.fillRect(wombatposition[i], 4, getActorWidth(),getActorHeight());
	    	
	      }

		      }
	      graphics.setColor(0x0044FF44);
	      graphics.fillRect(getX(), getY(), getActorWidth(), getActorHeight());
		  
}

   /**
    * An overridden version of the Actor setX method in order to stop the
    * wrapping it does. You want the wombat to stop on the edge of the screen.
    * @param newX The new x position.
    */
   public void setX(int newX)
   {
      super.setX(newX);

      // non-wrapping version for the wombat
      if (getX() < 0)
         setX(0);

      if (getX() > gameScreen.getWidth()-getActorWidth()-2)
         setX(gameScreen.getWidth() - getActorWidth()-2);
   }
   /**It checks if the wombat is reaching the landing destination for the first time.
    * If it has reached the position for the first time then it adds the position to the 
    * wombatposition array.
    * 
    * @param newX
    *              The x position of wombat to check its position. 
    */
   public void positionCheck(int newX){
	  this.flag=1;
	 cyclecheck=1;
	   for(int i=1;i<=count;i++){
				  if(wombatposition[i]==getX()) {
	                  this.flag=0;
					   break;
	     	  	  }

			  }
	 		   if(this.flag==1){
				  count++;
				  wombatposition[count]=getX();
				   }
  }
 /**
  * It checks if the x position of the wombat is present in the array.
  * i.e It checks if the wombat has previously reached that position.If
  * it already reached that position then it is blocked from entering it
  * in the GameScreen class up arrow key stroke .
  * @param newX 
  *             The x position of the wombat 
  * @return boolean
  */ 
 
 
    public boolean presentCheck(int newX){
 	   for(int i=1;i<=count;i++){
			  if(wombatposition[i]==getX()) {
               return true;

  	  	       }

		  }
	   return false;
  }
}
