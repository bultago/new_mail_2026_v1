package com.terracetech.tims.webmail.addrbook.manager.vendors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.exception.InvalidFileException;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.Validation;

public class TerraceMail implements IEmailVendor{

	private I18nResources resource = null;
	
	public TerraceMail(I18nResources resource) {
		this.resource = resource;
	}
	
	public StringBuffer getAddrCSVDownload(List<AddressBookMemberVO> list) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(resource.getMessage("addr.terrace.001") + ",");
		sb.append(resource.getMessage("addr.terrace.002") + ",");
		sb.append(resource.getMessage("addr.terrace.003") + ",");
		sb.append(resource.getMessage("addr.terrace.004") + ",");
		sb.append(resource.getMessage("addr.terrace.005") + ",");
		sb.append(resource.getMessage("addr.terrace.006") + ",");
		sb.append(resource.getMessage("addr.terrace.007") + ",");
		sb.append(resource.getMessage("addr.terrace.008") + ",");
		sb.append(resource.getMessage("addr.terrace.009") + ",");
		sb.append(resource.getMessage("addr.terrace.010") + ",");
		sb.append(resource.getMessage("addr.terrace.011") + ",");
		sb.append(resource.getMessage("addr.terrace.012") + ",");
		sb.append(resource.getMessage("addr.terrace.013") + ",");
		sb.append(resource.getMessage("addr.terrace.014") + ",");
		sb.append(resource.getMessage("addr.terrace.015") + ",");
		sb.append(resource.getMessage("addr.terrace.016") + ",");
		sb.append(resource.getMessage("addr.terrace.017") + ",");
		sb.append(resource.getMessage("addr.terrace.018") + ",");
		sb.append(resource.getMessage("addr.terrace.019") + ",");
		sb.append(resource.getMessage("addr.terrace.020") + ",");
		sb.append(resource.getMessage("addr.terrace.021") + ",");
		sb.append(resource.getMessage("addr.terrace.022") + ",");
		sb.append(resource.getMessage("addr.terrace.023") + ",");
		sb.append(resource.getMessage("addr.terrace.024") + ",");
		sb.append(resource.getMessage("addr.terrace.025") + ",");
		sb.append(resource.getMessage("addr.terrace.026") + ",");
		sb.append(resource.getMessage("addr.terrace.027") + ",");
		sb.append(resource.getMessage("addr.terrace.028") + ",");
		sb.append(resource.getMessage("addr.terrace.029") + ",");
		sb.append(resource.getMessage("addr.terrace.030") + ",");
		sb.append("\r\n");

		String dummy = "";
		for (AddressBookMemberVO member : list) {
		        sb.append(StringUtils.replaceString(member.getLastName(), dummy) + ",");
		        sb.append(StringUtils.replaceString(member.getMiddleName(), dummy) + ",");
		        sb.append(StringUtils.replaceString(member.getFirstName(), dummy) + ",");
		        sb.append(StringUtils.replaceString(member.getNickName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getMemberName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getMobileNo(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getBirthDay(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getAnniversaryDay(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getMemberEmail(), dummy) + ",");
			
			sb.append(StringUtils.replaceString(member.getHomePostalCode(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeCountry(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeState(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeCity(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeStreet(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeExtAddress(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeTel(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeFax(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getPrivateHomepage(), dummy) + ",");
			
			sb.append(StringUtils.replaceString(member.getCompanyName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getDepartmentName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getTitleName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficePostalCode(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeCountry(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeState(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeCity(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeStreet(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeExtAddress(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeTel(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeFax(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeHomepage(), dummy));
			
			sb.append("\r\n");
		}
		return sb;
	}

	public AddressBookMemberVO[] getAddressBookMemberVO(String[] results, int userSeq) throws IOException, InvalidFileException {
		List<AddressBookMemberVO> list = new ArrayList<AddressBookMemberVO>();


		AddressBookMemberVO vo = null;
		int index = 0;
		for (int i = 0; i < results.length; i++) {
			String line = results[i];
			index = 0;
			try {
				vo = new AddressBookMemberVO();
				
				String[] info = line.split(",", -1);
				if(info.length < 9) continue;
				
				vo.setUserSeq(userSeq);
				vo.setLastName(info[index++]);
				vo.setMiddleName(info[index++]);
				vo.setFirstName(info[index++]);
				vo.setNickName(info[index++]);
				vo.setMemberName(info[index++]);
				vo.setMobileNo(info[index++]);
				vo.setBirthDay(info[index++]);
				vo.setAnniversaryDay(info[index++]);
				if (!Validation.isEmail(info[index])) {
				    continue;
				}
				vo.setMemberEmail(info[index++]);
				
				vo.setHomePostalCode((info.length > index) ? info[index++] : "");
				vo.setHomeCountry((info.length > index) ? info[index++] : "");
				vo.setHomeState((info.length > index) ? info[index++] : "");
				vo.setHomeCity((info.length > index) ? info[index++] : "");
				vo.setHomeStreet((info.length > index) ? info[index++] : "");
				vo.setHomeExtAddress((info.length > index) ? info[index++] : "");
				vo.setHomeTel((info.length > index) ? info[index++] : "");
				vo.setHomeFax((info.length > index) ? info[index++] : "");
				vo.setPrivateHomepage((info.length > index) ? info[index++] : "");
				
				vo.setCompanyName((info.length > index) ? info[index++] : "");
				vo.setDepartmentName((info.length > index) ? info[index++] : "");
				vo.setTitleName((info.length > index) ? info[index++] : "");
				vo.setOfficePostalCode((info.length > index) ? info[index++] : "");
                                vo.setOfficeCountry((info.length > index) ? info[index++] : "");
                                vo.setOfficeState((info.length > index) ? info[index++] : "");
                                vo.setOfficeCity((info.length > index) ? info[index++] : "");
                                vo.setOfficeStreet((info.length > index) ? info[index++] : "");
                                vo.setOfficeExtAddress((info.length > index) ? info[index++] : "");
                                vo.setOfficeTel((info.length > index) ? info[index++] : "");
                                vo.setOfficeFax((info.length > index) ? info[index++] : "");
                                vo.setOfficeHomepage((info.length > index) ? info[index++] : "");
				
				list.add(vo);	
			} catch (Exception e) {
				throw new InvalidFileException(e);
			}
		}

		return list.toArray(new AddressBookMemberVO[list.size()]);
	}

	
}
