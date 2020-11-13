package com.simple.generator.service.generate.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.simple.generator.constant.PathConstant;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.service.generate.GenerateService;
import com.simple.generator.service.generatro.GeneratroService;
import com.simple.generator.utils.GeneratorUtils;

@Service
public class CommonQueryModelServiceImpl implements GenerateService {

	@Autowired
	private GeneratroService generatroService;
	@Autowired
	private SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO;
	
	@Override
	public boolean generate() {
		String writerPath = GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), simpleGeneratorConfigurationDTO.getModelPackagePath())
				+ File.separatorChar + "CommonQueryParam.java";
		
		Context context = new Context();
		context.setVariable("simpleGeneratorConfigurationDTO", simpleGeneratorConfigurationDTO);
		context.setVariable("serialVersionUID", GeneratorUtils.getSerialVersionUID().toString());
		context.setVariable("notesDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
		return generatroService.templateAnalysisGenerateFile(context, writerPath, PathConstant.generatorTemplatePath+"model/", "CommonQueryParam");
	}
}
