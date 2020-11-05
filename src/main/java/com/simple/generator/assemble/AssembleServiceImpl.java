package com.simple.generator.assemble;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.service.impl.ServiceImplAnalysisServiceImpl;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleServiceImpl {
	
	@Autowired
	private ServiceImplAnalysisServiceImpl serviceImplAnalysisServiceImpl;
	
	@Bean
	public List<PrepareGennerateFile> serviceImplList(List<GenerateModelDTO> generateModelDTOList){
		List<PrepareGennerateFile> serviceImpl = new ArrayList<>();
		
		String serviceImplText = GeneratorUtils.getCodeTemplate("\\generator-template\\serviceImpl.txt");
		
		for (GenerateModelDTO generateModelDTO : generateModelDTOList) {
			PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
			
			String analysis = serviceImplAnalysisServiceImpl.analysis(serviceImplText, generateModelDTO);
			
			gennerateFile.setFileName(generateModelDTO.getServiceImplClassName());
			gennerateFile.setFilePath(generateModelDTO.getServiceImplJavaFilePath());
			gennerateFile.setFileSuffix("java");
			gennerateFile.setText(analysis);
			serviceImpl.add(gennerateFile);
		}
		return serviceImpl;
	}
	
}
