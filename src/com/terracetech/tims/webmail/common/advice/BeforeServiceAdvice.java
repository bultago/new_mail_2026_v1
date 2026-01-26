package com.terracetech.tims.webmail.common.advice;

import java.lang.reflect.Method;
import java.util.Locale;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
// import org.directwebremoting.WebContext; // DWR 제거 (2025-10-21)
// import org.directwebremoting.WebContextFactory; // DWR 제거 (2025-10-21)
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.core.NestedRuntimeException;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseService;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.exception.SystemException;
import com.terracetech.tims.webmail.exception.UserAuthException;

/**
 * @deprecated DWR 전용 Advice - Phase 3.5에서 DWR 제거됨 (2025-10-21)
 */
@Deprecated
public class BeforeServiceAdvice implements MethodInterceptor, MethodBeforeAdvice{

	public void before(Method arg0, Object[] arg1, Object serviceClass)
			throws Throwable {

		if(serviceClass instanceof BaseService){
			BaseService serviceInstance = (BaseService)serviceClass;
			
			try {
				serviceInstance.loadHttpResource();	
			} catch (UserAuthException e) {
				LogManager.writeErr(this, e);
				// WebContext ctx = WebContextFactory.get(); // DWR 제거 (2025-10-21)
				// Locale locale = I18nConstants.getBundleUserLocale(ctx.getHttpServletRequest());
				// I18nResources resource = new I18nResources(locale,"common");
				throw new SystemException("auth.fail.notfound");
			}
				
		}
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		Object returnValue = doProceed(invocation);
		
		return returnValue;
	}
	
	private Object doProceed (MethodInvocation invocation) throws Throwable {
		Object returnValue = null;
		try {
			returnValue = invocation.proceed();
		} catch (NestedRuntimeException e) {
			LogManager.writeErr(this, e);
			// WebContext ctx = WebContextFactory.get(); // DWR 제거 (2025-10-21)
			// Locale locale = I18nConstants.getBundleUserLocale(ctx.getHttpServletRequest());
			// I18nResources resource = new I18nResources(locale,"common");
			throw new SystemException("error.default" ,e);
		}finally {
		}
		return returnValue;
	}
}
