package com.terracetech.tims.mobile.calendar.action;

import org.json.simple.JSONArray;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.CalendarService;
import com.terracetech.tims.service.tms.vo.CalendarAssetCondVO;
import com.terracetech.tims.service.tms.vo.CalendarCondVO;
import com.terracetech.tims.service.tms.vo.CalendarWriteVO;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class AssetCalendarAction extends BaseAction {

	private static final long serialVersionUID = 5319310245595674335L;
	
	private String startDate = null;
	private String endDate = null;
	private int schedulerId = 0;
	private int[] assetSeqs = null;
	
	private CalendarWriteVO calendarWriteVo = null;
	private CalendarService calendarService = null;
	
	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	public String ajax() throws Exception {
		
		CalendarAssetCondVO calendarAssetCondVO = new CalendarAssetCondVO();
		calendarAssetCondVO.setStartDate(startDate);
		calendarAssetCondVO.setEndDate(endDate);
		calendarAssetCondVO.setSchedulerId(schedulerId);
		calendarAssetCondVO.setAssetSeqs(assetSeqs);
		
		JSONArray result = calendarService.readJsonAssetUseList(calendarAssetCondVO ,user);
		request.setAttribute("data", result.toString());		
		return "success";
	}
	
	public String execute() throws Exception {
		
		CalendarCondVO calendarCondVo = new CalendarCondVO();
		calendarWriteVo = calendarService.getWriteCalendar(calendarCondVo, user);
		
		String forward = "success";
		
		if (calendarWriteVo == null || calendarWriteVo.getAssetList() == null || calendarWriteVo.getAssetList().length == 0) {
			forward = "empty";
		}
		
		return forward;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setSchedulerId(int schedulerId) {
		this.schedulerId = schedulerId;
	}

	public void setAssetSeqs(int[] assetSeqs) {
		this.assetSeqs = assetSeqs;
	}
	
	public CalendarWriteVO getCalendarWriteVo() {
		return calendarWriteVo;
	}
}
