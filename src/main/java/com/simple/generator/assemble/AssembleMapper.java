package com.simple.generator.assemble;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.constant.Symbol;
import com.simple.generator.pojo.ColumnInfo;
import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleMapper {

	private static final Logger log = LoggerFactory.getLogger(AssembleMapper.class);

	@Bean
	public List<PrepareGennerateFile> mapperList(List<GenerateModelDTO> generateModelDTOList) {
		InputStream mapperStream = null;
		mapperStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\mapper.txt");
		String mapperTxt = GeneratorUtils.readTxt(mapperStream);
//		System.out.println(mapperTxt);
		try {
			mapperStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<PrepareGennerateFile> modelList = new ArrayList<>();
		for (GenerateModelDTO generateModelDTO : generateModelDTOList) {
			
			String mapperTxtTemp = mapperTxt.replace("${generator.daoPackage}", generateModelDTO.getDaoPackage())
					.replace("${generator.findAllColumns}", findAllColumns(generateModelDTO.getColumnInfos()))
					.replace("${generator.whereFindAllConditionVague}", getVagueFindAllSql(generateModelDTO.getColumnInfos(), generateModelDTO.getModelPrimaryKeyInfo(), generateModelDTO.getModelVariableName()))
					.replace("${generator.whereFindAllCondition}", getFindAllSql(generateModelDTO.getColumnInfos(), generateModelDTO.getModelPrimaryKeyInfo(), generateModelDTO.getModelVariableName()))
					.replace("${generator.modelPackage}", generateModelDTO.getModelPackage())
					.replace("${generator.modelPrimaryKey}", generateModelDTO.getModelPrimaryKeyInfo().getJavaName())
					.replace("${generator.modelVariableName}", generateModelDTO.getModelVariableName())
					.replace("${generator.modelTableName}", generateModelDTO.getModelTableName())
					.replace("${generator.sqlColumns}", getInDataColumns(generateModelDTO.getColumnInfos()))
					.replace("${generator.modelColumns}", getInJdbcTypeColu(generateModelDTO))
					.replace("${generator.dynamicAddModelColumns}", getInDynamic(generateModelDTO))
					.replace("${generator.dynamicUpdateModelColumns}", getColuUpdateSet(generateModelDTO))
					.replace("${generator.batchDynamicUpdateModelColumns}", getColuUpdateSets(generateModelDTO.getColumnInfos(), generateModelDTO.getModelPrimaryKeyInfo(), generateModelDTO.getModelVariableName()))
					.replace("${generator.modelListVariableName}", generateModelDTO.getModelListVariableName())
					.replace("${generator.modelPrimaryKeyMethodName}", generateModelDTO.getDaoModelPrimaryKeyMethodName())
					.replace("${generator.modelPrimaryKeyJavaTypePackage}",generateModelDTO.getModelPrimaryKeyInfo().getJavaType().getTypeName())
					.replace("${generator.modelSqlPrimaryKey}", generateModelDTO.getModelPrimaryKeyInfo().getName())
					.replace("${generator.modelPrimaryKeyMapper}", getModelPrimaryKeyMapper(generateModelDTO.getModelPrimaryKeyInfo()))
					.replace("${generator.modelPrimaryKeyListMethodName}", generateModelDTO.getDaoModelPrimaryKeyListMethodName())
					.replace("${generator.modelPrimaryKeyVariableList}", generateModelDTO.getDaoModelPrimaryKeyListParamVariableName())
					.replace("${generator.modelQueryPackage}", generateModelDTO.getModelQueryPackage())
					.replace("${generator.dynamicFindColumns}", getDynamicColumnQuery(generateModelDTO.getColumnInfos(), "column"));
			
//			System.out.println(mapperTxtTemp);
			
			PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
			
//			String mapperText = getMapperText(generateModelDTO);
//			
//			if(null==mapperText || "".equals(mapperText)) {
//				continue ;
//			}
			
			gennerateFile.setFileName(generateModelDTO.getMapperXmlName());
			gennerateFile.setFilePath(generateModelDTO.getMapperXmlFilePath());
			gennerateFile.setFileSuffix("xml");
			gennerateFile.setText(mapperTxtTemp);
			modelList.add(gennerateFile);
		}
		return modelList;
	}
	
	
	public String getMapperText(GenerateModelDTO generateModelDTO) {
		if(null==generateModelDTO) {
			return null;
		}
		StringBuffer mapperBuffer = new StringBuffer();
		try {
			//主键对象
			String tableName = generateModelDTO.getModelTableName();
			String objectName = generateModelDTO.getModelClassName();
			String objectVarName = GeneratorUtils.firstCharToLowerCase(objectName);
			ColumnInfo columnInfo = generateModelDTO.getModelPrimaryKeyInfo();
			List<ColumnInfo> columnInfos = generateModelDTO.getColumnInfos();
			
			
			//主键字段名
			String primaryKeyColumnName = columnInfo.getName();
			//主键java名称
			String primaryKeyJavaName  = columnInfo.getJavaName();
			//主键驼峰变量名, 首字母大写
			String primaryKeyMethodName = GeneratorUtils.firstCharToUpperCase(primaryKeyJavaName);
			//主键java类型名称
			String primaryKeyJavaTypeName = columnInfo.getJavaType().getTypeName();
			//主键jdbcType
			String primaryKeyJdbcType = columnInfo.getJdbcType();
			//dao名称
			String daoName = generateModelDTO.getDaoClassName();
			//model包路径
			String modelPackagePath = generateModelDTO.getModelPackagePath();
			
			
//			String objectQeuryVarName = GeneratorUtils.firstCharToLowerCase(objectQeuryName);
			//modelQuery包路径
			String modelQeuryPackage = generateModelDTO.getModelQueryPackagePath() +"."+ generateModelDTO.getModelQueryClassName();
			
			
			//list对象别名
			String objListAsName = objectVarName + "List";
			//list主键别名
			String pkeyListAsName = primaryKeyJavaName + "List";
			//list主键方法名
			String pkeyMethodListAsName = primaryKeyMethodName + "List";
			
			
			
			
			//组装mapper头部
			mapperBuffer.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>").append(Symbol.lineStr)
			.append("<!DOCTYPE mapper ").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">").append(Symbol.lineStr)
			.append("<mapper namespace=\"").append(generateModelDTO.getDaoPackagePath()).append(".").append(daoName).append("\">").append(Symbol.lineStr);
			
			//组装findById - sql字段列
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- 查询所有字段 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<sql id=\"findAllColumns\">").append(Symbol.lineStr)
			.append(findAllColumns(columnInfos)).append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</sql>").append(Symbol.lineStr);
			
			
			
			//组装模糊sql条件
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- 查询所有, 模糊条件匹配 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<sql id=\"findAllConditionVague\">").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("<where>").append(Symbol.lineStr)
			.append(getVagueFindAllSql(columnInfos, columnInfo, objectVarName))
			.append(Symbol.tabBy2).append("</where>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</sql>").append(Symbol.lineStr);
			//组装精确sql条件
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- 查询所有, 精确条件匹配 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<sql id=\"findAllCondition\">").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("<where>").append(Symbol.lineStr)
			.append(getFindAllSql(columnInfos, columnInfo, objectVarName))
			.append(Symbol.tabBy2).append("</where>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</sql>").append(Symbol.lineStr);
			
			
			
			//组装insert
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- 插入 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<insert id=\"insert\" parameterType=\"").append(modelPackagePath).append("\"").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append(Symbol.tabBy3)
			.append("useGeneratedKeys=\"true\" keyProperty=\"").append(primaryKeyJavaName)
			.append("\" keyColumn=\"").append(primaryKeyColumnName).append("\">").append(Symbol.lineStr)
			//判空
			.append(Symbol.tabBy2).append("<if test=\"").append(objectVarName).append("!=null\">").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("INSERT INTO ").append(tableName).append(" (").append(Symbol.lineStr)
			.append(getInDataColumns(columnInfos)).append(Symbol.lineStr)
			.append(Symbol.tabBy3).append(") VALUES (").append(Symbol.lineStr)
			.append(getInJdbcTypeColu(generateModelDTO)).append(Symbol.lineStr)
			.append(Symbol.tabBy3).append(")").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</insert>").append(Symbol.lineStr);
			
			
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- 动态插入 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<insert id=\"insertDynamic\" parameterType=\"").append(modelPackagePath).append("\"").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append(Symbol.tabBy3)
			.append("useGeneratedKeys=\"true\" keyProperty=\"").append(primaryKeyJavaName)
			.append("\" keyColumn=\"").append(primaryKeyColumnName).append("\">").append(Symbol.lineStr)
			//判空
			.append(Symbol.tabBy2).append("<if test=\"").append(objectVarName).append("!=null\">").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("INSERT INTO ").append(tableName).append(" ").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<set>").append(Symbol.lineStr)
			.append(getInDynamic(generateModelDTO)).append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("</set>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</insert>").append(Symbol.lineStr);
			
			
			//组装insertBatch
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- 批量插入 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<insert id=\"insertBatch\" parameterType=\"").append("java.util.List").append("\"").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append(Symbol.tabBy3)
			.append("useGeneratedKeys=\"true\" keyProperty=\"").append(primaryKeyJavaName)
			.append("\" keyColumn=\"").append(primaryKeyColumnName).append("\">").append(Symbol.lineStr)
			//判空
			.append(Symbol.tabBy2).append("<if test=\"").append(objListAsName).append("!=null and ")
			.append(objListAsName).append(".size()>0\">").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("INSERT INTO ").append(generateModelDTO.getModelTableName()).append(" (").append(Symbol.lineStr)
			.append(getInDataColumns(columnInfos)).append(Symbol.lineStr)
			.append(Symbol.tabBy3).append(") VALUES ").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<foreach collection=\"").append(objectVarName).append("s\" ")
			.append("item=\"").append(objectVarName).append("\" index=\"index\" ").append("separator=\",\">").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("(").append(Symbol.lineStr)
			.append(getInBatchJdbc(columnInfos, objectVarName)).append(Symbol.lineStr)
			.append(Symbol.tabBy3).append(")").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("</foreach>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</insert>").append(Symbol.lineStr);
			
			//组装deleteById DELETE FROM runoob_tbl WHERE runoob_id=#{,jdbcType=};
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- 删除 By 主键 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<delete id=\"deleteBy").append(primaryKeyMethodName)
			.append("\" parameterType=\"").append(primaryKeyJavaTypeName).append("\">").append(Symbol.lineStr);
			if(isJdbcInteger(columnInfo)) {
				mapperBuffer.append(Symbol.tabBy2).append("<if test=\"").append(primaryKeyJavaName).append("!=null and ")
				.append(primaryKeyJavaName).append("!=''\">").append(Symbol.lineStr);
			} else {
				mapperBuffer.append(Symbol.tabBy2).append("<if test=\"").append(primaryKeyJavaName).append("!=null and ")
				.append(primaryKeyJavaName).append("!=''\">").append(Symbol.lineStr);
			}
			mapperBuffer.append(Symbol.tabBy3).append("DELETE FROM ").append(tableName).append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("WHERE ").append(primaryKeyColumnName)
			.append("=#{").append(primaryKeyJavaName).append(",jdbcType=").append(primaryKeyJdbcType).append("}").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</delete>").append(Symbol.lineStr);
			
			//组装</delete>
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- 批量删除 By 主键 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<delete id=\"deleteBy").append(pkeyMethodListAsName).append("\" parameterType=\"java.util.List\">").append(Symbol.lineStr)
			//判空
			.append(Symbol.tabBy2).append("<if test=\"").append(pkeyListAsName).append("!=null and ")
			.append(pkeyListAsName).append(".size()>0\">").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("DELETE FROM ").append(tableName).append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<where>").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append(primaryKeyColumnName).append(" in").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("<foreach collection=\"").append(pkeyListAsName).append("\"")
			.append(" item=\"").append(primaryKeyJavaName).append("\"").append(" open=\"(\" separator=\",\" close=\")\">").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append(Symbol.tabBy1).append("#{").append(primaryKeyJavaName).append("}").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("</foreach>").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("</where>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</delete>").append(Symbol.lineStr);
			
			//组装updateById
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- 修改 By 主键 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<update id=\"updateBy").append(primaryKeyMethodName)
			.append("\" parameterType=\"").append(modelPackagePath).append("\">").append(Symbol.lineStr);
			if(isJdbcInteger(columnInfo)) {
				mapperBuffer.append(Symbol.tabBy2).append("<if test=\"").append(objectVarName).append(".").append(primaryKeyJavaName).append("!=null and ")
				.append(objectVarName).append(".").append(primaryKeyJavaName).append("!=''\">").append(Symbol.lineStr);
			} else {
				mapperBuffer.append(Symbol.tabBy2).append("<if test=\"").append(objectVarName).append(".").append(primaryKeyJavaName).append("!=null and ")
				.append(objectVarName).append(".").append(primaryKeyJavaName).append("!=''\">").append(Symbol.lineStr);
			}
			mapperBuffer.append(Symbol.tabBy3).append("UPDATE ").append(tableName).append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<set>").append(Symbol.lineStr)
			.append(getColuUpdateSet(generateModelDTO)).append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("</set>").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("WHERE ").append(primaryKeyColumnName).append(" = #{").append(objectVarName).append(".").append(primaryKeyJavaName).append("}").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</update>").append(Symbol.lineStr);
			
			//组装批量更新 updateByIds
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- 批量修改 By 主键 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<update id=\"updateBy").append(pkeyMethodListAsName).append("\" parameterType=\"java.util.List\">").append(Symbol.lineStr)
			//判空
			.append(Symbol.tabBy2).append("<if test=\"").append(objListAsName).append("!=null and ")
			.append(objListAsName).append(".size()>0\">").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<foreach collection=\"").append(objListAsName).append("\" ").append("item=\"").append(objectVarName).append("\"")
			.append(" index=\"index\" open=\"\" close=\"\" separator=\";\">").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("UPDATE ").append(tableName).append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("<set>").append(Symbol.lineStr)
			.append(getColuUpdateSets(columnInfos, columnInfo, objectVarName)).append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("</set>").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("<where>").append(Symbol.lineStr)//primaryKeyJavaName
			.append(Symbol.tabBy5).append(primaryKeyColumnName)
			.append(" = #{").append(objectVarName).append(".").append(primaryKeyJavaName).append("}").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("</where>").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("</foreach>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</update>").append(Symbol.lineStr);
			
			
			//组装findById
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- 主键查询 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<select id=\"").append("findBy").append(primaryKeyMethodName)
			.append("\" parameterType=\"").append(primaryKeyJavaTypeName).append("\" resultType=\"").append(modelPackagePath).append("\">").append(Symbol.lineStr);
			if(isJdbcInteger(columnInfo)) {
				mapperBuffer.append(Symbol.tabBy2).append("<if test=\"").append(primaryKeyJavaName).append("!=null and ")
				.append(primaryKeyJavaName).append("!=''\">").append(Symbol.lineStr);
			} else {
				mapperBuffer.append(Symbol.tabBy2).append("<if test=\"").append(primaryKeyJavaName).append("!=null and ")
				.append(primaryKeyJavaName).append("!=''\">").append(Symbol.lineStr);
			}
			mapperBuffer.append(Symbol.tabBy3).append("SELECT").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("<include refid=\"findAllColumns\"/>").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("FROM ").append(tableName).append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<where>").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append(primaryKeyColumnName).append(" = #{").append(primaryKeyJavaName).append(",jdbcType=").append(primaryKeyJdbcType).append("}").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("</where>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</select>").append(Symbol.lineStr);
			
			//组装findByIds
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- 多个主键查询 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<select id=\"").append("findBy").append(pkeyMethodListAsName)
			.append("\" parameterType=\"java.util.List\" resultType=\"").append(modelPackagePath).append("\">").append(Symbol.lineStr)
			//判空
			.append(Symbol.tabBy2).append("<if test=\"").append(pkeyListAsName).append("!=null and ")
			.append(pkeyListAsName).append(".size()>0\">").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("SELECT").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("<include refid=\"findAllColumns\"/>").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("FROM ").append(tableName).append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<where>").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append(primaryKeyColumnName).append(" in").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("<foreach collection=\"").append(pkeyListAsName).append("\"").append(" item=\"").append(primaryKeyJavaName).append("\"")
			.append(" index=\"index\" open=\"(\" separator=\",\" close=\")\">").append(Symbol.lineStr)
			.append(Symbol.tabBy5).append("#{").append(primaryKeyJavaName).append("}").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("</foreach>").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("</where>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</select>").append(Symbol.lineStr);
			
			
			//组装查询总条数findTotalRow
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- findList查询数据行数 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<select id=\"findTotalRow\" parameterType=\"").append(modelQeuryPackage).append("\" resultType=\"int\">").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("SELECT").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("COUNT(0)").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("FROM ").append(tableName).append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("<!-- 是否精确匹配, 参数不等于null, 并且等于1, 为精确匹配 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("<if test=\"isAccurate!=null and isAccurate==1\">").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<include refid=\"findAllCondition\"/>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("<!-- 是否模糊匹配, 参数等于null, 或者等于0, 为模糊匹配 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("<if test=\"isAccurate==null or isAccurate==0\">").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<include refid=\"findAllConditionVague\"/>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</select>").append(Symbol.lineStr);
			
			//组装查询分页数据findAll
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- findList查询多行数据(支持: 分页, 精确匹配, 模糊匹配) -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<select id=\"findList\" parameterType=\"")
			.append(modelQeuryPackage).append("\"").append(Symbol.lineStr)
			.append(Symbol.tabBy5).append(Symbol.tabBy5).append("resultType=\"").append(modelPackagePath).append("\">").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("SELECT").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<include refid=\"findAllColumns\"/>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("FROM ").append(tableName).append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("<!-- 是否精确匹配, 参数不等于null, 并且等于1, 为精确匹配 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("<if test=\"isAccurate!=null and isAccurate==1\">").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<include refid=\"findAllCondition\"/>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("<!-- 是否模糊匹配, 参数等于null, 或者等于0, 为模糊匹配 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("<if test=\"isAccurate==null or isAccurate==0\">").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<include refid=\"findAllConditionVague\"/>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("<if test=\"endRowNumber>0\">").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("limit #{startRowNumber,jdbcType=INTEGER},#{endRowNumber,jdbcType=INTEGER}").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</select>").append(Symbol.lineStr);
			
			
			//动态查询sql select ?,? from tableName where ?=?
			mapperBuffer.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<!-- findList 动态查询多行数据(支持: 分页, 精确匹配, 模糊匹配, 自定义字段) -->").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("<select id=\"").append("findListByDynamicColumn\" parameterType=\"")
			.append(modelQeuryPackage).append("\"").append(Symbol.lineStr)
			.append(Symbol.tabBy5).append(Symbol.tabBy5).append("resultType=\"").append(modelPackagePath).append("\">").append(Symbol.lineStr)
			
			.append(Symbol.tabBy2).append("<if test=\"columnList!=null and columnList.size()>0\">").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("SELECT").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("<foreach collection=\"columnList\"").append(" item=\"column\"")
			.append(" index=\"index\" separator=\",\">").append(Symbol.lineStr)
			//判断动态字段
			.append(getDynamicColumnQuery(columnInfos, "column")).append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("</foreach>").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("FROM ").append(tableName).append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<!-- 是否精确匹配, 参数不等于null, 并且等于1, 为精确匹配 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<if test=\"isAccurate!=null and isAccurate==true\">").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("<include refid=\"findAllCondition\"/>").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<!-- 是否模糊匹配, 参数等于null, 或者等于0, 为模糊匹配 -->").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<if test=\"isAccurate==null or isAccurate==false\">").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("<include refid=\"findAllConditionVague\"/>").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("<if test=\"endRowNumber>0\">").append(Symbol.lineStr)
			.append(Symbol.tabBy4).append("limit #{startRowNumber,jdbcType=INTEGER},#{endRowNumber,jdbcType=INTEGER}").append(Symbol.lineStr)
			.append(Symbol.tabBy3).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("</select>").append(Symbol.lineStr);
			
			
			
			
			
			
			mapperBuffer.append(Symbol.lineStr).append("</mapper>");
			return mapperBuffer.toString();
		}catch (Exception e) {
			log.error("{}",e.getMessage());
		}
		return mapperBuffer.toString();
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
	protected String getInJdbcTypeColu(GenerateModelDTO generateModelDTO) {
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
	
	
	
	/**
	 * 动态插入
	 * */
	protected String getInDynamic(GenerateModelDTO generateModelDTO) {
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
	
	
	//批量插入字段
	protected String getInBatchJdbc(List<ColumnInfo> columnInfos, String objectVarName) {
		StringBuffer jdbcColumnBuffer = new StringBuffer();
		for(ColumnInfo cif : columnInfos) {
			String callJavaName = objectVarName + "." + cif.getJavaName();
			jdbcColumnBuffer
			.append(Symbol.tabBy4)
			.append("#{").append(callJavaName).append("}").append(",").append(Symbol.lineStr);
		}
		return jdbcColumnBuffer.toString().substring(0,jdbcColumnBuffer.toString().length()-2);
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
	protected String getColuUpdateSets(List<ColumnInfo> columnInfos,ColumnInfo mainColumnInfo, String objectVarName) {
		StringBuffer coluBuffer = new StringBuffer();
		for(ColumnInfo columnInfo : columnInfos) {
			String javaName = columnInfo.getJavaName();
			String columnName = columnInfo.getName();
			String callJavaName = objectVarName + "." + javaName;
			if(!mainColumnInfo.getJavaName().equals(javaName)) {
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
	protected String getVagueFindAllSql(List<ColumnInfo> columnInfos,ColumnInfo mainColumnInfo, String objectVarName) {
		StringBuffer vagueConditionBuffer = new StringBuffer();
		
		for(ColumnInfo columnInfo : columnInfos) {
			String javaName = columnInfo.getJavaName();
			String columnName = columnInfo.getName();
			String jdbcType = columnInfo.getJdbcType();
			//组装条件的jdbc类型, 要确切的区分开 String, Integer, Date
			if(isJdbcDate(columnInfo)) {
				String javaNameStart = javaName + "Start";
				String javaNameEnd = javaName + "End";
				
				//start
				vagueConditionBuffer.append(Symbol.tabBy2)
				.append("<if test=\"").append(javaNameStart).append("!=null and ").append(javaNameStart).append("!=''\">").append(Symbol.lineStr)
				.append(Symbol.tabBy3)
				.append("<![CDATA[ AND ").append(columnName).append(" >= ").append("DATE_FORMAT(#{").append(javaNameStart).append("},'%Y-%m-%d %T:%i:%s'").append(") ]]>").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
				//end
				.append(Symbol.tabBy2).append("<if test=\"").append(javaNameEnd).append("!=null and ").append(javaNameEnd).append("!=''\">").append(Symbol.lineStr)
				.append(Symbol.tabBy3)
				.append("<![CDATA[ AND ").append(columnName).append(" <= ").append("DATE_FORMAT(#{").append(javaNameEnd).append("},'%Y-%m-%d %T:%i:%s'").append(") ]]>").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr);
			}else if(isJdbcInteger(columnInfo)) {
				if(isIf0(columnInfo)) {
					vagueConditionBuffer.append(Symbol.tabBy2)
					.append("<if test=\"").append(javaName).append("!=null\">").append(Symbol.lineStr);
				} else {
					vagueConditionBuffer.append(Symbol.tabBy2)
					.append("<if test=\"").append(javaName).append("!=null and ").append(javaName).append("!=''\">").append(Symbol.lineStr);
				}
				vagueConditionBuffer.append(Symbol.tabBy3)
				.append("AND ").append(columnName).append(" = ").append("#{").append(javaName).append(",jdbcType=").append(jdbcType).append("}").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr);
			}else {
				if(isIf0(columnInfo)) {
					vagueConditionBuffer.append(Symbol.tabBy2)
					.append("<if test=\"").append(javaName).append("!=null\">").append(Symbol.lineStr);
				} else {
					vagueConditionBuffer.append(Symbol.tabBy2)
					.append("<if test=\"").append(javaName).append("!=null and ").append(javaName).append("!=''\">").append(Symbol.lineStr);
				}
				vagueConditionBuffer.append(Symbol.tabBy3)
				.append("AND ").append(columnName).append(" LIKE ").append("CONCAT('%',#{").append(javaName).append(",jdbcType=").append(jdbcType).append("},'%')")
				.append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr);
			}
		}
		//记录问题, jdbcType  java boolean 转 jdbc时 不对 , 待确认
		return vagueConditionBuffer.toString().substring(2,vagueConditionBuffer.toString().length()-1);
	}
	
	//精确查询条件
	protected String getFindAllSql(List<ColumnInfo> columnInfos,ColumnInfo mainColumnInfo, String objectVarName) {
		StringBuffer conditionBuffer = new StringBuffer();
		
		for(ColumnInfo columnInfo : columnInfos) {
			String javaName = columnInfo.getJavaName();
			String columnName = columnInfo.getName();
			String jdbcType = columnInfo.getJdbcType();
			
			if(isJdbcDate(columnInfo)) {
				String javaNameStart = javaName + "Start";
				String javaNameEnd = javaName + "End";
				
				//start
				conditionBuffer.append(Symbol.tabBy2)
				.append("<if test=\"").append(javaNameStart).append("!=null and ").append(javaNameStart).append("!=''\">").append(Symbol.lineStr)
				.append(Symbol.tabBy2)
				.append("<![CDATA[ AND ").append(columnName).append(" >= ").append("DATE_FORMAT(#{").append(javaNameStart).append("},'%Y-%m-%d %T:%i:%s'").append(") ]]>").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr)
				//end
				.append(Symbol.tabBy2).append("<if test=\"").append(javaNameEnd).append("!=null and ").append(javaNameEnd).append("!=''\">").append(Symbol.lineStr)
				.append(Symbol.tabBy3)
				.append("<![CDATA[ AND ").append(columnName).append(" <= ").append("DATE_FORMAT(#{").append(javaNameEnd).append("},'%Y-%m-%d %T:%i:%s'").append(") ]]>").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr);
			}else if(isJdbcInteger(columnInfo)) {
				if(isIf0(columnInfo)) {
					conditionBuffer.append(Symbol.tabBy2)
					.append("<if test=\"").append(javaName).append("!=null\">").append(Symbol.lineStr);
				} else {
					conditionBuffer.append(Symbol.tabBy2)
					.append("<if test=\"").append(javaName).append("!=null and ").append(javaName).append("!=''\">").append(Symbol.lineStr);
				}
				conditionBuffer.append(Symbol.tabBy3)
				.append("AND ").append(columnName).append(" = ").append("#{").append(javaName).append(",jdbcType=").append(jdbcType).append("}").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr);
			}else {
				if(isIf0(columnInfo)) {
					conditionBuffer.append(Symbol.tabBy2)
					.append("<if test=\"").append(javaName).append("!=null\">").append(Symbol.lineStr);
				} else {
					conditionBuffer.append(Symbol.tabBy2)
					.append("<if test=\"").append(javaName).append("!=null and ").append(javaName).append("!=''\">").append(Symbol.lineStr);
				}
				conditionBuffer.append(Symbol.tabBy3)
				.append("AND ").append(columnName).append(" = ").append("#{").append(javaName).append(",jdbcType=").append(jdbcType).append("}").append(Symbol.lineStr)
				.append(Symbol.tabBy2).append("</if>").append(Symbol.lineStr);
			}
		}
		
		return conditionBuffer.toString().substring(2,conditionBuffer.toString().length()-1);
	}
	
	
	protected String getModelPrimaryKeyMapper(ColumnInfo mainColumnInfo) {
		//#{").append(primaryKeyJavaName).append(",jdbcType=").append(primaryKeyJdbcType).append("}")
		return "${"+mainColumnInfo.getJavaName()+",jdbcType="+mainColumnInfo.getJdbcType()+"}";
	}
}
