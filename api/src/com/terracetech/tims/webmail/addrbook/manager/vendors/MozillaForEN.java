package com.terracetech.tims.webmail.addrbook.manager.vendors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.exception.InvalidFileException;
import com.terracetech.tims.webmail.util.StringUtils;

public class MozillaForEN implements IEmailVendor{
	private I18nResources resource = null;
	
	public MozillaForEN(I18nResources resource) {
		this.resource = resource;
	}
	public StringBuffer getAddrCSVDownload(List<AddressBookMemberVO> list) {
StringBuffer sb = new StringBuffer();
		
		sb.append(resource.getMessage("addr.outlookExpress.004") + ",");	//0.ÀüÃ¼ ÀÌ¸§
		sb.append(resource.getMessage("addr.outlookExpress.001") + ",");	//1.ÀÌ¸§
		sb.append(resource.getMessage("addr.outlookExpress.002") + ",");	//2.¼º
		sb.append(resource.getMessage("addr.outlookExpress.003") + ",");	//3.Áß°£ÀÌ¸§
		sb.append(resource.getMessage("addr.outlookExpress.005") + ",");	//4.¾ÖÄª
		sb.append(resource.getMessage("addr.outlookExpress.006") + ",");	//5.ÀüÀÚ ¸ÞÀÏ ÁÖ¼Ò
		sb.append(resource.getMessage("addr.outlookExpress.014") + ",");	//6.ÈÞ´ëÆù
		
		sb.append(resource.getMessage("addr.outlookExpress.007") + ",");	//7.ÁÖ¼Ò(Áý)
		sb.append(resource.getMessage("addr.outlookExpress.008") + ",");	//8.±¸±º½Ã(Áý)
		sb.append(resource.getMessage("addr.outlookExpress.009") + ",");	//9.¿ìÆí¹øÈ£(Áý)
		sb.append(resource.getMessage("addr.outlookExpress.010") + ",");	//10.½Ã/µµ(Áý)
		sb.append(resource.getMessage("addr.outlookExpress.011") + ",");	//11.±¹°¡(Áý)
		sb.append(resource.getMessage("addr.outlookExpress.012") + ",");	//12.Áý ÀüÈ­
		
		sb.append(resource.getMessage("addr.outlookExpress.015") + ",");	//13.°³ÀÎ À¥ ÆäÀÌÁö
		
		sb.append(resource.getMessage("addr.outlookExpress.016") + ",");	//14.ÁÖ¼Ò(È¸»ç)
		sb.append(resource.getMessage("addr.outlookExpress.017") + ",");	//15.±¸/±º/½Ã(È¸»ç)
		sb.append(resource.getMessage("addr.outlookExpress.018") + ",");	//16.¿ìÆí ¹øÈ£(È¸»ç)
		sb.append(resource.getMessage("addr.outlookExpress.019") + ",");	//17.½Ã/µµ(È¸»ç)
		sb.append(resource.getMessage("addr.outlookExpress.020") + ",");	//18.±¹°¡(È¸»ç)
		sb.append(resource.getMessage("addr.outlookExpress.022") + ",");	//19.È¸»ç ÀüÈ­
		sb.append(resource.getMessage("addr.outlookExpress.024") + ",");	//20.È¸»ç
		sb.append(resource.getMessage("addr.outlookExpress.025") + ",");	//21.Á÷ÇÔ
		sb.append(resource.getMessage("addr.outlookExpress.026") + ",");	//22.ºÎ¼­
		sb.append(resource.getMessage("addr.outlookExpress.021") + ",");	//23.È¸»ç À¥ ÆäÀÌÁö
		sb.append(resource.getMessage("addr.outlookExpress.023"));			//24.È¸»ç ÆÑ½º
		sb.append("\r\n");

		String dummy = "";
		for (AddressBookMemberVO member : list) {
			sb.append(StringUtils.replaceString(member.getMemberName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getFirstName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getLastName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getMiddleName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getNickName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getMemberEmail(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getMobileNo(), dummy) + ",");
			
			sb.append(StringUtils.replaceString(member.getHomeExtAddress(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeStreet(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomePostalCode(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeCity(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeCountry(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeTel(), dummy) + ",");
			
			sb.append(StringUtils.replaceString(member.getPrivateHomepage(), dummy) + ",");
			
			sb.append(StringUtils.replaceString(member.getOfficeExtAddress(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeStreet(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficePostalCode(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeCity(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeCountry(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeTel(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getCompanyName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getTitleName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getDepartmentName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeHomepage(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeFax(), dummy));
			
			sb.append("\r\n");
		}
		return sb;
	}

	public AddressBookMemberVO[] getAddressBookMemberVO(String[] results, int userSeq) throws IOException, InvalidFileException {
		List<AddressBookMemberVO> list = new ArrayList<AddressBookMemberVO>();

		AddressBookMemberVO vo = null;
		for (int i = 0; i < results.length; i++) {
			String line = results[i];
			try {
				vo = new AddressBookMemberVO();
				
				String[] info = line.split(",", -1);
				vo.setUserSeq(userSeq);
				vo.setMemberName(info[0]);
				if(StringUtils.isNotEmpty(info[1])){
					vo.setMemberName(vo.getMemberName() + " "+ info[1]);
				}
				if (info.length > 4)
					vo.setMemberEmail(info[4]);
				if (info.length > 10)
					vo.setMobileNo(info[10]);
				if (info.length > 11)
					vo.setHomeExtAddress(info[11]);
				if (info.length > 13)
					vo.setHomeStreet(info[13]);
				if (info.length > 15)
					vo.setHomePostalCode(info[15]);
				if (info.length > 14)
					vo.setHomeCity(info[14]);
				if (info.length > 16)
					vo.setHomeCountry(info[16]);
				if (info.length > 7)
					vo.setHomeTel(info[7]);

				if (info.length > 17)
					vo.setOfficeExtAddress(info[17]);
				if (info.length > 19)
					vo.setOfficeStreet(info[19]);
				if (info.length > 21)
					vo.setOfficePostalCode(info[21]);
				if (info.length > 20)
					vo.setOfficeCity(info[20]);
				if (info.length > 22)
					vo.setOfficeCountry(info[22]);
				if (info.length > 31) {
					// skip
				}
				if (info.length > 25)
					vo.setCompanyName(info[25]);
				if (info.length > 23)
					vo.setTitleName(info[23]);
				if (info.length > 24)
					vo.setDepartmentName(info[24]);
				if (info.length > 26)
					vo.setOfficeHomepage(info[26]);
				if (info.length > 8)
					vo.setOfficeFax(info[8]);
				if(info.length>6)
					vo.setOfficeTel(info[6]);

				vo.setDescription(line);

				list.add(vo);	
			} catch (Exception e) {
				throw new InvalidFileException();
			}
		}

		return list.toArray(new AddressBookMemberVO[list.size()]);
	}

	
}
