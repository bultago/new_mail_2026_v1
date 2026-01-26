package com.terracetech.tims.webmail.home.vo;

public class MailHomePortletVO {
   
	private int userSeq;
	
	private int domainSeq;
	
    private int portletSeq;
    
    private int layoutSeq;
    
    private String defaultPortlet;

    private String portletName;

    private String portletUrl;
    
    private String location;

    private String description;
    

    public int getDomainSeq() {
		return domainSeq;
	}

	public void setDomainSeq(int domainSeq) {
		this.domainSeq = domainSeq;
	}

	public void setLayoutSeq(int layoutSeq) {
		this.layoutSeq = layoutSeq;
	}

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}

	public Integer getPortletSeq() {
        return portletSeq;
    }

    public void setPortletSeq(int portletSeq) {
        this.portletSeq = portletSeq;
    }

    public String getPortletName() {
        return portletName;
    }

    public void setPortletName(String portletName) {
        this.portletName = portletName;
    }

    public String getPortletUrl() {
        return portletUrl;
    }

    public void setPortletUrl(String portletUrl) {
        this.portletUrl = portletUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getLocation() {
		return location.trim();
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getLayoutSeq() {
		return layoutSeq;
	}

	public void setLayoutSeq(Integer layoutSeq) {
		this.layoutSeq = layoutSeq;
	}

	public String getDefaultPortlet() {
		return defaultPortlet;
	}

	public void setDefaultPortlet(String defaultPortlet) {
		this.defaultPortlet = defaultPortlet;
	}

}