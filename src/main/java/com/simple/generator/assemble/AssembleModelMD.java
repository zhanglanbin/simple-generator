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
import com.simple.generator.pojo.ColumnInfo;
import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleModelMD  {

	private static final Logger log = LoggerFactory.getLogger(AssembleModelMD.class);

	@Bean
	public List<PrepareGennerateFile> modelMDList(List<GenerateModelDTO> generateModelDTOList) {
		List<PrepareGennerateFile> modelMDList = new ArrayList<>();
		
		InputStream modelMDStream = null;
		String modelMDTxt = null;
		try {
			modelMDStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\model\\ModelMD.txt");
			if(null == modelMDStream) {
				return modelMDList;
			}
			
			modelMDTxt = GeneratorUtils.readTxt(modelMDStream);
			if(null==modelMDTxt || "".equals(modelMDTxt)) {
				return modelMDList;
			}
//			System.out.println(daoTxt);
			
		} catch (Exception e) {
			log.error("{}", e);
			return modelMDList;
		} finally {
			try {
				modelMDStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (GenerateModelDTO generateModelDTO : generateModelDTOList) {
			try {
				String modelMDTxtTemp = modelMDTxt.replace("${generator.modelMDPackagePath}", generateModelDTO.getModelMDPackagePath())
						.replace("${generator.modelMDClassName}", generateModelDTO.getModelMDClassName())
						.replace("${generator.explain}", generateModelDTO.getExplain())
						.replace("${generator.author}", generateModelDTO.getAuthor())
						.replace("${generator.date}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
						.replace("${generator.modelMDAttribute}", assemblePropertyName(generateModelDTO));
				
				PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
				gennerateFile.setFileName(generateModelDTO.getModelMDClassName());
				gennerateFile.setFilePath(generateModelDTO.getModelMDJavaFilePath());
				gennerateFile.setFileSuffix("java");
				gennerateFile.setText(modelMDTxtTemp);
				
				modelMDList.add(gennerateFile);
			} catch (Exception e) {
				log.error("{}", e);
			}
		}
		return modelMDList;
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

	/**
	 * 组装数据列名常量
	 */
	protected String assembleColumnName(GenerateModelDTO generateModelDTO) {
		StringBuffer InterAttrBuffer = new StringBuffer();
		InterAttrBuffer.append(Symbol.lineStr).append(GeneratorUtils.getModelColNotes()).append(Symbol.lineStr).append(Symbol.tabBy1).append("public interface ColumnName {")
		        .append(Symbol.lineStr);
		for (ColumnInfo columnInfo : generateModelDTO.getColumnInfos()) {
			String attributeNotes = GeneratorUtils.getModelInterAttrNotes(columnInfo.getExplain());
			// 组装注释
			InterAttrBuffer.append(Symbol.lineStr).append(attributeNotes);

			// 组装常量值
			InterAttrBuffer.append(Symbol.lineStr).append(Symbol.tabBy1).append(Symbol.tabBy1).append("public static final String ").append(columnInfo.getName().toUpperCase())
			        .append(" = \"").append(columnInfo.getName()).append("\";").append(Symbol.lineStr);
		}
		InterAttrBuffer.append(Symbol.tabBy1).append("}");
		return InterAttrBuffer.toString();
	}

	protected boolean isJdbcDate(ColumnInfo columnInfo) {
		if (columnInfo.getJavaType().getName().equals(Date.class.getName())) {
			return true;
		} else {
			return false;
		}
	}
}
