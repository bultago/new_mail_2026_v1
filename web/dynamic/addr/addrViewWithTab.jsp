<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>

<div id="viewAddress" class="TM_addrview_content">
	<div class="addView_tab">
		<a id="basic_header1" class="btn_tabmenu2_on" href="#" onclick="toggleTab2('basic_header', 'viewAddress-', 1, 2, 3)"><span><tctl:msg key="addr.dialog.header.tab1.title" bundle="addr"/></span></a>
		<a id="basic_header2" class="btn_tabmenu2_off" href="#" onclick="toggleTab2('basic_header', 'viewAddress-', 2, 1, 3)"><span><tctl:msg key="addr.dialog.header.tab2.title" bundle="addr"/></span></a>
		<a id="basic_header3" class="btn_tabmenu2_off" href="#" onclick="toggleTab2('basic_header', 'viewAddress-', 3, 1, 2)"><span><tctl:msg key="addr.dialog.header.tab3.title" bundle="addr"/></span></a>
	</div>
    <div id="viewAddress-1" >
    	<table cellpadding="0" cellspacing="0" id="basic_info" class="TB_cols2">
			<colgroup span="2">
				<col width="130px"></col>
				<col></col>
			</colgroup>
			<tr>
				<th><tctl:msg key="addr.info.label.003" bundle="addr"/></th>
				<td id="vlastName">${member.lastName}&nbsp;</td>
				<th><tctl:msg key="addr.info.label.002" bundle="addr"/></th>
				<td id="vmiddleName">${member.middleName}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.001" bundle="addr"/></th>
				<td id="vfirstName">${member.firstName}&nbsp;</td>
				<th><tctl:msg key="addr.info.label.005" bundle="addr"/></th>
				<td id="vnickName">${member.nickName}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.004" bundle="addr"/></th>
				<td id="vmemberName">${member.memberName}&nbsp;</td>
				<th><tctl:msg key="addr.info.label.009" bundle="addr"/></th>
				<td id="vmobileNo">${member.mobileNo}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.007" bundle="addr"/></th>
				<td id="vbirthDay">${member.birthDay}&nbsp;</td>
				<th><tctl:msg key="addr.info.label.008" bundle="addr"/></th>
				<td id="vanniversaryDay">${member.anniversaryDay}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.006" bundle="addr"/></th>
				<td colspan="3">
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
    </div>
    <div id="viewAddress-2"  style="display: none">
    	<table cellpadding="0" cellspacing="0" id="home_info" class="TB_cols2">
			<colgroup span="4">
				<col width="130px"></col>
				<col width="50%"></col>
				<col width="130px"></col>
				<col width="50%"></col>
			</colgroup>
			<tr>
				<th><tctl:msg key="addr.info.label.011" bundle="addr"/></th>
				<td id="vhomePostalCode" colspan="3" class="content">${member.homePostalCode}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.012" bundle="addr"/></th>
				<td id="vhomeCountry">${member.homeCountry}&nbsp;</td>
				<th><tctl:msg key="addr.info.label.013" bundle="addr"/></th>
				<td id="vhomeState">${member.homeState}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.014" bundle="addr"/></th>
				<td id="vhomeCity">${member.homeCity}&nbsp;</td>
				<th><tctl:msg key="addr.info.label.015" bundle="addr"/></th>
				<td id="vhomeStreet">${member.homeStreet}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.016" bundle="addr"/></th>
				<td id="vhomeExtAddress" colspan="3" class="content">${member.homeExtAddress}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.017" bundle="addr"/></th>
				<td id="vhomeTel">${member.homeTel}&nbsp;</td>
				<th><tctl:msg key="addr.info.label.018" bundle="addr"/></th>
				<td id="vhomeFax">${member.homeFax}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.019" bundle="addr"/></th>
				<td id="vprivateHomepage" colspan="3" class="content">${member.privateHomepage}&nbsp;</td>
			</tr>
		</table>
    </div>
    <div id="viewAddress-3"  style="display: none">
    	<table cellpadding="0" cellspacing="0" id="office_info" class="TB_cols2">
			<colgroup span="2">
				<col width="130px"></col>
				<col width="50%"></col>
				<col width="130px"></col>
				<col width="50%"></col>
			</colgroup>
			<tr>
				<th><tctl:msg key="addr.info.label.031" bundle="addr"/></th>
				<td id="vcompanyName" colspan="3" class="content">${member.companyName}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.032" bundle="addr"/></th>
				<td id="vdepartmentName">${member.departmentName}&nbsp;</td>
				<th><tctl:msg key="addr.info.label.033" bundle="addr"/></th>
				<td id="vtitleName">${member.titleName}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.021" bundle="addr"/></th>
				<td id="vofficePostalCode" colspan="3" class="content">${member.officePostalCode}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.022" bundle="addr"/></th>
				<td id="vofficeCountry">${member.officeCountry}&nbsp;</td>
				<th><tctl:msg key="addr.info.label.023" bundle="addr"/></th>
				<td id="vofficeState">${member.officeState}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.024" bundle="addr"/></th>
				<td id="vofficeCity">${member.officeCity}&nbsp;</td>
				<th><tctl:msg key="addr.info.label.025" bundle="addr"/></th>
				<td id="vofficeStreet">${member.officeStreet}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.026" bundle="addr"/></th>
				<td id="vofficeExtAddress" colspan="3" class="content">${member.officeExtAddress}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.027" bundle="addr"/></th>
				<td id="vofficeTel">${member.officeTel}&nbsp;</td>
				<th><tctl:msg key="addr.info.label.028" bundle="addr"/></th>
				<td id="vofficeFax">${member.officeFax}&nbsp;</td>
			</tr>
			<tr>
				<th><tctl:msg key="addr.info.label.029" bundle="addr"/></th>
				<td id="vofficeHomepage" colspan="3" class="content">${member.officeHomepage}&nbsp;</td>
			</tr>
		</table>
    </div>
</div>

<script type="text/javascript">
jQuery(window).autoResize(jQuery("#addrViewWrapper"),"#copyRight");
</script>
