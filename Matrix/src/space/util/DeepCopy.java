package space.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeepCopy {
  public static Object copy(Object paramObject) {
    Object object = null;
    try {
      FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fastByteArrayOutputStream);
      objectOutputStream.writeObject(paramObject);
      objectOutputStream.flush();
      objectOutputStream.close();
      ObjectInputStream objectInputStream = new ObjectInputStream(fastByteArrayOutputStream.getInputStream());
      object = objectInputStream.readObject();
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } catch (ClassNotFoundException classNotFoundException) {
      classNotFoundException.printStackTrace();
    } 
    return object;
  }
}