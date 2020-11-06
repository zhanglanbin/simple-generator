package com.simple.generator.assemble;

import java.io.File;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.constant.PathConstant;
import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleApplicationYmlConfig {
	@Bean
	public PrepareGennerateFile applicationYml(final SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO) {
		InputStream mainAppStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\applicationYml.txt");
		String mainAppText = GeneratorUtils.readTxt(mainAppStream);
		mainAppText = mainAppText
				.replace("${generator.modelPackagePath}", simpleGeneratorConfigurationDTO.getModelPackagePath())
				.replace("${generator.groupId}", simpleGeneratorConfigurationDTO.getGroupId())
				.replace("${generator.databaseUrl}", simpleGeneratorConfigurationDTO.getDatabaseUrl())
				.replace("${generator.databaseUsername}", simpleGeneratorConfigurationDTO.getDatabaseUsername())
				.replace("${generator.databasePassword}", simpleGeneratorConfigurationDTO.getDatabasePassword())
				.replace("${generator.projectName}", simpleGeneratorConfigurationDTO.getProjectName());
		
		PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
		gennerateFile.setFileName("application");
		gennerateFile.setFilePath(simpleGeneratorConfigurationDTO.getProjectPath() + File.separatorChar + PathConstant.mainResourcesPath);
		gennerateFile.setFileSuffix("yml");
		gennerateFile.setText(mainAppText);
		
		return gennerateFile;
	}
}
