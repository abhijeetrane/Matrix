//Team: Tahiti
//Date: 2005/04/27
//Vers: 1.0
//File: VistaUI.java
package vista.ui;

import space.nucleus.MetaEntry;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Date;
import java.text.SimpleDateFormat;
import space.Entry;
import space.Transaction;
import space.remote.VistaSpace;
import vista.util.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;

import javax.swing.*;
import space.Entry;
import space.remote.VistaSpace;
import space.nucleus.*;
import space.remote.VistaSpace;
import vista.tuples.Entities;
import vista.tuples.FrequencyMethod;
import vista.tuples.UpdateMethod;
import vista.object.Cell;
import vista.test.star.MainSequenceEntry;
import vista.object.*;
import vista.tuples.*;


/** This class implements the GUI handshake client.
*/
public class VistaUI extends JFrame implements NucleusListener{
  /** Set to false to suppress debug/test activities */
  private final static boolean DEBUG = true;

  private final static String SPACEPLACE = "rmi://localhost/space";
  /** Space reference to bind to the Internet.
   */
   public static VistaSpace vistaSpace = null;

   /** Default RMI port number for listening to look up requests.
   */
   public final static Integer REGISTRY_PORT = new Integer(1098);

  // Generic date formatter
  private SimpleDateFormat formatter = new SimpleDateFormat ("hh:mm:ss a");
  private TabsPanel tabsPanel = new TabsPanel( );

  private final static int INSET_PAD_TOP = 10;
  private final static int INSET_PAD_LEFT = 2;
  private final static int INSET_PAD_BOTTOM = 10;
  private final static int INSET_PAD_RIGHT = 2;

  public static Insets padInsets =
    new Insets(INSET_PAD_TOP,
               INSET_PAD_LEFT,
               INSET_PAD_BOTTOM,
               INSET_PAD_RIGHT);
/**
 * Live entities that stores live entities.
 */
  private static LiveEntities liveEntities =new LiveEntities();
  /**
   * Dead entities that stores dead entities.
   */
  private static DeadEntities deadEntities = new DeadEntities();

  private static int clidindex=0;
/**
 * Static variables for storing the statistics.
 */
    public static int spaceWrites = 0;
    public static int spaceReads = 0;
    public static int spaceTakes = 0;
    public static int spaceSleeping = 0;
    public static int spaceRegisters = 0;
    public static int spaceNotifies = 0;
    public static int spaceExpires = 0;
    public static long spaceStart=System.currentTimeMillis();
    public static long lastupdate=0;


// Action strings
  String actionStrings[] = {
    "BOGY",
    "WRITE",
    "READ",
    "TAKE",
    "REGISTER",
    "NOTIFY",
    "SLEEP",
    "WAKEUP",
    "EXPIRE"
 };

  /** Constructor.
      @param url Space URL.
  */
  public VistaUI(String url) {
    super("Vista - Version 1.0 - Spid = Shiva and Clid=Shakti");
  try{
	  System.out.println("Constructor VistaUI Begin ");
      vistaSpace = new VistaSpace(url,REGISTRY_PORT);
      VistaSpace.setListener(this);
      System.out.println("Constructor VistaUI End ");
  }catch(Exception e){
  	System.out.println("The exception has occured");
  }

    
    setJMenuBar(new MenuBar(this ));

    //setIconImage(icon.getImage());

    // Create the main panel which contains all the subpanels
    GriddedPanel mainPanel = new GriddedPanel();

    // Add URL
    mainPanel.addComponent(new JLabel("URL:  "+url),0,0);

    mainPanel.addFilledComponent(new JSeparator(),1,0);

    mainPanel.addComponent(tabsPanel,2,0);

    // Put the main panel on a "top" panel to prevent swing
    // trying to resize it
    JPanel topPanel = new JPanel();
    topPanel.add(mainPanel);

    // Add the top panel to the content pane and size it
    getContentPane().add(topPanel, BorderLayout.CENTER);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setSize(400,525);
    
    System.out.println("DEBUG"+DEBUG);
    
     if(DEBUG)
      test(this);
   
	/*
	 * new Thread( new vista.test.MapUpdate(tabsPanel.getMapPanel(
	 * ),liveEntities,clidindex )).start( );
	 * 
	 * 
	 * new Thread( new vista.test.SpidsUpdate(tabsPanel.getSpidsPanel(
	 * ),liveEntities )).start( );
	 * 
	 * 
	 * new Thread( new vista.test.ClidsUpdate(tabsPanel.getClidsPanel(
	 * ),liveEntities )).start( );
	 * 
	 * 
	 * new Thread( new vista.test.StatsUpdate(tabsPanel.getStatsPanel(
	 * ),liveEntities)).start( );
	 * 
	 * new Thread( new vista.test.GraveUpdate(tabsPanel.getGravePanel(
	 * ),deadEntities )).start( );
	 * 
	 * 
	 * // Set listener to capture cell drills // Q: Why doesn't GC recycle this? A:
	 * CellDrill registers with MapPanel. new vista.test.CellDrill(this,
	 * tabsPanel.getMapPanel( ) );
	 * 
	 * 
	 * new ItemChangeListener(tabsPanel.getMapPanel());
	 * 
	 * new UpdateListener(tabsPanel.getMapPanel());
	 * 
	 * new ViewListener(tabsPanel.getMapPanel());
	 * 
	 * new OrganizeListener(tabsPanel.getMapPanel());
	 */

  }

  /** Executes unit tests to see if every works as expected */
  private void test(JFrame frame) {

	  MainSequenceEntry mainSequenceEntry = new MainSequenceEntry("A");
	  Transaction paramTransaction = new Transaction();
	  InetAddress paramInetAddress;
	  MetaEntry mentry;
	  
	try {
       System.out.println("Testing code begin");
       
		paramInetAddress = InetAddress.getLocalHost();
		mentry = new MetaEntry(mainSequenceEntry, paramTransaction, 999999,  paramInetAddress);
	
		for(int i = 0; i <=45; i++) {
		
		Entity entity = new vista.object.Entity(mentry);
	
		System.out.println("Id is "+entity.getId());
		
		System.out.println("clidindex is "+clidindex);
		
		liveEntities.insert(entity);
		
	
		   
		  // Test the map update
		   new Thread( new vista.test.MapUpdate(tabsPanel.getMapPanel( ),liveEntities,clidindex )).start( );

		   
		    // Test the spids table update
		    new Thread( new vista.test.SpidsUpdate(tabsPanel.getSpidsPanel( ),liveEntities )).start( );

		    
		    
		    //  Test the clids table update
		    new Thread( new vista.test.ClidsUpdate(tabsPanel.getClidsPanel( ),liveEntities )).start( );

		//  Test the statistics table update
		    new Thread( new vista.test.StatsUpdate(tabsPanel.getStatsPanel( ),liveEntities)).start( );

		//  Test the graveyard table update
		   new Thread( new vista.test.GraveUpdate(tabsPanel.getGravePanel( ),deadEntities )).start( );
		    // new vista.test.GraveUpdate(tabsPanel.getGravePanel( ),deadEntities );

		}
		   
		    // Set listener to capture cell drills
		    // Q: Why doesn't GC recycle this? A: CellDrill registers with MapPanel.
		    new vista.test.CellDrill(frame, tabsPanel.getMapPanel( ) );
		     
		    
		    new ItemChangeListener(tabsPanel.getMapPanel());

		    new UpdateListener(tabsPanel.getMapPanel());
		    
		    new ViewListener(tabsPanel.getMapPanel());
		    
		    new OrganizeListener(tabsPanel.getMapPanel());

		    System.out.println("Testing code end");
		
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  
	  
	   
	  
	  
      
}

/** Invoked on a nucleus action.
  @param action Action identifiers defined in NucleusAction.
  @param mentry Nucleus metaentry which is null for NucleusAction.SLEEP, NucleusAction.WAKE, and NucleusAction.NOTIFY.
  @param tmpl Entry template which is null for NucleusAction.WRITE.
*/
public void actionPerformed(int action,MetaEntry mentry,Entry tmpl) {
//System.out.println("Action: "+actionStrings[getActionNumber(action)]);
 Entity newentity;

// Update statistics
switch(action) {
  case NucleusAction.WRITE:
  	
   liveEntities.insert(new Entity(mentry));
   ++spaceWrites;
   lastupdate=System.currentTimeMillis();
    Integer classid;
   if(mentry!=null)
        //classid = getClid(mentry.getEntry().getClass().getName());
   	classid = getClid(mentry.getEntry());
   	break;

  case NucleusAction.READ:
    ++spaceReads;
     newentity= liveEntities.lookup(mentry.getId());
     newentity.incrementReads();
     long now =System.currentTimeMillis();
     newentity.setMRU(now);
       // Test of dispose method, used on examples/HelloWorld.java
    //vistaSpace.dispose(0);

    break;

  case NucleusAction.TAKE:
  	newentity=liveEntities.remove(mentry.getId());
    deadEntities.insert(newentity);
    newentity.setDeath(System.currentTimeMillis());
    ++spaceTakes;
    lastupdate=System.currentTimeMillis();
    
    break;

  case NucleusAction.SLEEP:
    ++spaceSleeping;
    break;

  case NucleusAction.WAKEUP:
    --spaceSleeping;
    break;

  case NucleusAction.REGISTER:
   ++spaceRegisters;
    break;

  case NucleusAction.NOTIFY:
    ++spaceNotifies;
    break;

  case NucleusAction.EXPIRE:
  	deadEntities.insert(liveEntities.remove(mentry.getId()));
    ++spaceExpires;
    break;
}
long id = mentry.getId();
 System.out.println("the space expires"+spaceExpires);
 System.out.println(" The mentry id is "+ id );
 print(mentry,tmpl);
}

  /** Print out the statistics so far.
      @param mentry Meta-entry from nucleus.
      @param tmpl Entry template for read, take, and notify operations.
  */
  private void print(MetaEntry mentry,Entry tmpl) {
    System.out.println("Number writes: "+spaceWrites);

    System.out.println("Number reads: "+spaceReads);

    System.out.println("Number takes: "+spaceTakes);

    System.out.println("Sleeping threads: "+spaceSleeping);

    System.out.println("Notify registers: "+spaceRegisters);

    System.out.println("Notifies: "+spaceNotifies);

    if(mentry != null) {

      Entry entry = mentry.getEntry();

      System.out.println("Spid: "+mentry.getId());

      System.out.println("Class: "+entry.getClass().getName());

     //System.out.println("Clid: "+getClid(entry.getClass().getName()));

      System.out.println("Owner: "+mentry.getOwner().toString());

      long expiry = mentry.getExpiration();

      System.out.print("Expiry: ");

      if(expiry == Long.MAX_VALUE)
        System.out.println("never");
      else
        System.out.println(formatter.format(new Date(expiry)));
    }

    if(tmpl != null) {
      System.out.println("Read/take template: "+tmpl.getClass().getName());
    }

    System.out.println("---");
  }

  /** Map a class to a clid, creating a new clid if necessary.
      @param e Entry.
      @return Integer.
  */
 
  
public static Integer getClid(Entry e){
	Clid clid =  Entities.clidtab.get(e);
	if(clid == null) {
	       Entities.clidtab.put(e);
	    }
	return Entities.clidtab.get(e).getId();
}
  /** Convert a nucleus action to a number.
      @param action Nucleus action.
      @return Number in range 1,2,3,...
  */
  private int getActionNumber(long action) {
    for(int i=1; i <= 32; i++) {
      if((action & 1) != 0)
        return i;

      action = action >> 1;
    }

    return 0;
  }

  /** Main method.
      @param args Command line arguments.
  */
  public static void main(String[] args) throws Exception {
	 System.setProperty("java.net.preferIPv4Stack" , "true");
	 System.out.println("Begin of VistaUI main");  
	  
	// Set the look and feel.
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch(Exception lfe) {}

    // Get access to space
    String spaceplace = SPACEPLACE;

    if(args.length != 0)
      spaceplace = args[0];

    // Put up the GUI
    VistaUI vistaUI = new VistaUI(spaceplace);


    vistaUI.setVisible(true);

    System.out.println("End of VistaUI main");
    
 }
 public static DeadEntities getDeadEntities(){
	  return deadEntities;
}

public static LiveEntities getLiveEntities(){
	return liveEntities;
}



}
