package space.remote;

import java.rmi.Naming;

public class SpaceAccessor {
  public static String SPACENAME = "rmi://localhost/space";
  
  public static MicroSpace getSpace() throws Exception {
    return getSpace(SPACENAME);
  }
  
  public static MicroSpace getSpace(String paramString) throws Exception {
    RemoteSpaceService remoteSpaceService = (RemoteSpaceService)Naming.lookup(paramString);
    return new MicroSpace(remoteSpaceService);
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\remote\SpaceAccessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */