package com.simple.generator.assemble;

import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleLogbackJavaConfig {
	
	@Bean
	public PrepareGennerateFile logbackJavaConfig(final SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO) {
		InputStream swaggerJavaConfigStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\ThreadDiscriminator.txt");
		String swaggerJavaConfigText = GeneratorUtils.readTxt(swaggerJavaConfigStream);
		swaggerJavaConfigText = swaggerJavaConfigText
				.replace("${generator.logbackConfigPackagePath}", simpleGeneratorConfigurationDTO.getLogbackConfigPackagePath())
				.replace("${generator.logbackConfigClassName}", simpleGeneratorConfigurationDTO.getLogbackConfigClassName());
		
		PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
		gennerateFile.setFileName(simpleGeneratorConfigurationDTO.getLogbackConfigClassName());
		gennerateFile.setFilePath(GeneratorUtils.analysisFilePath(simpleGeneratorConfigurationDTO.getProjectPath(), simpleGeneratorConfigurationDTO.getLogbackConfigPackagePath()));
		gennerateFile.setFileSuffix("java");
		gennerateFile.setText(swaggerJavaConfigText);
		
		return gennerateFile;
	}
}
