package com.simple.generator.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simple.generator.config.GeneratorConfig;
import com.simple.generator.constant.Symbol;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.service.AssembleAnalysisService;
import com.simple.generator.utils.GeneratorUtils;

@Component
public class ServiceImplAnalysisServiceImpl implements AssembleAnalysisService {
	
	@Autowired
	private GeneratorConfig generatorConfig;
	
	@Override
	public String analysis(String str, GenerateModelDTO generateModelDTO) {
		return str.replace("${generator.serviceImplPackagePath}", generateModelDTO.getServiceImplPackagePath())
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
				.replace("${generator.pagingVariableName}", GeneratorUtils.firstCharToUpperCase(generatorConfig.getUnifiedPageClassName()))
				.replace("${generator.modelQueryClassName}", generateModelDTO.getModelQueryClassName())
				.replace("${generator.modelQueryVariableName}", generateModelDTO.getModelQueryVariableName())
				.replace("${generator.serviceImplClassName}", generateModelDTO.getServiceImplClassName())
				.replace("${generator.daoClassName}", generateModelDTO.getDaoClassName())
				.replace("${generator.daoVariableName}", generateModelDTO.getDaoVariableName())
				.replace("${generator.modelListVariableName}", generateModelDTO.getModelListVariableName());
	}
	
	
	protected String assImport(GenerateModelDTO generateModelDTO) {
		String importStr = Symbol.lineStr + Symbol.lineStr
				+ "import java.util.List;"+ Symbol.lineStr
				+ Symbol.lineStr
				+ "import org.slf4j.Logger;"+ Symbol.lineStr
				+ "import org.slf4j.LoggerFactory;"+ Symbol.lineStr
				+ "import org.springframework.beans.factory.annotation.Autowired;"+ Symbol.lineStr
				+ "import org.springframework.stereotype.Service;"+ Symbol.lineStr
				+ "import org.springframework.transaction.annotation.Transactional;"+ Symbol.lineStr
				+ Symbol.lineStr
				+ "import " + generatorConfig.getUnifiedResponsePackage() + ";" + Symbol.lineStr
				+ "import " + generatorConfig.getUnifiedPagePackage() + ";" + Symbol.lineStr
				+ "import " + generatorConfig.getUnifiedPagePackage().replace("Page", "State") + ";" + Symbol.lineStr
				+ Symbol.lineStr
				+ "import " + generateModelDTO.getServicePackage() + ";" + Symbol.lineStr
				+ "import " + generateModelDTO.getModelPackage() + ";" + Symbol.lineStr
				+ "import " + generateModelDTO.getModelQueryPackage() + ";" + Symbol.lineStr
				+ "import " + generateModelDTO.getDaoPackage() + ";" + Symbol.lineStr;
		
		return importStr;
	}
}
