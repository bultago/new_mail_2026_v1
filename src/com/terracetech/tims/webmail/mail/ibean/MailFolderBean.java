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
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li>¸ÞÀÏÇÔ Á¤º¸ Å¬·¡½º. °¢ ¸ÞÀÏÇÔÀÇ ÁÖ¿äÁ¤º¸ ÇÊµå¸¦ °¡Áö¸ç ÇØ´ç Æú´õ Á¤º¸µµ Æ÷ÇÔ ½ÃÅ²´Ù.</li>
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
	 * <p>»ý¼ºÀÚ. Æú´õ¸¦ ¹Þ¾Æ »ý¼º</p>
	 *
	 * @param folder		Æú´õ°´Ã¼
	 */
	public MailFolderBean(TMailFolder folder) {
		initrBean(null, folder);
	}
	
	/**
	 * <p>»ý¼ºÀÚ Æú´õ¿Í  id¸¦ ¹Þ¾Æ »ý¼º</p>
	 *
	 * @param id			Æú´õ ¾ÆÀÌµð
	 * @param folder		Æú´õ °´Ã¼
	 */
	public MailFolderBean(String id, TMailFolder folder) {
		initrBean(id, folder);
	}	
	
	
	/**
	 * <p>Æú´õ Á¤º¸¸¦ ÃÊ±â¿¡ ÀúÀå</p>
	 *
	 * @param id			Æú´õ ¾ÆÀÌµð
	 * @param folder		Æú´õ °´Ã¼
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
	 * @return folder °ª ¹ÝÈ¯
	 */
	public TMailFolder getFolder() {
		return folder;
	}

	/**
	 * @param folder ÆÄ¶ó¹ÌÅÍ¸¦ folder°ª¿¡ ¼³Á¤
	 */
	public void setFolder(TMailFolder folder) {
		this.folder = folder;
	}

	/**
	 * @return id °ª ¹ÝÈ¯
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id ÆÄ¶ó¹ÌÅÍ¸¦ id°ª¿¡ ¼³Á¤
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return name °ª ¹ÝÈ¯
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name ÆÄ¶ó¹ÌÅÍ¸¦ name°ª¿¡ ¼³Á¤
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return fullName °ª ¹ÝÈ¯
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName ÆÄ¶ó¹ÌÅÍ¸¦ fullName°ª¿¡ ¼³Á¤
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return encName °ª ¹ÝÈ¯
	 */
	public String getEncName() {
		return encName;
	}

	/**
	 * @param encName ÆÄ¶ó¹ÌÅÍ¸¦ encName°ª¿¡ ¼³Á¤
	 */
	public void setEncName(String encName) {
		this.encName = encName;
	}

	/**
	 * @return share °ª ¹ÝÈ¯
	 */
	public boolean isShare() {
		return share;
	}

	/**
	 * @param share ÆÄ¶ó¹ÌÅÍ¸¦ share°ª¿¡ ¼³Á¤
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
	 * @return depth °ª ¹ÝÈ¯
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth ÆÄ¶ó¹ÌÅÍ¸¦ depth°ª¿¡ ¼³Á¤
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * @return parentFullName °ª ¹ÝÈ¯
	 */
	public String getParentFullName() {
		return parentFullName;
	}

	/**
	 * @param parentFullName ÆÄ¶ó¹ÌÅÍ¸¦ parentFullName°ª¿¡ ¼³Á¤
	 */
	public void setParentFullName(String parentFullName) {
		this.parentFullName = parentFullName;
	}

	/**
	 * @return parentId °ª ¹ÝÈ¯
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId ÆÄ¶ó¹ÌÅÍ¸¦ parentId°ª¿¡ ¼³Á¤
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return newCnt °ª ¹ÝÈ¯
	 */
	public long getNewCnt() {
		return newCnt;
	}

	/**
	 * @param newCnt ÆÄ¶ó¹ÌÅÍ¸¦ newCnt°ª¿¡ ¼³Á¤
	 */
	public void setNewCnt(long newCnt) {
		this.newCnt = newCnt;
	}

	/**
	 * @return unseenCnt °ª ¹ÝÈ¯
	 */
	public long getUnseenCnt() {
		return unseenCnt;
	}

	/**
	 * @param unseenCnt ÆÄ¶ó¹ÌÅÍ¸¦ unseenCnt°ª¿¡ ¼³Á¤
	 */
	public void setUnseenCnt(long unseenCnt) {
		this.unseenCnt = unseenCnt;
	}

	/**
	 * @return againg °ª ¹ÝÈ¯
	 */
	public long getAgaing() {
		return againg;
	}

	/**
	 * @param againg ÆÄ¶ó¹ÌÅÍ¸¦ againg°ª¿¡ ¼³Á¤
	 */
	public void setAgaing(long againg) {
		this.againg = againg;
	}

	/**
	 * @return totalCnt °ª ¹ÝÈ¯
	 */
	public long getTotalCnt() {
		return totalCnt;
	}

	/**
	 * @param totalCnt ÆÄ¶ó¹ÌÅÍ¸¦ totalCnt°ª¿¡ ¼³Á¤
	 */
	public void setTotalCnt(long totalCnt) {
		this.totalCnt = totalCnt;
	}

	/**
	 * <p>Æú´õÀÇ Á¤º¸¸¦  JSON OBJECT ·Î ±¸¼º ÇÏ¿© ¹ÝÈ¯</p>	
	 * 
	 * @return JSONObject			Æú´õ Á¤º¸ JSONObject
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
	 * <p>ÇöÀç Æú´õÀÇ »óÀ§ Æú´õÀÌ¸§À» ¼³Á¤</p>
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
	 * @return quota °ª ¹ÝÈ¯
	 */
	public MailQuotaBean getQuota() {
		return quota;
	}

	/**
	 * @param quota ÆÄ¶ó¹ÌÅÍ¸¦ quota°ª¿¡ ¼³Á¤
	 */
	public void setQuota(MailQuotaBean quota) {
		this.quota = quota;
	}
	
		
	
	
}
