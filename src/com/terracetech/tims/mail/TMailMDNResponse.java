package com.terracetech.tims.mail;

import java.util.*;
import java.text.*;
import jakarta.mail.internet.MimeUtility;

public class TMailMDNResponse {

    private String personal = null;
    private String address = null;
    private Date date = null;

    private String readCnt = null;
    private String unixTime = null;
    private String code = null;

	public TMailMDNResponse() {
	}

    public TMailMDNResponse(String address, Date date) {
        this.address = address;
        this.date = date;
    }

    public TMailMDNResponse(String personal, String address, Date date) {
        this.personal = personal;
        this.address = address;
        this.date = date;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getPersonal() {
		if (personal == null) {
			return "";
		}

		try {
			return MimeUtility.decodeText(personal);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return personal;
    }
    
	public void setAddress(String address) {
		this.address = address;
	}

    public String getAddress() {
        return address;
    }
    
    public Date getReadDate() {
		return date;
	}

    public long getReadTimeInSec() {
        if (date == null) {
            return 0;
        }

        return (date.getTime()/1000);
	}

    public long getSentDate() {
        if (date == null) {
            return 0;
        }

        return (date.getTime()/1000);
    }

    public String getSentDate2() {
        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy/MM/dd (E) HH:mm:ss");

        return sdf.format(date);
    }

    public String getSentDate3() {
        Date date1 = new Date();

        if (date == null) {
            return null;
        }

        long ms1 = date1.getTime();
        long ms2 = date.getTime();
        ms1 -= ms1 % (24 * 60 * 60 * 1000);

        SimpleDateFormat sdf =
            new SimpleDateFormat((ms1 < ms2)? "HH:mm:ss" : "yyyy/MM/dd");

        return sdf.format(date);
    }

    public String getSentDate4() {
        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (HH:mm)");

        return sdf.format(date);
    }

    public void setReadCnt(String readCnt) {
		this.readCnt = readCnt;
	}
    public String getReadCnt() {
		return readCnt;
	}

    public void setUnixTime(String unixTime) {
		this.unixTime = unixTime;

		if (unixTime != null) {
			long uTime = Long.parseLong(unixTime);
			this.date = new Date(uTime * 1000);
		}
	}

    public String getUnixTime() {
		return unixTime;
	}

    public void setCode(String code) {
		this.code = code;
	}
    public String getCode() {
		return code;
	}
}
