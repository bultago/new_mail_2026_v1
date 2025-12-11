package com.terracetech.tims.mail;

import java.lang.Long;
import java.util.Date;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeUtility;

public class TMailConfirm {

	private String message_id 	= null;
	private String sender_email = null;
	private String approval_email = null;
	private String mail_status	= null;
	private String status_time	= null;
    private Date date = null;

    private String readCnt = null;
    private String unixTime = null;
    private String code = null;

	public TMailConfirm() {
	}

	public TMailConfirm(String message_id, String sender_email, String approval_email, String mail_status, String status_time) {
        this.message_id 	= message_id;
        this.sender_email 	= sender_email;
		this.approval_email = approval_email;
        this.mail_status 	= mail_status;
        this.status_time 	= status_time;
    }
    
	public void setMessageID(String message_id) {
		this.message_id = message_id;
	}

    public String getMessageID() {
        return message_id;
    }

	public void setSenderEmail(String sender_email) {
		this.sender_email = sender_email;
	}

    public String getSenderEmail() {
        return sender_email;
    }

	public void setApprovalEmail(String approval_email) {
		this.approval_email = approval_email;
	}

    public String getApprovalEmail() {
		return approval_email;
    }

	public String getApprovalAddress() {

		if (approval_email != null && approval_email.indexOf("=?") >= 0) {
			try {
				String str = MimeUtility.decodeText(approval_email);
				InternetAddress[] ias = InternetAddress.parse(str);

				if (ias != null && ias.length > 0) {
					return ias[0].getAddress();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("-- Exception getApprovalEmail : ["+ex.getMessage()+"]");
			}
		}

		return approval_email;
	}


	public String getApprovalEmailString() {
		try {
			String str = MimeUtility.decodeText(approval_email);
			InternetAddress[] ias = InternetAddress.parse(str);

			return TMailAddress.getAddressString(ias);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("-- Exception getApprovalEmailString : ["+ex.getMessage()+"]");
		}

		return "";
	}

	public void setMailStatus(String mail_status) {
		this.mail_status = mail_status;
	}

	public String getMailStatus() {
        return mail_status;
    }

	public void setStatusTime(String status_time) {
		this.status_time = status_time;
	}

    public String getStatusTime() {
        return status_time;
    }

	public Date getStatusDate() {
		try {
			Date date = new Date(Long.parseLong(status_time));	

			return date;
		} catch (Exception ex) {
			System.out.println("-- ERR getStatusDate ["+status_time+"] : ["+ex.getMessage()+"]");
		}
		return null;
    }
}
