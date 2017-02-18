package com.automation.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.core.util.WebDriverUtil;

public class Actions {
	
	final static Logger logger = Logger.getLogger(Actions.class);
	String openBrowser;
	String click;
	String verifyText;
	String getText;
	String setText;
	String getErrorText;
	String clear;
	WebDriver driver;
	public Actions() {
		driver=WebDriverUtil.driver;
	}
	

	public String getopenBrowser() {
		return openBrowser;
	}
	public void openBrowser(TestStepModel tm) {
		Map<String,String> browserProperties=new HashMap<>();
		String openBrowser = tm.getdata();
		String[] split = openBrowser.split("\n");
		for(String each:split){
			String[] split2 = each.split("=");
			browserProperties.put(split2[0], split2[1]);
		}
		WebDriverUtil driverUtil=new WebDriverUtil("Chrome", browserProperties.get("driverpath"));
		 driver = driverUtil.getDriver();
		 driver.get(browserProperties.get("URL"));
		
	}
	public void click(TestStepModel model) {
		
		String locator = model.getLocator();
		String locaotrvalue = model.getLocaotrvalue();
		By byMethod = getByMethod(locator, locaotrvalue);
		driver.findElement(byMethod).click();
	}
	

	public void refreshPage(TestStepModel model) {
		driver.navigate().refresh();
	}
	
	

	
	
	public String isTextPresentInPage(TestStepModel model){
		String mytext = null;
		String locator = model.getLocator();
		String locaotrvalue = model.getLocaotrvalue();
		By byMethod = getByMethod(locator, locaotrvalue);
		mytext= driver.findElement(byMethod).getText();
		String[] split = mytext.split("\n");
		List<String> asList = Arrays.asList(split);
		String getdata = model.getdata();
		logger.info(model.getTestcaseId()+"-"+model.getTestcaseId()+"Actual values="+asList+" Expected Values= "+getdata);
		boolean contains = asList.contains(getdata);
		return contains?"true":"false";

		
	}
	

	
	public void clearText(TestStepModel model) {
		String locator = model.getLocator();
		String locaotrvalue = model.getLocaotrvalue();
		By byMethod = getByMethod(locator, locaotrvalue);
		WebElement findElement = driver.findElement(byMethod);
		findElement.sendKeys(Keys.CONTROL + "a");
		findElement.sendKeys(Keys.DELETE);
		
		
	}
	
	
	
	public void setText(TestStepModel model) {
		String locator = model.getLocator();
		String locaotrvalue = model.getLocaotrvalue();
		By byMethod = getByMethod(locator, locaotrvalue);
		driver.findElement(byMethod).sendKeys(model.getdata());
	}
	
	public String getText(TestStepModel model) {
		String locator = model.getLocator();
		String locaotrvalue = model.getLocaotrvalue();
		By byMethod = getByMethod(locator, locaotrvalue);
		String text = driver.findElement(byMethod).getText();
		return text;
	}



	
	public void waitFor(TestStepModel model) {
		String getdata = model.getdata();
		int waitfor=0;
		if(getdata.contains(".")){
			String substring = getdata.substring(0,getdata.indexOf("."));
			waitfor = Integer.parseInt(substring);
		}
		else{
			waitfor = Integer.parseInt(getdata);
		}
      try {
		Thread.sleep(waitfor);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	}
	
	
	public By getByMethod(String locator,String locaotrvalue){
		try {
			By newInstance = null ;
			switch (locator.toUpperCase().trim()) {
			case "TAGNAME":
				newInstance = By.tagName(locaotrvalue);
				break;
			case "CLASSNAME":
				newInstance = By.className(locaotrvalue);
				break;

			case "LINKTEXT":
				newInstance = By.linkText(locaotrvalue);
				break;

			case "NAME":
				newInstance = By.name(locaotrvalue);
				break;
				
			case "XPATH":
				newInstance = By.xpath(locaotrvalue);
				break;
			case "CSSSELECTOR":
				newInstance = By.cssSelector(locaotrvalue);
				break;

			default:
				break;
			}
			
			/*By newInstance = By.class.newInstance();
	       Method declaredMethod = By.class.getDeclaredMethod(locator, String.class);
			By invoke = (By)declaredMethod.invoke(newInstance, locaotrvalue);*/
			return newInstance;
			
		} catch (Exception  e) {
			e.printStackTrace();
		}
		return null;
		
	}
	

}
