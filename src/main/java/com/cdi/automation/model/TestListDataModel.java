package com.cdi.automation.model;
import java.io.Serializable;

public class TestListDataModel implements Serializable{
	
	
	private String testSystemId;
	private String testSystemName;
	private String testUrl;
	public String getTestSystemId() {
		return testSystemId;
	}
	public void setTestSystemId(String testSystemId) {
		this.testSystemId = testSystemId;
	}
	public String getTestSystemName() {
		return testSystemName;
	}
	public void setTestSystemName(String testSystemName) {
		this.testSystemName = testSystemName;
	}
	public String getTestUrl() {
		return testUrl;
	}
	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

	
	
	


}
