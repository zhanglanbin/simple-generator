package com.simple.generator.service.generate.impl;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.simple.generator.constant.PathConstant;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.service.generate.GenerateService;
import com.simple.generator.service.generatro.GeneratroService;
import com.simple.generator.utils.GeneratorUtils;

@Service
public class LogbackJavaConfigServiceImpl implements GenerateService {
	
	@Autowired
	private GeneratroService generatroService;
	@Resource
	private SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO;
	
	@Override
	public boolean generate() {
		String writerPath 
			= GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), simpleGeneratorConfigurationDTO.getLogbackConfigPackagePath())
			+ File.separatorChar + simpleGeneratorConfigurationDTO.getLogbackConfigClassName()+".java";
		
		Context context = new Context();
		context.setVariable("simpleGeneratorConfigurationDTO", simpleGeneratorConfigurationDTO);
		
		return generatroService.templateAnalysisGenerateFile(context, writerPath, PathConstant.generatorTemplatePath+"config/", "ThreadDiscriminator");
	}
}
