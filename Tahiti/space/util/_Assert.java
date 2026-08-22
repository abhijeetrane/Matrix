package space.util;

public class _Assert {
  private static void fail() {
    System.err.println("assertion failed:");
    Throwable throwable = new Throwable();
    throwable.printStackTrace();
    System.exit(1);
  }
  
  public static void _assert(boolean paramBoolean) {
    if (!paramBoolean)
      fail(); 
  }
  
  public static void _assert(long paramLong) {
    if (paramLong == 0L)
      fail(); 
  }
  
  public static void _assert(double paramDouble) {
    if (paramDouble == 0.0D)
      fail(); 
  }
  
  public static void _assert(Object paramObject) {
    if (paramObject == null)
      fail(); 
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\spac\\util\_Assert.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */