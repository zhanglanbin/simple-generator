package com.simple.generator.pojo;

public class PrepareGennerateFile {
	private String filePath;
	private String fileName;
	private String fileSuffix;
	private String text;
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileSuffix() {
		return fileSuffix;
	}
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "GennerateFile [filePath=" + filePath + ", fileName=" + fileName + ", fileSuffix=" + fileSuffix + ", text=" + text + "]";
	}
}
