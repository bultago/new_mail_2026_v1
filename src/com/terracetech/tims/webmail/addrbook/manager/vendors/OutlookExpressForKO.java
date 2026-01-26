package com.terracetech.tims.webmail.addrbook.manager.vendors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.exception.InvalidFileException;
import com.terracetech.tims.webmail.util.StringUtils;

public class OutlookExpressForKO implements IEmailVendor {

	private I18nResources resource = null;
	
	public OutlookExpressForKO(I18nResources resource) {
		this.resource = resource;
	}
	
	public StringBuffer getAddrCSVDownload(List<AddressBookMemberVO> list) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(resource.getMessage("addr.outlookExpress.001") + ",");	//1.ÀüÃ¼ ÀÌ¸§
		sb.append(resource.getMessage("addr.outlookExpress.006") + ",");	//2.ÀüÀÚ ¸ÞÀÏ ÁÖ¼Ò
		
		sb.append(resource.getMessage("addr.outlookExpress.007") + ",");	//3.ÁÖ¼Ò(Áý)
		sb.append(resource.getMessage("addr.outlookExpress.008") + ",");	//4.±¸±º½Ã(Áý)
		sb.append(resource.getMessage("addr.outlookExpress.009") + ",");	//5.¿ìÆí¹øÈ£(Áý)
		sb.append(resource.getMessage("addr.outlookExpress.010") + ",");	//6.½Ã/µµ(Áý)
		sb.append(resource.getMessage("addr.outlookExpress.011") + ",");	//7.±¹°¡(Áý)
		sb.append(resource.getMessage("addr.outlookExpress.012") + ",");	//8.Áý ÀüÈ­
		
		sb.append(resource.getMessage("addr.outlookExpress.014") + ",");	//9.ÈÞ´ëÆù
		
		sb.append(resource.getMessage("addr.outlookExpress.016") + ",");	//10.ÁÖ¼Ò(È¸»ç)
		sb.append(resource.getMessage("addr.outlookExpress.017") + ",");	//11.±¸/±º/½Ã(È¸»ç)
		sb.append(resource.getMessage("addr.outlookExpress.018") + ",");	//12.¿ìÆí ¹øÈ£(È¸»ç)
		sb.append(resource.getMessage("addr.outlookExpress.019") + ",");	//13.½Ã/µµ(È¸»ç)
		sb.append(resource.getMessage("addr.outlookExpress.020") + ",");	//14.±¹°¡(È¸»ç)
		sb.append(resource.getMessage("addr.outlookExpress.022") + ",");	//15.È¸»ç ÀüÈ­
		sb.append(resource.getMessage("addr.outlookExpress.024") + ",");	//16.È¸»ç
		sb.append(resource.getMessage("addr.outlookExpress.025") + ",");	//17.Á÷ÇÔ
		sb.append(resource.getMessage("addr.outlookExpress.026"));			//18.ºÎ¼­
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
	 * 1. Name ÀüÃ¼ ÀÌ¸§
	 * 2. Email	 ÀüÀÚ ¸ÞÀÏ ÁÖ¼Ò
	 * 3. Home ExtAddress	ÁÖ¼Ò(Áý) 
	 * 4. Home Street	±¸/±º/½Ã(Áý) 
	 * 5. Home Post	 ¿ìÆí ¹øÈ£(Áý)
	 * 6. Home City	½Ã/µµ(Áý) 
	 * 7. Home Country ±¹°¡(Áý)	 
	 * 8. Home Tel	 Mobile	 Áý ÀüÈ­
	 * 9. Office ExtAddress	 ÁÖ¼Ò(È¸»ç)
	 * 10. Office Street	±¸/±º/½Ã(È¸»ç) 
	 * 11. Office Post	¿ìÆí ¹øÈ£(È¸»ç) 
	 * 12. Office City	½Ã/µµ(È¸»ç) 
	 * 13. Office Country	±¹°¡(È¸»ç) 
	 * 14. Office Tel	È¸»ç ÀüÈ­ 
	 * 15. Company	 È¸»ç
	 * 16. Title Á÷ÇÔ	 
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
		if (info.length > 3) //±¸±º½Ã
			vo.setHomeCity(info[3]);
		if (info.length > 4)//¿ìÆí¹øÈ£
			vo.setHomePostalCode(info[4]);
		if (info.length > 5)//½Ãµµ
			vo.setHomeState(info[5]);
		if (info.length > 6)//±¹°¡Áö¿ª
			vo.setHomeCountry(info[6]);
		if (info.length > 7)//ÀüÈ­
			vo.setHomeTel(info[7]);
		
		if (info.length > 8)//ÀÌµ¿ÀüÈ­
			vo.setMobileNo(info[8]);
		
		if (info.length > 9)//ÁÖ¼Ò
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
