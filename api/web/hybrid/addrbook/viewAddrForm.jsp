<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
	<%@include file="/hybrid/common/header.jsp"%>
	<script type="text/javascript">

	function toggleView(){
		var detailView = document.getElementById("detailView");

		if(detailView.style.display == "none"){
			detailView.style.display = "block";
		} else {
			detailView.style.display = "none";
		}
	}

	function goBack(){
		history.back();
	}

	function modifyMember(){
		var url="/hybrid/addr/writeAddrView.do";
		var f = document.addrForm;

		var paramArray = [];
		paramArray.push({name:"addType", value:"modify"});
		paramArray.push({name:"mseq", value:"${member.memberSeq}"});
		paramArray.push({name:"page", value:"${fn:escapeXml(page)}"});
		paramArray.push({name:"bookSeq", value:"${fn:escapeXml(bookSeq)}"});
		paramArray.push({name:"groupSeq", value:"${fn:escapeXml(groupSeq)}"});
		paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
		paramArray.push({name:"addrType", value:"${fn:escapeXml(addrType)}"});
		executeUrl(url, paramArray);
	}

	function deleteAddr(){
		var url="/hybrid/addr/deleteAddr.do";

		var paramArray = [];
		paramArray.push({name:"page", value:"${fn:escapeXml(page)}"});
		paramArray.push({name:"bookSeq", value:"${fn:escapeXml(bookSeq)}"});
		paramArray.push({name:"groupSeq", value:"${fn:escapeXml(groupSeq)}"});
		paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
		paramArray.push({name:"mseqs", value:${member.memberSeq}});
		paramArray.push({name:"addrType", value:"${fn:escapeXml(addrType)}"});
		executeUrl(url, paramArray);
	}

	function writeEmail(name,address){
		var url="/hybrid/mail/mailWrite.do";
		var paramArray = [];
		var addrStr = (name !="" )?"\""+name+"\"<"+address+">":address;
		paramArray.push({name:"to", value:doubleUrlEncode(addrStr)});
		executeUrl(url, paramArray);
	}
	
	function viewUrl(url){
		
		if(checkOS() == "android"){
			eval("window.TMSMobile.attachView('"+url+"')");
		}else{
			window.location = "tmsmobile://attachView?"+url;
		}
	}
	</script>
</head>

<body>
	<div class="m_tms_wrap">
			<%@include file="/hybrid/addrbook/addr_body_top.jsp"%>
			
			<div class="container">
			<!-- btn area -->
			<div class="title_box">				
				<div class="btn_l">
					<a href="javascript:goBack();" class="btn2"><span><tctl:msg key="comn.prelist" bundle="common"/></span></a>
					<c:if test="${authority.writeAuth eq 'Y'}">
					<a href="javascript:deleteAddr();" class="btn2"><span><tctl:msg key="comn.del" bundle="common"/></span></a>
					</c:if>
				</div>
				<div class="btn_r">
					<c:if test="${authority.writeAuth eq 'Y'}">
					<a href="javascript:modifyMember();" class="btn2"><span><tctl:msg key="comn.modfy" bundle="common"/></span></a>
					</c:if>
				</div>
			</div>
			<!-- form-->
			<div class="write_wrap">
				<table class="write_form">					
					<tbody>
					<tr class="space"><th></th><td></td></tr>
					<tr>
						<th>* <tctl:msg key="addr.info.label.004" bundle="addr"/></th>
						<td>${fn:escapeXml(member.memberName)}</td>
					</tr>
					<tr>
						<th>* <tctl:msg key="addr.info.label.006" bundle="addr"/></th>
						<td>
							<div class="text_over">
							${fn:escapeXml(member.memberEmail)}
							</div>
							<div class="ic_area">
								<a href="javascript:writeEmail('${fn:escapeXml(member.memberName)}','${fn:escapeXml(member.memberEmail)}')" class="btn_mail" alt="mail">send mail</a>
							</div>
						</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.009" bundle="addr"/></th>
						<td>
							${fn:escapeXml(member.mobileNo)}
							<c:if test="${not empty member.mobileNo}">
							<div class="ic_area">
								<a href="tel:${fn:escapeXml(member.mobileNo)}" class="btn_phone" alt="phone">call</a>
							</div>
							</c:if>
						</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.031" bundle="addr"/></th>
						<td>${fn:escapeXml(member.companyName)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.032" bundle="addr"/></th>
						<td>${fn:escapeXml(member.departmentName)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.033" bundle="addr"/></th>
						<td>${fn:escapeXml(member.titleName)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.outlookExpress.012" bundle="addr"/></th>
						<td>
							${fn:escapeXml(member.homeTel)}
							<c:if test="${not empty member.homeTel}">
							<div class="ic_area">
								<a href="tel:${fn:escapeXml(member.homeTel)}" class="btn_phone" alt="phone">call</a>
							</div>
							</c:if>
						</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.outlookExpress.022" bundle="addr"/></th>
						<td>
							${fn:escapeXml(member.officeTel)}
							<c:if test="${not empty member.officeTel}">
							<div class="ic_area">
								<a href="tel:${fn:escapeXml(member.officeTel)}" class="btn_phone" alt="phone">call</a>
							</div>
							</c:if>
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
						<td>${fn:escapeXml(member.birthDay)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.008" bundle="addr"/></th>
						<td>${fn:escapeXml(member.anniversaryDay)}</td>
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
									<td style="text-align:center;padding:0px;">${fn:escapeXml(fn:split(member.homePostalCode,'-')[0]}</td>
									<td class="btn_calendar_wrap">~</td>
									<td style="text-align:center;padding:0px;">${fn:split(member.homePostalCode,'-')[1]}</td>
								</tr>
								</tbody>
							</table>
						</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.022" bundle="addr"/></th>
						<td>${fn:escapeXml(member.homeCountry)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.023" bundle="addr"/></th>
						<td>${fn:escapeXml(member.homeCity)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.024" bundle="addr"/></th>
						<td>${fn:escapeXml(member.homeState)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.025" bundle="addr"/></th>
						<td>${fn:escapeXml(member.homeStreet)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.026" bundle="addr"/></th>
						<td>${fn:escapeXml(member.homeExtAddress)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.018" bundle="addr"/></th>
						<td>${fn:escapeXml(member.homeFax)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.029" bundle="addr"/></th>
						<td>
							<c:if test="${not empty member.privateHomepage}">
							<a href="${fn:escapeXml(member.privateHomepage)}" target="_blank">${fn:escapeXml(member.privateHomepage)}</a>
							</c:if>
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
									<td style="text-align:center;padding:0px;">${fn:split(fn:escapeXml(member.officePostalCode),'-')[0]}</td>
									<td class="btn_calendar_wrap">~</td>
									<td style="text-align:center;padding:0px;">${fn:split(fn:escapeXml(member.officePostalCode),'-')[1]}</td>
								</tr>
								</tbody>
							</table>
						</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.022" bundle="addr"/></th>
						<td>${fn:escapeXml(member.officeCountry)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.023" bundle="addr"/></th>
						<td>${fn:escapeXml(member.officeCity)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.024" bundle="addr"/></th>
						<td>${fn:escapeXml(member.officeState)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.025" bundle="addr"/></th>
						<td>${fn:escapeXml(member.officeStreet)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.026" bundle="addr"/></th>
						<td>${fn:escapeXml(member.officeExtAddress)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.018" bundle="addr"/></th>
						<td>${fn:escapeXml(member.officeFax)}</td>
					</tr>
					<tr>
						<th><tctl:msg key="addr.info.label.029" bundle="addr"/></th>
						<td>
							<div class="text_over">
							<c:if test="${not empty member.officeHomepage}">
							<a href="javascript:viewUrl('${fn:escapeXml(member.officeHomepage)}')">${fn:escapeXml(member.officeHomepage)}</a>
							</c:if>
							</div>
						</td>
					</tr>
					<tr class="space"><th></th><td></td></tr>
					</tbody>
				</table>
				
				<div class="title_box title_box_bottom">				
					<div class="btn_l">
						<a href="javascript:goBack();" class="btn2"><span><tctl:msg key="comn.prelist" bundle="common"/></span></a>
						<c:if test="${authority.writeAuth eq 'Y'}">
						<a href="javascript:deleteAddr();" class="btn2"><span><tctl:msg key="comn.del" bundle="common"/></span></a>
						</c:if>
					</div>
					<div class="btn_r">
						<c:if test="${authority.writeAuth eq 'Y'}">
						<a href="javascript:modifyMember();" class="btn2"><span><tctl:msg key="comn.modfy" bundle="common"/></span></a>
						</c:if>
					</div>
				</div>
				</div>	
			</div>
		</div>
		<%@include file="/hybrid/common/footer.jsp"%>
	</div>
</body>
</html>
