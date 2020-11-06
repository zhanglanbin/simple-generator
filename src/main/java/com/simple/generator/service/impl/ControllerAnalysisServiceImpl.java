package com.simple.generator.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.simple.generator.constant.Symbol;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.service.AssembleAnalysisService;

@Component
public class ControllerAnalysisServiceImpl implements AssembleAnalysisService {
	
	@Resource
	private SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO;
	
	@Override
	public String analysis(String str, GenerateModelDTO generateModelDTO) {
		return str.replace("${generator.controllerPackagePath}", generateModelDTO.getControllerPackagePath())
				.replace("${generator.importPackage}", assImport(generateModelDTO))
				.replace("${generator.controllerClassName}", generateModelDTO.getControllerClassName())
				.replace("${generator.serviceClassName}", generateModelDTO.getServiceClassName())
				.replace("${generator.serviceVariableName}", generateModelDTO.getServiceVariableName())
				.replace("${generator.explain}", generateModelDTO.getExplain())
				.replace("${generator.author}", generateModelDTO.getAuthor())
				.replace("${generator.date}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.replace("${generator.modelVariableName}", generateModelDTO.getModelVariableName())
				.replace("${generator.unifiedResponseClassName}", simpleGeneratorConfigurationDTO.getUnifiedResponseClassName())
				.replace("${generator.modelClassName}", generateModelDTO.getModelClassName())
				.replace("${generator.modelPrimaryKeyMethodName}", generateModelDTO.getDaoModelPrimaryKeyMethodName())
				.replace("${generator.modelPrimaryKeyJavaType}", generateModelDTO.getModelPrimaryKeyInfo().getJavaType().getSimpleName())
				.replace("${generator.modelPrimaryKey}", generateModelDTO.getModelPrimaryKeyInfo().getJavaName())
				.replace("${generator.pagingClassName}", simpleGeneratorConfigurationDTO.getUnifiedPageClassName())
				.replace("${generator.modelQueryClassName}", generateModelDTO.getModelQueryClassName())
				.replace("${generator.modelQueryVariableName}", generateModelDTO.getModelQueryVariableName())
				.replace("${generator.modelListVariableName}", generateModelDTO.getModelListVariableName())
				.replace("${generator.controllerSwaggerApi}", swaggerApi(generateModelDTO));
	}
	
	
	protected String assImport(GenerateModelDTO generateModelDTO) {
		String importStr = Symbol.lineStr + Symbol.lineStr
				+ "import java.util.List;"+ Symbol.lineStr
				+ Symbol.lineStr
				+ "import " + simpleGeneratorConfigurationDTO.getUnifiedResponsePackage() + ";" + Symbol.lineStr
				+ "import " + simpleGeneratorConfigurationDTO.getUnifiedPagePackage() + ";" + Symbol.lineStr
				+ Symbol.lineStr
				+ "import " + generateModelDTO.getModelPackage() + ";" + Symbol.lineStr
				+ "import " + generateModelDTO.getModelQueryPackage() + ";" + Symbol.lineStr
				+ "import " + generateModelDTO.getServicePackage() + ";";
		if(generateModelDTO.isSwaggerAnnotation()) {
			importStr += Symbol.lineStr + Symbol.lineStr
					+ "import io.swagger.annotations.Api;";
			importStr += Symbol.lineStr + Symbol.lineStr
					+ "import io.swagger.annotations.ApiOperation;";
		}
		
		
		return importStr;
	}
	
	protected String swaggerApi(GenerateModelDTO generateModelDTO) {
		//
		if(generateModelDTO.isSwaggerAnnotation()) {
			return "@Api(value = \""+generateModelDTO.getExplain()+"相关接口\", tags = {\""+generateModelDTO.getExplain()+"相关接口\"})";
		} else {
			return "";
		}
	}
}
