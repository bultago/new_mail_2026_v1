package com.terracetech.tims.hybrid.mail.action;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.tag.TMailTag;
import com.terracetech.tims.service.manager.IMailServiceManager;
import com.terracetech.tims.service.tms.vo.ListCondVO;
import com.terracetech.tims.service.tms.vo.ListContentVO;
import com.terracetech.tims.service.tms.vo.ListInfoVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailListServiceAction extends BaseAction {
    
    private MailManager mailManager = null;
    private IMailServiceManager mailServiceManager = null;
    private String flag = null;
    private String folderName;
    private int page;
    private int pageBase;
    private String sortBy;
    private String sortDir;
    
    private String adv;
    private String category;
    private String keyWord;
    private String faddr;
    private String taddr;
    private String fdate;
    private String tdate;
    
    private String listType;
    private String tagId;

    
    public void setMailManager(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    public void setMailServiceManager(IMailServiceManager mailServiceManager) {
        this.mailServiceManager = mailServiceManager;
    }
    
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        I18nResources msgResource = getMessageResource("mail");
        TMailStore store = null;
        TMailStoreFactory factory = new TMailStoreFactory();
        Map<String, String> tagMap = new HashMap<String, String>();
        ListInfoVO listInfoVO = null;
        JSONObject result = null;
        int errorCode = HybridErrorCode.SUCCESS;
        
        char flagType = (flag != null && flag.length() > 0) ? flag.charAt(0) : 'x';
        page = (page == 0) ? 1 : page;
        pageBase = (pageBase == 0) ? 15 : pageBase;
        sortBy = (StringUtils.isEmpty(sortBy)) ? "arrival" : sortBy;
        sortDir = (StringUtils.isEmpty(sortDir)) ? "desc" : sortDir;
        folderName = (StringUtils.isEmpty(folderName)) ? FolderHandler.INBOX : folderName;
        boolean isAdSearch = ("on".equals(adv)) ? true : false;
        try {
            store = factory.connect(remoteIp, user);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            
            Thread.sleep(2000);
            try {
                store = factory.connect(remoteIp, user);
            } catch (Exception e2) {
                LogManager.writeErr(this, e.getMessage(), e);
            }
        }
        
        try {
            mailManager.setProcessResource(store, getMessageResource());
            TMailTag[] tagList = mailManager.getTagList(null);
            if (tagList != null && tagList.length > 0) {
	            for (TMailTag tag : tagList) {
	                tagMap.put(tag.getName(), tag.getColor().replaceAll("#", ""));
	            }
            }
        }catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        
        
        ListCondVO listVO = new ListCondVO();
        listVO.setRemoteIp(remoteIp);
        listVO.setEmail(user.get(User.EMAIL));
        listVO.setFolderName(folderName);
        listVO.setPage(page);
        listVO.setPageBase(pageBase);
        listVO.setLocale(user.get(User.LOCALE));
        listVO.setSortBy(sortBy);
        listVO.setSortDir(sortDir);
        
        
        listVO.setAdvancedSearch(isAdSearch);
        listVO.setOperation("and");
        listVO.setCategory(category);
        listVO.setKeyWord(keyWord);
        listVO.setFromEmailPattern(faddr);
        listVO.setToEmailPattern(taddr);
        listVO.setFromDate(fdate);
        listVO.setToDate(tdate);
        
        listVO.setFlagType(flagType);
        listVO.setListType(listType);
        listVO.setTagId(tagId);
        
        try {
            listInfoVO = mailServiceManager.doSimpleMailList(listVO, user);
        
            result = makeMailListJson(msgResource, listInfoVO, tagMap);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        } finally {
            if (store != null && store.isConnected()) {
                store.close();
            }
        }
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, result);
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private JSONObject makeMailListJson(I18nResources msgResource, ListInfoVO listInfoVO, Map<String,String> tagMap) {
        JSONObject result = new JSONObject();
        result.put("name", StringUtils.EscapeHTMLTag(listInfoVO.getFolderName()));
        result.put("display", StringUtils.EscapeHTMLTag(listInfoVO.getFolderAliasName()));
        result.put("page", listInfoVO.getPageNo());
        result.put("pageBase", listInfoVO.getSearchCnt());
        int total = listInfoVO.getTotalCnt();
        if (!StringUtils.isEmpty(keyWord)) {
        	total = (listInfoVO.getListContents() == null || listInfoVO.getListContents().length == 0) ? 0 : listInfoVO.getListContents().length;
        }
        result.put("total", total);
        result.put("unseen", listInfoVO.getUnreadCnt());
      
        JSONArray jsonMailList = convertMailListInfo(msgResource, listInfoVO.getListContents(), tagMap);
        result.put("mailList", jsonMailList);
        
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private JSONArray convertMailListInfo(I18nResources msgResource, ListContentVO[] listContentVOs, Map<String,String> tagMap) {
        JSONArray jsonArray = new JSONArray();
        if (listContentVOs != null && listContentVOs.length > 0) {
            JSONObject jsonObject = null;
            for (ListContentVO listContentVO : listContentVOs) {
                jsonObject = new JSONObject();
                jsonObject.put("fname", StringUtils.EscapeHTMLTag(listContentVO.getFolderName()));
                jsonObject.put("fdisplay", getFolderDispalyName(msgResource, listContentVO.getFolderDepthName()));
                jsonObject.put("uid", listContentVO.getId());
                jsonObject.put("name", StringUtils.EscapeHTMLTag(listContentVO.getName()));
                jsonObject.put("email", StringUtils.EscapeHTMLTag(listContentVO.getEmail()));
                jsonObject.put("subject", StringUtils.EscapeHTMLTag(listContentVO.getSubject()));
                jsonObject.put("date", listContentVO.getDate());
                jsonObject.put("flag", listContentVO.getFlag());
                jsonObject.put("preview", StringUtils.EscapeHTMLTag(listContentVO.getPreview()));
                
                String tagNameList = listContentVO.getTagNameList();
                int tagCount = 0;
                String tagColor = "";
                if (!StringUtils.isEmpty(tagNameList)) {
                    String[] tagNameArray = tagNameList.split("\\|");
                    tagCount = tagNameArray.length;
                    tagColor = tagMap.get(tagNameArray[0]);
                }
                jsonObject.put("tagCount", tagCount);
                jsonObject.put("tagColor", tagColor);
                
                jsonArray.add(jsonObject);
            }
        }
        
        return jsonArray;
    }
    
    private String getFolderDispalyName(I18nResources msgResource, String folderName) {
        if("Inbox".equals(folderName)){
            folderName = msgResource.getMessage("folder.inbox") ;
        } else if("Sent".equals(folderName)){
            folderName = msgResource.getMessage("folder.sent") ;
        } else if("Drafts".equals(folderName)){
            folderName = msgResource.getMessage("folder.drafts") ;
        } else if("Reserved".equals(folderName)){
            folderName = msgResource.getMessage("folder.reserved") ;
        } else if("Spam".equals(folderName)){
            folderName = msgResource.getMessage("folder.spam") ;
        } else if("Trash".equals(folderName)){
            folderName = msgResource.getMessage("folder.trash") ;
        }
        
        return folderName;      
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageBase(int pageBase) {
        this.pageBase = pageBase;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public void setAdv(String adv) {
        this.adv = adv;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFaddr(String faddr) {
        this.faddr = faddr;
    }

    public void setTaddr(String taddr) {
        this.taddr = taddr;
    }

    public void setFdate(String fdate) {
        this.fdate = fdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }
    
    
    
}
