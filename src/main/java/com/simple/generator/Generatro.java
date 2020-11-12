package com.simple.generator;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.simple.generator.manager.GetBeanUtils;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.service.generate.GenerateService;

@Component
@Order(value=10)
public class Generatro implements CommandLineRunner {
	
	private static final Logger log = LoggerFactory.getLogger(Generatro.class);
	
	@Resource
	private SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO;
	
	
	@Override
    public void run(String... args) throws Exception {
		System.out.println(simpleGeneratorConfigurationDTO);

        Map<String, GenerateService> beanMap = GetBeanUtils.getGenerateServiceAllImplClass();
        //遍历该接口的所有实现，将其放入map中
        for (GenerateService serviceImpl : beanMap.values()) {
        	log.info("调用::{} 生成代码文件", serviceImpl.getClass().getName());
        	serviceImpl.generate();
        }
        
		log.info("生成器 -- 工程生成完毕");
	}
}
