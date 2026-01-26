package com.terracetech.tims.webmail.plugin.pki;

public class IntechEPKIVerifierV7Impl implements IPKIVerifier {

	public PKIAuthResultBean certificateOnlyText(PKIAuthParamBean paramBean)
			throws Exception {
		
		PKIAuthResultBean authInfo = new PKIAuthResultBean();
		authInfo.setAuth(true);
		authInfo.setUserDn(paramBean.getUserDN());

		return authInfo;
	}

	public PKIAuthResultBean certificateWidthVidText(PKIAuthParamBean paramBean)
			throws Exception {
		
		PKIAuthResultBean authInfo = new PKIAuthResultBean();
		authInfo.setAuth(true);
		authInfo.setUserDn(paramBean.getUserDN());
		
		return authInfo;
	}

}
