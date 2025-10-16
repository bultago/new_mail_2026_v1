package com.sun.mail.handlers;

import com.sun.mail.util.UTF7Decoder;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeUtility;
import sun.io.MalformedInputException;

public class text_plain
  implements DataContentHandler
{
  private static ActivationDataFlavor myDF = new ActivationDataFlavor(String.class, "text/plain", "Text String");

  protected ActivationDataFlavor getDF()
  {
    return myDF;
  }

  public DataFlavor[] getTransferDataFlavors()
  {
    return new DataFlavor[] { getDF() };
  }

  public Object getTransferData(DataFlavor df, DataSource ds)
    throws IOException
  {
    if (getDF().equals(df)) {
      return getContent(ds);
    }
    return null;
  }

  public Object getContent(DataSource ds) throws IOException {
    String enc = null;
    InputStreamReader is = null;
    try
    {
      enc = getCharset(ds.getContentType());
      try {
        is = new InputStreamReader(ds.getInputStream(), enc);
      }
      catch (UnsupportedEncodingException e) {
        if ((enc.equalsIgnoreCase("UTF-7")) || (enc.equalsIgnoreCase("UTF7"))) {
          return UTF7Decoder.decode(ds.getInputStream());
        }

        is = new InputStreamReader(ds.getInputStream());
      }

    }
    catch (IllegalArgumentException iex)
    {
      throw new UnsupportedEncodingException(enc);
    }
    
    int pos = 0;

    char[] buf = new char[8192];
    StringBuffer sb;
    int count;
    try { 
      sb = new StringBuffer();
      while ((count = is.read(buf, 0, 8192)) != -1)
      {
        sb.append(buf, 0, count);
      }

      return sb.toString();
    }
    catch (MalformedInputException e)
    {
      is = new InputStreamReader(ds.getInputStream(), "8859_1");
      sb = new StringBuffer();
      while ((count = is.read(buf, 0, 8192)) != -1)
      {
        sb.append(buf, 0, count);
      }
    }

    return sb.toString();
  }

  public void writeTo(Object obj, String type, OutputStream os)
    throws IOException
  {
    if (!(obj instanceof String)) {
      throw new IOException("\"" + getDF().getMimeType() + "\" DataContentHandler requires String object, " + "was given object of type " + obj.getClass().toString());
    }

    String enc = null;
    OutputStreamWriter osw = null;
    try
    {
      enc = getEncCharset(type);
      osw = new OutputStreamWriter(os, enc);
    }
    catch (IllegalArgumentException iex)
    {
      throw new UnsupportedEncodingException(enc);
    }

    String s = (String)obj;
    osw.write(s, 0, s.length());
    osw.flush();
  }

  private String getEncCharset(String type)
  {
    try
    {
      ContentType ct = new ContentType(type);
      String charset = ct.getParameter("charset");

      if ((charset == null) || (charset.length() == 0)) {
        return MimeUtility.getDefaultJavaCharset();
      }

      charset = MimeUtility.encJavaCharset(charset);
      return charset;
    }
    catch (Exception ex)
    {
    }

    return null;
  }

  private String getCharset(String type)
  {
    try {
      ContentType ct = new ContentType(type);
      String charset = ct.getParameter("charset");

      if ((charset == null) || (charset.length() == 0)) {
        return MimeUtility.getDefaultJavaCharset();
      }

      charset = MimeUtility.javaCharset(charset);
      return charset;
    }
    catch (Exception ex)
    {
    }

    return null;
  }
}