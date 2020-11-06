package com.simple.generator.assemble;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simple.generator.config.SimpleGeneratorConfiguration;
import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.utils.GeneratorUtils;

@Configuration
public class AssembleResponseModel {

	@Bean
	public List<PrepareGennerateFile> responseModelList(final SimpleGeneratorConfiguration generatorConfig) {
		
		List<PrepareGennerateFile> responseModelList = new ArrayList<>();
		
		InputStream myMessageStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\response\\MyMessage.txt");
		String myMessageTxt = GeneratorUtils.readTxt(myMessageStream);
		myMessageTxt = myMessageTxt.replace("${generator.groupId}", generatorConfig.getGroupId())
				.replace("${generator.author}", generatorConfig.getAuthor())
				.replace("${generator.date}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.replace("${generator.serialVersionUID}", GeneratorUtils.getSerialVersionUID().toString());
		
		InputStream pageDataStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\response\\Page.txt");
		String pageDataTxt = GeneratorUtils.readTxt(pageDataStream);
		pageDataTxt = pageDataTxt.replace("${generator.groupId}", generatorConfig.getGroupId())
				.replace("${generator.author}", generatorConfig.getAuthor())
				.replace("${generator.date}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.replace("${generator.serialVersionUID}", GeneratorUtils.getSerialVersionUID().toString());
		
		InputStream responseDataStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\response\\ResponseResult.txt");
		String responseDataTxt = GeneratorUtils.readTxt(responseDataStream);
		responseDataTxt = responseDataTxt.replace("${generator.groupId}", generatorConfig.getGroupId())
				.replace("${generator.author}", generatorConfig.getAuthor())
				.replace("${generator.date}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.replace("${generator.serialVersionUID}", GeneratorUtils.getSerialVersionUID().toString());
		
		InputStream stateStream = ClassLoader.getSystemResourceAsStream("\\generator-template\\response\\State.txt");
		String stateTxt = GeneratorUtils.readTxt(stateStream);
		stateTxt = stateTxt.replace("${generator.groupId}", generatorConfig.getGroupId())
				.replace("${generator.author}", generatorConfig.getAuthor())
				.replace("${generator.date}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.replace("${generator.serialVersionUID}", GeneratorUtils.getSerialVersionUID().toString());
		
		
		String analysisPackagePathToFilePath = GeneratorUtils.analysisFilePath(generatorConfig.getProjectPath(), generatorConfig.getGroupId() + ".response");
		
		PrepareGennerateFile myMessage = new PrepareGennerateFile();
		myMessage.setFileName("MyMessage");
		myMessage.setFilePath(analysisPackagePathToFilePath);
		myMessage.setFileSuffix("java");
		myMessage.setText(myMessageTxt);
		responseModelList.add(myMessage);
		
		PrepareGennerateFile pageData = new PrepareGennerateFile();
		pageData.setFileName("Page");
		pageData.setFilePath(analysisPackagePathToFilePath);
		pageData.setFileSuffix("java");
		pageData.setText(pageDataTxt);
		responseModelList.add(pageData);
		
		PrepareGennerateFile responseData = new PrepareGennerateFile();
		responseData.setFileName("ResponseResult");
		responseData.setFilePath(analysisPackagePathToFilePath);
		responseData.setFileSuffix("java");
		responseData.setText(responseDataTxt);
		responseModelList.add(responseData);
		
		
		PrepareGennerateFile state = new PrepareGennerateFile();
		state.setFileName("State");
		state.setFilePath(analysisPackagePathToFilePath);
		state.setFileSuffix("java");
		state.setText(stateTxt);
		responseModelList.add(state);
		
		return responseModelList;
	}
}
