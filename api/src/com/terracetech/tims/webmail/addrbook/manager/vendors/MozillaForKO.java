package com.terracetech.tims.webmail.addrbook.manager.vendors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.exception.InvalidFileException;
import com.terracetech.tims.webmail.util.StringUtils;

public class MozillaForKO implements IEmailVendor{

	private I18nResources resource = null;
	
	public MozillaForKO(I18nResources resource) {
		this.resource = resource;
	}
	
	public StringBuffer getAddrCSVDownload(List<AddressBookMemberVO> list) {
		StringBuffer sb = new StringBuffer();
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
					vo.setMemberName(info[1] + ""+ vo.getMemberName());
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
