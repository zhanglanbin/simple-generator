package com.simple.generator.service.generatro.impl;

import java.io.File;
import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.simple.generator.constant.PathConstant;
import com.simple.generator.service.generatro.GeneratroService;
import com.simple.generator.utils.GeneratorUtils;

@Service
public class GeneratroServiceImpl implements GeneratroService {
	
	private static final Logger log = LoggerFactory.getLogger(GeneratroServiceImpl.class);

	/**
	* @Title: templateAnalysisGenerateFile
	* @author zhanglanbin
	* @Description: Template generatro implements
	* @param simpleGeneratorConfigurationDTO generator config of DTO
	* @param writerPath output file path, for example: D:\EclipseWorkSpace\test\simple-generator-demo\src\main\resources\application.yml
	* @param fileName file is name, for example: application
	* @throws
	*/
	@Override
	public boolean templateAnalysisGenerateFile(Context context, String writerPath, String fileName) {
		try {
			ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
			resolver.setPrefix(PathConstant.generatorTemplatePath);
			resolver.setSuffix(PathConstant.generatorTemplateSuffix);
			TemplateEngine templateEngine = new TemplateEngine();
			templateEngine.setTemplateResolver(resolver);
			
			File file = new File(writerPath);
			// 目录是否存在, 不存在则创建
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			// 文件是否存在, 不存在则创建
			if (!file.exists()) {
				file.createNewFile();
			}
			
			
			FileWriter fileWriter = new FileWriter(file);
			
			templateEngine.process(fileName, context, fileWriter);
			return true;
		} catch (Exception e) {
			log.error("{}", e);
		}
		return false;
	}
	
	
	/**
	 * @Title: templateAnalysisGenerateFile
	 * @author zhanglanbin
	 * @Description: Template generatro implements
	 * @param simpleGeneratorConfigurationDTO generator config of DTO
	 * @param writerPath output file path, for example: D:\EclipseWorkSpace\test\simple-generator-demo\src\main\resources\application.yml
	 * @param prefix templete file catalog, ending We can't lack it "/" , for example: generator-template/model/
	 * @param fileName file is name, for example: application
	 * @throws
	 */
	@Override
	public boolean templateAnalysisGenerateFile(Context context, String writerPath, String prefix, String fileName) {
		try {
			ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
			if(GeneratorUtils.isNotEmpty(prefix)) {
				resolver.setPrefix(prefix);
			} else {
				resolver.setPrefix(PathConstant.generatorTemplatePath);
			}
			resolver.setSuffix(PathConstant.generatorTemplateSuffix);
			TemplateEngine templateEngine = new TemplateEngine();
			templateEngine.setTemplateResolver(resolver);
			
			File file = new File(writerPath);
			// 目录是否存在, 不存在则创建
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			
			// 文件是否存在, 不存在则创建
			if (!file.exists()) {
				file.createNewFile();
			}
			
			
			FileWriter fileWriter = new FileWriter(file);
			
			templateEngine.process(fileName, context, fileWriter);
			return true;
		} catch (Exception e) {
			log.error("{}", e);
		}
		return false;
	}
}
