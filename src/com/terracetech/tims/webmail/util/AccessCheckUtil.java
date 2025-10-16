package com.terracetech.tims.webmail.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.common.vo.WebAccessInfoVO;

public class AccessCheckUtil {

    public final static String WEB_ACCESS = "web_access_";
    public final static String WEB_ACCESS_TYPE = "web_access_type";
    public final static String WEB_ACCESS_ALLOW_BIGATTACH = "web_access_ab";
    public final static String WEB_ACCESS_IP = "web_access_ip";
    public final static String BIG_ATTACH_URL = "/mail/downloadBigAttach.do";
    
    public static boolean checkAccessAllowIp(String remoteIp, String url) {
        if (remoteIp == null || url == null) return false;
        boolean isValidIp = false;
        SystemConfigDao systemConfigDao = (SystemConfigDao)ApplicationBeanUtil.getApplicationBean("systemConfigDao");
        Map<String, String> webAccessMap = systemConfigDao.getWebAccessConfig();
        
        if (webAccessMap != null && !webAccessMap.isEmpty()) {
            WebAccessInfoVO webAccessInfoVO = new WebAccessInfoVO();
            webAccessInfoVO.setWebAccessType(webAccessMap.get(WEB_ACCESS_TYPE));
            webAccessInfoVO.setAllowBigattach(webAccessMap.get(WEB_ACCESS_ALLOW_BIGATTACH)); 
            String ipStr = webAccessMap.get(WEB_ACCESS_IP);
            if (!StringUtils.isEmpty(ipStr)) {
                String[] ipList = ipStr.split("\\,");
                webAccessInfoVO.setAccessIpList(ipList);
            }
            if ("part".equalsIgnoreCase(webAccessInfoVO.getWebAccessType())) {
                Set<String> compareIps = new HashSet<String>();
                Set<String> compareSubnetIps = new HashSet<String>();
                String[] ipList = webAccessInfoVO.getAccessIpList();
                if (ipList != null && ipList.length > 0) {
                    for(int i=0; i<ipList.length; i++) {
                        if (ipList[i] == null) {
                            continue;
                        }
                        String[] ipData = ipList[i].split("\\:");
                        if (ipData.length != 2) {
                            continue;
                        }
                        String type = ipData[0];
                        String value = ipData[1];
                        if ("list".equalsIgnoreCase(type)) {
                            try {
                                List<String> ipListByGroup = systemConfigDao.readIpGroup(value);
                                for (String listIp : ipListByGroup) {
                                    if (listIp.indexOf("/") > 0) {
                                        compareSubnetIps.add(listIp);
                                    } else {
                                        compareIps.add(listIp);
                                    }
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        } else {
                            if (value.indexOf("/") > 0) {
                                compareSubnetIps.add(value);
                            } else {
                                compareIps.add(value);
                            }
                        }
                    }
                }
                
                if (!compareIps.isEmpty()) {
                    Iterator<String> ips = compareIps.iterator();

                    while (ips.hasNext()) {
                        String ip = ips.next();

                        if (IPUtils.ipRangeMatch(remoteIp, ip)) {
                            isValidIp = true;
                            break;
                        }
                    }
                }
                
                if (!isValidIp && !compareSubnetIps.isEmpty()) {
                    Iterator<String> ips = compareSubnetIps.iterator();

                    while (ips.hasNext()) {
                        String ip = ips.next();

                        if (IPUtils.cidrMatch(remoteIp, ip)) {
                            isValidIp = true;
                            break;
                        }
                    }
                }
                
                if (!isValidIp && "on".equalsIgnoreCase(webAccessInfoVO.getAllowBigattach())) {
                    if (BIG_ATTACH_URL.equalsIgnoreCase(url)) {
                        isValidIp = true;
                    }
                }
            } else {
                isValidIp = true;
            }
        } else {
            isValidIp = true;
        }
        return isValidIp;
    }
    
    public static boolean isHybridUrl(String url) {
        if (StringUtils.isEmpty(url)) return false;
        String extUrls = EnvConstants.getUtilSetting("web.ext.url");
        if (StringUtils.isEmpty(extUrls)) return false;
        String[] extUrlData = extUrls.split("\\|");
        boolean isSuccess = false;
        for (String extUrl : extUrlData) {
            if (url.startsWith(extUrl)) {
                isSuccess = true;
                break;
            }
        }
        return isSuccess;
    }
}
