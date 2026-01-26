package com.terracetech.tims.webmail.plugin.pki;

import com.epki.api.EpkiApi;
import com.epki.cert.CertValidator;
import com.epki.cert.X509Certificate;
import com.epki.cms.SignedData;
import com.epki.conf.ServerConf;
import com.epki.crypto.PrivateKey;
import com.epki.exception.CRLException;
import com.epki.exception.CertificateExpiredException;
import com.epki.exception.CertificateRevokedException;
import com.epki.exception.EpkiException;
import com.epki.vid.IdentityValidator;

public class KerisEPKIVerifierImpl implements IPKIVerifier {

	public PKIAuthResultBean certificateOnlyText(PKIAuthParamBean paramBean)
			throws Exception {
		PKIAuthResultBean authInfo = new PKIAuthResultBean();
		authInfo.setAuth(false);
		authInfo.setError(true);
		try{			
			EpkiApi.initApp();
			SignedData signedData = new SignedData();			
			signedData.verify(paramBean.getSignedText());
			
			X509Certificate signerCert = signedData.getSignerCert(0);		
			CertValidator validator = new CertValidator();
				
			validator.setValidateCertPathOption(CertValidator.CERT_VERIFY_FULLPATH);
			validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_ALL);
			
			validator.validate(CertValidator.CERT_TYPE_SIGN, signerCert);
			
			authInfo.setAuth(true);
			authInfo.setError(false);
			authInfo.setUserDn(signerCert.getSubjectName());
		} catch (CertificateExpiredException e){
			authInfo.setErrorMsg("Certificate Error Expired !!!");			
		} catch (CertificateRevokedException e){			
			authInfo.setErrorMsg("Certificate Error Revoked !!!");
		} catch (CRLException e){
			authInfo.setErrorMsg("Certificate CRL Error !!!");
		} catch (EpkiException e){
			authInfo.setErrorMsg("Certificate Error !!!");
		}
		
		return authInfo;
	}

	public PKIAuthResultBean certificateWidthVidText(PKIAuthParamBean paramBean)
			throws Exception {
		
		PKIAuthResultBean authInfo = new PKIAuthResultBean();
		try
		{
			EpkiApi.initApp();
			ServerConf conf = new ServerConf();
					
			X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);
			PrivateKey priKey = conf.getServerPriKey(ServerConf.CERT_TYPE_KM, "");
			
			IdentityValidator vidData = new IdentityValidator();
			vidData.verify(cert, priKey, paramBean.getSignedText(), paramBean.getSsn());
			
			X509Certificate clientCert;
			clientCert = vidData.getUserCert();
			
			CertValidator validator = new CertValidator();	

			validator.setValidateCertPathOption(CertValidator.CERT_VERIFY_FULLPATH);
			validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_ALL);
			
			validator.validate(CertValidator.CERT_TYPE_SIGN, clientCert);
			
			// ¿äÃ»ÀÚ ÀÎÁõ¼­ Á¤º¸ È¹µæ
			String strUserDN = clientCert.getSubjectName();
			authInfo.setAuth(true);
			authInfo.setError(false);
			authInfo.setUserDn(strUserDN);
		}
		catch (EpkiException e){
			e.printStackTrace();
			authInfo.setAuth(false);
			authInfo.setError(true);
			authInfo.setErrorMsg("Verify VID Error!<BR>");			
		}
		return authInfo;
	}

}
