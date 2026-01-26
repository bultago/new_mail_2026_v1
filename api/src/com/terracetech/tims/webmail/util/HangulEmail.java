/**
 * HangulEmail.java 2009. 2. 25.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <p><strong>HangulEmail.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class HangulEmail {
	public static void main (String [] argv) throws Exception {
		String name = null;

		if(argv.length < 3) {
			// printUsage();
		}

        for (int optind = 0; optind < argv.length; optind++) {
            if (argv[optind].equals("-n")) {
                name = argv[++optind];
            } else if (argv[optind].startsWith("-")) {
                printUsage();
            } else {
                break;
            }
        }

		String email = getHangulEmail(name);

		System.out.println("-- email : ["+email+"]");
	}

    public static boolean isHangulEmail(String hemail) {
		return isHangulAddress(hemail);
	}

    public static boolean isHangulAddress(String hemail) {
		byte[] b = hemail.getBytes();

		if (hemail.indexOf("@") < 0) {
			return false;
		}

		if (hemail.indexOf("\"") >= 0 
			&& hemail.indexOf("<") > 0 
			&& hemail.indexOf(">") > 0) {
			return false;
		}

		for (int i = 0; i < b.length; i++) {
			if (b[i] <= 20 || b[i] >= 127) {	// SPACE < email < DEL
				return true;
			}
		}

		return false;
	}

    public static String getHangulEmail(String hemail) {

		if (!isHangulAddress(hemail)) {
			return "";
		}

		String personal	= hemail.substring(0, hemail.indexOf("@"));
		String address	= getHangulAddress(hemail);
		String email	= "\""+personal+"\"<"+address+">";

		if (address.indexOf("@") > 0) {
			return email;
		}

		return "";
	}

    public static String getHangulAddress(String hemail) {

		try {
			String u = "http://go.netpia.com/nlemail.asp?"
				+"nlemail="+hemail
				+"&charset=uhc&language=ko&com=hmail-api";

			URL url = new URL(u);

			InputStream in = url.openStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];

			int n = 0;

			while((n = in.read(buffer)) != -1) {
				bos.write(buffer, 0, n);
			}

			in.close();
			bos.close();

			String retStr = bos.toString();

			// get email
			if (retStr.startsWith("mailto:")) {
				return retStr.substring(7);
			}
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException : ["+hemail+"]");

		} catch (IOException e) {
			System.out.println("IOException : ["+hemail+"]");
		}

		return "";
	}

    public static void printUsage() {
        System.out.println("Usage: java HangulEmail [-n name]");
        System.exit(1);
    }
}
