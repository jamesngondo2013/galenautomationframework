package managers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.james.api.context.TestObject;

public class BrowserFactory {

	public WebDriver driver;
	public TestObject testObject;
	
	
	public BrowserFactory(TestObject testObject) {
		this.testObject = testObject;
	}
	/*
	public void launchDriver(String browser, boolean useGrid) {
		//String gridURL = "";
		String gridURL = "http//testing122334.test.com:4600/wd/hub";
		
		if (System.getProperty("HUB_HOST") != null) {
			gridURL = "http://" + System.getProperty("HUB_HOST") + ":4444/wd/hub";
		}
		
		switch (browser.toLowerCase()) {
		case "chrome":
			HashMap<String, Object> chromePrefs = new HashMap<>();
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
				//System.setProperty("webdriver.chrome.driver", testObject.getServiceApi().getProp().getProperty("BROWSER").trim());
				System.setProperty("webdriver.chrome.driver", ".\\drivers\\chromedriver.exe");
				driver = new ChromeDriver();
			}
			break;
		case "ie":
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			InternetExplorerOptions ieOptions = new InternetExplorerOptions();
			ieOptions.merge(ieCapabilities);
			if (useGrid) {
				try {
					driver = new RemoteWebDriver(new URL(gridURL), ieOptions);
				} catch (MalformedURLException e) {
					System.out.println("Error in launching IE browser in Grid");
				}
			}
			else {
				System.setProperty("webdriver.ie.driver", "C:\\drivers\\IEDriver.exe");
				driver = new InternetExplorerDriver(ieOptions);
			}
		default:
			break;
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(90, TimeUnit.SECONDS);
		testObject.setDriver(driver);
	}
	*/
	public WebDriver getDriver() {
		return driver;
	}
	
}
