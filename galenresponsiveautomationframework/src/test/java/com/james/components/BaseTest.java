package com.james.components;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
import org.testng.annotations.Listeners;

import com.galenframework.api.Galen;
import com.galenframework.browser.SeleniumBrowser;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.TestReport;
import com.galenframework.reports.model.LayoutObject;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.reports.model.LayoutSection;
import com.galenframework.reports.model.LayoutSpec;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.support.LayoutValidationException;
import com.galenframework.testng.GalenTestNgTestBase;
import com.galenframework.support.GalenReportsContainer;
import com.galenframework.testng.GalenTestNgReportsListener;

/**
 * Base class for all Galen tests. <br>
 * <br>
 * To run with maven against Selenium grid use: <br>
 * mvn verify -Dselenium.grid=http://grid-ip:4444/wd/hub
 */
@Listeners(value = GalenTestNgReportsListener.class)
public class BaseTest  extends GalenTestNgTestBase{
	
	public WebDriver driver;
	private static final String baseURL = "https://www.lambdatest.com/";
	public static final String USERNAME = "jamesngondo1";
	public static final String AUTOMATE_KEY = "mupd3TBZYHzNASRZqA6y";
	public static final String BROWSERSTACK_URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

	@Override
	public WebDriver createDriver(Object[] args) {
		
		String testBrowser="chromeDesktop";
		//String platformName="local";
		String platformName="browserstack";
		boolean useGrid = false;
		
		if (platformName.equalsIgnoreCase("local")) {
			driver =  getDriverForLocalEnvironment(driver, testBrowser, useGrid);
			
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
		else if (platformName.equalsIgnoreCase("browserstack")) {		
			try {
				driver = getDriverForBrowserStack(driver, testBrowser);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
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
    private WebDriver getDriverForLocalEnvironment(WebDriver driver, String testBrowser, boolean useGrid){

    	String gridURL ="http://vc2ddjdjdjdjd.tkc.com:4600/wd/hub";
    	
    	if (System.getProperty("HUB_HOST") != null) {
    		gridURL = "http://" + System.getProperty("HUB_HOST") + ":4444/wd/hub";
		}
    	
    	if(testBrowser.equalsIgnoreCase("chrome")) {
   
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
 
    	}
    		
        return driver;
    }
    
    private WebDriver getDriverForBrowserStack(WebDriver driver, String device) throws MalformedURLException {
        
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
		/*
		capabilities = DesiredCapabilities.chrome();
		capabilities.setBrowserName("chrome");
		capabilities.setPlatform(Platform.WINDOWS);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		*/
       // DesiredCapabilities caps = new DesiredCapabilities();
        //caps.setCapability("realMobile", System.getProperty("browserStack.realMobile"));
        //caps.setCapability("realMobile", System.getProperty("browserStack.captureVideo"));
        //caps.setCapability("acceptSslCerts", "true");

        if (device.equalsIgnoreCase("iPhone7")) {
        	capabilities.setCapability("browserName", "iPhone");
        	capabilities.setCapability("device", "iPhone 7");
        } else if (device.equalsIgnoreCase("iPhone8")) {
        	capabilities.setCapability("browserName", "iPhone");
        	capabilities.setCapability("device", "iPhone 8");
        } else if (device.equalsIgnoreCase("iPhoneX")) {
        	capabilities.setCapability("browserName", "iPhone");
        	capabilities.setCapability("device", "iPhone X");
        } else if (device.equalsIgnoreCase("chromeDesktop")) {
        	capabilities.setCapability("os", "Windows");
        	capabilities.setCapability("os_version", "10");
        	capabilities.setCapability("browser", "Chrome");
        	capabilities.setCapability("browser_version", "80");
        	capabilities.setCapability("name", "jamesngondo1's First Test");
        	capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        }

        return new RemoteWebDriver(new URL(BROWSERSTACK_URL), options);
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
    
    public void checkPageLayout(final String pSpecPath, final TestDevice device, final List<String> groups) throws IOException, URISyntaxException, Exception {
		final String fullSpecPath;
		final String pName = getCaller() + " on " + device;
		if (BaseTest.class.getResource(pSpecPath) != null) {
			fullSpecPath = BaseTest.class.getResource(pSpecPath).toURI()
					.getPath();
		} else {
			fullSpecPath = pSpecPath;
		}
		TestReport test = GalenReportsContainer.get().registerTest(pName, groups);
		LayoutReport layoutReport = Galen.checkLayout(getDriver(), fullSpecPath, new SectionFilter(device.getTags(),null),
				new Properties(), null,null);
		layoutReport.setTitle(pName);
		test.layout(layoutReport, pName);
		if (layoutReport.errors() > 0) {
			final StringBuffer errorDetails = new StringBuffer();
			for (LayoutSection layoutSection : layoutReport.getSections()) {
				final StringBuffer layoutDetails = new StringBuffer();
				layoutDetails.append("\n").append("Layout Section: ")
						.append(layoutSection.getName()).append("\n");
				for (LayoutObject layoutObject : layoutSection.getObjects()) {
					boolean hasErrors = false;
					final StringBuffer errorElementDetails = new StringBuffer();
					errorElementDetails.append("  Element: ").append(
							layoutObject.getName());
					for (LayoutSpec layoutSpec : layoutObject.getSpecs()) {
						if (layoutSpec.getErrors() != null	&& layoutSpec.getErrors().size() > 0) {
							errorElementDetails.append(layoutSpec
									.getErrors().toString());
							hasErrors = true;
						}
					}
					if (hasErrors) {
						errorDetails.append("ViewPort Details: ")
								.append(device).append("\n");
						errorDetails.append(layoutDetails);
						errorDetails.append(errorElementDetails).append("\n");
					}
				}
			}
			throw new RuntimeException(errorDetails.toString());
		}
	}

	private String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	
	private static String getCaller() throws ClassNotFoundException {
		Throwable t = new Throwable();
		StackTraceElement[] elements = t.getStackTrace();
		String callerMethodName = elements[2].getMethodName();
		String callerClassName = elements[2].getClassName();
		return callerClassName + "->" + callerMethodName;
	}

}
