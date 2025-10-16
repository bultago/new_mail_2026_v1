<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
	<%@include file="/hybrid/common/header.jsp"%>
	<script type="text/javascript">
	pageCategory = "addAddrForm";
	function toggleView(){
		var detailView = document.getElementById("detailView");

		if(detailView.style.display == "none"){
			detailView.style.display = "block";
		} else {
			detailView.style.display = "none";
		}
	}

	function resetForm(){
		var f = document.addrSaveForm;
		f.reset();
	}

	function saveMember(){
		var addType = "${fn:escapeXml(addType)}";
		var f = document.addrSaveForm;		
		if(!isValidAddressInfo()){
			return false;
		}

		f.action = (addType == "add")?"/hybrid/addr/addAddrSave.do":"/hybrid/addr/updateAddrSave.do";
		f.submit();		
	}

	function isValidAddressInfo(){
		var f = document.addrSaveForm;
		
		if(!checkInputLength("", f.mName, "<tctl:msg key="addr.info.msg.006" bundle="addr"/>", 2, 255)) {
			f.mName.focus();
			return false;
		}
		if(!checkInputValidate("", f.mName, "userName")) {
			f.mName.focus();
			return false;
		}
		
		if (trim(f.mEmail.value) == "") {
			alert("<tctl:msg key="addr.info.msg.002" bundle="addr"/>");
			f.mEmail.focus();
			return false;
		}
		
		if(!isEmail(f.mEmail.value)){
			alert("<tctl:msg key="addr.info.msg.003" bundle="addr"/>");
			f.mEmail.focus();
			return false;
		}

		if(!checkInputLength("", f.mMobileNo, "", 0, 32)) {
			f.mMobileNo.focus();
			return false;
		}
		if(!checkInputValidate("", f.mMobileNo, "onlyBack")) {
			f.mMobileNo.focus();
			return false;
		}

		if(!checkInputLength("", f.mCompanyName, "", 0, 64)) {
			f.mCompanyName.focus();
			return false;
		}
		if(!checkInputValidate("", f.mCompanyName, "onlyBack")) {
			f.mCompanyName.focus();
			return false;
		}

		if(!checkInputLength("", f.mDepartmentName, "", 0, 64)) {
			f.mDepartmentName.focus();
			return false;
		}
		if(!checkInputValidate("", f.mDepartmentName, "onlyBack")) {
			f.mDepartmentName.focus();
			return false;
		}

		if(!checkInputLength("", f.mTitleName, "", 0, 64)) {
			f.mTitleName.focus();
			return false;
		}
		if(!checkInputValidate("", f.mTitleName, "onlyBack")) {
			f.mTitleName.focus();
			return false;
		}

		if(!checkInputLength("", f.mHomeTel, "", 0, 32)) {
			f.mHomeTel.focus();
			return false;
		}
		if(!checkInputValidate("", f.mHomeTel, "onlyBack")) {
			f.mHomeTel.focus();
			return false;
		}

		if(!checkInputLength("", f.mHomeTel, "", 0, 32)) {
			f.mHomeTel.focus();
			return false;
		}
		if(!checkInputValidate("", f.mHomeTel, "onlyBack")) {
			f.mHomeTel.focus();
			return false;
		}

		if(!checkInputLength("", f.mOfficeTel, "", 0, 32)) {
			f.mOfficeTel.focus();
			return false;
		}
		if(!checkInputValidate("", f.mOfficeTel, "onlyBack")) {
			f.mOfficeTel.focus();
			return false;
		}
		
		if(!checkInputLength("", f.mBirthDay, "", 0, 16)) {
			f.mBirthDay.focus();
			return false;
		}
		if(!checkInputValidate("", f.mBirthDay, "onlyBack")) {
			f.mBirthDay.focus();
			return false;
		}
		
		if(!checkInputLength("", f.mAnniversaryDay, "", 0, 16)) {
			f.mAnniversaryDay.focus();
			return false;
		}
		if(!checkInputValidate("",  f.mAnniversaryDay, "onlyBack")) {
			f.mAnniversaryDay.focus();
			return false;
		}

		if(trim(f.mPostalCode1.value) != "" && trim(f.mPostalCode2.value) != ""){
			var mPostalCode = trim(f.mPostalCode1.value) + "-" + trim(f.mPostalCode2.value);
			if(!isZipCode(mPostalCode)){
				alert("<tctl:msg key="addr.info.msg.038" bundle="addr"/>");
				f.mPostalCode1.focus();
				return false;
			}
			f.mPostalCode.value = mPostalCode;
		}		
				
		if(!checkInputLength("", f.mHomeCountry, "", 0, 64)) {
			f.mHomeCountry.focus();
			return false;
		}
		if(!checkInputValidate("", f.mHomeCountry, "case4")) {
			f.mHomeCountry.focus();
			return false;
		}

		if(!checkInputLength("", f.mHomeState, "", 0, 64)) {
			f.mHomeState.focus();
			return false;
		}
		if(!checkInputValidate("", f.mHomeState, "case4")) {
			f.mHomeState.focus();
			return false;
		}
		
		if(!checkInputLength("", f.mHomeCity, "", 0, 32)) {
			f.mHomeCity.focus();
			return false;
		}
		if(!checkInputValidate("", f.mHomeCity, "case4")) {
			f.mHomeCity.focus();
			return false;
		}
		
		if(!checkInputLength("", f.mHomeStreet, "", 0, 64)) {
			f.mHomeStreet.focus();
			return false;
		}
		if(!checkInputValidate("", f.mHomeStreet, "case4")) {
			f.mHomeStreet.focus();
			return false;
		}

		if(!checkInputLength("", f.mHomeExtAddress, "", 0, 128)) {
			f.mHomeExtAddress.focus();
			return false;
		}
		if(!checkInputValidate("", f.mHomeExtAddress, "onlyBack")) {
			f.mHomeExtAddress.focus();
			return false;
		}	
		
		if(!checkInputLength("", f.mHomeFax, "", 0, 32)) {
			f.mHomeFax.focus();
			return false;
		}
		if(!checkInputValidate("", f.mHomeFax, "onlyBack")) {
			f.mHomeFax.focus();
			return false;
		}
		
		if(!checkInputLength("", f.mPrivateHomepage, "", 0, 255)) {
			f.mPrivateHomepage.focus();
			return false;
		}
		if(!checkInputValidate("", f.mPrivateHomepage, "onlyBack")) {
			f.mPrivateHomepage.focus();
			return false;
		}


		if(trim(f.mOfficePostalCode1.value) != "" && trim(f.mOfficePostalCode2.value) != ""){
			var mPostalCode = trim(f.mOfficePostalCode1.value) + "-" + trim(f.mOfficePostalCode2.value);
			if(!isZipCode(mPostalCode)){
				alert("<tctl:msg key="addr.info.msg.038" bundle="addr"/>");
				f.mOfficePostalCode1.focus();
				return false;
			}
			f.mOfficePostalCode.value = mPostalCode;
		}
		
		
		if(!checkInputLength("", f.mOfficeCountry, "", 0, 64)) {
			f.mOfficeCountry.focus();
			return false;
		}
		if(!checkInputValidate("", f.mOfficeCountry, "case4")) {
			f.mOfficeCountry.focus();
			return false;
		}

		if(!checkInputLength("", f.mOfficeState, "", 0, 64)) {
			f.mOfficeState.focus();
			return false;
		}
		if(!checkInputValidate("", f.mOfficeState, "case4")) {
			f.mOfficeState.focus();
			return false;
		}
		
		if(!checkInputLength("", f.mOfficeCity, "", 0, 32)) {
			f.mOfficeCity.focus();
			return false;
		}
		if(!checkInputValidate("", f.mOfficeCity, "case4")) {
			f.mOfficeCity.focus();
			return false;
		}

		if(!checkInputLength("", f.mOfficeStreet, "", 0, 64)) {
			f.mOfficeStreet.focus();
			return false;
		}
		if(!checkInputValidate("", f.mOfficeStreet, "case4")) {
			f.mOfficeStreet.focus();
			return false;
		}
		
		if(!checkInputLength("", f.mOfficeExtAddress, "", 0, 128)) {
			f.mOfficeExtAddress.focus();
			return false;
		}
		if(!checkInputValidate("", f.mOfficeExtAddress, "onlyBack")) {
			f.mOfficeExtAddress.focus();
			return false;
		}
		
		if(!checkInputLength("", f.mOfficeFax, "", 0, 32)) {
			f.mOfficeFax.focus();
			return false;
		}
		if(!checkInputValidate("", f.mOfficeFax, "onlyBack")) {
			f.mOfficeFax.focus();
			return false;
		}
		
		if(!checkInputLength("", f.mOfficeHomepage, "", 0, 255)) {
			f.mOfficeHomepage.focus();
			return false;
		}
		if(!checkInputValidate("", f.mOfficeHomepage, "onlyBack")) {
			f.mOfficeHomepage.focus();
			return false;
		}
		
		return true;
	}
	
	function nativeGoBack(){
		
		var name = jQuery("#mName");
		var email = jQuery("#mEmail");
		
		if(name.val() != "" || email.val() != ""){
			if(!confirm(mailMsg.confirm_addressEscapewrite)){
				return;
			}
		}
		eval("window.TMSMobile.goBack('true')");
	}
	</script>
	
</head>

<body>
	<div class="m_tms_wrap">
			<%@include file="/hybrid/addrbook/addr_body_top.jsp"%>		
			
			<div class="container">
			<form name="addrSaveForm" method="post" onSubmit="return saveMember()">
			<input type="hidden" name="addrType" id="addrType" value="${fn:escapeXml(addrType)}" />
			<input type="hidden" name="page" id="page" value="${fn:escapeXml(page)}" />
			<input type="hidden" name="bookSeq" id="bookSeq" value="${fn:escapeXml(bookSeq)}" />
			<input type="hidden" name="groupSeq" id="groupSeq" value="${fn:escapeXml(groupSeq)}" />
			<input type="hidden" name="keyWord" id="keyWord" value="${fn:escapeXml(keyWord)}" />
			<input type="hidden" name="memberSeq" id="memberSeq" value="${member.memberSeq}" />
			<!-- btn area -->
			<div class="title_box">
				<div class="btn_l"><a href="javascript:resetForm();" class="btn2"><span><tctl:msg key="comn.cancel" bundle="common"/></span></a></div>
				<div class="btn_r"><a href="javascript:saveMember();" class="btn2"><span><tctl:msg key="common.20" bundle="setting"/></span></a></div>
			</div>
			<!-- form-->
			<div class="write_wrap">
				<table class="write_form">
					<tbody>
					<tr class="space"><th></th><td></td></tr>
					<tr>
						<th>* <tctl:msg key="addr.info.label.004" bundle="addr"/></th>
						<td><input type="text" name="mName" id="mName" class="it_full" value="${fn:escapeXml(member.memberName)}"/></td>
					</tr>
					<tr>
						<th>* <tctl:msg key="addr.info.label.006" bundle="addr"/></th>
						<td>
							<input type="text" name="mEmail" id="mEmail" class="it_full" value="${fn:escapeXml(member.memberEmail)}"/>							
						</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.009" bundle="addr"/></th>
						<td>
							<input type="text" name="mMobileNo" id="mMobileNo" class="it_full" value="${fn:escapeXml(member.mobileNo)}"/>
						</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.031" bundle="addr"/></th>
						<td><input type="text" name="mCompanyName" id="mCompanyName" class="it_full" value="${fn:escapeXml(member.companyName)}" /></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.032" bundle="addr"/></th>
						<td><input type="text" name="mDepartmentName" id="mDepartmentName" class="it_full" value="${fn:escapeXml(member.departmentName)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.033" bundle="addr"/></th>
						<td><input type="text" name="mTitleName" id="mTitleName" class="it_full" value="${fn:escapeXml(member.titleName)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.outlookExpress.012" bundle="addr"/></th>
						<td>
							<input type="text" name="mHomeTel" id="mHomeTel" class="it_full" value="${fn:escapeXml(member.homeTel)}"/>							
						</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.outlookExpress.022" bundle="addr"/></th>
						<td>
							<input type="text" name="mOfficeTel" id="mOfficeTel" class="it_full" value="${fn:escapeXml(member.officeTel)}"/>							
						</td>
					</tr>					
					</tbody>
				</table>
				<div class="info_split"><a href="javascript:toggleView();" class="btn4 reserv_search"><span><tctl:msg key="addr.dialog.view.title" bundle="addr"/></span></a></div>
				<div id="detailView" style="display:none">
				<table class="write_form">
					<tbody>
					<tr>
						<th><tctl:msg key="addr.info.label.007" bundle="addr"/></th>
						<td><input type="text" name="mBirthDay" id="mBirthDay" class="it_full" value="${fn:escapeXml(member.birthDay)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.008" bundle="addr"/></th>
						<td><input type="text" name="mAnniversaryDay" id="mAnniversaryDay" class="it_full" value="${fn:escapeXml(member.anniversaryDay)}"/></td>
					</tr>
					<tr class="space"><th></th><td></td></tr>
					<tr>						
						<td colspan="2" class="title"><tctl:msg key="addr.dialog.header.tab2.title" bundle="addr"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.021" bundle="addr"/></th>
						<td>
							<table class="tb_date">
								<tbody>
								<tr>
									<td style="text-align:center; padding:0px;"><input type="text" name="mPostalCode1" id="mPostalCode1" maxlength="3" class="it_full" value="${fn:split(fn:escapeXml(member.homePostalCode),'-')[0]}"/></td>
									<td class="btn_calendar_wrap">~</td>
									<td style="text-align:center; padding:0px;"><input type="text" name="mPostalCode2" id="mPostalCode2" maxlength="4" class="it_full" value="${fn:split(fn:escapeXml(member.homePostalCode),'-')[1]}"/></td>
									<input type="hidden" name="mPostalCode" id="mPostalCode" value="${fn:escapeXml(member.homePostalCode)}"/>
								</tr>
								</tbody>
							</table>
						</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.022" bundle="addr"/></th>
						<td><input type="text" name="mHomeCountry" id="mHomeCountry" class="it_full" value="${fn:escapeXml(member.homeCountry)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.023" bundle="addr"/></th>
						<td><input type="text" name="mHomeCity" id="mHomeCity" class="it_full" value="${fn:escapeXml(member.homeCity)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.024" bundle="addr"/></th>
						<td><input type="text" name="mHomeState" id="mHomeState" class="it_full" value="${fn:escapeXml(member.homeState)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.025" bundle="addr"/></th>
						<td><input type="text" name="mHomeStreet" id="mHomeStreet" class="it_full" value="${fn:escapeXml(member.homeStreet)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.026" bundle="addr"/></th>
						<td><input type="text" name="mHomeExtAddress" id="mHomeExtAddress" class="it_full" value="${fn:escapeXml(member.homeExtAddress)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.018" bundle="addr"/></th>
						<td><input type="text" name="mHomeFax" id="mHomeFax" class="it_full" value="${fn:escapeXml(member.homeFax)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.029" bundle="addr"/></th>
						<td>
							<input type="text" name="mPrivateHomepage" id="mPrivateHomepage" class="it_full" value="${fn:escapeXml(member.privateHomepage)}"/>							
						</td>
					</tr>
					<tr class="space"><th></th><td></td></tr>
					<tr>						
						<td colspan="2" class="title"><tctl:msg key="addr.dialog.header.tab3.title" bundle="addr"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.021" bundle="addr"/></th>
						<td>
							<table class="tb_date">
								<tbody>
								<tr>
									<td style="text-align:center; padding:0px;"><input type="text" name="mOfficePostalCode1" id="mOfficePostalCode1" maxlength="3" class="it_full" value="${fn:split(fn:escapeXml(member.officePostalCode),'-')[0]}"/></td>
									<td class="btn_calendar_wrap">~</td>
									<td style="text-align:center; padding:0px;"><input type="text" name="mOfficePostalCode2" id="mOfficePostalCode2" maxlength="4" class="it_full" value="${fn:split(fn:escapeXml(member.officePostalCode),'-')[1]}"/></td>
									<input type="hidden" name="mOfficePostalCode" id="mOfficePostalCode" value="${fn:escapeXml(member.officePostalCode)}"/>
								</tr>
								</tbody>
							</table>
						</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.022" bundle="addr"/></th>
						<td><input type="text" name="mOfficeCountry" id="mOfficeCountry" class="it_full" value="${fn:escapeXml(member.officeCountry)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.023" bundle="addr"/></th>
						<td><input type="text" name="mOfficeCity" id="mOfficeCity" class="it_full" value="${fn:escapeXml(member.officeCity)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.024" bundle="addr"/></th>
						<td><input type="text" name="mOfficeState" id="mOfficeState" class="it_full" value="${fn:escapeXml(member.officeState)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.025" bundle="addr"/></th>
						<td><input type="text" name="mOfficeStreet" id="mOfficeStreet" class="it_full" value="${fn:escapeXml(member.officeStreet)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.026" bundle="addr"/></th>
						<td><input type="text" name="mOfficeExtAddress" id="mOfficeExtAddress" class="it_full" value="${fn:escapeXml(member.officeExtAddress)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.018" bundle="addr"/></th>
						<td><input type="text" name="mOfficeFax" id="mOfficeFax" class="it_full" value="${fn:escapeXml(member.officeFax)}"/></td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.029" bundle="addr"/></th>
						<td>
							<input type="text" name="mOfficeHomepage" id="mOfficeHomepage" class="it_full" value="http://${fn:escapeXml(member.officeHomepage)}"/>							
						</td>
					</tr>
					<tr class="space"><th></th><td></td></tr>
					</tbody>
				</table>
				
				<div class="title_box title_box_bottom">
					<div class="btn_l"><a href="javascript:resetForm();" class="btn2"><span><tctl:msg key="comn.cancel" bundle="common"/></span></a></div>
					<div class="btn_r"><a href="javascript:saveMember();" class="btn2"><span><tctl:msg key="common.20" bundle="setting"/></span></a></div>
				</div>
				</div>	
			</div>
			</form>
			
		</div>
		<%@include file="/hybrid/common/footer.jsp"%>
		
	</div>
</body>
</html>
