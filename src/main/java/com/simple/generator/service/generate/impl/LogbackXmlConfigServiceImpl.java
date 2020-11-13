package com.simple.generator.service.generate.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.simple.generator.constant.PathConstant;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.service.generate.GenerateService;
import com.simple.generator.service.generatro.GeneratroService;

@Service
public class LogbackXmlConfigServiceImpl implements GenerateService {
	
	@Autowired
	private GeneratroService generatroService;
	@Autowired
	private SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO;
	
	@Override
	public boolean generate() {
		String writerPath = simpleGeneratorConfigurationDTO.getProjectPath() + File.separatorChar + PathConstant.mainResourcesPath + File.separatorChar 
				+ simpleGeneratorConfigurationDTO.getLogbackXmlName()+".xml";
		
		Context context = new Context();
		context.setVariable("simpleGeneratorConfigurationDTO", simpleGeneratorConfigurationDTO);
		
		return generatroService.templateAnalysisGenerateFile(context, writerPath, "logbackSpringXml");
	}
}
