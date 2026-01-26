///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// EPKI Center EPKI Client(EPKIWCtl / npEPKIPI) Toolkit Common Script v1.0
//
// Version | Date | Comment
// v1.1 | 2009.08.31 | Ubikey 휴대폰 인증서 서비스 관련 설정 추가
// v1.2 | 2010.01.15 | 멀티브라우저 지원을 위한 관련 설정 수정
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

var USER_OS;
var USER_BROWSER;

var MICROSOFT_WINDOWS = 0;
var UNSUPPORTED_OS = 1;

var MICROSOFT_IE = 10;
var MOZILLA_FF3 = 11;
var APPLE_SAFARI = 12;
var GOOGLE_CHROME = 13;
var OPERASOFTWARE_OPERA = 14;
var UNSUPPORTED_FF = 20;
var UNSUPPORTED_SAFARI = 21;
var UNSUPPORTED_BROWSER = 22;

// EPKI Client Toolkit Version
var EPKIWCTL_VERSION = "1,0,7,1";

// EPKI Client Toolkit 설치파일 경로
var EPKIWCTL_URL = "http://"+this.location.href+"/KerisEpki/EPKIWCtl.cab";
var EPKIWCTL_EXE_URL = "http://"+this.location.href+"/KerisEpki/EPKIWCtl.exe";

// EPKI Client Toolkit Install & Check Function

// 사용자의 운영체제를 확인합니다.
function DetectedOS()
{
	var strUserAgent;
	var strPatternWindows = /windows/i;

	strUserAgent = navigator.userAgent.toLowerCase();
	alert(strUserAgent);
	if(strPatternWindows.test(strUserAgent) == true)
	{
		return MICROSOFT_WINDOWS;
	}
	else
	{
		return UNSUPPORTED_OS;
	}
}

// 사용자의 브라우저를 확인합니다.
function DetectedBrowser()
{
	var strAppName = navigator.appName.toLowerCase();
	if(strAppName == "netscape")
	{
		var strUserAgent;
		var strPatternFireFox = /firefox/i;
		var strPatternFireFox3 = /firefox\/3/i;
		var strPatternAppleWebKit = /safari/i;
		var strPatternChrome = /chrome/i;
		var strPatternSafari4 = /Version\/4/i;
		
		strUserAgent = navigator.userAgent.toLowerCase();
		
		if(strPatternFireFox.test(strUserAgent) == true)
		{
			if(strPatternFireFox3.test(strUserAgent) == true)
			{
				return MOZILLA_FF3;
			}
			else
			{
				return UNSUPPORTED_FF;
			}
		}
		else if(strPatternAppleWebKit.test(strUserAgent) == true)
		{
			if(strPatternChrome.test(strUserAgent) == true)
			{
				return GOOGLE_CHROME;
			}
			else if(strPatternSafari4.test(strUserAgent) == true)
			{
				return APPLE_SAFARI;
			}
			else
			{
				return UNSUPPORTED_SAFARI;
			}
		}
		else
		{
			return UNSUPPORTED_BROWSER;
		}
	}
	else if(strAppName == "opera")
	{
		return OPERASOFTWARE_OPERA;
	}
	else
	{
		return MICROSOFT_IE;
	}		
}

// EPKIWCtl의 정상 설치 유무를 확인합니다.
function CheckObjEWC()
{
	if(typeof(document.ObjEWC) == "undefined" || document.ObjEWC == null || document.ObjEWC.object == null)
	{
		return false;
	}
	else
	{
		return true;
	}
}

// npEPKIPI의 정상 설치 유무를 확인합니다.
function CheckObjEPI()
{
	var EPI_VERSION = EPKIWCTL_VERSION;

	try
	{
		var strEPKIPIMime = navigator.mimeTypes["application/epkipi-plugin"];
		var strDescription = strEPKIPIMime.enabledPlugin.description;
	}
	catch(ex)
	{
		return false;
	}

	var index = strDescription.indexOf('v.', 0);
	if(index < 0)
	{
		return false;
	}
	strDescription += ' ';

	var strVersion = strDescription.substring(index+2, strDescription.length);
	var arrayVersionInfo = strVersion.split('.');
	var thisMajor = parseInt(arrayVersionInfo[0], 10);
	var thisMinor = parseInt(arrayVersionInfo[1], 10);
	var thisRelease = parseInt(arrayVersionInfo[2], 10);
	var thisTest = parseInt(arrayVersionInfo[3], 10);
	
	index = EPI_VERSION.indexOf('v.', 0);
	EPI_VERSION += ' ';
	
	var strCurrentVersion = EPI_VERSION.substring(index+1, EPI_VERSION.length);
	var arrayCurrentVersionInfo = strCurrentVersion.split('.');
	var versionMaj = parseInt(arrayCurrentVersionInfo[0], 10);
	var versionMin = parseInt(arrayCurrentVersionInfo[1], 10);
	var versionRel = parseInt(arrayCurrentVersionInfo[2], 10);
	var versionTel = parseInt(arrayCurrentVersionInfo[3], 10);

	if(thisMajor > versionMaj)
	{
		return true;
	}
	if(thisMajor < versionMaj)
	{
		return false;
	}
	if(thisMinor > versionMin)
	{
		return true;
	}
	if(thisMinor < versionMin)
	{
		return false;
	}
	if(thisRelease > versionRel)
	{
		return true;
	}
	if(thisRelease < versionRel)
	{
		return false;
	}
	if(thisTest > versionTel)
	{
		return true;
	}
	if(thisTest < versionTel)
	{
		return false;
	}

	return true;
}

// EPKI Client Toolkit 을 사용하기 위한 Object 를 설정합니다.
function SetupObjECT(objCheckmode)
{
	// 사용자의 환경을 획득합니다.
	USER_OS = DetectedOS();
	USER_BROWSER = DetectedBrowser();
	
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			// EPKIWCtl Object 설정
			document.writeln("<OBJECT");
			document.writeln("classid='clsid:E8631F4B-4A37-4E60-901C-03634D824B56'");
			document.writeln("CODEBASE='" + EPKIWCTL_URL + "#version=" + EPKIWCTL_VERSION + "'");
			document.writeln("id='ObjEWC'>");
			document.writeln("</OBJECT>");
			
			if(objCheckmode == true)
			{
				// 컨트롤이 설치 되지 않은 경우 설치 페이지로 이동합니다.
				if(CheckObjEWC() == true)
				{
					InitObjECT();
				}

				if(CheckObjEWC() == false)
				{
					location.href=this.location.href+"/KerisEpki/InstallProc.html";
				}
			}
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			// EPKIPI Object 설정
			if(CheckObjEPI() == true)
			{
				document.writeln("<EMBED ID='ObjEPI' TYPE='application/epkipi-plugin' hidden='true'></EMBED>");
				InitObjECT();
			}
			else
			{
				// EPKIPI 플러그인이 최신 버전이 아닌 경우 설치 페이지로 이동합니다.
				location.href=this.location.href+"/KerisEpki/InstallProc.html";
			}
		}
		else
		{
			alert("고객님이 사용 중이신 브라우저는 EPKI에서 지원하지 않습니다. 양해 부탁 드립니다.\nEPKI Client Toolkit은 Microsoft Internet Explorer(6.0 이상), Firefox(3.0 이상), Safari(4.0 이상), Google 크롬(3.0 이상), Opera(9.0 이상)를 지원합니다.");
		}
	}
	else
	{
		alert("고객님이 사용 중이신 운영체제는 EPKI에서 지원하지 않습니다. 양해 부탁 드립니다.\nEPKI Client Toolkit은 Microsoft Windows와 Linux (x86 / x64)만을 지원합니다.");
	}
}

function SetupObjError()
{
	alert("EPKI Client Toolkit이 지원하지 않는 운영체제 혹은 브라우져를\n사용 중이시거나, Object 선언이 올바르지 않습니다.\n\n관리자에게 문의하여 주시기 바랍니다.");
	return;
}

// EPKI Client Toolkit 에서 사용하기 위한 각종 속성값을 설정합니다.
function InitObjECT()
{
	/* 본 속성 설정 관련 함수는 필요에 따라 주석을 풀거나, 설정하여 적절히 조합한 후 사용하거나, 필요 속성별로 별도의 javascript 함수로 변경하여 사용하는 것을 권장합니다. */	
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			/* 인증서 선택창 필터링 관련 속성 설정 */
			// 인증서의 DN을 통해 선택 가능 인증서를 제한하고자 하는 경우 사용하며, 입력값이 복수인 경우 '$'를 구분자로 처리합니다.
			//document.ObjEWC.SetProperty("Filter_IDN", "O=Government of Korea,C=KR"); // 예) EPKI, GPKI 발급 인증서만 선택 가능
			
			// 인증서의 IssueDN과 Serial을 통해 선택 가능 인증서를 제한하고자 하는 경우 사용하며, 입력값이 복수인 경우 '$'를 구분자로 처리합니다.
			//document.ObjEWC.SetProperty("Filter_IDNAndSN", "CN=CA134040001,OU=GPKI,O=Government of Korea,C=KR|9896d7"); // 예) 발급자가 EPKI이며, 인증서의 Serial이 009896d7인 인증서만 선택 가능 하도록 처리합니다. IDN과 SN의 구분자는 '|'
			
			// 인증서의 발급 기관을 통하여 선택 가능 인증서를 제한하고자 하는 경우 사용하며, 입력값이 복수인 경우 '$'를 구분자로 처리합니다.
			//document.ObjEWC.SetProperty("Filter_CAType", "EPKI_GENERAL$EPKI_OFFICIAL$YESSIGN"); // 예) EPKI 개인용, 기관용 인증서와 금융결재원 인증서만이 선택 가능하도록 제한
	
			// 인증서의 OID를 통하여 선택 가능 인증서를 제한하고자 하는 경우 사용하며, 입력값이 복수인 경우 '$'를 구분자로 처리합니다.
			//document.ObjEWC.SetProperty("Filter_OID", "1.2.410.100001.5.3.1.3"); // 예) 인증서 OID가 1.2.410.100001.5.3.1.3에 해당하는 경우에만 처리하도록 설정

			// 인증서의 기본 저장소를 제한하고자 하는 경우 사용하며, 이 속성을 지정하지 않으면 인증서 저장소를 선택할 수 있도록 처리합니다.
			//document.ObjEWC.SetProperty("Filter_Storage", "HDD_DISK"); // 예) 인증서 저장소가 하드 디스크에 해당하는 경우에만 처리하도록 설정
			
			/* 보안 서버, Envelop 메세지 수신 대상자의 암호화용 인증서 검증 옵션 설정 : 기본값은 모두 ON으로 설정 */
			//document.ObjEWC.SetProperty("CertValid_Path", "OFF"); // 인증서 경로 검증 여부 지정
			//document.ObjEWC.SetProperty("CertValid_Time", "OFF"); // 인증서 유효기간 점증 여부 지정
			//document.ObjEWC.SetProperty("CertValid_CRL", "OFF"); // 인증서 폐지 목록 검증 지정
	
			/* 인증서 선택창에 표시되는 사용자 인증서의 검증 옵션 지정 : 기본값은 모두 ON으로 설정 */
			//document.ObjEWC.SetProperty("CertValidClient_Path", "OFF"); // 인증서 경로 검증 여부 지정
			//document.ObjEWC.SetProperty("CertValidClient_Time", "OFF"); // 인증서 유효기간 점증 여부 지정
			//document.ObjEWC.SetProperty("CertValidClient_CRL", "OFF"); // 인증서 폐지 목록 검증 지정
			
			/* 보안 서버의 암호화용 인증서의 웹 도메인 정보의 연관성 검증 여부를 지정합니다. : 기본값은 OFF로 설정*/
			//document.ObjEWC.SetProperty("VerifyDomain", "ON");

			/* 인증서 선택창에서 비밀번호 입력 오류시 허용 횟수를 제한하고자 하는 경우 사용하며, 지정한 횟수를 초과하면 인증서 선택창이 자동 종료됩니다. : 기본값은 0으로 설정(제한 없음) */
			//document.ObjEWC.SetProperty("PWDFail_Limit", "3");

			/* 문자열 처리시 인코딩 형식을 지정합니다. */
			//document.ObjEWC.SetProperty("CHAR_SET", "UTF-8");

			/* 인증서 선택창에 표시되는 사용자 인증서에 유효기간 만료일에 대한 툴팁을 표시하고자 하는 경우 사용하며, 표시할 범위의 남은 날짜를 설정합니다. : 기본값은 30으로 설정*/
			//document.ObjEWC.SetProperty("ExpirationTime", "365");

			/* 보안 서버 연결시 연결된 정보를 툴팁으로 표시합니다. 입력값은 초단위로 설정되며, 이 속성을 지정하지 않으며 툴팁을 표시하지 않습니다. */
			//document.ObjEWC.SetProperty("ConnectedInfo", "10");

			/* Ubikey 휴대폰 인증서 서비스를 활성화합니다. 서비스 이용을 위한 데이터를 '&' 구분자를 통해 연결하여 설정합니다. */
			//document.ObjEWC.SetProperty("Set_Ubikey", UbikeyServiceData);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			//document.getElementById('ObjEPI').SetProperty("Filter_IDN", "O=Government of Korea,C=KR");
			//document.getElementById('ObjEPI').SetProperty("Filter_IDNAndSN", "CN=CA134040001,OU=GPKI,O=Government of Korea,C=KR|9896d7");
			//document.getElementById('ObjEPI').SetProperty("Filter_CAType", "EPKI_GENERAL$YESSIGN");
			//document.getElementById('ObjEPI').SetProperty("Filter_OID", "1.2.410.100001.5.3.1.3");
			//document.getElementById('ObjEPI').SetProperty("Filter_Storage", "HDD_DISK");

			//document.getElementById('ObjEPI').SetProperty("CertValid_Path", "OFF");
			//document.getElementById('ObjEPI').SetProperty("CertValid_Time", "OFF");
			//document.getElementById('ObjEPI').SetProperty("CertValid_CRL", "OFF");
			
			//document.getElementById('ObjEPI').SetProperty("CertValidClient_Path", "OFF");
			//document.getElementById('ObjEPI').SetProperty("CertValidClient_Time", "OFF");
			//document.getElementById('ObjEPI').SetProperty("CertValidClient_CRL", "OFF");
							
			//document.getElementById('ObjEPI').SetProperty("VerifyDomain", "ON");
			//document.getElementById('ObjEPI').SetProperty("PWDFail_Limit", "3");
			//document.getElementById('ObjEPI').SetProperty("CHAR_SET", "UTF-8");
			//document.getElementById('ObjEPI').SetProperty("ExpirationTime", "365");
			//document.getElementById('ObjEPI').SetProperty("ConnectedInfo", "10");

			//document.getElementById('ObjEPI').SetProperty("Set_Ubikey", UbikeyServiceData);
		}
	}
	
	return;
}

// 에러 정보를 출력합니다.
// 사용 환경에 따라, 적절히 변경하여 사용합니다.
function ECTErrorInfo()
{
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			alert("EPKIWCtl 모듈에서 오류가 발생하였습니다.\n오류정보 : [" + document.ObjEWC.GetErrorNum() + "] - " + document.ObjEWC.GetErrorMsg());
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			try
			{
				alert("EPKIPI 플러그인에서 오류가 발생하였습니다.\n오류정보 : [" + document.getElementById('ObjEPI').GetErrorNum() + "] - " + document.getElementById('ObjEPI').GetErrorMsg());
			}
			catch(ex)
			{
				alert("EPKIPI 플러그인에서 예외가 발생하였습니다.\n" + ex);
			}
		}
	}
}

// 원문에 대해 전사서명을 수행합니다.
// 전자서명 수행 시 v1.0에서는 inDataFlag, algID가 확장성을 위해서만 존재하는 파라미터이며, 일반적으로 다음과 같이 입력하여 사용합니다.
// 예) Sign(1, "", theForm.OrgData.value);
function Sign(inDataFlag, algID, plainData)
{
	var strSignedData = "";
	
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			strSignedData = document.ObjEWC.SignData(inDataFlag, algID, plainData);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			strSignedData = document.getElementById('ObjEPI').SignData(inDataFlag, algID, plainData);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return strSignedData;
}

// 전자 서명 메시지에 대한 검증을 수행하고, 서명에 사용된 원문 메시지를 반환합니다.
// plainData는 서명 데이터에 원문이 포함되지 않은 경우에만 입력합니다.
function Verify(inDataFlag, signedData, plainData)
{
	var strVerifyData = "";

	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			strVerifyData = document.ObjEWC.VerifyData(inDataFlag, signedData, plainData);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			strVerifyData = document.getElementById('ObjEPI').VerifyData(inDataFlag, signedData, plainData);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return strVerifyData;
}

// 선택한 알고리즘에 적합한 임의의 대칭키를 생성합니다.
function GenSymmetricKey(algID)
{
	var strSymKey = "";
	
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			strSymKey = document.ObjEWC.GenSymKey(algID);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			strSymKey = document.getElementById('ObjEPI').GenSymKey(algID);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return strSymKey;
}

// 대칭키 암호화를 수행합니다.
function Encrypt(algID, symKey, data)
{
	var strEncryptedData = "";
	
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			strEncryptedData = document.ObjEWC.EncryptData(algID, symKey, data);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			strEncryptedData = document.getElementById('ObjEPI').EncryptData(algID, symKey, data);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return strEncryptedData;
}

// 대칭키 복호화를 수행합니다.
function Decrypt(algID, symKey, data)
{
	var strDecryptedData = "";
	
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			strDecryptedData = document.ObjEWC.DecryptData(algID, symKey, data);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			strDecryptedData = document.getElementById('ObjEPI').DecryptData(algID, symKey, data);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return strDecryptedData;
}

// 비대칭키 암호화를 수행합니다.
function Envelop(algID, recipientCerts, data)
{
	var strEnvelopedData = "";
	
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			strEnvelopedData = document.ObjEWC.EnvelopData(algID, recipientCerts, data);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			strEnvelopedData = document.getElementById('ObjEPI').EnvelopData(algID, recipientCerts, data);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return strEnvelopedData;
}

// 비대칭키 복호화를 수행합니다.
function Develop(strEnvelopedData)
{
	var strDevelopedData = "";
	
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			strDevelopedData = document.ObjEWC.DevelopData(strEnvelopedData);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			strDevelopedData = document.getElementById('ObjEPI').DevelopData(strEnvelopedData);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return strDevelopedData;
}

// 인증서 기반 사용자 인증 및 클라리언트와 서버 간 보안 채널 요청 데이터를 생성합니다.
function RequestSession(serverCert, algID, sessionID)
{
	var strRequestData = "";
	
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			// 서버 kmCert 설정(Base64 인코딩 문자열)
			ObjEWC.SetProperty("ServerCert", serverCert);
			strRequestData = document.ObjEWC.RequestSession(algID, sessionID);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			// 서버 kmCert 설정(Base64 인코딩 문자열)
			document.getElementById('ObjEPI').SetProperty("ServerCert", serverCert);
			strRequestData = document.getElementById('ObjEPI').RequestSession(algID, sessionID);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return strRequestData;
}

// 보안 채널이 형성된 후에 공유된 세션키를 사용하여 대칭키 암호화를 수행합니다.
function SessionEncrypt(sessionID, data)
{
	var strSessionEncryptData = "";
	
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			strSessionEncryptData = document.ObjEWC.SessionEncrypt(sessionID, data);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			strSessionEncryptData = document.getElementById('ObjEPI').SessionEncrypt(sessionID, data);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return strSessionEncryptData;
}

// 보안 채널이 형성된 후에 공유된 세션키를 사용하여 대칭키 복호화를 수행합니다.
function SessionDecrypt(sessionID, encData)
{
	var strSessionDecryptData = "";
	
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			strSessionDecryptData = document.ObjEWC.SessionDecrypt(sessionID, encData);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			strSessionDecryptData = document.getElementById('ObjEPI').SessionDecrypt(sessionID, encData);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return strSessionDecryptData;
}

// 보안 채널이 형성된 후, 내부적으로 관리되는 보안 세션 정보(Key, IV 등)를 안전하게 삭제합니다.
function DestroySession(sessionID)
{
	var nResult;
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			nResult = document.ObjEWC.DestroySession(sessionID);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			nResult = document.getElementById('ObjEPI').DestroySession(sessionID);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return nResult;
}

// 신원확인 정보를 이용한 인증서 기반, 사용자 인증을 수행합니다.
function RequestVerifyVID(serverCert, userID)
{
	var strRequestData = "";
	
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
		    // 서버 kmCert 설정(Base64 인코딩 문자열)
			ObjEWC.SetProperty("ServerCert", serverCert);
			strRequestData = document.ObjEWC.RequestVerifyVID(userID);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			// 서버 kmCert 설정(Base64 인코딩 문자열)
			document.getElementById('ObjEPI').SetProperty("ServerCert", serverCert);
			strRequestData = document.getElementById('ObjEPI').RequestVerifyVID(userID);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return strRequestData;
}

// EPKI Client Toolkit 에 설정된 속성값을 반환합니다.
function GetProperty(field)
{
	var nResult;
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			nResult = document.ObjEWC.GetProperty(field);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			nResult = document.getElementById('ObjEPI').GetProperty(field);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return nResult;
}

// 전달 받은 메시지에 전자 서명한 인증서의 정보를 추출합니다.
// 전자 서명 인증서는 전달 받은 메시지를 검증(Verify) 후에 GetProperty 함수에서 SignerCert 파라미터를 통해 얻을 수 있습니다.
function GetCertInfo(signerCert, certfield)
{
	var nResult;
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			nResult = document.ObjEWC.GetCertInfo(signerCert, certfield);
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			nResult = document.getElementById('ObjEPI').GetCertInfo(signerCert, certfield);
		}
	}
	else
	{
		SetupObjError();
	}
	
	return nResult;
}

// 인증서 관리자를 실행합니다.
function InvokeCMDlg()
{
	if(USER_OS == MICROSOFT_WINDOWS)
	{
		if(USER_BROWSER == MICROSOFT_IE)
		{
			document.ObjEWC.InvokeCMDlg();
		}
		else if(USER_BROWSER == MOZILLA_FF3 || USER_BROWSER == APPLE_SAFARI || USER_BROWSER == GOOGLE_CHROME || USER_BROWSER == OPERASOFTWARE_OPERA)
		{
			document.getElementById('ObjEPI').InvokeCMDlg();
		}
	}
	else
	{
		SetupObjError();
	}

	return;
}
