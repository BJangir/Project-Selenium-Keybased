package com.automation.testcase;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.automation.action.ActionExecution;
import com.automation.action.ActionUtil;
import com.automation.action.TestCaseMapper;
import com.automation.action.TestStepModel;
import com.automation.core.util.WebDriverUtil;
import com.automation.util.ConfigReader;
import com.automation.util.ExcelUtil;



@RunWith(Parameterized.class)
public class RunTestCases {
	
    static ConfigReader reader=new ConfigReader();
    TestCaseMapper testcasemapper; 
  String testcasename;
    final static Logger logger = Logger.getLogger(RunTestCases.class);
    
    public RunTestCases(TestCaseMapper testcasemapper,Object testcasename) {
     this.testcasemapper=testcasemapper;
    this.testcasename=(String)testcasename;
    }
    
    static{
    	String property = System.getProperty("auto.log4j.path", "log4j.properties");
    	
    	PropertyConfigurator.configure(property);
    }
    
    
    @Parameters(name="{1}")
    public static List<Object[]> getTestCases() throws IOException{
    	ExcelUtil util=new ExcelUtil();
		List<String[]> teststeps = util.getExcelDataBasedOnRunCol(reader.getKeyValue("EXCEL_PATH"), "TestSteps", 8);
		List<String[]> testTestcases = util.getExcelDataBasedOnRunCol(reader.getKeyValue("EXCEL_PATH"), "TestCases", 3);
		
		ActionUtil actionUtil=new ActionUtil();
		//List<TestStepModel> testCaseModel = actionUtil.getTestCaseModel(teststeps);
		List<Object[]> testCaseToStepsMapping  = actionUtil.getTestCaseToStepsMapping(teststeps, testTestcases);
		logger.info("Total TestCase found "+testCaseToStepsMapping.size());
		return testCaseToStepsMapping;
    	
    }
    
	@BeforeClass
	public static void beforeClass() throws IOException{
		if(Boolean.parseBoolean(reader.getKeyValue("START_BROWSER_FIRST"))){
			reader.getKeyValue("");
			WebDriverUtil driverUtil=new WebDriverUtil("Chrome", reader.getKeyValue("CHROME_DRIVER") );
			driverUtil.openBrowser(reader.getKeyValue("BROWSER_URL") );
		}
		
		
	}
	
	@Test
	public void testme()
	{


		   
		List<TestStepModel> testmodel = testcasemapper.getTm();
		ActionExecution execution=new ActionExecution();
		int size = testmodel.size();
		boolean testcasestatus=false;
		for(int i=0;i<size;i++){
			
			if(i==size-1){
				testcasestatus = execution.executeAction(testmodel.get(i));
				
			}
			else{
				execution.executeActionSequence(testmodel.get(i));
			}
			
		}
		
		Assert.assertTrue(testcasestatus);
		
		
		
		
		
	
		
		
		
		//PropertyConfigurator.configure("log4j.properties");
		
	}
	
	
	
	@AfterClass
	public static void postClass() throws IOException{
		WebDriverUtil.driver.quit();
	}
	
	
	
	public static void main(String[] args) throws IOException {/*
		WebDriverUtil driverUtil=new WebDriverUtil("Chrome", "");
		driverUtil.openBrowser("http://xcelerator.ninja");
		ExcelUtil util=new ExcelUtil();
		//List<String[]> excelData = util.getExcelData(EXCEL_PATH, "Pages", "5");
		
		List<String[]> testcaseData = util.getExcelDataBasedOnRunCol("", "Testcase", 6);
		ActionUtil actionUtil=new ActionUtil();
		List<TestStepModel> testCaseModel = actionUtil.getTestCaseModel(testcaseData);
		
	
		
		
		
		
	*/}


	

}
