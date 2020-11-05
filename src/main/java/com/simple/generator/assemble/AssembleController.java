package com.simple.generator.assemble;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.service.impl.ControllerAnalysisServiceImpl;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleController {
	
	@Autowired
	private ControllerAnalysisServiceImpl controllerAnalysisServiceImpl;
	
	@Bean
	public List<PrepareGennerateFile> controllerList(List<GenerateModelDTO> generateModelDTOList){
		List<PrepareGennerateFile> controller = new ArrayList<>();
		
		String controllerText = GeneratorUtils.getCodeTemplate("\\generator-template\\controller.txt");
		
		for (GenerateModelDTO generateModelDTO : generateModelDTOList) {
			PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
			
			String analysis = controllerAnalysisServiceImpl.analysis(controllerText, generateModelDTO);
			
			gennerateFile.setFileName(generateModelDTO.getControllerClassName());
			gennerateFile.setFilePath(generateModelDTO.getControllerJavaFilePath());
			gennerateFile.setFileSuffix("java");
			gennerateFile.setText(analysis);
			controller.add(gennerateFile);
		}
		return controller;
	}
	
}
