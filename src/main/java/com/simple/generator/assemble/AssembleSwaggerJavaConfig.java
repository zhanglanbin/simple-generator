package com.simple.generator.assemble;

import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.config.GeneratorConfig;
import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleSwaggerJavaConfig {
	
	@Bean
	public PrepareGennerateFile swaggerConfig(final GeneratorConfig generatorConfig) {
		InputStream swaggerJavaConfigStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\swaggerJavaConfig.txt");
		String swaggerJavaConfigText = GeneratorUtils.readTxt(swaggerJavaConfigStream);
		swaggerJavaConfigText = swaggerJavaConfigText
				.replace("${generator.groupId}", generatorConfig.getGroupId())
				.replace("${generator.controllerTopPackagePath}", generatorConfig.getControllerPackageFilePath());
		
		PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
		gennerateFile.setFileName("SwaggerConfig");
		gennerateFile.setFilePath(GeneratorUtils.analysisFilePath(generatorConfig.getProjectPath(), generatorConfig.getGroupId()+".config.swagger"));
		gennerateFile.setFileSuffix("java");
		gennerateFile.setText(swaggerJavaConfigText);
		
		return gennerateFile;
	}
}