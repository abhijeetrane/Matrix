//Team: Lanikai
//Date: 2005/12/05
//Vers: 1.0
//File: BusyRingPlayer.java

package ipphonehome.ui;

import jroll.atp.MP3FilePlayer;

/** Busy tone response.
  @author Lanikai
*/
public class BusyRingPlayer implements Runnable{
/**
It plays the mp3 file
*/
 protected MP3FilePlayer player;
/**
 * Constructor
 *
 */
 public BusyRingPlayer(){
 try{
	 player = new MP3FilePlayer("../res/busyring.mp3");
    }catch(Exception e){
	  System.out.println("I am the ringing response");
	  System.out.println(e);

  }
 }
 /**
  * It is executed when thread starts
  */
	public void run() {
		player.play();

	}

}
