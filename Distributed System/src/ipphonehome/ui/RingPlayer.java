//Team: Lanikai
//Date: 2005/12/05
//Vers: 1.0
//File: RingPlayer.java
package ipphonehome.ui;

import jroll.atp.MP3FilePlayer;
/**
It plays the ring file*/

public class RingPlayer implements Runnable{

/**
It plays the mp3 file
*/
protected MP3FilePlayer player;
/**
 * Constructor
 *
 */
public RingPlayer(){
try{
	player = new MP3FilePlayer("../res/ring.mp3");
}catch(Exception e){
	System.out.println("I am the ringing response");
	System.out.println(e);

}
}
/**
 * It starts the thread
 */
	public void run() {
		player.play();
		// TODO Auto-generated method stub

	}

}
