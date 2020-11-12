package com.simple.generator.service.generate.impl;

import java.io.File;
import java.math.BigDecimal;
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
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.service.generate.GenerateService;
import com.simple.generator.service.generatro.GeneratroService;
import com.simple.generator.utils.GeneratorUtils;

@Service
public class MapperServiceImpl implements GenerateService {
	
	private static final Logger log = LoggerFactory.getLogger(MapperServiceImpl.class);

	@Autowired
	private GeneratroService generatroService;
	@Resource
	private List<GenerateModelDTO> generateModelDTOList;
	
	@Override
	public boolean generate() {
		
		for (GenerateModelDTO generateModelDTO : generateModelDTOList) {
			try {
				String writerPath = generateModelDTO.getMapperXmlFilePath() + File.separatorChar + generateModelDTO.getMapperXmlName() + ".xml";
				Context context = new Context();
				context.setVariable("generateModelDTO", generateModelDTO);
				context.setVariable("modelPrimaryKeyJavaTypeName", generateModelDTO.getModelPrimaryKeyInfo().getJavaType().getSimpleName());
				context.setVariable("findAllColumns", findAllColumns(generateModelDTO.getColumnInfos()));
				context.setVariable("findAllConditionVague", getFindAllConditionVague(generateModelDTO));
				context.setVariable("findAllCondition", getFindAllCondition(generateModelDTO));
				context.setVariable("inDataColumns", getInDataColumns(generateModelDTO.getColumnInfos()));
				context.setVariable("inJdbcTypeColumns", getInJdbcTypeColumns(generateModelDTO));
				context.setVariable("dynamicAddModelColumns", getDynamicAddModelColumns(generateModelDTO));
				context.setVariable("modelPrimaryKeyJavaTypeName", generateModelDTO.getModelPrimaryKeyInfo().getJavaType().getSimpleName());
				context.setVariable("modelPrimaryKeyMapper", getModelPrimaryKeyMapper(generateModelDTO.getModelPrimaryKeyInfo()));
				context.setVariable("dynamicUpdateModelColumns", getColuUpdateSet(generateModelDTO));
				context.setVariable("batchDynamicUpdateModelColumns", getColuUpdateSets(generateModelDTO));
				context.setVariable("dynamicFindColumns", getDynamicColumnQuery(generateModelDTO.getColumnInfos(), "column"));
				boolean result = generatroService.templateAnalysisGenerateFile(context, writerPath, PathConstant.generatorTemplatePath+"dao/", "mapper");
				if(!result) {
					log.debug("{} :: 生成失败!", context);
				}
			} catch (Exception e) {
				log.error("{}", e);
			}
		}
		
		return true;
	}
	
	//查询结果的所有字段
	protected String findAllColumns(List<ColumnInfo> columnInfos) {
		StringBuffer columnsbBuffer = new StringBuffer();
		for(ColumnInfo columnInfo : columnInfos) {
			String columnName = columnInfo.getName();
			String javaAttrName = columnInfo.getJavaName();
			columnsbBuffer.append(Symbol.tabBy2).append(columnName).append(" ").append(javaAttrName).append(",").append(Symbol.lineStr);
		}
		return columnsbBuffer.toString().substring(2,columnsbBuffer.toString().length()-2);
	}
	
	
	//模糊查询条件
	protected String getFindAllConditionVague(GenerateModelDTO generateModelDTO) {
		StringBuffer vagueConditionBuffer = new StringBuffer();
		
		for(ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			String javaName = columnInfo.getJavaName();
			String columnName = columnInfo.getName();
			String jdbcType = columnInfo.getJdbcType();
			// 条件字符
			String conditionCharOR = "OR";
			//组装条件的jdbc类型, 要确切的区分开 String, Integer, Date
			if(isJdbcDate(columnInfo)) {
				String javaNameStart = javaName + "Start";
				String javaNameEnd = javaName + "End";
				
				//start
				vagueConditionBuffer.append(Symbol.tabBy2)
				.append("<if test=\"").append(javaNameStart).append("!=null").append(" and ").append(javaNameEnd).append("==null\">").append(Symbol.lineStr)
				.append(Symbol.tabBy3)
				.append("<![CDATA[ ").append(conditionCharOR).append(" ").append(columnName).append(" >= ").append("DATE_FORMAT(#{").append(javaNameStart).append("},'%Y-%m-%d %T:%i:%s'").append(") ]]>").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
				//end
				.append(Symbol.tabBy2).append("<if test=\"").append(javaNameStart).append("==null").append(" and ").append(javaNameEnd).append("!=null\">").append(Symbol.lineStr)
				.append(Symbol.tabBy3)
				.append("<![CDATA[ ").append(conditionCharOR).append(" ").append(columnName).append(" <= ").append("DATE_FORMAT(#{").append(javaNameEnd).append("},'%Y-%m-%d %T:%i:%s'").append(") ]]>").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
				// start and end
				.append(Symbol.tabBy2).append("<if test=\"").append(javaNameStart).append("!=null").append(" and ").append(javaNameEnd).append("!=null\">").append(Symbol.lineStr)
				.append(Symbol.tabBy3)
				.append("<![CDATA[ ").append(conditionCharOR).append(" (")
				.append(columnName).append(" >= ").append("DATE_FORMAT(#{").append(javaNameStart).append("},'%Y-%m-%d %T:%i:%s') AND ")
				.append(columnName).append(" <= ").append("DATE_FORMAT(#{").append(javaNameEnd).append("},'%Y-%m-%d %T:%i:%s')) ]]>").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr);
				
			}else if(isJdbcInteger(columnInfo)) {
				vagueConditionBuffer.append(Symbol.tabBy2)
				.append("<if test=\"").append(javaName).append("!=null\">").append(Symbol.lineStr)
				.append(Symbol.tabBy3)
				.append(conditionCharOR).append(" ").append(columnName).append(" = ").append("#{").append(javaName).append(",jdbcType=").append(jdbcType).append("}").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr);
			}else {
				vagueConditionBuffer.append(Symbol.tabBy2)
				.append("<if test=\"").append(javaName).append("!=null and ").append(javaName).append("!=''\">").append(Symbol.lineStr)
				.append(Symbol.tabBy3)
				.append(conditionCharOR).append(" ").append(columnName).append(" LIKE ").append("CONCAT('%',#{").append(javaName).append(",jdbcType=").append(jdbcType).append("},'%')")
				.append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr);
			}
		}
		//记录问题, jdbcType  java boolean 转 jdbc时 不对 , 待确认
		return vagueConditionBuffer.toString().substring(2,vagueConditionBuffer.toString().length()-1);
	}
	
	
	//精确查询条件
	protected String getFindAllCondition(GenerateModelDTO generateModelDTO) {
		StringBuffer conditionBuffer = new StringBuffer();
		
		for(ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			String javaName = columnInfo.getJavaName();
			String columnName = columnInfo.getName();
			String jdbcType = columnInfo.getJdbcType();
			
			if(isJdbcDate(columnInfo)) {
				String javaNameStart = javaName + "Start";
				String javaNameEnd = javaName + "End";
				
				//start
				conditionBuffer.append(Symbol.tabBy2)
				.append("<if test=\"").append(javaNameStart).append("!=null").append("\">").append(Symbol.lineStr)
				.append(Symbol.tabBy3)
				.append("<![CDATA[ AND ").append(columnName).append(" >= ").append("DATE_FORMAT(#{").append(javaNameStart).append("},'%Y-%m-%d %T:%i:%s'").append(") ]]>").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
				//end
				.append(Symbol.tabBy2).append("<if test=\"").append(javaNameEnd).append("!=null").append("\">").append(Symbol.lineStr)
				.append(Symbol.tabBy3)
				.append("<![CDATA[ AND ").append(columnName).append(" <= ").append("DATE_FORMAT(#{").append(javaNameEnd).append("},'%Y-%m-%d %T:%i:%s'").append(") ]]>").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr);
			}else if(isJdbcInteger(columnInfo)) {
				conditionBuffer.append(Symbol.tabBy2)
				.append("<if test=\"").append(javaName).append("!=null\">").append(Symbol.lineStr)
				.append(Symbol.tabBy3)
				.append("AND ").append(columnName).append(" = ").append("#{").append(javaName).append(",jdbcType=").append(jdbcType).append("}").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr);
			}else {
				conditionBuffer.append(Symbol.tabBy2)
				.append("<if test=\"").append(javaName).append("!=null and ").append(javaName).append("!=''\">").append(Symbol.lineStr)
				.append(Symbol.tabBy3)
				.append("AND ").append(columnName).append(" = ").append("#{").append(javaName).append(",jdbcType=").append(jdbcType).append("}").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr);
			}
		}
		
		return conditionBuffer.toString().substring(2,conditionBuffer.toString().length()-1);
	}
	
	
	
	
	
	
	
	protected boolean isIf0(ColumnInfo columnInfo) {
		if(columnInfo.getJavaType().getName().equals(Boolean.class.getName()) ||
				columnInfo.getJavaType().getName().equals(Integer.class.getName()) ||
				columnInfo.getJavaType().getName().equals(Long.class.getName()) ||
				columnInfo.getJavaType().getName().equals(Float.class.getName()) ||
				columnInfo.getJavaType().getName().equals(Double.class.getName()) ||
				columnInfo.getJavaType().getName().equals(BigDecimal.class.getName()) ||
				columnInfo.getJavaType().getName().equals(Date.class.getName())
				) {
			return true;
		}else {
			return false;
		}
	}
	
	protected boolean isJdbcDate(ColumnInfo columnInfo) {
		if(columnInfo.getJavaType().getName().equals(Date.class.getName())) {
			return true;
		}else {
			return false;
		}
	}
	
	protected boolean isJdbcInteger(ColumnInfo columnInfo) {
		if(columnInfo.getJavaType().getName().equals(Boolean.class.getName()) ||
				columnInfo.getJavaType().getName().equals(Integer.class.getName()) ||
				columnInfo.getJavaType().getName().equals(Long.class.getName()) ||
				columnInfo.getJavaType().getName().equals(Float.class.getName()) ||
				columnInfo.getJavaType().getName().equals(Double.class.getName()) ||
				columnInfo.getJavaType().getName().equals(BigDecimal.class.getName())
				) {
			return true;
		}else {
			return false;
		}
	}
	
	
	//插入字段列
	protected String getInDataColumns(List<ColumnInfo> columnInfos) {
		StringBuffer dataColumnBuffer = new StringBuffer();
		for(ColumnInfo cif : columnInfos) {
				dataColumnBuffer
				.append(Symbol.tabBy4)
				.append(cif.getName()).append(",").append(Symbol.lineStr);
		}
		return dataColumnBuffer.toString().substring(4,dataColumnBuffer.toString().length()-2);
	}
	
	
	
	//插入字段
	protected String getInJdbcTypeColumns(GenerateModelDTO generateModelDTO) {
		List<ColumnInfo> columnInfos = generateModelDTO.getColumnInfos();
		String objectName = generateModelDTO.getModelClassName();
		String objectVarName = GeneratorUtils.firstCharToLowerCase(objectName);
		StringBuffer jdbcColumnBuffer = new StringBuffer();
		for(ColumnInfo cif : columnInfos) {
			jdbcColumnBuffer
			.append(Symbol.tabBy4)
			.append("#{").append(objectVarName).append(".").append(cif.getJavaName()).append("}").append(",").append(Symbol.lineStr);
		}
		return jdbcColumnBuffer.toString().substring(4,jdbcColumnBuffer.toString().length()-2);
	}
	
	
	
	/**
	 * 动态插入
	 * */
	protected String getDynamicAddModelColumns(GenerateModelDTO generateModelDTO) {
		List<ColumnInfo> columnInfos = generateModelDTO.getColumnInfos();
		String objectName = generateModelDTO.getModelClassName();
		String objectVarName = GeneratorUtils.firstCharToLowerCase(objectName);
		StringBuffer coluBuffer = new StringBuffer();
		for(ColumnInfo columnInfo : columnInfos) {
			String javaName = columnInfo.getJavaName();
			String columnName = columnInfo.getName();
			if(isIf0(columnInfo)) {
				coluBuffer.append(Symbol.tabBy4)
				.append("<if test=\"").append(objectVarName).append(".").append(javaName).append("!=null\">").append(Symbol.lineStr);
			} else {
				coluBuffer.append(Symbol.tabBy4)
				.append("<if test=\"").append(objectVarName).append(".").append(javaName).append("!=null and ")
				.append(objectVarName).append(".").append(javaName).append("!=''\">").append(Symbol.lineStr);
			}
			coluBuffer.append(Symbol.tabBy5)
			.append(columnName).append(" = ").append("#{").append(objectVarName).append(".").append(javaName).append("},").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("</if>").append(Symbol.lineStr);
		}
		return coluBuffer.toString().substring(4,coluBuffer.toString().length()-1);
	}
	
	protected String getModelPrimaryKeyMapper(ColumnInfo mainColumnInfo) {
		return "#{"+mainColumnInfo.getJavaName()+",jdbcType="+mainColumnInfo.getJdbcType()+"}";
	}
	
	//更新字段
	protected String getColuUpdateSet(GenerateModelDTO generateModelDTO) {
		List<ColumnInfo> columnInfos = generateModelDTO.getColumnInfos();
		String objectName = generateModelDTO.getModelClassName();
		String objectVarName = GeneratorUtils.firstCharToLowerCase(objectName);
		
		StringBuffer coluBuffer = new StringBuffer();
		for(ColumnInfo columnInfo : columnInfos) {
			String javaName = columnInfo.getJavaName();
			String columnName = columnInfo.getName();
			if(!generateModelDTO.getModelPrimaryKeyInfo().getJavaName().equals(javaName)) {
				if(isIf0(columnInfo)) {
					
					coluBuffer.append(Symbol.tabBy4)
					.append("<if test=\"").append(objectVarName).append(".").append(javaName).append("!=null\">").append(Symbol.lineStr);
					
				} else {
					coluBuffer.append(Symbol.tabBy4)
					.append("<if test=\"").append(objectVarName).append(".").append(javaName).append("!=null and ")
					.append(objectVarName).append(".").append(javaName).append("!=''\">").append(Symbol.lineStr);
				}
				coluBuffer.append(Symbol.tabBy5)
				.append(columnName).append(" = ").append("#{").append(objectVarName).append(".").append(javaName).append("},").append(Symbol.lineStr)
				.append(Symbol.tabBy4).append("</if>").append(Symbol.lineStr);
			}
		}
		return coluBuffer.toString().substring(4,coluBuffer.toString().length()-1);
	}
	
	//批量更新set字段
	protected String getColuUpdateSets(GenerateModelDTO generateModelDTO) {
		StringBuffer coluBuffer = new StringBuffer();
		for(ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			String javaName = columnInfo.getJavaName();
			String columnName = columnInfo.getName();
			String callJavaName = generateModelDTO.getModelVariableName() + "." + javaName;
			if(!generateModelDTO.getModelPrimaryKeyInfo().getJavaName().equals(javaName)) {
				if(isIf0(columnInfo)) {
					coluBuffer.append(Symbol.tabBy5)
					.append("<if test=\"").append(callJavaName).append("!=null\">").append(Symbol.lineStr);
					
				} else {
					coluBuffer.append(Symbol.tabBy5)
					.append("<if test=\"").append(callJavaName).append("!=null and ").append(callJavaName).append("!=''\">").append(Symbol.lineStr);
				}
				
				//组装条件的jdbc类型, 要确切的区分开 String, Integer, Date
				coluBuffer.append(Symbol.tabBy3).append(Symbol.tabBy3)
				.append(columnName).append(" = ").append("#{").append(callJavaName).append("},").append(Symbol.lineStr)
				.append(Symbol.tabBy5).append("</if>").append(Symbol.lineStr);
			}
		}
		return coluBuffer.toString().substring(4,coluBuffer.toString().length()-1);
	}
	
	/**
	 * 动态列查询
	 * */
	protected String getDynamicColumnQuery(List<ColumnInfo> columnInfos, String dynColumnName) {
		StringBuffer coluBuffer = new StringBuffer();
		for(ColumnInfo columnInfo : columnInfos) {
			String columnName = columnInfo.getName();
			String javaAttrName = columnInfo.getJavaName();
			coluBuffer.append(Symbol.tabBy5)
			.append("<if test=\"").append(dynColumnName).append("!=null and ").append(dynColumnName).append("=='").append(columnName).append("'\">").append(Symbol.lineStr)
			.append(Symbol.tabBy6).append(columnName).append(" ").append(javaAttrName).append(Symbol.lineStr)
			.append(Symbol.tabBy5).append("</if>").append(Symbol.lineStr);
		}
		return coluBuffer.toString().substring(5,coluBuffer.toString().length()-1);
	}
}
