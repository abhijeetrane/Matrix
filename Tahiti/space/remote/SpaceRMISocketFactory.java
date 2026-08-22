package space.remote;

import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

public class SpaceRMISocketFactory extends RMISocketFactory {
  private int serverPort = 0;
  
  public SpaceRMISocketFactory() {}
  
  public SpaceRMISocketFactory(int paramInt) {
    this.serverPort = paramInt;
  }
  
  public Socket createSocket(String paramString, int paramInt) {
    Socket socket = null;
    try {
      socket = new Socket(paramString, paramInt);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    return socket;
  }
  
  public ServerSocket createServerSocket(int paramInt) {
    ServerSocket serverSocket = null;
    try {
      if (paramInt == 0)
        paramInt = this.serverPort; 
      serverSocket = new ServerSocket(paramInt);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    return serverSocket;
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\remote\SpaceRMISocketFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */