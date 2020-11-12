package com.simple.generator.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;

import com.simple.generator.constant.PathConstant;
import com.simple.generator.constant.Symbol;
import com.simple.generator.pojo.ColumnInfo;
import com.simple.generator.pojo.dto.GenerateModelDTO;

public class GeneratorUtils {
	
	private static final Logger log = LoggerFactory.getLogger(GeneratorUtils.class);
	
	public static String readTxt(InputStream is) {
		InputStreamReader in = null;
		BufferedReader br = null;
		try {
			int available = is.available();
			byte[] data = new byte[available];
			int len = is.read(data);
			return new String(data, 0, len);
		} catch (IOException e) {
			return null;
		} finally {
			if(null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String readTxt(File file) {
		String s = "";
		InputStreamReader in = null;
		BufferedReader br = null;
		try {
			in = new InputStreamReader(new FileInputStream(file), "UTF-8");
			br = new BufferedReader(in);
			StringBuffer content = new StringBuffer();
			while ((s = br.readLine()) != null) {
				content = content.append(s);
			}
			return content.toString();
		} catch (IOException e) {
			return null;
		} finally {
			if(null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getSerialVersionUID() {
		long uid=1L;
		
		Random random = new Random();
		uid = random.nextLong();
		
		return Long.toString(uid) + "L";
	}
	
	
	public static String analysisPackagePathToFilePath(String projectPath, String modelPackagePath) {
		String javaFilePath = "";
		if(null!=modelPackagePath && !"".equals(modelPackagePath) && modelPackagePath.contains(".")) {
			String[] split = modelPackagePath.split("\\.");
			for(int j=0 ; j<split.length ; j++) {
				javaFilePath += File.separatorChar + split[j];
			}
		}
		return javaFilePath;
	}
	
	public static String analysisFilePath(String projectPath, String modelPackagePath) {
//		String javaFilePath = projectPath + "\\src\\main\\java";
		String javaFilePath = projectPath + File.separatorChar + PathConstant.mainJavaPath;
		if(null!=modelPackagePath && !"".equals(modelPackagePath) && modelPackagePath.contains(".")) {
			String[] split = modelPackagePath.split("\\.");
			for(int j=0 ; j<split.length ; j++) {
				javaFilePath += File.separatorChar + split[j];
			}
		}
		return javaFilePath;
	}
	
	
	
	
	/**
	 * 创建java文件
	 */
	public static File createJavaFile(String javaFilePath, String fileName, String fileSuffixName, String content) {
		FileOutputStream fop = null;
		File file = null;
		try {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(javaFilePath).append(File.separatorChar);
			stringBuffer.append(fileName).append(".").append(fileSuffixName);
			
			log.info("生成文件的路径::{}",stringBuffer.toString());

			// 初始化文件路径
			file = new File(stringBuffer.toString());

			// 目录是否存在, 不存在则创建
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			// 文件是否存在, 不存在则创建
			if (!file.exists()) {
				file.createNewFile();
			}

			// 文件字节流初始化
			fop = new FileOutputStream(file);

			// 获取字符串字节
			byte[] contentInBytes = content.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 获取运行程序的上级目录
	 * 
	 * @return 上级目录
	 */
	public static String getJarPath() {
		ApplicationHome h = new ApplicationHome(new Object().getClass());
		File jarF = h.getSource();
		if (null == jarF) {
			jarF = new File(h.getDir().toString());

		}
		return jarF.getParentFile().getParentFile().toString();
	}
	
	/**
	 * 获取运行程序的上级目录
	 * 
	 * @return 上级目录
	 */
	public static String getThisProjectPath() {
		ApplicationHome h = new ApplicationHome(new Object().getClass());
		File jarF = h.getSource();
		if (null == jarF) {
			jarF = new File(h.getDir().toString());

		}
		return jarF.getParentFile().toString();
	}
	

	/**
	 * 首字母转大写
	 */
	public static String firstCharToUpperCase(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	/**
	 * 首字母转小写
	 */
	public static String firstCharToLowerCase(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	
	
	
	
	
	
	
	/**
	 * 组装toString()方法
	 * */
	public static String asModelToString(GenerateModelDTO generateModelDTO) {
		return asModelToString(generateModelDTO, false, false);
	}
	
	/**
	 * 组装toString()方法
	 * */
	public static String asModelToString(GenerateModelDTO generateModelDTO, boolean isSuperToString, boolean isQueryModel) {
		if(generateModelDTO.getToStringFormatType() == 2) {
			//idea格式toString
			return ideaToString(generateModelDTO, isSuperToString, isQueryModel);
			
		} else if(generateModelDTO.getToStringFormatType() == 1) {
			//eclipse格式toString
			return eclipseToString(generateModelDTO, isSuperToString, isQueryModel);
			
		} else if(generateModelDTO.getToStringFormatType() == 0) {
			//json格式toString
			return jsonToString();
			
		}else {
			//json格式toString
			return jsonToString();
		}
	}
	
	
	
	
	
	public static String ideaToString(GenerateModelDTO generateModelDTO, boolean isSuperToString, boolean isQueryModel) {
		StringBuffer toStringBuffer = new StringBuffer();
		List<ColumnInfo> columnInfos = generateModelDTO.getColumnInfos();
		toStringBuffer
		.append(Symbol.tabBy1).append("@Override").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("public String toString() {").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append("return \"").append(generateModelDTO.getModelClassName()).append("{\" +");
		String str = "";
		String strsp = "";
		for(ColumnInfo columnInfo : columnInfos) {
			String name = columnInfo.getJavaName();
			String type = columnInfo.getJavaType().getName();
			
			if(isJdbcDate(columnInfo) && isQueryModel) {
				String startAttributeName = columnInfo.getJavaName() + "Start";
				String endAttributeName = columnInfo.getJavaName() + "End";
				if(String.class.getName().equals(type)) {
					toStringBuffer.append(Symbol.lineStr)
					.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("\"").append(str).append(strsp).append(startAttributeName).append("=").append("'\" + ").append(startAttributeName)
					.append(" + '\\'' + ");
					toStringBuffer.append(Symbol.lineStr)
					.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("\"").append(str).append(strsp).append(endAttributeName).append("=").append("'\" + ").append(endAttributeName)
					.append(" + '\\'' + ");
				} else {
					//不是字符串组装不需要外加引号"'"
					toStringBuffer.append(Symbol.lineStr)
					.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("\"").append(str).append(strsp).append(startAttributeName).append("=").append("\" + ").append(startAttributeName)
					.append(" + ");
					toStringBuffer.append(Symbol.lineStr)
					.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("\"").append(str).append(strsp).append(endAttributeName).append("=").append("\" + ").append(endAttributeName)
					.append(" + ");
				}
			} else {
				if(String.class.getName().equals(type)) {
					toStringBuffer.append(Symbol.lineStr)
					.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("\"").append(str).append(strsp).append(name).append("=").append("'\" + ").append(name)
					.append(" + '\\'' + ");
				} else {
					//不是字符串组装不需要外加引号"'"
					toStringBuffer.append(Symbol.lineStr)
					.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("\"").append(str).append(strsp).append(name).append("=").append("\" + ").append(name)
					.append(" + ");
				}
			}
			str = ",";
			strsp = " ";
		}
		if(isSuperToString) {
			toStringBuffer.append(Symbol.lineStr).append(Symbol.tabBy3).append("\", toString()=\" + super.toString() + ");
		}
		toStringBuffer.append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append("'}';").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("}");
		return toStringBuffer.toString();
	}
	
	
	public static String eclipseToString(GenerateModelDTO generateModelDTO, boolean isSuperToString, boolean isQueryModel) {
		StringBuffer toStringBuffer = new StringBuffer();
		List<ColumnInfo> columnInfos = generateModelDTO.getColumnInfos();
		toStringBuffer
		.append(Symbol.tabBy1).append("@Override").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("public String toString() {").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append("return \"").append(generateModelDTO.getModelClassName()).append(" [\" +");
		String str = "";
		String strsp = "";
		for(ColumnInfo columnInfo : columnInfos) {
			String name = columnInfo.getJavaName();
			
			if(isJdbcDate(columnInfo) && isQueryModel) {
				String startAttributeName = columnInfo.getJavaName() + "Start";
				String endAttributeName = columnInfo.getJavaName() + "End";
				
				//不是字符串组装不需要外加引号"'"
				toStringBuffer.append(Symbol.lineStr)
				.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("\"").append(str).append(strsp).append(startAttributeName).append("=").append("\" + ").append(startAttributeName)
				.append(" + ");
				toStringBuffer.append(Symbol.lineStr)
				.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("\"").append(str).append(strsp).append(endAttributeName).append("=").append("\" + ").append(endAttributeName)
				.append(" + ");
			} else {
				//不是字符串组装不需要外加引号"'"
				toStringBuffer.append(Symbol.lineStr)
				.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("\"").append(str).append(strsp).append(name).append("=").append("\" + ").append(name)
				.append(" + ");
			}
			str = ",";
			strsp = " ";
		}
		if(isSuperToString) {
			toStringBuffer.append(Symbol.lineStr).append(Symbol.tabBy3).append("\", toString()=\" + super.toString() + ");
		}
		toStringBuffer.append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append("\"]\";").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("}");
		return toStringBuffer.toString().substring(1, toStringBuffer.toString().length());
	}
	
	
	public static String jsonToString() {
		StringBuffer toStringBuffer = new StringBuffer();
		toStringBuffer
		.append(Symbol.tabBy1).append("@Override").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("public String toString() {").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append("return JSON.toJSONString(this, new SerializerFeature[] {").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("SerializerFeature.WriteMapNullValue,").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("SerializerFeature.WriteNullListAsEmpty,").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("SerializerFeature.WriteNullStringAsEmpty,").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("SerializerFeature.WriteNullNumberAsZero,").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("SerializerFeature.WriteNullBooleanAsFalse,").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(Symbol.tabBy1).append(Symbol.tabBy1).append("SerializerFeature.UseISO8601DateFormat });").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append("}");
		return toStringBuffer.toString();
	}
	
	
	public static boolean isJdbcDate(ColumnInfo columnInfo) {
		if(columnInfo.getJavaType().getName().equals(Date.class.getName())) {
			return true;
		}else {
			return false;
		}
	}
	
	
	
	
	
	/**
	 * @Description: 组装文档注释
	 * @param author 作者
	 * @param objectName 对象名称
	 * @param tableExplain 说明
	 * @return 文档注释
	 * */
	public static String getFileNotes(String title, String desc, String author) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer
		.append("/**").append(Symbol.lineStr)
		.append(" * @Title: ").append(title).append(Symbol.lineStr)
		.append(" * @Description: ").append(desc).append(Symbol.lineStr)
		.append(" * @author: ").append(author).append(Symbol.lineStr)
		.append(" * @date: ").append(simpleDateFormat.format(new Date())).append(Symbol.lineStr)
		.append(" * */");
		return stringBuffer.toString();
	}
	/**
	 * model 属性注释
	 * */
	public static String getModelAttrNotes(String columnExplain) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer
		.append("/**").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(" * @Description: ").append(columnExplain).append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(" * */");
		return stringBuffer.toString();
	}
	/**
	 * modelConst 内部接口注释
	 * */
	public static String getModelInterNotes() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer
		.append(Symbol.tabBy1).append("/**").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(" * @Description: ").append("属性名").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(" * */");
		return stringBuffer.toString();
	}
	
	
	/**
	 * modelConst 内部接口注释
	 * */
	public static String getModelColNotes() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer
		.append(Symbol.tabBy1).append("/**").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(" * @Description: ").append("数据列名").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(" * */");
		return stringBuffer.toString();
	}
	
	/**
	 * model 内部接口常量注释
	 * */
	public static String getModelInterAttrNotes(String columnExplain) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer
		.append(Symbol.tabBy1).append("/**").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(" * @Description: ").append(columnExplain).append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(" * */");
		return stringBuffer.toString();
	}
	
	
	/**
	 * dao 动态查询注释
	 * */
	public static String getDaoDynamicFindNotes() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer
		.append(Symbol.tabBy1).append("/**").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(" * @Description: ").append("必须传入一个属性为columnList的集合, 其中包含一个或多个数据列名称, 其他条件K为列名, V为值").append(Symbol.lineStr)
		.append(Symbol.tabBy1).append(" * */");
		return stringBuffer.toString();
	}
	
	
	/**
	 * 获取生成器代码模板文本
	 * @param 代码模板资源路径, resources下
	 * */
	public static String getCodeTemplate(String resourcePath) {
		InputStream stream = null;
		String codeTxt = null;
		try {
			
			stream = new ClassPathResource(resourcePath).getInputStream();
//					ClassLoader.getSystemResourceAsStream(resourcePath);
			if(null == stream) {
				return null;
			}
			
			codeTxt = GeneratorUtils.readTxt(stream);
			if(null==codeTxt || "".equals(codeTxt)) {
				return null;
			}
			return codeTxt;
		} catch (Exception e) {
			log.error("{}", e);
			return null;
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 字符串是否, 为空
	 * 为空或"" 返回true , 否则返回false
	 * */
	public static boolean isEmpty(String str) {
		return (null==str || "".equals(str) || " ".equals(str) || "null".equals(str) || "undefined".equals(str));
	}
	
	
	/**
	 * 字符串是否, 不为空
	 * 不为空并且不为"" , 返回true, 否则返回false
	 * */
	public static boolean isNotEmpty(String str) {
		return (null!=str && !"".equals(str) && !" ".equals(str) && !"null".equals(str) && !"undefined".equals(str));
	}
	
	
	/**
	 * 字符串是否包含 值, 允许是null
	 * */
	public static boolean isContainAllowEmpty(String str,String... str2) {
		for(String s : str2) {
			if(null==str && str==s) {
				return true;
			} else if(null!=str && str.equals(s)) {
				return true;
			}
		}
		return false;
	}
}
