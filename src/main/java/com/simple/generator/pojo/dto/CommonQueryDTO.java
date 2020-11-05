package com.simple.generator.pojo.dto;

import java.util.List;

import com.simple.generator.pojo.ColumnInfo;

public class CommonQueryDTO {
	private String explain;
	private String modelClassName;
	private String author;
	private boolean swaggerAnnotation;
	private int toStringFormatType;
	private String commonQueryPackagePath;
	private String commonQuerypackage;
	private String commonQueryJavaFilePath;
	private boolean importList;
	private List<ColumnInfo> columnInfos;
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getModelClassName() {
		return modelClassName;
	}
	public void setModelClassName(String modelClassName) {
		this.modelClassName = modelClassName;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public boolean isSwaggerAnnotation() {
		return swaggerAnnotation;
	}
	public void setSwaggerAnnotation(boolean swaggerAnnotation) {
		this.swaggerAnnotation = swaggerAnnotation;
	}
	public int getToStringFormatType() {
		return toStringFormatType;
	}
	public void setToStringFormatType(int toStringFormatType) {
		this.toStringFormatType = toStringFormatType;
	}
	public String getCommonQueryPackagePath() {
		return commonQueryPackagePath;
	}
	public void setCommonQueryPackagePath(String commonQueryPackagePath) {
		this.commonQueryPackagePath = commonQueryPackagePath;
	}
	public String getCommonQuerypackage() {
		return commonQuerypackage;
	}
	public void setCommonQuerypackage(String commonQuerypackage) {
		this.commonQuerypackage = commonQuerypackage;
	}
	public String getCommonQueryJavaFilePath() {
		return commonQueryJavaFilePath;
	}
	public void setCommonQueryJavaFilePath(String commonQueryJavaFilePath) {
		this.commonQueryJavaFilePath = commonQueryJavaFilePath;
	}
	public boolean isImportList() {
		return importList;
	}
	public void setImportList(boolean importList) {
		this.importList = importList;
	}
	public List<ColumnInfo> getColumnInfos() {
		return columnInfos;
	}
	public void setColumnInfos(List<ColumnInfo> columnInfos) {
		this.columnInfos = columnInfos;
	}
	@Override
	public String toString() {
		return "CommonQueryDTO [explain=" + explain + ", modelClassName=" + modelClassName + ", author=" + author + ", swaggerAnnotation=" + swaggerAnnotation
		        + ", toStringFormatType=" + toStringFormatType + ", commonQueryPackagePath=" + commonQueryPackagePath + ", commonQuerypackage="
		        + commonQuerypackage + ", commonQueryJavaFilePath=" + commonQueryJavaFilePath + ", importList=" + importList + ", columnInfos=" + columnInfos
		        + "]";
	}
}
