function addList(txtObj, selObj, maxCnt,myEmail,onlyEmail) {
	var address = txtObj.value.toLowerCase();
	var name = get_name(address);
	var email = get_email(address);
	
	if(!checkInputLength("", txtObj, "", 0, 130)) {
		return;
	}
	
	if (email == myEmail) {
		alert(comMsg.common_form_001);
		txtObj.select();
		return ;
	}
	
	if (selObj.length >= maxCnt) {
		alert(msgArgsReplace(comMsg.common_form_002,[maxCnt]));
		txtObj.select();
		return;
	}
	
	if (trim(txtObj.value) == "") {
        alert(comMsg.common_form_003);
        txtObj.select();
        return;
    }
	
	if (!onlyEmail || onlyEmail!="true") {
		if (name != "") {
			if(!isEmailName(name)){
				alert(comMsg.common_form_004);
				txtObj.select();
		        return;
			}
		}
	} else {
		email = address;
	}
	
	if(!isMail(email)){
		alert(comMsg.common_form_004);
		txtObj.select();
        return;
	}

	if (dup_check(txtObj, selObj)) {
		alert(comMsg.common_form_005);
		txtObj.select();
		return;
	}
	
	selObj.options[selObj.length] = new Option(address, address);

    txtObj.value = "";
}

function searchList(txtObj, selObj) {
	var address = txtObj.value.toLowerCase();

	clearSelObj(selObj);

	var isPattern = false;
	
	if (trim(txtObj.value) == "") {
        alert(comMsg.common_form_006);
        return;
    }

	for (var i = 0; i < selObj.length; i++) {
		if (selObj.options[i].text.indexOf(address) >= 0) {
			selObj.options[i].selected = true;
			isPattern = true;
		}
	}

}

function clearSelObj(selObj) {
	for (var i = 0; i < selObj.length; i++) {
		selObj.options[i].selected = false;
	}
}

function selectAll(list,select) {
	
	var bool = true;
	
	for (i=0; i < list.length; i++) {
        list.options[i].selected = bool;
    }
}

function deleteList(selObj) {
	move = new Array();
	var count=0;
	for(i=0;i<selObj.options.length;i++){
		if(selObj.options[i].selected){
			move[count] = selObj.options[i].text;
			 count++;
		}
	}
	
	if (count == 0) {
		alert(comMsg.common_form_007);
		return;
	}
	else if (count > 0) {
		if(!confirm(comMsg.common_form_009)) {
			return;
		}
	}
	
	for(i=0;i<move.length;i++){
		for(j=0;j<selObj.options.length;j++){
			if(selObj.options[j].text==move[i]) {
				if(getBrowserType() == "ie" || getBrowserType() == "opera")
				selObj.options.remove(j);
				else if(getBrowserType() == "nav" || getBrowserType() == "gecko")
				selObj.options[j] = null;
			}
		}
	}
}

