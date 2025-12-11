package com.terracetech.tims.mail;

import java.io.*;
import java.text.*;
import java.util.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class TMailAddress {

    private InternetAddress ias	= null;
	private String[] strAddrs	= null;
	private String myiaddress	= null;
	private String mypersonal	= null;
	private String myaddress	= null;
	private String mycharset	= null;

    public TMailAddress(InternetAddress ias) {
        this.ias = ias;
    }

    public TMailAddress(String myaddress) {
        this.myiaddress	= myaddress;
        this.mypersonal	= getPersonal(myaddress);
        this.myaddress	= getAddress(myaddress);
    }

    public TMailAddress(String myaddress, String mycharset) {
        this.myiaddress	= myaddress;
        this.mypersonal	= getPersonal(myaddress);
        this.myaddress	= getAddress(myaddress);
        this.mycharset	= mycharset;
    }

	public static boolean isPersonalString(String personal) {
		if(personal.charAt(0) == '\"' 
			&& personal.indexOf('\"', 1) < 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmailID(String id) {
		// return id.matches("^[0-9a-zA-Z_]([0-9a-zA-Z_\.!#\$%&*+-/=\?]*)$");
		return id.matches("^[0-9a-zA-Z_]([0-9a-zA-Z_]*)$");
	}

	public static boolean isEmailAddress(String email) {
		if(email == null || email.length() <= 0) {
			return false;
		}

		try {
			InternetAddress[] ias = InternetAddress.parse(email, false);

			if (ias.length == 1) {
				return true;
			}
		} catch(AddressException e) {
		}

		return false;
	}

	public static String getEmailAddress(String str) {
		if (str == null || str.length() <= 0 || str.indexOf('<') < 0) {
			return str;
		}

		int s = str.indexOf('<') + 1;
		int e = (str.length() - 1);

		if (s > e) {
			return str.substring(s);
		}

		return str.substring(s, e);
	}

	public static String[] parseEmail2Array(String addr) {
		if (TMailUtility.isNull(addr)) {
			return null;
		}

		String[] addrs = addr.split("[;,\r\n]");
		String strTmp = "";

		Vector v = new Vector();
		
		for(int i = 0; i < addrs.length; i++) {
			String addr_trim = addrs[i].trim();

			if(addr_trim.length() == 0) {
				continue;
			}
			else if(isPersonalString(addr_trim)) {
				strTmp += addr_trim + ",";
				continue;
			}
			else {
				strTmp += addr_trim;
				v.addElement(strTmp);
			}
			strTmp = "";
		}

		String[] addresses = new String[v.size()];
		v.copyInto(addresses);

		return addresses;
	}

	public static InternetAddress[] getParseEmailAddress(String addr){
		if (TMailUtility.isNull(addr)) {
			return null;
		}

		try {
			InternetAddress[] ias = InternetAddress.parse(addr, false);
			InternetAddress[] newias = new InternetAddress[ias.length];

			for(int i = 0; i < ias.length; i++) {
				newias[i] = new InternetAddress(ias[i].toString());				
			}

			return newias;
		} catch(AddressException e) {

		}
		return null;
	}

	public static InternetAddress[] getParseEmailAddress(
		String addr, String charset) throws AddressException {

		if (TMailUtility.isNull(addr)) {
			return null;
		}

		InternetAddress[] ias = InternetAddress.parse(addr, false);
		InternetAddress[] newias = new InternetAddress[ias.length];

		for(int i = 0; i < ias.length; i++) {
			newias[i] = new InternetAddress();

			if(ias[i].getPersonal() != null) {
				try {
					newias[i].setPersonal(ias[i].getPersonal(), charset);
				} catch(UnsupportedEncodingException e) {
				}
			}
			if(ias[i].getAddress() != null) {
				newias[i].setAddress(ias[i].getAddress());
			}
		}

		return newias;
	}

	public static javax.mail.Address[][] getParseRcptToAddressArray(String addr) {
		if (TMailUtility.isNull(addr)) {
			return null;
		}

		try {
			InternetAddress[] ias = InternetAddress.parse(addr, false);
			javax.mail.Address[][] madr = 
				new javax.mail.Address[ias.length][];

			for(int i = 0; i < ias.length; i++) {
				InternetAddress[] newias = new InternetAddress[1];
				newias[0] = new InternetAddress();

				if(ias[i].getAddress() != null) {
					newias[0].setAddress(ias[i].getAddress());
					madr[i] = newias;
				}
			}

			return madr;
		} catch(AddressException e) { }
		return null;
	}

	public static String getReservedRcptToAddress(String addr, String charset) {
		if (TMailUtility.isNull(addr)) {
			return null;
		}

		StringBuffer new_rcpts = new StringBuffer();

		try {
			InternetAddress[] ias = InternetAddress.parse(addr, false);
			String addrStr = null;
			String personal = null;
			String address = null;
			for(int i = 0; i < ias.length; i++) {
				if(ias[i].getAddress() != null) {
					personal = ias[i].getPersonal();
					address = ias[i].getAddress();
					addrStr = getEncodeAddressString(address, personal, charset);
					addrStr = (addrStr.indexOf("<") > -1)?addrStr:"<"+addrStr+">";
					new_rcpts.append(addrStr);
					if(i+1 != ias.length) {
						new_rcpts.append(",\r\n\t");
					}
				}
			}
			addrStr = null;
			return new_rcpts.toString();
		} catch(AddressException e) {

		}
		return null;
	}

	public static String getAddressString(InternetAddress ia) {
		if(ia != null) {
			String personal = ia.getPersonal();
			String address = ia.getAddress();
			
			if(personal != null){
				personal = personal.replaceAll("\'", "");
			}
			
			if (personal != null 
				&& address != null && address.equals("@")) {
				return personal;
			}
			else if (personal != null && !personal.equals(address)) {
				return "\""+personal+"\"<"+address+">";
			}
			else if (address != null) {
				/*
				* modified by doyoung
				* lgtel.co.kr
				* 2007.02.10
				*/
				if (address.startsWith("=?") && address.endsWith("@")) {
					address = address.substring(0, address.length() - 1);
					try {
						address = MimeUtility.decodeText(address);
					} 
					catch (UnsupportedEncodingException e) { }
    					catch (IndexOutOfBoundsException e) {return "";}
				}

				return address;
			}
		}
		return "";
	}

	public static String getAddressString(InternetAddress[] ias) {
		if (!TMailUtility.isNull(ias)) {
			String addr = "";
			for(int i = 0; i < ias.length; i++) {

				addr += getAddressString(ias[i]);

				if (i+1 < ias.length) {
					addr += ", ";
				}
			}
			return addr;
		}
		return null;
	}

	public static String getAddressString(InternetAddress[] ias, String email) {
		if (!TMailUtility.isNull(ias)) {
            String addr = "";
            for(int i = 0; i < ias.length; i++) {
                String address = ias[i].getAddress();

				if(email.equalsIgnoreCase(address)) {
					continue;
				}

				addr += getAddressString(ias[i]);

                if(i+1 < ias.length) {
                    addr += ", ";
                }
            }
            return addr;
        }
        return null;
    }

	/*
	 * added by doyoung
	 * 2005.12.21
	 */
	public static InternetAddress[] removeInvalidAddress(InternetAddress[] ias) {
		if (ias == null) {
			return null;
		}

		Vector v = new Vector();

		for (int i = 0; i < ias.length; i++) {
			String personal = ias[i].getPersonal();
			String address = ias[i].getAddress();
			
			if(personal != null){
				personal = personal.replaceAll("'", "");
				try {
					ias[i].setPersonal(personal);
				} catch (Exception e) {}
				
			}
			
			if(address.indexOf("<")!=-1){				
				String tmpA[] = address.split("<");
				address = tmpA[1];			
				
				if(personal ==null && tmpA[0].indexOf("\"")!=-1 ){					
					tmpA[0] = tmpA[0].replaceAll("\"", "");					
					tmpA[0] = tmpA[0].replaceAll("\\\\", "");
					tmpA[0] = tmpA[0].replaceAll("'", "");
					tmpA[0] = tmpA[0].trim();
					try { 	
						ias[i].setPersonal(tmpA[0]);
					}catch (UnsupportedEncodingException e) {}
				}
					
				if(tmpA[1].indexOf(">")!=-1){
				    address = tmpA[1].split(">")[0];
				}
				ias[i].setAddress(address);
				
		   }
			if (address.indexOf("null@null") < 0) {
				v.addElement(ias[i]);
			}
		}

		if (v.size() == 0) {
			return null;
		}

		InternetAddress[] ias2 = new InternetAddress[v.size()];

		v.copyInto(ias2);

		return ias2;
	}

	/*
	 * added by doyoung
	 * 2006.01.23
	 */
    public static String getEncodeAddressString(
		String address, String personal, String charset) 
		throws AddressException {
		if (address == null) {
			return null;
		}
	
		InternetAddress ia = new InternetAddress(address);

		if (personal != null && address != null) {
			try {
				ia.setPersonal(personal, charset);
			} catch (UnsupportedEncodingException e) { }
		}

		return ia.toString();
	}

	/*
	 * added by doyoung
	 * 2006.03.03
	 */
    public static TMailAddress[] parse(String address) {
		return parse(address, null);
	}

    public static TMailAddress[] parse(String address, String charset) {
		String[] addresses = parseEmail2Array(address);
		TMailAddress[] ma = new TMailAddress[addresses.length];

		for (int i = 0; i < addresses.length; i++) {
			ma[i] = new TMailAddress(addresses[i], charset);
		}

		return ma;
	}

	/*
	 * added by doyoung
	 * 2006.03.03
	 */
    public String getCharset() {
		return mycharset;
	}

    public String getPersonal() {
		return mypersonal;
	}

    public static String getPersonal(String address) {
		if (address == null) {
			return null;
		}

		int s = address.indexOf("\"")+1;	
		int e = address.lastIndexOf("\"");	

		if (e <= s) {
			return null;
		}

		return address.substring(s, e);			
	}

	/*
	 * added by doyoung
	 * 2006.03.03
	 */
    public String getAddress() {
		return myaddress;
	}

    public static String getAddress(String address) {
		if (address == null) {
			return null;
		}

		int s = address.lastIndexOf("\"");	
		int e = address.lastIndexOf(">");	
		s = address.indexOf("<", s)+1;	

		if (e <= s) {
			return address;
		}

		return address.substring(s, e);			
	}

	/*
	 * added by doyoung
	 * 2006.03.03
	 */
    public static InternetAddress[] getInternetAddress(String address) 
		throws AddressException {

    	return getInternetAddress(address, null);
	}

    public static InternetAddress[] getInternetAddress(
		String address, String charset) 
		throws AddressException {
		
		if (address == null) {
			return null;
		}

		TMailAddress[] mas = parse(address, charset);

    	return getInternetAddress(mas, charset);
	}

    public static InternetAddress[] getInternetAddress(TMailAddress[] mas) 
		throws AddressException {

    	return getInternetAddress(mas, null);
	}

    public static InternetAddress[] getInternetAddress(
		TMailAddress[] mas, String charset) 
		throws AddressException {

		if (mas == null || mas.length == 0) {
			return null;
		}

		InternetAddress[] ias = new InternetAddress[mas.length];
		
		for (int i = 0; i < mas.length; i++) {
			if (charset != null) {
				ias[i] = getInternetAddress(mas[i], charset);
			} else {
				ias[i] = getInternetAddress(mas[i]);
			}
		}

		return ias;
	}

	/*
	 * added by doyoung
	 * 2006.03.03
	 */
    public static InternetAddress getInternetAddress(TMailAddress ma) 
		throws AddressException {

		return getInternetAddress(ma, ma.getCharset());
	}

    public static InternetAddress getInternetAddress(
		TMailAddress ma, String charset) 
		throws AddressException {

		if (ma == null) {
			return null;
		}

		String address	= ma.getAddress();
		String personal	= ma.getPersonal();
		InternetAddress ia = null;
	
		if (personal != null) {
			try {
				if (charset != null) {
					ia = new InternetAddress(address, personal, charset);
				} else {
					ia = new InternetAddress(address, personal);
				}
			} catch (UnsupportedEncodingException e) { }
		}
		else {
			ia = new InternetAddress(address);
		}

		return ia;
	}

    public InternetAddress getInternetAddress() 
		throws AddressException {

		if (myiaddress == null) {
			return null;
		}
	
		InternetAddress ia = null;

		if (mypersonal != null) {
			try {
				if (mycharset != null) {
					ia = new InternetAddress(myaddress, mypersonal, mycharset);
				} else {
					ia = new InternetAddress(myaddress, mypersonal);
				}
			} catch (UnsupportedEncodingException e) { }
		}
		else {
			ia = new InternetAddress(myaddress);
		}


		return ia;
	}
	
	/*
	* added by doyoung
	* 2006.07.24
	*/
    public static String getTrimAddress(String address) {
		if (address == null) {
			return null;
		}

		String addr_trim = address.trim();

		if (addr_trim.indexOf("\\\"") > 0) {
			addr_trim = addr_trim.replaceAll("\\\\\"", "");
		}

		return addr_trim;
	}

	/*
	* added by doyoung
	* 2006.09.01
	*/
	public static String getString(InternetAddress ia) {
		return getString(new InternetAddress[] {ia});
	}

	public static String getString(InternetAddress[] ias) {
		if (!TMailUtility.isNull(ias)) {
			String addr = "";

			for (int i = 0; i < ias.length; i++) {
				addr += ias[i].toString();

				if (i+1 < ias.length) {
					addr += ", ";
				}
			}

			return addr;
		}

		return null;
	}

	/*
	* added by doyoung
	* 2006.09.01
	*/
	public static String getId(String str) {

		String id = str;
		int posAt = id.indexOf("@");

		if(posAt > 0) {
			id = id.substring(0, posAt);
		}

		return id;
	}

	public static String getDomain(String str) {

		String domain = str;
		int posAt = domain.indexOf("@");

		if(posAt > 0) {
			domain = domain.substring(posAt);
		}

		return domain;
	}
}
