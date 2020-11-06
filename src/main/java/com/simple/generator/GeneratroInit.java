package com.simple.generator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.simple.generator.config.SimpleGeneratorConfiguration;
import com.simple.generator.config.GroupProperties;
import com.simple.generator.constant.DateFormatString;
import com.simple.generator.pojo.ControllerAnnotation;
import com.simple.generator.pojo.dto.CommonQueryDTO;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.utils.GeneratorUtils;
import com.simple.generator.utils.MysqlLibStructure;

@Order(2)
@Component
public class GeneratroInit {
	
	
	@Bean
	public List<GenerateModelDTO> generateModelDTOList(SimpleGeneratorConfiguration generatorConfig) {
		MysqlLibStructure mysqlLibStructure = new MysqlLibStructure(generatorConfig.getDatabaseUrl()
				, generatorConfig.getDatabaseUsername()
				, generatorConfig.getDatabasePassword()
				, generatorConfig.getDatabaseName()
				, generatorConfig.getIgnoreTalbePrefixList());
		List<GenerateModelDTO> generateModelDTOList = mysqlLibStructure.getGenerateModelDTOList();
		
		mysqlLibStructure.closeConnection(mysqlLibStructure.getConnection());
		for(int i=0 ; null!=generateModelDTOList&&i<generateModelDTOList.size() ; i++) {
			GenerateModelDTO generateModelDTO = generateModelDTOList.get(i);
			generateModelDTO.setAuthor(generatorConfig.getAuthor());
			generateModelDTO.setIgnoreTalbePrefixList(generatorConfig.getIgnoreTalbePrefixList());
			
			String analysisGroupName = analysisGroupName(generateModelDTO.getModelTableName(), generatorConfig.getGroupPropertiesList());
			analysisGroupName = "."+analysisGroupName;
			generateModelDTO.setModelPackagePath(generatorConfig.getModelPackagePath() + analysisGroupName);
			generateModelDTO.setModelPackage(generateModelDTO.getModelPackagePath() + "." + generateModelDTO.getModelClassName());
			generateModelDTO.setModelJavaFilePath(GeneratorUtils.analysisFilePath(generatorConfig.getProjectPath(), generateModelDTO.getModelPackagePath()));
			
			String modelClassName = generateModelDTO.getModelClassName();
			generateModelDTO.setModelVariableName(firstCharToLowerCase(modelClassName));
			generateModelDTO.setModelListVariableName(firstCharToLowerCase(modelClassName) + generatorConfig.getListVariableNameSuffix());
			
			generateModelDTO.setModelQueryClassName(modelClassName + generatorConfig.getModelQueryClassSuffix());
			generateModelDTO.setModelQueryPackagePath(generateModelDTO.getModelPackagePath() + "." + generatorConfig.getModelQuerySubPackageName());
			generateModelDTO.setModelQueryPackage(generateModelDTO.getModelQueryPackagePath() + "." + generateModelDTO.getModelQueryClassName());
			generateModelDTO.setModelQueryJavaFilePath(GeneratorUtils.analysisFilePath(generatorConfig.getProjectPath(), generateModelDTO.getModelQueryPackagePath()));
			generateModelDTO.setModelQueryVariableName(firstCharToLowerCase(generateModelDTO.getModelQueryClassName()));
			
			generateModelDTO.setModelMDClassName(modelClassName + generatorConfig.getModelMDClassSuffix());
			generateModelDTO.setModelMDPackagePath(generateModelDTO.getModelPackagePath() + "." + generatorConfig.getModelMDSubPackageName());
			generateModelDTO.setModelMDPackage(generateModelDTO.getModelMDPackagePath() + "." + generateModelDTO.getModelMDClassName());
			generateModelDTO.setModelMDJavaFilePath(GeneratorUtils.analysisFilePath(generatorConfig.getProjectPath(), generateModelDTO.getModelMDPackagePath()));
			
			generateModelDTO.setDaoClassName(modelClassName + generatorConfig.getDaoClassSuffix());
			generateModelDTO.setDaoPackagePath(generatorConfig.getDaoPackagePath() + analysisGroupName);
			generateModelDTO.setDaoPackage(generateModelDTO.getDaoPackagePath() + "." + generateModelDTO.getDaoClassName());
			generateModelDTO.setDaoJavaFilePath(GeneratorUtils.analysisFilePath(generatorConfig.getProjectPath(), generateModelDTO.getDaoPackagePath()));
			generateModelDTO.setDaoVariableName(firstCharToLowerCase(generateModelDTO.getDaoClassName()));
			generateModelDTO.setDaoModelPrimaryKeyMethodName(GeneratorUtils.firstCharToUpperCase(generateModelDTO.getModelPrimaryKeyInfo().getJavaName()));
			generateModelDTO.setDaoModelPrimaryKeyListMethodName(generateModelDTO.getDaoModelPrimaryKeyMethodName() + generatorConfig.getListVariableNameSuffix());
			generateModelDTO.setDaoModelPrimaryKeyListParamVariableName(generateModelDTO.getModelPrimaryKeyInfo().getJavaName() + generatorConfig.getListVariableNameSuffix());
			
			generateModelDTO.setMapperXmlName(modelClassName + generatorConfig.getMapperFileNameSuffix());
			generateModelDTO.setMapperXmlFilePath(assembleMapperPath(generateModelDTO.getModelTableName(), generatorConfig));
			
			generateModelDTO.setServiceClassName(modelClassName + generatorConfig.getServiceClassSuffix());
			generateModelDTO.setServicePackagePath(generatorConfig.getServicePackageFilePath() + analysisGroupName);
			generateModelDTO.setServicePackage(generateModelDTO.getServicePackagePath() + "." + generateModelDTO.getServiceClassName());
			generateModelDTO.setServiceJavaFilePath(GeneratorUtils.analysisFilePath(generatorConfig.getProjectPath(), generateModelDTO.getServicePackagePath()));
			generateModelDTO.setServiceVariableName(firstCharToLowerCase(generateModelDTO.getServiceClassName()));
			
			generateModelDTO.setServiceImplClassName(modelClassName + generatorConfig.getServiceImplClassSuffix());
			generateModelDTO.setServiceImplPackagePath(generatorConfig.getServiceImplPackageFilePath().replace(".*", analysisGroupName));
			generateModelDTO.setServiceImplPackage(generateModelDTO.getServiceImplPackagePath() + "." + generateModelDTO.getServiceImplClassName());
			generateModelDTO.setServiceImplJavaFilePath(GeneratorUtils.analysisFilePath(generatorConfig.getProjectPath(), generateModelDTO.getServiceImplPackagePath()));
			generateModelDTO.setServiceImplVariableName(firstCharToLowerCase(generateModelDTO.getServiceImplClassName()));
			
			generateModelDTO.setControllerClassName(modelClassName + generatorConfig.getControllerClassSuffix());
			generateModelDTO.setControllerPackagePath(generatorConfig.getControllerPackageFilePath() + analysisGroupName);
			generateModelDTO.setControllerPackage(generateModelDTO.getControllerPackagePath() + "." + generateModelDTO.getControllerClassName());
			generateModelDTO.setControllerJavaFilePath(GeneratorUtils.analysisFilePath(generatorConfig.getProjectPath(), generateModelDTO.getControllerPackagePath()));
			generateModelDTO.setControllerVariableName(firstCharToLowerCase(generateModelDTO.getControllerClassName()));
			
			generateModelDTO.setControllerAnnotations(analysisControllerAnnotationListToMap(generatorConfig.getControllerAnnotationList()));
			
			generateModelDTO.setSwaggerAnnotation(generatorConfig.isSwaggerAnnotation());
			
			generateModelDTO.setToStringFormatType(generatorConfig.getToStringFormatType());
			
			generateModelDTO.setDateFormat(generatorConfig.isDateFormat());
		}
		return generateModelDTOList;
	}
	
	//simpleGeneratorConfiguration
	@Bean
	public SimpleGeneratorConfiguration generatorConfig(SimpleGeneratorConfiguration simpleGeneratorConfiguration) {
		SimpleGeneratorConfiguration generatorConfig = (SimpleGeneratorConfiguration)simpleGeneratorConfiguration.clone();
		// 解析数据库 库名
		if(GeneratorUtils.isNotEmpty(generatorConfig.getDatabaseUrl()) && generatorConfig.getDatabaseUrl().contains("?")) {
			String[] split = generatorConfig.getDatabaseUrl().split("\\?");
			String connPath = split[0];
			if(GeneratorUtils.isNotEmpty(connPath) && connPath.contains("/")) {
				String[] split2 = connPath.split("\\/");
				String connName = split2[split2.length-1];
				generatorConfig.setDatabaseName(connName);
			}
		}
		
		// 如果未配置项目名, 则使用数据库连接名
		if(GeneratorUtils.isEmpty(generatorConfig.getProjectName())) {
			String projectName = generatorConfig.getDatabaseName().toLowerCase().replace("_", "-");
			generatorConfig.setProjectName(projectName);
		}
		
		//工程路径
		if(GeneratorUtils.isEmpty(generatorConfig.getProjectPath())) {
			String thisProjectPath = GeneratorUtils.getThisProjectPath();
			generatorConfig.setProjectPath(thisProjectPath + File.separatorChar + generatorConfig.getProjectName());
		} else {
			generatorConfig.setProjectPath(generatorConfig.getProjectPath() + File.separatorChar + generatorConfig.getProjectName());
		}
		
		if(GeneratorUtils.isEmpty(generatorConfig.getModelPackagePath())) {
			generatorConfig.setModelPackagePath(generatorConfig.getGroupId() + ".pojo");
		}
		
		if(GeneratorUtils.isEmpty(generatorConfig.getDaoPackagePath())) {
			generatorConfig.setDaoPackagePath(generatorConfig.getGroupId() + ".dao");
		}
		
		if(GeneratorUtils.isEmpty(generatorConfig.getServicePackageFilePath())) {
			generatorConfig.setServicePackageFilePath(generatorConfig.getGroupId() + ".service");
		}
		
		if(GeneratorUtils.isEmpty(generatorConfig.getServiceImplPackageFilePath())) {
			generatorConfig.setServiceImplPackageFilePath(generatorConfig.getGroupId() + ".service.*.impl");
		}
		
		if(GeneratorUtils.isEmpty(generatorConfig.getControllerPackageFilePath())) {
			generatorConfig.setControllerPackageFilePath(generatorConfig.getGroupId() + ".controller");
		}
		
		if(GeneratorUtils.isEmpty(generatorConfig.getUnifiedResponsePackage())) {
			generatorConfig.setUnifiedResponsePackage(generatorConfig.getGroupId() + ".response." + generatorConfig.getUnifiedResponseClassName());
		}
		
		if(GeneratorUtils.isEmpty(generatorConfig.getUnifiedPagePackage())) {
			generatorConfig.setUnifiedPagePackage(generatorConfig.getGroupId() + ".response." + generatorConfig.getUnifiedPageClassName());
		}
		
		if(GeneratorUtils.isEmpty(generatorConfig.getSwaggerConfigPackagePath())) {
			generatorConfig.setSwaggerConfigPackagePath(generatorConfig.getGroupId() + ".config.swagger");
		}
		
		if(GeneratorUtils.isEmpty(generatorConfig.getLogbackConfigPackagePath())) {
			generatorConfig.setLogbackConfigPackagePath(generatorConfig.getGroupId() + ".config.logback");
		}
		
		return generatorConfig;
	}
	
	
	
	
	@Bean
	public CommonQueryDTO commonQueryDTO(SimpleGeneratorConfiguration generatorConfig) {
		CommonQueryDTO commonQueryDTO = new CommonQueryDTO();
		commonQueryDTO.setAuthor(generatorConfig.getAuthor());
		commonQueryDTO.setExplain("公共查询参数");
		commonQueryDTO.setModelClassName("CommonQueryParam");
		commonQueryDTO.setSwaggerAnnotation(generatorConfig.isSwaggerAnnotation());
		commonQueryDTO.setToStringFormatType(generatorConfig.getToStringFormatType());
		commonQueryDTO.setCommonQueryPackagePath(generatorConfig.getModelPackagePath());
		commonQueryDTO.setCommonQuerypackage(commonQueryDTO.getCommonQueryPackagePath() + "." + commonQueryDTO.getModelClassName());
		commonQueryDTO.setCommonQueryJavaFilePath(GeneratorUtils.analysisFilePath(generatorConfig.getProjectPath(), commonQueryDTO.getCommonQueryPackagePath()));
		
		return commonQueryDTO;
	}
	
	
	
	
	/**
	 * 表名
	 * */
	private String analysisGroupName(String tableName, List<GroupProperties> groupPropertiesList) {
		for (GroupProperties groupProperties : groupPropertiesList) {
			List<String> groupTableNameList = groupProperties.getGroupTableName();
			for (String groupTableName : groupTableNameList) {
				if(groupTableName.equals(tableName)) {
					return groupProperties.getGroupName();
				}
			}
		}
		return "";
	}
	
	/**
	 * 首字母转小写
	 */
	public static String firstCharToLowerCase(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	/**
	 * 表名
	 * */
	private String assembleMapperPath(String tableName, SimpleGeneratorConfiguration generatorConfig) {
		String projectPath = generatorConfig.getProjectPath();
		String mapperXmlFilePath = generatorConfig.getMapperXmlFilePath();
		String analysisGroupName = analysisGroupName(tableName, generatorConfig.getGroupPropertiesList());
		
		return projectPath +File.separatorChar+ mapperXmlFilePath +File.separatorChar+ analysisGroupName;
	}
	
	private Map<String, ControllerAnnotation> analysisControllerAnnotationListToMap(List<ControllerAnnotation> controllerAnnotationList) {
		Map<String, ControllerAnnotation> map = new HashMap<>();
		for(int i=0 ; null!=controllerAnnotationList&&i<controllerAnnotationList.size() ; i++) {
			ControllerAnnotation controllerAnnotation = controllerAnnotationList.get(i);
			List<String> method = controllerAnnotation.getMethod();
			List<String> exclude = controllerAnnotation.getExclude();
			
			if(controllerAnnotation.getAnnotation().indexOf("yyyyMMddHHmmss") > -1) {
				String replace = controllerAnnotation.getAnnotation().replace("${yyyyMMddHHmmss}", DateFormatString.yyyyMMddHHmmss);
				controllerAnnotation.setAnnotation(replace);
			} else if(controllerAnnotation.getAnnotation().indexOf("yyyyMMddHHmm") > -1) {
				String replace = controllerAnnotation.getAnnotation().replace("${yyyyMMddHHmm}", DateFormatString.yyyyMMddHHmm);
				controllerAnnotation.setAnnotation(replace);
			} else if(controllerAnnotation.getAnnotation().indexOf("yyyyMMddHH") > -1) {
				String replace = controllerAnnotation.getAnnotation().replace("${yyyyMMddHH}", DateFormatString.yyyyMMddHH);
				controllerAnnotation.setAnnotation(replace);
			} else if(controllerAnnotation.getAnnotation().indexOf("yyyyMMdd") > -1) {
				String replace = controllerAnnotation.getAnnotation().replace("${yyyyMMdd}", DateFormatString.yyyyMMdd);
				controllerAnnotation.setAnnotation(replace);
			}
			
			
			for(int j=0 ; null!=method&&j<method.size() ; j++) {
				map.put(method.get(j), controllerAnnotation);
			}
			
			for(int j=0 ; null!=exclude&&j<exclude.size() ; j++) {
				map.put(exclude.get(j), controllerAnnotation);
			}
		}
		return map;
	}
}
