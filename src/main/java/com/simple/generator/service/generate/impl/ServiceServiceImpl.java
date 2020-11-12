package com.simple.generator.service.generate.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.simple.generator.constant.PathConstant;
import com.simple.generator.constant.Symbol;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.service.generate.GenerateService;
import com.simple.generator.service.generatro.GeneratroService;

@Service
public class ServiceServiceImpl implements GenerateService {
	
	private static final Logger log = LoggerFactory.getLogger(ServiceServiceImpl.class);

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
				String writerPath = generateModelDTO.getServiceJavaFilePath() + File.separatorChar + generateModelDTO.getServiceClassName()+".java";
				Context context = new Context();
				context.setVariable("generateModelDTO", generateModelDTO);
				context.setVariable("notesDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				context.setVariable("importPackage", assImport(generateModelDTO));
				context.setVariable("simpleGeneratorConfigurationDTO", simpleGeneratorConfigurationDTO);
				boolean result = generatroService.templateAnalysisGenerateFile(context, writerPath, PathConstant.generatorTemplatePath+"service/", "service");
				if(!result) {
					log.debug("{} :: 生成失败!", context);
				}
			} catch (Exception e) {
				log.error("{}", e);
			}
		}
		
		return true;
	}
	
	
	protected String assImport(GenerateModelDTO generateModelDTO) {
		String importStr = "import java.util.List;" + Symbol.lineStr
				+ Symbol.lineStr
				+ "import " + simpleGeneratorConfigurationDTO.getUnifiedResponsePackage() + ";" + Symbol.lineStr
				+ "import " + simpleGeneratorConfigurationDTO.getUnifiedPagePackage() + ";" + Symbol.lineStr
				+ Symbol.lineStr
				+ "import " + generateModelDTO.getModelPackage() + ";" + Symbol.lineStr
				+ "import " + generateModelDTO.getModelQueryPackage() + ";";
		return importStr;
	}
}
