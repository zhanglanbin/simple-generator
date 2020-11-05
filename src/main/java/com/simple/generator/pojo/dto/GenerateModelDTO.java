package com.simple.generator.pojo.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.simple.generator.pojo.ColumnInfo;
import com.simple.generator.pojo.ControllerAnnotation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("生成Model配置")
public class GenerateModelDTO implements Serializable {
	
	private static final long serialVersionUID = -4203078793655652564L;
	
	@ApiModelProperty("是否覆盖")
	private Boolean isCover;
	
	@ApiModelProperty("作者")
	private String author;
	
	@ApiModelProperty("是否使用swagger注解")
	private boolean swaggerAnnotation;
	
	@ApiModelProperty("是否转换日期时间格式")
	private boolean dateFormat;
	
	@ApiModelProperty("是否导入java.util.Date包")
	private boolean importDate;
	
	@ApiModelProperty("忽略表前缀, 多个用逗号隔开, 为空则不忽略")
	private List<String> ignoreTalbePrefixList;
	
	@ApiModelProperty("实体类DTO, 可以多个, key=dto名称, value=多个字段用逗号隔开")
	private Map<String, String> dtoList;
	
	@ApiModelProperty("实体类VO, 可以多个, key=dto名称, value=多个字段用逗号隔开")
	private Map<String, String> voList;
	
	@ApiModelProperty("实体类BO, 可以多个, key=dto名称, value=多个字段用逗号隔开")
	private Map<String, String> boList;
	
	@ApiModelProperty("方法自定义扩展注解, key=方法名称,*表示所有方法, 所有方法有, save*,find*,update*,delete*,也可以使用find*, findById,    value=注解全名称, 例如:org.springframework.stereotype.Controller ")
	private Map<String, ControllerAnnotation> controllerAnnotations;
	
	@ApiModelProperty("实体类java文件路径")
	private String modelJavaFilePath;
	@ApiModelProperty("实体类包路径")
	private String modelPackagePath;
	@ApiModelProperty("实体类包")
	private String modelPackage;
	@ApiModelProperty("实体类 类名")
	private String modelClassName;
	@ApiModelProperty("实体类 变量名")
	private String modelVariableName;
	@ApiModelProperty("实体类 list 变量名")
	private String modelListVariableName;
	@ApiModelProperty("实体类 数据库表名")
	private String modelTableName;
	
	@ApiModelProperty("实体类 md java文件路径")
	private String modelMDJavaFilePath;
	@ApiModelProperty("实体类 md 包路径")
	private String modelMDPackagePath;
	@ApiModelProperty("实体类 md 包")
	private String modelMDPackage;
	@ApiModelProperty("实体类 md 类名")
	private String modelMDClassName;
	
	@ApiModelProperty("实体类query java文件路径")
	private String modelQueryJavaFilePath;
	@ApiModelProperty("实体类query 包路径")
	private String modelQueryPackagePath;
	@ApiModelProperty("实体类query 包")
	private String modelQueryPackage;
	@ApiModelProperty("实体类query 类名")
	private String modelQueryClassName;
	@ApiModelProperty("实体类query 变量名")
	private String modelQueryVariableName;
	
	@ApiModelProperty("db dao java文件路径")
	private String daoJavaFilePath;
	@ApiModelProperty("db dao 包路径")
	private String daoPackagePath;
	@ApiModelProperty("db dao 包")
	private String daoPackage;
	@ApiModelProperty("db dao 类名")
	private String daoClassName;
	@ApiModelProperty("db dao 变量名")
	private String daoVariableName;
	@ApiModelProperty("db dao 主键方法名")
	private String daoModelPrimaryKeyMethodName;
	@ApiModelProperty("db dao 主键List集合方法名")
	private String daoModelPrimaryKeyListMethodName;
	@ApiModelProperty("db dao 主键List集合参数变量名")
	private String daoModelPrimaryKeyListParamVariableName;
	
	
	@ApiModelProperty("mapper文件路径")
	private String mapperXmlFilePath;
	@ApiModelProperty("mapper xml文件名称")
	private String mapperXmlName;
	
	@ApiModelProperty("service java文件路径")
	private String serviceJavaFilePath;
	@ApiModelProperty("service 包路径")
	private String servicePackagePath;
	@ApiModelProperty("service 包")
	private String servicePackage;
	@ApiModelProperty("service 类名")
	private String serviceClassName;
	@ApiModelProperty("service 变量名")
	private String serviceVariableName;
	
	@ApiModelProperty("serviceImpl java文件路径")
	private String serviceImplJavaFilePath;
	@ApiModelProperty("serviceImpl 包路径")
	private String serviceImplPackagePath;
	@ApiModelProperty("serviceImpl 包")
	private String serviceImplPackage;
	@ApiModelProperty("serviceImpl 类名")
	private String serviceImplClassName;
	@ApiModelProperty("serviceImpl 变量名")
	private String serviceImplVariableName;
	
	@ApiModelProperty("controller java文件路径")
	private String controllerJavaFilePath;
	@ApiModelProperty("controller 包路径")
	private String controllerPackagePath;
	@ApiModelProperty("controller 包")
	private String controllerPackage;
	@ApiModelProperty("controller 类名")
	private String controllerClassName;
	@ApiModelProperty("controller 变量名")
	private String controllerVariableName;
	
	@ApiModelProperty("字段信息列表")
	private List<ColumnInfo> columnInfos;
	
	@ApiModelProperty("主键字段信息")
	private ColumnInfo modelPrimaryKeyInfo;
	
	@ApiModelProperty("备注")
	private String explain;

	@ApiModelProperty("是否存在bigDecimal")
	private boolean isImportBigDecimal;
	@ApiModelProperty("是否存在List")
	private boolean isImportList;
	@ApiModelProperty("0=json, 1=eclipse, 2=idea")
	private int toStringFormatType;
	public Boolean getIsCover() {
		return isCover;
	}
	public void setIsCover(Boolean isCover) {
		this.isCover = isCover;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public boolean isSwaggerAnnotation() {
		return swaggerAnnotation;
	}
	public void setSwaggerAnnotation(boolean swaggerAnnotation) {
		this.swaggerAnnotation = swaggerAnnotation;
	}
	public boolean isDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(boolean dateFormat) {
		this.dateFormat = dateFormat;
	}
	public boolean isImportDate() {
		return importDate;
	}
	public void setImportDate(boolean importDate) {
		this.importDate = importDate;
	}
	public List<String> getIgnoreTalbePrefixList() {
		return ignoreTalbePrefixList;
	}
	public void setIgnoreTalbePrefixList(List<String> ignoreTalbePrefixList) {
		this.ignoreTalbePrefixList = ignoreTalbePrefixList;
	}
	public Map<String, String> getDtoList() {
		return dtoList;
	}
	public void setDtoList(Map<String, String> dtoList) {
		this.dtoList = dtoList;
	}
	public Map<String, String> getVoList() {
		return voList;
	}
	public void setVoList(Map<String, String> voList) {
		this.voList = voList;
	}
	public Map<String, String> getBoList() {
		return boList;
	}
	public void setBoList(Map<String, String> boList) {
		this.boList = boList;
	}
	public Map<String, ControllerAnnotation> getControllerAnnotations() {
		return controllerAnnotations;
	}
	public void setControllerAnnotations(Map<String, ControllerAnnotation> controllerAnnotations) {
		this.controllerAnnotations = controllerAnnotations;
	}
	public String getModelJavaFilePath() {
		return modelJavaFilePath;
	}
	public void setModelJavaFilePath(String modelJavaFilePath) {
		this.modelJavaFilePath = modelJavaFilePath;
	}
	public String getModelPackagePath() {
		return modelPackagePath;
	}
	public void setModelPackagePath(String modelPackagePath) {
		this.modelPackagePath = modelPackagePath;
	}
	public String getModelPackage() {
		return modelPackage;
	}
	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}
	public String getModelClassName() {
		return modelClassName;
	}
	public void setModelClassName(String modelClassName) {
		this.modelClassName = modelClassName;
	}
	public String getModelVariableName() {
		return modelVariableName;
	}
	public void setModelVariableName(String modelVariableName) {
		this.modelVariableName = modelVariableName;
	}
	public String getModelListVariableName() {
		return modelListVariableName;
	}
	public void setModelListVariableName(String modelListVariableName) {
		this.modelListVariableName = modelListVariableName;
	}
	public String getModelTableName() {
		return modelTableName;
	}
	public void setModelTableName(String modelTableName) {
		this.modelTableName = modelTableName;
	}
	public String getModelMDJavaFilePath() {
		return modelMDJavaFilePath;
	}
	public void setModelMDJavaFilePath(String modelMDJavaFilePath) {
		this.modelMDJavaFilePath = modelMDJavaFilePath;
	}
	public String getModelMDPackagePath() {
		return modelMDPackagePath;
	}
	public void setModelMDPackagePath(String modelMDPackagePath) {
		this.modelMDPackagePath = modelMDPackagePath;
	}
	public String getModelMDPackage() {
		return modelMDPackage;
	}
	public void setModelMDPackage(String modelMDPackage) {
		this.modelMDPackage = modelMDPackage;
	}
	public String getModelMDClassName() {
		return modelMDClassName;
	}
	public void setModelMDClassName(String modelMDClassName) {
		this.modelMDClassName = modelMDClassName;
	}
	public String getModelQueryJavaFilePath() {
		return modelQueryJavaFilePath;
	}
	public void setModelQueryJavaFilePath(String modelQueryJavaFilePath) {
		this.modelQueryJavaFilePath = modelQueryJavaFilePath;
	}
	public String getModelQueryPackagePath() {
		return modelQueryPackagePath;
	}
	public void setModelQueryPackagePath(String modelQueryPackagePath) {
		this.modelQueryPackagePath = modelQueryPackagePath;
	}
	public String getModelQueryPackage() {
		return modelQueryPackage;
	}
	public void setModelQueryPackage(String modelQueryPackage) {
		this.modelQueryPackage = modelQueryPackage;
	}
	public String getModelQueryClassName() {
		return modelQueryClassName;
	}
	public void setModelQueryClassName(String modelQueryClassName) {
		this.modelQueryClassName = modelQueryClassName;
	}
	public String getModelQueryVariableName() {
		return modelQueryVariableName;
	}
	public void setModelQueryVariableName(String modelQueryVariableName) {
		this.modelQueryVariableName = modelQueryVariableName;
	}
	public String getDaoJavaFilePath() {
		return daoJavaFilePath;
	}
	public void setDaoJavaFilePath(String daoJavaFilePath) {
		this.daoJavaFilePath = daoJavaFilePath;
	}
	public String getDaoPackagePath() {
		return daoPackagePath;
	}
	public void setDaoPackagePath(String daoPackagePath) {
		this.daoPackagePath = daoPackagePath;
	}
	public String getDaoPackage() {
		return daoPackage;
	}
	public void setDaoPackage(String daoPackage) {
		this.daoPackage = daoPackage;
	}
	public String getDaoClassName() {
		return daoClassName;
	}
	public void setDaoClassName(String daoClassName) {
		this.daoClassName = daoClassName;
	}
	public String getDaoVariableName() {
		return daoVariableName;
	}
	public void setDaoVariableName(String daoVariableName) {
		this.daoVariableName = daoVariableName;
	}
	public String getDaoModelPrimaryKeyMethodName() {
		return daoModelPrimaryKeyMethodName;
	}
	public void setDaoModelPrimaryKeyMethodName(String daoModelPrimaryKeyMethodName) {
		this.daoModelPrimaryKeyMethodName = daoModelPrimaryKeyMethodName;
	}
	public String getDaoModelPrimaryKeyListMethodName() {
		return daoModelPrimaryKeyListMethodName;
	}
	public void setDaoModelPrimaryKeyListMethodName(String daoModelPrimaryKeyListMethodName) {
		this.daoModelPrimaryKeyListMethodName = daoModelPrimaryKeyListMethodName;
	}
	public String getDaoModelPrimaryKeyListParamVariableName() {
		return daoModelPrimaryKeyListParamVariableName;
	}
	public void setDaoModelPrimaryKeyListParamVariableName(String daoModelPrimaryKeyListParamVariableName) {
		this.daoModelPrimaryKeyListParamVariableName = daoModelPrimaryKeyListParamVariableName;
	}
	public String getMapperXmlFilePath() {
		return mapperXmlFilePath;
	}
	public void setMapperXmlFilePath(String mapperXmlFilePath) {
		this.mapperXmlFilePath = mapperXmlFilePath;
	}
	public String getMapperXmlName() {
		return mapperXmlName;
	}
	public void setMapperXmlName(String mapperXmlName) {
		this.mapperXmlName = mapperXmlName;
	}
	public String getServiceJavaFilePath() {
		return serviceJavaFilePath;
	}
	public void setServiceJavaFilePath(String serviceJavaFilePath) {
		this.serviceJavaFilePath = serviceJavaFilePath;
	}
	public String getServicePackagePath() {
		return servicePackagePath;
	}
	public void setServicePackagePath(String servicePackagePath) {
		this.servicePackagePath = servicePackagePath;
	}
	public String getServicePackage() {
		return servicePackage;
	}
	public void setServicePackage(String servicePackage) {
		this.servicePackage = servicePackage;
	}
	public String getServiceClassName() {
		return serviceClassName;
	}
	public void setServiceClassName(String serviceClassName) {
		this.serviceClassName = serviceClassName;
	}
	public String getServiceVariableName() {
		return serviceVariableName;
	}
	public void setServiceVariableName(String serviceVariableName) {
		this.serviceVariableName = serviceVariableName;
	}
	public String getServiceImplJavaFilePath() {
		return serviceImplJavaFilePath;
	}
	public void setServiceImplJavaFilePath(String serviceImplJavaFilePath) {
		this.serviceImplJavaFilePath = serviceImplJavaFilePath;
	}
	public String getServiceImplPackagePath() {
		return serviceImplPackagePath;
	}
	public void setServiceImplPackagePath(String serviceImplPackagePath) {
		this.serviceImplPackagePath = serviceImplPackagePath;
	}
	public String getServiceImplPackage() {
		return serviceImplPackage;
	}
	public void setServiceImplPackage(String serviceImplPackage) {
		this.serviceImplPackage = serviceImplPackage;
	}
	public String getServiceImplClassName() {
		return serviceImplClassName;
	}
	public void setServiceImplClassName(String serviceImplClassName) {
		this.serviceImplClassName = serviceImplClassName;
	}
	public String getServiceImplVariableName() {
		return serviceImplVariableName;
	}
	public void setServiceImplVariableName(String serviceImplVariableName) {
		this.serviceImplVariableName = serviceImplVariableName;
	}
	public String getControllerJavaFilePath() {
		return controllerJavaFilePath;
	}
	public void setControllerJavaFilePath(String controllerJavaFilePath) {
		this.controllerJavaFilePath = controllerJavaFilePath;
	}
	public String getControllerPackagePath() {
		return controllerPackagePath;
	}
	public void setControllerPackagePath(String controllerPackagePath) {
		this.controllerPackagePath = controllerPackagePath;
	}
	public String getControllerPackage() {
		return controllerPackage;
	}
	public void setControllerPackage(String controllerPackage) {
		this.controllerPackage = controllerPackage;
	}
	public String getControllerClassName() {
		return controllerClassName;
	}
	public void setControllerClassName(String controllerClassName) {
		this.controllerClassName = controllerClassName;
	}
	public String getControllerVariableName() {
		return controllerVariableName;
	}
	public void setControllerVariableName(String controllerVariableName) {
		this.controllerVariableName = controllerVariableName;
	}
	public List<ColumnInfo> getColumnInfos() {
		return columnInfos;
	}
	public void setColumnInfos(List<ColumnInfo> columnInfos) {
		this.columnInfos = columnInfos;
	}
	public ColumnInfo getModelPrimaryKeyInfo() {
		return modelPrimaryKeyInfo;
	}
	public void setModelPrimaryKeyInfo(ColumnInfo modelPrimaryKeyInfo) {
		this.modelPrimaryKeyInfo = modelPrimaryKeyInfo;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public boolean isImportBigDecimal() {
		return isImportBigDecimal;
	}
	public void setImportBigDecimal(boolean isImportBigDecimal) {
		this.isImportBigDecimal = isImportBigDecimal;
	}
	public boolean isImportList() {
		return isImportList;
	}
	public void setImportList(boolean isImportList) {
		this.isImportList = isImportList;
	}
	public int getToStringFormatType() {
		return toStringFormatType;
	}
	public void setToStringFormatType(int toStringFormatType) {
		this.toStringFormatType = toStringFormatType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "GenerateModelDTO [isCover=" + isCover + ", author=" + author + ", swaggerAnnotation=" + swaggerAnnotation + ", dateFormat=" + dateFormat
		        + ", importDate=" + importDate + ", ignoreTalbePrefixList=" + ignoreTalbePrefixList + ", dtoList=" + dtoList + ", voList=" + voList
		        + ", boList=" + boList + ", controllerAnnotations=" + controllerAnnotations + ", modelJavaFilePath=" + modelJavaFilePath + ", modelPackagePath="
		        + modelPackagePath + ", modelPackage=" + modelPackage + ", modelClassName=" + modelClassName + ", modelVariableName=" + modelVariableName
		        + ", modelListVariableName=" + modelListVariableName + ", modelTableName=" + modelTableName + ", modelMDJavaFilePath=" + modelMDJavaFilePath
		        + ", modelMDPackagePath=" + modelMDPackagePath + ", modelMDPackage=" + modelMDPackage + ", modelMDClassName=" + modelMDClassName
		        + ", modelQueryJavaFilePath=" + modelQueryJavaFilePath + ", modelQueryPackagePath=" + modelQueryPackagePath + ", modelQueryPackage="
		        + modelQueryPackage + ", modelQueryClassName=" + modelQueryClassName + ", modelQueryVariableName=" + modelQueryVariableName
		        + ", daoJavaFilePath=" + daoJavaFilePath + ", daoPackagePath=" + daoPackagePath + ", daoPackage=" + daoPackage + ", daoClassName="
		        + daoClassName + ", daoVariableName=" + daoVariableName + ", daoModelPrimaryKeyMethodName=" + daoModelPrimaryKeyMethodName
		        + ", daoModelPrimaryKeyListMethodName=" + daoModelPrimaryKeyListMethodName + ", daoModelPrimaryKeyListParamVariableName="
		        + daoModelPrimaryKeyListParamVariableName + ", mapperXmlFilePath=" + mapperXmlFilePath + ", mapperXmlName=" + mapperXmlName
		        + ", serviceJavaFilePath=" + serviceJavaFilePath + ", servicePackagePath=" + servicePackagePath + ", servicePackage=" + servicePackage
		        + ", serviceClassName=" + serviceClassName + ", serviceVariableName=" + serviceVariableName + ", serviceImplJavaFilePath="
		        + serviceImplJavaFilePath + ", serviceImplPackagePath=" + serviceImplPackagePath + ", serviceImplPackage=" + serviceImplPackage
		        + ", serviceImplClassName=" + serviceImplClassName + ", serviceImplVariableName=" + serviceImplVariableName + ", controllerJavaFilePath="
		        + controllerJavaFilePath + ", controllerPackagePath=" + controllerPackagePath + ", controllerPackage=" + controllerPackage
		        + ", controllerClassName=" + controllerClassName + ", controllerVariableName=" + controllerVariableName + ", columnInfos=" + columnInfos
		        + ", modelPrimaryKeyInfo=" + modelPrimaryKeyInfo + ", explain=" + explain + ", isImportBigDecimal=" + isImportBigDecimal + ", isImportList="
		        + isImportList + ", toStringFormatType=" + toStringFormatType + "]";
	}
}
