package com.simple.generator.assemble;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssemblePom {
	
	@Bean
	public List<PrepareGennerateFile> pom(SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO){
		List<PrepareGennerateFile> pom = new ArrayList<>();
		
		String pomText = GeneratorUtils.getCodeTemplate("\\generator-template\\pom.txt");
		
		PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
		
		pomText = pomText.replace("${generator.groupId}", simpleGeneratorConfigurationDTO.getGroupId())
				.replace("${generator.projectName}", simpleGeneratorConfigurationDTO.getProjectName())
				.replace("${generator.projectPath}", simpleGeneratorConfigurationDTO.getProjectPath())
				.replace("${generator.javaJdkVersion}", simpleGeneratorConfigurationDTO.getJavaJdkVersion());
		
		gennerateFile.setFileName("pom");
		gennerateFile.setFilePath(simpleGeneratorConfigurationDTO.getProjectPath());
		gennerateFile.setFileSuffix("xml");
		gennerateFile.setText(pomText);
		pom.add(gennerateFile);
		return pom;
	}
	
}
