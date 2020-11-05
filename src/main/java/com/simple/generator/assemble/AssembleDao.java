package com.simple.generator.assemble;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.constant.Symbol;
import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleDao {
	
	private static final Logger log = LoggerFactory.getLogger(AssembleDao.class);

	@Bean
	public List<PrepareGennerateFile> daoList(List<GenerateModelDTO> generateModelDTOList) {
		List<PrepareGennerateFile> modelList = new ArrayList<>();
		InputStream daoStream = null;
		daoStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\dao.txt");
		String daoTxt = GeneratorUtils.readTxt(daoStream);
//		System.out.println(daoTxt);
		try {
			daoStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (GenerateModelDTO generateModelDTO : generateModelDTOList) {
			
			String daoTxtTemp = daoTxt.replace("${generator.daoPackagePath}", generateModelDTO.getDaoPackagePath())
					.replace("${generator.daoClassName}", generateModelDTO.getDaoClassName())
					.replace("${generator.explain}", generateModelDTO.getExplain())
					.replace("${generator.author}", generateModelDTO.getAuthor())
					.replace("${generator.date}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
					.replace("${generator.modelPackage}", generateModelDTO.getModelPackage())
					.replace("${generator.modelClassName}", generateModelDTO.getModelClassName())
					.replace("${generator.modelPrimaryKey}", generateModelDTO.getModelPrimaryKeyInfo().getJavaName())
					.replace("${generator.modelVariableName}", generateModelDTO.getModelVariableName())
					.replace("${generator.modelListVariableName}", generateModelDTO.getModelListVariableName())
					.replace("${generator.modelPrimaryKeyMethodName}", generateModelDTO.getDaoModelPrimaryKeyMethodName())
					.replace("${generator.modelPrimaryKeyJavaType}",generateModelDTO.getModelPrimaryKeyInfo().getJavaType().getSimpleName())
					.replace("${generator.modelPrimaryKeyListMethodName}", generateModelDTO.getDaoModelPrimaryKeyListMethodName())
					.replace("${generator.modelPrimaryKeyVariableList}", generateModelDTO.getDaoModelPrimaryKeyListParamVariableName())
					.replace("${generator.modelQueryPackage}", generateModelDTO.getModelQueryPackage())
					.replace("${generator.modelQueryClassName}", generateModelDTO.getModelQueryClassName())
					.replace("${generator.modelQueryVariableName}", generateModelDTO.getModelQueryVariableName());
			
//			System.out.println(daoTxtTemp);
			
			PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
			
//			String daoText = getDaoText(generateModelDTO);
//			
//			if(null==daoText || "".equals(daoText)) {
//				continue ;
//			}
			
			gennerateFile.setFileName(generateModelDTO.getDaoClassName());
			gennerateFile.setFilePath(generateModelDTO.getDaoJavaFilePath());
			gennerateFile.setFileSuffix("java");
			gennerateFile.setText(daoTxtTemp);
			modelList.add(gennerateFile);
		}
		return modelList;
	}
	
	public static String getDaoText(GenerateModelDTO generateModelDTO) {
		if(null==generateModelDTO) {
			return null;
		}
		StringBuffer daoBuffer = new StringBuffer();
		try {
			//组装包路径
			daoBuffer.append("package ").append(generateModelDTO.getDaoPackagePath()).append(";").append(Symbol.lineStr);
			
			//组装导包
			daoBuffer.append(assImport(generateModelDTO));
			
			//组装注释文档
			daoBuffer.append(Symbol.lineStr)
			.append(GeneratorUtils.getFileNotes(generateModelDTO.getModelClassName(),generateModelDTO.getExplain(),generateModelDTO.getAuthor())).append(Symbol.lineStr);
			
			//组装类头部
			daoBuffer.append("public interface ").append(generateModelDTO.getModelClassName()).append(" {").append(Symbol.lineStr);
			
			//组装方法
			String ObjectName = generateModelDTO.getModelClassName();
			String ObjectVarName = GeneratorUtils.firstCharToLowerCase(ObjectName);
			String ObjectNameQuery = generateModelDTO.getModelQueryClassName();
			String ObjectNameQueryVar = GeneratorUtils.firstCharToLowerCase(ObjectNameQuery);
			String primaryKeyJavaArrName  = generateModelDTO.getModelPrimaryKeyInfo().getJavaName();
			String primaryKeyMethodName = GeneratorUtils.firstCharToUpperCase(primaryKeyJavaArrName);
			String primaryKeyJavaClassName = generateModelDTO.getModelPrimaryKeyInfo().getJavaType().getSimpleName();
			//list对象别名
			String objListAsName = ObjectVarName + "List";
			//list主键别名
			String pkeyListAsName = primaryKeyJavaArrName + "List";
			//list主键方法名
			String pkeyMethodListAsName = primaryKeyMethodName + "List";
			
			
			daoBuffer
			//insert
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public int insert(")
			.append(ObjectName).append(" ").append(ObjectVarName).append(");").append(Symbol.lineStr)
			
			//insertAllColumn
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public int insertDynamic(")
			.append(ObjectName).append(" ").append(ObjectVarName).append(");").append(Symbol.lineStr)
			
			//insertBatch
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public int insertBatch(@Param(\"").append(objListAsName).append("\") ")
			.append("List<").append(ObjectName).append("> ").append(objListAsName).append(");").append(Symbol.lineStr)
			
			//deleteByPrimaryKey
//			.append(Symbol.lineStr)
//			.append(Symbol.tabBy1).append("public int deleteBy").append(primaryKeyMethodName).append("(@Param(\"").append(primaryKeyJavaArrName).append("\") ")
//			.append(primaryKeyJavaClassName).append(" ").append(primaryKeyJavaArrName).append(");").append(Symbol.lineStr)
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public int deleteBy").append(primaryKeyMethodName).append("(")
			.append(primaryKeyJavaClassName).append(" ").append(primaryKeyJavaArrName).append(");").append(Symbol.lineStr)
			
			//deleteByPrimaryKeys
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public int deleteBy").append(pkeyMethodListAsName).append("(@Param(\"").append(pkeyListAsName).append("\") ")
			.append("List<").append(primaryKeyJavaClassName).append("> ")
			.append(pkeyListAsName).append(");").append(Symbol.lineStr)
			
			//updateByPrimaryKey
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public int updateBy").append(primaryKeyMethodName).append("(")
			.append(ObjectName).append(" ").append(ObjectVarName).append(");").append(Symbol.lineStr)
			
			//updateByPrimaryKeys
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public int updateBy").append(pkeyMethodListAsName).append("(@Param(\"").append(objListAsName).append("\") List<").append(ObjectName).append("> ")
			.append(objListAsName).append(");").append(Symbol.lineStr)
			
			//findByPrimaryKey
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public ").append(ObjectName).append(" findBy").append(primaryKeyMethodName).append("(")
			.append(primaryKeyJavaClassName).append(" ").append(primaryKeyJavaArrName).append(");").append(Symbol.lineStr)
			
			//findByPrimaryKeys
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public List<").append(ObjectName).append("> findBy").append(pkeyMethodListAsName)
			.append("(@Param(\"").append(pkeyListAsName).append("\") List<").append(primaryKeyJavaClassName).append("> ").append(pkeyListAsName).append(");").append(Symbol.lineStr)
			
			//findTotalRow
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public int findTotalRow(")
			.append(ObjectNameQuery).append(" ").append(ObjectNameQueryVar).append(");").append(Symbol.lineStr)
			
			//findAll
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public List<").append(ObjectName)
			.append("> findList(").append(ObjectNameQuery).append(" ").append(ObjectNameQueryVar).append(");").append(Symbol.lineStr)
			
			//findListByDynamicColumn
			.append(Symbol.lineStr)
			.append(Symbol.tabBy1).append("public List<").append(ObjectName)
			.append("> findListByDynamicColumn(").append(ObjectNameQuery).append(" ").append(ObjectNameQueryVar).append(");").append(Symbol.lineStr);
			
			
			daoBuffer.append(Symbol.lineStr).append("}");
			return daoBuffer.toString();
		}catch (Exception e) {
			log.error("{}",e.getMessage());
		}
		return daoBuffer.toString();
	}
	
	
	/**
	 * 组装导包
	 * */
	protected static String assImport(GenerateModelDTO generateModelDTO) {
		StringBuffer importBuffer = new StringBuffer();
		
		importBuffer
		.append(Symbol.lineStr)
		.append("import java.util.List;").append(Symbol.lineStr)
//		.append("import java.util.Map;").append(Symbol.lineStr)
		.append(Symbol.lineStr)
		.append("import org.apache.ibatis.annotations.Param;").append(Symbol.lineStr)
//		.append(Symbol.lineStr)
//		.append("import org.apache.ibatis.annotations.Param;").append(Symbol.lineStr)
		//导入对应实体类
		.append(Symbol.lineStr)
		.append("import ").append(generateModelDTO.getModelPackagePath()).append(".").append(generateModelDTO.getModelClassName()).append(";").append(Symbol.lineStr)
		
		.append(Symbol.lineStr)
		.append("import ").append(generateModelDTO.getModelQueryPackagePath()).append(".").append(generateModelDTO.getModelQueryClassName()).append(";").append(Symbol.lineStr);
		
		return importBuffer.toString();
	}
}
