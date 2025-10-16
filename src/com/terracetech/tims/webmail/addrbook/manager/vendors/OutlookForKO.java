package com.terracetech.tims.webmail.addrbook.manager.vendors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.icu.util.StringTokenizer;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.exception.InvalidFileException;
import com.terracetech.tims.webmail.util.StringUtils;

public class OutlookForKO implements IEmailVendor {

	private I18nResources resource = null;
	
	public OutlookForKO(I18nResources resource) {
		this.resource = resource;
	}
	
	public StringBuffer getAddrCSVDownload(List<AddressBookMemberVO> list) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(resource.getMessage("addr.outlook.001") + ",");	//1.이름
		sb.append(resource.getMessage("addr.outlook.002") + ",");	//2.성
		sb.append(resource.getMessage("addr.outlook.004") + ",");	//3.전자 메일 주소
		
		sb.append(resource.getMessage("addr.outlook.005") + ",");	//4.집 번지
		sb.append(resource.getMessage("addr.outlook.006") + ",");	//5.집 주소 구/군/시
		sb.append(resource.getMessage("addr.outlook.007") + ",");	//6.집 주소 우편 번호
		sb.append(resource.getMessage("addr.outlook.008") + ",");	//7.집 주소 시/도
		sb.append(resource.getMessage("addr.outlook.009") + ",");	//8.집 주소 국가/지역
		sb.append(resource.getMessage("addr.outlook.010") + ",");	//9.집 전화 번호
		
		sb.append(resource.getMessage("addr.outlook.012") + ",");	//10.휴대폰
		
		sb.append(resource.getMessage("addr.outlook.013") + ",");	//11.근무지 주소 번지
		sb.append(resource.getMessage("addr.outlook.014") + ",");	//12.근무지 구/군/시
		sb.append(resource.getMessage("addr.outlook.015") + ",");	//13.근무지 우편 번호
		sb.append(resource.getMessage("addr.outlook.016") + ",");	//14.근무지 시/도
		sb.append(resource.getMessage("addr.outlook.017") + ",");	//15.근무지 국가/지역
		sb.append(resource.getMessage("addr.outlook.018") + ",");	//16.근무처 전화
		sb.append(resource.getMessage("addr.outlook.020") + ",");	//17.회사
		sb.append(resource.getMessage("addr.outlook.021") + ",");	//18.직함
		sb.append(resource.getMessage("addr.outlook.022"));			//19.부서
		sb.append("\r\n");

		String dummy = "";
		for (AddressBookMemberVO member : list) {
			sb.append(StringUtils.replaceString(member.getFirstName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getLastName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getMemberEmail(), dummy) + ",");
			
			sb.append(StringUtils.replaceString(member.getHomeExtAddress(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeCity(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomePostalCode(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeState(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeCountry(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getHomeTel(), dummy) + ",");
			
			sb.append(StringUtils.replaceString(member.getMobileNo(), dummy) + ",");
			
			sb.append(StringUtils.replaceString(member.getOfficeExtAddress(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeCity(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficePostalCode(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeState(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeCountry(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getOfficeTel(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getCompanyName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getTitleName(), dummy) + ",");
			sb.append(StringUtils.replaceString(member.getDepartmentName(), dummy));
			
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
				if (info.length == 0)
					continue;

				vo.setUserSeq(userSeq);
				if(info.length <= 19 || info.length==20){
					caseCustomCSVFormat(vo, line, info);
				}else{
					caseFullCSVFormat(vo, line, info);	
				}

				list.add(vo);
			} catch (Exception e) {
				throw new InvalidFileException(e);
			}
		}

		return list.toArray(new AddressBookMemberVO[list.size()]);
	}
	
	private void caseCustomCSVFormat(AddressBookMemberVO vo, String line, String[] info) {
		
		if(info.length < 19){
			String[] tempInfo = new String[19];
			int idx = 0;
			for (int i = 0; i < info.length; i++) {
				tempInfo[idx] = info[i];
				idx++;
			}
			info = null;
			
			for (int i = idx; i < tempInfo.length; i++) {
				tempInfo[i] = "";
			}
			info = tempInfo;
		}
		
		
		vo.setFirstName(info[0]);	//0.이름
		vo.setLastName(info[1]);	//1.성
		vo.setMemberName(info[0]);	//2.이름		
		if(StringUtils.isNotEmpty(info[1])){
			vo.setMemberName(info[1] + vo.getMemberName());
		}
		
		if (info.length > 2)
			vo.setMemberEmail(info[2]);
		
		if (info.length > 3)
			vo.setHomeExtAddress(info[3]);
		if (info.length > 4)
			vo.setHomeCity(info[4]);
		if (info.length > 5)
			vo.setHomePostalCode(info[5]);
		if (info.length > 6)
			vo.setHomeState(info[6]);
		if (info.length > 7)
			vo.setHomeCountry(info[7]);
		if (info.length > 8)
			vo.setHomeTel(info[8]);
		
		if (info.length > 9)
			vo.setMobileNo(info[9]);
		
		if (info.length > 10)
			vo.setOfficeExtAddress(info[10]);
		if (info.length > 11)
			vo.setOfficeCity(info[11]);
		if (info.length > 12)
			vo.setOfficePostalCode(info[12]);
		if (info.length > 13)
			vo.setOfficeState(info[13]);
		if (info.length > 14)
			vo.setOfficeCountry(info[14]);
		if (info.length > 15)
			vo.setOfficeTel(info[15]);
		if (info.length > 16)
			vo.setCompanyName(info[16]);
		if (info.length > 17)
			vo.setTitleName(info[17]);
		if (info.length > 18)
			vo.setDepartmentName(info[18]);

		vo.setDescription(line);
	}
	
	private void caseFullCSVFormat(AddressBookMemberVO vo, String line, String[] info) {
		vo.setMemberName(info[1]);
		if (StringUtils.isNotEmpty(info[2])) {
			vo.setMemberName(info[2] + "" + vo.getMemberName());
		}
		if (StringUtils.isNotEmpty(info[3])) {
			vo.setMemberName(info[3] + "" + vo.getMemberName());
		}

		if (info.length > 76)
			vo.setMemberEmail(info[76]);
		if (info.length > 40)
			vo.setMobileNo(info[40]);
		if (info.length > 15)
			vo.setHomeExtAddress(info[15]);
		if (info.length > 11)
			vo.setHomeCity(info[11]);
		if (info.length > 13)
			vo.setHomePostalCode(info[13]);
		if (info.length > 12)
			vo.setHomeState(info[12]);
		if (info.length > 14)
			vo.setHomeCountry(info[14]);
		if (info.length > 37)
			vo.setHomeTel(info[37]);

		if (info.length > 8)
			vo.setOfficeExtAddress(info[8]);
		if (info.length > 10)
			vo.setOfficeCity(info[10]);
		if (info.length > 13)
			vo.setOfficePostalCode(info[13]);
		if (info.length > 12)
			vo.setOfficeState(info[12]);
		if (info.length > 14)
			vo.setOfficeCountry(info[14]);
		if (info.length > 31)
			vo.setOfficeTel(info[31]);
		if (info.length > 5)
			vo.setCompanyName(info[5]);
		if (info.length > 7)
			vo.setTitleName(info[7]);
		if (info.length > 6)
			vo.setDepartmentName(info[6]);
		if (info.length > 73)
			vo.setOfficeHomepage(info[73]);
		if (info.length > 19) {
			// skip
		}
		if (info.length > 30)
			vo.setOfficeFax(info[30]);

		vo.setDescription(line);
	}
}
