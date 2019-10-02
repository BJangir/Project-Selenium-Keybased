package com.automation.testcase;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.automation.core.util.WebDriverUtil;
import com.automation.util.ConfigReader;
import com.automation.util.ConnectionUtil;
import com.automation.util.ExcelUtil;
import com.xceleratorninja.action.ActionExecution;
import com.xceleratorninja.action.ActionUtil;
import com.xceleratorninja.action.TestCaseMapper;
import com.xceleratorninja.action.TestStepModel;



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
			WebDriverUtil driverUtil=new WebDriverUtil(reader.getKeyValue("BROWSER_TYPE"), reader.getKeyValue("CHROME_DRIVER") );
			driverUtil.openBrowser(reader.getKeyValue("BROWSER_URL") );
		}
		
		
	}
	
	@Test
	public void testcaseExecution()
	{
		List<TestStepModel> testmodel = testcasemapper.getTm();
		ActionExecution execution=new ActionExecution();
		int size = testmodel.size();
		boolean testcasestatus=false;
		for(int i=0;i<size;i++){
			
			if(testmodel.get(i).getTestcase_action().equalsIgnoreCase("closeBrowser")){
				execution.executeActionSequence(testmodel.get(i));
			}
			else{
				testcasestatus = execution.executeAction(testmodel.get(i));
			}
			
			
			/*if(i==size-1){
				if(testmodel.get(i).getTestcase_action().equalsIgnoreCase("closeBrowser")){
					execution.executeActionSequence(testmodel.get(i));
				}
				else{
					testcasestatus = execution.executeAction(testmodel.get(i));
				}
				
			}
			else{
				
				
				execution.executeActionSequence(testmodel.get(i));
			}*/
			
		}
		Assert.assertTrue(execution.getFailMessage(), testcasestatus);
		
	}
	
	
	@Before
	public void precondition(){/*
		
		ConnectionUtil util=new ConnectionUtil();
		Connection connection = util.getConnection();
		resetUsers(util, connection);
		util.closeConnection();
		
	
		*/}


	
	


	
	
	@AfterClass
	public static void postClass() throws IOException{
		try{
			WebDriverUtil.driver.quit();
		}catch (Exception e) {
			logger.info("Seems driver is closed already.Ignorning");
		}
		
		
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
