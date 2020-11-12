package com.simple.generator.service.generatro;

import org.thymeleaf.context.Context;

public interface GeneratroService {
	public boolean templateAnalysisGenerateFile(Context context, String writerPath, String Prefix, String fileName);
	
	public boolean templateAnalysisGenerateFile(Context context, String writerPath, String fileName);
}
