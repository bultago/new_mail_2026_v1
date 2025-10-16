package com.terracetech.tims.webmail.addrbook.manager.vendors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.exception.InvalidFileException;
import com.terracetech.tims.webmail.util.StringUtils;

public class OutlookExpressForJP implements IEmailVendor {
	
	private I18nResources resource = null;
	
	public OutlookExpressForJP(I18nResources resource) {
		this.resource = resource;
	}

	public StringBuffer getAddrCSVDownload(List<AddressBookMemberVO> list) {
StringBuffer sb = new StringBuffer();
		
		sb.append(resource.getMessage("addr.outlookExpress.001") + ",");	//전체 이름
		sb.append(resource.getMessage("addr.outlookExpress.006") + ",");	//전자 메일 주소
		
		sb.append(resource.getMessage("addr.outlookExpress.007") + ",");	//주소(집)
		sb.append(resource.getMessage("addr.outlookExpress.008") + ",");	//구군시(집)
		sb.append(resource.getMessage("addr.outlookExpress.009") + ",");	//우편번호(집)
		sb.append(resource.getMessage("addr.outlookExpress.010") + ",");	//시/도(집)
		sb.append(resource.getMessage("addr.outlookExpress.011") + ",");	//국가(집)
		sb.append(resource.getMessage("addr.outlookExpress.012") + ",");	//집 전화
		
		sb.append(resource.getMessage("addr.outlookExpress.014") + ",");	//휴대폰
		
		sb.append(resource.getMessage("addr.outlookExpress.016") + ",");	//주소(회사)
		sb.append(resource.getMessage("addr.outlookExpress.017") + ",");	//구/군/시(회사)
		sb.append(resource.getMessage("addr.outlookExpress.018") + ",");	//우편 번호(회사)
		sb.append(resource.getMessage("addr.outlookExpress.019") + ",");	//시/도(회사)
		sb.append(resource.getMessage("addr.outlookExpress.020") + ",");	//국가(회사)
		sb.append(resource.getMessage("addr.outlookExpress.022") + ",");	//회사 전화
		sb.append(resource.getMessage("addr.outlookExpress.024") + ",");	//회사
		sb.append(resource.getMessage("addr.outlookExpress.025") + ",");	//직함
		sb.append(resource.getMessage("addr.outlookExpress.026"));			//부서
		sb.append("\r\n");

		String dummy = "";
		for (AddressBookMemberVO member : list) {
			sb.append(StringUtils.replaceString(member.getMemberName(), dummy) + ",");
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
	
	/**
	 * <pre>
	 * 1. Name 전체 이름
	 * 2. Email	 전자 메일 주소
	 * 3. Home ExtAddress	주소(집) 
	 * 4. Home Street	구/군/시(집) 
	 * 5. Home Post	 우편 번호(집)
	 * 6. Home City	시/도(집) 
	 * 7. Home Country 국가(집)	 
	 * 8. Home Tel	 Mobile	 집 전화
	 * 9. Office ExtAddress	 주소(회사)
	 * 10. Office Street	구/군/시(회사) 
	 * 11. Office Post	우편 번호(회사) 
	 * 12. Office City	시/도(회사) 
	 * 13. Office Country	국가(회사) 
	 * 14. Office Tel	회사 전화 
	 * 15. Company	 회사
	 * 16. Title 직함	 
	 * 17. Deptartment	 
	 * 18. Homepage	 	 
	 * 20. Fax,
	 * </pre>
	 * @param results
	 * @param userSeq
	 * @return
	 * @throws IOException
	 * @throws InvalidFileException
	 */
	public AddressBookMemberVO[] getAddressBookMemberVO(String[] results, int userSeq) throws IOException, InvalidFileException {
		List<AddressBookMemberVO> list = new ArrayList<AddressBookMemberVO>();


		AddressBookMemberVO vo = null;
		for (int i = 0; i < results.length; i++) {
			String line = results[i];
			try {
				vo = new AddressBookMemberVO();
				
				String[] info = line.split(",", -1);
				if(info.length ==0)
					continue;
				
				vo.setUserSeq(userSeq);
				if(info.length==18 || info.length==19){
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
		vo.setMemberName(info[0]);
		
		if (info.length > 1)
			vo.setMemberEmail(info[1]);
		
		if (info.length > 2)
			vo.setHomeExtAddress(info[2]);
		if (info.length > 3) //구군시
			vo.setHomeCity(info[3]);
		if (info.length > 4)//우편번호
			vo.setHomePostalCode(info[4]);
		if (info.length > 5)//시도
			vo.setHomeState(info[5]);
		if (info.length > 6)//국가지역
			vo.setHomeCountry(info[6]);
		if (info.length > 7)//전화
			vo.setHomeTel(info[7]);
		
		if (info.length > 8)//이동전화
			vo.setMobileNo(info[8]);
		
		if (info.length > 9)//주소
			vo.setOfficeExtAddress(info[9]);
		if (info.length > 10)
			vo.setOfficeCity(info[10]);
		if (info.length > 11)
			vo.setOfficePostalCode(info[11]);
		if (info.length > 12)
			vo.setOfficeState(info[12]);
		if (info.length > 13)
			vo.setOfficeCountry(info[13]);
		if (info.length > 14)
			vo.setOfficeTel(info[14]);
		if (info.length > 15)
			vo.setCompanyName(info[15]);
		if (info.length > 16)
			vo.setTitleName(info[16]);
		if (info.length > 17)
			vo.setDepartmentName(info[17]);

		vo.setDescription(line);
	}
	
	private void caseFullCSVFormat(AddressBookMemberVO vo, String line, String[] info) {
		vo.setMemberName(info[0]);
		if(info.length > 1)
			vo.setMemberEmail(info[1]);
		if(info.length > 8)
			vo.setMobileNo(info[8]);
		if(info.length > 2)
			vo.setHomeExtAddress(info[2]);
		if(info.length > 3)
			vo.setHomeStreet(info[3]);
		if(info.length > 4)
			vo.setHomePostalCode(info[4]);
		if(info.length > 5)
			vo.setHomeCity(info[5]);
		if(info.length > 6)
			vo.setHomeCountry(info[6]);
		if(info.length > 7)
			vo.setHomeTel(info[7]);
		
		if(info.length > 9)
			vo.setOfficeExtAddress(info[9]);
		if(info.length > 10)
			vo.setOfficeStreet(info[10]);
		if(info.length > 11)
			vo.setOfficePostalCode(info[11]);
		if(info.length > 12)
			vo.setOfficeCity(info[12]);
		if(info.length > 13)
			vo.setOfficeCountry(info[13]);
		if(info.length > 14)
			vo.setOfficeTel(info[14]);
		if(info.length > 15)
			vo.setCompanyName(info[15]);
		if(info.length > 16)
			vo.setTitleName(info[16]);
		if(info.length > 17)
			vo.setDepartmentName(info[17]);
		if(info.length > 18)
			vo.setOfficeHomepage(info[18]);
		if(info.length > 19){
			//skip
		}
		if(info.length > 20)
			vo.setOfficeFax(info[20]);
	}
}
