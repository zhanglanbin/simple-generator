package com.simple.generator.service.generate.impl;

import java.io.File;
import java.math.BigDecimal;
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
import com.simple.generator.pojo.ColumnInfo;
import com.simple.generator.pojo.dto.CommonQueryDTO;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.service.generate.GenerateService;
import com.simple.generator.service.generatro.GeneratroService;
import com.simple.generator.utils.GeneratorUtils;

@Service
public class ModelQueryServiceImpl implements GenerateService {
	
	private static final Logger log = LoggerFactory.getLogger(ModelQueryServiceImpl.class);

	@Autowired
	private GeneratroService generatroService;
	@Resource
	private List<GenerateModelDTO> generateModelDTOList;
	@Resource
	private CommonQueryDTO commonQueryDTO;
	
	@Override
	public boolean generate() {
		
		for (GenerateModelDTO generateModelDTO : generateModelDTOList) {
			try {
				String writerPath = generateModelDTO.getModelQueryJavaFilePath() + File.separatorChar + generateModelDTO.getModelQueryClassName()+".java";
				Context context = new Context();
				context.setVariable("generateModelDTO", generateModelDTO);
				context.setVariable("importPackage", assImport(generateModelDTO));
				context.setVariable("serialVersionUID", GeneratorUtils.getSerialVersionUID().toString());
				context.setVariable("notesDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				context.setVariable("commonQueryDTO", commonQueryDTO);
				context.setVariable("attribute", assembleAttribute(generateModelDTO));
				context.setVariable("getSetMethod", assembleGetAndSetMethod(generateModelDTO));
				context.setVariable("modelToString", GeneratorUtils.asModelToString(generateModelDTO, true, true));
				context.setVariable("querySynamicColumnMethod", getModelQuerySynamicColumnMethod(generateModelDTO));
				boolean result = generatroService.templateAnalysisGenerateFile(context, writerPath, PathConstant.generatorTemplatePath+"model/", "ModelQuery");
				if(!result) {
					log.debug("{} :: 生成失败!", context);
				}
			} catch (Exception e) {
				log.error("{}", e);
			}
		}
		
		return true;
	}
	
	/**
	 * 组装导包
	 * */
	protected String assImport(GenerateModelDTO generateModelDTO) {
		StringBuffer importBuffer = new StringBuffer();
		
		//序列化包
		importBuffer.append("import java.io.Serializable;").append(Symbol.lineStr);
		//导入big包
		if(generateModelDTO.isImportBigDecimal()) {
			importBuffer
			//时间日期转换注解
			.append("import ").append(BigDecimal.class.getName()).append(";").append(Symbol.lineStr);
		}
		//导入时间日期包
		if(generateModelDTO.isImportDate()) {
			importBuffer
			.append("import ").append(Date.class.getName()).append(";").append(Symbol.lineStr);
		}
		if(generateModelDTO.isImportList()) {
			importBuffer
			//时间日期转换注解
			.append("import ").append(List.class.getName()).append(";").append(Symbol.lineStr);
		}
				
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
		
		importBuffer.append(Symbol.lineStr)
		.append("import ").append(commonQueryDTO.getCommonQuerypackage()).append(";").append(Symbol.lineStr);
		
		//增加swagger包
		if(generateModelDTO.isSwaggerAnnotation()) {
			importBuffer.append(Symbol.lineStr)
			.append("import io.swagger.annotations.ApiModel;").append(Symbol.lineStr)
			.append("import io.swagger.annotations.ApiModelProperty;").append(Symbol.lineStr);
		}
		
		return importBuffer.toString();
	}
	
	/**
	 * 组装model属性
	 * */
	protected String assembleAttribute(GenerateModelDTO generateModelDTO) {
		StringBuffer attributeBuffer = new StringBuffer();
		for (int i=0 ; null!=generateModelDTO&&i<generateModelDTO.getColumnInfos().size() ; i++) {
			ColumnInfo columnInfo = generateModelDTO.getColumnInfos().get(i);
			
			if(i>00) {
				attributeBuffer.append(Symbol.lineStr).append(Symbol.tabBy1);
			}
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String nowTimeStr = simpleDateFormat.format(new Date());
			
			if(isJdbcDate(columnInfo)) {
				dateStartEndAttribute(attributeBuffer, generateModelDTO, columnInfo, nowTimeStr);
			} else {
				attribute(attributeBuffer, generateModelDTO, columnInfo, nowTimeStr);
			}
		}
		return attributeBuffer.toString();
	}
	
	
	protected void attribute(StringBuffer attributeBuffer, GenerateModelDTO generateModelDTO, ColumnInfo columnInfo, String nowTimeStr) {
		//组装注释
		attributeBuffer
		.append(GeneratorUtils.getModelAttrNotes(columnInfo.getExplain()));
		
		//是否启用DateFormat
		if(generateModelDTO.isDateFormat()) {
			attributeDateFormatAnnotation(attributeBuffer, columnInfo);
		}
		
		//是否启用swagger
		if(generateModelDTO.isSwaggerAnnotation()) {
			//组装swagger ApiModelProperty
			attributeSwaggerAnnotation(attributeBuffer, columnInfo, nowTimeStr);
		}
		//组装属性
		attributeBuffer.append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("private ").append(columnInfo.getJavaType().getSimpleName()).append(" ").append(columnInfo.getJavaName()).append(";");
	}
	
	
	protected void dateStartEndAttribute(StringBuffer attributeBuffer, GenerateModelDTO generateModelDTO, ColumnInfo columnInfo, String nowTimeStr) {
		String javaNameStart = columnInfo.getJavaName() + "Start";
		String javaNameEnd = columnInfo.getJavaName() + "End";
		String type = columnInfo.getJavaType().getSimpleName();
		for(int j=0 ; j<2 ; j++) {
			//组装注释
			attributeBuffer.append(GeneratorUtils.getModelAttrNotes(columnInfo.getExplain()));
			
			//是否启用DateFormat
			if(generateModelDTO.isDateFormat()) {
				attributeDateFormatAnnotation(attributeBuffer, columnInfo);
			}
			
			//是否启用swagger
			if(generateModelDTO.isSwaggerAnnotation()) {
				//组装swagger ApiModelProperty
				attributeSwaggerAnnotation(attributeBuffer, columnInfo, nowTimeStr);
			}
			//组装属性
			if(j==0) {
				attributeBuffer.append(Symbol.lineStr)
				.append(Symbol.tabBy1).append("private ").append(type).append(" ").append(javaNameStart).append(";")
				.append(Symbol.lineStr).append(Symbol.tabBy1);
			} else if(j==1) {
				attributeBuffer.append(Symbol.lineStr)
				.append(Symbol.tabBy1).append("private ").append(type).append(" ").append(javaNameEnd).append(";");
			}
		}
	}
	
	

	
	protected void attributeDateFormatAnnotation(StringBuffer attributeBuffer, ColumnInfo columnInfo) {
		if(Date.class.getName().equals(columnInfo.getJavaType().getName())) {
			attributeBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("@DateTimeFormat(pattern = \"yyyy-MM-dd hh:mm:ss\")");
		}
	}
	
	protected void attributeSwaggerAnnotation(StringBuffer attributeBuffer, ColumnInfo columnInfo, String nowTimeStr) {
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
	
	
	
	
	
	/**
	 * 组装get set方法
	 * */
	protected String assembleGetAndSetMethod(GenerateModelDTO generateModelDTO) {
		StringBuffer getSetMethodBuffer = new StringBuffer();
		for (int i=0 ; null!=generateModelDTO&&i<generateModelDTO.getColumnInfos().size() ; i++) {
			ColumnInfo columnInfo = generateModelDTO.getColumnInfos().get(i);
			
			if(i>0) {
				getSetMethodBuffer.append(Symbol.lineStr).append(Symbol.tabBy1);
			}
			if(isJdbcDate(columnInfo)) {
				dateTypeGetSetMethod(getSetMethodBuffer, columnInfo);
			} else {
				getSetMethod(getSetMethodBuffer, columnInfo);
			}
		}
		return getSetMethodBuffer.toString();
	}
	
	
	protected void dateTypeGetSetMethod(StringBuffer getSetBuffer, ColumnInfo columnInfo) {
		String javaType = columnInfo.getJavaType().getSimpleName();
		String startAttributeName = columnInfo.getJavaName() + "Start";
		String endAttributeName = columnInfo.getJavaName() + "End";
		String startMethodName = columnInfo.getJavaMethodName() + "Start";
		String endMethodName = columnInfo.getJavaMethodName() + "End";
		
		//组装get start方法
		getSetBuffer
		.append("public ").append(javaType).append(" get").append(startMethodName).append("() {").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append("return ").append(startAttributeName).append(";").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("}")
		//组装set start方法
		.append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("public ").append("void").append(" set").append(startMethodName).append("(").append(javaType).append(" ").append(startAttributeName).append(")")
		.append(" {").append(Symbol.lineStr).append(Symbol.tabBy1).append(Symbol.tabBy1).append("this.").append(startAttributeName).append(" = ").append(startAttributeName).append(";").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("}")
		
		//组装get end方法
		.append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("public ").append(javaType).append(" get").append(endMethodName).append("() {").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append("return ").append(endAttributeName).append(";").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("}")
		//组装set end方法
		.append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("public ").append("void").append(" set").append(endMethodName).append("(").append(javaType).append(" ").append(endAttributeName).append(")")
		.append(" {").append(Symbol.lineStr).append(Symbol.tabBy1).append(Symbol.tabBy1).append("this.").append(endAttributeName).append(" = ").append(endAttributeName).append(";").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("}");
	}
	
	protected void getSetMethod(StringBuffer getSetBuffer, ColumnInfo columnInfo) {
		String javaType = columnInfo.getJavaType().getSimpleName();
		String attributeName = columnInfo.getJavaName();
		//组装get方法
		getSetBuffer
		.append("public ").append(javaType).append(" get").append(columnInfo.getJavaMethodName()).append("() {").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append("return ").append(attributeName).append(";").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("}")
		
		//组装set方法
		.append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("public ").append("void").append(" set").append(columnInfo.getJavaMethodName()).append("(").append(javaType).append(" ").append(attributeName).append(")")
		.append(" {").append(Symbol.lineStr).append(Symbol.tabBy1).append(Symbol.tabBy1).append("this.").append(attributeName).append(" = ").append(attributeName).append(";").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("}");
	}
	
	
	
	
	
	
	/**
	 * 组装hashCode
	 * */
	protected String assembleHashCode(GenerateModelDTO generateModelDTO) {
		StringBuffer attributeBuffer = new StringBuffer();
		attributeBuffer
		.append("@Override").append(Symbol.lineStr)
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
	protected String assembleEquals(GenerateModelDTO generateModelDTO) {
		StringBuffer attributeBuffer = new StringBuffer();
		
		String ObjectName = generateModelDTO.getModelClassName();
		
		attributeBuffer
		.append("@Override").append(Symbol.lineStr)
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
	
	
	protected boolean isJdbcDate(ColumnInfo columnInfo) {
		if(columnInfo.getJavaType().getName().equals(Date.class.getName())) {
			return true;
		}else {
			return false;
		}
	}
}
