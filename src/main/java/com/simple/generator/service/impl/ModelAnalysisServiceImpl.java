package com.simple.generator.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.simple.generator.constant.Symbol;
import com.simple.generator.pojo.ColumnInfo;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.service.AssembleAnalysisService;
import com.simple.generator.utils.GeneratorUtils;

@Component
public class ModelAnalysisServiceImpl implements AssembleAnalysisService {

	@Override
	public String analysis(String str, GenerateModelDTO generateModelDTO) {
		return str.replace("${generator.modelPackagePath}", generateModelDTO.getModelPackagePath())
				.replace("${generator.importPackage}", assImport(generateModelDTO))
				.replace("${generator.modelClassName}", generateModelDTO.getModelClassName())
				.replace("${generator.explain}", generateModelDTO.getExplain())
				.replace("${generator.author}", generateModelDTO.getAuthor())
				.replace("${generator.date}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.replace("${generator.swaggerApiModelAnnotation}", getSwaggerApiModelAnnotation(generateModelDTO))
				.replace("${generator.serialVersionUID}", GeneratorUtils.getSerialVersionUID().toString())
				.replace("${generator.modelAttribute}", assembleAttribute(generateModelDTO))
				.replace("${generator.getAndSetMethod}", assembleGetAndSetMethod(generateModelDTO))
				.replace("${generator.toString}", GeneratorUtils.asModelToString(generateModelDTO, false))
				.replace("${generator.hashCode}", assembleHashCode(generateModelDTO))
				.replace("${generator.equals}", assembleEquals(generateModelDTO));
	}

	/**
	 * 组装导包
	 * */
	protected static String assImport(GenerateModelDTO generateModelDTO) {
		StringBuffer importBuffer = new StringBuffer();
		
		//序列化包
		importBuffer.append(Symbol.lineStr).append("import java.io.Serializable;").append(Symbol.lineStr);
		
		if(generateModelDTO.isImportList()) {
			importBuffer.append(Symbol.lineStr)
			//时间日期转换注解
			.append("import ").append(List.class.getName()).append(";").append(Symbol.lineStr);
		}
		
		
		//导入big包
		if(generateModelDTO.isImportBigDecimal()) {
			importBuffer.append(Symbol.lineStr)
			//时间日期转换注解
			.append("import ").append(BigDecimal.class.getName()).append(";").append(Symbol.lineStr);
		}
		
		//导入时间日期包
		if(generateModelDTO.isDateFormat()) {
			importBuffer.append(Symbol.lineStr)
			.append("import ").append(Date.class.getName()).append(";").append(Symbol.lineStr)
			
			//时间日期转换注解
			.append(Symbol.lineStr)
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
		
		return importBuffer.toString();
	}
	
	protected String getSwaggerApiModelAnnotation(GenerateModelDTO generateModelDTO) {
		if(generateModelDTO.isSwaggerAnnotation()) {
			return "@ApiModel(value = \"" + generateModelDTO.getExplain() + "\", description = \"" + generateModelDTO.getExplain() + "\")";
		} else {
			return "";
		}
	}
	
	
	/**
	 * 组装model属性
	 * */
	protected static String assembleAttribute(GenerateModelDTO generateModelDTO) {
		StringBuffer attributeBuffer = new StringBuffer();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String nowTimeStr = simpleDateFormat.format(new Date());
		
		for (ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			//获取属性注释
			String attributeNotes = GeneratorUtils.getModelAttrNotes(columnInfo.getExplain());
			//组装注释
			attributeBuffer
			.append(Symbol.lineStr)
			.append(attributeNotes);
			
			//组装属性
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
			
			if(columnInfo.getJavaType() == List.class) {
				String JavaTypeByListT = columnInfo.getJavaTypeByListT().getSimpleName();
				attributeBuffer.append(Symbol.lineStr)
				.append(Symbol.tabBy1).append("private List<").append(JavaTypeByListT).append("> ").append(name).append(";");
			} else {
				attributeBuffer.append(Symbol.lineStr)
				.append(Symbol.tabBy1).append("private ").append(type).append(" ").append(name).append(";");
			}
		}
		return attributeBuffer.toString();
	}
	
	
	/**
	 * 组装get set方法
	 * */
	protected static String assembleGetAndSetMethod(GenerateModelDTO generateModelDTO) {
		StringBuffer InterAttrBuffer = new StringBuffer();
		
		for (ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			String type = columnInfo.getJavaType().getSimpleName();
			String name = columnInfo.getJavaName();
			String methodName = GeneratorUtils.firstCharToUpperCase(name);
			
			if(columnInfo.getJavaType() == List.class) {
				String JavaTypeByListT = columnInfo.getJavaTypeByListT().getSimpleName();
				//组装get方法
				InterAttrBuffer
				.append(Symbol.lineStr)
				.append(Symbol.lineStr)
				.append(Symbol.tabBy1).append("public List<").append(JavaTypeByListT).append("> get").append(methodName).append("() {").append(Symbol.lineStr)
				.append(Symbol.tabBy1).append(Symbol.tabBy1).append("return ").append(name).append(";").append(Symbol.lineStr)
				.append(Symbol.tabBy1).append("}")
				
				//组装set方法
				.append(Symbol.lineStr)
				.append(Symbol.tabBy1).append("public ").append("void").append(" set").append(methodName).append("(List<").append(JavaTypeByListT).append("> ").append(name).append(")")
				.append(" {").append(Symbol.lineStr).append(Symbol.tabBy1).append(Symbol.tabBy1).append("this.").append(name).append(" = ").append(name).append(";").append(Symbol.lineStr)
				.append(Symbol.tabBy1).append("}");
			} else {
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
		}
		return InterAttrBuffer.toString();
	}
	
	/**
	 * 组装hashCode
	 * */
	protected static String assembleHashCode(GenerateModelDTO generateModelDTO) {
		StringBuffer attributeBuffer = new StringBuffer();
		attributeBuffer.append(Symbol.lineStr).append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("@Override").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("public int hashCode() {").append(Symbol.lineStr)
		.append(Symbol.tabBy2).append("final int prime = 31;").append(Symbol.lineStr)
		.append(Symbol.tabBy2).append("int result = 1;").append(Symbol.lineStr);
		for (ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			//组装属性
			String name = columnInfo.getJavaName();
			attributeBuffer
			.append(Symbol.tabBy2).append("result = prime * result + ((").append(name).append(" == null) ? 0 : ").append(name).append(".hashCode());").append(Symbol.lineStr);
		}
		attributeBuffer.append(Symbol.tabBy2).append("return result;").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("}");
		return attributeBuffer.toString();
	}
	
	/**
	 * 组装equals
	 * */
	protected static String assembleEquals(GenerateModelDTO generateModelDTO) {
		StringBuffer attributeBuffer = new StringBuffer();
		
		String ObjectName = generateModelDTO.getModelClassName();
		
		attributeBuffer.append(Symbol.lineStr).append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("@Override").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("public boolean equals(Object obj) {").append(Symbol.lineStr)
		.append(Symbol.tabBy2).append("if (this == obj)").append(Symbol.lineStr)
		.append(Symbol.tabBy3).append("return true;").append(Symbol.lineStr)
		.append(Symbol.tabBy2).append("if (obj == null)").append(Symbol.lineStr)
		.append(Symbol.tabBy3).append("return false;").append(Symbol.lineStr)
		.append(Symbol.tabBy2).append("if (getClass() != obj.getClass())").append(Symbol.lineStr)
		.append(Symbol.tabBy3).append("return false;").append(Symbol.lineStr)
		.append(Symbol.tabBy2).append(ObjectName).append(" other = (").append(ObjectName).append(") obj;").append(Symbol.lineStr);
		for (ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			//组装属性
			String name = columnInfo.getJavaName();
			attributeBuffer
			.append(Symbol.tabBy2).append("if (").append(name).append(" == null) {").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("if (other.").append(name).append(" != null)").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("return false;").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("} else if (!").append(name).append(".equals(other.").append(name).append("))").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("return false;").append(Symbol.lineStr);
		}
		attributeBuffer.append(Symbol.tabBy2).append("return true;").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("}");
		return attributeBuffer.toString();
	}
}
