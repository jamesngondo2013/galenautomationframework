package com.james.tests;

import java.io.IOException;

import org.testng.annotations.Test;

import com.james.components.BaseTest;
import com.james.components.TestDevice;

public class TestHomePage extends BaseTest{

	private static final String lambdaTestPage = "src/main/resources/specs/lambdatest.gspec";
	
	@Test(dataProvider = "devices")
	public void homePageTest(TestDevice device) throws IOException {
		loadURL();
		checkLayout(lambdaTestPage, device.getTags());
	}
}
