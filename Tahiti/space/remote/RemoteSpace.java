package space.remote;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMISocketFactory;
import space.nucleus.Nucleus;

public class RemoteSpace extends Nucleus {
  private static final Integer REGISTRY_PORT = new Integer(1099);
  
  private static final String DEFAULT_URL = "rmi://localhost/space";
  
  public RemoteSpace() throws Exception {
    this("rmi://localhost/space", REGISTRY_PORT, null);
  }
  
  public RemoteSpace(String paramString) throws Exception {
    this(paramString, REGISTRY_PORT, null);
  }
  
  public RemoteSpace(String paramString, Integer paramInteger) throws Exception {
    this(paramString, paramInteger, null);
  }
  
  public RemoteSpace(String paramString, Integer paramInteger1, Integer paramInteger2) throws Exception {
    super(paramString);
    System.out.println("Constructor RemoteSpace Begin "); 
    try {
      System.out.println("Constructor RemoteSpace try 1 ");	
      if (paramInteger2 != null) {
    	  
    	  System.out.println("Constructor RemoteSpace try paramInteger2 is not null"+paramInteger2);
    	  
    	  RMISocketFactory.setSocketFactory(new SpaceRMISocketFactory(paramInteger2.intValue())); 
      
      }
      System.out.println("Constructor RemoteSpace try 2 ");
      
      int i = (paramInteger1 != null) ? paramInteger1.intValue() : REGISTRY_PORT.intValue();
      
      System.out.println("Constructor RemoteSpace try 3 ");
      
      LocateRegistry.createRegistry(i);
    
      System.out.println("Constructor RemoteSpace try 4 ");
      
      Naming.rebind(paramString, (Remote)this);
    
    } catch (Exception exception) {
      System.err.println(exception);
    } 
    System.out.println("Constructor RemoteSpace End "); 
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\remote\RemoteSpace.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */