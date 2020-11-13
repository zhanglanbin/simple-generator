package com.simple.generator.service.generate.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.simple.generator.constant.PathConstant;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.service.generate.GenerateService;
import com.simple.generator.service.generatro.GeneratroService;
import com.simple.generator.utils.GeneratorUtils;

@Service
public class UnifiedReturnServiceImpl implements GenerateService {
	
	private static final Logger log = LoggerFactory.getLogger(UnifiedReturnServiceImpl.class);

	@Autowired
	private GeneratroService generatroService;
	@Autowired
	private SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO;
	
	@Override
	public boolean generate() {
		String writerPath = GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), simpleGeneratorConfigurationDTO.getGroupId() + ".response");
		
		Context context = new Context();
		context.setVariable("simpleGeneratorConfigurationDTO", simpleGeneratorConfigurationDTO);
		context.setVariable("serialVersionUID", GeneratorUtils.getSerialVersionUID().toString());
		context.setVariable("notesDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
		//MyMessage
		String myMessageFilePath = writerPath + File.separatorChar + "MyMessage.java";
		boolean result = generatroService.templateAnalysisGenerateFile(context, myMessageFilePath, PathConstant.generatorTemplatePath+"unifiedReturn/", "MyMessage");
		if(!result) {
			log.error("{}::生成失败!", myMessageFilePath);
		}
		
		//Page
		String pageFilePath = writerPath + File.separatorChar + "Page.java";
		result = generatroService.templateAnalysisGenerateFile(context, pageFilePath, PathConstant.generatorTemplatePath+"unifiedReturn/", "Page");
		if(!result) {
			log.error("{}::生成失败!", myMessageFilePath);
		}
		
		//ResponseResult
		String responseResultPath = writerPath + File.separatorChar + "ResponseResult.java";
		result = generatroService.templateAnalysisGenerateFile(context, responseResultPath, PathConstant.generatorTemplatePath+"unifiedReturn/", "ResponseResult");
		if(!result) {
			log.error("{}::生成失败!", myMessageFilePath);
		}
		
		//State
		String statePath = writerPath + File.separatorChar + "State.java";
		result = generatroService.templateAnalysisGenerateFile(context, statePath, PathConstant.generatorTemplatePath+"unifiedReturn/", "State");
		if(!result) {
			log.error("{}::生成失败!", myMessageFilePath);
		}
		
		return true;
	}
}
