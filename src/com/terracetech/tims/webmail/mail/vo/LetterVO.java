package com.terracetech.tims.webmail.mail.vo;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class LetterVO {

	private int letterSeq;
	
	private int mailDomainSeq;
	
	private String attachMode;
	
	private String thumbnailImgName;
	
	private String headImgName;
	
	private String bodyImgName;
	
	private String tailImgName;
	
	private byte[] thumbnailImg;
	
	private byte[] headImg;
	
	private byte[] bodyImg;
	
	private byte[] tailImg;
	
	private String thumbnailImgUrl;
	
	private String letterHeaderUrl;
	private String letterBodyUrl;
	private String letterTailUrl;
	
	private String letterHeaderPath;
	private String letterBodyPath;
	private String letterTailPath;
	
	public JSONObject toJson(){
		JSONObject json = new JSONObject();
		json.put("letterSeq", letterSeq);
		json.put("attachMode", attachMode);
		json.put("thumbnail", thumbnailImgUrl);
		json.put("header", letterHeaderUrl);
		json.put("body", letterBodyUrl);
		json.put("tail", letterTailUrl);
		
		return json;
	}
	

	public int getLetterSeq() {
		return letterSeq;
	}

	public void setLetterSeq(int letterSeq) {
		this.letterSeq = letterSeq;
	}

	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public String getAttachMode() {
		return attachMode;
	}

	public void setAttachMode(String attachMode) {
		this.attachMode = attachMode;
	}

	public String getThumbnailImgName() {
		return thumbnailImgName;
	}

	public void setThumbnailImgName(String thumbnailImgName) {
		this.thumbnailImgName = thumbnailImgName;
	}

	public String getHeadImgName() {
		return headImgName;
	}

	public void setHeadImgName(String headImgName) {
		this.headImgName = headImgName;
	}

	public String getBodyImgName() {
		return bodyImgName;
	}

	public void setBodyImgName(String bodyImgName) {
		this.bodyImgName = bodyImgName;
	}

	public String getTailImgName() {
		return tailImgName;
	}

	public void setTailImgName(String tailImgName) {
		this.tailImgName = tailImgName;
	}

	public byte[] getThumbnailImg() {
		return thumbnailImg;
	}

	public void setThumbnailImg(byte[] thumbnailImg) {
		this.thumbnailImg = thumbnailImg;
	}

	public byte[] getHeadImg() {
		return headImg;
	}

	public void setHeadImg(byte[] headImg) {
		this.headImg = headImg;
	}

	public byte[] getBodyImg() {
		return bodyImg;
	}

	public void setBodyImg(byte[] bodyImg) {
		this.bodyImg = bodyImg;
	}

	public byte[] getTailImg() {
		return tailImg;
	}

	public void setTailImg(byte[] tailImg) {
		this.tailImg = tailImg;
	}

	public String getThumbnailImgUrl() {
		return thumbnailImgUrl;
	}

	public void setThumbnailImgUrl(String thumbnailImgUrl) {
		this.thumbnailImgUrl = thumbnailImgUrl;
	}

	/**
	 * @return letterHeaderUrl °ª ¹ÝÈ¯
	 */
	public String getLetterHeaderUrl() {
		return letterHeaderUrl;
	}

	/**
	 * @param letterHeaderUrl ÆÄ¶ó¹ÌÅÍ¸¦ letterHeaderUrl°ª¿¡ ¼³Á¤
	 */
	public void setLetterHeaderUrl(String letterHeaderUrl) {
		this.letterHeaderUrl = letterHeaderUrl;
	}

	/**
	 * @return letterBodytUrl °ª ¹ÝÈ¯
	 */
	public String getLetterBodyUrl() {
		return letterBodyUrl;
	}

	/**
	 * @param letterBodytUrl ÆÄ¶ó¹ÌÅÍ¸¦ letterBodytUrl°ª¿¡ ¼³Á¤
	 */
	public void setLetterBodyUrl(String letterBodytUrl) {
		this.letterBodyUrl = letterBodytUrl;
	}

	/**
	 * @return letterTailUrl °ª ¹ÝÈ¯
	 */
	public String getLetterTailUrl() {
		return letterTailUrl;
	}

	/**
	 * @param letterTailUrl ÆÄ¶ó¹ÌÅÍ¸¦ letterTailUrl°ª¿¡ ¼³Á¤
	 */
	public void setLetterTailUrl(String letterTailUrl) {
		this.letterTailUrl = letterTailUrl;
	}


	/**
	 * @return letterHeaderPath °ª ¹ÝÈ¯
	 */
	public String getLetterHeaderPath() {
		return letterHeaderPath;
	}


	/**
	 * @param letterHeaderPath ÆÄ¶ó¹ÌÅÍ¸¦ letterHeaderPath°ª¿¡ ¼³Á¤
	 */
	public void setLetterHeaderPath(String letterHeaderPath) {
		this.letterHeaderPath = letterHeaderPath;
	}


	/**
	 * @return letterBodyPath °ª ¹ÝÈ¯
	 */
	public String getLetterBodyPath() {
		return letterBodyPath;
	}


	/**
	 * @param letterBodyPath ÆÄ¶ó¹ÌÅÍ¸¦ letterBodyPath°ª¿¡ ¼³Á¤
	 */
	public void setLetterBodyPath(String letterBodyPath) {
		this.letterBodyPath = letterBodyPath;
	}


	/**
	 * @return letterTailPath °ª ¹ÝÈ¯
	 */
	public String getLetterTailPath() {
		return letterTailPath;
	}


	/**
	 * @param letterTailPath ÆÄ¶ó¹ÌÅÍ¸¦ letterTailPath°ª¿¡ ¼³Á¤
	 */
	public void setLetterTailPath(String letterTailPath) {
		this.letterTailPath = letterTailPath;
	}
	
	
}
