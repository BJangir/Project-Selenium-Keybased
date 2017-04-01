package com.automation.core.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverUtil {
 public static  WebDriver driver ;
	

 
 public WebDriverUtil(String driverType,String driverpath) {
	
	switch (driverType.toUpperCase().trim()) {
	case "CHROME":
		 System.setProperty("webdriver.chrome.driver",driverpath);
		 driver=new ChromeDriver();
		break;

	case "MOZILLA":
		 System.setProperty("webdriver.chrome.driver",driverpath);
		 driver=new FirefoxDriver();
		break;
		
	default:
		break;
	}
	 
	
}
 
 public WebDriverUtil(String driverType,String driverpath,DesiredCapabilities desiredCap) {
	 System.setProperty("webdriver.chrome.driver",driverpath);
	 switch (driverType.toUpperCase().trim()) {
		case "CHROME":
			 driver=new ChromeDriver(desiredCap);
			break;

		case "MOZILLA":
			 driver=new FirefoxDriver(desiredCap);
			break;
			
		default:
			break;
		}
}
 
 public  WebDriver getDriver(){
	 return driver;
 }
 
 public void quitDriver(){
	 driver.quit();
 }
 
 public void stopDriver(){
	 driver.close();
 }
 
 public void openBrowser(String url){
   driver.get(url);
 }
 
 public void openBrowser(String url,DesiredCapabilities desiredCap ){
	 
	   driver.get(url);
	 }
 
 
}
