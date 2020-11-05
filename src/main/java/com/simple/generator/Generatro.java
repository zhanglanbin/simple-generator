package com.simple.generator;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.simple.generator.config.GeneratorConfig;
import com.simple.generator.constant.PathConstant;
import com.simple.generator.pojo.PrepareGennerateFile;
import com.simple.generator.utils.GeneratorUtils;

@Component
@Order(value=10)
public class Generatro implements CommandLineRunner {
	
	private static final Logger log = LoggerFactory.getLogger(Generatro.class);
	
	@Autowired
	private GeneratorConfig generatorConfig;
	
	@Autowired
    private ApplicationContext appContext;
	
	@Resource
	private PrepareGennerateFile commonQueryModel;
	@Resource
	private List<PrepareGennerateFile> modelMDList;
	@Resource
	private List<PrepareGennerateFile> modelQureyList;
	@Resource
	private List<PrepareGennerateFile> modelList;
	@Resource
	private List<PrepareGennerateFile> daoList;
	@Resource
	private List<PrepareGennerateFile> mapperList;
	@Resource
	private List<PrepareGennerateFile> responseModelList;
	@Resource
	private List<PrepareGennerateFile> serviceList;
	@Resource
	private List<PrepareGennerateFile> serviceImplList;
	@Resource
	private List<PrepareGennerateFile> controllerList;
	@Resource
	private PrepareGennerateFile pom;
	@Resource
	private PrepareGennerateFile mainApp;
	@Resource
	private PrepareGennerateFile applicationYml;
	@Resource
	private PrepareGennerateFile swaggerConfig;
	
	
	
	
	
	@Override
    public void run(String... args) throws Exception {
		System.out.println(generatorConfig);
		
		String[] beans = appContext.getBeanDefinitionNames();
        Arrays.sort(beans);
        for (String bean : beans) {
            System.out.println(bean + " of Type :: " + appContext.getBean(bean).getClass());
        }
        System.out.println(commonQueryModel);
        System.out.println(modelMDList);
		
		
		// 生成公共查询参数实体类
		File file = GeneratorUtils.createJavaFile(commonQueryModel.getFilePath(), commonQueryModel.getFileName(), commonQueryModel.getFileSuffix(), commonQueryModel.getText());
		if(null==file) {
			log.info("{}生成失败!", commonQueryModel.getFileName());
		}
		
		// 生成pojo数据模型
		for(PrepareGennerateFile gennerateFile : modelMDList) {
			file = GeneratorUtils.createJavaFile(gennerateFile.getFilePath(), gennerateFile.getFileName(), gennerateFile.getFileSuffix(), gennerateFile.getText());
			if(null==file) {
				log.info("{}生成失败!", gennerateFile.getFileName());
			}
		}
		// 生成查询类
		for(PrepareGennerateFile gennerateFile : modelQureyList) {
			file = GeneratorUtils.createJavaFile(gennerateFile.getFilePath(), gennerateFile.getFileName(), gennerateFile.getFileSuffix(), gennerateFile.getText());
			if(null==file) {
				log.info("{}生成失败!", gennerateFile.getFileName());
			}
		}
		// 生成pojo
		for(PrepareGennerateFile gennerateFile : modelList) {
			file = GeneratorUtils.createJavaFile(gennerateFile.getFilePath(), gennerateFile.getFileName(), gennerateFile.getFileSuffix(), gennerateFile.getText());
			if(null==file) {
				log.info("{}生成失败!", gennerateFile.getFileName());
			}
		}
		// 生成dao层
		for(PrepareGennerateFile gennerateFile : daoList) {
			file = GeneratorUtils.createJavaFile(gennerateFile.getFilePath(), gennerateFile.getFileName(), gennerateFile.getFileSuffix(), gennerateFile.getText());
			if(null==file) {
				log.info("{}生成失败!", gennerateFile.getFileName());
			}
		}
//		// 生成mapper文件
		for(PrepareGennerateFile gennerateFile : mapperList) {
			file = GeneratorUtils.createJavaFile(gennerateFile.getFilePath(), gennerateFile.getFileName(), gennerateFile.getFileSuffix(), gennerateFile.getText());
			if(null==file) {
				log.info("{}生成失败!", gennerateFile.getFileName());
			}
		}
//		// 生成统一返回
		for(PrepareGennerateFile gennerateFile : responseModelList) {
			file = GeneratorUtils.createJavaFile(gennerateFile.getFilePath(), gennerateFile.getFileName(), gennerateFile.getFileSuffix(), gennerateFile.getText());
			if(null==file) {
				log.info("{}生成失败!", gennerateFile.getFileName());
			}
		}
		// 生成service
		for(PrepareGennerateFile gennerateFile : serviceList) {
			file = GeneratorUtils.createJavaFile(gennerateFile.getFilePath(), gennerateFile.getFileName(), gennerateFile.getFileSuffix(), gennerateFile.getText());
			if(null==file) {
				log.info("{}生成失败!", gennerateFile.getFileName());
			}
		}
		// 生成service impl
		for(PrepareGennerateFile gennerateFile : serviceImplList) {
			file = GeneratorUtils.createJavaFile(gennerateFile.getFilePath(), gennerateFile.getFileName(), gennerateFile.getFileSuffix(), gennerateFile.getText());
			if(null==file) {
				log.info("{}生成失败!", gennerateFile.getFileName());
			}
		}
		
		// 生成controller
		for(PrepareGennerateFile gennerateFile : controllerList) {
			file = GeneratorUtils.createJavaFile(gennerateFile.getFilePath(), gennerateFile.getFileName(), gennerateFile.getFileSuffix(), gennerateFile.getText());
			if(null==file) {
				log.info("{}生成失败!", gennerateFile.getFileName());
			}
		}
		// 生成pom.xml
		file = GeneratorUtils.createJavaFile(pom.getFilePath(), pom.getFileName(), pom.getFileSuffix(), pom.getText());
		if(null==file) {
			log.info("{}生成失败!", pom.getFileName());
		}
		//生成mainApp启动入口
		file = GeneratorUtils.createJavaFile(mainApp.getFilePath(), mainApp.getFileName(), mainApp.getFileSuffix(), mainApp.getText());
		if(null==file) {
			log.info("{}生成失败!", mainApp.getFileName());
		}
		//生成yml配置文件
		file = GeneratorUtils.createJavaFile(applicationYml.getFilePath(), applicationYml.getFileName(), applicationYml.getFileSuffix(), applicationYml.getText());
		if(null==file) {
			log.info("{}生成失败!", applicationYml.getFileName());
		}
		//生成swagger config
		file = GeneratorUtils.createJavaFile(swaggerConfig.getFilePath(), swaggerConfig.getFileName(), swaggerConfig.getFileSuffix(), swaggerConfig.getText());
		if(null==file) {
			log.info("{}生成失败!", swaggerConfig.getFileName());
		}
		
		
		// 生成test目录
		File testJava = new File(generatorConfig.getProjectPath() + File.separatorChar + PathConstant.testJavaPath + File.separatorChar+"test.java");
		if (!testJava.getParentFile().exists()) {
			testJava.getParentFile().mkdirs();
		}
		File testResources = new File(generatorConfig.getProjectPath() + File.separatorChar + PathConstant.testResourcesPath + File.separatorChar+"test.xml");
		if (!testResources.getParentFile().exists()) {
			testResources.getParentFile().mkdirs();
		}
		
		log.info("生成器 -- 工程生成完毕");
	}
}
