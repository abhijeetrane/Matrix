package space.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UnoptimizedDeepCopy {
  public static Object copy(Object paramObject) {
    Object object = null;
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objectOutputStream.writeObject(paramObject);
      objectOutputStream.flush();
      objectOutputStream.close();
      ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
      object = objectInputStream.readObject();
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } catch (ClassNotFoundException classNotFoundException) {
      classNotFoundException.printStackTrace();
    } 
    return object;
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\spac\\util\UnoptimizedDeepCopy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */