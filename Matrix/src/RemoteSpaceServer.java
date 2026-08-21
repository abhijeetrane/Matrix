import space.remote.RemoteSpace;

public class RemoteSpaceServer {
  public static final Integer REGISTRY_PORT = new Integer(1099);
  
  public static final String SERVER_LOCATION = "rmi://localhost/space";
  
  public static void main(String[] paramArrayOfString) {
    String str;
    if (paramArrayOfString.length == 0) {
      str = "rmi://localhost/space";
    } else {
      str = paramArrayOfString[0];
    } 
    try {
      RemoteSpace remoteSpace = new RemoteSpace(str, REGISTRY_PORT);
    } catch (Exception exception) {
      System.err.println(exception);
    } 
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\RemoteSpaceServer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */