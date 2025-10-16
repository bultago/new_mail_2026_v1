package com.terracetech.tims.webmail.home.manager;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.vo.MailConfigVO;
import com.terracetech.tims.webmail.home.dao.IMailHomePortletDao;
import com.terracetech.tims.webmail.home.vo.MailHomeLayoutVO;
import com.terracetech.tims.webmail.home.vo.MailHomePortletVO;
import com.terracetech.tims.webmail.home.vo.MailMenuLayoutVO;
import com.terracetech.tims.webmail.setting.dao.ISettingUserEtcInfoDao;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailHomeManager {
	
	public final static String GROUP_MENU = "group_menu";

	private IMailHomePortletDao dao;
	
	private ISettingUserEtcInfoDao etcDao = null;
	
	private SystemConfigDao systemConfigDao;
	
	public void setSystemConfigDao(SystemConfigDao systemConfigDao) {
		this.systemConfigDao = systemConfigDao;
	}

	public void setDao(IMailHomePortletDao dao) {
		this.dao = dao;
	}
	
	
	public void setEtcDao(ISettingUserEtcInfoDao etcDao) {
		this.etcDao = etcDao;
	}


	public MailHomePortletVO[] readPortlets(int domainSeq) throws SQLException {
		List<MailHomePortletVO> list = dao.readPortlets(domainSeq); 
		
		return list.toArray(new MailHomePortletVO[list.size()]);
	}
	
	public List<MailHomePortletVO> readLayoutPortlet(int userSeq){
		List<MailHomePortletVO> list = dao.readLayoutPortlet(userSeq);
		
		return list;
	}
	
	public String readAfterLogin(int mailUserSeq){
		UserEtcInfoVO infoVO = etcDao.readUserEtcInfo(mailUserSeq);
		
		return infoVO.getAfterLogin();
	}
	
	public List<MailHomeLayoutVO> readLayoutList(){
		return dao.readLayout();
	}
	
	public MailHomeLayoutVO readLayout(){
		if(dao.readLayout().size()==0)
			return null;
		
		return dao.readLayout().get(0);
	}
	
	public String getMailHomeSetting(int mailUserSeq){
		UserEtcInfoVO vo = etcDao.readUserEtcInfo(mailUserSeq);		
		return (vo != null)?vo.getAfterLogin():"intro";		
	}
	
	public void saveLayoutPortlet(MailHomeLayoutVO layout, 
			int domainSeq, int userSeq,
			String setMailHome,
			HttpServletRequest request){
		MailHomePortletVO vo = null;
		for (int i = 1; i < (layout.getPortletCount()+1); i++) {
			vo = new MailHomePortletVO();
			vo.setLayoutSeq(layout.getLayoutSeq());
			vo.setUserSeq(userSeq);
			vo.setPortletSeq(Integer.parseInt(request.getParameter("portlet"+i)));
			vo.setLocation(String.valueOf(i));
			
			dao.saveLayoutPortlet(vo);
		}		
		etcDao.updateMailHome(Integer.toString(userSeq), setMailHome);
	}

	public void deleteLayoutPortlet(MailHomeLayoutVO layout, int userSeq) {
		dao.deleteLayoutPortlet(userSeq);
		
	}

	public MailHomePortletVO readLayoutPortlet(int domainSeq, String ps) {
		return dao.readPortlet(domainSeq, ps);
	}
	
	public MailMenuLayoutVO[] readMenusList(I18nResources resource, int mailDomainSeq, int groupSeq){
		List<MailMenuLayoutVO> list = dao.readMenusList(mailDomainSeq);
		String menuName = null;
		for (MailMenuLayoutVO mailMenuLayoutVO : list) {
			menuName = null;
			try {
				menuName = resource.getMessage(mailMenuLayoutVO.getMenuName());
				if(StringUtils.isEmpty(menuName)){
					menuName = mailMenuLayoutVO.getMenuName();
				}
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);	
			}
							
			if(menuName != null){
				mailMenuLayoutVO.setMenuName(menuName);
			}
		}
		
		MailMenuLayoutVO[] menus = list.toArray(new MailMenuLayoutVO[list.size()]);
		
		MailConfigVO vo = systemConfigDao.readDomainGroupConfig(mailDomainSeq, groupSeq, GROUP_MENU);
		if(vo !=null){
			String value = vo.getConfigValue();
			if(StringUtils.isNotEmpty(value)){
				list.clear();
				
				String[] seqs = value.split(",");
				for (String seq : seqs) {
					for (MailMenuLayoutVO menu : menus) {
						if(menu.getMenuSeq().equals(Integer.parseInt(seq.trim()))){
							list.add(menu);
						}
					}	
				}
			}
		}
		return list.toArray(new MailMenuLayoutVO[list.size()]);
	}
}
