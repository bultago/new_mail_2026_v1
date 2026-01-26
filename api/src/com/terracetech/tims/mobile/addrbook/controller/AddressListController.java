package com.terracetech.tims.mobile.addrbook.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

@Controller("mobileAddressListController")
public class AddressListController {
	@Autowired
	private AddressBookManager addressbookManager;

	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		List<AddressVO> addressList = addressbookManager.getAddressList(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		model.addAttribute("addressList", addressList);
		return "success";
	}
}
