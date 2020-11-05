package com.simple.generator.config;

import java.util.List;

public class GroupProperties {
	private String groupName;
	private List<String> groupTableName;
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<String> getGroupTableName() {
		return groupTableName;
	}
	public void setGroupTableName(List<String> groupTableName) {
		this.groupTableName = groupTableName;
	}
	@Override
	public String toString() {
		return "GroupProperties [groupName=" + groupName + ", groupTableName=" + groupTableName + "]";
	}
}
