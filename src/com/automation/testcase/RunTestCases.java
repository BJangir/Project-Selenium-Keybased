package com.automation.testcase;

import java.io.IOException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
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
	
	protected  static String CHROME_DRIVER="D:\\mydrivers\\chromedriver.exe";
	protected  static String EXCEL_PATH="C:\\Users\\Administrator\\Documents\\DemoTest.xlsx";
    static ConfigReader reader=new ConfigReader();
    TestCaseMapper testcasemapper; 
	
    public RunTestCases(TestCaseMapper testcasemapper) {
     this.testcasemapper=testcasemapper;
    
    }
    
    
    @Parameters(name="{0}")
    public static List<TestCaseMapper[]> getTestCases() throws IOException{
    	ExcelUtil util=new ExcelUtil();
    	
		List<String[]> teststeps = util.getExcelDataBasedOnRunCol(EXCEL_PATH, "TestSteps", 8);
		List<String[]> testTestcases = util.getExcelDataBasedOnRunCol(reader.getKeyValue("EXCEL_PATH"), "TestCases", 3);
		
		ActionUtil actionUtil=new ActionUtil();
		//List<TestStepModel> testCaseModel = actionUtil.getTestCaseModel(teststeps);
		List<TestCaseMapper[]> testCaseToStepsMapping  = actionUtil.getTestCaseToStepsMapping(teststeps, testTestcases);
		return testCaseToStepsMapping;
    	
    }
    
	@BeforeClass
	public static void beforeClass() throws IOException{
		if(Boolean.parseBoolean(reader.getKeyValue("START_BROWSER_FIRST"))){
			WebDriverUtil driverUtil=new WebDriverUtil("Chrome", CHROME_DRIVER);
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
	
	
	
	public static void main(String[] args) throws IOException {
		WebDriverUtil driverUtil=new WebDriverUtil("Chrome", CHROME_DRIVER);
		driverUtil.openBrowser("http://xcelerator.ninja");
		ExcelUtil util=new ExcelUtil();
		//List<String[]> excelData = util.getExcelData(EXCEL_PATH, "Pages", "5");
		
		List<String[]> testcaseData = util.getExcelDataBasedOnRunCol(EXCEL_PATH, "Testcase", 6);
		ActionUtil actionUtil=new ActionUtil();
		List<TestStepModel> testCaseModel = actionUtil.getTestCaseModel(testcaseData);
		
		for(TestStepModel tm:testCaseModel){

			ActionExecution execution=new ActionExecution();
			execution.executeAction(tm);
		}
		
		
		
		
		
	}


	

}
