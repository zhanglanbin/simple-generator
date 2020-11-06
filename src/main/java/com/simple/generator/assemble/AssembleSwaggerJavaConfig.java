package com.simple.generator.assemble;

import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleSwaggerJavaConfig {
	
	@Bean
	public PrepareGennerateFile swaggerJavaConfig(final SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO) {
		InputStream swaggerJavaConfigStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\swaggerJavaConfig.txt");
		String swaggerJavaConfigText = GeneratorUtils.readTxt(swaggerJavaConfigStream);
		swaggerJavaConfigText = swaggerJavaConfigText
				.replace("${generator.groupId}", simpleGeneratorConfigurationDTO.getGroupId())
				.replace("${generator.swaggerConfigClassName}", simpleGeneratorConfigurationDTO.getSwaggerConfigClassName())
				.replace("${generator.controllerTopPackagePath}", simpleGeneratorConfigurationDTO.getControllerPackageFilePath());
		
		PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
		gennerateFile.setFileName(simpleGeneratorConfigurationDTO.getSwaggerConfigClassName());
		gennerateFile.setFilePath(GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), simpleGeneratorConfigurationDTO.getSwaggerConfigPackagePath()));
		gennerateFile.setFileSuffix("java");
		gennerateFile.setText(swaggerJavaConfigText);
		
		return gennerateFile;
	}
}
