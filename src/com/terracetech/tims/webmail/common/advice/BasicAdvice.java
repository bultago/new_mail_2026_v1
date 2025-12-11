package com.terracetech.tims.webmail.common.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.terracetech.tims.webmail.common.log.LogManager;

/**
 * <p><strong>BasicAdvice.java</strong> Class Description</p>
 * <p>·Î±ë ¹× ¿¹¿Ü ºÐ±â¸¦ Ã³¸®ÇÏ´Â ¾îµå¹ÙÀÌ½º</p>
 * <ul>
 * <li>·Î±ë Ã³¸®</li>
 * <li>Æ¯Á¤ ¿¹¿Ü¿¡ ´ëÇÑ ºÐ±â Ã³¸®</li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class BasicAdvice implements MethodInterceptor{
	
	/**
	 * <p>
	 * 	aspectJ Çü½ÄÀÇ ÆÐÅÏ¿¡ ¸ÅÄª µÇ´Â ¸Þ¼­µå È£Ãâ½Ã ½ÇÇà ÀüÈÄÀÇ ·Î±ë Ã³¸®¸¦ ÇÑ´Ù.<br>
	 * 	<li>È£ÃâµÇ´Â ¸Þ¼­µåÀÇ Ç® ³×ÀÓ ·Î±ë</li>
	 * 	<li>¸Þ¼­µå ¼öÇà ¼Ò¿ä½Ã°£ ·Î±ë</li>
	 * </p>
	 *
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 * @param invocation
	 * @return
	 * @throws Throwable 
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		String className = invocation.getMethod().getDeclaringClass().getName();
		String methodName = invocation.getMethod().getName();
		
		LogManager.writeInfo(this, "<< Log info start [" + className + "." + methodName + "] >>");
		if (LogManager.isDebugEnabled())
			AdviceUtil.printArguents(invocation);

		Object returnValue = doProceed(invocation);
		
		LogManager.writeInfo(this, "<< Log info end (Total elalpsed time : " + (System.currentTimeMillis() - start) + " ms) [" + className + "." + methodName + "]  >>");
		
		return returnValue;
	}

	/**
	 * <p>
	 * 	½ÇÁ¦ ½ÇÇàÇØ¾ßÇÒ ¸Þ¼­µå¸¦ ¼öÇà ½ÃÅ²´Ù.<br>
	 * 	<li>µð¹ö±× ·¹º§¿¡¼­ÀÇ ÆÄ¶ó¹ÌÅÍ ·Î±ë</li>
	 * 	<li>¿¹¿Ü ¹ß»ý½Ã ¿¹¿Ü ·Î±ë</li>
	 * 	<li>Á¤ÀÇÇÑ ¿¹¿Ü¿¡ ´ëÇÑ ÀÏ°ý Ã³¸®</li>
	 * </p>
	 *
	 * @param invocation
	 * @return
	 * @return Object
	 * @throws Throwable 
	 */
	private Object doProceed (MethodInvocation invocation) throws Throwable {
		Object returnValue = null;
		try {
			returnValue = invocation.proceed();
		} catch (Throwable e) {
			String className = invocation.getMethod().getDeclaringClass().getName();
			String methodName = invocation.getMethod().getName();
			//·Î±ë Ã³¸® ¸Þ¼­µå µµÃâ
			AdviceUtil.printError(e, className, methodName);
			//TODO ±âÅ¸ Ã³¸® ¸Þ¼­µå µµÃâ
			
		} finally {
			//TODO ÇÊ¼ö Ã³¸®
		}
		return returnValue;
	}
}