//Team: Tahiti
//Date: 2005/03/27
//Vers: 1.0
//File: GraveSpidUpdate.java

package vista.test;

import java.util.Vector;
import vista.ui.InspectDialog;
import vista.object.InspectSpidData;
import java.util.Date;
import java.text.SimpleDateFormat;
import vista.object.*;
import vista.tuples.*;
import vista.util.*;

/** This class tests the inspect dialog.
*/

public class GraveSpidUpdate  {

/** Reference to InspectDialog */

private InspectDialog id;
private SimpleDateFormat formatter = new SimpleDateFormat ("hh:mm:ss a");

/** Constructor.
 @param id InspectDialog. */

public GraveSpidUpdate(InspectDialog id) {
this.id = id;
}

/** The update function to show dummy test data in insepct dialog*/

public void update( ) {
Vector vector = new Vector( );
System.out.println("I am called gravespidupdate");
vector.addElement(new InspectSpidData("Class",id.entity.getEntry().getClass().getName()));
vector.addElement(new InspectSpidData("Clid",(Entities.clidtab.get(id.entity.getEntry().getClass().getName())).toString()));
vector.addElement(new InspectSpidData("Spid",new Long (id.entity.getId()).toString()));
vector.addElement(new InspectSpidData("Reads",new Long(id.entity.getReads()).toString()));
vector.addElement(new InspectSpidData("Owner",id.entity.getOwner().toString()));
vector.addElement(new InspectSpidData("Written",formatter.format(new Date(id.entity.getBirth())).toString()));
vector.addElement(new InspectSpidData("Expires",formatter.format(new Date(id.entity.getDeath())).toString()));


// Send in new attribute-value  information

id.update(vector);
}

}