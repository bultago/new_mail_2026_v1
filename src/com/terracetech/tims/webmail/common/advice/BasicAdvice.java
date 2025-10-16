package com.terracetech.tims.webmail.common.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.terracetech.tims.webmail.common.log.LogManager;

/**
 * <p><strong>BasicAdvice.java</strong> Class Description</p>
 * <p>로깅 및 예외 분기를 처리하는 어드바이스</p>
 * <ul>
 * <li>로깅 처리</li>
 * <li>특정 예외에 대한 분기 처리</li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class BasicAdvice implements MethodInterceptor{
	
	/**
	 * <p>
	 * 	aspectJ 형식의 패턴에 매칭 되는 메서드 호출시 실행 전후의 로깅 처리를 한다.<br>
	 * 	<li>호출되는 메서드의 풀 네임 로깅</li>
	 * 	<li>메서드 수행 소요시간 로깅</li>
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
	 * 	실제 실행해야할 메서드를 수행 시킨다.<br>
	 * 	<li>디버그 레벨에서의 파라미터 로깅</li>
	 * 	<li>예외 발생시 예외 로깅</li>
	 * 	<li>정의한 예외에 대한 일괄 처리</li>
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
			//로깅 처리 메서드 도출
			AdviceUtil.printError(e, className, methodName);
			//TODO 기타 처리 메서드 도출
			
		} finally {
			//TODO 필수 처리
		}
		return returnValue;
	}
}