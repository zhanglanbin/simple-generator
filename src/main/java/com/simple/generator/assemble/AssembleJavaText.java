package com.simple.generator.assemble;

import org.springframework.stereotype.Component;

import com.simple.generator.constant.Symbol;
import com.simple.generator.pojo.dto.GenerateModelDTO;
import com.simple.generator.service.AssembleAnalysisService;

/**
 * 组装模板解析文本
 * */
@Component
public class AssembleJavaText {
	public String getJavaText(String modelQueryTxt, GenerateModelDTO generateModelDTO, AssembleAnalysisService assembleAnalysisService) {
		String[] split = modelQueryTxt.split(Symbol.lineStr);
		String javaTxt = "";
		String previousLineStr = "";
		for(int i=0 ; i<split.length ; i++) {
			String str = split[i];
			
			int placeholderIndex = str.indexOf(Symbol.tabBy1+"${generator");
			if(placeholderIndex > -1) {
				str = str.replace(Symbol.tabBy1, "");
			}
			
			if("\t\r".equals(str)) {
				str = str.replace(Symbol.tabBy1, "\n");
			}
			
			String analysis = assembleAnalysisService.analysis(str, generateModelDTO);
			
			if(str.indexOf("${generator")>-1 && (null==analysis || "".equals(analysis) || analysis.equals(previousLineStr))) {
				continue ;
			}
			
			previousLineStr = analysis;
			
			javaTxt += analysis;
		}
		return javaTxt;
	}
}
