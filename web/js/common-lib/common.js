var agent	= navigator.userAgent.toUpperCase();
var isMsie	= isMsie();
var isMsie6	= (isMsie && (agent.indexOf("MSIE 6") > 0 && (agent.indexOf("MSIE 7") < 0)));
var isMsie7	= (isMsie && (agent.indexOf("MSIE 7") > 0));
var isMsie8	= (isMsie && (agent.indexOf("MSIE 8") > 0));
var isMsie9	= (isMsie && (agent.indexOf("MSIE 9") > 0));
var isPopup = isPopup();

function isMsie() {
	if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("MSIE") != -1) ) {
		return true;
	}else {
		return false;
	}
}
function getBrowserType(){
	if((agent.indexOf("MSIE") != -1) && (agent.indexOf("Opera") == -1))
		return "ie";
	else if(agent.indexOf('Gecko') != -1)
		return "gecko";
	else if(agent.indexOf('Opera') != -1)
	    return "opera";
	else return "nav";
}

function isPopup() {
	try { 
		if(opener){
			opener.document;
			return true;
		} else {
			return false;
		} 
	} catch (e){ 
		return false; 
	}
}

function setTopMenu(menu) {
	
	if(IS_TMENU_USE){	
		jQuery('#topMenu_left li').removeClass();
		if (menu == 'mail') {		
			jQuery('#mtop_menu_mail').addClass('on');
		}
		else if (menu == 'addr') {
			jQuery('#mtop_menu_addr').addClass('on')
		}
		else if (menu == 'scheduler') {
			jQuery('#mtop_menu_calendar').addClass('on')
		}
		else if (menu == 'setting') {
			jQuery('#mtop_menu_setting').addClass('on')
		}
		else if (menu == 'bbs') {
			jQuery('#mtop_menu_bbs').addClass('on')
		}
		else if (menu == 'webfolder') {
			jQuery('#mtop_menu_webfolder').addClass('on')
		}
		else if (menu == 'organization') {
			jQuery('#mtop_menu_org').addClass('on')
		}
	}
}

function gotoURL(url) {
    this.location = url;
}

function gotoParentURL(url) {
	parent.location = url;
}

/*
 * Desc : Move write-page with parameters
 * Args : [to][type][folder][uid]
 */

function help() {
    var url= "/help/" + locale + "/index.htm";
    openSimplePopup(url,700,550,false);
}

function printSize(num) {
	if (num > 1024 * 1024) {
    	var pstr = Math.floor((num*10) / (1024*1024));
    	return parseInt((pstr/10)) +"MB";
	} else if (num > 1024) {
    	var pstr = Math.floor((num*10) / (1024));
    	return parseInt((pstr/10)) +"KB";
	} else {
    	return num +"B";
	}
}

function trim(str) {
	return str.replace(/^\s*|\s*$/g,"");
}

function setCookie(name, value, expiredays) {	
	var date = new Date();
	date.setDate( date.getDate() + expiredays );
	var pstr = name + "=" + escape(value)
		+ "; path=/; expires=" + date.toGMTString() + ";"
	document.cookie = pstr;
}

function getCookie(cookieName) {
	var str = "";
	allCookies = document.cookie.split('; ');
	for (var i = 0; i < allCookies.length; i++) {
		cookieArray = allCookies[i].split('=');
		if (cookieName == cookieArray[0]) {
			return unescape(cookieArray[1]);
		}
	}
	return str;
}

/**
 * pick out mail part from email string("xxx"<a@a.com>)
 * return : a@a.com
 **/
function get_email(email) {
    if(email.indexOf('<') < 0) {
        return email;
    }

    var s = email.indexOf('<') + 1;
    var e = (email.length - 1);
    return email.substr(s, (e-s));
}

/**
 * pick out name part from email string("xxx"<a@a.com>)
 * return : xxx
 **/
function get_name(email) {
    if(email.indexOf('<') < 0) {
        return "";
    }

    var s = 0;
    var e = email.indexOf('<');
    var name = email.substr(s, e);
    return name.replace(/[\"]/g, "");
}

/**
 * split email addresses str.
 * return email address array.
 * ex: str = "name1"<a@a.com>, "name2"<b@b.com>
 *     return = {""name1"<a@a.com>", ""name2"<b@b.com>"}
 **/
function getEmailArray(str) {
	var addr_array = str.split(/\s*[,;\r\n]\s*/);
	var new_array = new Array;

	var j = 0;

	for(var i = 0; i < addr_array.length; i++) {
		var address = addr_array[i];

		if(address.charAt(0) == '"'
		&& address.indexOf("\"", 1) < 0) {

			if(addr_array[i+1] != null) {
				addr_array[i+1] = address + "," + addr_array[i+1];
				continue;
			}
		}

		new_array[j] = address;
		j++;
	}

	return new_array;
}

function getOnlyEmailArray(str){
	var emails = [];
	str = jQuery.trim(str);
	if(str && str.length != ""){
		var addresses = getEmailArray(str);
		
		var address;
		for(var i = 0; i < addresses.length; i++) {
	        address = addresses[i];        
			address = jQuery.trim(address);		
			emails.push(get_email(address));		
		}
	}	
	return emails;
}

function str_realLength(str) {
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


/*
Korean Alphabat String validate
*/
function validateHan(val)
{
        var re = /^[0-9a-zA-Z^-~!#$%&*+-.\/=\?\[\]\{\}\(\)\'\"\@\;\:|\s]*$/;
        return !(re.test(val));
}

/* ------------------------------------------------------------------
* Renewed Functions 
* ------------------------------------------------------------------*/

function JSONSort(array, pos, how, start, row) {
	if (!how)   how = "asc";
	if (!row)   row = array.length;
	if (!start) start = 0;

	for (var i = start; i < row ; ++i) {
		for (var j = i+1 ; j < row ; ++j) {
			if ((how == "asc" && (array[i][pos] > array[j][pos]))
					|| (how == "desc" && (array[i][pos] < array[j][pos]))) {
				var tmp = array[i];
				array[i] = array[j];
				array[j] = tmp;

			}
		}
	}
}

function toggleDisplay(obj) {
	if (obj) {
		if (obj.style.display == "none") {
			obj.style.display = "";
		} else {
			obj.style.display = "none";
		}
	}
}

/*
 * Move focus to next item
 * - obj: current object
 * - nextID: ID of object to focus
 */
function cntlFocus(curObj, nextObj) {
	if (nextObj == null) return;
	var maxLen = curObj.getAttribute("maxlength");
	if (curObj.value.length >= maxLen) {
		nextObj.focus();
	}
}

function winResize(minHeight) {
	var Dwidth = 0;
	var Dheight = 0;
	
    var divEl = document.createElement("div");
    divEl.style.position = "absolute";
    divEl.style.left = "0px";
    divEl.style.top = "0px";
    divEl.style.width = "100%";
    divEl.style.height = "100%";
    document.body.appendChild(divEl);
        
    if (jQuery.browser.msie){
    	Dwidth = 0;
    	Dheight = jQuery("body").innerHeight();    	
    }else{
    	Dwidth = document.body.clientWidth;
    	Dheight = document.body.clientHeight;
    }
    
    var pheight = Dheight-divEl.offsetHeight;
    if(minHeight && minHeight < pheight){
    	pheight = 0;
    }   
    
  	if(jQuery.browser.msie){  		
  		window.resizeBy(Dwidth,pheight);
  	} else {  		
  		window.resizeBy(Dwidth-divEl.offsetWidth,pheight);
  	}
  	  			    
    document.body.removeChild(divEl); 
	
}



function openSimplePopup(url,width,height,resize,xpos,ypos){
	var xp,yp;
	var resizeVal = 0;
	if(xpos){
		xp = xpos;
	} else {
		xp = (screen.width/2)-180;
	}
	
	if(ypos){
		yp = ypos;
	} else {
		yp = (screen.height/2)-200;
	}
	
	if(resize){
		resizeVal = 1;
	}

	window.open(url, "",
        "width="+width+",height="+height+",top="+yp+",left="+xp+",,resizable="+resizeVal);
}

/*
 * Check or Uncheck all checkbox items
 */
function checkAll(obj_checkAll, obj_checkBox) {
	if (!obj_checkBox) {
		return;
	}

	var flag = obj_checkAll.checked ? true : false;

	if (obj_checkBox.length) {
		for (var i = 0; i < obj_checkBox.length; i++) {
			obj_checkBox[i].checked = flag;
		}
	} else {
		obj_checkBox.checked = flag;
	}
}

function checkedCnt(obj) {
    var chkCnt = 0;

    if (!obj) {
        return 0;
    }

    if (obj.length) {
        for (var i = 0; i < obj.length; i++) {
            if (obj[i].checked) {
                chkCnt++;
            }
        }
    }
    else {
        if(obj.checked) {
            chkCnt++;
        }
    }

	return chkCnt;
}

function objCnt(obj) {
    var oCnt = 0;

    if (!obj) {
        return 0;
    }

    if (obj.length) {
        for (var i = 0; i < obj.length; i++) {
        	oCnt++;
        }
    }
    else {
    	oCnt++;
    }
	return oCnt;
}

function BottomMove() {
	try {
		var showDiv = document.getElementById("progressLayer");
		windowHeight = document.body.clientHeight;
		windowWidth = document.body.clientWidth;
		scrollY = document.body.scrollTop + 62;
		scrollX = document.body.scrollLeft;
		showDiv.style.top = scrollY;
		showDiv.style.left = scrollX;
		showDiv.style.heigth = windowHeight;
		showDiv.style.width = windowWidth;
	} catch (e) {alert(e);}
}

var defaultProgressText;

function setDefaultProgressText(str){
	defaultProgressText = str
}
function viewProgress(str) {
	
	if(!str){
		document.getElementById("progressText").innerHTML=defaultProgressText;
	} else {
		document.getElementById("progressText").innerHTML=str;
	}	
	
	try {
		var showDiv = document.getElementById("progressLayer");
		
		showDiv.style.display = "";
		//setInterval("BottomMove()",500);
		// BottomMove();
	} catch (e) {alert(e);}
}

function hiddenProgress() {
	try {
		var showDiv = document.getElementById("progressLayer");
		showDiv.style.display = "none";
	} catch (e) {}
}
function keyEvent(evt) {
	var Code;
    if(!isMsie){
     Code = evt.which;
    }else{
     Code = event.keyCode;
    }
    
    return Code;
}

function linecheck(check,str){
	var checkedData=check.checked;
	if(checkedData){
		yellowline("tr_"+str);
	}	else{
		whiteline("tr_"+str);
	}
}

function yellowline(id) {
	if (typeof(id) == "object") {
		chgBgColor(id, '#FFFFCC');
	} else {
		chgBgColor(document.getElementById(id), '#FFFFCC');
	}
}
function grayline(id) {
	if (typeof(id) == "object") {
		chgBgColor(id, '#F6F6F6');
	} else {
		chgBgColor(document.getElementById(id), '#F6F6F6');
	}
}
function whiteline(id) {
	if (typeof(id) == "object") {
		chgBgColor(id, '#FFFFFF');
	} else {
		chgBgColor(document.getElementById(id), '#FFFFFF');
	}
}

function yellowline2(obj) {
    var trObj = (obj.parentNode).parentNode;
    if (obj.checked) {
		chgBgColor(trObj, '#FFFFCC');
    } else {
		chgBgColor(trObj, '#FFFFFF');
    }
}

function chgBgColor(obj, color) {
	obj.style.backgroundColor = color;
}

function ellipsisFolderName(folderName, depth) {
	var viewLength = 10;

	if (depth == 2) {
		viewLength = 8;
	} else if (depth == 3) {
		viewLength = 6;
	}

	if (folderName.length > viewLength) {
		folderName = folderName.substring(0, viewLength);
		folderName += "...";
	}

	return folderName;
}

function ellipsisString(folderName, viewLength) {	
	if (folderName.length > viewLength) {
		folderName = folderName.substring(0, viewLength);
		folderName += "...";
	}

	return folderName;
}

/*function replaceAll(obj, oldstr, newstr) {
	var strip = new RegExp(oldstr, "gi");
	return obj.replace(strip, newstr);
}*/

function replaceAll(str,oldStr,reqStr){	
	return str.split(oldStr).join(reqStr);
}

function isFolderName(str) {
	var regExp = /[`~\"\'!@#\$%^\*\(\)\+\|\\\/:;,\.\?<>\{\}\[\]]/;
	if (regExp.test(str)) {
		return false;
	}
	return true;
}

function msgArgsReplace(msg, args){	
	for ( var i = 0; i < args.length; i++) {
		var matchArgs = "{"+i+"}";		
		msg = replaceAll(msg,matchArgs,args[i]);
	}
	return msg;
}

function printSize(num) {
	if (num > 1024 * 1024) {
    	var pstr = Math.floor((num*10) / (1024*1024));
    	return parseInt((pstr/10)) +"MB";
	} else if (num > 1024) {
    	var pstr = Math.floor((num*10) / (1024));
    	return parseInt((pstr/10)) +"KB";
	} else {
    	return num +"B";
	}
}

function get_length(str) {
    real_length = 0;
    len = str.length;
    for(i = 0; i < len; i++) {
        ch = str.charCodeAt(i);
        if(ch >= 0xFFFFFF) {
            real_length += 4;
        } else if(ch >= 0xFFFF) {
            real_length += 3;
        } else if (ch >= 0xFF) {
            real_length += 2;
        } else {
            real_length++;
        }
    }
    return real_length;
}

function isMail(str){
    len =  get_length(str);
    if(len > 256)
        return false;
    for(i=0;i<len;i++){
        ch = str.charAt(i);
        if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z')
        || (ch >= 'a' && ch <= 'z') || ch == '-' || ch == '.' ||
            ch == '_' || ch == '@')
            continue;
        else{
            return false;
        }
    }
    for(i=0;i<len;i++){
        ch = str.charAt(i);
        if(ch == '@')
            return true;
    }
    return false;
}

function unescape_tag(s) {

	if (s && Number(s)) {
		return s;
	}
	
	var r = "";	
	if(s && s != "" && typeof(s) == "string"){		
		r = replaceAll(s, '&quot;', '\"');
		r = replaceAll(r, '&lt;', '<');
		r = replaceAll(r, '&gt;', '>');
		r = replaceAll(r, '&#034;', '\"');
		r = replaceAll(r, '&#91;', '[');
		r = replaceAll(r, '&#93;', ']');
		r = replaceAll(r, '&#039;', '\'');
	}
	return r;
}

function unescape_tag_title(s) {

	if (s && Number(s)) {
		return s;
	}
	
	var r = "";	
	if(s && s != "" && typeof(s) == "string"){		
		r = replaceAll(s, '&quot;', '\"');
		r = replaceAll(r, '&lt;', '<');
		r = replaceAll(r, '&gt;', '>');
		r = replaceAll(r, '&#034;', '\"');
		r = replaceAll(r, '&#91;', '[');
		r = replaceAll(r, '&#93;', ']');
		r = replaceAll(r, '&amp;', '&');
	}
	
	return r;
}

function escape_tag(s) {

	if (s && Number(s)) {
		return s;
	}
	
	var r = "";
	if(s && s != "" && typeof(s) == "string"){
		r = replaceAll(s, '<', '&lt;');
		r = replaceAll(r, '>', '&gt;');
		r = replaceAll(r, '\"', '&quot;');
		r = replaceAll(r, '\"', '&#034;');
		r = replaceAll(r, '\'', '&#039;');
	}
	return r;
}

function dup_check(txtObj, selObj) {
	var isDup = false;
	
	for(i=0; i<selObj.options.length; i++) {
		if (txtObj.value == selObj.options[i].value) {
			isDup = true;
			break;
		}
	}
	
	return isDup;
}

function markAll(flag, forms){
	if(forms){
		if(forms.length > 1){
			for (var i = 0 ; i < forms.length ; i++){
				forms[i].checked = flag; 
			}
		} else {
			forms.checked = flag;
		}
	}	
}

function chkBoxCheck(forms){
	var isChecked = false;
	if(forms){
		if(forms.length > 1){
			for (var i = 0 ; i < forms.length ; i++){
				if(forms[i].checked){
					isChecked = true;
					break;
				} 
			}
		} else {
			if(forms.checked){
				isChecked = true;			
			} 
		}
	}
	
	return isChecked;
}

function pausecomp(millis) 
{
	var date = new Date();
	var curDate = null;
	
	do {curDate = new Date();} 
	while((curDate - date) < millis);
}


/*
* Date Format 1.2.3
* (c) 2007-2009 Steven Levithan <stevenlevithan.com>
* MIT license
*
* Includes enhancements by Scott Trenda <scott.trenda.net>
* and Kris Kowal <cixar.com/~kris.kowal/>
*
* Accepts a date, a mask, or a date and a mask.
* Returns a formatted version of the given date.
* The date defaults to the current date/time.
* The mask defaults to dateFormat.masks.default.
*/

var dateFormat = function () {
	var	token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
		timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
		timezoneClip = /[^-+\dA-Z]/g,
		pad = function (val, len) {
			val = String(val);
			len = len || 2;
			while (val.length < len) val = "0" + val;
			return val;
		};

	// Regexes and supporting functions are cached through closure
	return function (date, mask, utc) {
		var dF = dateFormat;

		// You can't provide utc if you skip other args (use the "UTC:" mask prefix)
		if (arguments.length == 1 && Object.prototype.toString.call(date) == "[object String]" && !/\d/.test(date)) {
			mask = date;
			date = undefined;
		}

		// Passing date through Date applies Date.parse, if necessary
		date = date ? new Date(date) : new Date;
		if (isNaN(date)) throw SyntaxError("invalid date");

		mask = String(dF.masks[mask] || mask || dF.masks["default"]);

		// Allow setting the utc argument via the mask
		if (mask.slice(0, 4) == "UTC:") {
			mask = mask.slice(4);
			utc = true;
		}

		var	_ = utc ? "getUTC" : "get",
			d = date[_ + "Date"](),
			D = date[_ + "Day"](),
			m = date[_ + "Month"](),
			y = date[_ + "FullYear"](),
			H = date[_ + "Hours"](),
			M = date[_ + "Minutes"](),
			s = date[_ + "Seconds"](),
			L = date[_ + "Milliseconds"](),
			o = utc ? 0 : date.getTimezoneOffset(),
			flags = {
				d:    d,
				dd:   pad(d),
				ddd:  dF.i18n.dayNames[D],
				dddd: dF.i18n.dayNames[D + 7],
				m:    m + 1,
				mm:   pad(m + 1),
				mmm:  dF.i18n.monthNames[m],
				mmmm: dF.i18n.monthNames[m + 12],
				yy:   String(y).slice(2),
				yyyy: y,
				h:    H % 12 || 12,
				hh:   pad(H % 12 || 12),
				H:    H,
				HH:   pad(H),
				M:    M,
				MM:   pad(M),
				s:    s,
				ss:   pad(s),
				l:    pad(L, 3),
				L:    pad(L > 99 ? Math.round(L / 10) : L),
				t:    H < 12 ? "a"  : "p",
				tt:   H < 12 ? "am" : "pm",
				T:    H < 12 ? "A"  : "P",
				TT:   H < 12 ? "AM" : "PM",
				Z:    utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
				o:    (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
				S:    ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
			};

		return mask.replace(token, function ($0) {
			return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
		});
	};
}();

//Some common format strings
dateFormat.masks = {
	"default":      "ddd mmm dd yyyy HH:MM:ss",
	shortDate:      "m/d/yy",
	mediumDate:     "mmm d, yyyy",
	longDate:       "mmmm d, yyyy",
	fullDate:       "dddd, mmmm d, yyyy",
	shortTime:      "h:MM TT",
	mediumTime:     "h:MM:ss TT",
	longTime:       "h:MM:ss TT Z",
	isoDate:        "yyyy-mm-dd",
	isoTime:        "HH:MM:ss",
	isoDateTime:    "yyyy-mm-dd'T'HH:MM:ss",
	isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
};

//Internationalization strings
dateFormat.i18n = {
	dayNames: [
		"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
		"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
	],
	monthNames: [
		"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
		"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
	]
};

//For convenience...
Date.prototype.format = function (mask, utc) {
	return dateFormat(this, mask, utc);
};

function checkTdLine(chkObj){	
	var bool = chkObj.checked;		
			
	var trObj = chkObj;
	while (trObj.tagName != "TR") {
        trObj = trObj.parentNode;
    }    

	if(bool){			
		jQuery(trObj).addClass("TM_checkLow");			
	} else {
		jQuery(trObj).removeClass("TM_checkLow");		
	}					
}

function checkAllTdLine(chkObj,bool){
	var chkList;
	if(chkObj){
		if(chkObj.length > 1){
			chkList = chkObj;
		} else {
			chkList = [chkObj];
		}
	
		for ( var i = 0; i < chkList.length; i++) {
			if(chkList[i].checked != bool){
				chkList[i].checked = bool;
				checkTdLine(chkList[i],bool);	
			}				
		}
	}
}


function storeLinkId(id){	
	var oldLink = jQuery("#storeData").data("currentLinkId");
	if(oldLink && oldLink != ""){
		if(skin != "skin3"){
			jQuery("#"+oldLink).css("font-weight","normal");
		}
	}		
	if(id && id !=""){
		id = getFolderNameEscape(id);
		if(skin != "skin3"){
			jQuery("#"+id).css("font-weight","bold");
		}
		jQuery("#storeData").data("currentLinkId",id);
	}	
}

function setStoreScrollHeight(height){
	jQuery.cookie("storeScrollHeight", height,{path:"/"});	
}

function getStoreScrollHeight(){	
	return jQuery.cookie("storeScrollHeight");
}

function isImgFile(str) {
	var pattern = "jpg|jpeg|gif|png|bmp|tif";
	var regExp = new RegExp("\\.(" + pattern + ")$", "i");

	if (!regExp.test(str)) {
		return false;
	}

	return true;
}

function isJpgNGifFile(str) {
	var pattern = "jpg|jpeg|gif";
	var regExp = new RegExp("\\.(" + pattern + ")$", "i");

	if (!regExp.test(str)) {
		return false;
	}

	return true;
}

function isCsvFile(str) {
	var pattern = "csv";
	var regExp = new RegExp("\\.(" + pattern + ")$", "i");

	if (!regExp.test(str)) {
		return false;
	}

	return true;
}

function makeRandom(){
	return Math.floor(Math.random() * 1000000) + 1;
}

function cutMessageLength(message, maximum, textlimit)
{
	var inc = 0;
	var nbytes = 0;
	var msg = "";
	var msglen = message.length;

	for (i=0; i<msglen; i++) {
		var ch = message.charAt(i);
		if (escape(ch).length > 4) {
			inc = 2;
		} else if (ch == '\n') {
			if (message.charAt(i-1) != '\r') {
				inc = 1;
			}
		} else if (ch == '<' || ch == '>') {
			inc = 4;
		} else {
			inc = 1;
		}
		if ((nbytes + inc) > maximum) {
			break;
		}
		nbytes += inc;
		msg += ch;
	}
	document.getElementById(textlimit).innerHTML = nbytes;
	return msg;
}

function onloadRedy(init_func) {
	jQuery(document).ready( function (){
		eval(init_func);
	});
}

function clone(obj){
    if(obj == null || typeof(obj) != 'object')
        return obj;

    var temp = {};
    for(var key in obj)
        temp[key] = clone(obj[key]);

    return temp;
}

function makeParamElement(form,obj){
    if(obj == null || typeof(obj) != 'object')
        return;
    
    var formObj = jQuery(form);

    var inputForms = "";    	
	var param = jQuery.param(obj);   
    var values = param.split("&");
	var elems,val;
	try{
		var param = jQuery.param(obj);
	    
	    var values = param.split("&");
		var elems,val;
		for ( var i = 0; i < values.length; i++) {
			elems = values[i].split("=");			
			val = decodeURI(elems[1]);
			val = replaceAll(val,"+"," ");
			val = replaceAll(val,"%40","@");
			val = replaceAll(val, "%2C", ",");			
			inputForms += "<input type='hidden' name='"+elems[0]+"' value='"+escape_tag(val)+"'>";
		}
		
	}finally{
		param = values = elems = null;
	}
	
    formObj.empty();
    formObj.html(inputForms);
}

function doubleUrlEncode(str) {
	return encodeURIComponent(encodeURIComponent(str));
}


function getFileTypeImage(fileName){
	if(fileName){
		var fileType = fileName.substring((fileName.lastIndexOf(".")+1),fileName.length);	
		if(fileType !='doc' && 	fileType!='docx'&& 	fileType!='gif' && 
			fileType!='pdf' && 	fileType!='html'&& 	fileType!='hwp' && 
			fileType!='jpg' && 	fileType!='bmp' &&	fileType!='ppt' && 
			fileType!='pptx'&& 	fileType!='txt' && 	fileType!='xls' && 
			fileType!='xlsx'&& 	fileType!='zip' && 	fileType!='xml' &&
			fileType!='mpeg'&&	fileType!='avi' && 	fileType!='htm' &&
			fileType!='mp3' &&	fileType!='mp4' &&  fileType!='eml'){
			
			fileType = "unknown";
		}
		
		return fileType;
	} else {
		return "unknown";
	}
}

function getFolderNameEscape(fname){
	fname = jQuery.trim(replaceAll(fname,"(","-S-"));
	fname = jQuery.trim(replaceAll(fname,")","-E-"));
	fname = jQuery.trim(replaceAll(fname," ","_-_"));
	fname = jQuery.trim(replaceAll(fname,".","-_-"));
	fname = jQuery.trim(replaceAll(fname,"<","-LT-"));
	fname = jQuery.trim(replaceAll(fname,">","-GT-"));
	fname = jQuery.trim(replaceAll(fname,"=","-EQ-"));
	fname = jQuery.trim(replaceAll(fname,"@","-AT-"));
	fname = jQuery.trim(replaceAll(fname,"^","-CA-"));
	fname = jQuery.trim(replaceAll(fname,"{","-OCB-"));
	fname = jQuery.trim(replaceAll(fname,"}","-CCB-"));
	fname = jQuery.trim(replaceAll(fname,"|","-BA-"));
	fname = jQuery.trim(replaceAll(fname,"~","-TD-"));
	fname = jQuery.trim(replaceAll(fname,"!","-EM-"));
	fname = jQuery.trim(replaceAll(fname,";","-SM-"));
	fname = jQuery.trim(replaceAll(fname,"#","-SH-"));
	fname = jQuery.trim(replaceAll(fname,"+","-PL-"));
	fname = jQuery.trim(replaceAll(fname,"[","-SSB-"));
	fname = jQuery.trim(replaceAll(fname,"]","-ESB-"));
	fname = jQuery.trim(replaceAll(fname,",","-CM-"));
	
	return fname;
}

function getFolderNameUnescape(fname){
	fname = jQuery.trim(replaceAll(fname,"-S-","("));
	fname = jQuery.trim(replaceAll(fname,"-E-",")"));
	fname = jQuery.trim(replaceAll(fname,"_-_"," "));
	fname = jQuery.trim(replaceAll(fname,"-_-","."));
	fname = jQuery.trim(replaceAll(fname,"-LT-","<"));
	fname = jQuery.trim(replaceAll(fname,"-GT-",">"));
	fname = jQuery.trim(replaceAll(fname,"-EQ-","="));
	fname = jQuery.trim(replaceAll(fname,"-AT-","@"));
	fname = jQuery.trim(replaceAll(fname,"-CA-","^"));
	fname = jQuery.trim(replaceAll(fname,"-OCB-","{"));
	fname = jQuery.trim(replaceAll(fname,"-CCB-","}"));
	fname = jQuery.trim(replaceAll(fname,"-BA-","|"));
	fname = jQuery.trim(replaceAll(fname,"-TD-","~"));
	fname = jQuery.trim(replaceAll(fname,"-EM-","!"));
	fname = jQuery.trim(replaceAll(fname,"-SM-",";"));
	fname = jQuery.trim(replaceAll(fname,"-SH-","#"));
	fname = jQuery.trim(replaceAll(fname,"-PL-","+"));
	fname = jQuery.trim(replaceAll(fname,"-SSB-","["));
	fname = jQuery.trim(replaceAll(fname,"-ESB-","]"));
	fname = jQuery.trim(replaceAll(fname,"-CM-",","));
	return fname;
}
function setCurrentPage(currentPage){
	jQuery("#page"+currentPage).removeClass("pageStyleBasic");
	jQuery("#page"+currentPage).addClass("pageStyleSelect");
}
function parentSetCurrentPage(currentPage){
	if(typeof(fraContent) != 'undefined'){
		fraContent.jQuery("#page"+currentPage).removeClass("pageStyleBasic");
		fraContent.jQuery("#page"+currentPage).addClass("pageStyleSelect");
	}
}
function hasFlashPlayer() {
	return (jQuery.browser.flash);
}

//201802 
String.prototype.toUnicode = function(){
    var result = "";
    for(var i = 0; i < this.length; i++){
        // Assumption: all characters are < 0xffff
        result += "\\u" + ("000" + this[i].charCodeAt(0).toString(16)).substr(-4);
    }
    return result;
};