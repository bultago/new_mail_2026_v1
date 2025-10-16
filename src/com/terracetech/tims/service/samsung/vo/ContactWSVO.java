package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;

public class ContactWSVO implements Serializable{

	private static final long serialVersionUID = 20090821L;
	
	public String compid = null;	//회사코드
	
	public String deptid; //부서코드	
	public String bcuid; //연락처ID	
	public String userid; //사용자 ID	사용자 고유 UID (로그인ID아님)
	public String regno; //사용자 주민번호	
	public String lstfg; //명함종류	P : 개인, D : 부서, J : 지인
	public String openfg; //공개여부	0 : 전체공개, 1 : 비공개, 2 : 지정인 공개
	public String name; //이름	
	public String fname; //영문이름	
	public String lname; //영문성	
	public String searchField; //검색필드	전호번호 검색 : phone, 이름 검색 : name
	public String searchKey; //검색어	검색할 전화번호 또는 이름
	public String ctel1, ctel2; //회사전화 1, 2	
	public String htel1, htel2; //집전화 1, 2	
	public String mobile; //핸드폰	
	public String email1, email2; //이메일 1, 2	
	public String cfax, hfax; //회사팩스, 개인팩스	
	public String company; //회사명	
	public String dept; //부서명	
	public String title; //직위	
	public String jobdef; //담당업무	
	public String cpost, caddr; //회사 우편번호, 주소	
	public String hpost, haddr; //자택 우편번호, 주소	
	public String important; //주요인사 여부	Y / N
	public String inid, indt, inname; //등록자 ID, 등록일(yyyyMMddHHmmss), 등록자명
	public String upid, updt, upname; //수정자 ID, 수정일(yyyyMMddHHmmss), 수정자명
	public String fid; //FID	
	public String hitno; //조회수	
	public String status; //		

	public String getCompid() {
		return compid;
	}

	public void setCompid(String compid) {
		this.compid = compid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getBcuid() {
		return bcuid;
	}

	public void setBcuid(String bcuid) {
		this.bcuid = bcuid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRegno() {
		return regno;
	}

	public void setRegno(String regno) {
		this.regno = regno;
	}

	public String getLstfg() {
		return lstfg;
	}

	public void setLstfg(String lstfg) {
		this.lstfg = lstfg;
	}

	public String getOpenfg() {
		return openfg;
	}

	public void setOpenfg(String openfg) {
		this.openfg = openfg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getCtel1() {
		return ctel1;
	}

	public void setCtel1(String ctel1) {
		this.ctel1 = ctel1;
	}

	public String getCtel2() {
		return ctel2;
	}

	public void setCtel2(String ctel2) {
		this.ctel2 = ctel2;
	}

	public String getHtel1() {
		return htel1;
	}

	public void setHtel1(String htel1) {
		this.htel1 = htel1;
	}

	public String getHtel2() {
		return htel2;
	}

	public void setHtel2(String htel2) {
		this.htel2 = htel2;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getCfax() {
		return cfax;
	}

	public void setCfax(String cfax) {
		this.cfax = cfax;
	}

	public String getHfax() {
		return hfax;
	}

	public void setHfax(String hfax) {
		this.hfax = hfax;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getJobdef() {
		return jobdef;
	}

	public void setJobdef(String jobdef) {
		this.jobdef = jobdef;
	}

	public String getCpost() {
		return cpost;
	}

	public void setCpost(String cpost) {
		this.cpost = cpost;
	}

	public String getCaddr() {
		return caddr;
	}

	public void setCaddr(String caddr) {
		this.caddr = caddr;
	}

	public String getHpost() {
		return hpost;
	}

	public void setHpost(String hpost) {
		this.hpost = hpost;
	}

	public String getHaddr() {
		return haddr;
	}

	public void setHaddr(String haddr) {
		this.haddr = haddr;
	}

	public String getImportant() {
		return important;
	}

	public void setImportant(String important) {
		this.important = important;
	}

	public String getInid() {
		return inid;
	}

	public void setInid(String inid) {
		this.inid = inid;
	}

	public String getIndt() {
		return indt;
	}

	public void setIndt(String indt) {
		this.indt = indt;
	}

	public String getInname() {
		return inname;
	}

	public void setInname(String inname) {
		this.inname = inname;
	}

	public String getUpid() {
		return upid;
	}

	public void setUpid(String upid) {
		this.upid = upid;
	}

	public String getUpdt() {
		return updt;
	}

	public void setUpdt(String updt) {
		this.updt = updt;
	}

	public String getUpname() {
		return upname;
	}

	public void setUpname(String upname) {
		this.upname = upname;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getHitno() {
		return hitno;
	}

	public void setHitno(String hitno) {
		this.hitno = hitno;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
