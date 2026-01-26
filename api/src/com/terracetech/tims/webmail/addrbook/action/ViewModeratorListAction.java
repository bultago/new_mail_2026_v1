package com.terracetech.tims.webmail.addrbook.action;

import java.util.List;

import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookModeratorVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.util.StringUtils;

public class ViewModeratorListAction extends BaseAction {

	private static final long serialVersionUID = 20081215L;

	private AddressBookManager manager;

	private int bookSeq;

	private AddressBookModeratorVO[] moderatorList;
	
	private int page;
	private int total;
	private int pageBase = 8;
	
	private String searchType = null; 
	private String keyWord = null;
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public void setBookSeq(int bookSeq) {
		this.bookSeq = bookSeq;
	}

	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}

	public AddressBookModeratorVO[] getModeratorList() {
		return moderatorList;
	}

	public String execute() throws Exception {

		return executePart();
	}

	public String executePart() throws Exception {
		if(StringUtils.isEmpty(keyWord))
			keyWord = "";
		
		if(keyWord.indexOf("@") > 1){
			try {
				keyWord = keyWord.split("@")[0];	
			} catch (Exception ignore) {
				LogManager.writeErr(this, ignore.getMessage(), ignore);	
			}
				
		}

		total = manager.readAddressBookModeratorListCount(bookSeq, searchType,keyWord);
		List<AddressBookModeratorVO> list = manager.readAddressBookModeratorList(bookSeq, page, pageBase, searchType,keyWord);

		moderatorList = list.toArray(new AddressBookModeratorVO[list.size()]);

		PageManager pm = new PageManager();
		pm.initParameter(total, pageBase, 5);
		pm.setPage(page);
		
		request.setAttribute("currentPage", page);
		request.setAttribute("pageBase", pageBase);
		request.setAttribute("total", total);
		request.setAttribute("pm", pm);
		
		return "success";
	}
}
