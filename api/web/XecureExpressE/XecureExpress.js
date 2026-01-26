
function loadExpressE(id){
	var ocxTag = '<object id="MAIL" '+
		'classid="CLSID:F564BD75-62F5-4400-BBC7-063794EF126C" '+
		'codebase="http://'+window.location.host+'/XecureExpressE/v2607/XecureOutMail_Install.cab#Version=2,6,0,7">'+
		'</object>';
	document.getElementById(id).innerHTML = ocxTag;
}

function GetDate()
{
	WeekAbbr = new Array("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");
	MonthAbbr = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun",
						   "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");


    date = new Date();

    Hour =  date.getHours();
    if (Hour < 10) Hour = '0' + Hour;

    Minute =  date.getMinutes();
    if (Minute < 10) Minute = '0' + Minute;

    Second =  date.getSeconds();
    if (Second < 10) Second = '0' + Second;

    DateStr = WeekAbbr[date.getDay()] + ', ' + date.getDate() + ' '
			  + MonthAbbr[date.getMonth()] + ' ' + date.getYear() + ' '
			  + Hour+ ':' + Minute + ':' + Second;

	tzHour = -date.getTimezoneOffset()/60;

    if (tzHour >= 10) {
        tzHour = '+' + tzHour;
    } else if (tzHour >= 0 && tzHour < 10) {
    	tzHour = '+0' + tzHour;
    } else if (tzHour < 0 && tzHour > -10) {
	    tzHour = '-0' + (-tzHour);
	}

    tzMinute = date.getTimezoneOffset()%60;

    if (tzMinute < 0) tzMinute = -tzMinute;

    if (tzMinute >= 0 && tzMinute < 10) {
        tzMinute = '0' + tzMinute;
    }

    DateStr += ' ' + tzHour + tzMinute + ' (KST)';

	return DateStr;
 }

function countLine(content)
{
	var count = 0;
	var index_total = 0;

	index = content.indexOf('\n');
	while(index > -1) {
		count++;
		index_total += (index + 1);
		index = content.substring(index_total).indexOf('\n');
	}
	return count * 22 + 100;
}

function makeDNValue(dnList){
	var listStr = "";
	if(dnList && dnList.length > 0){
		for ( var i = 0; i < dnList.length; i++) {
			if(i > 0){
				listStr += ";";
			}
			listStr += dnList[i];
		}
	} 
	
	return listStr;
}

function createSecureMail (secureMailInfo)
{    //1. create mail space
	var MAIL = document.uploadForm.MAIL;
	MAIL.CreateMailSpace();

	// 2. set error message locale
    MAIL.ErrMsgLocale = 1;	// 1 : korean
    						// 2 : english
	// 라이센스 적용
	//XecureWeb Security Option Signer에서   127.0.0.1:signature,sym_encryption,asym_encryption
    var license = "30820665020101310b300906052b0e03021a0500304106092a864886f70d010701a03404323132372e302e302e313a7369676e61747572652c73796d5f656e6372797074696f6e2c6173796d5f656e6372797074696f6ea0820467308204633082034ba003020102020107300d06092a864886f70d01010505003077310b3009060355040613024b52311e301c060355040a1315536f6674666f72756d20436f72706f726174696f6e3121301f06035504031318536f6674666f72756d20526f6f7420417574686f726974793125302306092a864886f70d010901161663616d617374657240736f6674666f72756d2e636f6d301e170d3034303431393030303030305a170d3333303131333030303030305a308192310b3009060355040613024b52311e301c060355040a1315536f6674666f72756d20436f72706f726174696f6e311e301c060355040b1315536563757269747920524e44204469766973696f6e311c301a06035504031313536f6674666f72756d205075626c69632043413125302306092a864886f70d010901161663616d617374657240736f6674666f72756d2e636f6d30820121300d06092a864886f70d01010105000382010e00308201090282010043340b4e1f2f30d6634c818e9fa4b35c199e0628503dbe0d1f5ad2c05890a918408dc330c991083bc7cdfc50021303c04afab4cb522d22fced11d1be6559835f1f000d466120cff97a2a80e4fdf972ac127f9bb8e8ddb84974323e4cb822c5f15b22f82da3de6ef61a0b6798ca49a85af3d8f8298912b4d26411e2e1635c081a3306931716c5e56b279c4d36068a4b645c10aa582693086e14132ba67fb03526312790261f9c641993e2ffc3fd9e8df3efebfddecd722e874d6366ad1252ac0d8bddb5674533cc2717a7342e5cfb18f8a301e7196ca33d6c3bb7e1f1e4bee34f5358af6ae0fd52a9fc3bdd4925f5eab7db6628e24738f6c882bb0aaa0e10afbf0203010001a381de3081db301f0603551d2304183016801409b5e27e7d2ac24a8f56bb67accebb93f5318fd3301d0603551d0e041604142e49ab278ae8c8af977537de8b74bb240e0d275f300e0603551d0f0101ff04040302010630120603551d130101ff040830060101ff02010030750603551d1f046e306c306aa068a06686646c6461703a2f2f6c6461702e736f6674666f72756d2e636f6d3a3338392f434e3d58656375726543524c505542432c4f553d536563757269747920524e44204469766973696f6e2c4f3d536f6674666f72756d20436f72706f726174696f6e2c433d4b52300d06092a864886f70d010105050003820101003ce700a0492b225b1665d9c73d84c34f7a5faad7b397ed49231f030e4e0e91953a607bd9006425373d490ef3ba1cf47810ca8c22fabe0c609f93823efdede64744458e910267f9f857c907318e286da6c131c9dd5fada43fd8cfdf6bd1b1b239338cea83eb6b6893b88fbcfd8e86a677b7270ad96be5a82b40569efc2dda6df4bcd642d067183186d6cace6c8f73b80f30b57acb3bcd5cbbc51307922d5edb38cb0d90c3917a8e37534183ba10f403c1c034287f39442df795050f39d78ddad97da8a43f02d7641549af9b5d68908e49faa8a1597cfed4a43baadd42c8fe4fd44c96d314df56147b8a7fa6ba65ffdee9ed3a5da52ef9ac7f9ca5afb633e1ccdf318201a33082019f020101307c3077310b3009060355040613024b52311e301c060355040a1315536f6674666f72756d20436f72706f726174696f6e3121301f06035504031318536f6674666f72756d20526f6f7420417574686f726974793125302306092a864886f70d010901161663616d617374657240736f6674666f72756d2e636f6d020107300906052b0e03021a0500300d06092a864886f70d01010105000482010025d7ecdeccdeafde8706c964b16746e11113e091906284a16298cb838bfd2ca9590d0355d75887cea703b234c5458c31c1fbacd42496b9853632f034abd354d57db653a4d575206c6ed82575c0120f4fa139680b8931ca244efbdfa36d85a8d07c9fc460cf57328e4b602baae5db5870b1751fe42d3a134da8b1a51d21614f0c216496a74d60d613bfa698be0c8a068490d7a866f781efddca78ae878ce6c957e44dcd078864be9b0228bbaf7cabbdd2ef74fb10d4185f843b86c74d7af7f91c5199162dc95203437d9f1b941e197ae8a6758e000129836bcad9f677fb0d5278bb1a0cfeb7b47a87dd0f16300b92db3622bad85223ad7517a08735fce631f3b2";
    MAIL.CheckLicense = license;
 	if ( MAIL.ErrNo < 0 )
    {
		alert("CheckLicense : " + MAIL.ErrMsg + "[" + MAIL.ErrNo + "]" );
		MAIL.DeleteMailMessage();
   		return false;
   	}
    
    // 3. download template
    templateUrl = "http://"+window.location.host+"/XecureExpressE/template/template.html";
    version = "2.0";
    MAIL.downloadTemplate( templateUrl, version );
    if ( MAIL.ErrNo < 0 )
    {
		alert("Download Template : " + MAIL.ErrMsg + "[" + MAIL.ErrNo + "]" );
		MAIL.DeleteMailMessage();
   		return false;
   	}

	// 4. set parameter
	// 4.1 set mail parameter
	MAIL.SecKeyStroke = "XW_SKS_JRSOFT_DRIVER";
	MAIL.From = secureMailInfo.from;
	MAIL.To = secureMailInfo.to;
	MAIL.Cc = secureMailInfo.cc;
	MAIL.Bcc = secureMailInfo.bcc;
	MAIL.Subject = secureMailInfo.subject;
	MAIL.Content = secureMailInfo.contents;
	MAIL.Date = GetDate();
	//MAIL.SetSize.Limit = ""

	for(i=1; i<= attachFileListLength; i++) {
        var formFile = eval("document.uploadForm.attachFile"+i);
        if ( formFile.value != "" ) {
            MAIL.Attach(formFile.value);
        }
    }
	
	if(secureMailInfo.type == "PKI"){
		smimeType = 2;
   		MAIL.SmimeType = 2;
        MAIL.EncAlg = "RSASEED_CBC";
	} else if(secureMailInfo.type == "PASSWD"){
		smimeType = 4;
    	MAIL.SmimeType = 4;
   		MAIL.EncPwd = "";
        MAIL.EncAlg = "SEED_CBC";
	}
		
	
	/*
	// 4.2 set encryption method
	// signd-data
   	if ( form.chkSign.checked && !form.chkEnc.checked) {
   	   	alert("1");
   	    smimeType = 1;
   		MAIL.SmimeType = 1;
        MAIL.SignAlg = form.selSign.value;
   	}
   	// enveloped-data with certificate //인증서방식 암호화
	else if ( !form.chkSign.checked && form.chkEnc.checked && form.rdoEncWithCert.checked) {
		alert("2");
		alert(form.selAsymetric.value);
        smimeType = 2;
   		MAIL.SmimeType = 2;
        MAIL.EncAlg = form.selAsymetric.value;
    }
    // signed-enveloped-data
	else if ( form.chkSign.checked && form.chkEnc.checked && form.rdoEncWithCert.checked) {
		alert("3");
    	smimeType = 3;
    	MAIL.SmimeType = 3;
        MAIL.EncAlg = form.selAsymetric.value;
        MAIL.SignAlg = form.selSign.value;
    }
	// password-enveloped data
	else if ( !form.chkSign.checked && form.chkEnc.checked && form.rdoEncWithPwd.checked) {
		alert("4");
		alert("form.selSymetric.value: " + form.selSymetric.value );
    	smimeType = 4;
    	MAIL.SmimeType = 4;
   		MAIL.EncPwd = form.encpasswd.value;
        MAIL.EncAlg = form.selSymetric.value;
        
    }
    // signed-password-enveloped data
	else if ( form.chkSign.checked && form.chkEnc.checked && form.rdoEncWithPwd.checked) {
		alert("5");
    	smimeType = 5;
    	MAIL.SmimeType = 5;
   		MAIL.EncPwd = form.encpasswd.value;
        MAIL.EncAlg = form.selSymetric.value;
        MAIL.SignAlg = form.selSign.value;
    }
    // id/password-enveloped data
	else if ( !form.chkSign.checked && form.chkEnc.checked && form.rdoEncWithIDPwd.checked) {
		alert("6");
    	smimeType = 6;
    	MAIL.SmimeType = 6;
   		MAIL.EncPwd = form.encpasswd.value;
        MAIL.EncAlg = form.selSymetric.value;
    }
    // signed-id/password-enveloped data
	else if ( form.chkSign.checked && form.chkEnc.checked && form.rdoEncWithIDPwd.checked) {
		alert("1");
    	smimeType = 7;
    	MAIL.SmimeType = 7;
   		MAIL.EncPwd = form.encpasswd.value;
        MAIL.EncAlg = form.selSymetric.value;
        MAIL.SignAlg = form.selSign.value;
    }
    */

    //alert(smimeType );

	// 4.3 set ldap parameter
	//MAIL.DS_IP = "192.168.10.80"; //"moon.signgate.com";
	//MAIL.DS_PORT = 389;
	//MAIL.DS_START = "ou=PKI(CA),o=softforum,c=kr"; //"ou=licensedCA,o=KICA,c=KR";
	//MAIL.UseLdapFilter = 1;
	//MAIL.AcceptableCertDN = "PKI(CA),PKI(CA)RootCert,yessignCA,yessignCA-OCSP,signGATE CA,SignKorea CA";
   	
   	//MAIL.DS_CN = "cn=999에피아이모듈001,ou=people,ou=정부전자문서유통관리센터,o=Government of Korea,c=KR";
	//MAIL.DS_IP = "ldap.gcc.go.kr"; //"moon.signgate.com";
	//==NPKI
	
   
   	//===========================================================================================
	
	//==EPKI
	/*
   	//MAIL.DS_CN = "ID=해당기관 DN(국정원인가 GPKI에서 발급/PW=DN에 대한 pwd/사용자 메일 주소;ID=해당기관 DN(국정원인가 GPKI에서 발급/PW=DN에 대한 pwd/사용자 메일 주소";
	MAIL.DS_CN = "ID=cn=smba4433,ou=Access User,c=KR/PW=spi13579/yuni-ya@hanmail.net;ID=cn=smba4433,ou=Access User,c=KR/PW=spi13579/yuni-ya@hanmail.net";
	MAIL.DS_IP = "ldap.epki.go.kr"; //"moon.signgate.com";

	MAIL.DS_START = "o=Government of Korea,c=KR";
	MAIL.UseLdapFilter = 1;
	MAIL.LdapAccountEnable = 1;
	*/
   	//===========================================================================================
	
	/*
	MAIL.DS_IP = "ldap.gcc.go.kr"; //"moon.signgate.com";
	MAIL.DS_PORT = 389;
	//MAIL.DS_START = "ou=행정안전부,o=Government of Korea,c=KR"; //"ou=licensedCA,o=KICA,c=KR";
	MAIL.DS_START = "o=Government of Korea,c=KR";
	MAIL.UseLdapFilter = 1;
	//MAIL.LdapSearchField = "mail";
	MAIL.LdapAccountEnable = 1;
	//MAIL.DS_CN = "ID=cn=smba4433,ou=Access User,c=KR/PW=spi13579/"+form.From.value+";"+form.To.value+"";
	MAIL.DS_CN = "ID=cn=smba4433,ou=Access User,c=KR/PW=spi13579/yuni-ya@hanmail.net;ID=cn=smba4433,ou=Access User,c=KR/PW=spi13579/yuni-ya@hanmail.net";

	MAIL.AcceptableCertDN = "CA131000002,CA128000002,yessignCA";
*/	

	//MAIL.DS_CN = "cn=최황석()00880402006110788001078,ou=SHB,ou=personal4IB,o=yessign,c=kr";
	//MAIL.DS_IP = "ds.yessign.or.kr"; //"moon.signgate.com";
	//MAIL.DS_PORT = 389;
	//MAIL.DS_START = "ou=행정안전부,o=Government of Korea,c=KR"; //"ou=licensedCA,o=KICA,c=KR";
	//MAIL.DS_START = "ou=SHB,ou=personal4IB,o=yessign,c=kr";
	//MAIL.UseLdapFilter = 1;
	//MAIL.AcceptableCertDN = "yessignCA,CA131000002,CA134040001,CA128000002";
	//MAIL.AcceptableCertDN = "CA131000002";
	//MAIL.AcceptableCertDN = "CA134040001";
	
	
	//MAIL.AcceptableCertDN = "CA128000002"; // 행자부
	//MAIL.AcceptableCertDN = "CA131000002"; // 병무청
	//MAIL.AcceptableCertDN = "PKI(CA),PKI(CA)RootCert,CA131000002,CA128000002,CA131000002Test,Xecure TestCA,SoftforumCA,Softforum Demo CA,Softforum CA 3.0,yessignCA,yessignCA-OCSP,signGATE CA,SignKorea CA,CrossCertCA,CrossCertCA-Test2,NCASign CA,TradeSignCA,yessignCA-TEST,lotto test CA,NCATESTSign,SignGateFTCA,SignKorea Test CA,TestTradeSignCA,Matsushita Group PKI Card CA 2002";// 지원가능 전체 인증서
	
    // 4.4 set readmail control
	MAIL.DS_CN = makeDNValue(secureMailInfo.sendDnList);
	MAIL.DS_IP = "ldap.epki.go.kr";
	MAIL.DS_PORT = 389;
	MAIL.AcceptableCertDN = "";
		
	codebase = "http://"+window.location.host+"/XecureExpressE/xei_install2.cab";//  "http://download.softforum.co.kr/XecureExpressI/xei_install2.cab"; //"http://192.168.10.52:8080/webmail/control/xei_install.cab";
    MAIL.ReadMailCodebase = codebase;
    MAIL.ReadMailVersion  = "2,6,0,9";
    MAIL.ReadMailWidth = 600;
    MAIL.ReadMailHeight = 50;//500
    // option
    // 설정하지 않으면 기본값이 적용됨 XecureExpress-(I).
    MAIL.ReadMailUIDesc = secureMailInfo.title;
    // option
    // 기본값은 타이틀바와 프린트 버튼이 출력이 된다.
    //MAIL.ReadMailUIOpt = "NOTITLE";  // "NOTITLE", "NOPRINT"
    // option
    // MAIL.ReadMailPwdMsg를 설정하지 않으면 기본값이 적용되며, 줄바꿈은 0x0A0x0A 으로 한다.


	// 설정하지 않으면 설치될때 기본으로 적용된 error page가 사용된다.
	MAIL.ReadMailErrorUrl = "http://"+window.location.host+"/XecureExpressE/template/error.html";
	
    switch ( smimeType )
    {
        case 1 : // signed-data
            break;
        case 2 : // enveloped-data
			break;
        case 3 : // signed-enveloped-data
			break;
        case 4 : // password-enveloped-data
        case 5 : // signed-enveloped-data
        case 6 : // id/pw-enveloped-data
        case 7 : // signed-id/pw-enveloped-data
			//alert(form.selPasswd_msg.value);
		//MAIL.ReadMailPwdMsg = document.createmail.selPasswd_msg.value;
		//뒷자리에 공백 들어가지 않도록 주의함.
	  	//MAIL.ReadMailMsgImgUrl = document.createmail.encpasswd_img.value.replace(/\s/g,""); 
	  	
	  	
	  	//alert(document.createmail.encpasswd_img.value);
			break;
        default :
            break;
    }
    // xei banner
    //MAIL.ReadMailBannerVersion = "4.0";
    //MAIL.ReadMailBannerUrl = "http://127.0.0.1:8081/XecureExpressE/image/banner.bmp";

    // 5. download banner
    bannerOpt = 1;  // 0 : not download banner
                    // 1 : download banner
    if ( bannerOpt == 0 )
    {
    	
        bannerUrl = "http://"+window.location.host+"/XecureExpressE/xecure_daegu.bmp";
        bannerversion = "1.9";
        //alert("bannerUrl =" + bannerUrl  );
        //alert("bannerversion : " + bannerversion);
        MAIL.downloadBanner( bannerUrl, bannerversion );
        if ( MAIL.ErrNo < 0 )
        {
    		alert("Download Banner: " + MAIL.ErrMsg + "[" + MAIL.ErrNo + "]" );
    		//MAIL.DeleteMailMessage();
       		//return false;
       	}
    }


    // 6. 수신확인
    /*if ( form.chkConfirm.checked )
    {
    	alert("수신확인");
        MAIL.ReadMailConfirmMode = form.selConfirm.value;   
        								// -1 : 수신확인 메시지 전송하지 않음
    									// 0 : 수신자의 동의 없이 수신확인 메시지 전송
    									// 1 : 수신확인 메시지 전송후 수신자에게 전송사실을 알림
    									// 2 : 수신자가 수신확인 메세지 전송 여부를 결정
        MAIL.ReadMailConfirmUrl = "http://127.0.0.1:8081/XecureExpressE/jsp/confirm.jsp";
    }*/

    // 7. timestamp 서명 추가
    /*if ( form.chkTimeStamp.checked )
    {
        MAIL.TimeStampOpt = 1;  // 0 : not use,
                                // 1 : use
        // for http
        MAIL.TimeStampUrl = "http://127.0.0.1:8081/XecureExpressE/tsacert/XecureTSA.SC.der";
        // for ldap
        //MAIL.TimeStampUrl = "ldap://192.168.10.25";
        //MAIL.TimeStampPort = 389;
        //MAIL.TimeStampDN = "CN=softforum tsa,O=SoftForum,OU=Ca Team,E=tsa@softforum.com,C=KR";

        MAIL.TimeServerIP = "192.168.10.25";
        MAIL.TimeServerPort = 8230;

        tsaVersion = "1.0";
        MAIL.downloadTimeStampCert( tsaVersion );
    }*/
		
    // 8. set option
    MAIL.isHead = 1;		// 0 - html head
    						// 1 - no html head

	// 9. Make Smime Message
    //alert("before MAKEMAIL");
    MAIL.MakeMailMessage();
    //alert("after MAKEMAIL");
    //alert(MAIL.MailMessage);
    // 인증서 가져오기 실패한 수신자 보기
    //if ( ( smimeType == 2 ) || ( smimeType == 3 ) )
    if(MAIL.FailTo.length > 0)
        alert("Error PKI User : \r\n " + MAIL.FailTo );

	// 10. Get Mail Message
   var resultEncMessage = MAIL.MailMessage;
   	//alert("111111111" + form.mail_msg.value);
   	if (MAIL.MailMessage == "")
   	{
		alert("Get Mail Message : " + MAIL.ErrMsg + "[" + MAIL.ErrNo + "]" );
		MAIL.DeleteMailMessage();
   		return false;
	}

	// 보안메일 컨트롤에서 반환한 메시지를 저장하거나 컨트롤이 내부에 가지고 있는 메시지를 사용자 PC 에 저장한다.
	// C:\Program Files\Softforum\XecureOutMail\temp\smime.html
	//	MAIL.StoreSmimeMessage( 	form.mail_msg.value ); 
	//	MAIL.saveSmimeMessage();
	//MAIL.StoreSmimeMessage( form.mail_msg.value );
//	MAIL.StoreSmimeMessage( form.mail_msg.value );
	// 11. set sendmail parameters
//	document.sendmail.from.value = form.From.value;
	//alert("document.sendmail.from.value"+ document.sendmail.from.value);
//	document.sendmail.to.value = form.To.value;
	//alert("document.sendmail.to.value"+ document.sendmail.to.value);
//	document.sendmail.subject.value = form.Subject.value;
	//alert("document.sendmail.subject.value"+ document.sendmail.subject.value);
//	document.sendmail.content.value = form.mail_msg.value;
	//alert("document.sendmail.content.value"+ document.sendmail.content.value);
//	document.sendmail.content_view.value = form.mail_msg.value;

	//alert("document.sendmail.content_view.value"+ document.sendmail.content_view.value);
	MAIL.StoreSmimeMessage( resultEncMessage ); 
	//alert( MAIL.StoreSmimeFilePath );
	// 12. Delete Mail Message
    MAIL.DeleteMailMessage();

	return resultEncMessage;
}// end create mail


function    chkEncryption()
{
    if ( document.createmail.chkEnc.checked )
    {
		document.createmail.rdoEncWithCert.disabled = false;
		document.createmail.rdoEncWithPwd.disabled = false;
		document.createmail.rdoEncWithIDPwd.disabled = false;
        if(document.createmail.rdoEncWithCert.checked) {
        	document.createmail.encpasswd.disabled = true;
        	document.createmail.selPasswd_msg.disabled = true;
        	document.createmail.selSymetric.disabled = true;
        	document.createmail.selAsymetric.disabled = false;
        }
        else {
			document.createmail.encpasswd.disabled = false;
			document.createmail.selPasswd_msg.disabled = false;
			document.createmail.selSymetric.disabled = false;
			document.createmail.selAsymetric.disabled = true;
		}
    }
    else
    {
		document.createmail.rdoEncWithCert.disabled = true;
		document.createmail.rdoEncWithPwd.disabled = true;
		document.createmail.rdoEncWithIDPwd.disabled = true;
   		document.createmail.encpasswd.disabled = true;
   		document.createmail.selPasswd_msg.disabled = true;
   		document.createmail.selSymetric.disabled = true;
   		document.createmail.selAsymetric.disabled = true;
    }
}

function    chkSignature()
{
    if ( document.createmail.chkSign.checked )
    {
		document.createmail.selSign.disabled = false;
    }
    else
    {
		document.createmail.selSign.disabled = true;
    }

}

function    chkConf()
{
    if ( document.createmail.chkConfirm.checked )
    {
		document.createmail.selConfirm.disabled = false;
    }
    else
    {
		document.createmail.selConfirm.disabled = true;
    }

}

function    chkEncOption(radio)
{
    if ( radio.value == "CERT" )
    {
		document.createmail.rdoEncWithPwd.checked = false;
		document.createmail.rdoEncWithIDPwd.checked = false;
		document.createmail.selAsymetric.disabled = false;
		document.createmail.selSymetric.disabled = true;
		document.createmail.selPasswd_msg.disabled = true;
    }
    else if(radio.value == "PWD")
    {
		document.createmail.rdoEncWithCert.checked = false;
		document.createmail.rdoEncWithIDPwd.checked = false;
		document.createmail.selAsymetric.disabled = true;
		document.createmail.selSymetric.disabled = false;
		document.createmail.selPasswd_msg.disabled = false;
    }
    else if(radio.value == "IDPWD")
    {
		document.createmail.rdoEncWithCert.checked = false;
		document.createmail.rdoEncWithPwd.checked = false;
		document.createmail.selAsymetric.disabled = true;
		document.createmail.selSymetric.disabled = false;
		document.createmail.selPasswd_msg.disabled = false;
    }
}
