package com.terracetech.tims.webmail.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;

public class ByteUtil {
	
	public static byte[] getContent(InputStream is, int sizeHint) throws IOException {
		return getContent(is, sizeHint, -1);
	} 
	 
	public static byte[] getContent(InputStream is, int sizeHint, boolean close) throws IOException {
		return getContent(is, -1, sizeHint, -1, close);
	}
	 
	public static byte[] getContent(InputStream is, int sizeHint, long sizeLimit)throws IOException {
		return getContent(is, -1, sizeHint, sizeLimit, true);
	}
	
	 private static byte[] getContent(InputStream is, int length, int sizeHint, long sizeLimit, boolean close) throws IOException {
	        if (length == 0)
	            return new byte[0];

	        try {
	            BufferStream bs = sizeLimit == -1 ?  new BufferStream(sizeHint,
	                Integer.MAX_VALUE, Integer.MAX_VALUE) : new BufferStream(sizeHint,
	                (int)sizeLimit, sizeLimit);
	            
	            bs.readFrom(is, length == -1 ? Long.MAX_VALUE : length);
	            if (sizeLimit > 0 && bs.size() > sizeLimit)
	                throw new IOException("stream too large");
	            return bs.toByteArray();
	        } finally {
	            if (close)
	                closeStream(is);
	        }
	    } 
	 

	public static byte[] getContent(File file) throws IOException {
		byte[] buffer = new byte[(int) file.length()];
		
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			int total_read = 0, num_read;
			
			int num_left = buffer.length;
			
			while (num_left > 0 && (num_read = is.read(buffer, total_read, num_left)) != -1) {
				total_read += num_read;
				num_left -= num_read;
			}
		} finally {
            closeStream(is);
		}
		return buffer;
	} 
	
	public static void closeStream(InputStream is) {
        if (is == null)
            return;

        if (is instanceof PipedInputStream) {
            try {
                while (is.read() != -1);
            } catch (Exception e) {
            }
        }

        try {
            is.close();
        } catch (Exception ignore) {
        }
    } 
	
	public static void closeStream(OutputStream os) {
        if (os == null)
            return;

        try {
            os.close();
        } catch (Exception ignore) {
        }
    }
	
	public static long copy(InputStream in, boolean closeIn, OutputStream out, boolean closeOut) throws IOException {
        return copy(in, closeIn, out, closeOut, -1L);
    } 
	
	public static long copy(InputStream in, boolean closeIn, OutputStream out, boolean closeOut, long maxLength) throws IOException {
        try {
            long transferred = 0;
            if (maxLength != 0) {
                byte buffer[] = new byte[8192];
                int numRead;
                do {
                    int readMax = buffer.length;
                    if (maxLength > 0 && maxLength - transferred < readMax)
                        readMax = (int) (maxLength - transferred);

                    if ((numRead = in.read(buffer, 0, readMax)) < 0)
                        break;
                    out.write(buffer, 0, numRead);
                    transferred += numRead;
                } while (maxLength < 0 || transferred < maxLength);
            }
            return transferred;
        } finally {
            if (closeIn)
                closeStream(in);
            if (closeOut)
                closeStream(out);
        }
    }
}
