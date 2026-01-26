package com.terracetech.tims.webmail.addrbook.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Locale;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.exception.SaveFailedException;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.CSVReader;
import com.terracetech.tims.webmail.util.StringUtils;

public class ImportAddrAction  extends BaseAction{

	private static final long serialVersionUID = 20081215L;
	
	private AddressBookManager manager;
	
	private int theGroupSeq;

	private int theBookSeq;
	
	private int theVendor;
	
	private String result;
	
	private String encoding;
	
	private String dupAddrType;
	
	public void setDupAddrType(String dupAddrType) {
		this.dupAddrType = dupAddrType;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public void setTheVendor(int theVendor) {
		this.theVendor = theVendor;
	}

	public void setTheGroupSeq(int theGroupSeq) {
		this.theGroupSeq = theGroupSeq;
	}

	public void setTheBookSeq(int theBookSeq) {
		this.theBookSeq = theBookSeq;
	}
	
	/**
	 * shift_jis
	 * 
	 * @return
	 */
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}
	
	public String execute() throws Exception{
		Locale locale = new Locale(user.get(User.LOCALE));
		manager.setResource(new I18nResources(locale,"addr"));
		
		if(StringUtils.isEmpty(encoding)){
			if("jp".equals(user.get(User.LOCALE))){
				setEncoding("shift-jis");	
			}else if("ko".equals(user.get(User.LOCALE))){
				setEncoding("euc-kr");	
			}else{
				setEncoding("");
			}
		}
			
		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		MultiPartRequestWrapper multiWrapper = null;	
		
		if(request instanceof MultiPartRequestWrapper) {
			multiWrapper = (MultiPartRequestWrapper) request;		
		}
		CSVReader csv = null;
		try {
			if(multiWrapper.getFiles("theFile").length!=0){
				File attFile = multiWrapper.getFiles("theFile")[0];
				
				String sb = getContents(attFile);
				
				BufferedReader reader = new BufferedReader(new StringReader(sb.toString().replaceAll("\"\"", "").replaceAll("\"", "")));
				
				csv =new CSVReader(reader, ',', '\"');
				
				String[] results = csv.getAllFieldsInLine();
				manager.importAddressMember(encoding, results, userSeq, theBookSeq, theGroupSeq, domainSeq, theVendor , dupAddrType);
				
				result = "success";
			}	
		}catch (SaveFailedException e) {
			result = "license";
		}catch (Exception e) {
			result = "failed";
		}finally{
			try {
				csv.close();	
			} catch (Exception ignore) {
				LogManager.writeErr(this, ignore.getMessage(), ignore);	
			}
		}
				
		return "success";
	}

	private String getContents(File attFile) throws IOException {
		InputStreamReader isr = null;
		StringBuffer sb = new StringBuffer();
		try {
			if(StringUtils.isNotEmpty(encoding)){
				isr = new InputStreamReader(new FileInputStream(attFile), encoding);
				
				BufferedReader reader1 = new BufferedReader(isr);

				String line = reader1.readLine();
				
				while ((line = reader1.readLine()) != null) {
					if(line.startsWith("\",")){
						sb.append(line);
					}else{
						sb.append('\n').append(line);
					}
				}
				try {
					reader1.close();
				} catch (Exception e) {
					LogManager.writeErr(this, e.getMessage(), e);	
				}
			}else{
				isr = new InputStreamReader(new FileInputStream(attFile));
				
				BufferedReader reader1 = new BufferedReader(isr);

				String line = reader1.readLine();
				
				while ((line = reader1.readLine().trim()) != null) {
					//�ƿ��� 2007���� csv �������⸦ �ϸ� 2���� �ϳ��� �ּҸ� ������ �ֱ� ���ǰ� �ʿ��ϴ�.
					if(line.endsWith(",\"")){
						sb.append(line);
					}else{
						sb.append(line).append('\n');
					}
				}
				try {
					reader1.close();
				} catch (Exception e) {
					LogManager.writeErr(this, e.getMessage(), e);	
				}
			}
			
			
		} catch (Exception e) {
		}finally{
			try {
				isr.close();
			} catch (Exception ignore) {
				LogManager.writeErr(this, ignore.getMessage(), ignore);	
			}
		}
		
		
		return sb.toString();
	}
	
	
}
