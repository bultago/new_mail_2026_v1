package com.terracetech.tims.service.samsung;

import java.rmi.Remote;

import com.terracetech.tims.service.samsung.vo.AttachEtyCSVO;
import com.terracetech.tims.service.samsung.vo.ConditionCSVO;
import com.terracetech.tims.service.samsung.vo.DetailESBVO;
import com.terracetech.tims.service.samsung.vo.ExtractedAttachESBVO;
import com.terracetech.tims.service.samsung.vo.HeaderHelperCSVO;
import com.terracetech.tims.service.samsung.vo.ListESBVO;
import com.terracetech.tims.service.samsung.vo.RecipientEtyCSVO;
import com.terracetech.tims.service.samsung.vo.ResourceCSVO;
import com.terracetech.tims.service.samsung.vo.SyncListCSVO;


public interface IMailService extends Remote {
	
	public final static String ERR_UNKNOWN = "ERROR_000";

	public void setRemoteIp(String remoteIp);

	/**
	 * 메일발신에 필요한 필수 파라미터들을 이용해서 메일을 전송하고, 정상 발신된 메일은 고유한 msg-key값 리턴
	 * 
	 * @param bodyStr
	 * @param headerHelperCSVO
	 * @param recipientEtyCSVO
	 * @param attachEtyCSVO
	 * @param resourceCSVO
	 * @return
	 */
	public String sendMISMail(String bodyStr,
			HeaderHelperCSVO headerHelperCSVO,
			RecipientEtyCSVO[] recipientEtyCSVO, AttachEtyCSVO[] attachEtyCSVO,
			ResourceCSVO resourceCSVO);

	/**
	 * 목록 선택옵션정보(listOption), 에이징을 적용할지를 판단하는 현재시간정보(currentTime),
	 * 검색조건정보(conditionCSVO), 메일함 커넥션을 위한 기본정보(resourceCSVO) 등으로 오퍼레이션을 호출하여 메일
	 * 목록 정보를 담은 ListESBVO 리턴
	 * 
	 * @param listOption
	 * @param currentTime
	 * @param conditionCSVO
	 * @param resourceCSVO
	 * @return
	 */
	public ListESBVO getSimpleMailList(String listOption, String currentTime,
			ConditionCSVO conditionCSVO, ResourceCSVO resourceCSVO);

	/**
	 * 메일함 내의 메시지 번호(msgNo), 원하는 메일함정보(folderName), 메일함 커넥션을 위한
	 * 기본정보(resourceCSVO) 등을 이용하여 오퍼레이션을 호출하면 메일 목록 정보를 담은 ListESBVO 리턴
	 * 
	 * @param msgNo
	 * @param folderName
	 * @param resourceCSVO
	 * @return
	 */
	public ListESBVO getMailListAfterMsgNo(int msgNo, String folderName,
			ResourceCSVO resourceCSVO);

	/**
	 * 메일함 내의 메시지 번호(msgNo), 원하는 메일함정보(folderName), 검색조건정보(conditionCSVO), 메일함
	 * 커넥션을 위한 기본정보(resourceCSVO) 등을 이용하여 오퍼레이션을 호출하면 메일 상세 정보를 담은 DetailESBVO
	 * 리턴
	 * 
	 * @param msgNo
	 * @param conditionCSVO
	 * @param resourceCSVO
	 * @return
	 */
	public DetailESBVO getMailDetailView(int msgNo,
			ConditionCSVO conditionCSVO, ResourceCSVO resourceCSVO);

	/**
	 * 메일 메시지 UID(msgUID), 원하는 메일함정보(folderName), 검색조건정보(conditionCSVO), 메일함
	 * 커넥션을 위한 기본정보(resourceCSVO) 등을 이용하여 오퍼레이션을 호출하면 메일 상세 정보를 담은 DetailESBVO 리
	 * 
	 * @param msgUID
	 * @param conditionCSVO
	 * @param resourceCSVO
	 * @return
	 */
	public DetailESBVO getMailDetailViewByUID(String msgUID,
			ConditionCSVO conditionCSVO, ResourceCSVO resourceCSVO);

	/**
	 * 메일 메시지 UID(msgUID), part number 정보 배열(partNos), 첨부파일의 확장자명 배열(exts), 원하는
	 * 메일함정보(folderName), 메일함 커넥션을 위한 기본정보(resourceCSVO) 등을 이용하여 오퍼레이션을 호출하면 메일
	 * body내에 포함된 첨부파일을 다운받을 수 있도록 추출된 다운로드 정보(ExtractedAttachESBVO) 를 리턴
	 * 
	 * @param msgUID
	 * @param partNos
	 * @param exts
	 * @param folderName
	 * @param resourceCSVO
	 * @return
	 */
	public ExtractedAttachESBVO getExtractedAttachInfo(String msgUID,
			String[] partNos, String[] exts, String folderName,
			ResourceCSVO resourceCSVO);

	/**
	 * 
	 * @param msgUID
	 * @param folderName
	 * @param resourceCSVO
	 * @return
	 */
	public String getMailDetailBody(String msgUID, String folderName,
			ResourceCSVO resourceCSVO);

	/**
	 * 메일서버 내의 메일정보와 동기화시킬 대상 메일목록(syncListESBVO)를 넘겨주면, 개봉시간이 있는 정보는 개봉여부를 업데이트
	 * 하고 개봉시간이 없는 정보는 현재의 서버상태를 다시 목록정보로 구성하여 리턴
	 * 
	 * @param syncListCSVO
	 * @param resourceCSVO
	 * @return
	 */
	public SyncListCSVO syncMails(SyncListCSVO syncListCSVO,
			ResourceCSVO resourceCSVO);
}
