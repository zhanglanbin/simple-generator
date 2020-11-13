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
import com.simple.generator.service.generate.impl.ApplicationYmlServiceImpl;
import com.simple.generator.service.generate.impl.CommonQueryModelServiceImpl;
import com.simple.generator.service.generate.impl.ControllerServiceImpl;
import com.simple.generator.service.generate.impl.DaoServiceImpl;
import com.simple.generator.service.generate.impl.LogbackJavaConfigServiceImpl;
import com.simple.generator.service.generate.impl.LogbackXmlConfigServiceImpl;
import com.simple.generator.service.generate.impl.MainAppServiceImpl;
import com.simple.generator.service.generate.impl.MapperServiceImpl;
import com.simple.generator.service.generate.impl.ModelMDServiceImpl;
import com.simple.generator.service.generate.impl.ModelQueryServiceImpl;
import com.simple.generator.service.generate.impl.ModelServiceImpl;
import com.simple.generator.service.generate.impl.MybatisXmlConfigServiceImpl;
import com.simple.generator.service.generate.impl.PomServiceImpl;
import com.simple.generator.service.generate.impl.ServiceImplServiceImpl;
import com.simple.generator.service.generate.impl.ServiceServiceImpl;
import com.simple.generator.service.generate.impl.SwaggerJavaConfigServiceImpl;
import com.simple.generator.service.generate.impl.UnifiedReturnServiceImpl;

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
        	// 是否调用生成方法
        	boolean isGenerate = true;
        	
        	if(simpleGeneratorConfigurationDTO.isGenerateController()==false && serviceImpl instanceof ControllerServiceImpl) {
        		isGenerate = false;
        	} else if(simpleGeneratorConfigurationDTO.isGenerateService()==false 
        			&& (serviceImpl instanceof ServiceServiceImpl || serviceImpl instanceof ServiceImplServiceImpl)) {
        		isGenerate = false;
        	} else if(simpleGeneratorConfigurationDTO.isGenerateDao()==false 
        			&& (serviceImpl instanceof DaoServiceImpl
        					|| serviceImpl instanceof MapperServiceImpl
        					|| serviceImpl instanceof MybatisXmlConfigServiceImpl
        				)) {
        		isGenerate = false;
        	} else if(simpleGeneratorConfigurationDTO.getIsCreateProject()==false
        			&& (serviceImpl instanceof PomServiceImpl
        					|| serviceImpl instanceof UnifiedReturnServiceImpl
        					|| serviceImpl instanceof MybatisXmlConfigServiceImpl
        					|| serviceImpl instanceof MainAppServiceImpl
        					|| serviceImpl instanceof LogbackJavaConfigServiceImpl
        					|| serviceImpl instanceof LogbackXmlConfigServiceImpl
        					|| serviceImpl instanceof ApplicationYmlServiceImpl
        					|| serviceImpl instanceof SwaggerJavaConfigServiceImpl
        				)) {
        		isGenerate = false;
        	} else if(simpleGeneratorConfigurationDTO.isSwaggerAnnotation()==false && serviceImpl instanceof SwaggerJavaConfigServiceImpl) {
        		isGenerate = false;
        	} else if(simpleGeneratorConfigurationDTO.isGenerateModelMD()==false && serviceImpl instanceof ModelMDServiceImpl) {
        		isGenerate = false;
        	} else if(simpleGeneratorConfigurationDTO.isGenerateQueryModel()==false
        			&& (serviceImpl instanceof ModelQueryServiceImpl
        					|| serviceImpl instanceof CommonQueryModelServiceImpl
        			)) {
        		isGenerate = false;
        	} else if(simpleGeneratorConfigurationDTO.isGeneratePojo()==false 
        			&& (serviceImpl instanceof ModelServiceImpl
        					|| serviceImpl instanceof ModelMDServiceImpl
        					|| serviceImpl instanceof ModelQueryServiceImpl
        				)) {
        		isGenerate = false;
        	}
        	
        	if(isGenerate) {
        		log.info("调用::{} 生成代码文件", serviceImpl.getClass().getName());
        		serviceImpl.generate();
        	}
        }
        
		log.info("生成器 -- 工程生成完毕");
	}
}
