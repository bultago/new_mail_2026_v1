<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>

<div class="TM_content_wrapper">
	<div class="address_subContent">
		<div id="basic_header" class="bar_off" onclick="toggleMenu('basic_header', 'basic_info')">
			<a class="info" href="#">
				<span><tctl:msg key="addr.dialog.header.tab1.title" bundle="addr"/></span>
			</a>
		</div>
		<table id="basic_info" class="TB_cols2">
			<colgroup>
				<col width="20%"></col>
				<col></col>
			</colgroup>
			<tr>
				<th><tctl:msg key="addr.info.label.003" bundle="addr"/></th>
				<td id="vlastName">${member.lastName}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.002" bundle="addr"/></th>
				<td id="vmiddleName">${member.middleName}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.001" bundle="addr"/></th>
				<td id="vfirstName">${member.firstName}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.005" bundle="addr"/></th>
				<td id="vnickName">${member.nickName}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.004" bundle="addr"/></th>
				<td id="vmemberName">${member.memberName}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.009" bundle="addr"/></th>
				<td id="vmobileNo">${member.mobileNo}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.007" bundle="addr"/></th>
				<td id="vbirthDay">${member.birthDay}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.008" bundle="addr"/></th>
				<td id="vanniversaryDay">${member.anniversaryDay}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.006" bundle="addr"/></th>
				<td>
					<table cellspacing='0' cellpadding='0' border='0' class='treeNodeWrapperTable'>
					<tbody>
					<tr>
						<td id="vmemberEmail" class='treeNodeWrapperContents'>
						${member.memberEmail}&nbsp;
						</td>
					</tr>
					</tbody>
					</table>
				</td>
			</tr>
		</table>
		<div id="home_header" class="bar_off" onclick="toggleMenu('home_header', 'home_info')">
			<a class="info" href="#">
				<span><tctl:msg key="addr.dialog.header.tab2.title" bundle="addr"/></span>
			</a>
		</div>
		<table id="home_info" class="TB_cols2">
			<colgroup>
				<col width="20%"></col>
				<col></col>
			</colgroup>
			<tr>
				<th><tctl:msg key="addr.info.label.011" bundle="addr"/></th>
				<td id="vhomePostalCode" class="content">${member.homePostalCode}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.012" bundle="addr"/></th>
				<td id="vhomeCountry">${member.homeCountry}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.013" bundle="addr"/></th>
				<td id="vhomeState">${member.homeState}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.014" bundle="addr"/></th>
				<td id="vhomeCity">${member.homeCity}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.015" bundle="addr"/></th>
				<td id="vhomeStreet">${member.homeStreet}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.016" bundle="addr"/></th>
				<td id="vhomeExtAddress" class="content">${member.homeExtAddress}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.017" bundle="addr"/></th>
				<td id="vhomeTel">${member.homeTel}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.018" bundle="addr"/></th>
				<td id="vhomeFax">${member.homeFax}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.019" bundle="addr"/></th>
				<td id="vprivateHomepage" class="content">${member.privateHomepage}&nbsp;</td>
			</tr>
		</table>
		<div id="office_header" class="bar_off" onclick="toggleMenu('office_header', 'office_info')">
			<a class="info" href="#">
				<span><tctl:msg key="addr.dialog.header.tab3.title" bundle="addr"/></span>
			</a>
		</div>
		<table id="office_info" class="TB_cols2">
			<colgroup>
				<col width="20%"></col>
				<col></col>
			</colgroup>
			<tr>
				<th><tctl:msg key="addr.info.label.031" bundle="addr"/></th>
				<td id="vcompanyName" class="content">${member.companyName}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.032" bundle="addr"/></th>
				<td id="vdepartmentName">${member.departmentName}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.033" bundle="addr"/></th>
				<td id="vtitleName">${member.titleName}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.021" bundle="addr"/></th>
				<td id="vofficePostalCode" class="content">${member.officePostalCode}&nbsp;</td>
			</tr>
			
			<tr>
				<th><tctl:msg key="addr.info.label.022" bundle="addr"/></th>
				<td id="vofficeCountry">${member.officeCountry}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.023" bundle="addr"/></th>
				<td id="vofficeState">${member.officeState}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.024" bundle="addr"/></th>
				<td id="vofficeCity">${member.officeCity}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.025" bundle="addr"/></th>
				<td id="vofficeStreet">${member.officeStreet}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.026" bundle="addr"/></th>
				<td id="vofficeExtAddress" class="content">${member.officeExtAddress}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.027" bundle="addr"/></th>
				<td id="vofficeTel">${member.officeTel}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.028" bundle="addr"/></th>
				<td id="vofficeFax">${member.officeFax}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.029" bundle="addr"/></th>
				<td id="vofficeHomepage" class="content">${member.officeHomepage}&nbsp;</td>
			</tr>
		</table>
	</div>
</div>

<script type="text/javascript">	
toggleMenu('home_info');
toggleMenu('office_info');

function toggleMenu(header, id) {
	jQuery("#"+id).toggle();

	if(jQuery("#"+header).hasClass("bar_on")){
		jQuery("#"+header).removeClass("bar_on");
		jQuery("#"+header).addClass("bar_off");
	}else{
		jQuery("#"+header).removeClass("bar_off");
		jQuery("#"+header).addClass("bar_on");
	}	
	
}
</script>