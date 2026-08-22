package space.util;

import java.io.InputStream;

public class FastByteArrayInputStream extends InputStream {
  protected byte[] buf = null;
  
  protected int count = 0;
  
  protected int pos = 0;
  
  public FastByteArrayInputStream(byte[] paramArrayOfbyte, int paramInt) {
    this.buf = paramArrayOfbyte;
    this.count = paramInt;
  }
  
  public final int available() {
    return this.count - this.pos;
  }
  
  public final int read() {
    return (this.pos < this.count) ? (this.buf[this.pos++] & 0xFF) : -1;
  }
  
  public final int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (this.pos >= this.count)
      return -1; 
    if (this.pos + paramInt2 > this.count)
      paramInt2 = this.count - this.pos; 
    System.arraycopy(this.buf, this.pos, paramArrayOfbyte, paramInt1, paramInt2);
    this.pos += paramInt2;
    return paramInt2;
  }
  
  public final long skip(long paramLong) {
    if (this.pos + paramLong > this.count)
      paramLong = (this.count - this.pos); 
    if (paramLong < 0L)
      return 0L; 
    this.pos = (int)(this.pos + paramLong);
    return paramLong;
  }
}