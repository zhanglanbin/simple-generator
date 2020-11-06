package com.simple.generator.assemble;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.config.SimpleGeneratorConfiguration;
import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssemblePom {
	
	@Bean
	public List<PrepareGennerateFile> pom(SimpleGeneratorConfiguration generatorConfig){
		List<PrepareGennerateFile> pom = new ArrayList<>();
		
		String pomText = GeneratorUtils.getCodeTemplate("\\generator-template\\pom.txt");
		
		PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
		
		pomText = pomText.replace("${generator.groupId}", generatorConfig.getGroupId())
				.replace("${generator.projectName}", generatorConfig.getProjectName())
				.replace("${generator.projectPath}", generatorConfig.getProjectPath())
				.replace("${generator.javaJdkVersion}", generatorConfig.getJavaJdkVersion());
		
		gennerateFile.setFileName("pom");
		gennerateFile.setFilePath(generatorConfig.getProjectPath());
		gennerateFile.setFileSuffix("xml");
		gennerateFile.setText(pomText);
		pom.add(gennerateFile);
		return pom;
	}
	
}
