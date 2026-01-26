package com.terracetech.tims.webmail.plugin.pki;


public interface IPKIVerifier {	
	public PKIAuthResultBean certificateOnlyText(PKIAuthParamBean paramBean) throws Exception;
	public PKIAuthResultBean certificateWidthVidText(PKIAuthParamBean paramBean) throws Exception;
}
