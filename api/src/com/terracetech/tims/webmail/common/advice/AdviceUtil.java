package com.terracetech.tims.webmail.common.advice;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInvocation;
import com.terracetech.tims.webmail.common.log.LogManager;

/**
 * <p><strong>AdviceUtil.java</strong> Class Description</p>
 * <p>Advice ���� ���������� ����ϴ� ��ƿ��Ƽ �޼���</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
@SuppressWarnings("unchecked")
public class AdviceUtil {
	
	private static Map<Class, String> primitiveTypes = null;
	
	static {
		Map<Class, String> tmpMap = new HashMap<Class, String> (8);
		
		tmpMap.put(Byte.class, "byte");
		tmpMap.put(Character.class, "char");
		tmpMap.put(Boolean.class, "boolean");
		tmpMap.put(Short.class, "short");
		tmpMap.put(Integer.class, "int");
		tmpMap.put(Long.class, "long");
		tmpMap.put(Float.class, "float");
		tmpMap.put(Double.class, "double");
		
		primitiveTypes = Collections.unmodifiableMap(tmpMap);
		tmpMap = null;
	}
	
	private AdviceUtil(){
		super();
	}
	
	/**
	 * <p>
	 * ����� ����϶� �޼��尡 ȣ��Ǹ� ���޵Ǵ� ���ڵ��� �α��Ѵ�.<br>
	 * ��µǴ� ���ڵ��� ������ ����.
	 * 	<li>HttpServletRequest Info<br>
	 * 			Host address,
	 * 			User address
	 * 			Protocol,
	 * 			HTTP Method,
	 * 			Character encoding,
	 * 			URL,
	 * 			Request Parameters
	 * 	</li>
	 * 	<li>String</li>
	 * 	<li>Primitive</li>
	 * 	<li>Object.toString()</li>
	 * </p>
	 *
	 * @param invocation
	 * @return void
	 */
	static void printArguents (MethodInvocation invocation) {
		Object[] args = invocation.getArguments();
		try {
			LogManager.writeDebug(AdviceUtil.class, "===== [Arguents log start] =====");
			for (Object arg: args) {
				if (arg == null)
					continue;

				if (arg instanceof HttpServletRequest) {
					HttpServletRequest request = (HttpServletRequest)arg;
					Enumeration<String> params = request.getParameterNames();
					LogManager.writeDebug(AdviceUtil.class, "=== Request info [start] ===");
					LogManager.writeDebug(AdviceUtil.class, "Host address : " + request.getLocalAddr() + " (port : " + request.getLocalPort() + ")" );
					LogManager.writeDebug(AdviceUtil.class, "User address : " + request.getRemoteAddr() + " (port : " + request.getRemotePort() + ")" );
					LogManager.writeDebug(AdviceUtil.class, "Protocol : " + request.getProtocol());
					LogManager.writeDebug(AdviceUtil.class, "HTTP Method : " + request.getMethod());
					LogManager.writeDebug(AdviceUtil.class, "Character encoding : " + request.getCharacterEncoding());
					LogManager.writeDebug(AdviceUtil.class, "URL : [" + request.getRequestURL() + "]");
					
					String paramKey = null;
					for (int i = 0; params.hasMoreElements(); i++) {
						paramKey = params.nextElement();
						LogManager.writeDebug(AdviceUtil.class, "Parameter info[" + i +"] : " + paramKey +"="+request.getParameter(paramKey));
					}
					LogManager.writeDebug(AdviceUtil.class, "=== Request info [end] =====");
					
				} else if (arg instanceof String) {
					LogManager.writeDebug(AdviceUtil.class, "String argument : [" + arg + "]");
					
				} else if (primitiveTypes.containsKey(arg.getClass())) {   
					LogManager.writeDebug(AdviceUtil.class, "Primitive argument : " + primitiveTypes.get(arg.getClass()) + "[" + arg + "]");
					
				} else {
					LogManager.writeDebug(AdviceUtil.class, 
							"Object argument : \n ** Type \t: " + arg.getClass().getName() + 
							" \n ** toString \t: "+ arg);
				}
			}
			LogManager.writeDebug(AdviceUtil.class, "===== [Arguents log end] ======");
		} catch (Exception e) {
			LogManager.writeErr(AdviceUtil.class, "EXCEPTION MESSAGE [" + e.getMessage() + "]");
		} finally {
			args = null;
		}
	}
	
	/**
	 * <p>
	 * 	��� Ÿ���� ���ܿ� ���� �α�ó�� �Ѵ�.<br>
	 * 	�⺻������ com.terracetech ������ Stack Trace �Ѵ�.
	 * </p>
	 *
	 * @param e
	 * @param className
	 * @param methodName
	 * @return void
	 */
	static void printError(Throwable e, String className, String methodName) {
		LogManager.writeErr(AdviceUtil.class, "===============[ERROR MESSAGE START]===============");
        LogManager.writeErr(AdviceUtil.class, "Intercepted exception of type [ "+ e.getClass().getName()+ " ]");
        LogManager.writeErr(AdviceUtil.class, "Thrown by target class [ "+ className + "." + methodName+ " ]");
        LogManager.writeErr(AdviceUtil.class, "Error Message [ "+ e.getMessage()+" ]");
           
        if (LogManager.isDebugEnabled()) {
        	StackTraceElement[] stackTraces = e.getStackTrace();
        	for (StackTraceElement stackTrace: stackTraces){
            	if( stackTrace.getClassName().indexOf("com.terracetech") != -1)
            		LogManager.writeErr(AdviceUtil.class, stackTrace);
            }
        }
        LogManager.writeErr(AdviceUtil.class, "================[ERROR MESSAGE END]================");
	}
}