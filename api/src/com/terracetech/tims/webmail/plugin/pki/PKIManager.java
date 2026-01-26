package com.terracetech.tims.webmail.plugin.pki;

import com.terracetech.tims.webmail.common.ExtPartConstants;

public class PKIManager {
	private IPKIVerifier verifier = null;
	public PKIManager() {
		this.verifier = PKIFactory.getVerifier();
	}
	
	public PKIAuthResultBean getLoginCertificate(PKIAuthParamBean pkiParam) throws Exception{
		PKIAuthResultBean resultBean = null;
		if(ExtPartConstants.VENDER_SOFTFORUM == ExtPartConstants.getPKIVender()){
			resultBean = verifier.certificateOnlyText(pkiParam);
		} else if(ExtPartConstants.VENDER_INITECH_V7 == ExtPartConstants.getPKIVender()){
			resultBean = verifier.certificateOnlyText(pkiParam);
		} else if(ExtPartConstants.VENDER_KERIS == ExtPartConstants.getPKIVender()){
			resultBean = verifier.certificateOnlyText(pkiParam);
		}
		return resultBean;
	}
	
	public PKIAuthResultBean getRegistCertificate(PKIAuthParamBean pkiParam) throws Exception{
		PKIAuthResultBean resultBean = null;
		if(ExtPartConstants.VENDER_SOFTFORUM == ExtPartConstants.getPKIVender()){
			resultBean = verifier.certificateWidthVidText(pkiParam);
			if(resultBean.isError()){
				throw new Exception("PKI Certificate Error ["+resultBean.getErrorMsg()+"]");
			}				
			if(!resultBean.isAuth()){
				throw new Exception("PKI Certificate No User Infomation");
			}		
			
		} else if(ExtPartConstants.VENDER_INITECH_V7 == ExtPartConstants.getPKIVender()){
			resultBean = verifier.certificateWidthVidText(pkiParam);			
		} else if(ExtPartConstants.VENDER_KERIS == ExtPartConstants.getPKIVender()){
			resultBean = verifier.certificateWidthVidText(pkiParam);
			
			if(resultBean.isError()){
				throw new Exception("PKI Certificate Error ["+resultBean.getErrorMsg()+"]");
			}				
			if(!resultBean.isAuth()){
				throw new Exception("PKI Certificate No User Infomation");
			}
		}
		
		return resultBean;
	}
	
		
}
