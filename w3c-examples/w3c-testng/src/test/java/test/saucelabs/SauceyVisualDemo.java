package test.saucelabs;

import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebElement;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SauceyVisualDemo {
    protected RemoteWebDriver driver;
    protected Boolean result;
    boolean createVisualDiffs = true;
    //boolean createVisualDiffs = false;
    protected String browserName;
    protected String browserVersion;
    protected String viewportSize;
    protected String platformName;
    protected String demoPrefix;
    protected String testName;
    protected String methodName;

    @BeforeMethod
    @Parameters({"testName", "browserName", "browserVersion", "viewportSize", "platformName"})
    public void setup(Method method, String testName, String browserName, String browserVersion, String viewportSize, String platformName) throws MalformedURLException {
        String username = System.getenv("SAUCE_USERNAME");
        String accessKey = System.getenv("SAUCE_ACCESS_KEY");
        String visualApiKey = System.getenv("SAUCE_VISUAL_API_KEY");

        methodName = method.getName();

        demoPrefix = "Saucey";
        String buildName = demoPrefix + " Visual Demo";

        ChromeOptions chromeOpts = new ChromeOptions();
        chromeOpts.setExperimentalOption("w3c", true);

        MutableCapabilities sauceVisual = new MutableCapabilities();
        sauceVisual.setCapability("apiKey", visualApiKey);
        //sauceVisual.setCapability("group", visualGroup);

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
    @Parameters({"testName", "browserName", "browserVersion", "viewportSize", "platformName"})
    public void SauceDemoVisualTest(String testName, String browserName, String browserVersion, String viewportSize, String platformName) throws AssertionError, InterruptedException {
        driver.get("https://fd29-82-20-169-30.ngrok.io/");
        String getTitle = driver.getTitle();
        String pageLabel = demoPrefix + " " + browserName + " " + browserVersion + " " + platformName + " "
                + viewportSize;
        // Test Login Page on Entry
        driver.executeScript("/*@visual.snapshot*/", "HomePage");

        Assert.assertEquals(getTitle, "Andy's Saucey Demo Page");
        if (getTitle.equals("Andy's Saucey Demo Page")) {
            result = true;
        } else result = false;

        driver.executeScript("/*@visual.snapshot*/", "Saucey - Blank Form");

        WebElement el1 = driver.findElement(By.id("forename"));
        el1.sendKeys("Andy");

        el1 = null;
        el1 = driver.findElement(By.id("familyname"));
        el1.sendKeys("Wyatt");

        el1 = null;
        el1 = driver.findElement(By.id("emailAddr"));
        el1.sendKeys("andy@saucelabs.com");

        driver.executeScript("/*@visual.snapshot*/", "Saucey - Filled Form");

        el1 = null;
        el1 = driver.findElement(By.id("submit"));
        el1.click();

        /* check that you are now on page 2 */
        el1 = null;
        el1 = driver.findElementByCssSelector("body > marquee > marquee");

        driver.executeScript("/*@visual.snapshot*/", "Saucey - Page 2");
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        driver.executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
        driver.executeScript("/*@visual.end*/");
        driver.quit();
    }
}