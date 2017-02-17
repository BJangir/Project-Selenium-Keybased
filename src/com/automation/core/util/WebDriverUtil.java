package com.automation.core.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverUtil {
 public static  WebDriver driver ;
	

 
 public WebDriverUtil(String driverType,String driverpath) {
	 System.setProperty("webdriver.chrome.driver",driverpath);
	 driver=new ChromeDriver();
}
 
 public WebDriverUtil(String driverType,String driverpath,DesiredCapabilities desiredCap) {
	 System.setProperty("webdriver.chrome.driver",driverpath);
	 driver=new ChromeDriver(desiredCap);
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
