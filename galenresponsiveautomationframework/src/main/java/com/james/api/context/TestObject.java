package com.james.api.context;

import java.io.IOException;
import org.openqa.selenium.WebDriver;
import com.james.api.service.ServiceApi;


public class TestObject {

	private ServiceApi serviceApi;
	
	private Long timeStart;
	private Long finishTime;
	//private VaultConfig vaultConfig;
	private WebDriver driver;
	
	
	public TestObject() throws IOException {
		this.serviceApi = new ServiceApi();		
	}

	public ServiceApi getServiceApi() {
		return serviceApi;
	}
	
	public void setServiceApi(ServiceApi serviceApi) {
		this.serviceApi = serviceApi;
	}
	
	public Long getTimeStart() {
		return timeStart;
	}
	
	public void setTimeStart(Long timeStart) {
		this.timeStart = timeStart;
	}
	
	public Long getFinishTime() {
		return finishTime;
	}
	
	public void setFinishTime(Long finishTime) {
		this.finishTime = finishTime;
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	
}
