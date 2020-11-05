package com.simple.generator.assemble;

import java.io.File;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.config.GeneratorConfig;
import com.simple.generator.constant.PathConstant;
import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleApplicationYmlConfig {
	@Bean
	public PrepareGennerateFile applicationYml(final GeneratorConfig generatorConfig) {
		InputStream mainAppStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\applicationYml.txt");
		String mainAppText = GeneratorUtils.readTxt(mainAppStream);
		mainAppText = mainAppText
				.replace("${generator.modelPackagePath}", generatorConfig.getModelPackagePath())
				.replace("${generator.groupId}", generatorConfig.getGroupId())
				.replace("${generator.databaseUrl}", generatorConfig.getDatabaseUrl())
				.replace("${generator.databaseUsername}", generatorConfig.getDatabaseUsername())
				.replace("${generator.databasePassword}", generatorConfig.getDatabasePassword())
				.replace("${generator.projectName}", generatorConfig.getProjectName());
		
		PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
		gennerateFile.setFileName("application");
		gennerateFile.setFilePath(generatorConfig.getProjectPath() + File.separatorChar + PathConstant.mainResourcesPath);
		gennerateFile.setFileSuffix("yml");
		gennerateFile.setText(mainAppText);
		
		return gennerateFile;
	}
}
