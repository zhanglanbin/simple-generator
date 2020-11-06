package com.simple.generator.assemble;

import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.config.SimpleGeneratorConfiguration;
import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleLogbackJavaConfig {
	
	@Bean
	public PrepareGennerateFile logbackJavaConfig(final SimpleGeneratorConfiguration generatorConfig) {
		InputStream swaggerJavaConfigStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\ThreadDiscriminator.txt");
		String swaggerJavaConfigText = GeneratorUtils.readTxt(swaggerJavaConfigStream);
		swaggerJavaConfigText = swaggerJavaConfigText
				.replace("${generator.logbackConfigPackagePath}", generatorConfig.getLogbackConfigPackagePath())
				.replace("${generator.logbackConfigClassName}", generatorConfig.getLogbackConfigClassName());
		
		PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
		gennerateFile.setFileName(generatorConfig.getLogbackConfigClassName());
		gennerateFile.setFilePath(GeneratorUtils.analysisFilePath(generatorConfig.getProjectPath(), generatorConfig.getLogbackConfigPackagePath()));
		gennerateFile.setFileSuffix("java");
		gennerateFile.setText(swaggerJavaConfigText);
		
		return gennerateFile;
	}
}
