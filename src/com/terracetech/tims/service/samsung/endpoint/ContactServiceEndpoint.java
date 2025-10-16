package com.terracetech.tims.service.samsung.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.terracetech.tims.service.samsung.IContactService;
import com.terracetech.tims.service.samsung.vo.AttachWSVO;
import com.terracetech.tims.service.samsung.vo.ContactWSVO;

public class ContactServiceEndpoint extends ServletEndpointSupport implements IContactService{

	private IContactService service;
	
	protected void onInit() { 
		this.service = (IContactService) getWebApplicationContext().getBean("contactWebService");
		
		MessageContext msgCtx = (MessageContext) getServletEndpointContext().getMessageContext ();
		HttpServletRequest req = (HttpServletRequest) msgCtx.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
		System.out.println(req.getRemoteAddr());
	}

	public int addContact(String licenseKey, ContactWSVO param) {
		return service.addContact(licenseKey, param);
	}

	public int addContactWithAttach(String licenseKey, ContactWSVO param,  AttachWSVO[] attach) {
		return service.addContactWithAttach(licenseKey, param, attach);
	}

	public int delContact(String licenseKey, ContactWSVO param) {
		return service.delContact(licenseKey, param);
	}

	public ContactWSVO getContact(String licenseKey, ContactWSVO param) {
		return service.getContact(licenseKey, param);
	}

	public ContactWSVO[] getContactList(String licenseKey, ContactWSVO param) {
		return service.getContactList(licenseKey, param);
	}

	public int modContact(String licenseKey, ContactWSVO param) {
		return service.modContact(licenseKey, param);
	}

	public int modContactWithAttach(String licenseKey, ContactWSVO param, AttachWSVO[] attach) {
		return service.modContactWithAttach(licenseKey, param, attach);
	}

	public String testConnect() {
		return "success";
	}
	
}
