//File:RMSGameScores.java
//Team:Cudjoe
//Date:10/04/2005

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * A class used for storing and showing game scores.
 * @author Cudjoe
 */
public class RMSGameScores  implements RecordFilter , RecordComparator {
  /*
   * The RecordStore used for storing the game scores.
   */
  private RecordStore recordStore = null;

  /**
   * The player name to use when filtering.
   */
  public static String playerNameFilter = null;

  /**
   * The constuctor opens the underlying record store, creating it if
   * necessary.
   */
  public RMSGameScores() {
    // Create a new record store for this example
    try {

       // recordStore = RecordStore.openRecordStore("scores", true);
        //recordStore.closeRecordStore();//close the recordstore if it exists previously
       // RecordStore.deleteRecordStore("scores");//delete the previous recordstore
        recordStore = RecordStore.openRecordStore("scores", true);//create a new recordstore

    } catch (RecordStoreException rse) {
      System.out.println("Record Store Exception in the ctor." + rse);
      rse.printStackTrace();
    }
  }


/**
   * Part of the RecordFilter interface.
   */
  public boolean matches(byte[] candidate) throws IllegalArgumentException {
    // If no filter set, nothing can match it.
    if (this.playerNameFilter == null) {
      return false;
    }

    ByteArrayInputStream bais = new ByteArrayInputStream(candidate);
    DataInputStream inputStream = new DataInputStream(bais);
    String name = null;

    try {
      int score = inputStream.readInt();
      name = inputStream.readUTF();
    } catch (EOFException eofe) {   System.out.println(eofe);
      eofe.printStackTrace();
    } catch (IOException eofe) {
      System.out.println(eofe);
      eofe.printStackTrace();
    }
    return (this.playerNameFilter.equals(name));
  }



  /**
   * Part of the RecordComparator interface.
   */
  public int compare(byte[] rec1, byte[] rec2) {

    // Construct DataInputStreams for extracting the scores from
    // the records.
    ByteArrayInputStream bais1 = new ByteArrayInputStream(rec1);
    DataInputStream inputStream1 = new DataInputStream(bais1);
    ByteArrayInputStream bais2 = new ByteArrayInputStream(rec2);
    DataInputStream inputStream2 = new DataInputStream(bais2);
    int score1 = 0;
    int score2 = 0;
    try {
      // Extract the scores.
      score1 = inputStream1.readInt();
      score2 = inputStream2.readInt();
    } catch (EOFException eofe) {
      System.out.println(eofe);
      eofe.printStackTrace();
    } catch (IOException eofe) {
      System.out.println(eofe);
      eofe.printStackTrace();
    }

    // Sort by score
    if (score1 > score2) {
      return RecordComparator.FOLLOWS;
    } else if (score1 < score2) {
      return RecordComparator.PRECEDES;
    } else {
      return RecordComparator.EQUIVALENT;
    }
  }
/**
 * It returns the highest score.
 * 
 * @return highscore
 *          The highest score in the recordstore.
 */
   public int getHighScore(){
      int highscore=0;
       try{
	    int  id = recordStore.getNumRecords();
	   			   		   try {
	   		   		             if(id==0){
						                     highscore=0;
				                  }else{
					RecordEnumeration re = recordStore.enumerateRecords(null, this,true);
				int i=0;
				while(re.hasNextElement()){
					id = re.nextRecordId();
				}



	    		   ByteArrayInputStream bais = new ByteArrayInputStream(recordStore.getRecord(id));
	   	           DataInputStream inputStream = new DataInputStream(bais);
		           highscore = inputStream.readInt();
	   	           String playerName = inputStream.readUTF();
							   }
	   			   		         
	   			   		        } catch (EOFException eofe) {
	   			   		           System.out.println(eofe);
	   			   		           eofe.printStackTrace();
	   		         }
	   		     } catch (RecordStoreException rse) {
	   		       System.out.println(rse);
	   		       rse.printStackTrace();
	   		     } catch (IOException ioe) {
	   		       System.out.println(ioe);
	   		       ioe.printStackTrace();
			   }
    return highscore;
   }
/**
 * It returns the number of the records in the recordstore.
 * @return numcount 
 *                 The number of records in recordstore.
 */
    public int getNumRecords(){
       int numcount=0;
	   try{
	   numcount= recordStore.getNumRecords();
	    } catch (RecordStoreException rse) {
		       System.out.println(rse);
		       rse.printStackTrace();
        }
        return numcount;
     }


/**
 * It deletes the lowest the lowest score from the recordstore.
 *
 */
   public void deletelowest(){
	 try{

	RecordEnumeration re = recordStore.enumerateRecords(null, this,true);
	int id = re.nextRecordId();
	System.out.println("the delete record id is"+id);
	recordStore.deleteRecord(id);

     } catch (RecordStoreException rse) {
      System.out.println(rse);
      rse.printStackTrace();
    }
   }
/**
 * It returns the lowest score in the recordstore.
 * @return score 
 *              returns lowest score
 */
  public  int lowScore(){
          int i=1;
          int score=0;
          int id=0;
          try {
			 RecordEnumeration re = recordStore.enumerateRecords(null, this,true);

                    id = re.nextRecordId();
               ByteArrayInputStream bais = new ByteArrayInputStream(
			   		             recordStore.getRecord(id));
			   		         DataInputStream inputStream = new DataInputStream(bais);
			   		         try {
			   		            score = inputStream.readInt();
			   		           String playerName = inputStream.readUTF();
			   		           System.out.println(playerName + " = " + score+"id = "+id);

			   		        } catch (EOFException eofe) {
			   		           System.out.println(eofe);
			   		           eofe.printStackTrace();
		         }



		     } catch (RecordStoreException rse) {
		       System.out.println(rse);
		       rse.printStackTrace();
		     } catch (IOException ioe) {
		       System.out.println(ioe);
		       ioe.printStackTrace();
    }
    return score;
  }
  /**
   * Add a new score to the storage.
   *
   * @param score
   *            the score to store.
   * @param playerName
   *            the name of the play achieving this score.
   */
  public void addScore(int score, String playerName) {
    // Each score is stored in a separate record, formatted with
    // the score, followed by the player name.
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream outputStream = new DataOutputStream(baos);
    try {
      // Push the score into a byte array.
      outputStream.writeInt(score);
      // Then push the player name.
      outputStream.writeUTF(playerName);
    } catch (IOException ioe) {
      System.out.println(ioe);
      ioe.printStackTrace();
    }

    // Extract the byte array
    byte[] b = baos.toByteArray();
    try {
      // Add it to the record store
      recordStore.addRecord(b, 0, b.length);
    } catch (RecordStoreException rse) {
      System.out.println(rse);
      rse.printStackTrace();
    }
  }
/**
 * It returns the recordstore.
 * @return RecordStore
 */
public RecordStore getRecordStore(){
  return recordStore;
}
}