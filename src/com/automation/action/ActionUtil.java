package com.automation.action;

import java.util.ArrayList;
import java.util.List;

public class ActionUtil {
	
	public void getActionPageMap(List<String[]> excelData){
		List<ActionPageMap> actionPageMaps=new ArrayList<>();
		for(String[] eachrow:excelData){
			ActionPageMap pageMap=new ActionPageMap();
			pageMap.setPage(eachrow[0]);
			pageMap.setAction_support(eachrow[1]);
			pageMap.setAction_on(eachrow[2]);
			pageMap.setReturn_page(eachrow[3]);
			actionPageMaps.add(pageMap);
		}
	}
	
	
	

	
	
	public List<TestStepModel> getTestCaseModel(List<String[]> excelData){
		List<TestStepModel> testmodeles=new ArrayList<>();
		for(String[] eachrow:excelData){
			TestStepModel testModel=new TestStepModel();
			testModel.setTestcaseId(eachrow[0]);
			testModel.setTestStep(eachrow[1]);
			testModel.setTestcaseDesc(eachrow[2]);
			testModel.setLocator(eachrow[3]);
			testModel.setLocaotrvalue(eachrow[4]);
			testModel.setTestcase_action(eachrow[5]);
			testModel.setdata(eachrow[6]);
			testmodeles.add(testModel);
		}
		return testmodeles;
	}
	
	
	public List<TestCaseMapper[]>  getTestCaseToStepsMapping(List<String[]> teststeps,List<String[]> testTestcases){
	List<TestCaseMapper[]> caseMappers=new ArrayList<>();
	List<TestCaseMapper> testCaseMappers=new ArrayList<>();
	
		for(String tc[]:testTestcases){
			TestCaseMapper[] testCaseMappers2=new TestCaseMapper[1];
			TestCaseMapper mapper=new TestCaseMapper();
			List<String[]> tmp=new ArrayList<>();
			
			for(String[] ts:teststeps){
				if(tc[0].equalsIgnoreCase(ts[0])){
					tmp.add(ts);
				}
			}
			List<TestStepModel> testStepCaseModel = getTestCaseModel(tmp);
			mapper.setTestcaseid(tc[0]);
			mapper.setDesc(tc[1]);
			mapper.setTm(testStepCaseModel);
			testCaseMappers2[0]=mapper;
			//testCaseMappers.add(mapper);
			caseMappers.add(testCaseMappers2);
		}
		
		return caseMappers;
	}
	

}
