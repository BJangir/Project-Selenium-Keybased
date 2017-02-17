package com.automation.action;

import java.util.List;

public class TestCaseMapper {
	String testcaseid;
	String desc;
	List<TestStepModel> tm;
	public String getTestcaseid() {
		return testcaseid;
	}
	public void setTestcaseid(String testcaseid) {
		this.testcaseid = testcaseid;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<TestStepModel> getTm() {
		return tm;
	}
	public void setTm(List<TestStepModel> tm) {
		this.tm = tm;
	}

}
