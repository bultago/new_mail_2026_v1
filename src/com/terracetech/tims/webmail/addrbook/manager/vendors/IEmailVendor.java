package com.terracetech.tims.webmail.addrbook.manager.vendors;

import java.io.IOException;
import java.util.List;

import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.exception.InvalidFileException;

public interface IEmailVendor {

	public abstract StringBuffer getAddrCSVDownload(List<AddressBookMemberVO> list);

	public AddressBookMemberVO[] getAddressBookMemberVO(String[] results, int userSeq) throws IOException, InvalidFileException;
}