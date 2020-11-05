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
public class AssembleModel {

	@Autowired
	private AssembleAnalysisService modelAnalysisServiceImpl;
	@Autowired
	private AssembleJavaText assembleJavaText;
	
	@Bean
	public List<PrepareGennerateFile> modelList(List<GenerateModelDTO> generateModelDTOList) {
		List<PrepareGennerateFile> modelList = new ArrayList<>();
		
		String modelTxt = GeneratorUtils.getCodeTemplate("\\generator-template\\model\\Model.txt");
		
		for (GenerateModelDTO generateModelDTO : generateModelDTOList) {
			PrepareGennerateFile gennerateFile = new PrepareGennerateFile();
			
			String javaTxt = assembleJavaText.getJavaText(modelTxt, generateModelDTO, modelAnalysisServiceImpl);
			
			if(null==javaTxt || "".equals(javaTxt)) {
				continue ;
			}
			
			gennerateFile.setFileName(generateModelDTO.getModelClassName());
			gennerateFile.setFilePath(generateModelDTO.getModelJavaFilePath());
			gennerateFile.setFileSuffix("java");
			gennerateFile.setText(javaTxt);
			modelList.add(gennerateFile);
		}
		return modelList;
	}
}
