/**
 * MailUserAddressVO.java 2008  2008-09-26
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.vo;

/**
 * <p><strong>MailUserAddressVO.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li>»ç¿ëÀÚ ÁÖ¼Ò Á¤º¸¸¦ °¡Á®¿À´Â VO. ÁÖ¼ÒÁ¤º¸¿¡ °üÇÑ °øÅëÀûÀÎ  VO°´Ã¼</li>
 * <li>ÁÖ¼ÒÁ¤º¸¿¡°üÇÑ Á¤º¸ Æ÷ÇÔ. outlook ±âÁØÀÇ Á¤º¸ ÇÊµå Æ÷ÇÔ.</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailUserAddressVO {

	/**
	 * <p>ÇÚµåÆù ¹øÈ£ Á¤º¸</p>
	 */
	private String mobileNo = null;
	/**
	 * <p>Áý ÀüÈ­¹øÈ£ Á¤º¸</p>
	 */
	private String homeTel = null;
	/**
	 * <p>Áý ÆÑ½º¹øÈ£ Á¤º¸</p>
	 */
	private String homeFax = null;
	/**
	 * <p>Áý ¿ìÆí¹øÈ£</p>
	 */
	private String homePostalCode = null;
	/**
	 * <p>»ç¿ëÀÚ ±¹°¡ Á¤º¸</p>
	 */
	private String homeCountry = null;
	/**
	 * <p>»ç¿ëÀÚÀÇ °ÅÁÖ ÁÖ Á¤º¸</p>
	 */
	private String homeState = null;
	/**
	 * <p>»ç¿ëÀÚ µµ½Ã Á¤º¸</p>
	 */
	private String homeCity = null;
	/**
	 * <p>»ç¿ëÀÚÀÇ ÁÖ¼Ò Street Á¤º¸</p>
	 */
	private String homeStreet = null;
	/**
	 * <p>±âº» ÁÖ¼Ò Á¤º¸</p>
	 */
	private String homeBasicAddress = null;
	/**
	 * <p>È®Àå ÁÖ¼Ò Á¤º¸</p>
	 */
	private String homepage = null;
	/**
	 * <p>È¸»ç¸í Á¤º¸</p>
	 */
	private String companyName = null;
	/**
	 * <p>ºÎ¼­ Á¤º¸</p>
	 */
	private String departmentName = null;
	/**
	 * <p>»ç¹«½Ç ÀüÈ­ ¹øÈ£ Á¤º¸</p>
	 */
	private String officeTel = null;
	/**
	 * <p>»ç¹«½Ç ÆÑ½º¹øÈ£ Á¤º¸</p>
	 */
	private String officeFax = null;
	/**
	 * <p>»ç¹«½Ç ¿ìÆí ¹øÈ£ Á¤º¸</p>
	 */
	private String officePostalCode = null;
	/**
	 * <p>»ç¹«½Ç ÀÌ À§Ä¡ÇÑ ±¹°¡ Á¤º¸</p>
	 */
	private String officeCountry = null;
	/**
	 * <p>»ç¹«½Ç Áö¿ª ÁÖ Á¤º¸</p>
	 */
	private String officeState = null;
	/**
	 * <p>»ç¹«½Ç À§Ä¡ µµ½Ã Á¤º¸</p>
	 */
	private String officeCity = null;
	/**
	 * <p>»ç¹«½Ç Street Á¤º¸</p>
	 */
	private String officeStreet = null;
	/**
	 * <p>»ç¹«½Ç ±âº» ÁÖ¼Ò Á¤º¸</p>
	 */
	private String officeBasicAddress = null;
	/**
	 * <p>»ç¹«½Ç È®Àå ÁÖ¼Ò Á¤º¸</p>
	 */
	private String officeExtAddress = null;
	/**
	 * <p>±âÅ¸ ¼³¸í Á¤º¸</p>
	 */
	private String description = null;
	
	/**
	 * @return mobileNo °ª ¹ÝÈ¯
	 */
	public String getMobileNo() {
		return mobileNo;
	}
	/**
	 * @param mobileNo ÆÄ¶ó¹ÌÅÍ¸¦ mobileNo°ª¿¡ ¼³Á¤
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	/**
	 * @return homeTel °ª ¹ÝÈ¯
	 */
	public String getHomeTel() {
		return homeTel;
	}
	/**
	 * @param homeTel ÆÄ¶ó¹ÌÅÍ¸¦ homeTel°ª¿¡ ¼³Á¤
	 */
	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}
	/**
	 * @return homeFax °ª ¹ÝÈ¯
	 */
	public String getHomeFax() {
		return homeFax;
	}
	/**
	 * @param homeFax ÆÄ¶ó¹ÌÅÍ¸¦ homeFax°ª¿¡ ¼³Á¤
	 */
	public void setHomeFax(String homeFax) {
		this.homeFax = homeFax;
	}
	/**
	 * @return homePostalCode °ª ¹ÝÈ¯
	 */
	public String getHomePostalCode() {
		return homePostalCode;
	}
	/**
	 * @param homePostalCode ÆÄ¶ó¹ÌÅÍ¸¦ homePostalCode°ª¿¡ ¼³Á¤
	 */
	public void setHomePostalCode(String homePostalCode) {
		this.homePostalCode = homePostalCode;
	}
	/**
	 * @return homeCountry °ª ¹ÝÈ¯
	 */
	public String getHomeCountry() {
		return homeCountry;
	}
	/**
	 * @param homeCountry ÆÄ¶ó¹ÌÅÍ¸¦ homeCountry°ª¿¡ ¼³Á¤
	 */
	public void setHomeCountry(String homeCountry) {
		this.homeCountry = homeCountry;
	}
	/**
	 * @return homeState °ª ¹ÝÈ¯
	 */
	public String getHomeState() {
		return homeState;
	}
	/**
	 * @param homeState ÆÄ¶ó¹ÌÅÍ¸¦ homeState°ª¿¡ ¼³Á¤
	 */
	public void setHomeState(String homeState) {
		this.homeState = homeState;
	}
	/**
	 * @return homeCity °ª ¹ÝÈ¯
	 */
	public String getHomeCity() {
		return homeCity;
	}
	/**
	 * @param homeCity ÆÄ¶ó¹ÌÅÍ¸¦ homeCity°ª¿¡ ¼³Á¤
	 */
	public void setHomeCity(String homeCity) {
		this.homeCity = homeCity;
	}
	/**
	 * @return homeStreet °ª ¹ÝÈ¯
	 */
	public String getHomeStreet() {
		return homeStreet;
	}
	/**
	 * @param homeStreet ÆÄ¶ó¹ÌÅÍ¸¦ homeStreet°ª¿¡ ¼³Á¤
	 */
	public void setHomeStreet(String homeStreet) {
		this.homeStreet = homeStreet;
	}
	/**
	 * @return homeBasicAddress °ª ¹ÝÈ¯
	 */
	public String getHomeBasicAddress() {
		return homeBasicAddress;
	}
	/**
	 * @param homeBasicAddress ÆÄ¶ó¹ÌÅÍ¸¦ homeBasicAddress°ª¿¡ ¼³Á¤
	 */
	public void setHomeBasicAddress(String homeBasicAddress) {
		this.homeBasicAddress = homeBasicAddress;
	}
	/**
	 * @return homepage °ª ¹ÝÈ¯
	 */
	public String getHomepage() {
		return homepage;
	}
	/**
	 * @param homepage ÆÄ¶ó¹ÌÅÍ¸¦ homepage°ª¿¡ ¼³Á¤
	 */
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	/**
	 * @return companyName °ª ¹ÝÈ¯
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName ÆÄ¶ó¹ÌÅÍ¸¦ companyName°ª¿¡ ¼³Á¤
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return departmentName °ª ¹ÝÈ¯
	 */
	public String getDepartmentName() {
		return departmentName;
	}
	/**
	 * @param departmentName ÆÄ¶ó¹ÌÅÍ¸¦ departmentName°ª¿¡ ¼³Á¤
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	/**
	 * @return officeTel °ª ¹ÝÈ¯
	 */
	public String getOfficeTel() {
		return officeTel;
	}
	/**
	 * @param officeTel ÆÄ¶ó¹ÌÅÍ¸¦ officeTel°ª¿¡ ¼³Á¤
	 */
	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}
	/**
	 * @return officeFax °ª ¹ÝÈ¯
	 */
	public String getOfficeFax() {
		return officeFax;
	}
	/**
	 * @param officeFax ÆÄ¶ó¹ÌÅÍ¸¦ officeFax°ª¿¡ ¼³Á¤
	 */
	public void setOfficeFax(String officeFax) {
		this.officeFax = officeFax;
	}
	/**
	 * @return officePostalCode °ª ¹ÝÈ¯
	 */
	public String getOfficePostalCode() {
		return officePostalCode;
	}
	/**
	 * @param officePostalCode ÆÄ¶ó¹ÌÅÍ¸¦ officePostalCode°ª¿¡ ¼³Á¤
	 */
	public void setOfficePostalCode(String officePostalCode) {
		this.officePostalCode = officePostalCode;
	}
	/**
	 * @return officeCountry °ª ¹ÝÈ¯
	 */
	public String getOfficeCountry() {
		return officeCountry;
	}
	/**
	 * @param officeCountry ÆÄ¶ó¹ÌÅÍ¸¦ officeCountry°ª¿¡ ¼³Á¤
	 */
	public void setOfficeCountry(String officeCountry) {
		this.officeCountry = officeCountry;
	}
	/**
	 * @return officeState °ª ¹ÝÈ¯
	 */
	public String getOfficeState() {
		return officeState;
	}
	/**
	 * @param officeState ÆÄ¶ó¹ÌÅÍ¸¦ officeState°ª¿¡ ¼³Á¤
	 */
	public void setOfficeState(String officeState) {
		this.officeState = officeState;
	}
	/**
	 * @return officeCity °ª ¹ÝÈ¯
	 */
	public String getOfficeCity() {
		return officeCity;
	}
	/**
	 * @param officeCity ÆÄ¶ó¹ÌÅÍ¸¦ officeCity°ª¿¡ ¼³Á¤
	 */
	public void setOfficeCity(String officeCity) {
		this.officeCity = officeCity;
	}
	/**
	 * @return officeStreet °ª ¹ÝÈ¯
	 */
	public String getOfficeStreet() {
		return officeStreet;
	}
	/**
	 * @param officeStreet ÆÄ¶ó¹ÌÅÍ¸¦ officeStreet°ª¿¡ ¼³Á¤
	 */
	public void setOfficeStreet(String officeStreet) {
		this.officeStreet = officeStreet;
	}
	/**
	 * @return officeBasicAddress °ª ¹ÝÈ¯
	 */
	public String getOfficeBasicAddress() {
		return officeBasicAddress;
	}
	/**
	 * @param officeBasicAddress ÆÄ¶ó¹ÌÅÍ¸¦ officeBasicAddress°ª¿¡ ¼³Á¤
	 */
	public void setOfficeBasicAddress(String officeBasicAddress) {
		this.officeBasicAddress = officeBasicAddress;
	}
	/**
	 * @return officeExtAddress °ª ¹ÝÈ¯
	 */
	public String getOfficeExtAddress() {
		return officeExtAddress;
	}
	/**
	 * @param officeExtAddress ÆÄ¶ó¹ÌÅÍ¸¦ officeExtAddress°ª¿¡ ¼³Á¤
	 */
	public void setOfficeExtAddress(String officeExtAddress) {
		this.officeExtAddress = officeExtAddress;
	}
	/**
	 * @return description °ª ¹ÝÈ¯
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description ÆÄ¶ó¹ÌÅÍ¸¦ description°ª¿¡ ¼³Á¤
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
}
