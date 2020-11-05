package com.simple.generator.pojo;

import java.util.List;

public class ControllerAnnotation {
	private String annotationPackage;
	private String annotation;
	private List<String> method;
	private List<String> exclude;
	public String getAnnotationPackage() {
		return annotationPackage;
	}
	public void setAnnotationPackage(String annotationPackage) {
		this.annotationPackage = annotationPackage;
	}
	public String getAnnotation() {
		return annotation;
	}
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	public List<String> getMethod() {
		return method;
	}
	public void setMethod(List<String> method) {
		this.method = method;
	}
	public List<String> getExclude() {
		return exclude;
	}
	public void setExclude(List<String> exclude) {
		this.exclude = exclude;
	}
	@Override
	public String toString() {
		return "ControllerAnnotation [annotationPackage=" + annotationPackage + ", annotation=" + annotation + ", method=" + method + ", exclude=" + exclude
		        + "]";
	}
}
