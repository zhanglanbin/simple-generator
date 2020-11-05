package com.simple.generator.assemble;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.service.AssembleAnalysisService;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleModelQurey {
	
	@Autowired
	private AssembleAnalysisService modelQueryAnalysisServiceImpl;
	@Autowired
	private AssembleJavaText assembleJavaText;
	
	@Bean
	public List<PrepareGennerateFile> modelQureyList(List<GenerateModelDTO> generateModelDTOList){
		List<PrepareGennerateFile> modelQurey = new ArrayList<>();
		
		String modelQueryTxt = GeneratorUtils.getCodeTemplate("\\generator-template\\model\\ModelQuery.txt");
		
		for (GenerateModelDTO generateModelDTO : generateModelDTOList) {
			PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
			
			String javaTxt = assembleJavaText.getJavaText(modelQueryTxt, generateModelDTO, modelQueryAnalysisServiceImpl);
			
			gennerateFile.setFileName(generateModelDTO.getModelQueryClassName());
			gennerateFile.setFilePath(generateModelDTO.getModelQueryJavaFilePath());
			gennerateFile.setFileSuffix("java");
			gennerateFile.setText(javaTxt);
			modelQurey.add(gennerateFile);
		}
		return modelQurey;
	}
	
}
