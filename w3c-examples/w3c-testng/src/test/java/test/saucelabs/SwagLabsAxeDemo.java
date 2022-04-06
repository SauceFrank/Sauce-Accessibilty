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
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.selenium.AxeBuilder;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SwagLabsAxeDemo {
    protected RemoteWebDriver driver;
    protected Boolean result;
    protected String browserName;
    protected String browserVersion;
    protected String viewportSize;
    protected String platformName;
    protected String demoPrefix;
    protected String testName;
    protected String methodName;
    protected boolean axeTest;
    protected boolean perfTest;

    @BeforeMethod
    @Parameters({"testName", "browserName", "browserVersion", "screenResolution", "platformName"})
    public void setup(Method method, String testName, String browserName, String browserVersion, String screenResolution, String platformName) throws MalformedURLException {
        axeTest = true;
        perfTest = true;

        String username = System.getenv("SAUCE_USERNAME");
        String accessKey = System.getenv("SAUCE_ACCESS_KEY");

        methodName = method.getName();

        demoPrefix = "SwagLabs";
        String buildName = demoPrefix + " Axe Demo";

        ChromeOptions chromeOpts = new ChromeOptions();
        chromeOpts.setExperimentalOption("w3c", true);

        MutableCapabilities sauceOpts = new MutableCapabilities();
        sauceOpts.setCapability("name", testName);
        sauceOpts.setCapability("build", buildName);
        sauceOpts.setCapability("seleniumVersion", "3.141.59");
        sauceOpts.setCapability("username", username);
        sauceOpts.setCapability("accessKey", accessKey);
        sauceOpts.setCapability("tags", demoPrefix + "-Axe");
        if (perfTest) {
            if (browserName.equals("chrome")) {
                sauceOpts.setCapability("extendedDebugging", true);
                sauceOpts.setCapability("capturePerformance", true);
            }
        }


        DesiredCapabilities caps = new DesiredCapabilities();
        if (browserName.equals("chrome")) {
            caps.setCapability(ChromeOptions.CAPABILITY, chromeOpts);
        }
        caps.setCapability("sauce:options", sauceOpts);
        caps.setCapability("browserName", browserName);
        caps.setCapability("browserVersion", browserVersion);
        caps.setCapability("platformName", platformName);
        caps.setCapability("resolution", screenResolution);
        String sauceUrl = "https://ondemand.eu-central-1.saucelabs.com/wd/hub";
        URL url = new URL(sauceUrl);
        driver = new RemoteWebDriver(url, caps);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }

    @Test
    @Parameters({"testName", "browserName", "browserVersion", "screenResolution", "platformName"})
    public void SauceDemoAxeTest(String testName, String browserName, String browserVersion, String screenResolution, String platformName) throws AssertionError, InterruptedException {
        driver.get("https://www.saucedemo.com");
        String getTitle = driver.getTitle();
        String pageLabel = demoPrefix + " " + testName + " " + browserName + " " + browserVersion + " " + screenResolution + " " + platformName;

        Assert.assertEquals(getTitle, "Swag Labs");
        if (getTitle.equals("Swag Labs")) {
            result = true;
        } else result = false;

        AxeBuilder axeBuilder = new AxeBuilder();

        if (axeTest) {
            Results accessibilityResults = axeBuilder.analyze(driver);
            //Assert.assertEquals(3, accessibilityResults.getViolations().size());
        }


        WebElement el1 = driver.findElement(By.id("user-name"));
        el1.sendKeys("standard_user");

        // Login with no password - it should fail
        el1 = null;
        el1 = driver.findElement(By.id("login-button"));
        el1.click();

        if (axeTest) {
            //accessibilityResults = axeBuilder.analyze(driver);
            //Assert.assertEquals(3, accessibilityResults.getViolations().size());
            axeBuilder.analyze(driver);
        }


        // Login correctly
        el1 = null;
        el1 = driver.findElement(By.id("password"));
        el1.sendKeys("secret_sauce");

        el1 = null;
        el1 = driver.findElement(By.id("login-button"));
        el1.click();
        Thread.sleep(1000);
        el1 = null;

        if (axeTest) {
            //accessibilityResults = axeBuilder.analyze(driver);
            //Assert.assertEquals(3, accessibilityResults.getViolations().size());
            axeBuilder.analyze(driver);
        }

        el1 = driver.findElement(By.cssSelector("#header_container > div.header_secondary_container > div.right_component > span > select"));
        el1.click();

        // Sort the items differently
        el1 = null;
        el1 = driver.findElement(By.cssSelector("#header_container > div.header_secondary_container > div.right_component > span > select > option:nth-child(2)"));
        el1.click();

        if (axeTest) {
            //accessibilityResults = axeBuilder.analyze(driver);
            //Assert.assertEquals(3, accessibilityResults.getViolations().size());
            axeBuilder.analyze(driver);
        }
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        driver.executeScript("sauce:job-tags=NFT,Accessibility,Performance");
        driver.executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
        driver.quit();
    }
}