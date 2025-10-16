package com.terracetech.tims.webmail.common;

import java.util.Map;

public class ExtPartConstants {
	
	public static final int VENDER_SOFTFORUM = 1;
	public static final int VENDER_INITECH_V7 = 2;
	public static final int VENDER_KERIS= 3;
	
	public static final int EPKI_MODE = 10;
	public static final int NPKI_MODE = 20;
	public static final int GPKI_MODE = 30;
	
	private static int PKI_VENDER = -1;
	private static int PKI_MODE = -1;
	private static boolean IS_CKKEYPRO_MODULE = false;
	private static String CKKEYPRO_URL = "";
	private static boolean IS_CKWEBPRO_MODULE = false;
	private static String CKWEBPRO_URL = "";
	private static boolean IS_XECUREWEB_MODULE = false;	
	private static boolean IS_PKILOGIN_MODULE = false;
	private static boolean IS_EXPRESS_E_MODULE = false;
	private static boolean IS_KERIS_MODULE = false;
	private static Map<String, String> extSettingMap = null;
	
	public static void setExtPartEnv(Map<String, String> extSettingMap){
		ExtPartConstants.extSettingMap = extSettingMap;
		
		IS_PKILOGIN_MODULE = "PKI".equalsIgnoreCase(extSettingMap.get("login.mode"));
		IS_CKKEYPRO_MODULE = Boolean.parseBoolean(extSettingMap.get("softforum.ckkeypro.use"));
		IS_CKWEBPRO_MODULE = Boolean.parseBoolean(extSettingMap.get("softforum.ckwebpro.use"));
		IS_XECUREWEB_MODULE = Boolean.parseBoolean(extSettingMap.get("softforum.xecureweb.use"));
		if("softforum".equalsIgnoreCase(extSettingMap.get("pki.module.vender"))){
			PKI_VENDER = VENDER_SOFTFORUM;
		} else if("initech_v7".equalsIgnoreCase(extSettingMap.get("pki.module.vender"))){
			PKI_VENDER = VENDER_INITECH_V7;
		} else if("keris".equalsIgnoreCase(extSettingMap.get("pki.module.vender"))){
			PKI_VENDER = VENDER_KERIS;
			IS_KERIS_MODULE = true;
		}
		
		if("EPKI".equalsIgnoreCase(extSettingMap.get("pki.type"))){
			PKI_MODE = EPKI_MODE;
		} else if("NPKI".equalsIgnoreCase(extSettingMap.get("pki.type"))){
			PKI_MODE = NPKI_MODE;
		}  else if("GPKI".equalsIgnoreCase(extSettingMap.get("pki.type"))){
			PKI_MODE = GPKI_MODE;
		}
		
		CKKEYPRO_URL = extSettingMap.get("softforum.ckkeypro.url");
		CKWEBPRO_URL = extSettingMap.get("softforum.ckwebpro.url");
		
		IS_EXPRESS_E_MODULE = EnvConstants.S_MAIL_SOFTFORUM.equalsIgnoreCase(
				EnvConstants.getUtilSetting("secure.module"));
	}
	
	public static String getExtPartConfig(String key){
		if(extSettingMap != null){
			return (extSettingMap.containsKey(key))?extSettingMap.get(key):"";
		} else {
			return "";
		}
	}
	
	public static boolean isCkKeyProUse(){
		return IS_CKKEYPRO_MODULE;
	}
	public static String getCkKeyProUrl(){
		return CKKEYPRO_URL;
	}
	
	public static boolean isCkWebProUse(){
		return IS_CKWEBPRO_MODULE;
	}
	public static String getCkWebProUrl(){
		return CKWEBPRO_URL;
	}
	
	public static boolean isXecureWebUse(){
		return IS_XECUREWEB_MODULE;
	}
	
	public static boolean isPkiLoginUse(){
		return IS_PKILOGIN_MODULE;
	}
	
	public static int getPKIVender(){
		return PKI_VENDER;
	}
	
	public static String getPKIVenderStr(){
		 return (PKI_VENDER > 0)?extSettingMap.get("pki.module.vender"):"none";
	}
	
	public static int getPKIMode(){
		return PKI_MODE;
	}
	
	public static String getPKIModeStr(){
		 return (PKI_MODE > 0)?extSettingMap.get("pki.type"):"none";
	}
	
	public static boolean isXecureExpressE(){
		return IS_EXPRESS_E_MODULE;
	}
	
	public static boolean isKerisUse(){
		return IS_KERIS_MODULE;
	}
	
	
}
