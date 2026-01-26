package com.terracetech.tims.mobile.addrbook.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

@Controller("mobileAddressWorkController")
public class AddressWorkController {
	@Autowired
	private AddressBookManager addressbookManager;

	public String execute(
			@RequestParam(value = "addressIds", required = false) String[] addressIds,
			@RequestParam(value = "action", required = false) String action,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		boolean result = "delete".equals(action) ? 
			addressbookManager.deleteAddresses(addressIds) :
			addressbookManager.updateAddresses(addressIds);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
