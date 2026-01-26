/**
 * ByteArrayDataSource.java 2008. 12. 5.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jakarta.activation.DataSource;

/**
 * <p><strong>ByteArrayDataSource.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jpjung
 * @since Tims7
 * @version 7.0 
 */
public class ByteArrayDataSource implements DataSource {

	private byte[] data;
	
	
	public ByteArrayDataSource(byte[] data) {
		super();
		this.data = data;
	}

	/**
	 * <p></p>
	 *
	 * @see javax.activation.DataSource#getContentType()
	 * @return 
	 */
	public String getContentType() {
		return "image/gif";
	}

	/**
	 * <p></p>
	 *
	 * @see javax.activation.DataSource#getInputStream()
	 * @return
	 * @throws IOException 
	 */
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(data);
	}

	/**
	 * <p></p>
	 *
	 * @see javax.activation.DataSource#getName()
	 * @return 
	 */
	public String getName() {
		return null;
	}

	/**
	 * <p></p>
	 *
	 * @see javax.activation.DataSource#getOutputStream()
	 * @return
	 * @throws IOException 
	 */
	public OutputStream getOutputStream() throws IOException {
		return new ByteArrayOutputStream();
	}

}
