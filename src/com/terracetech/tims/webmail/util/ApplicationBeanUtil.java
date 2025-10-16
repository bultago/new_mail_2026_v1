package com.terracetech.tims.webmail.util;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ApplicationBeanUtil {
	
	static ServletContext context = null;

	public static void setContext(ServletContext context){
		System.out.println(context);
		if(ApplicationBeanUtil.context == null){
			ApplicationBeanUtil.context = context;
		}		
	}
	
	public static Object getApplicationBean(String beanName){
		WebApplicationContext wac = WebApplicationContextUtils
								.getRequiredWebApplicationContext(context);

		return wac.getBean(beanName);
	}
}
