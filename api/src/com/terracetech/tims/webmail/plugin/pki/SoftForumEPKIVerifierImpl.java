package com.terracetech.tims.webmail.plugin.pki;


// import xecure.crypto.SignVerifier;
// import xecure.crypto.VidVerifier;
import xecure.servlet.XecureConfig;

public class SoftForumEPKIVerifierImpl implements IPKIVerifier {

	public PKIAuthResultBean certificateOnlyText(PKIAuthParamBean paramBean) throws Exception {
		PKIAuthResultBean authInfo = new PKIAuthResultBean();
		SignVerifier verifier = new SignVerifier (new XecureConfig(),paramBean.getSignedText());
		
		int	nVerifierResult = verifier.getLastError();
		if ( nVerifierResult != 0 ) {
			authInfo.setAuth(false);
			authInfo.setErrorCode(nVerifierResult);
			authInfo.setErrorMsg(verifier.getLastErrorMsg());
		} else {
			String userDN = verifier.getSignerCertificate().getSubject();
			if(userDN != null && userDN.length() > 0){
				authInfo.setAuth(true);
				authInfo.setUserDn(userDN);
			} else {
				authInfo.setAuth(false);				
			}
			
		}
		
		return authInfo;
	}
	
	public PKIAuthResultBean certificateWidthVidText(PKIAuthParamBean paramBean) throws Exception {
		PKIAuthResultBean authInfo = new PKIAuthResultBean();
		SignVerifier verifier = new SignVerifier (new XecureConfig(),paramBean.getSignedText());
		VidVerifier vid = new VidVerifier(new XecureConfig());
		
		
		int	nVerifierResult = verifier.getLastError();
		if ( nVerifierResult != 0 ) {
			authInfo.setAuth(false);
			authInfo.setErrorCode(nVerifierResult);
			authInfo.setErrorMsg(verifier.getLastErrorMsg());
		} else {
			String userDN = verifier.getSignerCertificate().getSubject();
			if(userDN != null && userDN.length() > 0){
				authInfo.setAuth(true);
				authInfo.setUserDn(userDN);
			} else {
				authInfo.setAuth(false);				
			}
			
		}
		
		vid.virtualIDVerifyS(paramBean.getSignedVid(), verifier.getSignerCertificate().getCertPem());
		int	nResult = vid.getLastError();
		if( nResult != 0) {			
			authInfo.setAuth(false);
			authInfo.setErrorCode(nResult);
			authInfo.setErrorMsg(vid.getLastErrorMsg());
		}
		else {			
			String ssn = vid.getIdn();
			if(ssn != null && ssn.length() > 0){
				authInfo.setAuth(true);
				authInfo.setVid(ssn);
			} else {
				authInfo.setAuth(false);				
			}	
		}
		
		return authInfo;
	}

}
