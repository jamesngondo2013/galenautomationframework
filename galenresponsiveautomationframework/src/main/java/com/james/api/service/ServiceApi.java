package com.james.api.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;

public class ServiceApi {

	private String environment;
	private String category;
	private String tenant;
	private String grid;
	private String gridURL;
	private String platform;
	private String browser;
	private String oauth;
	private OutputStream outputStream;
	private Properties prop;

	
	public ServiceApi() throws IOException {
		setEnvironment();
	}
	
	public String getEnvironment() {
		return environment;
	}
	
	public String getPlatform() {
		return platform;
	}
	
	public String getBrowser() {
		browser = prop.getProperty("BROWSER");
		return browser;
	}
	
	public String getGridURL() {
		gridURL = prop.getProperty("GRIDURL");
		return gridURL;
	}
	
	
	public void setEnvironment() throws IOException {
		String env = getCurrentEnv();
		
		if(environment == null) {
			InputStream input = new FileInputStream("config/project.properties");
			prop = new Properties();
			
			prop.load(input);
			//initialize from properties file
			category = prop.getProperty("CATEGORY_NAME").trim();
			tenant = prop.getProperty("TENANT_NAME").trim();
			platform = prop.getProperty("PLATFORM").trim();
			
			if (env == null) {
				if (prop.getProperty("ENV") == null) {
					environment = "INT".toLowerCase();
				}
				else {
					environment = prop.getProperty("ENV");
				}
			}
			else {
				environment = env.trim();
			}
			//log.info("[Environment] = " + environment);
			System.out.println("[Environment] = " + environment);
		}
	}
	
	 public Set<Object> getAllKeys(){
	        Set<Object> keys = prop.keySet();
	        return keys;
	    }
	     
	    public String getPropertyValue(String key){
	        return this.prop.getProperty(key);
	    }
	
	public String getCurrentEnv() {
		String currentEnv;
		try {
			currentEnv = System.getenv("ENV");
		} catch (Exception e) {
			currentEnv = null;
		}
		return currentEnv;
	}
	
	public String getCategory() {
		return category;
	}
	
	public boolean isOauthUserEnabled() {
		oauth = prop.getProperty("OAUTH_USER_PASSWORD");
		return Boolean.valueOf(oauth.toLowerCase());
	}
	
	public String getTenant() {
		return tenant;
	}
	public boolean isDatabaseIconfigEnabled() {
		oauth = prop.getProperty("db.iconfig.credentials");
		return Boolean.valueOf(oauth.toLowerCase());
	}

	public boolean isGridEnabled()
	{
		grid = prop.getProperty("GRID");
		return Boolean.valueOf(grid.toLowerCase());
	}
	
	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public Properties getProp() {
		return prop;
	}
	
	public String getProperty(String key) {
		return prop.getProperty(key);
	}
	
	public String getSystemPropertyDir(String key) {
		return System.getProperty(key);
	}
	
	public String maskData(String data) {
	
		data = data.replaceAll("Authorization=Bearer (\\w*)", "Authorization=**masked");
		data = data.replaceAll("client_secret=(\\w*)-(\\w*)-(\\w*)-(\\w*)", "client_secret=**masked");
		data = data.replaceAll("([0-9]{3})-([0-9]{2})-([0-9]{4})", "XXX-XX-XXXX");
		return data;
	}
	
	
}
