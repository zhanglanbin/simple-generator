package com.simple.generator.pojo;

import java.io.Serializable;

/**
 * @author: zhanglanbin
 * @date: Oct 19, 2019 2:02:22 PM
 */
public class ColumnInfo implements Serializable {

	private static final long serialVersionUID = -1812119119402313291L;

	private String name;
	/**
	 * java属性名称
	 * */
	private String javaName;
	/**
	 * mysql类型
	 */
	private String dataType;
	/**
	 * jdbc类型
	 */
	private String jdbcType;
	/**
	 * java类型
	 * */
	private Class<?> javaType;
	/**
	 * java类型 list泛型
	 * */
	private Class<?> javaTypeByListT;
	/**
	 * 排序位置
	 */
	private int sortPosition;
	/**
	 * 是否可以为null, false=不能,true=能
	 */
	private boolean isNullable;
	/**
	 * 约束类型
	 */
	private String constraintType;
	/**
	 * 说明
	 */
	private String explain;
	/**
	 * 整长度
	 */
	private int length;
	/**
	 * 小数位长度
	 */
	private int floatLength;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJavaName() {
		return javaName;
	}
	public void setJavaName(String javaName) {
		this.javaName = javaName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	public Class<?> getJavaType() {
		return javaType;
	}
	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}
	public Class<?> getJavaTypeByListT() {
		return javaTypeByListT;
	}
	public void setJavaTypeByListT(Class<?> javaTypeByListT) {
		this.javaTypeByListT = javaTypeByListT;
	}
	public int getSortPosition() {
		return sortPosition;
	}
	public void setSortPosition(int sortPosition) {
		this.sortPosition = sortPosition;
	}
	public boolean isNullable() {
		return isNullable;
	}
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
	public String getConstraintType() {
		return constraintType;
	}
	public void setConstraintType(String constraintType) {
		this.constraintType = constraintType;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getFloatLength() {
		return floatLength;
	}
	public void setFloatLength(int floatLength) {
		this.floatLength = floatLength;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "ColumnInfo [name=" + name + ", javaName=" + javaName + ", dataType=" + dataType + ", jdbcType=" + jdbcType + ", javaType=" + javaType
		        + ", javaTypeByListT=" + javaTypeByListT + ", sortPosition=" + sortPosition + ", isNullable=" + isNullable + ", constraintType="
		        + constraintType + ", explain=" + explain + ", length=" + length + ", floatLength=" + floatLength + "]";
	}
}