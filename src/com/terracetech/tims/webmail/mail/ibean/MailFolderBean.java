/**
 * MailFolderBean.java 2008. 11. 25.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.ibean;

import org.json.simple.JSONObject;

import com.terracetech.tims.mail.TMailFolder;

/**
 * <p><strong>MailFolderBean.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li>메일함 정보 클래스. 각 메일함의 주요정보 필드를 가지며 해당 폴더 정보도 포함 시킨다.</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailFolderBean {
	
	private TMailFolder folder = null;
	private String id = null;
	private String name = null;
	private String fullName = null;	
	private String encName = null;
	private boolean share = false;
	private int sharedUid = 0;
	private int depth = 0;
	private String parentFullName = null;
	private String parentId = null;
	private long againg = -1;
	
	private long newCnt = -1;
	private long unseenCnt = -1;
	private long totalCnt = -1;
	
	private MailQuotaBean quota = null;
	
	
	/**
	 * <p>생성자. 폴더를 받아 생성</p>
	 *
	 * @param folder		폴더객체
	 */
	public MailFolderBean(TMailFolder folder) {
		initrBean(null, folder);
	}
	
	/**
	 * <p>생성자 폴더와  id를 받아 생성</p>
	 *
	 * @param id			폴더 아이디
	 * @param folder		폴더 객체
	 */
	public MailFolderBean(String id, TMailFolder folder) {
		initrBean(id, folder);
	}	
	
	
	/**
	 * <p>폴더 정보를 초기에 저장</p>
	 *
	 * @param id			폴더 아이디
	 * @param folder		폴더 객체
	 * @return void
	 */
	private void initrBean(String id, TMailFolder folder) {
		this.id = id;
		this.folder = folder;
		this.fullName = folder.getFullName();
		this.encName = folder.getEncName();
		this.name = folder.getAlias4subFolder();
		this.depth = folder.getFolderDepth()-1;
		settingDepthName();
	}


	/**
	 * @return folder 값 반환
	 */
	public TMailFolder getFolder() {
		return folder;
	}

	/**
	 * @param folder 파라미터를 folder값에 설정
	 */
	public void setFolder(TMailFolder folder) {
		this.folder = folder;
	}

	/**
	 * @return id 값 반환
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id 파라미터를 id값에 설정
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return name 값 반환
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 파라미터를 name값에 설정
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return fullName 값 반환
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName 파라미터를 fullName값에 설정
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return encName 값 반환
	 */
	public String getEncName() {
		return encName;
	}

	/**
	 * @param encName 파라미터를 encName값에 설정
	 */
	public void setEncName(String encName) {
		this.encName = encName;
	}

	/**
	 * @return share 값 반환
	 */
	public boolean isShare() {
		return share;
	}

	/**
	 * @param share 파라미터를 share값에 설정
	 */
	public void setShare(boolean share) {
		this.share = share;
	}

	public int getSharedUid() {
		return sharedUid;
	}

	public void setSharedUid(int sharedUid) {
		this.sharedUid = sharedUid;
	}

	/**
	 * @return depth 값 반환
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth 파라미터를 depth값에 설정
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * @return parentFullName 값 반환
	 */
	public String getParentFullName() {
		return parentFullName;
	}

	/**
	 * @param parentFullName 파라미터를 parentFullName값에 설정
	 */
	public void setParentFullName(String parentFullName) {
		this.parentFullName = parentFullName;
	}

	/**
	 * @return parentId 값 반환
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId 파라미터를 parentId값에 설정
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return newCnt 값 반환
	 */
	public long getNewCnt() {
		return newCnt;
	}

	/**
	 * @param newCnt 파라미터를 newCnt값에 설정
	 */
	public void setNewCnt(long newCnt) {
		this.newCnt = newCnt;
	}

	/**
	 * @return unseenCnt 값 반환
	 */
	public long getUnseenCnt() {
		return unseenCnt;
	}

	/**
	 * @param unseenCnt 파라미터를 unseenCnt값에 설정
	 */
	public void setUnseenCnt(long unseenCnt) {
		this.unseenCnt = unseenCnt;
	}

	/**
	 * @return againg 값 반환
	 */
	public long getAgaing() {
		return againg;
	}

	/**
	 * @param againg 파라미터를 againg값에 설정
	 */
	public void setAgaing(long againg) {
		this.againg = againg;
	}

	/**
	 * @return totalCnt 값 반환
	 */
	public long getTotalCnt() {
		return totalCnt;
	}

	/**
	 * @param totalCnt 파라미터를 totalCnt값에 설정
	 */
	public void setTotalCnt(long totalCnt) {
		this.totalCnt = totalCnt;
	}

	/**
	 * <p>폴더의 정보를  JSON OBJECT 로 구성 하여 반환</p>	
	 * 
	 * @return JSONObject			폴더 정보 JSONObject
	 */
	@SuppressWarnings("all")
	public JSONObject toJson(){
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("name", name);
		json.put("fullName", fullName);
		json.put("encName", encName);
		json.put("depth", depth);
		json.put("parentid", parentId);
		json.put("newCnt", newCnt);
		json.put("unseenCnt", unseenCnt);
		json.put("totalCnt", totalCnt);
		json.put("share", share);
		json.put("sharedUid", sharedUid);
		
		return json;
	}
	
	/**
	 * <p>현재 폴더의 상위 폴더이름을 설정</p>
	 *
	 * @return void
	 */
	private void settingDepthName() {
		if (this.fullName != null) {
			int dotIdx = fullName.lastIndexOf(".");
			if (dotIdx > 0) {
				this.parentFullName = fullName.substring(0, dotIdx);
			}
			
		} 
	}

	/**
	 * @return quota 값 반환
	 */
	public MailQuotaBean getQuota() {
		return quota;
	}

	/**
	 * @param quota 파라미터를 quota값에 설정
	 */
	public void setQuota(MailQuotaBean quota) {
		this.quota = quota;
	}
	
		
	
	
}
