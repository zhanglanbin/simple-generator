package com.simple.generator.config;

import java.io.Serializable;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.simple.generator.pojo.ControllerAnnotation;
import com.simple.generator.pojo.dto.GenerateModelDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("生成工程配置")
@Component
@ConfigurationProperties(prefix = "generate-project-config")
public class GeneratorConfig implements Serializable {
	
	private static final long serialVersionUID = -8266356787895928670L;
	
	@ApiModelProperty("数据库名")
	private String databaseName;
	
	@ApiModelProperty("数据库连接地址")
	private String databaseUrl;
	
	@ApiModelProperty("数据库用户名")
	private String databaseUsername;
	
	@ApiModelProperty("数据库密码")
	private String databasePassword;
	
	@ApiModelProperty("是否创建工程, 默认:是")
	private Boolean isCreateProject = true;
	
	@ApiModelProperty("工程路径, 如果为空, 默认为生成工具同级路径")
	private String projectPath;
	
	@ApiModelProperty("工程名称, 不能为空")
	private String projectName;
	
	@ApiModelProperty("作者")
	private String author = "generator";
	
	@ApiModelProperty("组织id")
	private String groupId = "com.h3.generator";
	
	@ApiModelProperty("是否使用swagger注解")
	private boolean swaggerAnnotation = true;
	
	@ApiModelProperty("忽略表前缀")
	private List<String> ignoreTalbePrefixList;
	
	@ApiModelProperty("实体类包路径")
	private String modelPackagePath = groupId + ".pojo";
	
	@ApiModelProperty("db dao 包路径")
	private String daoPackagePath = groupId + ".dao";
	
	@ApiModelProperty("mapper文件路径")
	private String mapperXmlFilePath = "src/main/resources/mybatis/mapper";
	
	@ApiModelProperty("service 包路径")
	private String servicePackageFilePath = groupId + ".service";
	
	@ApiModelProperty("serviceImpl 包路径")
	private String serviceImplPackageFilePath = servicePackageFilePath;
	
	@ApiModelProperty("controller 包路径")
	private String controllerPackageFilePath = groupId + ".controller";
	
	@ApiModelProperty("方法自定义扩展注解, key=方法名称,*表示所有方法, 所有方法有, save*,find*,update*,delete*,也可以使用find*, findById,    value=注解全名称, 例如:org.springframework.stereotype.Controller ")
	private List<ControllerAnnotation> controllerAnnotationList;
	
	@ApiModelProperty("model 配置")
	private List<GenerateModelDTO> generateModelConfig;

	@ApiModelProperty("0=json, 1=eclipse, 2=idea")
	private int toStringFormatType = 1;
	
	@ApiModelProperty("java jdk版本")
	private String javaJdkVersion = "1.8";
	
	@ApiModelProperty("是否转换日期时间格式")
	private boolean dateFormat = true;

	@ApiModelProperty("分组配置")
	private List<GroupProperties> groupPropertiesList;
	
	@ApiModelProperty("List集合变量名后缀")
	private String listVariableNameSuffix = "List";
	
	@ApiModelProperty("model query 后缀")
	private String modelQueryClassSuffix = "Query";
	
	@ApiModelProperty("model query 子包")
	private String modelQuerySubPackageName = "query";
	
	@ApiModelProperty("model MD 后缀")
	private String modelMDClassSuffix = "MD";
	
	@ApiModelProperty("model MD 子包")
	private String modelMDSubPackageName = "md";
	
	@ApiModelProperty("dao class 后缀")
	private String daoClassSuffix = "Dao";
	
	@ApiModelProperty("mapper 文件 后缀")
	private String mapperFileNameSuffix = "Mapper";
	
	@ApiModelProperty("service class 后缀")
	private String serviceClassSuffix = "Service";
	
	@ApiModelProperty("service Impl class 后缀")
	private String serviceImplClassSuffix = "ServiceImpl";
	
	@ApiModelProperty("controller class 后缀")
	private String controllerClassSuffix = "Controller";
	
	@ApiModelProperty("统一返回类名")
	private String unifiedResponseClassName = "ResponseResult";
	
	@ApiModelProperty("统一返回包")
	private String unifiedResponsePackage = groupId + ".response";
	
	@ApiModelProperty("统一分页对象类名")
	private String unifiedPageClassName = "Page";

	@ApiModelProperty("统一分页对象包")
	private String unifiedPagePackage = unifiedResponsePackage;

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public String getDatabaseUsername() {
		return databaseUsername;
	}

	public void setDatabaseUsername(String databaseUsername) {
		this.databaseUsername = databaseUsername;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	public Boolean getIsCreateProject() {
		return isCreateProject;
	}

	public void setIsCreateProject(Boolean isCreateProject) {
		this.isCreateProject = isCreateProject;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public boolean isSwaggerAnnotation() {
		return swaggerAnnotation;
	}

	public void setSwaggerAnnotation(boolean swaggerAnnotation) {
		this.swaggerAnnotation = swaggerAnnotation;
	}

	public List<String> getIgnoreTalbePrefixList() {
		return ignoreTalbePrefixList;
	}

	public void setIgnoreTalbePrefixList(List<String> ignoreTalbePrefixList) {
		this.ignoreTalbePrefixList = ignoreTalbePrefixList;
	}

	public String getModelPackagePath() {
		return modelPackagePath;
	}

	public void setModelPackagePath(String modelPackagePath) {
		this.modelPackagePath = modelPackagePath;
	}

	public String getDaoPackagePath() {
		return daoPackagePath;
	}

	public void setDaoPackagePath(String daoPackagePath) {
		this.daoPackagePath = daoPackagePath;
	}

	public String getMapperXmlFilePath() {
		return mapperXmlFilePath;
	}

	public void setMapperXmlFilePath(String mapperXmlFilePath) {
		this.mapperXmlFilePath = mapperXmlFilePath;
	}

	public String getServicePackageFilePath() {
		return servicePackageFilePath;
	}

	public void setServicePackageFilePath(String servicePackageFilePath) {
		this.servicePackageFilePath = servicePackageFilePath;
	}

	public String getServiceImplPackageFilePath() {
		return serviceImplPackageFilePath;
	}

	public void setServiceImplPackageFilePath(String serviceImplPackageFilePath) {
		this.serviceImplPackageFilePath = serviceImplPackageFilePath;
	}

	public String getControllerPackageFilePath() {
		return controllerPackageFilePath;
	}

	public void setControllerPackageFilePath(String controllerPackageFilePath) {
		this.controllerPackageFilePath = controllerPackageFilePath;
	}

	public List<ControllerAnnotation> getControllerAnnotationList() {
		return controllerAnnotationList;
	}

	public void setControllerAnnotationList(List<ControllerAnnotation> controllerAnnotationList) {
		this.controllerAnnotationList = controllerAnnotationList;
	}

	public List<GenerateModelDTO> getGenerateModelConfig() {
		return generateModelConfig;
	}

	public void setGenerateModelConfig(List<GenerateModelDTO> generateModelConfig) {
		this.generateModelConfig = generateModelConfig;
	}

	public int getToStringFormatType() {
		return toStringFormatType;
	}

	public void setToStringFormatType(int toStringFormatType) {
		this.toStringFormatType = toStringFormatType;
	}

	public String getJavaJdkVersion() {
		return javaJdkVersion;
	}

	public void setJavaJdkVersion(String javaJdkVersion) {
		this.javaJdkVersion = javaJdkVersion;
	}

	public boolean isDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(boolean dateFormat) {
		this.dateFormat = dateFormat;
	}

	public List<GroupProperties> getGroupPropertiesList() {
		return groupPropertiesList;
	}

	public void setGroupPropertiesList(List<GroupProperties> groupPropertiesList) {
		this.groupPropertiesList = groupPropertiesList;
	}

	public String getListVariableNameSuffix() {
		return listVariableNameSuffix;
	}

	public void setListVariableNameSuffix(String listVariableNameSuffix) {
		this.listVariableNameSuffix = listVariableNameSuffix;
	}

	public String getModelQueryClassSuffix() {
		return modelQueryClassSuffix;
	}

	public void setModelQueryClassSuffix(String modelQueryClassSuffix) {
		this.modelQueryClassSuffix = modelQueryClassSuffix;
	}

	public String getModelQuerySubPackageName() {
		return modelQuerySubPackageName;
	}

	public void setModelQuerySubPackageName(String modelQuerySubPackageName) {
		this.modelQuerySubPackageName = modelQuerySubPackageName;
	}

	public String getModelMDClassSuffix() {
		return modelMDClassSuffix;
	}

	public void setModelMDClassSuffix(String modelMDClassSuffix) {
		this.modelMDClassSuffix = modelMDClassSuffix;
	}

	public String getModelMDSubPackageName() {
		return modelMDSubPackageName;
	}

	public void setModelMDSubPackageName(String modelMDSubPackageName) {
		this.modelMDSubPackageName = modelMDSubPackageName;
	}

	public String getDaoClassSuffix() {
		return daoClassSuffix;
	}

	public void setDaoClassSuffix(String daoClassSuffix) {
		this.daoClassSuffix = daoClassSuffix;
	}

	public String getMapperFileNameSuffix() {
		return mapperFileNameSuffix;
	}

	public void setMapperFileNameSuffix(String mapperFileNameSuffix) {
		this.mapperFileNameSuffix = mapperFileNameSuffix;
	}

	public String getServiceClassSuffix() {
		return serviceClassSuffix;
	}

	public void setServiceClassSuffix(String serviceClassSuffix) {
		this.serviceClassSuffix = serviceClassSuffix;
	}

	public String getServiceImplClassSuffix() {
		return serviceImplClassSuffix;
	}

	public void setServiceImplClassSuffix(String serviceImplClassSuffix) {
		this.serviceImplClassSuffix = serviceImplClassSuffix;
	}

	public String getControllerClassSuffix() {
		return controllerClassSuffix;
	}

	public void setControllerClassSuffix(String controllerClassSuffix) {
		this.controllerClassSuffix = controllerClassSuffix;
	}

	public String getUnifiedResponseClassName() {
		return unifiedResponseClassName;
	}

	public void setUnifiedResponseClassName(String unifiedResponseClassName) {
		this.unifiedResponseClassName = unifiedResponseClassName;
	}

	public String getUnifiedResponsePackage() {
		return unifiedResponsePackage;
	}

	public void setUnifiedResponsePackage(String unifiedResponsePackage) {
		this.unifiedResponsePackage = unifiedResponsePackage;
	}

	public String getUnifiedPageClassName() {
		return unifiedPageClassName;
	}

	public void setUnifiedPageClassName(String unifiedPageClassName) {
		this.unifiedPageClassName = unifiedPageClassName;
	}

	public String getUnifiedPagePackage() {
		return unifiedPagePackage;
	}

	public void setUnifiedPagePackage(String unifiedPagePackage) {
		this.unifiedPagePackage = unifiedPagePackage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "GenerateProjectConfig [databaseName=" + databaseName + ", databaseUrl=" + databaseUrl + ", databaseUsername=" + databaseUsername
		        + ", databasePassword=" + databasePassword + ", isCreateProject=" + isCreateProject + ", projectPath=" + projectPath + ", projectName="
		        + projectName + ", author=" + author + ", groupId=" + groupId + ", swaggerAnnotation=" + swaggerAnnotation + ", ignoreTalbePrefixList="
		        + ignoreTalbePrefixList + ", modelPackagePath=" + modelPackagePath + ", daoPackagePath=" + daoPackagePath + ", mapperXmlFilePath="
		        + mapperXmlFilePath + ", servicePackageFilePath=" + servicePackageFilePath + ", serviceImplPackageFilePath=" + serviceImplPackageFilePath
		        + ", controllerPackageFilePath=" + controllerPackageFilePath + ", controllerAnnotationList=" + controllerAnnotationList
		        + ", generateModelConfig=" + generateModelConfig + ", toStringFormatType=" + toStringFormatType + ", javaJdkVersion=" + javaJdkVersion
		        + ", dateFormat=" + dateFormat + ", groupPropertiesList=" + groupPropertiesList + ", listVariableNameSuffix=" + listVariableNameSuffix
		        + ", modelQueryClassSuffix=" + modelQueryClassSuffix + ", modelQuerySubPackageName=" + modelQuerySubPackageName + ", modelMDClassSuffix="
		        + modelMDClassSuffix + ", modelMDSubPackageName=" + modelMDSubPackageName + ", daoClassSuffix=" + daoClassSuffix + ", mapperFileNameSuffix="
		        + mapperFileNameSuffix + ", serviceClassSuffix=" + serviceClassSuffix + ", serviceImplClassSuffix=" + serviceImplClassSuffix
		        + ", controllerClassSuffix=" + controllerClassSuffix + ", unifiedResponseClassName=" + unifiedResponseClassName + ", unifiedResponsePackage="
		        + unifiedResponsePackage + ", unifiedPageClassName=" + unifiedPageClassName + ", unifiedPagePackage=" + unifiedPagePackage + "]";
	}
}