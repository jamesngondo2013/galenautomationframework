package com.james.tests;

import java.io.IOException;

import org.testng.annotations.Test;

import com.james.components.BaseTest;
import com.james.components.TestDevice;

import managers.FileReaderManager;
import testdatatypes.CustomerJson;

public class TestHomePage extends BaseTest{

	private static final String lambdaTestPage = "src/main/resources/specs/lambdatest.gspec";
	private CustomerJson customer;
	
	@Test(dataProvider = "devices")
	public void homePageTest(TestDevice device) throws IOException {
		customer = FileReaderManager.getInstance().getJsonReader().getCustomerByPPSN("ACP PPSN");
		System.out.println("PPSN: "+ customer.ppsn);
		System.out.println("Username: "+ customer.username);
		loadURL();
		//checkLayout(lambdaTestPage, device.getTags());
		checkPageLayout(lambdaTestPage, device.getTags());
	}
}
