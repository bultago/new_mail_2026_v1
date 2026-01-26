package com.terracetech.tims.webmail.mail.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import java.io.File;
import java.io.FileInputStream;
import com.terracetech.tims.webmail.common.EnvConstants;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

@SuppressWarnings("unchecked")
public class AttachCheckAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, Preparable {

	private static final long serialVersionUID = -7899408186307574274L;
	public HttpServletRequest request = null;
	public HttpServletResponse response = null;
	public Logger log = LoggerFactory.getLogger(this.getClass());

	public String execute() throws Exception {

		String strFile = request.getParameter("filename");
		if (strFile == null) {
			return null;
		}

		if (strFile.indexOf("..\\") > -1 || strFile.indexOf(".\\") > -1
				|| strFile.indexOf("./") > -1
				|| strFile.indexOf("../") > -1) {
			return null;
		}

		byte[] buffer = null;
		InputStream in = null;
		BufferedOutputStream out = null;

		String tmpDir = EnvConstants.getBasicSetting("tmpdir");
		String tmpPath = tmpDir + EnvConstants.DIR_SEPARATOR + strFile;
		File targetFile = new File(tmpPath);
		if (targetFile.exists()) {
			// DOWNLOAD FILE
			try {
				int n;				
				buffer = new byte[1024 * 1024];
				in = new FileInputStream(targetFile);
				out = new BufferedOutputStream(response.getOutputStream());
				while ((n = in.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, n);
				}
				out.flush();
			} catch (Exception e) {
				log.error(e.getMessage());
			} finally {
				try {
					in.close();
					out.close();
					in = null;
					out = null;
					buffer = null;
				} catch (Exception ignore) { }
			}
		} else {
			// DOWNLOAD FILE NOT EXIST
			try {
				buffer = new byte[1];
				out = new BufferedOutputStream(response.getOutputStream());				
				out.write(buffer, 0, 0);
				out.flush();
			} catch (Exception e) {
				log.error(e.getMessage());
			} finally {
				try {
					out.close();
					out = null;
					buffer = null;
				} catch (Exception ignore) { }
			}			
		}
		return null;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void prepare() throws Exception {
	}
}
