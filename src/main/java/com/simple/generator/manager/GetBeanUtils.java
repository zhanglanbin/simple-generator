package com.simple.generator.manager;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.simple.generator.service.generate.GenerateService;

@Component
public class GetBeanUtils implements InitializingBean, ApplicationContextAware {
	
	private static ApplicationContext applicationContext;
	
	public static Map<String, GenerateService> getGenerateServiceAllImplClass() {
		Map<String, GenerateService> beanMap = applicationContext.getBeansOfType(GenerateService.class);
		return beanMap;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		GetBeanUtils.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
