package com.simple.generator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.simple.generator.config.SimpleGeneratorConfiguration;
import com.simple.generator.config.GroupProperties;
import com.simple.generator.constant.DateFormatString;
import com.simple.generator.pojo.ControllerAnnotation;
import com.simple.generator.pojo.dto.CommonQueryDTO;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.utils.GeneratorUtils;
import com.simple.generator.utils.MysqlLibStructure;

@Order(2)
@Component
public class GeneratroInit {
	
	
	@Bean
	public List<GenerateModelDTO> generateModelDTOList(SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO) {
		MysqlLibStructure mysqlLibStructure = new MysqlLibStructure(simpleGeneratorConfigurationDTO.getDatabaseUrl()
				, simpleGeneratorConfigurationDTO.getDatabaseUsername()
				, simpleGeneratorConfigurationDTO.getDatabasePassword()
				, simpleGeneratorConfigurationDTO.getDatabaseName()
				, simpleGeneratorConfigurationDTO.getIgnoreTalbePrefixList());
		List<GenerateModelDTO> generateModelDTOList = mysqlLibStructure.getGenerateModelDTOList();
		
		mysqlLibStructure.closeConnection(mysqlLibStructure.getConnection());
		for(int i=0 ; null!=generateModelDTOList&&i<generateModelDTOList.size() ; i++) {
			GenerateModelDTO generateModelDTO = generateModelDTOList.get(i);
			generateModelDTO.setAuthor(simpleGeneratorConfigurationDTO.getAuthor());
			generateModelDTO.setIgnoreTalbePrefixList(simpleGeneratorConfigurationDTO.getIgnoreTalbePrefixList());
			
			String analysisGroupName = analysisGroupName(generateModelDTO.getModelTableName(), simpleGeneratorConfigurationDTO.getGroupPropertiesList());
			analysisGroupName = "."+analysisGroupName;
			generateModelDTO.setModelPackagePath(simpleGeneratorConfigurationDTO.getModelPackagePath() + analysisGroupName);
			generateModelDTO.setModelPackage(generateModelDTO.getModelPackagePath() + "." + generateModelDTO.getModelClassName());
			generateModelDTO.setModelJavaFilePath(GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), generateModelDTO.getModelPackagePath()));
			
			String modelClassName = generateModelDTO.getModelClassName();
			generateModelDTO.setModelVariableName(firstCharToLowerCase(modelClassName));
			generateModelDTO.setModelListVariableName(firstCharToLowerCase(modelClassName) + simpleGeneratorConfigurationDTO.getListVariableNameSuffix());
			
			generateModelDTO.setModelQueryClassName(modelClassName + simpleGeneratorConfigurationDTO.getModelQueryClassSuffix());
			generateModelDTO.setModelQueryPackagePath(generateModelDTO.getModelPackagePath() + "." + simpleGeneratorConfigurationDTO.getModelQuerySubPackageName());
			generateModelDTO.setModelQueryPackage(generateModelDTO.getModelQueryPackagePath() + "." + generateModelDTO.getModelQueryClassName());
			generateModelDTO.setModelQueryJavaFilePath(GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), generateModelDTO.getModelQueryPackagePath()));
			generateModelDTO.setModelQueryVariableName(firstCharToLowerCase(generateModelDTO.getModelQueryClassName()));
			
			generateModelDTO.setModelMDClassName(modelClassName + simpleGeneratorConfigurationDTO.getModelMDClassSuffix());
			generateModelDTO.setModelMDPackagePath(generateModelDTO.getModelPackagePath() + "." + simpleGeneratorConfigurationDTO.getModelMDSubPackageName());
			generateModelDTO.setModelMDPackage(generateModelDTO.getModelMDPackagePath() + "." + generateModelDTO.getModelMDClassName());
			generateModelDTO.setModelMDJavaFilePath(GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), generateModelDTO.getModelMDPackagePath()));
			
			generateModelDTO.setDaoClassName(modelClassName + simpleGeneratorConfigurationDTO.getDaoClassSuffix());
			generateModelDTO.setDaoPackagePath(simpleGeneratorConfigurationDTO.getDaoPackagePath() + analysisGroupName);
			generateModelDTO.setDaoPackage(generateModelDTO.getDaoPackagePath() + "." + generateModelDTO.getDaoClassName());
			generateModelDTO.setDaoJavaFilePath(GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), generateModelDTO.getDaoPackagePath()));
			generateModelDTO.setDaoVariableName(firstCharToLowerCase(generateModelDTO.getDaoClassName()));
			generateModelDTO.setDaoModelPrimaryKeyMethodName(GeneratorUtils.firstCharToUpperCase(generateModelDTO.getModelPrimaryKeyInfo().getJavaName()));
			generateModelDTO.setDaoModelPrimaryKeyListMethodName(generateModelDTO.getDaoModelPrimaryKeyMethodName() + simpleGeneratorConfigurationDTO.getListVariableNameSuffix());
			generateModelDTO.setDaoModelPrimaryKeyListParamVariableName(generateModelDTO.getModelPrimaryKeyInfo().getJavaName() + simpleGeneratorConfigurationDTO.getListVariableNameSuffix());
			
			generateModelDTO.setMapperXmlName(modelClassName + simpleGeneratorConfigurationDTO.getMapperFileNameSuffix());
			generateModelDTO.setMapperXmlFilePath(assembleMapperPath(generateModelDTO.getModelTableName(), simpleGeneratorConfigurationDTO));
			
			generateModelDTO.setServiceClassName(modelClassName + simpleGeneratorConfigurationDTO.getServiceClassSuffix());
			generateModelDTO.setServicePackagePath(simpleGeneratorConfigurationDTO.getServicePackageFilePath() + analysisGroupName);
			generateModelDTO.setServicePackage(generateModelDTO.getServicePackagePath() + "." + generateModelDTO.getServiceClassName());
			generateModelDTO.setServiceJavaFilePath(GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), generateModelDTO.getServicePackagePath()));
			generateModelDTO.setServiceVariableName(firstCharToLowerCase(generateModelDTO.getServiceClassName()));
			
			generateModelDTO.setServiceImplClassName(modelClassName + simpleGeneratorConfigurationDTO.getServiceImplClassSuffix());
			generateModelDTO.setServiceImplPackagePath(simpleGeneratorConfigurationDTO.getServiceImplPackageFilePath().replace(".*", analysisGroupName));
			generateModelDTO.setServiceImplPackage(generateModelDTO.getServiceImplPackagePath() + "." + generateModelDTO.getServiceImplClassName());
			generateModelDTO.setServiceImplJavaFilePath(GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), generateModelDTO.getServiceImplPackagePath()));
			generateModelDTO.setServiceImplVariableName(firstCharToLowerCase(generateModelDTO.getServiceImplClassName()));
			
			generateModelDTO.setControllerClassName(modelClassName + simpleGeneratorConfigurationDTO.getControllerClassSuffix());
			generateModelDTO.setControllerPackagePath(simpleGeneratorConfigurationDTO.getControllerPackageFilePath() + analysisGroupName);
			generateModelDTO.setControllerPackage(generateModelDTO.getControllerPackagePath() + "." + generateModelDTO.getControllerClassName());
			generateModelDTO.setControllerJavaFilePath(GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), generateModelDTO.getControllerPackagePath()));
			generateModelDTO.setControllerVariableName(firstCharToLowerCase(generateModelDTO.getControllerClassName()));
			
			generateModelDTO.setControllerAnnotations(analysisControllerAnnotationListToMap(simpleGeneratorConfigurationDTO.getControllerAnnotationList()));
			
			generateModelDTO.setSwaggerAnnotation(simpleGeneratorConfigurationDTO.isSwaggerAnnotation());
			
			generateModelDTO.setToStringFormatType(simpleGeneratorConfigurationDTO.getToStringFormatType());
			
			generateModelDTO.setDateFormat(simpleGeneratorConfigurationDTO.isDateFormat());
		}
		return generateModelDTOList;
	}
	
	@Bean
	public SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO(SimpleGeneratorConfiguration simpleGeneratorConfiguration) {
		SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO = new SimpleGeneratorConfigurationDTO();
		BeanUtils.copyProperties(simpleGeneratorConfiguration,simpleGeneratorConfigurationDTO);
		// 解析数据库 库名
		if(GeneratorUtils.isNotEmpty(simpleGeneratorConfigurationDTO.getDatabaseUrl()) && simpleGeneratorConfigurationDTO.getDatabaseUrl().contains("?")) {
			String[] split = simpleGeneratorConfigurationDTO.getDatabaseUrl().split("\\?");
			String connPath = split[0];
			if(GeneratorUtils.isNotEmpty(connPath) && connPath.contains("/")) {
				String[] split2 = connPath.split("\\/");
				String connName = split2[split2.length-1];
				simpleGeneratorConfigurationDTO.setDatabaseName(connName);
			}
		}
		
		// 如果未配置项目名, 则使用数据库连接名
		if(GeneratorUtils.isEmpty(simpleGeneratorConfigurationDTO.getProjectName())) {
			String projectName = simpleGeneratorConfigurationDTO.getDatabaseName().toLowerCase().replace("_", "-");
			simpleGeneratorConfigurationDTO.setProjectName(projectName);
		}
		
		//工程路径
		if(GeneratorUtils.isEmpty(simpleGeneratorConfigurationDTO.getProjectPath())) {
			String thisProjectPath = GeneratorUtils.getThisProjectPath();
			simpleGeneratorConfigurationDTO.setProjectPath(thisProjectPath + File.separatorChar + simpleGeneratorConfigurationDTO.getProjectName());
		} else {
			simpleGeneratorConfigurationDTO.setProjectPath(simpleGeneratorConfigurationDTO.getProjectPath() + File.separatorChar + simpleGeneratorConfigurationDTO.getProjectName());
		}
		
		if(GeneratorUtils.isEmpty(simpleGeneratorConfigurationDTO.getModelPackagePath())) {
			simpleGeneratorConfigurationDTO.setModelPackagePath(simpleGeneratorConfigurationDTO.getGroupId() + ".pojo");
		}
		
		if(GeneratorUtils.isEmpty(simpleGeneratorConfigurationDTO.getDaoPackagePath())) {
			simpleGeneratorConfigurationDTO.setDaoPackagePath(simpleGeneratorConfigurationDTO.getGroupId() + ".dao");
		}
		
		if(GeneratorUtils.isEmpty(simpleGeneratorConfigurationDTO.getServicePackageFilePath())) {
			simpleGeneratorConfigurationDTO.setServicePackageFilePath(simpleGeneratorConfigurationDTO.getGroupId() + ".service");
		}
		
		if(GeneratorUtils.isEmpty(simpleGeneratorConfigurationDTO.getServiceImplPackageFilePath())) {
			simpleGeneratorConfigurationDTO.setServiceImplPackageFilePath(simpleGeneratorConfigurationDTO.getGroupId() + ".service.*.impl");
		}
		
		if(GeneratorUtils.isEmpty(simpleGeneratorConfigurationDTO.getControllerPackageFilePath())) {
			simpleGeneratorConfigurationDTO.setControllerPackageFilePath(simpleGeneratorConfigurationDTO.getGroupId() + ".controller");
		}
		
		if(GeneratorUtils.isEmpty(simpleGeneratorConfigurationDTO.getUnifiedResponsePackage())) {
			simpleGeneratorConfigurationDTO.setUnifiedResponsePackage(simpleGeneratorConfigurationDTO.getGroupId() + ".response." + simpleGeneratorConfigurationDTO.getUnifiedResponseClassName());
		}
		
		if(GeneratorUtils.isEmpty(simpleGeneratorConfigurationDTO.getUnifiedPagePackage())) {
			simpleGeneratorConfigurationDTO.setUnifiedPagePackage(simpleGeneratorConfigurationDTO.getGroupId() + ".response." + simpleGeneratorConfigurationDTO.getUnifiedPageClassName());
		}
		
		if(GeneratorUtils.isEmpty(simpleGeneratorConfigurationDTO.getSwaggerConfigPackagePath())) {
			simpleGeneratorConfigurationDTO.setSwaggerConfigPackagePath(simpleGeneratorConfigurationDTO.getGroupId() + ".config.swagger");
		}
		
		if(GeneratorUtils.isEmpty(simpleGeneratorConfigurationDTO.getLogbackConfigPackagePath())) {
			simpleGeneratorConfigurationDTO.setLogbackConfigPackagePath(simpleGeneratorConfigurationDTO.getGroupId() + ".config.logback");
		}
		
		return simpleGeneratorConfigurationDTO;
	}
	
	
	
	
	@Bean
	public CommonQueryDTO commonQueryDTO(SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO) {
		CommonQueryDTO commonQueryDTO = new CommonQueryDTO();
		commonQueryDTO.setAuthor(simpleGeneratorConfigurationDTO.getAuthor());
		commonQueryDTO.setExplain("公共查询参数");
		commonQueryDTO.setModelClassName("CommonQueryParam");
		commonQueryDTO.setSwaggerAnnotation(simpleGeneratorConfigurationDTO.isSwaggerAnnotation());
		commonQueryDTO.setToStringFormatType(simpleGeneratorConfigurationDTO.getToStringFormatType());
		commonQueryDTO.setCommonQueryPackagePath(simpleGeneratorConfigurationDTO.getModelPackagePath());
		commonQueryDTO.setCommonQuerypackage(commonQueryDTO.getCommonQueryPackagePath() + "." + commonQueryDTO.getModelClassName());
		commonQueryDTO.setCommonQueryJavaFilePath(GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), commonQueryDTO.getCommonQueryPackagePath()));
		
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
	private String assembleMapperPath(String tableName, SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO) {
		String projectPath = simpleGeneratorConfigurationDTO.getProjectPath();
		String mapperXmlFilePath = simpleGeneratorConfigurationDTO.getMapperXmlFilePath();
		String analysisGroupName = analysisGroupName(tableName, simpleGeneratorConfigurationDTO.getGroupPropertiesList());
		
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
