package com.simple.generator.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "model-groups")
public class GroupsProperties {
	private List<GroupProperties> groupPropertiesList;

	public List<GroupProperties> getGroupPropertiesList() {
		return groupPropertiesList;
	}

	public void setGroupPropertiesList(List<GroupProperties> groupPropertiesList) {
		this.groupPropertiesList = groupPropertiesList;
	}

	@Override
	public String toString() {
		return "GroupsProperties [groupPropertiesList=" + groupPropertiesList + "]";
	}
}
