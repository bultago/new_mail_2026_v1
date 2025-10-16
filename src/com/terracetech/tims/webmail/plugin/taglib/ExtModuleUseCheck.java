package com.terracetech.tims.webmail.plugin.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.terracetech.tims.webmail.common.ExtPartConstants;

public class ExtModuleUseCheck extends BodyTagSupport {
	
	private static final long serialVersionUID = -8492524846885141291L;
	
	private boolean msie = false;
	private String moduleName = null;
		
	public void setMsie(boolean msie) {
		this.msie = msie;
	}
	
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	
	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}
	
	public int doAfterBody () throws JspException {
		try {			
			String agent = ((HttpServletRequest) pageContext.getRequest()).getHeader("user-agent");			
			boolean isMsieCheck = (agent != null && agent.toUpperCase().indexOf("MSIE") > 0)?true : false;
            BodyContent body = getBodyContent();
            String content = body.getString().trim();
            boolean isMatch = false;
            isMsieCheck = (msie)?isMsieCheck:true;            
            
            if((isMsieCheck) && moduleName.equalsIgnoreCase("ckweb") && 
        			ExtPartConstants.isCkWebProUse()){isMatch = true;}
        	else if((isMsieCheck) && moduleName.equalsIgnoreCase("ckkey") && 
        			ExtPartConstants.isCkKeyProUse()){isMatch = true;}
        	else if((isMsieCheck) && moduleName.equalsIgnoreCase("xecureWeb") && 
        			ExtPartConstants.isXecureWebUse()){isMatch = true;}
        	else if((isMsieCheck) && moduleName.equalsIgnoreCase("expressE") && 
        			ExtPartConstants.isXecureExpressE()){isMatch = true;}
        	else if((isMsieCheck) && moduleName.equalsIgnoreCase("keris") && 
        			ExtPartConstants.isKerisUse()){isMatch = true;}
            
            body.clearBody();           
            
            getPreviousOut().print((isMatch)?content:"");
        }
        catch (IOException e) {
            throw new JspTagException("IO exception " + e.getMessage());
        }

        return SKIP_BODY;
	}
	
	public void release() {
        super.release();
        moduleName = null;
        msie = false;    
    }
}
