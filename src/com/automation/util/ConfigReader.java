package com.automation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	
	 Properties properties;
	 
	public ConfigReader() {
		String property = System.getProperty("conf.path", "conf.properties");
     properties=new Properties();
     try {
		properties.load(new FileInputStream(new File(property)));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	}

	
	public Properties getProperties(){
		return properties;
	}
	
	public String getKeyValue(String key){
		String property = properties.getProperty(key);
		return property;
		
	}
}
