var formtags=["input", "textarea", "select", "html" , "embed"];
var blurtags=["input","textarea"];
function disableselect(e){
	if(typeof e == "undefined") return;
	var oldTarget = document.activeElement || null;
	if(formtags.indexOf(e.target.tagName.toLowerCase()) != -1) return true;
	if (navigator.userAgent.toLowerCase().indexOf("gecko") != -1) {
		document.body.style.MozUserSelect = 'none';
	}
	var oldTagName = oldTarget?oldTarget.tagName.toLowerCase():"";
	if(blurtags.indexOf(oldTagName) != -1 && oldTarget.blur){
		oldTarget.blur();
	}
	return false;
}
function reEnable(){
	if (navigator.userAgent.toLowerCase().indexOf("gecko") != -1) {
		document.body.style.MozUserSelect = '';
	}
	return true; 
}

function blockContent(e){
	var e = window.event || e;
	if(e.preventDefault){ e.preventDefault(); }
	if(e.stopPropagation){ e.stopPropagation(); }
	e.returnValue=false;
	e.cancelBubble=true;
}
 
if (document.addEventListener){
	//document.addEventListener('selectstart', blockContent, false);
	//document.addEventListener('dragstart', blockContent, false);
	document.addEventListener('contextmenu', blockContent, false);
} else if (document.attachEvent){
	//document.attachEvent('onselectstart', blockContent);
	//document.attachEvent('ondragstart', blockContent);
	document.attachEvent('oncontextmenu', blockContent);
}
//document.onmousedown = disableselect;
//document.onmouseup = reEnable;