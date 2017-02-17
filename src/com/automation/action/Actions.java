package com.automation.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.automation.core.util.WebDriverUtil;

public class Actions {
	
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
	public String getClick() {
		return click;
	}
	public void setClick(String click) {
		this.click = click;
	}
	public String getVerifyText() {
		return verifyText;
	}
	public void setVerifyText(String verifyText) {
		this.verifyText = verifyText;
	}
	public String getGetText(TestStepModel model) {
		String mytext = null;
		String locator = model.getLocator();
		String locaotrvalue = model.getLocaotrvalue();
	try {
		By byMethod = getByMethod(locator, locaotrvalue);
		mytext= driver.findElement(byMethod).getText();
	} catch (Exception  e) {
		e.printStackTrace();
	}
	
	return mytext;
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
		boolean contains = asList.contains(getdata);
		return contains?"true":"false";

		
	}
	
	public By getByMethod(String locator,String locaotrvalue){
		try {
			By newInstance = null ;
			switch (locator.toUpperCase()) {
			case "TAGNAME":
				newInstance = By.tagName(locaotrvalue);
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
	
	public void setGetText(String getText) {
		this.getText = getText;
	}
	public String getSetText() {
		return setText;
	}
	public void setSetText(String setText) {
		this.setText = setText;
	}
	public String getGetErrorText() {
		return getErrorText;
	}
	public void setGetErrorText(String getErrorText) {
		this.getErrorText = getErrorText;
	}
	public String getClear() {
		return clear;
	}
	public void setClear(String clear) {
		this.clear = clear;
	}
	
	
	

}
