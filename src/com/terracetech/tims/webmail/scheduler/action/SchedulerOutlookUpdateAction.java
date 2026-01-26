package com.terracetech.tims.webmail.scheduler.action;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.terracetech.tims.webmail.exception.UserAuthException;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class SchedulerOutlookUpdateAction extends SchedulerOutlookBaseAction {

	private static final long serialVersionUID = 20091215L;
	private String scheduledata = null;
	
	private SchedulerManager schedulerManager = null;
	
	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
	
		JSONObject workResult = new JSONObject();
		JSONObject syncResultList = new JSONObject();
		JSONArray successSyncId = new JSONArray();
		JSONArray failSyncId = new JSONArray();
		
		try {
			if (!isCheckAuth()) {
				throw new UserAuthException();
			}
			
			if (StringUtils.isNotEmpty(scheduledata)) {
				JSONObject scheduleObj = (JSONObject)JSONValue.parse(scheduledata);
				if (scheduleObj != null) {
					SchedulerDataVO schedulerDataVo = null;
					if (scheduleObj.containsKey("create")) {
						JSONObject jsonSchedule = (JSONObject)scheduleObj.get("create");
						schedulerDataVo = makeJsonToVo(jsonSchedule, getMailUserSeq());
						try {
							schedulerManager.saveSchedule(schedulerDataVo);
							List<String> repeatIgnoreList = schedulerDataVo.getIgnoreTimeList();
							if (repeatIgnoreList != null && repeatIgnoreList.size() > 0) {
								for (int i=0; i < repeatIgnoreList.size(); i++) {
									schedulerManager.saveRepeatIgnore(schedulerDataVo.getSchedulerId(), repeatIgnoreList.get(i));
								}
							}
							successSyncId.add(schedulerDataVo.getSchedulerId());
						}catch (Exception e) {
							failSyncId.add(schedulerDataVo.getSchedulerId());
						}
						
						syncResultList.put("syncSuccess", successSyncId);
						
					} else if (scheduleObj.containsKey("modify")) {
						JSONArray scheduleList = (JSONArray)scheduleObj.get("modify");
					
						if (scheduleList != null && scheduleList.size() > 0) {
							JSONObject jsonSchedule = null;
							for (int i=0; i<scheduleList.size(); i++) {
								schedulerDataVo = new SchedulerDataVO();
								jsonSchedule = (JSONObject)scheduleList.get(i);
								if (jsonSchedule != null) {
									if ("true".equalsIgnoreCase((String)jsonSchedule.get("share"))) {
										continue;
									}
									schedulerDataVo = makeJsonToVo(jsonSchedule, getMailUserSeq());

									try {
										schedulerManager.modifySchedule(schedulerDataVo);
										List<String> repeatIgnoreList = schedulerDataVo.getIgnoreTimeList();
										if (repeatIgnoreList != null && repeatIgnoreList.size() > 0) {
											
											for (int j=0; j < repeatIgnoreList.size(); j++) {
												schedulerManager.saveRepeatIgnore(schedulerDataVo.getSchedulerId(), repeatIgnoreList.get(j));
											}
										}
										successSyncId.add(schedulerDataVo.getSchedulerId());
									} catch (Exception e) {
										failSyncId.add(schedulerDataVo.getSchedulerId());
									}
									
									syncResultList.put("syncSuccess", successSyncId);
									syncResultList.put("syncFail", failSyncId);
								}
							}
						}
					}
				}
			}
					
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

	public void setScheduledata(String scheduledata) {
		this.scheduledata = scheduledata;
	}

	private SchedulerDataVO makeJsonToVo(JSONObject jsonSchedule, int mailUserSeq) {
		SchedulerDataVO schedulerDataVo = null;
		if (jsonSchedule != null) {
			schedulerDataVo = new SchedulerDataVO();
			schedulerDataVo.setMailUserSeq(mailUserSeq);
			schedulerDataVo.setTitle((String)jsonSchedule.get("title"));
			schedulerDataVo.setLocation((String)jsonSchedule.get("location"));
			schedulerDataVo.setContent((String)jsonSchedule.get("content"));
			schedulerDataVo.setStartDate((String)jsonSchedule.get("start_date"));
			schedulerDataVo.setEndDate((String)jsonSchedule.get("end_date"));
			schedulerDataVo.setAllDay((String)jsonSchedule.get("all_day"));
			schedulerDataVo.setHoliday((String)jsonSchedule.get("holiday"));
			schedulerDataVo.setRepeatFlag((String)jsonSchedule.get("repeat_flag"));
			schedulerDataVo.setRepeatTerm((String)jsonSchedule.get("repeat_term"));
			schedulerDataVo.setRepeatEndDate((String)jsonSchedule.get("repeat_end_date"));
			schedulerDataVo.setOutlookSync("sync");
			
			if (jsonSchedule.containsKey("scheduler_id")) {
				schedulerDataVo.setSchedulerId(Integer.parseInt((String)jsonSchedule.get("scheduler_id")));
			} else {
				schedulerDataVo.setCreateTime(FormatUtil.getBasicDateStr());
			}
			
			if (jsonSchedule.containsKey("check_share")) {
				if ("on".equalsIgnoreCase((String)jsonSchedule.get("check_share"))) {
					schedulerDataVo.setCheckShare("on");
					schedulerDataVo.setShareValue((String)jsonSchedule.get("share_value"));
				} else {
					schedulerDataVo.setCheckShare("off");
				}
			} else {
				schedulerDataVo.setCheckShare("off");
			}
			
			if (jsonSchedule.containsKey("repeat_ignore")) {
				JSONArray ignoreTimeList = (JSONArray)jsonSchedule.get("repeat_ignore");
				if (ignoreTimeList != null && ignoreTimeList.size() > 0) {
					List<String> repeatIgnoreList = new ArrayList<String>();
					for (int i=0; i < ignoreTimeList.size(); i++) {
						repeatIgnoreList.add((String)ignoreTimeList.get(i));
					}
					schedulerDataVo.setIgnoreTimeList(repeatIgnoreList);
				}
			}
		}
		return schedulerDataVo;
	}
	
}
