package com.terracetech.tims.mobile.addrbook.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

@Controller("mobileAddressViewController")
public class AddressViewController {
	@Autowired
	private AddressBookManager addressbookManager;

	public String execute(
			@RequestParam(value = "addressId", required = false) String addressId,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		AddressVO address = addressbookManager.getAddress(Integer.parseInt(addressId));
		model.addAttribute("address", address);
		return "success";
	}
}
