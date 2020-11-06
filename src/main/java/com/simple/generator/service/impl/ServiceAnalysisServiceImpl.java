package com.simple.generator.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.simple.generator.config.SimpleGeneratorConfiguration;
import com.simple.generator.constant.Symbol;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.service.AssembleAnalysisService;

@Component
public class ServiceAnalysisServiceImpl implements AssembleAnalysisService {
	
	@Resource
	private SimpleGeneratorConfiguration generatorConfig;
	
	@Override
	public String analysis(String str, GenerateModelDTO generateModelDTO) {
		return str.replace("${generator.servicePackagePath}", generateModelDTO.getServicePackagePath())
				.replace("${generator.importPackage}", assImport(generateModelDTO))
				.replace("${generator.serviceClassName}", generateModelDTO.getServiceClassName())
				.replace("${generator.explain}", generateModelDTO.getExplain())
				.replace("${generator.author}", generateModelDTO.getAuthor())
				.replace("${generator.date}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.replace("${generator.modelVariableName}", generateModelDTO.getModelVariableName())
				.replace("${generator.unifiedResponseClassName}", generatorConfig.getUnifiedResponseClassName())
				.replace("${generator.modelClassName}", generateModelDTO.getModelClassName())
				.replace("${generator.modelPrimaryKeyMethodName}", generateModelDTO.getDaoModelPrimaryKeyMethodName())
				.replace("${generator.modelPrimaryKeyJavaType}", generateModelDTO.getModelPrimaryKeyInfo().getJavaType().getSimpleName())
				.replace("${generator.modelPrimaryKey}", generateModelDTO.getModelPrimaryKeyInfo().getJavaName())
				.replace("${generator.pagingClassName}", generatorConfig.getUnifiedPageClassName())
				.replace("${generator.modelQueryClassName}", generateModelDTO.getModelQueryClassName())
				.replace("${generator.modelQueryVariableName}", generateModelDTO.getModelQueryVariableName());
	}
	
	
	protected String assImport(GenerateModelDTO generateModelDTO) {
		String importStr = Symbol.lineStr + Symbol.lineStr
				+ "import java.util.List;" + Symbol.lineStr
				+ Symbol.lineStr
				+ "import " + generatorConfig.getUnifiedResponsePackage() + ";" + Symbol.lineStr
				+ "import " + generatorConfig.getUnifiedPagePackage() + ";" + Symbol.lineStr
				+ Symbol.lineStr
				+ "import " + generateModelDTO.getModelPackage() + ";" + Symbol.lineStr
				+ "import " + generateModelDTO.getModelQueryPackage() + ";" + Symbol.lineStr;
		
		return importStr;
	}
}
