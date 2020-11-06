package com.simple.generator.assemble;

import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleMainApp {
	@Bean
	public PrepareGennerateFile mainApp(final SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO) {
		InputStream mainAppStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\mainApp.txt");
		String mainAppText = GeneratorUtils.readTxt(mainAppStream);
		mainAppText = mainAppText
				.replace("${generator.groupId}", simpleGeneratorConfigurationDTO.getGroupId())
				.replace("${generator.daoTopPackagePath}", simpleGeneratorConfigurationDTO.getDaoPackagePath());
		
		PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
		gennerateFile.setFileName("MainApp");
		gennerateFile.setFilePath(GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), simpleGeneratorConfigurationDTO.getGroupId()));
		gennerateFile.setFileSuffix("java");
		gennerateFile.setText(mainAppText);
		
		return gennerateFile;
	}
}
