package com.terracetech.tims.webmail.plugin.taglib;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspTagException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.BodyContent;
import jakarta.servlet.jsp.tagext.BodyTagSupport;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.ExtPartConstants;

public class PKICheckTag extends BodyTagSupport {

	private static final long serialVersionUID = -8312597974157376630L;
	
	private String vender = null;
	private String pkimode = null;
	private boolean msie = false;
	private String loginMode = null;;
	private boolean print = false;
	
	public void setVender(String vender) {
		this.vender = vender;
	}
	public void setPkimode(String pkimode) {
		this.pkimode = pkimode;
	}	
	public void setMsie(boolean msie) {
		this.msie = msie;
	}
	public void setLoginMode(String loginMode) {
		this.loginMode = loginMode;
	}
	public void setPrint(boolean print) {
		this.print = print;
	}
	@Override
	public int doStartTag() throws JspException {
		
		int result = EVAL_BODY_BUFFERED;
		try{
			if(print){			
				JSONObject jObj = new JSONObject();
				jObj.put("vender", ExtPartConstants.getPKIVenderStr().toUpperCase());
				jObj.put("mode", ExtPartConstants.getPKIModeStr().toUpperCase());
				jObj.put("loginPKI", ExtPartConstants.isPkiLoginUse());
				jObj.put("useage", ExtPartConstants.getExtPartConfig("pki.use"));
				
				JspWriter writer = pageContext.getOut();
	            writer.print(jObj.toString());
				result = SKIP_BODY;
				
			}
		} catch (Exception e) {
			throw new JspTagException("IO exception " + e.getMessage());
		}
		return result;
	}
	
	@Override
	public int doAfterBody() throws JspException {
		try{			
			BodyContent body = getBodyContent();
			String content = body.getString().trim();
			boolean isMatch = false;
			if(!print){
				String agent = ((HttpServletRequest) pageContext.getRequest()).getHeader("user-agent");			
				boolean isMsieCheck = (agent != null && agent.toUpperCase().indexOf("MSIE") > 0)?true : false;
				int venderType = -1;
				int pkiModeType = -1;				
				isMsieCheck = (msie)?isMsieCheck:true;
				
				if(loginMode != null){
					if(loginMode.equalsIgnoreCase("PKI") && 
						ExtPartConstants.isPkiLoginUse()){
						isMatch = true;
					}
				}
											
				if(vender != null || pkimode != null){
					if(vender != null){
						if(vender.equalsIgnoreCase("softforum")){
							venderType = ExtPartConstants.VENDER_SOFTFORUM;
						}
						if(venderType == ExtPartConstants.getPKIVender()){
							isMatch = true;
						} else {
							isMatch = false;
						}					
					}
					
					if(pkimode != null){
						if(pkimode.equalsIgnoreCase("EPKI")){
							pkiModeType = ExtPartConstants.EPKI_MODE;
						}
						if(pkiModeType == ExtPartConstants.getPKIVender()){
							isMatch = true;
						} else {
							isMatch = false;
						}
					}
									
				}
				isMatch = (isMatch && isMsieCheck);
				body.clearBody();
				getPreviousOut().print((isMatch)?content:"");
			}			
		} catch (IOException e) {
			throw new JspTagException("IO exception " + e.getMessage());
		}
        
		return SKIP_BODY;
	}
	
	public void release() {
        super.release();
        vender = null;
        pkimode = null;        
    }
}
