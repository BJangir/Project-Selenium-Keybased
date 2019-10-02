package com.xceleratorninja.action;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.core.util.WebDriverUtil;
import com.automation.util.ConnectionUtil;
import com.automation.util.MailUtil;

public class Actions {
	
	public static int invalidLinksCount;
	public static String url;

	final static Logger logger = Logger.getLogger(Actions.class);
	String openBrowser;
	String click;
	String verifyText;
	String getText;
	String setText;
	String getErrorText;
	String clear;
	String countLinks;
	String getBrokenLinks;
	WebDriver driver;

	public Actions() {
		driver = WebDriverUtil.driver;
	}

	public String getopenBrowser() {
		return openBrowser;
	}

	public void openBrowser(TestStepModel tm) {
		Map<String, String> browserProperties = new HashMap<>();
		String openBrowser = tm.getdata();
		openBrowser=refactorExcelValue(openBrowser);
		String[] split = openBrowser.split("\n");
		for (String each : split) {
			String[] split2 = each.split("=");
			browserProperties.put(split2[0], split2[1]);
		}
		
		
		//WebDriverUtil driverUtil = new WebDriverUtil(browserProperties.get("Type"), browserProperties.get("driverpath"));
		WebDriverUtil driverUtil=new WebDriverUtil(browserProperties.get("Type"), browserProperties.get("driverpath"));
		
		if(null!=driverUtil.getDriver()){
			driver=driverUtil.getDriver();
			driver.get(browserProperties.get("URL"));
		}
		else{
			logger.error("driver is null ,Some issue while starting the driver");
		}
		

		
	}
	
	public void resetDBUser(TestStepModel tm){
		ConnectionUtil util=new ConnectionUtil();
		Connection connection = util.getConnection();
		String user = refactorExcelValue(tm.getdata());
		resetUsers(util, connection,user);
		util.closeConnection();
	}

	public String refactorExcelValue(String cellValue){
		String[] textInCells = cellValue.split("\n");
		StringBuffer resultBuffer=new StringBuffer();
		for(String text:textInCells){
			if(!text.trim().equalsIgnoreCase("")&&!text.trim().isEmpty()){
				resultBuffer.append(text.trim()).append("\n");
			}
		}
		StringBuffer deletedLastchar = resultBuffer.deleteCharAt(resultBuffer.lastIndexOf("\n"));
		return deletedLastchar.toString();
	}
	private void resetUsers(ConnectionUtil util, Connection connection,String test_user) {
		String updatequery="update profiles set email_address = ? ,phone =? where email_address = ?";
	      try {
	    	  PreparedStatement preparedStmt = connection.prepareStatement(updatequery);
	    	   String nextEmail = nextEmail();
	    	   String nextPhoneNo = nextPhoneNo();
	    	  preparedStmt.setString(1, nextEmail);
	    	  preparedStmt.setString(2, nextPhoneNo);
	    	 // preparedStmt.setString(3, reader.getKeyValue("test.user"));
	    	  preparedStmt.setString(3,test_user);
	    	  util.updateRecords(updatequery, preparedStmt);
	    	  
	    	   updatequery="update users set username = ? ,email =? where email = ?";
	    	   preparedStmt = connection.prepareStatement(updatequery);
	    	  preparedStmt.setString(1, nextEmail);
	    	  preparedStmt.setString(2, nextEmail);
	    	  preparedStmt.setString(3, test_user);
	    	  util.updateRecords(updatequery, preparedStmt);
	    	  
	    	  
		} catch (SQLException e) {
			logger.error("Update is failled for tables profiles and users");
			e.printStackTrace();
		}
	}
	
	public String nextEmail() {
		SecureRandom random = new SecureRandom();
		String email = new BigInteger(130, random).toString(32);
	    return email+"@gmail.com";
	  }
	
	public String nextPhoneNo() {
		SecureRandom random = new SecureRandom();
		long maximum=9999999999l;
		int minimum=1000000000;
		long randomNum =  random.nextInt(minimum)+ minimum;
		if(randomNum>maximum){
			randomNum=randomNum-minimum;
		}
		return randomNum+"";
	  }
	
	public void closeBrowser(TestStepModel tm) {
		driver.quit();
		}
		
	public void click(TestStepModel model) {

		String locator = model.getLocator();
		String locaotrvalue = model.getLocaotrvalue();
		By byMethod = getByMethod(locator, locaotrvalue);
		//driver.findElement(byMethod).click();
		org.openqa.selenium.interactions.Actions actions=new org.openqa.selenium.interactions.Actions(driver);
		WebElement setTextElement = driver.findElement(byMethod);
		actions.moveToElement(setTextElement);
		actions.click();
		actions.build().perform();
		
		
	}

	public void refreshPage(TestStepModel model) {
		driver.navigate().refresh();
	}

	/**
	 * This method is used for navigating back in a webPage
	 * 
	 * @param model
	 */
	public void navigateBack(TestStepModel model) {
		// driver.navigate()
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("window.history.go(-1)");

		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript("history.go(-1);", new Object[0]);
		}

		driver.navigate().back();

	}

	public String isTextPresentInPage(TestStepModel model) {
		String mytext = null;
		String locator = model.getLocator();
		String locaotrvalue = model.getLocaotrvalue();
		By byMethod = getByMethod(locator, locaotrvalue);
		mytext = driver.findElement(byMethod).getText();
		String[] split = mytext.split("\n");
		List<String> asList = Arrays.asList(split);
		String getdata = model.getdata();
		logger.info(model.getTestcaseId() + "-" + model.getTestStep() + " Actual values=" + asList
				+ " Expected Values= " + getdata);
		boolean contains = asList.contains(getdata);
		return contains ? "true" : "false";

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
			org.openqa.selenium.interactions.Actions actions=new org.openqa.selenium.interactions.Actions(driver);
			WebElement setTextElement = driver.findElement(byMethod);
			actions.moveToElement(setTextElement);
			actions.click();
			actions.perform();
		//	driver.findElement(byMethod).click();
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
		int waitfor = 0;
		if (getdata.contains(".")) {
			String substring = getdata.substring(0, getdata.indexOf("."));
			waitfor = Integer.parseInt(substring);
		} else {
			waitfor = Integer.parseInt(getdata);
		}
		try {
			Thread.sleep(waitfor);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public By getByMethod(String locator, String locaotrvalue) {
		try {
			By newInstance = null;
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

			/*
			 * By newInstance = By.class.newInstance(); Method declaredMethod =
			 * By.class.getDeclaredMethod(locator, String.class); By invoke =
			 * (By)declaredMethod.invoke(newInstance, locaotrvalue);
			 */
			return newInstance;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String countLinks(TestStepModel model) {
		try {
			String locator = model.getLocator();
			String locaotrvalue = model.getLocaotrvalue();
			By byMethod = getByMethod(locator, locaotrvalue);
			List<WebElement> linkList = driver.findElements(byMethod);
			int size = linkList.size();
			// System.out.println(String.valueOf(size));
			return String.valueOf(size);
		} catch (Exception e) {
			e.printStackTrace();
			return String.valueOf(0);
		}

	}

	public String getTitle(TestStepModel model) {
		try {
			String title = driver.getTitle();
			return title;
		} catch (Exception e) {

			return "";
		}

	}
	
	
	public String getBrokenLinks(TestStepModel model){
		invalidLinksCount=0;
		
		try {
			String locator = model.getLocator();
			String locaotrvalue = model.getLocaotrvalue();
			By byMethod = getByMethod(locator, locaotrvalue);
			List<WebElement> anchorList = driver.findElements(byMethod);
			
			for (WebElement link: anchorList){
			url = link.getAttribute("href");
			
			if (url !=null && !url.contains("javascript")){
				
				verifyURLStatus(url);
			} else {
				invalidLinksCount++;
			}
			
		}} catch (Exception e) {
			e.printStackTrace();
		}
		
		return String.valueOf(invalidLinksCount);
	}



	private void verifyURLStatus(String url) {
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		
		try {
			
			HttpResponse response = client.execute(request);
			
			if (response.getStatusLine().getStatusCode()!=200){
				
				invalidLinksCount++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
		
	}
