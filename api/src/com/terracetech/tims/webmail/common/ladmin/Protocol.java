package com.terracetech.tims.webmail.common.ladmin;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import org.eclipse.angus.mail.iap.*;
import org.eclipse.angus.mail.iap.Response;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;

@SuppressWarnings("unchecked")
public class Protocol extends org.eclipse.angus.mail.iap.Protocol{
	private static int adminPort = Integer.parseInt(EnvConstants.getLadminSetting("ladmin.port"));
	private static boolean adminDebug = Boolean.parseBoolean(EnvConstants.getLadminSetting("ladmin.debug"));

	private Map errResult = null;
	
	public Map getErrorResult(){
		return errResult;
	}

	public Protocol(String host)throws IOException, ProtocolException {
		super(host, adminPort, adminDebug, null, "ladmin");
	}
	
	public Protocol(String host, int port) throws IOException, ProtocolException {
		super(host, port, adminDebug, null, "ladmin");
	}	

	public Protocol(String host, int port, boolean debug) throws IOException, ProtocolException {
		super(host, port, debug, null, "ladmin");
	}
	
	public void logout() {
	    command("LOGOUT", null);
	    disconnect();
	}

	public void compulsionClose(){
		disconnect();
	}

	public static int getAdminPort(){
		return adminPort;
	}
	
	public static void setAdminPort(int portValue){
		adminPort = portValue;
	}
	
	public void setConfig(String config) throws ProtocolException {
		Response[] r = this.doCommand("CONFIG_SYNCWITHDB", new String[] { config });
		for (int i = 0; i < r.length - 1; i++) {
			String message = r[i].toString();
			if (message.indexOf(config)>= 0 && message.indexOf("success") != -1){
				break;
			}else
				throw new ProtocolException(message);
		}
	}

	public Map setConfig(String[] config) throws ProtocolException{
	    Response[] r = this.doCommand("CONFIG_SYNCWITHDB", config);
	    return this.makeResult(r);
	}

	private String[] makeResponseArray (String reponseStr) {
		int startIndex = reponseStr.indexOf("*");
        reponseStr = reponseStr.substring(startIndex + 1).trim();
		Pattern pattern = Pattern.compile("(\\s+)");
		
        return pattern.split(reponseStr);
	}
	
	public Map getMap(String command, String[] params) throws ProtocolException{
		Response[] r= this.nonCkeckCommand(command.toUpperCase(), params);
		Map result = new HashMap();
		
		String[] tempValues = null;
        for (int i = 0; i < r.length - 1; i++) {
           	tempValues = this.makeResponseArray(r[i].toString());
           	if (tempValues != null && tempValues.length > 1){
           		String[] tmp = new String[tempValues.length -1];
           		for (int j = 0; j < tmp.length; j++) {
           			tmp[j] = tempValues[j+1];
           		}
           		result.put(tempValues[0], tempValues);
           	}
        }
		return result;
	}
	
	public List getResultStr(String command, String[] params)  throws ProtocolException{
		Response[] r= this.doCommand(command.toUpperCase(), params);
		List results = new ArrayList();
        for (int i = 0; i < r.length - 1; i++) {
           	results.add( this.makeResponseArray(r[i].toString()));
        }
        
        return results;
	}
	
	public List fetchResults(String command, String[] params) throws ProtocolException{
		Response[] r= this.doCommand(command.toUpperCase(), params);
		List results = new ArrayList();
        for (int i = 0; i < r.length - 1; i++) {
           	results.add(this.makeAtomList(r[i]));
        }
        return results;
	}
	
	public String[] fetchResultLine(String command, String[] params) throws ProtocolException{
		Response[] r= this.doCommand(command.toUpperCase(), params);
		List<String> results = new ArrayList<String>();
        for (int i = 0; i < r.length - 1; i++) {
        	results.add(r[i].toString());
        }
        return results.toArray(new String[results.size()]);
	}
	
	public synchronized Map msgProcess(String command, String[] param) throws ProtocolException{
		Response[] r= this.doMsgCommand(command.toUpperCase(), param);
		return this.makeResult(r);
	}

	public synchronized Map process(String command, String[] param) throws ProtocolException{
	    return this.get(command.toUpperCase(), param);
	}
	
	public Map get(String command, String[] params) throws ProtocolException {
		Response[] r = this.doCommand(command.toUpperCase(), params);
		return this.makeResult(r);
	}

	public void fileUpload(String command, String[] params, InputStream stream) throws ProtocolException {
		Argument args = this.makeArgs(params);
		this.doWriteCommand(command.toUpperCase(), args);

  		Response r1 = null;

		try {
			r1 = readResponse();
		} catch (IOException ioex) {			
			LogManager.writeErr(this, ioex.getMessage(), ioex);			
			r1 = Response.ByeResponse;
		} catch (ProtocolException pex) {			
			LogManager.writeErr(this, pex.getMessage(), pex);
		}
		
		String result = r1.toString();
		boolean start = false;
		if ( result.substring(0,1).equals("+")){
			result = result.substring(1).trim();
			if ( result.equals("OK"))
				start = true;
		}
		if ( start == false)
			throw new ProtocolException("Server is not ready for file upload");

        OutputStream output = super.getOutputStream();
        try{
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while((bytesRead = stream.read(buffer,0,1024)) != -1){
				output.write(buffer,0,bytesRead);
			}
			output.flush();
		} catch (Exception ex) {
			throw new ProtocolException(ex.getMessage());
		} 

		Response r = null;
		boolean done = false;
		Vector v = new Vector();
		int count = 0;
		while (!done) {
			count++;
			if (count > 100) {
				break;
			}
			try {
				r = readResponse();
			} catch (IOException ioex) {
				r = Response.ByeResponse;
			} catch (ProtocolException pex) {
				pex.printStackTrace();
				
				break; // skip this response
			}
			v.addElement(r);	
			if (r.isBYE()) // shouldn't wait for command completion response
				done = true;
       		if (r.isContinuation())
				done = true; 
			// If this is a matching command completion response, we are done
			if (r.isTagged() )
				done = true;
		}
		if ( r.isBAD())
			throw new ProtocolException("command unknown or arguments invalid");
		else if ( r.isNO())
			throw new ProtocolException("can��t get version info");
			
	}
	
	public long fileFetch(String command, String[] params)
		throws ProtocolException{
		Argument args = this.makeArgs(params);
		this.doWriteCommand(command.toUpperCase(), args);
		
		int idx = 0;
        byte[] buffer = new byte[1024];
        int size = buffer.length;
        boolean end = false;
        byte b = 0;
        InputStream input = getInputStream();
        try{
            while( end == false ){
                b= (byte)input.read();
                if ( b == '\n'){
                    end = true;
                }
                if ( idx >= size ){
                    byte[] nbuf = new byte[size+10];
                    if ( buffer != null )
                        System.arraycopy(buffer,0,nbuf,0,idx);
                    buffer = nbuf;
                    size += 10;
                }
                buffer[idx++] = b;
            }
        }
        catch(Exception e){
            throw new ProtocolException(e.getMessage());
        }		
        String line = new String(buffer,0, idx);
		
        int position = line.indexOf("{");
        if ( position == -1 ){

			String[] results = line.split(" ");
			String result = null; 
			if ( results[0].substring(0,1).equals("A"))
				result = results[1];
			else
				result = results[0];
			if ( result.equals("NO"))
       			throw new ProtocolException("error");
	        else if ( result.equals("BAD"))
   			 	throw new ProtocolException("command unknown or arguments invalid");
			else 
		       	throw new ProtocolException("file size cannot recognize");
        }
		else{
        	return Long.parseLong(line.substring(position+1, line.indexOf('}')));
        }
	}
	
	public InputStream getFileStream(){
		return getInputStream();
	}

	public long getLogFetch(String[] params)
		throws ProtocolException{		
		Argument args = this.makeArgs(params);
		this.doWriteCommand("WEBMON_DATA_FETCH", args);
		
		long resultSize = 0;
		int idx = 0;
        byte[] buffer = new byte[1024];
        int size = buffer.length;
        boolean end = false;
        byte b = 0;
        InputStream input = getInputStream();

        try{
             while( end == false ){
                b= (byte)input.read();				
                if ( b == '\n'){
                    end = true;
                }

                if ( idx >= size ){
                    byte[] nbuf = new byte[size+10];
                    if ( buffer != null )
                        System.arraycopy(buffer,0,nbuf,0,idx);
                    buffer = nbuf;
                    size += 10;
                }
                buffer[idx++] = b;
            }
        }
        catch(Exception e){
            throw new ProtocolException(e.getMessage());
        }		
        String line = new String(buffer,0, idx);
		
        int position = line.indexOf("{");
        if ( position == -1 ){

			String[] results = line.split(" ");
			String result = null; 
			if ( results[0].substring(0,1).equals("A"))
				result = results[1];
			else
				result = results[0];
			if ( result.equals("NO"))
       			resultSize = Long.parseLong("-1");
	        else if ( result.equals("BAD"))
   			 	resultSize = Long.parseLong("-2");
			else 
		       	resultSize = Long.parseLong("-2");
        }
		else{
        	resultSize = Long.parseLong(
						line.substring(position+1, line.indexOf('}')));
        }

		return resultSize;
	}
	
	public boolean sendCommand (String command, String[] params) {
		boolean jobSuccess = false;
		
		Argument args = this.makeArgs(params);
		Response[] r = command(command, args);
        Response response = r[r.length - 1];
        
		if ( response.isOK()) {
			jobSuccess = true;
        } 
		
		return jobSuccess;
	}
	
	private Response[] doCommand(String command, String[] params) throws ProtocolException{
		Argument args = this.makeArgs(params);
		Response[] r = command(command, args);
        Response response = r[r.length - 1];
        
        if ( response.isNO()) {
        	throw new ProtocolException("error");
        } else if ( response.isBAD()) {
        	throw new ProtocolException("command unknown or arguments invalid");
        } 
        return r;
	}

	private Response[] doMsgCommand(String command, String[] params) throws ProtocolException{
		Argument args = this.makeArgs(params);
		Response[] r = command(command, args);
        Response response = r[r.length - 1];
        
        if ( response.isNO()) {
			errResult = makeResult(r);
        	throw new ProtocolException("error");
        } else if ( response.isBAD()) {
        	throw new ProtocolException("command unknown or arguments invalid");
        } 
        return r;
	}
	
	private Response[] nonCkeckCommand(String command, String[] params) throws ProtocolException{
		Argument args = this.makeArgs(params);
		Response[] r = command(command, args);
        return r;
	}
	
	private void doWriteCommand(String command, Argument args) throws ProtocolException{
		try{
			writeCommand(command, args);
		}catch(IOException ie){
            throw new ProtocolException(ie.getMessage());
		}
	}
	
	private Map makeResult(Response[] r) throws ProtocolException{
		Map result = new HashMap();
		
		for (int i = 0; i < r.length - 1; i++) {
			List atoms = this.makeAtomList(r[i]);
			this.makeAtomMap(result, atoms);
		}
		return result;
	}
	
	private Argument makeArgs(String[] params){
		Argument args = null;
		if ( params != null ){
			args = new Argument();
			for( int i = 0; i < params.length; i++){
				args.writeAtom(params[i]);
			}
		}
		return args;
	}
	
	private void makeAtomMap(Map result, List atoms){
		if (atoms.size() > 1) {
			String mapKey = (String) atoms.get(0);
			atoms.remove(0);
			result.put(mapKey, (String[]) atoms.toArray(new String[atoms.size()]));
		}
	}
	
	private List makeAtomList(Response response) throws ProtocolException {
		int atomNullCnt = 0;
		List tempResult = new ArrayList();
		String atom = null;
		while ((atom = response.readAtom()) != null) {
			if (atom.length() > 0) {
				tempResult.add(atom);
				atomNullCnt = 0;
			} else if (atomNullCnt > 100) {
				throw new ProtocolException("Atom_read_error");
			} else {
				atomNullCnt++;
			}
		}
		return tempResult;
	}
	
}