package test.swisscom;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SwisscomVisualDemo {
    protected RemoteWebDriver driver;
    protected Boolean result;
    //protected boolean createVisualDiffs = false;
    protected String browserName;
    protected String browserVersion;
    protected String viewportSize;
    protected String platformName;
    protected String demoPrefix;
    protected String testName;
    protected String methodName;

    @BeforeMethod
    @Parameters({"testName","browserName","browserVersion","viewportSize","platformName"})
    public void setup(Method method, String testName, String browserName, String browserVersion, String viewportSize, String platformName) throws MalformedURLException {
        String username = System.getenv("SAUCE_USERNAME");
        String accessKey = System.getenv("SAUCE_ACCESS_KEY");
        String visualApiKey = System.getenv("SAUCE_VISUAL_API_KEY");
        methodName = method.getName();

        demoPrefix = "Swisscom";
        String buildName = demoPrefix + " Visual Demo";

        ChromeOptions chromeOpts = new ChromeOptions();
        chromeOpts.setExperimentalOption("w3c", true);

        MutableCapabilities sauceVisual = new MutableCapabilities();
        sauceVisual.setCapability("apiKey", visualApiKey);
        //sauceVisual.setCapability("projectName", demoPrefix + " Homepage-" + viewportSize);
        sauceVisual.setCapability("projectName", demoPrefix + " Homepage");
        sauceVisual.setCapability("viewportSize", viewportSize);

        MutableCapabilities sauceOpts = new MutableCapabilities();
        sauceOpts.setCapability("name", testName);
        sauceOpts.setCapability("build", buildName);
        sauceOpts.setCapability("seleniumVersion", "3.141.59");
        sauceOpts.setCapability("username", username);
        sauceOpts.setCapability("accessKey", accessKey);
        sauceOpts.setCapability("tags", demoPrefix + "-Visual");

        DesiredCapabilities caps = new DesiredCapabilities();
        if (browserName.equals("chrome")) {
            caps.setCapability(ChromeOptions.CAPABILITY, chromeOpts);
        }
        caps.setCapability("sauce:options", sauceOpts);
        caps.setCapability("browserName", browserName);
        caps.setCapability("browserVersion", browserVersion);
        caps.setCapability("platformName", platformName);
        caps.setCapability("sauce:visual", sauceVisual);

        //String sauceUrl = "https://ondemand.saucelabs.com:443/wd/hub";
        String sauceUrl = "https://hub.screener.io:443/wd/hub";
        URL url = new URL(sauceUrl);
        driver = new RemoteWebDriver(url, caps);
        driver.executeScript("/*@visual.init*/", "Init");
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }

    @Test
    @Parameters({"testName","browserName","browserVersion","viewportSize","platformName"})
    public void SwisscomVisualTest(String testName, String browserName, String browserVersion, String viewportSize, String platformName) throws AssertionError, InterruptedException {
        driver.get("https://www.swisscom.ch");
        String getTitle = driver.getTitle();
        String pageLabel = demoPrefix + " " + browserName + " " + browserVersion + " " + platformName + " "
+ viewportSize;
        // Test Login Page on Entry
        driver.executeScript("/*@visual.snapshot*/", "HomePage");

        Assert.assertEquals(getTitle, "Mobile, TV, Internet: Swisscom Residential Customers | Swisscom");
        if (getTitle.equals("Mobile, TV, Internet: Swisscom Residential Customers | Swisscom")) {
            result = true;
        }else result = false;
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        driver.executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
        driver.executeScript("/*@visual.end*/");
        driver.quit();
    }
}