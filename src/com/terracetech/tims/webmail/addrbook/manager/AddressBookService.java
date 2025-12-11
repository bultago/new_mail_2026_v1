package com.terracetech.tims.webmail.addrbook.manager;

import java.util.List;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.addrbook.ibean.AddressBookDupCheckMemberBean;
import com.terracetech.tims.webmail.addrbook.ibean.AddressBookMemberBean;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookAuthVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookDupMemberVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookGroupVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookModeratorVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookReaderVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookVO;
import com.terracetech.tims.webmail.common.BaseService;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.exception.InvalidParameterException;
import com.terracetech.tims.webmail.exception.SaveFailedException;
import com.terracetech.tims.webmail.exception.UserAuthException;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.exception.MailUserNotFoundException;
import com.terracetech.tims.webmail.organization.manager.OrganizationManager;
import com.terracetech.tims.webmail.organization.vo.MemberVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class AddressBookService extends BaseService{

	private AddressBookManager manager;
	
	private I18nResources resource = null;
	
	public AddressBookService() {
		super();
	}
	
	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}
	
	public void loadHttpResource() throws UserAuthException{
		super.loadHttpResource();
		
		if(user != null){
			Locale locale = new Locale(user.get(User.LOCALE));
			resource = new I18nResources(locale,"addr");
		}else{
			Locale locale = I18nConstants.getUserLocale(request);
			resource = new I18nResources(locale,"addr");
		}
		
		manager.setResource(resource);
	}

	public JSONArray getJsonPrivateAddressListByIndex(String startChar, int currentPage, int maxResult) {
		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		return manager.readJSonPrivateAddressListByIndex(userSeq, 0, startChar, currentPage, maxResult, "", "");
	}
	
	public JSONObject getJsonAddressMember(int bookSeq, int memberSeq) throws UserAuthException{
		
		if(user==null)
			loadHttpResource();
		
		AddressBookMemberVO member = null;
		if(bookSeq==0){
			int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
			member = manager.readPrivateAddressMember(userSeq, memberSeq);
		}else{
			member = manager.readSharedAddressMember(bookSeq, memberSeq);
		}
		
		AddressBookMemberBean bean = new AddressBookMemberBean(member);
		return bean.toJson();
	}
	
	public JSONArray getJSonPrivateGroupList(){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		return manager.readJSonPrivateGroupList(userSeq);
	}
	
	public JSONArray getJSonSharedGroupList(int bookSeq){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		return manager.readJSonSharedGroupList(bookSeq, userSeq);
	}
	
	public void savePrivateGroup(String name) throws SaveFailedException{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		AddressBookGroupVO group = new AddressBookGroupVO();
		group.setUserSeq(userSeq);
		group.setGroupName(name);
		
		manager.savePrivateGroup(domainSeq, userSeq, group);
	}
	
	public void saveSharedGroup(int bookSeq, String name) throws SaveFailedException{
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		AddressBookGroupVO group = new AddressBookGroupVO();
		group.setUserSeq(userSeq);
		group.setGroupName(name);
		group.setAdrbookSeq(bookSeq);
		
		manager.saveSharedGroup(domainSeq,bookSeq, userSeq, group);
	}
	
	public void updatePrivateGroup(int groupSeq, String name) throws SaveFailedException{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		AddressBookGroupVO group = new AddressBookGroupVO();
		group.setGroupSeq(groupSeq);
		group.setUserSeq(userSeq);
		group.setGroupName(name);
		
		manager.updatePrivateGroup(group);
	}
	
	public void deletePrivateGroup(int groupSeq){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		manager.deletePrivateGroup(userSeq, groupSeq);
	}
	
	public void deleteSharedGroup(int bookSeq, int groupSeq){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		manager.deleteSharedGroup(bookSeq, userSeq, groupSeq);
	}
	
	public void updateSharedGroup(int bookSeq , int groupSeq, String name) throws SaveFailedException{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		AddressBookGroupVO group = new AddressBookGroupVO();
		group.setGroupSeq(groupSeq);
		group.setUserSeq(userSeq);
		group.setAdrbookSeq(bookSeq);
		group.setGroupName(name);
		
		manager.updateSharedGroup(userSeq, group, domainSeq);
	}
	
	public void savePrivateAddressMember(AddressBookMemberVO member, int groupSeq) throws SaveFailedException{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		member.setUserSeq(userSeq);
		
		Locale locale = new Locale(user.get(User.LOCALE));
		I18nResources resource = new I18nResources(locale,"addr");
		
		if(StringUtils.isEmpty(member.getMemberName())){
			throw new InvalidParameterException(resource.getMessage("addr.info.msg.006"));
		}
		
		if(member.getMemberSeq()==0)
			manager.savePrivateAddressMemberWithTransactional(member, groupSeq, domainSeq);
		else
			manager.updatePrivateAddressMember(member);
	}
	
	public void saveSharedAddressMember(AddressBookMemberVO member, int groupSeq) throws SaveFailedException{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		member.setUserSeq(userSeq);
		member.setGroupSeq(groupSeq);
		
		if(member.getMemberSeq()==0)
			manager.saveSharedAddressMemberWithTransactional(member, groupSeq, domainSeq);
		else
			manager.updateSharedAddressMember(member);
	}
	
	public void saveSharedAddressBook(String name) throws SaveFailedException{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		if(StringUtils.isEmpty(name))
			throw new InvalidParameterException(resource.getMessage("addr.info.msg.006"));
		
		AddressBookVO book = new AddressBookVO();
		book.setMailDomainSeq(domainSeq);
		book.setAddrbookName(name);
		book.setCreatorSeq(userSeq);
		book.setReaderType("R");
		book.setDescription("");
		
		try {
			manager.saveSharedAddressBook(userSeq, domainSeq, book);	
		} catch (SaveFailedException e) {
			throw e;
		}
	}
	
	public void updateSharedAddressBook(int bookSeq, String name){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		manager.updateSharedAddressBook(userSeq, bookSeq, name);
	}
	
	public void deleteSharedAddressBook(int bookSeq){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		manager.deleteSharedAddressBook(userSeq, bookSeq, domainSeq);
	}
	
	public JSONArray getJSonSharedAddressBookList(){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		return manager.readJSonSharedAddressBookList(domainSeq, userSeq);
	}
	
	public void deleteMember(int bookSeq, int groupSeq, int[] memberSeqs){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		if(bookSeq==0){
			if(groupSeq==0){
				manager.deletePrivateMember(userSeq, memberSeqs);	
			}else{
				manager.deletePrivateMemberRelation(userSeq, memberSeqs, groupSeq);
			}
				
		}else{
			if(groupSeq==0){
				manager.deleteSharedMember(bookSeq, memberSeqs);	
			}else{
				manager.deleteSharedMemberRelation(bookSeq, memberSeqs, groupSeq);
			}
		}
		
	}
	
	public void saveAddressBookReader(int bookSeq, String email) throws MailUserNotFoundException {
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		AddressBookReaderVO reader = new AddressBookReaderVO();
		reader.setAddrbookSeq(bookSeq);
		reader.setUserEmail(email);
		reader.setDescription("");
		reader.setUserName("");
		
		manager.saveAddressBookReader(domainSeq, userSeq, reader);	
	}
	
	public void saveAddressBookModerator(int bookSeq, String email) throws MailUserNotFoundException {
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		AddressBookModeratorVO moderator = new AddressBookModeratorVO();
		moderator.setAddrbookSeq(bookSeq);
		moderator.setUserEmail(email);
		moderator.setDescription("");
		moderator.setUserName("");
		
		manager.saveAddressBookModerator(domainSeq, userSeq, moderator);	
	}
	
	public void deleteAddressBookReader(int bookSeq, int[] memberSeqs){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		manager.deleteAddressBookReader(bookSeq, userSeq, memberSeqs);
	}
	
	public void deleteAddressBookModerator(int bookSeq, int[] memberSeqs){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		manager.deleteAddressBookModerator(bookSeq, userSeq, memberSeqs);
	}
	
	public void moveMember(int bookSeq, int[] memberSeqs, int source, int target){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		if(bookSeq==0){
			manager.movePrivateMember(userSeq, memberSeqs, source, target);	
		}else{
			manager.moveSharedMember(bookSeq, memberSeqs, source, target);
		}
	}
	
	public void copyMember(int bookSeq, int[] memberSeqs, int groupSeq){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		if(bookSeq==0){
			manager.copyPrivateMember(userSeq, memberSeqs, groupSeq);	
		}else{
			manager.copySharedMember(bookSeq, memberSeqs, groupSeq);
		}
		
	}
	
	public void copyMemberFromOrg(String[] values, int groupSeq)throws SaveFailedException{
		String locale = user.get(User.LOCALE);
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		//orgCode,memberSeq
		for (String val : values) {
			if(StringUtils.isEmpty(val))
				continue;
			
			String[] info = val.split(",");
			if(info.length != 2)
				continue;
			
			try {
				Integer.parseInt(info[1]);
			} catch (Exception e) {
				log.error(e.getMessage());
				continue;
			}
			
			AddressBookMemberVO member = manager.readOrgMember(locale, info[0], domainSeq, Integer.parseInt(info[1]));
			
			if(member ==null)
				continue;
			
			member.setUserSeq(userSeq);
			member.setMemberSeq(0);
			
			manager.savePrivateAddressMemberWithTransactional(member, groupSeq, domainSeq);
		}
	}
	
	public void importCSVMember(int bookSeq, int groupSeq){
		
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getJsonAuth(int bookSeq){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		AddressBookAuthVO auth = manager.getAddrAuth(domainSeq,bookSeq, userSeq);
		
		JSONObject obj = new JSONObject();
		obj.put("creatorAuth", auth.getCreatorAuth());
		obj.put("readAuth", auth.getReadAuth());
		obj.put("writeAuth", auth.getWriteAuth());
		
		return obj;
	}
	
	@SuppressWarnings("unchecked")
    public JSONArray checkMemberAddrDupFromOrg(String[] values, int groupSeq) {
    	JSONArray beanArray = new JSONArray();
    	JSONObject beanObject = null;
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
	    int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
	    List<AddressBookMemberVO> addrMember;
	    int dupCnt = 0;
	
	    String installLocale = EnvConstants.getBasicSetting("setup.state");
	    String userLocale = user.get(User.LOCALE);
	    String orgLocale = userLocale;
	    if ("jp".equals(installLocale)) {
	        orgLocale = "ko".equals(orgLocale) ? "en" : orgLocale;
	        orgLocale = "cn".equals(orgLocale) ? "jp" : orgLocale;
	    } else if ("ko".equals(installLocale)) {
	        orgLocale = "jp".equals(orgLocale) ? "en" : orgLocale;
	        orgLocale = "cn".equals(orgLocale) ? "en" : orgLocale;
	    }
        try {
        	
        // orgCode,memberSeq
            for (String val : values) {
                if (StringUtils.isEmpty(val))
                    continue;
    
                String[] info = val.split(",");
                if (info.length != 2)
                    continue;
        
                AddressBookMemberVO member = manager.readOrgMember(orgLocale, info[0], domainSeq, Integer.parseInt(info[1]));
                
                addrMember = manager.getExistEmail(userSeq,member.getMemberEmail(),member.getMemberName());
                
                dupCnt = addrMember.size();
                AddressBookDupMemberVO vo = new AddressBookDupMemberVO();
                vo.setMemberEmail(member.getMemberEmail());
                vo.setMemberName(member.getMemberName());
                vo.setOrgCode(info[0]);
                vo.setUserSeq(Integer.parseInt(info[1]));
                vo.setDupCnt(dupCnt);
                vo.setGroupSeq(groupSeq);
                
                beanObject = new AddressBookDupCheckMemberBean(vo).toJson();
                beanArray.add(beanObject);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        
        return beanArray;
    }
	
	@SuppressWarnings("unchecked")
    public JSONObject copyOrgMember(AddressBookDupMemberVO[] dupMember) {
		JSONObject jsonObject = new JSONObject();
		boolean isSuccess = true;
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
	        int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
	
	        String installLocale = EnvConstants.getBasicSetting("setup.state");
	        String userLocale = user.get(User.LOCALE);
	        String orgLocale = userLocale;
	        if ("jp".equals(installLocale)) {
	            orgLocale = "ko".equals(orgLocale) ? "en" : orgLocale;
	            orgLocale = "cn".equals(orgLocale) ? "jp" : orgLocale;
	        } else if ("ko".equals(installLocale)) {
	            orgLocale = "jp".equals(orgLocale) ? "en" : orgLocale;
	            orgLocale = "cn".equals(orgLocale) ? "en" : orgLocale;
	        }
	        try {
	        	JSONObject obj = null;
	        // orgCode,memberSeq
	        	List<AddressBookMemberVO> addrMember;
				int[] memberSeq = new int[1];	
				
				String addrAddType = ""; // Áßº¹ÁÖ¼Ò¿¡ ´ëÇÑ ¼±ÅÃ
				String orgCode = "";     // ÇØ´çÁ¶Á÷ ÄÚµå
				int orgUserSeq = 0;      // Á¶Á÷µµ¿¡¼­ ¼±ÅÃÇÑ »ç¿ëÀÚ userSeq
				int groupSeq = 0;        // Ãß°¡ÇÒ ÁÖ¼Ò·ÏÀÇ groupSeq
				
	            for (int i=0 ; i < dupMember.length ; i++) {
	            	
	            	addrAddType = dupMember[i].getAddrAddType();  //Áßº¹ÁÖ¼Ò¿¡ ´ëÇÑ ¼±ÅÃ(Áßº¹Ãß°¡,µ¤¾î¾²±â,Ãß°¡¾ÈÇÏ±â,Ãë¼Ò)
	            	orgCode = dupMember[i].getOrgCode();
	            	orgUserSeq = dupMember[i].getUserSeq();
	            	groupSeq = dupMember[i].getGroupSeq();
	            	
	            	if(!("noWrite".equalsIgnoreCase(addrAddType) || "cancel".equalsIgnoreCase(addrAddType))){ // Áßº¹Ãß°¡,µ¤¾î¾²±â
	            	
		            	AddressBookMemberVO member = manager.readOrgMember(orgLocale, orgCode, domainSeq, orgUserSeq);
		            	 
		            	if (member == null)
			                continue;
		            	
		            	if("overWrite".equalsIgnoreCase(addrAddType)){  //µ¤¾î¾²±â½Ã ±âÁ¸ ÁÖ¼Ò »èÁ¦
		            		addrMember = manager.getExistEmail(userSeq,member.getMemberEmail(),member.getMemberName());
		            		memberSeq[0] = addrMember.get(0).getMemberSeq();
		            		manager.deletePrivateMember(userSeq, memberSeq);
		            	}
		            		
	            		member.setUserSeq(userSeq);
		                member.setMemberSeq(0);
		                manager.savePrivateAddressMemberWithTransactional(member, groupSeq, domainSeq);
		            	
	            	}
	            	
	            	    		
	            }
	            isSuccess = true;
	        } catch (Exception e) {
	        	e.printStackTrace();
	            log.error(e.getMessage());
	            isSuccess = false;
		}
	        jsonObject.put("isSuccess", isSuccess);
	        return jsonObject;
    }
	@SuppressWarnings("unchecked")
    public JSONObject checkPrivateDupEmail(String email, String name) {
    	JSONObject jsonObject = new JSONObject();
    	int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        int dupCnt = 0;
        try {
        	List<AddressBookMemberVO> addrMember;
        
        	addrMember = manager.getExistEmail(userSeq,email,name);
        	dupCnt = addrMember.size();        		 
        	jsonObject.put("dupCnt", dupCnt);
        	jsonObject.put("isSuccess", true);
        } catch (Exception e) {
        	e.printStackTrace();
            log.error(e.getMessage());
            jsonObject.put("isSuccess", false);
        }
        return jsonObject;
    }
	public void saveDupPrivateAddressMember(String type,AddressBookMemberVO member,int groupSeq) throws SaveFailedException{
	    	int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
	        int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
	        List<AddressBookMemberVO> addrMember = manager.getExistEmail(userSeq,member.getMemberEmail(), member.getMemberName());
	        int[] memberSeq = new int[1];
	        member.setUserSeq(userSeq);
	        
	        if("overWrite".equals(type)){
	        	memberSeq[0] =  addrMember.get(0).getMemberSeq();
	    		manager.deletePrivateMember(userSeq, memberSeq);
	    		manager.savePrivateAddressMemberWithTransactional(member, groupSeq, domainSeq);
	    }else{
	    	manager.savePrivateAddressMemberWithTransactional(member, groupSeq, domainSeq);        	
	    }
		
	}
	  
	@SuppressWarnings("unchecked")
	public JSONObject copyMailMember(AddressBookDupMemberVO[] dupMember) {
	JSONObject jsonObject = new JSONObject();
	boolean isSuccess = true;
	int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
	    int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
	
	    String memberEmail = "";
	    String memberName = "";
	    int groupSeq = 0;  
	    String addrAddType = "";
	    
	    try {
	    	List<AddressBookMemberVO> addrMember;
			int[] memberSeq = new int[1];
			AddressBookMemberVO member;
	        for (int i=0 ; i < dupMember.length ; i++) {
	        	
	        	memberEmail = dupMember[i].getMemberEmail();
	        	memberName = dupMember[i].getMemberName();
	        	groupSeq = dupMember[i].getGroupSeq();
	        	addrAddType = dupMember[i].getAddrAddType();
	        	
	        	member = new AddressBookMemberVO();
	        	member.setUserSeq(userSeq);
	        	member.setMemberEmail(memberEmail);
	        	member.setMemberName(dupMember[i].getMemberName());
	        	
	        	if("overWrite".equalsIgnoreCase(addrAddType)){
	        		addrMember = manager.getExistEmail(userSeq,memberEmail,memberName);
	        		memberSeq[0] = addrMember.get(0).getMemberSeq();
	        		manager.deletePrivateMember(userSeq, memberSeq);
	        		manager.savePrivateAddressMemberWithTransactional(member, groupSeq, domainSeq);
	                 
	        	}else if("addWrite".equalsIgnoreCase(addrAddType)){
	        		manager.savePrivateAddressMemberWithTransactional(member, groupSeq, domainSeq); 
	        	}
	        }
	        isSuccess = true;
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	        log.error(e.getMessage());
	        isSuccess = false;
	    }
	    jsonObject.put("isSuccess", isSuccess);
	    return jsonObject;
	}
	@SuppressWarnings("unchecked")
	public JSONArray checkDupEmail(AddressBookDupMemberVO[] member) {
	    	
	    	JSONArray beanArray = new JSONArray();
	    	JSONObject beanObject = null;
	    	
	        int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
	        int dupCnt = 0;
	        try {
	        	List<AddressBookMemberVO> addrMember;
	        	AddressBookDupMemberVO vo;
	        	 for (int i=0 ; i < member.length ; i++) {
	        		 addrMember = manager.getExistEmail(userSeq,member[i].getMemberEmail(),member[i].getMemberName());
	        		 dupCnt = addrMember.size();
	        		 vo = new AddressBookDupMemberVO();
	        		 vo.setMemberName(member[i].getMemberName());
	        		 vo.setMemberEmail(member[i].getMemberEmail());
	        		 vo.setDupCnt(dupCnt);
	        		 vo.setGroupSeq(member[i].getGroupSeq());
	        		 
	        		 beanObject = new AddressBookDupCheckMemberBean(vo).toJson();
	        		 beanArray.add(beanObject);    		 
	        	 }
	    		
	        } catch (Exception e) {
	            log.error(e.getMessage());
	        }
	        
	        return beanArray;
	}
}
