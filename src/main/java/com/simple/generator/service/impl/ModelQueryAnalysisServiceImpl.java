package com.simple.generator.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simple.generator.constant.Symbol;
import com.simple.generator.pojo.ColumnInfo;
import com.simple.generator.pojo.dto.CommonQueryDTO;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.service.AssembleAnalysisService;
import com.simple.generator.utils.GeneratorUtils;

@Component
public class ModelQueryAnalysisServiceImpl implements AssembleAnalysisService {
	
	@Autowired
	private CommonQueryDTO commonQueryDTO;
	
	@Override
	public String analysis(String str, GenerateModelDTO generateModelDTO) {
		return str.replace("${generator.modelQueryPackagePath}", generateModelDTO.getModelQueryPackagePath())
				.replace("${generator.importPackage}", assImport(generateModelDTO))
				.replace("${generator.modelQueryClassName}", generateModelDTO.getModelQueryClassName())
				.replace("${generator.explain}", generateModelDTO.getExplain())
				.replace("${generator.author}", generateModelDTO.getAuthor())
				.replace("${generator.date}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.replace("${generator.swaggerApiModelAnnotation}", getSwaggerApiModelAnnotation(generateModelDTO))
				.replace("${generator.commonQueryClassName}", commonQueryDTO.getModelClassName())
				.replace("${generator.serialVersionUID}", GeneratorUtils.getSerialVersionUID().toString())
				.replace("${generator.modelAttribute}", assembleAttribute(generateModelDTO))
				.replace("${generator.getAndSetMethod}", assembleGetAndSetMethod(generateModelDTO))
				.replace("${generator.toString}", GeneratorUtils.asModelToString(generateModelDTO, true))
				.replace("${generator.querySynamicColumnMethod}", getModelQuerySynamicColumnMethod(generateModelDTO));
	}
	
	/**
	 * 组装导包
	 * */
	protected String assImport(GenerateModelDTO generateModelDTO) {
		StringBuffer importBuffer = new StringBuffer();
		
		//序列化包
		importBuffer.append(Symbol.lineStr).append("import java.io.Serializable;").append(Symbol.lineStr);
		
		//导入big包
		if(generateModelDTO.isImportBigDecimal()) {
			importBuffer.append(Symbol.lineStr)
			//时间日期转换注解
			.append("import ").append(BigDecimal.class.getName()).append(";").append(Symbol.lineStr);
		}
		
		//导入时间日期包
		if(generateModelDTO.isImportDate()) {
			importBuffer.append(Symbol.lineStr)
			.append("import ").append(Date.class.getName()).append(";").append(Symbol.lineStr);
		}
		
		// 导入日期格式化包
		if(generateModelDTO.isDateFormat()) {
			//时间日期转换注解
			importBuffer.append(Symbol.lineStr)
			.append("import org.springframework.format.annotation.DateTimeFormat;").append(Symbol.lineStr);
		}
		
		
		
		//是否使用json格式的toString
		if(generateModelDTO.getToStringFormatType()==0) {
			importBuffer.append(Symbol.lineStr)
			.append("import com.alibaba.fastjson.JSON;").append(Symbol.lineStr)
			.append("import com.alibaba.fastjson.serializer.SerializerFeature;").append(Symbol.lineStr);
		}
		
		//增加swagger包
		if(generateModelDTO.isSwaggerAnnotation()) {
			importBuffer.append(Symbol.lineStr)
			.append("import io.swagger.annotations.ApiModel;").append(Symbol.lineStr)
			.append("import io.swagger.annotations.ApiModelProperty;").append(Symbol.lineStr);
		}
		
		importBuffer.append(Symbol.lineStr)
		.append("import ").append(commonQueryDTO.getCommonQuerypackage()).append(";");
		
		return importBuffer.toString();
	}
	
	protected String getSwaggerApiModelAnnotation(GenerateModelDTO generateModelDTO) {
		if(generateModelDTO.isSwaggerAnnotation()) {
			return "@ApiModel(value = \"" + generateModelDTO.getExplain() + "查询\", description = \"" + generateModelDTO.getExplain() + "查询\")";
		} else {
			return "";
		}
	}
	
	/**
	 * 组装model属性
	 * */
	protected String assembleAttribute(GenerateModelDTO generateModelDTO) {
		StringBuffer attributeBuffer = new StringBuffer();
		for (ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String nowTimeStr = simpleDateFormat.format(new Date());
			//获取属性注释
			String attributeNotes = GeneratorUtils.getModelAttrNotes(columnInfo.getExplain());
			//组装注释
			attributeBuffer
			.append(Symbol.lineStr)
			.append(attributeNotes);
			
			String type = columnInfo.getJavaType().getSimpleName();
			String name = columnInfo.getJavaName();
			
			//是否启用DateFormat
			if(generateModelDTO.isDateFormat()) {
				if(Date.class.getName().equals(columnInfo.getJavaType().getName())) {
					attributeBuffer.append(Symbol.lineStr)
					.append(Symbol.tabBy1).append("@DateTimeFormat(pattern = \"yyyy-MM-dd hh:mm:ss\")");
				}
			}
			
			//是否启用swagger
			if(generateModelDTO.isSwaggerAnnotation()) {
				//组装swagger ApiModelProperty
				if(Date.class.getName().equals(columnInfo.getJavaType().getName())) {
					attributeBuffer.append(Symbol.lineStr)
					.append(Symbol.tabBy1).append("@ApiModelProperty(value = \"").append(columnInfo.getExplain()).append("\"")
					.append(", example = \"").append(nowTimeStr).append("\")");
				}else {
					attributeBuffer.append(Symbol.lineStr)
					.append(Symbol.tabBy1).append("@ApiModelProperty(value = \"").append(columnInfo.getExplain()).append("\"").append(")");
				}
			}
			
			//组装属性
			attributeBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("private ").append(type).append(" ").append(name).append(";");
		}
		return attributeBuffer.toString();
	}
	
	/**
	 * 组装get set方法
	 * */
	protected String assembleGetAndSetMethod(GenerateModelDTO generateModelDTO) {
		StringBuffer InterAttrBuffer = new StringBuffer();
		for (ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			String type = columnInfo.getJavaType().getSimpleName();
			String name = columnInfo.getJavaName();
			String methodName = GeneratorUtils.firstCharToUpperCase(name);
			//组装get方法
			InterAttrBuffer
			.append(Symbol.lineStr)
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public ").append(type).append(" get").append(methodName).append("() {").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append(Symbol.tabBy1).append("return ").append(name).append(";").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("}")
			
			//组装set方法
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public ").append("void").append(" set").append(methodName).append("(").append(type).append(" ").append(name).append(")")
			.append(" {").append(Symbol.lineStr).append(Symbol.tabBy1).append(Symbol.tabBy1).append("this.").append(name).append(" = ").append(name).append(";").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("}");
		}
		return InterAttrBuffer.toString();
	}
	
	protected String getModelQuerySynamicColumnMethod(GenerateModelDTO generateModelDTO) {
		return assembleAddMethod(generateModelDTO) + assembleRemoveMethod(generateModelDTO) + assembleAddAllMethod(generateModelDTO);
	}
	
	/**
	 * 组装addAll方法
	 * */
	protected String assembleAddAllMethod(GenerateModelDTO generateModelDTO) {
		StringBuffer InterAttrBuffer = new StringBuffer();
		//组装addAll方法
		String javaObjectName = generateModelDTO.getModelQueryClassName();
		InterAttrBuffer
		.append(Symbol.lineStr)
		.append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("public ").append(javaObjectName).append(" addAllColumn").append("() {").append(Symbol.lineStr);
		for (ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			String column = columnInfo.getName();
			InterAttrBuffer.append(Symbol.tabBy1).append(Symbol.tabBy1).append("super.addColumn").append("(\"").append(column).append("\");").append(Symbol.lineStr);
		}
		InterAttrBuffer.append(Symbol.tabBy1).append(Symbol.tabBy1).append("return this;").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("}");
		return InterAttrBuffer.toString();
	}
	
	
	/**
	 * 组装add方法
	 * */
	protected String assembleAddMethod(GenerateModelDTO generateModelDTO) {
		StringBuffer InterAttrBuffer = new StringBuffer();
		InterAttrBuffer.append(Symbol.lineStr);
		for (ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			String name = columnInfo.getJavaName();
			String methodName = GeneratorUtils.firstCharToUpperCase(name);
			String javaObjectName = generateModelDTO.getModelQueryClassName();
			String column = columnInfo.getName();
			
			//组装add方法
			InterAttrBuffer
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public ").append(javaObjectName).append(" addColumnBy").append(methodName).append("() {").append(Symbol.lineStr)
//		super.addColumn("id");
			.append(Symbol.tabBy1).append(Symbol.tabBy1).append("super.addColumn").append("(\"").append(column).append("\");").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append(Symbol.tabBy1).append("return this;").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("}");
		}
		return InterAttrBuffer.toString();
	}
	
	
	/**
	 * 组装remove方法
	 * */
	protected String assembleRemoveMethod(GenerateModelDTO generateModelDTO) {
		StringBuffer InterAttrBuffer = new StringBuffer();
		
		InterAttrBuffer.append(Symbol.lineStr);
		for (ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			String name = columnInfo.getJavaName();
			String methodName = GeneratorUtils.firstCharToUpperCase(name);
			String javaObjectName = generateModelDTO.getModelQueryClassName();
			String column = columnInfo.getName();
			
			//组装add方法
			InterAttrBuffer
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public ").append(javaObjectName).append(" removeColumnBy").append(methodName).append("() {").append(Symbol.lineStr)
	//		super.addColumn("id");
			.append(Symbol.tabBy1).append(Symbol.tabBy1).append("super.removeColumn").append("(\"").append(column).append("\");").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append(Symbol.tabBy1).append("return this;").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("}");
		}
		return InterAttrBuffer.toString();
	}
}
