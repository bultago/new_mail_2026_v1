package com.terracetech.tims.webmail.scheduler.action;

import java.util.List;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.terracetech.tims.webmail.exception.UserAuthException;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class SchedulerOutlookSyncAction extends SchedulerOutlookBaseAction {

	private static final long serialVersionUID = 20091215L;
	private String startDate = null;
	private String syncType = null;
	private String scheduleId = null;

	private SchedulerManager schedulerManager = null;
	
	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		
		JSONObject workResult = new JSONObject();
		JSONObject jsonSchedulerIds = null;
		
		try {
			if (!isCheckAuth()) {
				throw new UserAuthException();
			}
			
			if (StringUtils.isNotEmpty(scheduleId)) {
				jsonSchedulerIds = (JSONObject)JSONValue.parse(scheduleId);
				if (jsonSchedulerIds.containsKey("schedulerIds")) {
					JSONArray scheduleIdArray = (JSONArray)jsonSchedulerIds.get("schedulerIds");
					int[] schedulerIds = null;
					if (scheduleIdArray != null && scheduleIdArray.size() > 0) {
						schedulerIds = new int[scheduleIdArray.size()];
						for (int i=0; i<scheduleIdArray.size(); i++) {
							schedulerIds[i] = Integer.parseInt((String)scheduleIdArray.get(i));
						}
						List<SchedulerDataVO> scheduleList = schedulerManager.getSchedulerListByIds(schedulerIds);
						JSONArray scheduleJsonList = makeJsonScheduleList(scheduleList, getMailUserSeq());
						workResult.put("schedule", scheduleJsonList);
					}
				}
			}
			workResult.put("auth", "success");	
		}catch (UserAuthException e) {
			workResult.put("auth", "fail");
		}catch (Exception e) {
			workResult.put("auth", "error");
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		
		ResponseUtil.processResponse(response, workResult);
		
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String executePart() throws Exception {
	
		JSONObject workResult = new JSONObject();
		JSONArray syncList = null;
		StringTokenizer st = null;
		String[] syncTypeArray = {"sync", "unsync", "delete"};
		String[] syncArray = null;
		try {
			if (!isCheckAuth()) {
				throw new UserAuthException();
			}
			
			if (StringUtils.isEmpty(syncType)) {
				syncArray = syncTypeArray;
			} else {
				st = new StringTokenizer(syncType, ",");
				if (st.countTokens() > 0) {
					syncArray = new String[st.countTokens()];
					int count = 0;
					while (st.hasMoreTokens()) {
						syncArray[count++] = st.nextToken();
					}
				}
			}
			
			List<Integer> syncTypeIdList = null;
			if (syncArray != null && syncArray.length > 0) {
				for (int i=0; i<syncArray.length; i++) {
					syncTypeIdList = schedulerManager.getSchedulerIdOutlookSyncStatus(getMailUserSeq(), syncArray[i], startDate);
					if (syncTypeIdList != null && syncTypeIdList.size() > 0) {
						syncList = new JSONArray();
						for (int j=0; j<syncTypeIdList.size(); j++) {
							syncList.add(syncTypeIdList.get(j));
						}
						workResult.put(syncArray[i], syncList);
					}
				}
			}
			workResult.put("auth", "success");
		}catch (UserAuthException e) {
			workResult.put("auth", "fail");
		}catch (Exception e) {
			workResult.put("auth", "error");
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		
		ResponseUtil.processResponse(response, workResult);
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String executeShare() throws Exception {
	
		JSONObject workResult = new JSONObject();
		JSONArray modiftTimeList = null;
	
		try {
			if (!isCheckAuth()) {
				throw new UserAuthException();
			}
			
			int [] shareScheduleIds = schedulerManager.readSchedulerShareIds(getMailDomainSeq(), getMailUserSeq(), getUserId()+"@"+getMailDomain());
			
			if (shareScheduleIds != null) {
				List<SchedulerDataVO> shareModifyTimeList = schedulerManager.getSchedulerShareModifyTime(shareScheduleIds, startDate);
				if (shareModifyTimeList != null && shareModifyTimeList.size() > 0) {
					modiftTimeList = new JSONArray();
					JSONObject modifyTimeObj = null; 
					for (int i=0; i<shareModifyTimeList.size(); i++) {
						modifyTimeObj = new JSONObject();
						modifyTimeObj.put("scheduler_id", shareModifyTimeList.get(i).getSchedulerId());
						modifyTimeObj.put("modify_time", shareModifyTimeList.get(i).getModifyTime());
						modiftTimeList.add(modifyTimeObj);
					}
					workResult.put("info", modiftTimeList);
				}
			}
			
			workResult.put("auth", "success");
		}catch (UserAuthException e) {
			workResult.put("auth", "fail");
		}catch (Exception e) {
			workResult.put("auth", "error");
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		
		ResponseUtil.processResponse(response, workResult);
		
		return null;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setSyncType(String syncType) {
		this.syncType = syncType;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	@SuppressWarnings("unchecked")
	private JSONArray  makeJsonScheduleList(List<SchedulerDataVO> scheduleList, int mailUserSeq) {
		JSONArray jsonScheduleList = new JSONArray();
		if (scheduleList != null && scheduleList.size() > 0) {
			JSONObject jsonSchedule = null;
			List<SchedulerDataVO> repeatIgnoreList = null;
			for (int i=0; i<scheduleList.size(); i++) {
				jsonSchedule = new JSONObject();
				jsonSchedule.put("scheduler_id", scheduleList.get(i).getSchedulerId());
				jsonSchedule.put("title", scheduleList.get(i).getTitle());
				jsonSchedule.put("start_date", scheduleList.get(i).getStartDate());
				jsonSchedule.put("end_date", scheduleList.get(i).getEndDate());
				jsonSchedule.put("location", scheduleList.get(i).getLocation());
				jsonSchedule.put("content", scheduleList.get(i).getContent());
				jsonSchedule.put("all_day", scheduleList.get(i).getAllDay());
				jsonSchedule.put("holiday", scheduleList.get(i).getHoliday());
				jsonSchedule.put("repeat_flag", scheduleList.get(i).getRepeatFlag());
				jsonSchedule.put("repeat_term", scheduleList.get(i).getRepeatTerm());
				jsonSchedule.put("repeat_end_date", scheduleList.get(i).getRepeatEndDate());
				
				if (scheduleList.get(i).getShareSeq() > 0) {
					jsonSchedule.put("check_share", "on");
					jsonSchedule.put("share_value", Integer.toString(scheduleList.get(i).getShareSeq()));
				} else {
					jsonSchedule.put("check_share", "off");
				}
				
				if (mailUserSeq != scheduleList.get(i).getMailUserSeq()) {
					jsonSchedule.put("share", "true");
				} else {
					jsonSchedule.put("share", "false");
				}
				
				repeatIgnoreList = schedulerManager.readRepeatIgnoreList(scheduleList.get(i).getSchedulerId());
				
				if (repeatIgnoreList != null && repeatIgnoreList.size() > 0) {
					JSONArray ignoreDateList = new JSONArray();
					for (int j=0; j<repeatIgnoreList.size(); j++) {
						ignoreDateList.add(repeatIgnoreList.get(j).getIgnoreTime());
					}
					jsonSchedule.put("repeat_ignore", ignoreDateList);
				}

				jsonScheduleList.add(jsonSchedule);
			}
		}
		return jsonScheduleList;
	}
}
