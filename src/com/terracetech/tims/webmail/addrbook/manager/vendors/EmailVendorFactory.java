package com.terracetech.tims.webmail.addrbook.manager.vendors;

import com.terracetech.tims.common.I18nResources;

public class EmailVendorFactory {

	/**
	 * 
	 * @param resource
	 * @param encoding
	 * @param vendorSeq
	 * 		1 : TMS
	 * 		2 : Outlook Express
	 * 		3 : Outlook
	 * 		4 : Thunderbird
	 * @return
	 */
	public static IEmailVendor getEmailVendor(I18nResources resource, String encoding, int vendorSeq){
		IEmailVendor vendorClass = new OutlookExpressForEN(resource);
		if(vendorSeq==0){
			vendorClass = new OutlookExpressForEN(resource);	
		}if(vendorSeq==1){
			vendorClass = new TerraceMail(resource);	
		}else if(vendorSeq==2){
			if("shift-jis".equals(encoding)){
				vendorClass = new OutlookExpressForJP(resource);	
			}else if("euc-kr".equals(encoding)){
				vendorClass = new OutlookExpressForKO(resource);	
			}else{
				vendorClass = new OutlookExpressForEN(resource);
			}
		}else if(vendorSeq==3){
			if("shift-jis".equals(encoding)){
				vendorClass = new OutlookForJP(resource);	
			}else if("euc-kr".equals(encoding)){
				vendorClass = new OutlookForKO(resource);	
			}else{
				vendorClass = new OutlookForEN(resource);
			}
		}else if(vendorSeq==4){
			if("shift-jis".equals(encoding)){
				vendorClass = new MozillaForJP(resource);	
			}else if("euc-kr".equals(encoding)){
				vendorClass = new MozillaForKO(resource);	
			}else{
				vendorClass = new MozillaForEN(resource);
			}
		}
		
		return vendorClass;
	}
}
