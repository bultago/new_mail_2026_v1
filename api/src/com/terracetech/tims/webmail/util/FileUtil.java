/**
 * FileUtil.java 2008. 11. 26.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;

/**
 * <p>
 * <strong>FileUtil.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public class FileUtil {
	public final static int SIZE_KILO = 1024; 
	public final static int SIZE_MEGA = SIZE_KILO * SIZE_KILO; 
	public final static int SIZE_GIGA = SIZE_MEGA * SIZE_KILO;
	
	/**
	 * 
	 * <p>
	 * �ӽ÷� ���� ������ �����Ѵ�.
	 * </p>
	 * 
	 * @param attlist
	 *            ÷�θ��
	 * @return void
	 */
	public void deleteUploadFile(String attlist) {
		StringTokenizer st = new StringTokenizer(attlist, "\n");

		while (st.hasMoreElements()) {
			String strTmp = (String) st.nextElement();
			StringTokenizer st_sub = new StringTokenizer(strTmp, "\t");
			String strFile = (String) st_sub.nextElement();

			File file_src = new File(strFile);
			if (file_src.exists()) {
				file_src.delete();
			}
		}
	}

	public static String getImageFileName(String orgStr, String newStr) {
		if (orgStr == null || newStr == null) {
			return "unknown";
		}

		int pos = orgStr.indexOf(".");

		if (pos > 0) {
			return (newStr + orgStr.substring(pos));
		} else {
			return newStr;
		}
	}
	
	public static void remoteHttpFileDown(String url, String filePath) 
	throws HttpClientError, IOException{
		
		 HttpClient client = new HttpClient();
		 HttpMethod method = new GetMethod(url);
		 int statusCode = 0;		 
		 try {
			statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				copy(method.getResponseBodyAsStream(),new File(filePath));
			}		 		 
		 } finally{
			method.releaseConnection();
		 }
	}

	/**
	 * 
	 * <p>������ �о byte[]�� ��ȯ�� �Ѵ�.</p>
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 * @return byte[]
	 */
	public static byte[] readFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		byte[] bytes = new byte[(int) length];

		try {
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			if (offset < bytes.length) {
				throw new IOException(file.getName());
			}
		} finally {
			is.close();
		}

		return bytes;
	}
	
	public static String[] readFileLine(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));	
		List<String> lineList = new ArrayList<String>();
		String[] lines = null;
		String line = null; 
		try {
			while ((line = reader.readLine()) != null) {
				lineList.add(line);
			}			
		} finally {
			reader.close();
		}
		
		if(lineList.size() > 0){
			lines = new String[lineList.size()];
			lineList.toArray(lines);
		}

		return lines;
	}
	
	public static boolean writeFile(byte[] bytes, File file) {
		boolean jobSuccess = false;
		
		if (bytes == null) {
			return false;
		}
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(bytes);
			out.flush();
			jobSuccess = true;
		} catch (IOException e) {
			LogManager.writeErr(FileUtil.class, e.getMessage(), e);
		} finally {
			FileUtil.closeStream(null, out);
		}
		return jobSuccess;
	}
	
	/**
	 * <p>
	 * 	������ �����Ѵ�.<br>
	 * 	������ ���丮 �ϰ�� ������ ���Ե� ��� �׸�(���丮, ����)�� ���� �Ѵ�.
	 * </p>
	 *
	 * @param fileName
	 * @return void
	 */
	public static void remove (String fileName) {
		remove(new File (fileName));
	}
	
	/**
	 * <p>
	 * 	���ϸ���Ʈ�� �����Ѵ�.<br>
	 * 	������ ���� �� ���丮�� ������ ���Ե� ��� �׸�(���丮, ����)�� ���� �Ѵ�.
	 * </p>
	 *
	 * @param fileName
	 * @return void
	 */
	public static void remove (File[] files) {
		for (File child : files)
			remove(child);
	}
	
	/**
	 * <p>
	 * 	������ �����Ѵ�.<br>
	 * 	������ ���丮 �ϰ�� ������ ���Ե� ��� �׸�(���丮, ����)�� ���� �Ѵ�.
	 * </p>
	 *
	 * @param fileName
	 * @return void
	 */
	public static void remove (File file) {
		if (!file.exists())
			return;
		
		if (file.isDirectory()) {
			remove(file.listFiles());
		}
		
		file.delete();
	}
	
	/**
	 * <p>
	 * ��� �迭�� ��Ʈ���� �ݴ´�.
	 * </p>
	 *
	 * @param in
	 * @param out
	 * @return
	 * @return boolean
	 */
	public static boolean closeStream (Object in, Object out) {
		boolean jobSuccess = false;
		try {
			if (out instanceof OutputStream)
				((OutputStream)out).close();
			else if (out instanceof Writer)
				((Writer)out).close();

			if (in instanceof InputStream)
				((InputStream)in).close();
			else if (in instanceof Reader)
				((Reader)in).close();
			
			jobSuccess = true;
		} catch (IOException e) {
			LogManager.writeErr(FileUtil.class, e.getMessage(), e);
		} finally {
			out = null;
			in = null;
		}
		return jobSuccess;
	}
	
	/**
	 * <p>
	 * ��Ʈ�� ���縦 �Ѵ�.<br>
	 * ���� ���ε�, �ٿ�ε�, �ܼ� ���� � ������ �� �ִ�.<br>
	 * <code>
	 * 	String filPath = "/tmp/tmp.file";
	 * 	FileUtils.copy(filPath, response.getOutputStream());
	 * </code>
	 * </p>
	 * @param from
	 * @param to
	 * @return
	 * @return boolean
	 */
	public final static boolean copy(InputStream from, OutputStream to) {
		if (from == null || to == null)
			return false;
		
		boolean jobSuccess = false;
		int bufferSize = 1024 * 16; //16k
		
		byte[] buffer = new byte[bufferSize];
		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		try {
			input = new BufferedInputStream(from);
			output = new BufferedOutputStream(to);
			int idx = -1;
			while ((idx = input.read(buffer)) != -1) {
				output.write(buffer, 0, idx);
			}
			output.flush();
			jobSuccess = true;
		} catch (Exception e){
			LogManager.writeErr(FileUtil.class, e.getMessage(), e);
		} finally {
			closeStream(input, output);
			input = null;
			output = null;
			buffer = null;
		}
		return jobSuccess;
	}

	public final static boolean copy(File from, File to) {
		return copy(getInputStream(from), getOutputStream(to));
	}
	public final static boolean copy(File from, OutputStream to) {
		return copy(getInputStream(from), to);
	}
	public final static boolean copy(File from, String to) {
		return copy(getInputStream(from), getOutputStream(to));
	}
	public final static boolean copy(String from,File to) {
		return copy(getInputStream(from),  getOutputStream(to));
	}
	public final static boolean copy(String from, OutputStream to) {
		return copy(getInputStream(from), to);
	}
	public final static boolean copy(String from, String to) {
		return copy(getInputStream(from), getOutputStream(to));
	}
	public final static boolean copy(InputStream from, String to) {
		return copy(from, getOutputStream(to));
	}
	public final static boolean copy(InputStream from, File to) {
		return copy(from, getOutputStream(to));
	}
	
	/**
	 * <p>input Ÿ���� �˻��Ͽ� ��Ʈ���� ��ȯ�Ѵ�.</p>
	 *
	 * @param input
	 * @return
	 * @return InputStream
	 */
	private final static InputStream getInputStream (Object input) {
		try {
			if (input instanceof String)
				return new FileInputStream((String)input);
			else if (input instanceof File && ((File)input).exists()){
				return new FileInputStream((File)input);
			}else if (input instanceof InputStream)
				return (InputStream)input;
		} catch (FileNotFoundException fe){			
			LogManager.writeErr(FileUtil.class, fe.getMessage(), fe);
		} 
		return null;
	}
	
	/**
	 * <p>output Ÿ���� �˻��Ͽ� ��Ʈ���� ��ȯ�Ѵ�.</p>
	 *
	 * @param output
	 * @return
	 * @return OutputStream
	 */
	private final static OutputStream getOutputStream (Object output) {
		try {
			if (output instanceof String)
				return new FileOutputStream((String)output);
			else if (output instanceof File)
				return new FileOutputStream((File)output);
			else if (output instanceof OutputStream)
				return (OutputStream)output;
		} catch (FileNotFoundException fe){			
			LogManager.writeErr(FileUtil.class, fe.getMessage(), fe);
		} 
		return null;
	}
	
	/**<p>
	 * 	�ӽ����� ��ü�� �� �ؼ� ��ȯ�Ѵ�.<b>
	 * 	<li>�ӽ� ���ϸ��� 60���� nano�ʷ� ȯ���� Ÿ�� �������̴�.</li>
	 * 	<li>�ӽ� �۾� ���丮�� ���� ��� ���丮�� ���� �ӽ����� ��ü�� ����� ��ȯ�Ѵ�.</li>
	 * </p>
	 * @return File �ӽ����� ��ü
	 */
	public static File makeTmpFile () {
		File tempkDir = new File (EnvConstants.getBasicSetting("tmpdir") + 
										System.getProperty("file.separator")+
										FormatUtil.getTempDirStr());
		if (!tempkDir.exists())
			tempkDir.mkdirs();
		
		return new File (tempkDir, Long.toString(System.nanoTime()));
	}

	public static String saveTmpFile(File file, String fileName) throws IOException {
		
		String saveFileName = EnvConstants.getBasicSetting("tmpdir") + 
										"/" + fileName;		
		FileOutputStream os = new FileOutputStream(saveFileName);
		
		copy(file,os);
		
		return saveFileName;
	}
	
	public static void deletePathFiles(File file) {
    	File[] files = file.listFiles();
    	for(int i=0; i < files.length; i++) {
    		if (files[i].isFile()) {
    			files[i].delete();
    			continue;
    		}
    		if (files[i].isDirectory()) {
    			deletePathFiles(files[i]);
    		}
    		files[i].delete();
    	}
    }
	
	public static byte[] getByteFile(String file) {
		byte[] byMsg = null;

		try
		{
			// File�� �о byte[] ���·� ����
			File fp =new File(file);
			FileInputStream is = new FileInputStream(fp);
			DataInputStream dis = new DataInputStream(is);
			
			byMsg = new byte[dis.available()];
			dis.readFully(byMsg);
			dis.close();		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return byMsg;
	}

	/**
	 * //2012.05.17 - ÷�������� �ٸ� ���� �ö��� ��� attachCheck.url ��ü�� ������ local disk �� ���
	 * @param attachFileName
	 * @return boolean
	 */
	public static boolean checkUploadFiles(String attachFileName) {
		boolean result = false;

		String url = null;
		File saveDir = null;
		String tmpDir = null;
		boolean attachCheckUse = false; 
		String attachCheckUrl = null;
		String[] attachCheckUrls = null;

		URL u = null;
		InputStream in = null;
		OutputStream os = null;

		byte[] buffer = null;
		int size = 0;
		int n = 0;

		try {
			// ��� on . off
			attachCheckUse = Boolean.parseBoolean(EnvConstants.getBasicSetting("attachCheck.use"));
			if (attachCheckUse) {
				attachCheckUrl = EnvConstants.getBasicSetting("attachCheck.url");
				if (StringUtils.isEmpty(attachCheckUrl)) {
					return result;
				}

				tmpDir = EnvConstants.getBasicSetting("tmpdir");
				attachCheckUrls = attachCheckUrl.split(";");

				for (int i = 0; i < attachCheckUrls.length; i++) {
					url = attachCheckUrls[i].trim() + "mail/attachCheck.do?filename=" + attachFileName;
					
					// URL getting
					u = new URL(url);
					in = u.openStream();
					if (in.available() > 0) {
						saveDir = new File(tmpDir + EnvConstants.DIR_SEPARATOR + attachFileName);
						os = new FileOutputStream(saveDir);
						buffer = new byte[1024 * 1024];
						n = 0;
						size = 0;
						while ((n = in.read(buffer)) != -1) {
							os.write(buffer, 0, n);
							size += n;
						}

						os.close();
						result = true;
					}
					in.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			url = null;
			saveDir = null;
			tmpDir = null;
			attachCheckUrl = null;
			attachCheckUrls = null;
			u = null;
			in = null;
			os = null;
			buffer = null;
		}

		return result;
	}
	
    
    public static String replaceBlockingWrongFilePath(String str){
    	str = str.replaceAll("/\\.\\." , "" );
    	str = str.replaceAll("\\\\" , "" );
    	str = str.replaceAll("&" , " ");
    	return str;
    }	
}