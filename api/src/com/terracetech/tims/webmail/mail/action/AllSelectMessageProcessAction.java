package com.terracetech.tims.webmail.mail.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.json.simple.JSONObject;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class AllSelectMessageProcessAction extends BaseAction {

	private static final long serialVersionUID = 4501222114801991567L;
	
	private static final int MAX_SEARCH_COUNT = 1000;
	private static final int MAX_BATCH_COUNT = 150;
	
	private MailManager mailManager = null;
	private SettingManager userSettingManager = null;
	private String targetFolder = null;
	
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public void setUserSettingManager(SettingManager userSettingManager) {
		this.userSettingManager = userSettingManager;
	}
	
	@SuppressWarnings("unchecked")
	public String execute() throws Exception{				
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		String actionType = request.getParameter("funcType");
		String targetFolderStr = request.getParameter("targetFolder");
		this.targetFolder = (targetFolderStr != null)?TMailUtility.IMAPFolderEncode(targetFolderStr):null;	
		
		String sharedFlag = request.getParameter("sharedFlag");
		boolean isShared = (sharedFlag != null && sharedFlag.equals("shared"))?true:false;
		String sharedUserSeq = request.getParameter("sharedUserSeq");
		String sharedFolderName = request.getParameter("sharedFolderName");			
		
		String keyWord = request.getParameter("keyWord");		
		String advancedSearch = request.getParameter("adv");
		String category = request.getParameter("category");
		String folderName = request.getParameter("folder");
		
		String listType = request.getParameter("listType");
		String tagIdStr = request.getParameter("tagId");
		
		String flag = request.getParameter("flag");
		String fromAddr = request.getParameter("fromaddr");
		String toAddr = request.getParameter("toaddr");
		String fromDate = request.getParameter("sdate");
		String toDate = request.getParameter("edate");		
		String sortBy = request.getParameter("sortBy");
		String sortDir = request.getParameter("sortDir");
		
		char flagType = (flag != null && flag.length() > 0)?flag.charAt(0):'x';
		listType = (listType != null)?listType:"mail";
		
		boolean isMailSearch = "mail".equals(listType);
        boolean isAllFolder = "all".equals(folderName);
        //tagIdStr = (tagIdStr == null) ? "-1" : tagIdStr;
        // TCUSTOM-2092
        tagIdStr = StringUtils.isEmpty(tagIdStr) ? "-1" : tagIdStr;
		
		String[] exceptFolders = null;
		String folderEncodeName = null;				
		
		if (StringUtils.isEmpty(folderName))
			folderName = FolderHandler.INBOX;		
		
		if (StringUtils.isEmpty(sortBy))
			sortBy = "arrival";
		
		if (StringUtils.isEmpty(sortDir))
			sortDir = "desc";
		
		if(isShared){
			folderEncodeName = sharedFolderName;
		} else {
			folderEncodeName = TMailUtility.IMAPFolderEncode(folderName);
		}
		
		TMailStore store = null;
		TMailStore userStore = null;
		TMailFolder userFolder = null;
		TMailFolder currentFolder = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		JSONObject jObj = new JSONObject();
		try {
			MessageSortInfoBean sortBean = new MessageSortInfoBean();
			sortBean.setSortBy(sortBy);
			sortBean.setSortDir(sortDir);
			if (StringUtils.isNotEmpty(advancedSearch)) {
				sortBean.setAdSearchCategory(category);
				sortBean.setAdvanceMode(true);
				sortBean.setAdFromEmailPattern(fromAddr);
				sortBean.setAdToEmailPattern(toAddr);
				sortBean.setFromDate(fromDate);
				sortBean.setToDate(toDate);
				sortBean.setAdSearchPattern(keyWord);				
			} else {
				sortBean.setPattern(keyWord);				
			}	
			
			sortBean.setSearchFlag(flagType);
			
			UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(userSeq);
			
			store = factory.connect(isShared, sharedUserSeq, remoteIp, user);
			if(isShared){
				userStore = factory.connect(remoteIp, user);
				userFolder = userStore.getFolder(targetFolder);
				currentFolder = store.getFolder(folderName);
			}
			
			mailManager.setProcessResource(store, getMessageResource());
			
			if (isMailSearch && isAllFolder) {
            	if ("on".equalsIgnoreCase(userSettingVo.getSearchAllFolder())) {
                    exceptFolders = new String[2];
                    exceptFolders[0] = FolderHandler.RESERVED;
                    exceptFolders[1] = FolderHandler.DRAFTS;
                } else {
                    exceptFolders = new String[4];
                    exceptFolders[0] = FolderHandler.RESERVED;
                    exceptFolders[1] = FolderHandler.DRAFTS;
                    exceptFolders[2] = FolderHandler.SPAM;
                    exceptFolders[3] = FolderHandler.TRASH;
                }
            	folderEncodeName = null;
            }
            
            if (!isMailSearch) {
            	folderEncodeName = null;
            }
            int tagId = Integer.parseInt(tagIdStr);
			
            MailSortMessageBean[] messageBeans = null;
            sortBean.setPageBase(Integer.toString(MAX_SEARCH_COUNT));
            sortBean.setPage("1");
			
            Map<String, List<Long>> folderUidListMap = new HashMap<String, List<Long>>();
            
            messageBeans = getXSortMessageBeans(isMailSearch, sortBean, folderEncodeName, exceptFolders, tagId);
            
            addFolderUidListMap(folderUidListMap, messageBeans);
            
            int total = mailManager.getXCommandTotal();
            int npages = (int) Math.ceil((double) total / MAX_SEARCH_COUNT);
            
            for (int i=2; i<=npages; i++) {
            	sortBean.setPage(Integer.toString(i));
            	
            	messageBeans = getXSortMessageBeans(isMailSearch, sortBean, folderEncodeName, exceptFolders, tagId);
                addFolderUidListMap(folderUidListMap, messageBeans);
            }
            
            if (!folderUidListMap.isEmpty()) {
            	Set<String> keySet = folderUidListMap.keySet();
            	for (String key : keySet) {
            		List<Long> uidList = folderUidListMap.get(key);
            		int batchCount = uidList.size() / MAX_BATCH_COUNT;
            		if (batchCount == 0) {
            			Long[] uidArray = uidList.toArray(new Long[uidList.size()]);
                		long[] uids = ArrayUtils.toPrimitive(uidArray);
            			executeProcessBatch(actionType, key, uids, isShared, isAllFolder, currentFolder, userFolder, targetFolder);
            		} else {            			
            			int batchMod = uidList.size() % MAX_BATCH_COUNT;
            			for (int i=0; i<=batchCount; i++) {
            				List<Long> uidSubList = null;
            				if (i == batchCount) {
            					if (batchMod > 0) {
            						uidSubList = uidList.subList(i * MAX_BATCH_COUNT, (i * MAX_BATCH_COUNT) + batchMod);            						
            					} else {
            						break;
            					}
            				} else {
            					uidSubList = uidList.subList(i * MAX_BATCH_COUNT, (i+1) * MAX_BATCH_COUNT);            					
            				}
            				Long[] uidArray = uidSubList.toArray(new Long[uidSubList.size()]);
                    		long[] uids = ArrayUtils.toPrimitive(uidArray);
                    		executeProcessBatch(actionType, key, uids, isShared, isAllFolder, currentFolder, userFolder, targetFolder);
            			}            			
            		}
            	}
            }
			jObj.put("result", "success");
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			jObj.put("result", "error");
		} finally {
			if(store !=null && store.isConnected())
				store.close();
			
			if(userStore !=null && userStore.isConnected())
				userStore.close();
		}
		
		ResponseUtil.processResponse(response, jObj);		
		return null;
	}
	
    public void workMessageAction(boolean isShared, TMailFolder currentFolder, TMailFolder userFolder, int cnt,
            long[] uids, List<String> folderEncNameList, String actionType, String folderName) throws Exception {
		if(folderName.equals("all")){
			long[] multiUid = null;
			String workFolderName = folderEncNameList.get(0);
			
			List<Long> uidList = new ArrayList<Long>();
			
			for (int i = 0; i < uids.length; i++) {
				if(folderEncNameList.get(i).equals(workFolderName)) {
					uidList.add(uids[i]);
					
					if(i+1 != uids.length){
						continue;
					}
				}

				multiUid =  ArrayUtils.toPrimitive(uidList.toArray(new Long[uidList.size()]));
                
				if(actionType.equals("delete")){
					mailManager.deleteMessage(multiUid, workFolderName);					
					writeMailLog(false,"action_message_delete", workFolderName, "", Long.toString(uids[i]));					
                }
                if (actionType.equals("clean")) {
					mailManager.cleanMessage(multiUid, workFolderName);
					writeMailLog(false,"action_message_clean", workFolderName, "", Long.toString(uids[i]));
                }
                if (actionType.equals("copy")) {
					if(!workFolderName.equals(targetFolder)){
						mailManager.copyMessage(multiUid, workFolderName, targetFolder);
						writeMailLog(false,"action_message_copy", workFolderName, targetFolder, Long.toString(uids[i]));
					}					
                }
                if (actionType.equals("move")) {
					if(!workFolderName.equals(targetFolder)){
						mailManager.moveMessage(multiUid, workFolderName, targetFolder);
						writeMailLog(false,"action_message_move", workFolderName, targetFolder, Long.toString(uids[i]));
					}
				}
				
                workFolderName = folderEncNameList.get(i);
                uidList.clear();
                uidList.add(uids[i]);
			}										
		} else {
			String actionName = null;
			if(actionType.equals("delete")){
				mailManager.deleteMessage(uids, folderName);
				actionName = "action_message_delete";
			}else if(actionType.equals("clean")){
				mailManager.cleanMessage(uids, folderName);
				actionName = "action_message_clean";
			}else if(actionType.equals("move")){
				if(!folderName.equals(targetFolder)){
					mailManager.moveMessage(uids, folderName, targetFolder);
				}
				actionName = "action_message_move";
			}else if(actionType.equals("copy")){
				actionName = "action_message_copy";
				if(isShared){
					mailManager.copySharedMessage(uids, currentFolder, userFolder);					
				} else {
					mailManager.copyMessage(uids, folderName, targetFolder);
				}				
			}			
			
			writeMailLog(false,actionName, folderName, targetFolder, StringUtils.getLongsToString(uids));
				
		}	
	
	}
    
    private void executeProcessBatch(String actionType, String key, long[] uids, boolean isShared, 
    		boolean isAllFolder, TMailFolder currentFolder, TMailFolder userFolder, String targetFolder) throws Exception {
    	String actionName = null;
    	if ("delete".equals(actionType)) {
			actionName = "action_message_delete";
			mailManager.deleteMessage(uids, key);
		} else if ("clean".equals(actionType)) {
			actionName = "action_message_clean";
			mailManager.cleanMessage(uids, key);
		} else if ("copy".equals(actionType)) {
			 actionName = "action_message_copy";
             if (isShared && !isAllFolder) {
                 mailManager.copySharedMessage(uids, currentFolder, userFolder);
             } else {
                 mailManager.copyMessage(uids, key, targetFolder);
             }
		} else if ("move".equals(actionType)) {
			actionName = "action_message_move";
			if (!key.equals(targetFolder)) {
                mailManager.moveMessage(uids, key, targetFolder);
            }
		}
		writeMailLog(false, actionName, key, "", StringUtils.getLongsToString(uids));
	}
    
    private void addFolderUidListMap(Map<String, List<Long>> folderUidListMap, MailSortMessageBean[] messageBeans) {
		if (messageBeans != null) {
			for (MailSortMessageBean bean : messageBeans) {
				String folderName = bean.getFolderEncName();
				long uid = bean.getId();
				addFolderUidList(folderUidListMap, folderName, uid);
			}
		}
	}

	private void addFolderUidList(Map<String, List<Long>> folderUidListMap, String folderName, long uid) {
		if (folderUidListMap.containsKey(folderName)) {
			folderUidListMap.get(folderName).add(uid);
		} else {
			List<Long> uidList = new ArrayList<Long>();
			uidList.add(uid);
			folderUidListMap.put(folderName, uidList);
		}
	}
    
    private MailSortMessageBean[] getXSortMessageBeans(boolean isMailSearch,
			MessageSortInfoBean sortBean, String folderEncodeName,
			String[] exceptFolders, int tagId) throws Exception {
    	MailSortMessageBean[] messageBeans = null;
		if (isMailSearch) {
            messageBeans  = mailManager.getXSortMessageBeans(folderEncodeName, exceptFolders, sortBean);
        } else {
            messageBeans = mailManager.getTagSortMessageBeans(sortBean, tagId);
        }
		return messageBeans;
	}

}
