//File:CountDown.java
//Team:Cudjoe
//Date:10/04/2005

import java.util.TimerTask;
/**
 * A countdown class that extends Timertask which counts down time.
 * @author Cudjoe
 *
 */
public class CountDown extends TimerTask {
//To keep track of time
  int countime;
/**
 * Constructs the countdown clock
 * @param clockTime
 */
  public CountDown(int clockTime) {
    countime = clockTime;
  }
/**
 * It reduces the time after a certain period.
 */
  public void run() {
	countime--;
  }
/**
 * It returns the counter variable
 * @return countime ,which counts time
 */
  public int getTime() { return this.countime; }
}