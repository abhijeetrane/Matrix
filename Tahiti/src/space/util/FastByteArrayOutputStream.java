package space.util;

import java.io.InputStream;
import java.io.OutputStream;

public class FastByteArrayOutputStream extends OutputStream {
  protected byte[] buf = null;
  
  protected int size = 0;
  
  public FastByteArrayOutputStream() {
    this(5120);
  }
  
  public FastByteArrayOutputStream(int paramInt) {
    this.size = 0;
    this.buf = new byte[paramInt];
  }
  
  private void verifyBufferSize(int paramInt) {
    if (paramInt > this.buf.length) {
      byte[] arrayOfByte = this.buf;
      this.buf = new byte[Math.max(paramInt, 2 * this.buf.length)];
      System.arraycopy(arrayOfByte, 0, this.buf, 0, arrayOfByte.length);
      arrayOfByte = null;
    } 
  }
  
  public int getSize() {
    return this.size;
  }
  
  public byte[] getByteArray() {
    return this.buf;
  }
  
  public final void write(byte[] paramArrayOfbyte) {
    verifyBufferSize(this.size + paramArrayOfbyte.length);
    System.arraycopy(paramArrayOfbyte, 0, this.buf, this.size, paramArrayOfbyte.length);
    this.size += paramArrayOfbyte.length;
  }
  
  public final void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    verifyBufferSize(this.size + paramInt2);
    System.arraycopy(paramArrayOfbyte, paramInt1, this.buf, this.size, paramInt2);
    this.size += paramInt2;
  }
  
  public final void write(int paramInt) {
    verifyBufferSize(this.size + 1);
    this.buf[this.size++] = (byte)paramInt;
  }
  
  public void reset() {
    this.size = 0;
  }
  
  public InputStream getInputStream() {
    return new FastByteArrayInputStream(this.buf, this.size);
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\spac\\util\FastByteArrayOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */