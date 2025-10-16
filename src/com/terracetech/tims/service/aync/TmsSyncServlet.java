package com.terracetech.tims.service.aync;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kxml2.io.KXmlSerializer;
import org.kxml2.wap.WbxmlParser;
import org.xmlpull.v1.XmlPullParserException;

import com.terracetech.secure.Base64;
import com.terracetech.tims.service.aync.action.ISyncAction;
import com.terracetech.tims.service.aync.action.Ping;
import com.terracetech.tims.service.aync.util.Roundtrip;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.mobile.manager.SyncListener;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.ByteUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class TmsSyncServlet extends HttpServlet {

	private static final long serialVersionUID = 20100513L;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private static final String USER_AGENT_HEADER = "User-Agent";
	private static final String POLICY_KEY_HEADER = "X-MS-PolicyKey";
	private static final String WWW_AUTHENTICATE_VALUE = "BASIC realm=\"Tms\"";
	private static final String WWW_AUTHENTICATE_HEADER = "WWW-Authenticate";
	private static final String BASIC_AUTH_HEADER = "Authorization";

	private static final String TMS_ASYNC_CMDS[] = { "Sync", "SendMail",
			"SmartForward", "SmartReply", "GetAttachment", "GetHierarchy",
			"CreateCollection", "DeleteCollection", "MoveCollection",
			"FolderSync", "FolderCreate", "FolderDelete", "FolderUpdate",
			"MoveItems", "GetItemEstimate", "MeetingResponse", "Search",
			"Ping", "Provision" };

	private static String TMS_ASYNC_CMDS_CDT = StringUtils.join(",", TMS_ASYNC_CMDS);
	
	public void init() throws ServletException {		
		ApplicationBeanUtil.setContext(getServletContext());
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		setHeaders(resp);
		
		try {
			processPostRequest(req, resp);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		setHeaders(resp);
		
		try {
			processPostRequest(req, resp);
		} catch (XmlPullParserException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		 setHeaders(resp);
		 resp.setHeader("Public", "OPTIONS, POST");
		 resp.setHeader("Allow", "OPTIONS, POST");
	     
		 try
	        {
	            if(authenticate(req, resp) != null)
	            {
	                resp.setHeader("MS-ASProtocolCommands", TMS_ASYNC_CMDS_CDT);
	            }
	        }
	        catch(Exception t)
	        {
	        	log.warn(t.getMessage());
	            sendError(resp, 500);
	        }
	}
	
	private User authenticate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String userPassedIn;
        String uid;
        String pass;
        
        SystemConfigManager systemManager = (SystemConfigManager) ApplicationBeanUtil.getApplicationBean("systemConfigManager");
        String mobileMailUse = systemManager.getMobileMailConfig();
        boolean isMobileMailUse = "enabled".equals(mobileMailUse);	
        if(!isMobileMailUse){
        	resp.addHeader(WWW_AUTHENTICATE_HEADER, WWW_AUTHENTICATE_VALUE);
        	sendError(resp, 403, "Server license check error");
            return null;
        }
        
        String auth = req.getHeader(BASIC_AUTH_HEADER);
        if(auth == null || !auth.toLowerCase().startsWith("basic "))
        {
            resp.addHeader(WWW_AUTHENTICATE_HEADER, WWW_AUTHENTICATE_VALUE);
            sendError(resp, 401, "Unauthorized");
            return null;
        }
        
        String cred = new String(Base64.decode(auth.substring(6)));
        int bslash = cred.indexOf('\\');
        int colon = cred.indexOf(':');
        if(colon == -1 || colon <= bslash + 1){
        	 sendError(resp, 400, "Invalid basic auth credentials");
             return null;
        }
        
        String domain = bslash <= 0 ? "" : cred.substring(0, bslash);
        userPassedIn = cred.substring(bslash + 1, colon);
        uid = cred.substring(bslash + 1, colon);
        pass = cred.substring(colon + 1);
        if(pass.length() == 0)
        {
            sendError(resp, 400, "Empty password");
            return null;
        }
        if(domain.length() > 0 && uid.indexOf('@') == -1)
        {
            if(domain.charAt(0) != '@')
                uid = (new StringBuilder()).append(uid).append('@').toString();
            uid = (new StringBuilder()).append(uid).append(domain).toString();
        }
        
        UserAuthManager userAuthManager = (UserAuthManager) ApplicationBeanUtil.getApplicationBean("userAuthManager");
        
        AuthUser authUser;
		try {
			authUser = userAuthManager.validateUser(userPassedIn, pass, domain);
			if(!authUser.isAuthSuccess()){
				sendError(resp, 401, "Invalid username or password");
	            return null;
			}
		} catch (ParseException e) {
			sendError(resp, 400, "Invalid basic auth credentials");
            return null;
		}
        
		return authUser;
	}

	private void setHeaders(HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("X-Powered-By", "TMS Collaboration Server");
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("MS-Server-ActiveSync", "6.5.7638.1");
		resp.setHeader("MS-ASProtocolVersions", "2.0,2.1,2.5");
	}

	public boolean processPostRequest(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException, XmlPullParserException {
		User user = authenticate(req, resp);
		if(user==null)
			return false;

		String deviceId;
        String deviceType;
        String cmd;
        String userAgent;
        String protocolVersion;
        String policyKey;
        
        deviceId = req.getParameter("DeviceId");
        if(deviceId == null || deviceId.length() == 0){
        	 sendError(resp, 400, "Missing device ID");
             return false;
        }
        
        deviceType = req.getParameter("DeviceType");
        if(deviceType == null || deviceType.length() == 0)
        {
            sendError(resp, 400, "Missing device type");
            return false;
        }
        
        cmd = req.getParameter("Cmd");
        if(cmd == null || cmd.length() == 0)
        {
            sendError(resp, 400, "Missing TmsSync command");
            return false;
        }
        
        userAgent = req.getHeader(USER_AGENT_HEADER);
        protocolVersion = req.getHeader("MS-ASProtocolVersion");
        policyKey = req.getHeader(POLICY_KEY_HEADER);
        
        log.debug(deviceId);
        log.debug(deviceType);
        log.debug(cmd);
        log.debug(userAgent);
        log.debug(protocolVersion);
        log.debug(policyKey);
        
        processCommand(user, deviceId, deviceType, protocolVersion, cmd, req, resp);
        
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private void processCommand(User user, String deviceId, String deviceType,
			String protocolVersion, String cmdStr, HttpServletRequest req,
			HttpServletResponse resp) throws IOException, XmlPullParserException {
		
		try {
			ISyncAction cmd = null;
			if(cmdStr.equals("Ping")){
				SyncListener.cancelListener(user, deviceId);
				
				Ping ping = (Ping)(Ping)req.getAttribute(Ping.class.getName());
				if(ping ==null){
					cmd = new Ping(req, deviceId);
					cmd.setUser(user);
				}
			}else{
				Class cmdClass = Class.forName((new StringBuilder()).append("com.terracetech.tims.service.aync.action.").append(cmdStr).toString());
				cmd = (ISyncAction) cmdClass.newInstance();
				cmd.setUser(user);	
			}
			
			log.debug("======================================== ActiveSync START ===========================================================");
			parseRequest(cmd, req);
			process(cmd, req, user, deviceId, deviceType);
			sendResponse(cmd, resp);
			log.debug("======================================== ActiveSync END ============================================================");
			
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (InstantiationException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (WbxmlException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void process(ISyncAction cmd, HttpServletRequest req, User user, String deviceId, String deviceType) {
		try {
			cmd.process(deviceId, deviceType);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void sendResponse(ISyncAction cmd, HttpServletResponse resp)
			throws IOException, WbxmlException {
		ByteArrayOutputStream bao = null;
		ByteArrayInputStream bis = null;
		WbxmlSerializer serializer = null;
		try {
			bao = new ByteArrayOutputStream();
			serializer = new WbxmlSerializer(bao);
			cmd.encodeResponse(serializer);
			
			byte[] wbxml = bao.toByteArray();
			bis = new ByteArrayInputStream(wbxml);
			ByteUtil.copy(bis, true, resp.getOutputStream(), false);
			
			resp.setContentType("application/vnd.ms-sync.wbxml");
			resp.setContentLength(wbxml.length);
			
			log.debug(serializer.getXmlWriter().getBuffer().toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			ByteUtil.closeStream(bao);
			ByteUtil.closeStream(bis);
			if(serializer != null)
				serializer.closeStream();
		}
		
		
	}
	
	private void parseRequest(ISyncAction cmd, HttpServletRequest req) throws IOException, XmlPullParserException{
		byte reqBytes[] = null;
        if(req.getContentLength() > 0){
        	reqBytes = ByteUtil.getContent(req.getInputStream(), req.getContentLength());
        }
            
        if(reqBytes != null){
        	ByteArrayInputStream bis = null;
        	try {
        		bis = new ByteArrayInputStream(reqBytes);
            	WbxmlParser parser = Wbxml.createParser();
            	parser.setInput(bis, null);
            	
            	KXmlSerializer xs = new KXmlSerializer();
        		ByteArrayOutputStream bas = new ByteArrayOutputStream();
        		xs.setOutput(bas, null);
        		new Roundtrip(parser, xs).roundTrip();
        		
        		log.debug(bas.toString());
            	cmd.parseRequest(bas.toString());
            	
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}finally{
				ByteUtil.closeStream(bis);
			}
        	
        }
	}
	
	private void sendError(HttpServletResponse resp, int errCode) throws IOException{
		resp.sendError(errCode, String.valueOf(errCode));
	}
	
	private void sendError(HttpServletResponse resp, int errCode, String reason) throws IOException
	{
	    resp.sendError(errCode, reason);
	}
}
