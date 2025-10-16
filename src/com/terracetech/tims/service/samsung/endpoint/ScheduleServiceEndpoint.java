package com.terracetech.tims.service.samsung.endpoint;

import com.terracetech.tims.service.samsung.IScheduleService;
import com.terracetech.tims.service.samsung.vo.AttachWSVO;
import com.terracetech.tims.service.samsung.vo.ScheduleWSVO;

public class ScheduleServiceEndpoint implements IScheduleService {

	public int addSchedule(String licenseKey, ScheduleWSVO param) {
		
		return 0;
	}

	public int addScheduleWithAttach(String licenseKey, ScheduleWSVO param,
			AttachWSVO[] attach) {
		
		return 0;
	}

	public int delSchedule(String licenseKey, ScheduleWSVO param) {
		
		return 0;
	}

	public ScheduleWSVO getSchedule(String licenseKey, ScheduleWSVO param) {
		
		return new ScheduleWSVO();
	}

	public ScheduleWSVO[] getScheduleList(String licenseKey, ScheduleWSVO param) {
		
		return null;
	}

	public int modSchedule(String licenseKey, ScheduleWSVO param) {
		
		return 0;
	}

	public int modScheduleWithAttach(String licenseKey, ScheduleWSVO param,
			AttachWSVO[] attach) {
		
		return 0;
	}

}
