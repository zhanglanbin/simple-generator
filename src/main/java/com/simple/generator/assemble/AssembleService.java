package com.simple.generator.assemble;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.service.impl.ServiceAnalysisServiceImpl;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleService {
	
	@Autowired
	private ServiceAnalysisServiceImpl serviceAnalysisServiceImpl;
	
	@Bean
	public List<PrepareGennerateFile> serviceList(List<GenerateModelDTO> generateModelDTOList){
		List<PrepareGennerateFile> service = new ArrayList<>();
		
		String serviceText = GeneratorUtils.getCodeTemplate("\\generator-template\\service.txt");
		
		for (GenerateModelDTO generateModelDTO : generateModelDTOList) {
			PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
			
			String analysis = serviceAnalysisServiceImpl.analysis(serviceText, generateModelDTO);
			
			gennerateFile.setFileName(generateModelDTO.getServiceClassName());
			gennerateFile.setFilePath(generateModelDTO.getServiceJavaFilePath());
			gennerateFile.setFileSuffix("java");
			gennerateFile.setText(analysis);
			service.add(gennerateFile);
		}
		return service;
	}
	
}
