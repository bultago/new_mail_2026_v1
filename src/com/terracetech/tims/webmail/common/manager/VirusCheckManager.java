package com.terracetech.tims.webmail.common.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import com.sun.mail.util.TraceInputStream;
import com.sun.mail.util.TraceOutputStream;
import com.terracetech.tims.webmail.common.log.LogManager;

public class VirusCheckManager {
	private String host;
	private int port;
	private Socket socket;
	private boolean debug;
	
	private TraceInputStream traceInput; // the Tracer
	private TraceOutputStream traceOutput; // the Tracer
	
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	
	private byte[] buffer = new byte[1024];
	private int readIdx= 0;
	
	private String resultCode;
	private boolean detect;
	private boolean repair;
	private String virusName;
	
	private StringBuffer icap_res = null;
	private StringBuffer res_body = null;
	private StringBuffer content = null;
	
	public VirusCheckManager(String host, int port, boolean debug){
		this.host = host;
		this.port = port;
		this.debug = debug;
		
		this.virusName = "";
		
		this.detect = false;
		this.repair = true;
	}
	
	public boolean makeConnection(){
		boolean isConnect = false;
		try {
			this.socket = new Socket(this.host, this.port);
			this.traceInput = new TraceInputStream(this.socket.getInputStream(), System.out);
			this.traceOutput = new TraceOutputStream(socket.getOutputStream(), System.out);				
			this.traceInput.setTrace(debug);
			this.traceOutput.setTrace(debug);			
			this.dataInputStream = new DataInputStream(new BufferedInputStream(this.traceInput));
			this.dataOutputStream = new DataOutputStream(new BufferedOutputStream(this.traceOutput));
			
			isConnect = true;
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return isConnect;
	}
	
	public boolean checkVirus(String fileName){
		boolean result = true;
		ByteArrayOutputStream bout = null;
		FileInputStream fileInputStream = null;
		int fileByte = 0;
		int fileSize = 0;
		try {
			this.dataOutputStream.writeBytes(this.getOptionHeader().toString());
			this.dataOutputStream.flush();
			this.readOptionResponse();
			
			this.dataOutputStream.writeBytes(this.getMsgHeader().toString());
			this.dataOutputStream.flush();			
			
			fileInputStream = new FileInputStream(new File(fileName));
			bout = new ByteArrayOutputStream();

	        while((fileByte = fileInputStream.read()) != -1){
	            bout.write((byte)fileByte);
	        }

	        fileSize = bout.size();
	        this.dataOutputStream.writeBytes(Integer.toHexString(fileSize)+"\r\n");
	        this.dataOutputStream.flush();

	        this.dataOutputStream.write(bout.toByteArray());
	        this.dataOutputStream.flush();

	        this.dataOutputStream.writeBytes("\r\n\r\n0\r\n\r\n");
	        this.dataOutputStream.flush();	
	        
	        this.readResponse();
	        
	        result = this.parse();
	        
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		} finally{
			if(bout != null){
				try {
					bout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(fileInputStream != null){
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
	
	public void closeConnection(){
		if(this.traceInput != null){
			try {
				this.traceInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(this.traceOutput != null){
			try {
				this.traceOutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(this.dataInputStream != null){
			try {
				this.dataInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(this.dataOutputStream != null){
			try {
				this.dataOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(this.socket != null){
			try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void readOptionResponse(){
        int b = 0;
        boolean gotEND=false; //CRLFCRLF

		byte[] bu =  new byte[1024];

        try{
        while(!gotEND && (b = this.dataInputStream.read()) != -1 ){
            if( b == '\n' && bu[readIdx-1] == '\r' && bu[readIdx-2] == '\n' && bu[readIdx-3]=='\r'){
                gotEND = true;
            }
            if (readIdx >= 1024){
                growBuffer(1024);
            }
            bu[readIdx++] = (byte)b;
        }
        }catch(IOException e){
        	LogManager.writeErr(this, e.getMessage(), e);
        }
    }
	
	private void growBuffer(int incr) {
		int sz = this.buffer.length;
	    byte[] nbuf = new byte[sz + incr];
	    if (this.buffer != null){
	        System.arraycopy(this.buffer, 0, nbuf, 0, this.readIdx);
	    }
	    this.buffer = nbuf;
	    sz += incr;
	}
	
	private StringBuffer getOptionHeader(){
		StringBuffer msg = new StringBuffer();
		msg.append("OPTIONS icap://"+host+"/avscan ICAP/1.0\r\n");
		msg.append("Host: "+host + "\r\n\r\n");
		return msg;
	}
	
	
	private StringBuffer getMsgHeader(){
		StringBuffer msg = new StringBuffer();
		String policy = "SCAN";

		msg.append("RESPMOD icap://"+host+":"+port+"/AVSCAN?action="+policy+" ICAP/1.0\r\n");

		//req_hdr
        StringBuffer sb = new StringBuffer();
		sb.append("GET http://www.symantec.com/file HTTP/1.1\r\n");
		sb.append("Host: "+host + "\r\n\r\n");
		String req_hdr = sb.toString();
        int len_req_hdr = req_hdr.length();

        //res_hdr
        sb = new StringBuffer();
        sb.append("HTTP/1.1 300 OK\r\n");
        sb.append("Transfer-Encoding: chunked\r\n\r\n");
        String res_hdr =  sb.toString();

        int len_res_hdr = res_hdr.length();
        int len_res_body = len_res_hdr + len_req_hdr;

        //icap_hdr
        sb = new StringBuffer();
        sb.append("Host: " + host + ":"+port +"\r\n");
        sb.append("Allow: " + 204 +"\r\n");
        sb.append("Encapsulated: req-hdr=0, res-hdr=");
        sb.append(Integer.toString(len_req_hdr));
        sb.append(", res-body="+ Integer.toString(len_res_body));
        sb.append("\r\n\r\n");
        String icap_hdr = sb.toString();

		msg.append(icap_hdr);
		msg.append(req_hdr);
		msg.append(res_hdr);
		return msg;
	}
	
	private void readResponse(){
		int b = 0;
		boolean gotZERO=false;		
		try{
		while(!gotZERO && (b = this.dataInputStream.read()) != -1 ){
			if( b == '0' && this.buffer[this.readIdx-1] == '\n'){
				gotZERO = true;
			}
			if (this.readIdx >= 1024){
           		this.growBuffer(1024);
			}
			this.buffer[this.readIdx++] = (byte)b;
		}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private boolean parse(){
		boolean result = true;
		DataInputStream msgIn = new DataInputStream(new ByteArrayInputStream(this.buffer));

		icap_res = new StringBuffer();
		res_body = new StringBuffer();
		content = new StringBuffer();

		String line = "";
		try {
			line = msgIn.readLine();
			StringTokenizer str = new StringTokenizer(line, " ");
			str.nextToken();
			this.resultCode = (String) str.nextToken();
		
			if (this.resultCode.equalsIgnoreCase("204")) {
				return result;
			}

			if (!this.resultCode.equalsIgnoreCase("201") && !this.resultCode.equalsIgnoreCase("200")) {
				return result;
			}

			if (this.resultCode.equalsIgnoreCase("200"))
				this.repair = false;
			if (this.resultCode.equalsIgnoreCase("201"))
				this.repair = true;

			while( (line= msgIn.readLine()) != null && !line.startsWith("Encapsulated:")){
				this.icap_res.append(line+"\r\n");
				if(line != null){
					if(line.startsWith("X-Violations-Found")){
						this.detect = true;
						msgIn.readLine();
						this.virusName = (msgIn.readLine()).trim();
					}
				}
			}

			if (this.detect == false)
				return result;

			line = msgIn.readLine();

			while ((line = msgIn.readLine()) != null && line.length() != 0){
				this.res_body.append(line + "\r\n");
			}

			line = msgIn.readLine();// size

			while ((line = msgIn.readLine()) != null && !line.trim().equals("0")){
				this.content.append(line.trim() + "\r\n");
			}	
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		} finally{
			if(msgIn != null){
				try {
					msgIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
	
	public void updateFile(String fname) {
		FileOutputStream f = null;
		try {
			f = new FileOutputStream(fname);
			f.write((this.content.toString()).getBytes());
			f.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(f != null){
				try {
					f.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public DataInputStream getDataInputStream() {
		return dataInputStream;
	}

	public void setDataInputStream(DataInputStream dataInputStream) {
		this.dataInputStream = dataInputStream;
	}

	public DataOutputStream getDataOutputStream() {
		return dataOutputStream;
	}

	public void setDataOutputStream(DataOutputStream dataOutputStream) {
		this.dataOutputStream = dataOutputStream;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public int getReadIdx() {
		return readIdx;
	}

	public void setReadIdx(int readIdx) {
		this.readIdx = readIdx;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String result) {
		this.resultCode = result;
	}

	public boolean isDetect() {
		return detect;
	}

	public void setDetect(boolean detect) {
		this.detect = detect;
	}

	public boolean isRepair() {
		return repair;
	}

	public void setRepair(boolean repair) {
		this.repair = repair;
	}

	public String getVirusName() {
		return virusName;
	}

	public void setVirusName(String virusName) {
		this.virusName = virusName;
	}

	public StringBuffer getIcap_res() {
		return icap_res;
	}

	public void setIcap_res(StringBuffer icap_res) {
		this.icap_res = icap_res;
	}

	public StringBuffer getRes_body() {
		return res_body;
	}

	public void setRes_body(StringBuffer res_body) {
		this.res_body = res_body;
	}

	public StringBuffer getContent() {
		return content;
	}

	public void setContent(StringBuffer content) {
		this.content = content;
	}
}