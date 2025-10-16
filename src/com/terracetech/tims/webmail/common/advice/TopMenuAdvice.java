package com.terracetech.tims.webmail.common.advice;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;

public class TopMenuAdvice implements AfterReturningAdvice {

	public void afterReturning(Object arg0, Method arg1, Object[] arg2,
			Object arg3) throws Throwable {

		System.out.println(arg3);
		
	}

}
