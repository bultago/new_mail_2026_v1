package com.terracetech.tims.webmail.scheduler.action;

import org.json.simple.*;

import com.terracetech.tims.webmail.exception.UserAuthException;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class SchedulerOutlookReceiveAction extends SchedulerOutlookBaseAction {

	private static final long serialVersionUID = 20091215L;

	private String[] itemList = null;

	private SchedulerManager schedulerManager = null;
	
	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}
	
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		JSONObject jsonItem = null;
		JSONObject workResult = new JSONObject();
		JSONObject syncResultList = new JSONObject();
		JSONObject deleteResultList = new JSONObject();
		
		try {
			if (!isCheckAuth()) {
				throw new UserAuthException();
			}
			
			if (itemList != null && itemList.length > 0) {
				jsonItem = new JSONObject();
				for (int i=0; i<itemList.length; i++) {
					jsonItem = (JSONObject)JSONValue.parse(itemList[i]);
					if (jsonItem.containsKey("delete")) {
						JSONArray deleteList = (JSONArray)jsonItem.get("delete");
						if (deleteList != null && deleteList.size() > 0) {
							String deleteScheduleId = null;
							String deleteResult = null;
							JSONArray successDeleteId = new JSONArray();
							JSONArray failDeleteId = new JSONArray();
		
							for (int j=0; j<deleteList.size(); j++) {
								deleteScheduleId = (String)deleteList.get(j);
								deleteResult = schedulerManager.deleteScheduleComplete(Integer.parseInt(deleteScheduleId));
								if ("fail".equals(deleteResult)) {
									failDeleteId.add(deleteScheduleId);
								} else {
									successDeleteId.add(deleteScheduleId);
								}
							}
							deleteResultList.put("deleteSuccess", successDeleteId);
							deleteResultList.put("deleteFail", failDeleteId);
						}
					}
					else if (jsonItem.containsKey("sync")) {
						JSONArray syncList = (JSONArray)jsonItem.get("sync");
						if (syncList != null && syncList.size() > 0) {
							String syncScheduleId = null;
							JSONArray successSyncId = new JSONArray();
							JSONArray failSyncId = new JSONArray();
							for (int j=0; j<syncList.size(); j++) {
								syncScheduleId = (String)syncList.get(j);
								try {
									schedulerManager.changeSyncFlag(Integer.parseInt(syncScheduleId),"sync");
									successSyncId.add(syncScheduleId);
								} catch (Exception e) {
									failSyncId.add(syncScheduleId);
								}
							}
							syncResultList.put("syncSuccess", successSyncId);
							syncResultList.put("syncFail", failSyncId);
						}
					}
				}
			}
			workResult.put("delete", deleteResultList);
			workResult.put("sync", syncResultList);
			workResult.put("auth", "success");
		
		}catch (UserAuthException e) {
			workResult.put("auth", "fail");
		}catch (Exception e) {
			workResult.put("auth", "error");
		}
		
		ResponseUtil.processResponse(response, workResult);
		
		return null;
	}
	
	public void setItemList(String[] itemList) {
		this.itemList = itemList;
	}
}
