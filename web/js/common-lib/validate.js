function checkInputSearch(inputObj,minSize,maxSize,isCheckAllowChar){
	var str = jQuery.trim(inputObj.value);
	var len = str_realLength(str);
	
	if(len==0 && minSize==0){
		return true;
	}
	
	if(str == ""){
		alert(comMsg.error_inputtext);
		inputObj.select();
		return false;
	}
	
	if(isCheckAllowChar && incNotAllowSearch(str)){
		alert(comMsg.error_invalidtext+"\n\n"
				+"'%\\");
		inputObj.select();
		return false;
	}
	
	if (minSize == 0 && len > maxSize) {
		alert(msgArgsReplace(comMsg.error_inputlength_over,[maxSize]));
		inputObj.select();
		return false;
	}
	
	if(len < minSize || len > maxSize){
		alert(msgArgsReplace(comMsg.error_inputlength,[minSize,maxSize]));
		inputObj.select();
		return false;
	}
	
	return true;
}

function checkInputSearchAddr(str,minSize,maxSize,isCheckAllowChar){	
	var len = str_realLength(str);
	
	if(len==0 && minSize==0){
		return true;
	}
	
	if(str == ""){
		alert(comMsg.error_inputtext);		
		return false;
	}
	
	if(isCheckAllowChar && incNotAllowSearch(str)){
		alert(comMsg.error_invalidtext+"\n\n"
				+"'%\\");		
		return false;
	}
	
	if (minSize == 0 && len > maxSize) {
		alert(msgArgsReplace(comMsg.error_inputlength_over,[maxSize]));
		inputObj.select();
		return false;
	}
	
	if(len < minSize || len > maxSize){
		alert(msgArgsReplace(comMsg.error_inputlength,[minSize,maxSize]));		;
		return false;
	}
	
	return true;
}


function checkInputText(inputObj,minSize,maxSize,isCheckAllowChar){
	var str = jQuery.trim(inputObj.value);
	var len = str_realLength(str);
	
	if(len==0 && minSize==0){
		return true;
	}
	
	if(str == ""){
		alert(comMsg.error_inputtext);
		inputObj.select();
		return false;
	}
	
	if(isCheckAllowChar && incNotAllowStr(str)){
		alert(comMsg.error_invalidtext+"\n\n"
				+"`'~\"!@#$%^*()+|\\/:;,.?<>=");
		inputObj.select();
		return false;
	}
	
	if (minSize == 0 && len > maxSize) {
		alert(msgArgsReplace(comMsg.error_inputlength_over,[maxSize]));
		inputObj.select();
		return false;
	}
	
	if(len < minSize || len > maxSize){
		alert(msgArgsReplace(comMsg.error_inputlength,[minSize,maxSize]));
		inputObj.select();
		return false;
	}
	
	return true;
}

function checkInputName(inputObj,minSize,maxSize,isCheckAllowChar){
	var str = jQuery.trim(inputObj.value);
	var len = str_realLength(str);
	
	if(len==0 && minSize==0){
		return true;
	}
	
	if(str == ""){
		alert(comMsg.error_inputtext);
		inputObj.select();
		return false;
	}
	
	if(isCheckAllowChar && !isUserName(str)){
		alert(comMsg.error_invalidtext+"\n\n"
				+"\"#$<>,;\\");
		inputObj.select();
		return false;
	}
	
	if (minSize == 0 && len > maxSize) {
		alert(msgArgsReplace(comMsg.error_inputlength_over,[maxSize]));
		inputObj.select();
		return false;
	}
	
	if(len < minSize || len > maxSize){
		alert(msgArgsReplace(comMsg.error_inputlength,[minSize,maxSize]));
		inputObj.select();
		return false;
	}
	
	return true;
}

function checkInputLength(type, inputObj, emptyMsg, minSize, maxSize) {
	var str = "";
	if (type == "jQuery") {
		str = jQuery.trim(inputObj.val());
	} else {
		str = jQuery.trim(inputObj.value);
	}
	var len = str_realLength(str);
	
	if(len==0 && minSize==0){
		return true;
	}
	
	if(str == ""){
		alert(emptyMsg);
		inputObj.select();
		return false;
	}
	
	if (minSize == 0 && len > maxSize) {
		alert(msgArgsReplace(comMsg.error_inputlength_over,[maxSize]));
		inputObj.select();
		return false;
	}
	
	if(len < minSize || len > maxSize){
		alert(msgArgsReplace(comMsg.error_inputlength,[minSize,maxSize]));
		inputObj.select();
		return false;
	}
	
	return true;
}

function checkInputValidate(type, inputObj, funcType){
	
	var str = "";
	var invalidMsg = "";
	var isAllow = false;
	var checkType = false;
	var isFormat = false;
	var errorMsg = comMsg.error_invalidtext;
	if (type == "jQuery") {
		str = jQuery.trim(inputObj.val());
	} else {
		str = jQuery.trim(inputObj.value);
	}
	
	if (str == "") {
		return true;
	}
	
	if (funcType == "password") {
		isAllow = isPass(str);
		invalidMsg = "\\\"\<\>\(\)\[\]\,\;\:\@\|";
	} else if (funcType == "case2") {
		isAllow = !incNotAllowChar2(str);
		invalidMsg = "!@#\$%\?^\*\+\:;<>\=^\|\\";
	} else if (funcType == "case3") {
		isAllow = !incNotAllowChar3(str);
		invalidMsg = "!#\$%\*\+\:;<>\=\?^\\";
	} else if (funcType == "case4") {
		isAllow = !incNotAllowChar4(str);
		invalidMsg = "!#\$%\*\?^\\";
	} else if (funcType == "case5") {
		isAllow = !incNotAllowChar5(str);
		invalidMsg = "\\\"";
	} else if (funcType == "onlyBack") {
		isAllow = !incNotAllowBackslash(str);
		invalidMsg = "\\";
	} else if (funcType == "folderName") {
		isAllow = isFolderName(str);
		invalidMsg = "%,&,*,.,/,\\,',\"";
	} else if (funcType == "id") {
		isAllow = isId(str);
		invalidMsg = "0-9a-zA-Z_\-~!#$*+.\/=\?\{\}\'\^\|\`";
		checkType = true;
	} else if (funcType == "number") {
		isAllow = isNumber(str);
		invalidMsg = "0-9";
		checkType = true;
	} else if (funcType == "mobile") {
		isAllow = isCellPhone(str);
		invalidMsg = "(010|011|016|017|018|019) \n000-000-0000, 000-0000-0000, 0000000000, 00000000000";
		checkType = true;
		isFormat = true;
	} else if (funcType == "phone") {
		isAllow = isPhone(str);
		invalidMsg = "000-000-0000, 000-0000-0000, 0000000000, 00000000000";
		checkType = true;
		isFormat = true;
	} else if (funcType == "addr") {
		isAllow = !incNotAllowAddr(str);
		invalidMsg = "!@#\$%\?^\\";
	} else if (funcType == "empNo") {
		isAllow = isEmpNo(str);
		invalidMsg = "0-9a-zA-Z\-_";
		checkType = true;
	} else if (funcType == "senderName") {
		isAllow = !incNotAllowSenderName(str);
		invalidMsg = "#$%;,@&\\";
	} else if (funcType == "userName") {
		isAllow = isUserName(str);
		invalidMsg = "\"#$<>,;\\";
	} else if (funcType == "shareName") {
		isAllow = !incNotAllowQuotBackslash(str);
		invalidMsg = "\\\"\'";
	}
	
	if (checkType) {
		errorMsg = comMsg.error_validtext;
	}
	
	if (isFormat) {
		errorMsg = comMsg.error_ext;
	}
	
	if(!isAllow){
		alert(errorMsg+"\n\n"+invalidMsg);
		inputObj.select();
		return false;
	}
	
	for (i=0; i<str.length; i++) {
		var charCode = str.charCodeAt(i);
		if ((charCode >= 0 && charCode <= 31) || (charCode == 127)) {
			alert(comMsg.error_control);
			return false;
		}
	}

	return true;
}

function checkInputFolderName(inputObj,minSize,maxSize,isCheckAllowChar){
	var str = jQuery.trim(inputObj.val());
	var len = str_realLength(str);
	
	if(len==0 && minSize==0){
		return true;
	}
	
	if(str == ""){
		alert(comMsg.error_inputtext);
		inputObj.select();
		return false;
	}
	
	if (minSize == 0 && len > maxSize) {
		alert(msgArgsReplace(comMsg.error_inputlength_over,[maxSize]));
		inputObj.select();
		return false;
	}
	
	if(len < minSize || len > maxSize){
		alert(msgArgsReplace(comMsg.error_inputlength,[minSize,maxSize]));
		inputObj.select();
		return false;
	}
	
	if(isCheckAllowChar && !isFolderName(str)){
		alert(comMsg.error_invalidtext+"\n\n"
				+"`~\"\'!@#\$%^\*\(\)\+\|\\\/:;,\.\?<>\{\}\[\]");
		inputObj.select();
		return false;
	}
	
	return true;
}

function checkInputAddr(inputObj,minSize,maxSize,isCheckAllowChar){
	var str = jQuery.trim(inputObj.value);
	var len = str_realLength(str);
	
	if(len==0 && minSize==0){
		return true;
	}
	
	if(str == ""){
		alert(comMsg.error_inputtext);
		inputObj.select();
		return false;
	}
	
	if(isCheckAllowChar && incNotAllowAddr(str)){
		alert(comMsg.error_invalidtext+"\n\n"
				+"!#$%?^");
		inputObj.select();
		return false;
	}
	
	if (minSize == 0 && len > maxSize) {
		alert(msgArgsReplace(comMsg.error_inputlength_over,[maxSize]));
		inputObj.select();
		return false;
	}
	
	if(len < minSize || len > maxSize){
		alert(msgArgsReplace(comMsg.error_inputlength,[minSize,maxSize]));
		inputObj.select();
		return false;
	}
	
	return true;
}

function checkDatePeriod(sdate,edate){
	if(sdate == ""){
		alert(comMsg.error_inputdate);
		return false;
	}
	
	if(edate == ""){
		alert(comMsg.error_inputdate);
		return false;
	}
	
	sdate = replaceAll(sdate,"-","");
	edate = replaceAll(edate,"-","");	
	
	if(Number(sdate) > Number(edate)){
		alert(comMsg.error_invaliddate);
		return false;
	}
	
	return true;
}

function incNotAllowChar(str) {
	var regExp = /[\\\/:\*\?<>\|\.'"`]/;

	if (regExp.test(str)) {
		return true;
	} 
	return false;
}

function incNotAllowAddr(str) {
	var regExp = /[!@#\$%\?^\\]/;

	if (regExp.test(str)) {
		return true;
	} 
	return false;
}

function incNotAllowSearch(str) {
	var regExp = /[%\'\\]/;

	if (regExp.test(str)) {
		return true;
	} 
	return false;
}

function incNotAllowChar2(str) {
	var regExp = /[!@#\$%\?^\*\+\:;<>\=^\|\\]/;
	if (regExp.test(str)) {
		return true;
	} 
	return false;
}

function incNotAllowChar3(str) {
	var regExp = /[!#\$%\*\+\:;<>\=\?^\\]/;
	if (regExp.test(str)) {
		return true;
	} 
	return false;
}

function incNotAllowChar4(str) {
	var regExp = /[!#\$%\*\?^\\]/;
	if (regExp.test(str)) {
		return true;
	} 
	return false;
}	

function incNotAllowChar5(str) {
	var regExp = /[\\\"]/;
	if (regExp.test(str)) {
		return true;
	} 
	return false;
}
		
function incNotAllowBackslash(str) {
	var regExp = /[\\]/;

	if (regExp.test(str)) {
		return true;
	} 
	return false;
}

function incNotAllowQuotBackslash(str) {
	var regExp = /[\\\'\"]/;

	if (regExp.test(str)) {
		return true;
	} 
	return false;
}

function incNotAllowStr(str) {
	var regExp = /[`~\"\'!@#\$%^\*\(\)\+\|\\\/:;,\.\?<>\=]/;

	if (regExp.test(str)) {
		return true;
	} 
	return false;
}

function incNotAllowName(str) {
	var regExp = /[`~\"\'!@#\$%^\*\(\)\+\|\\\:;,\.\?<>\=]/;

	if (regExp.test(str)) {
		return true;
	} 
	return false;
}

function incNotAllowSenderName(str) {
	var regExp = /[#$%;,@&\\]/;

	if (regExp.test(str)) {
		return true;
	} 
	return false;
}

function isId(str) {
	var regExp = /^([0-9a-zA-Z_\-~!#$*+.\/=\?\{\}\'\^\|\`])+$/;

	if (str == "" || !regExp.test(str)) {
		return false;
	}
	return true;
}

function isEmailName(str){
	var regExp = /([\`#$%!*+\'\(\)\,\:\;\<\=\>\?\@\[\]\^\{\}\|\~\&\\])/;
	
	if (str == "" || regExp.test(str)) {
	    return false;
	}
	    return true;
}

function isValidEmail(str) {
	var name = get_name(str);
	var email = get_email(str);
	
	if(!isEmail(email) || !isEmailName(name)){
		return false;
	}
	return true;
}

function isNumber(str) {
	var regExp = /^[0-9]+$/;

	if (str == "" || !regExp.test(str)) {
		return false;
	}
	return true;
}

function isAlphabetNumber(str) {
	var regExp = /^[0-9a-zA-Z]+$/;

	if (str == "" || !regExp.test(str)) {
		return false;
	}
	return true;
}

function isName(str) {
	var regExp = /[\\\/\*\?\|'"`=]/;

	if (str == "" || regExp.test(str)) {
		return false;
	}
	return true;
}

function isPass(str) {
	var regExp = /[\\\"\<\>\(\)\[\]\,\;\:\@\|]/;

	if (str == "" || regExp.test(str)) {
		return false;
	}
	return true;
}

function isFolderName(str) {
	str = jQuery.trim(str);
	
	var len = str_realLength(str);
	
	var regExp = /[\`\"\'&%\*\\\\\/\.]/;
	if (regExp.test(str)) {
		return false;
	}
	return true;
}

function isUserName(str) {
	str = jQuery.trim(str);
	
	var len = str_realLength(str);
	
	var regExp = /[\"#$<>,;\\]/;
	if (regExp.test(str)) {
		return false;
	}
	return true;
	
}

function isPhone(str) {
	var regExp = /^[+0-9][0-9]+[0-9-]*[0-9]{3,4}$/

	if (str.length < 7 || !regExp.test(str)) {
		return false;
	}

	return true;
}

function isEmpNo(str) {
	var regExp = /^([0-9a-zA-Z\-_])+$/

	if (!regExp.test(str)) {
		return false;
	}

	return true;
}

function isCellPhone(str) {
	/*
	* 017-333-5555, 0173335555
	*/
	var regExp1 = /^(010|011|016|017|018|019)-[0-9]{3,4}-[0-9]{4}$/
	var regExp2 = /^(010|011|016|017|018|019)[0-9]{3,4}[0-9]{4}$/

	if (regExp1.test(str) || regExp2.test(str)) {
		return true;
	}

	return false;
}

function isZipCode(str) {
	if (str == "") {
		return true;
	}

	var regExp = /^[0-9\-]+[0-9]$/;
	if (regExp.test(str)) {
		return true;
	}

	return false;
}

function checkMaxLength(str, len)
{
	if (getStrByte(str) > len) {
		return false;
	}
	return true;
}

function getStrByte(str)
{
	var real_length = 0;
	var len = str.length;

	for (i = 0; i < len; i++) {
		ch = str.charCodeAt(i);
		if (ch >= 0xFFFFFF) {
			real_length += 4;
		}
		else if (ch >= 0xFFFF) {
			real_length += 3;
		}
		else if (ch >= 0xFF) {
			real_length += 2;
		}
		else {
			real_length++;
		}
	}

	return real_length;
}

function validateIP(ip) {
    if ( ip == "")
        return false;

	var ipchk = ip.split(".");
    if ( ipchk.length != 4 &&
         ipchk.length != 6 ){
        return false;
    }
    for( i = 0 ; i < ipchk.length; i++){
        if ( trim(ipchk[i]) == '' || isNaN(ipchk[i]) || parseInt(ipchk[i]) > 255 || parseInt(ipchk[i]) < 0)
            return false;
    }

    return true;
}

function isUrl(s) {
 	var regexp = /http:\/\/[A-Za-z0-9\.-]{3,}\.[A-Za-z]{3}/;
 	return regexp.test(s);
}

function isEmail(val)
{
	if (val.indexOf("@") < 0){
		return false;
	}

	if(val.length > 256){
		return false;
	}

	var strMember = new Array();
	strMember = val.split("@");	

	if (strMember.length > 2){
		return false;
	}	
	
	return isId(strMember[0]) && isDomain(strMember[1]);	
}

function isLocal(str)
{
	/*
	* local part allow character set
	* 0-9 a-z A-Z '!' '#' '$' '%' '&' '*' '+' '-' '.' '/' '=' '?'
	*/
	var localRegExp = /^[0-9a-zA-Z^-~!#$%&*+-.\/=\?]+$/;

	if (str == "" || !localRegExp.test(str)) {
		return false;
	}

	return true;
}

function isDomain(val)
{
	/*
	* domain allow character set
	* 0-9 a-z A-Z '-' '.'
	*/
	var re1 = /^[0-9a-zA-Z-]+([\.0-9a-zA-Z-]*)$/;

	if(val.length > 256){
		return false;
	}

	if(!re1.test(val)){
		return false;
	}

	if (val.indexOf(".") < 0){
		return false;		
	}

	var domainItems = new Array();
	domainItems = val.split(".");

	if(domainItems.length > 255){
		return false;
	}

	for (i = 0 ; i < domainItems.length; i++ ){
		var item = domainItems[i];
		if(item == "" || 
			item == null || 
			item.length < 1 || 
			item.length > 64){
			
			return false;
		}
	}

	if (val.charAt(val.length-1) == "." || 
		val.charAt(val.length-1) == "-"){
		return false;		
	}

	if (val.indexOf(".-") > 0 || 
		val.indexOf("-.") > 0 ||
		val.indexOf("--") > 0){
		return false;
	}
		
	return true;
	
}

function isImgFile(str) {
	var pattern = "jpg|jpeg|gif|png|bmp|tif";
	var regExp = new RegExp("\\.(" + pattern + ")$", "i");

	if (!regExp.test(str)) {
		return false;
	}

	return true;
}

function isConfirmFile(str,chktype) {	
	var regExp = new RegExp("\\.(" + chktype + ")$", "i");

	if (!regExp.test(str)) {
		return false;
	}

	return true;
}

/*
 * allowed file length is 64
 */
function chkUploadFileLength(str) {
	if (getStrByte(str) > 64) {
		return false;
	}

	return true;
}

function isPort(port){
    if ( isNaN(port) || port == '' || parseInt(port) <= 0 || parseInt(port) > 65535)
        return false;
    else
        return true;
}

function isSsn(ssn) 
{
	fmt = /^\d{6}-[1234]\d{6}$/;
	if (!fmt.test(ssn)) {
		return false;
	}

	birthYear = (ssn.charAt(7) <= "2") ? "19" : "20";
	birthYear += ssn.substr(0, 2);
	birthMonth = ssn.substr(2, 2) - 1;
	birthDate = ssn.substr(4, 2);
	birth = new Date(birthYear, birthMonth, birthDate);

	if ( birth.getFullYear() % 100 != ssn.substr(0, 2) ||
		birth.getMonth() != birthMonth ||
		birth.getDate() != birthDate) {
		return false;
	}

	buf = new Array(13);
	for (i = 0; i < 6; i++) buf[i] = parseInt(ssn.charAt(i));
	for (i = 6; i < 13; i++) buf[i] = parseInt(ssn.charAt(i + 1));
	multipliers = [2,3,4,5,6,7,8,9,2,3,4,5];
	for (i = 0, sum = 0; i < 12; i++) sum += (buf[i] *= multipliers[i]);

	if ((11 - (sum % 11)) % 10 != buf[12]) {
		return false;
	}

	return true;
}

function isFgnSsn(ssn) { 
    var sum=0; 
    var odd=0; 
    buf = new Array(13); 
    for(i=0; i<13; i++) { buf[i]=parseInt(ssn.charAt(i)); } 
    odd = buf[7]*10 + buf[8]; 
    if(odd%2 != 0) { return false; } 
    if( (buf[11]!=6) && (buf[11]!=7) && (buf[11]!=8) && (buf[11]!=9) ) { 
            return false; 
    } 
    multipliers = [2,3,4,5,6,7,8,9,2,3,4,5]; 
    for(i=0, sum=0; i<12; i++) { sum += (buf[i] *= multipliers[i]); } 
    sum = 11 - (sum%11); 
    if(sum >= 10) { sum -= 10; } 
    sum += 2; 
    if(sum >= 10) { sum -= 10; } 
    if(sum != buf[12]) { return false } 
    return true; 
} 

function isMailDomain(val){	

	if(val.charAt(0) != '@') {
		return false;
	}

	var a = val.split("@");
	if (!isDomain(a[1])) {
		return false;
	}

	return true;
}

function isDisplayName(obj) {
    var str = obj.value;

    if (jQuery.trim(str) == "") {
        alert(comMsg.comn_name_alert_001);
        obj.value = "";
        obj.value = "";
        obj.focus();
        return false;
    }

	var regExp = /[\\\/\*\?\|'"`=]/;
	if (regExp.test(str)) {
		alert(comMsg.comn_name_alert_002+"\n"
			+ "\\\/\*\?\'\|\"\`=");
        obj.focus();
        return false;
	}

    if (str_realLength(str) > 60) {
        alert(comMsg.comn_name_alert_003);
        obj.value = "";
        obj.focus();
        return false;
    }
    return true;
}

function checkNumber (input, min, max) {
	var re = /^([0-9])+$/;
	
	if (!re.test(input) || !re.test(min) || !re.test(max))
		return false;
	
	var number = new Number (input);
	var minNumber = new Number (min);
	var maxNumber = new Number (max);
	if (minNumber <= number && number <= maxNumber)
		return true;
	else
		return false;
}