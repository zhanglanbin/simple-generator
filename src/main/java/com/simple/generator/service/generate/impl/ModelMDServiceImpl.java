package com.simple.generator.service.generate.impl;

import java.io.File;
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
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.service.generate.GenerateService;
import com.simple.generator.service.generatro.GeneratroService;
import com.simple.generator.utils.GeneratorUtils;

@Service
public class ModelMDServiceImpl implements GenerateService {
	
	private static final Logger log = LoggerFactory.getLogger(ModelMDServiceImpl.class);

	@Autowired
	private GeneratroService generatroService;
	@Resource
	private List<GenerateModelDTO> generateModelDTOList;
	
	@Override
	public boolean generate() {
		
		for (GenerateModelDTO generateModelDTO : generateModelDTOList) {
			try {
				String writerPath = generateModelDTO.getModelMDJavaFilePath() + File.separatorChar + generateModelDTO.getModelMDClassName()+".java";
				Context context = new Context();
				context.setVariable("generateModelDTO", generateModelDTO);
				context.setVariable("notesDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				context.setVariable("propertyName", assemblePropertyName(generateModelDTO));
				boolean result = generatroService.templateAnalysisGenerateFile(context, writerPath, PathConstant.generatorTemplatePath+"model/", "ModelMD");
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
	 * 组装属性名常量
	 */
	protected String assemblePropertyName(GenerateModelDTO generateModelDTO) {
		StringBuffer InterAttrBuffer = new StringBuffer();
		// InterAttrBuffer.append(lineStr).append(GeneratorUtils.getModelInterNotes()).append(lineStr);
		// .append(tabStr).append("public interface PropertyName {").append(lineStr);
		for (ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			String attributeNotes = GeneratorUtils.getModelInterAttrNotes(columnInfo.getExplain());
			// 组装注释
			InterAttrBuffer.append(Symbol.lineStr).append(attributeNotes);

			// 组装常量值
			InterAttrBuffer.append(Symbol.lineStr).append(Symbol.tabBy1).append("public static final String ").append(columnInfo.getName().toUpperCase()).append(" = \"")
			        .append(columnInfo.getJavaName()).append("\";").append(Symbol.lineStr);

			
			// 时间类型追加 开始和结束
			if (isJdbcDate(columnInfo)) {
				String dateStart = columnInfo.getJavaName() + "Start";
				String dateEnd = columnInfo.getJavaName() + "End";
				String columnStart = columnInfo.getName() + "_Start";
				String columnEnd = columnInfo.getName() + "_End";

				InterAttrBuffer.append(Symbol.lineStr).append(Symbol.tabBy1).append("public static final String ").append(columnStart.toUpperCase()).append(" = \"")
				        .append(dateStart).append("\";").append(Symbol.lineStr);

				InterAttrBuffer.append(Symbol.lineStr).append(Symbol.tabBy1).append("public static final String ").append(columnEnd.toUpperCase()).append(" = \"")
				        .append(dateEnd).append("\";").append(Symbol.lineStr);
			}
		}
		// InterAttrBuffer.append(tabStr).append("}");
		return InterAttrBuffer.toString().substring(0, InterAttrBuffer.toString().length()-1);
	}
	
	
	protected boolean isJdbcDate(ColumnInfo columnInfo) {
		if (columnInfo.getJavaType().getName().equals(Date.class.getName())) {
			return true;
		} else {
			return false;
		}
	}
}
