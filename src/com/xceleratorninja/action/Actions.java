package com.xceleratorninja.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;

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
	static WebDriver driver;

	public Actions() {
		driver = WebDriverUtil.driver;
	}

	public String getopenBrowser() {
		return openBrowser;
	}

	public void openBrowser(TestStepModel tm) {
		Map<String, String> browserProperties = new HashMap<>();
		String openBrowser = tm.getdata();
		String[] split = openBrowser.split("\n");
		for (String each : split) {
			String[] split2 = each.split("=");
			browserProperties.put(split2[0], split2[1]);
		}
		WebDriverUtil driverChrome = new WebDriverUtil("Chrome", browserProperties.get("driverpath"));
		driver = driverChrome.getDriver();
		driver.get(browserProperties.get("URL"));

		WebDriverUtil driverFF = new WebDriverUtil("Mozilla", browserProperties.get("driverpath"));
		driver = driverFF.getDriver();
		driver.get(browserProperties.get("URL"));
		
		WebDriverUtil driverIE = new WebDriverUtil("IE", browserProperties.get("driverpath"));
		driver = driverIE.getDriver();
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
