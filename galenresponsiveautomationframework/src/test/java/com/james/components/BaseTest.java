package com.james.components;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.DataProvider;

import com.galenframework.api.Galen;
import com.galenframework.browser.SeleniumBrowser;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.support.LayoutValidationException;
import com.galenframework.testng.GalenTestNgTestBase;

public class BaseTest  extends GalenTestNgTestBase{
	
	public WebDriver driver;
	private static final String baseURL = "https://www.lambdatest.com/";

	@Override
	public WebDriver createDriver(Object[] args) {
		
		String testBrowser="chrome";
		String platformName="local";
		
		if (platformName.equalsIgnoreCase("local")) {
			driver =  getDriverForLocalEnvironment(driver, testBrowser);
			
			//resize the window
			if (args.length > 0) {
				if (args[0] !=null && args[0] instanceof TestDevice) {
					TestDevice device = (TestDevice) args[0];
					if (device.getScreenSize() != null) {
						driver.manage().window().setSize(device.getScreenSize());
					}
				}
			}
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
		}
		
		return driver;
	}
	
	public void loadURL() {
		getDriver().get(baseURL);
	}
	
	@DataProvider(name = "devices")
	public Object[][] devices(){
		
		return new Object[][] {
			{new TestDevice("mobile", new Dimension(450, 800), Arrays.asList("mobile"))},
			{new TestDevice("tablet", new Dimension(750, 800), Arrays.asList("tablet"))},
			{new TestDevice("desktop", new Dimension(1024, 800), Arrays.asList("desktop"))},
		};
	}
	
	// set up the local environment
    private WebDriver getDriverForLocalEnvironment(WebDriver driver, String testBrowser){

    	String gridURL ="";
    	
    	if(testBrowser.contains("chrome")) {
   
    		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
    		chromePrefs.put("profile.default_content_settings.popups", 0);
    		chromePrefs.put("safebrowsing.enabled", "true");
    		chromePrefs.put("disable-popup-blocking", "true");
    		chromePrefs.put("download.prompt_for_download", "false");
    		//Chrome Options
    		ChromeOptions options = new ChromeOptions();
    		//options.setExperimentalOption("useAutomationExtension", "false");
    		options.setExperimentalOption("prefs", chromePrefs);
    		//options.addArguments("test-type");
    		options.addArguments("--disable-extensions");
    		options.addArguments("--disable-web-security");
    		options.addArguments("--ignore-certificate-errors");
    		options.addArguments("--no-sandbox");
    		//Capabilities
    		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
    		capabilities = DesiredCapabilities.chrome();
    		capabilities.setBrowserName("chrome");
    		capabilities.setPlatform(Platform.WINDOWS);
    		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
    		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
    		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    		
    		System.setProperty("webdriver.chrome.driver", ".\\drivers\\chromedriver.exe");
			driver = new ChromeDriver(options);
    		/*
    		if (useGrid) {
    			try {
    				driver = new RemoteWebDriver(new URL(gridURL), options);
    			} catch (MalformedURLException e) {
    				System.out.println("Error in launching Chrome browser in Grid");
    			}
    		}
    		else {
    			System.setProperty("webdriver.chrome.driver", ".\\drivers\\chromedriver.exe");
    			driver = new ChromeDriver();
    		}
    		*/
 
    	}
    	else {
    		ChromeDriverService service = new ChromeDriverService.Builder().usingAnyFreePort().build();
    		driver = new ChromeDriver(service);
    	}
    		
        return driver;
    }
    
    //method  to execute Galen specs and generate reports
    public void checkPageLayout(String specPath, List<String> includedTags) throws IOException{
    	SectionFilter sectionFilter = new SectionFilter(includedTags, Collections.<String>emptyList());
    	
    	LayoutReport layoutReport = Galen.checkLayout(new SeleniumBrowser(getDriver()), specPath, sectionFilter, new Properties(), null,null,null);
    	
    	 List<GalenTestInfo> tests = new LinkedList<GalenTestInfo>();
         GalenTestInfo test = GalenTestInfo.fromString(testInfo.get().getName());

         test.getReport().layout(layoutReport, "Checking " + this.getClass().getSimpleName() + " for " +
                 includedTags.get(0));
         tests.add(test);
         new HtmlReportBuilder().build(tests, "target/galen-" + includedTags.get(0) + "-reports-" + getCurrentDate());

         if (layoutReport.errors() > 0){
              throw new LayoutValidationException(specPath, layoutReport, sectionFilter);
          }
    }

	private String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}

}
