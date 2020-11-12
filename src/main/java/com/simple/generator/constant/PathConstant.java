package com.simple.generator.constant;

import java.io.File;

public interface PathConstant {
	public static final String testResourcesPath = "src"+File.separatorChar+"test"+File.separatorChar+"resources";
	public static final String testJavaPath = "src"+File.separatorChar+"test"+File.separatorChar+"java";
	public static final String mainResourcesPath = "src"+File.separatorChar+"main"+File.separatorChar+"resources";
	public static final String mainJavaPath = "src"+File.separatorChar+"main"+File.separatorChar+"java";
	public static final String generatorTemplatePath = "generator-template/";
	public static final String generatorTemplateSuffix = ".txt";
}
