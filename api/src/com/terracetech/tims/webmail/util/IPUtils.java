package com.terracetech.tims.webmail.util;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP ��� ���� Util Class
 * 
 * @author lkg1024
 */
public class IPUtils {
	private static final String LOCAL_IP = "127.0.0.1";
	private static final String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    /**
     * IP ���ڿ��� Long ������ ��ȯ�Ѵ�
     * 
     * @param ip
     *            ���ڿ� IP
     * @return ��ȯ�� Long ��
     */
    public static long ip2long(String ip) {
        long f1, f2, f3, f4;
        String[] tokens = ip.split("\\.");
        if (tokens.length != 4) {
            return -1;
        }

        try {
            f1 = Long.parseLong(tokens[0]) << 24;
            f2 = Long.parseLong(tokens[1]) << 16;
            f3 = Long.parseLong(tokens[2]) << 8;
            f4 = Long.parseLong(tokens[3]);

            return f1 + f2 + f3 + f4;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Long ���� IP ���ڿ��� ��ȯ�Ѵ�
     * 
     * @param longValue
     *            Long ��
     * @return ���ڿ� IP
     */
    public static String long2ip(long longValue) {
        StringBuffer ip = new StringBuffer();

        if (longValue < 0) {
            longValue = (long) (longValue + Math.pow(2, 32));
        }

        for (int i = 3; i >= 0; i--) {
            ip.append((int) (longValue / Math.pow(256, i)));
            longValue -= (int) (longValue / Math.pow(256, i)) * Math.pow(256, i);

            if (i > 0) {
                ip.append(".");
            }
        }

        return ip.toString();
    }

    public static boolean ipRangeMatch(String ip, String ips) {
        long startRange = 0L;
        long endRange = 0L;

        if (ips.indexOf("-") > 0) {
            startRange = ip2long(ips.split("-")[0]);
            endRange = ip2long(ips.split("-")[1]);
        } else {
            startRange = ip2long(ips);
            endRange = ip2long(ips);
        }

        long longedIp = ip2long(ip);

        return ((longedIp >= startRange) && (longedIp <= endRange));
    }

    /**
     * Subnet�� �뿪�� IP�� ���ϴ��� ���θ� �����Ѵ�
     * 
     * @param ip
     *            üũ IP
     * @param range
     *            Subnet ����
     * @return ���Կ���
     */
    public static boolean cidrMatch(String ip, String range) {
        if (range.indexOf("/") == -1) {
            return false;
        }

        String subnet = range.split("/")[0];
        int bits = Integer.parseInt(range.split("/")[1]);

        long longIp = ip2long(ip);
        long longSubnet = ip2long(subnet);

        int mask = -1 << (32 - bits);
        longSubnet &= mask;

        return ((longIp & mask) == longSubnet);
    }

	public static Stack<String> parseIpPattern(String[] ipStringArray) {
		
		Stack<String> ipStack = new Stack<String>();
		
		if (ipStringArray == null || ipStringArray.length == 0) {
			return ipStack;
		}
		
		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);

		for (String ipString : ipStringArray) {
			Matcher matcher = pattern.matcher(ipString);
			if(matcher.find()) {
				ipStack.push(matcher.group());
			}
		}
		
		return ipStack;
	}
	
	public static boolean containIp(String[] srcIps, String targetIp) {
        boolean valid = false;
        for (String ip : srcIps) {
            if (ip.indexOf('/') > 0) {
                valid = cidrMatch(targetIp, ip);
            } else {
                valid = ipRangeMatch(targetIp, ip);
            }
            if (valid) {
                return true;
            }
        }
        return false;
    }
	
	public static boolean isLocalIp(String targetIp) {
		return LOCAL_IP.equals(targetIp);
	}
}