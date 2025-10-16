package com.terracetech.tims.service.mcnc.vo;

import java.io.Serializable;

/**
 * 첨부 파일 정보를 나타내는 클래스
 * @author kevin
 *
 */
public class AttachmentInfoWDO implements Serializable{
	private static final long serialVersionUID = -1181795292629940986L;
	
	private String	id;
	private String	name;
	private long	size;
	private int		sequence;	
	private String	format;	
	private PayloadWDO[]	payload;
	
	/**
	 * 첨부 파일 ID를 얻는다.
	 * @return 첨부 파일 ID
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * 첨부 파일 ID를 세팅한다.
	 * @param id 첨부 파일 ID
	 */
	public void setID(String id) {
		this.id = id;
	}
	
	/**
	 * 첨부 파일 이름을 얻는다.
	 * @return 첨부 파일 이름
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 첨부 파일 이름을 세팅한다.
	 * @param name 첨부 파일 이름
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 첨부 파일 크기를 얻는다. 
	 * @return 첨부 파일 크기
	 */
	public long getSize() {
		return size;
	}
	
	/**
	 * 첨부 파일 크기를 세팅한다.
	 * @param size 첨부 파일 크기
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * 첨부 파일 포맷을 얻는다.
	 * @return 첨부 파일 포맷
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 첨부 파일 포맷을 세팅한다.
	 * @param format 첨푸 파일 포맷
	 */
	public void setFormat(String format) {
		this.format = format;
	}


	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public PayloadWDO[] getPayload() {
		return payload;
	}

	public void setPayload(PayloadWDO[] payload) {
		this.payload = payload;
	}
}