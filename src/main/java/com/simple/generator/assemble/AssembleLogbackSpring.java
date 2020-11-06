package com.simple.generator.assemble;

import java.io.File;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.simple.generator.constant.PathConstant;
import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.utils.GeneratorUtils;

@Component
public class AssembleLogbackSpring {
	@Bean
	public PrepareGennerateFile logbackSpringXml(final SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO) {
		InputStream mainAppStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\logbackSpringXml.txt");
		String mainAppText = GeneratorUtils.readTxt(mainAppStream);
		mainAppText = mainAppText
				.replace("${generator.groupId}", simpleGeneratorConfigurationDTO.getGroupId())
				.replace("${generator.logbackConfigPackagePath}", simpleGeneratorConfigurationDTO.getLogbackConfigPackagePath())
				.replace("${generator.logbackConfigClassName}", simpleGeneratorConfigurationDTO.getLogbackConfigClassName());
		
		PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
		gennerateFile.setFileName(simpleGeneratorConfigurationDTO.getLogbackXmlName());
		gennerateFile.setFilePath(simpleGeneratorConfigurationDTO.getProjectPath() + File.separatorChar + PathConstant.mainResourcesPath);
		gennerateFile.setFileSuffix("xml");
		gennerateFile.setText(mainAppText);
		
		return gennerateFile;
	}
}
