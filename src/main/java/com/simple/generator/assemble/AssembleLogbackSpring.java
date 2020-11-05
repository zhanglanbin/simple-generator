package com.simple.generator.assemble;

import java.io.File;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.simple.generator.config.GeneratorConfig;
import com.simple.generator.constant.PathConstant;
import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.utils.GeneratorUtils;

@Component
public class AssembleLogbackSpring {
	@Bean
	public PrepareGennerateFile logbackSpringXml(final GeneratorConfig generatorConfig) {
		InputStream mainAppStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\logbackSpringXml.txt");
		String mainAppText = GeneratorUtils.readTxt(mainAppStream);
		mainAppText = mainAppText
				.replace("${generator.groupId}", generatorConfig.getGroupId())
				.replace("${generator.logbackConfigPackagePath}", generatorConfig.getLogbackConfigPackagePath())
				.replace("${generator.logbackConfigClassName}", generatorConfig.getLogbackConfigClassName());
		
		PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
		gennerateFile.setFileName(generatorConfig.getLogbackXmlName());
		gennerateFile.setFilePath(generatorConfig.getProjectPath() + File.separatorChar + PathConstant.mainResourcesPath);
		gennerateFile.setFileSuffix("xml");
		gennerateFile.setText(mainAppText);
		
		return gennerateFile;
	}
}
