package com.automation.testcase.welcome;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.automation.action.ActionExecution;
import com.automation.action.ActionUtil;
import com.automation.action.TestStepModel;
import com.automation.core.util.WebDriverUtil;
import com.automation.util.ExcelUtil;

public class ActionBasedTest {
	
	
	protected  static String CHROME_DRIVER="D:\\mydrivers\\chromedriver.exe";
	protected  static String EXCEL_PATH="C:\\Users\\Administrator\\Documents\\DemoTest.xlsx";
	static List<TestStepModel> testCaseModel;
	
	@BeforeClass
	public static void beforeClass() throws IOException{
		WebDriverUtil driverUtil=new WebDriverUtil("Chrome", CHROME_DRIVER);
		driverUtil.openBrowser("http://xcelerator.ninja");
		ExcelUtil util=new ExcelUtil();
		//List<String[]> excelData = util.getExcelData(EXCEL_PATH, "Pages", "5");
		List<String[]> testcaseData = util.getExcelDataBasedOnRunCol(EXCEL_PATH, "Testcase", 6);
		ActionUtil actionUtil=new ActionUtil();
		testCaseModel = actionUtil.getTestCaseModel(testcaseData);
	}
	
	@Test
	public void testme()
	{
		for(TestStepModel tm:testCaseModel){
			ActionExecution execution=new ActionExecution();
			boolean executeAction = execution.executeAction(tm);
			Assert.assertTrue(executeAction);
		}	
		//PropertyConfigurator.configure("log4j.properties");
		
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
