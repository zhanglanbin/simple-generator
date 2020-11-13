package com.simple.generator.service.generate.impl;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.simple.generator.constant.PathConstant;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.service.generate.GenerateService;
import com.simple.generator.service.generatro.GeneratroService;

@Service
public class ControllerServiceImpl implements GenerateService {
	
	private static final Logger log = LoggerFactory.getLogger(ControllerServiceImpl.class);

	@Autowired
	private GeneratroService generatroService;
	@Resource
	private List<GenerateModelDTO> generateModelDTOList;
	@Resource
	private SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO;
	
	@Override
	public boolean generate() {
		
		for (GenerateModelDTO generateModelDTO : generateModelDTOList) {
			try {
				String writerPath = generateModelDTO.getControllerJavaFilePath() + File.separatorChar + generateModelDTO.getControllerClassName()+".java";
				Context context = new Context();
				context.setVariable("generateModelDTO", generateModelDTO);
				context.setVariable("simpleGeneratorConfigurationDTO", simpleGeneratorConfigurationDTO);
				context.setVariable("modelPrimaryKeyJavaTypeName", generateModelDTO.getModelPrimaryKeyInfo().getJavaType().getSimpleName());
				boolean result = generatroService.templateAnalysisGenerateFile(context, writerPath, PathConstant.generatorTemplatePath+"controller/", "controller");
				if(!result) {
					log.debug("{} :: 生成失败!", context);
				}
			} catch (Exception e) {
				log.error("{}", e);
			}
		}
		
		return true;
	}
}
