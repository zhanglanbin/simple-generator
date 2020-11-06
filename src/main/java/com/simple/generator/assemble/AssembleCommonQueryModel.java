package com.simple.generator.assemble;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.utils.GeneratorUtils;

@Component
public class AssembleCommonQueryModel {

	@Bean
	public PrepareGennerateFile commonQueryModel(final SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO) {
		InputStream commonQueryParamStream = null;
		if(simpleGeneratorConfigurationDTO.isSwaggerAnnotation()) {
			commonQueryParamStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\model\\swagger\\CommonQueryParamBySwagger.txt");
		} else {
			commonQueryParamStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\model\\CommonQueryParam.txt");
		}
		String commonQueryParamTxt = GeneratorUtils.readTxt(commonQueryParamStream);
		commonQueryParamTxt = commonQueryParamTxt
				.replace("${generator.modelPackagePath}", simpleGeneratorConfigurationDTO.getModelPackagePath())
				.replace("${generator.author}", simpleGeneratorConfigurationDTO.getAuthor())
				.replace("${generator.date}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.replace("${generator.serialVersionUID}", GeneratorUtils.getSerialVersionUID().toString());
		
		PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
		gennerateFile.setFileName("CommonQueryParam");
		gennerateFile.setFilePath(GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), simpleGeneratorConfigurationDTO.getModelPackagePath()));
		gennerateFile.setFileSuffix("java");
		gennerateFile.setText(commonQueryParamTxt);
		
		return gennerateFile;
	}
}
