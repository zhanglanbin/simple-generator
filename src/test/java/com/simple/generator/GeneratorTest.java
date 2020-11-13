package com.simple.generator;

import java.io.File;
import java.io.FileWriter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.simple.generator.pojo.dto.SimpleGeneratorConfigurationDTO;

public class GeneratorTest {
	
	private static final Logger log = LoggerFactory.getLogger(GeneratorTest.class);
	
	SimpleGeneratorConfigurationDTO simpleGeneratorConfigurationDTO;
	
	@Test
	public void run() {
		try {
			ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
			resolver.setPrefix("generator-template/");
			resolver.setSuffix(".txt");
			TemplateEngine templateEngine = new TemplateEngine();
			templateEngine.setTemplateResolver(resolver);
			
			Context context = new Context();
			context.setVariable("simpleGeneratorConfigurationDTO", simpleGeneratorConfigurationDTO);
			
			String writerPath = simpleGeneratorConfigurationDTO.getProjectPath(); 
			writerPath += File.separatorChar + "pom.xml";
			FileWriter fileWriter = new FileWriter(writerPath);
			
			templateEngine.process("pom", context, fileWriter);
		} catch (Exception e) {
			log.error("{}", e);
		}
	}
}
