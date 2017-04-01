package com.automation.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.core.util.WebDriverUtil;
import com.automation.util.MailUtil;

public class Actions {
	
	final static Logger logger = Logger.getLogger(Actions.class);
	String verifyText;
	String getText;
	String getErrorText;
	String clear;
	WebDriver driver;
	public Actions() {
		driver=WebDriverUtil.driver;
	}
	

	
	public void openBrowser(TestStepModel tm) {
		Map<String,String> browserProperties=new HashMap<>();
		String openBrowser = tm.getdata();
		String[] split = openBrowser.split("\n");
		for(String each:split){
			String[] split2 = each.split("=");
			browserProperties.put(split2[0], split2[1]);
		}
	
		WebDriverUtil driverUtil=new WebDriverUtil(browserProperties.get("Type"), browserProperties.get("driverpath"));
		 driver = driverUtil.getDriver();
		 driver.get(browserProperties.get("URL"));
		
	}
	public void closeBrowser(TestStepModel tm) {
		driver.quit();
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
		logger.info(model.getTestcaseId()+"-"+model.getTestStep()+" Actual values="+asList+" Expected Values= "+getdata);
		boolean contains = asList.contains(getdata);
		return contains?"true":"false";

		
	}
	

	public String activateLink(TestStepModel model){
		try {
			String activationLinkFromMail = getActivationLinkFromMail(model);
			if(null!=activationLinkFromMail && !activationLinkFromMail.isEmpty()){
				String resultOfActivationLink = null ;
				String currentWindowHandler = driver.getWindowHandle();
				((JavascriptExecutor)driver).executeScript("window.open();");
				Set<String> windowHandles = driver.getWindowHandles();
				for(String eachtitle:windowHandles){
					if(!eachtitle.equals(currentWindowHandler)){
						driver.switchTo().window(eachtitle);
						driver.get(activationLinkFromMail);
						 resultOfActivationLink = driver.findElement(By.cssSelector("body")).getText();
						//driver.close(); we will not close as we will login in same window.
					}
				}
				logger.info(model.getTestcaseId()+"-"+model.getTestStep()+" Activation link is "+activationLinkFromMail);
				logger.info(model.getTestcaseId()+"-"+model.getTestStep()+" Result of activatio link  "+resultOfActivationLink);
				//driver.switchTo().window(currentWindowHandler); we will not switched to current window as will continue in same
				return resultOfActivationLink.contains("Sorry")? true+"": false+"";
			}
			else{
				logger.error(model.getTestcaseId()+"-"+model.getTestStep()+" No link Found in the Given Email  "+activationLinkFromMail);
				return false+"";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false+"";
		}
		
	}
	
	public void deleteMail(TestStepModel model){
		try {
			MailUtil mailUtil=new MailUtil();
			String senderEmailId = model.getdata();
			boolean deleteMail = mailUtil.deleteMail(senderEmailId);
			logger.info("Deleation of Email from "+senderEmailId+" is "+deleteMail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getActivationLinkFromMail(TestStepModel model){
		  String activationlink = null;
		try {
			String senderEmailId = model.getdata();
			MailUtil mailUtil=new MailUtil();
			String getcontent = mailUtil.getcontent(senderEmailId);
			
			if(null==getcontent || getcontent.isEmpty()){
              int timeout=10;
              while(timeout!=0){
            	  timeout--;
            	  Thread.sleep(3000);
            	   getcontent = mailUtil.getcontent(senderEmailId);
            	  if(!getcontent.isEmpty()){
            		  break;
            	  }
              }
			}
			String[] split = getcontent.split("\n");
			for(String s:split){
			  if(s.contains("copy the")&& s.contains("link")&& s.contains("in your browser")){
				   activationlink = s.substring(s.indexOf("http"), s.indexOf("in your browser"));
				  logger.info("Activation link"+activationlink);
				  return activationlink;
			  }
			}
		} catch (Exception e) {
			e.printStackTrace();
			return activationlink;
		}
		return activationlink;
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
		org.openqa.selenium.interactions.Actions actions=new org.openqa.selenium.interactions.Actions(driver);
		WebElement setTextElement = driver.findElement(byMethod);
		actions.moveToElement(setTextElement);
		actions.click();
		actions.sendKeys(model.getdata());
		actions.build().perform();
		//driver.findElement(byMethod).sendKeys(model.getdata());
	}
	
	
	public void selectFromDropBox(TestStepModel model) {
		try{
			String locator = model.getLocator();
			String locaotrvalue = model.getLocaotrvalue();
			By byMethod = getByMethod(locator, locaotrvalue);
			driver.findElement(byMethod).click();
			 byMethod = getByMethod("xpath", "//*[text()='"+model.getdata()+"']");
			driver.findElement(byMethod).click();	
		}
		catch (Exception e) {
        logger.error(model.getdata()+" does not found or"+model.getLocaotrvalue()+" does not found");
		}
		
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
