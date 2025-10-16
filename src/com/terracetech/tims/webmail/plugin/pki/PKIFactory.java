package com.terracetech.tims.webmail.plugin.pki;

import com.terracetech.tims.webmail.common.ExtPartConstants;

public class PKIFactory {		
	public static IPKIVerifier getVerifier(){
		IPKIVerifier verifier = null;
		if(ExtPartConstants.getPKIVender() == ExtPartConstants.VENDER_SOFTFORUM){
			if(ExtPartConstants.EPKI_MODE == ExtPartConstants.getPKIMode()){
				verifier = new SoftForumEPKIVerifierImpl();
			}
		} else if(ExtPartConstants.getPKIVender() == ExtPartConstants.VENDER_INITECH_V7){
			if(ExtPartConstants.EPKI_MODE == ExtPartConstants.getPKIMode()){
				verifier = new IntechEPKIVerifierV7Impl();
			}
		} else if(ExtPartConstants.getPKIVender() == ExtPartConstants.VENDER_KERIS){
			if(ExtPartConstants.EPKI_MODE == ExtPartConstants.getPKIMode()){
				verifier = new KerisEPKIVerifierImpl();
			}
		}
		return verifier;
	}	

}
