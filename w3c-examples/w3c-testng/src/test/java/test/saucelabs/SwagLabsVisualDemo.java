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

public class SwagLabsVisualDemo {
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
    @Parameters({"testName","browserName","browserVersion","viewportSize","platformName"})
    public void setup(Method method, String testName, String browserName, String browserVersion, String viewportSize, String platformName) throws MalformedURLException {
        String username = System.getenv("SAUCE_USERNAME");
        String accessKey = System.getenv("SAUCE_ACCESS_KEY");
        String visualApiKey = System.getenv("SAUCE_VISUAL_API_KEY");

        methodName = method.getName();

        demoPrefix = "SwagLabs";
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
    @Parameters({"testName","browserName","browserVersion","viewportSize","platformName"})
    public void SauceDemoVisualTest(String testName, String browserName, String browserVersion, String viewportSize, String platformName) throws AssertionError, InterruptedException {
        driver.get("https://www.saucedemo.com");
        String getTitle = driver.getTitle();
        String pageLabel = demoPrefix + " " + browserName + " " + browserVersion + " " + platformName + " "
+ viewportSize;
        // Test Login Page on Entry
        driver.executeScript("/*@visual.snapshot*/", "HomePage");

        Assert.assertEquals(getTitle, "Swag Labs");
        if (getTitle.equals("Swag Labs")) {
            result = true;
        }else result = false;

        WebElement el1 = driver.findElement(By.id("user-name"));
        el1.sendKeys("standard_user");

        // Login with no password - it should fail
        el1 = null;
        el1 = driver.findElement(By.id("login-button"));
        el1.click();
        driver.executeScript("/*@visual.snapshot*/", "Swag Labs - Failed Login");

        // Login correctly
        el1 = null;
        el1 = driver.findElement(By.id("password"));
        el1.sendKeys("secret_sauce");

        el1 = null;
        el1 = driver.findElement(By.id("login-button"));
        el1.click();
        Thread.sleep(1000);

        if (createVisualDiffs) {
            //OK mess with the layout
            driver.executeScript("document.querySelector('#item_4_title_link > div').style.color = 'black';");
            driver.executeScript("document.querySelector('#item_0_title_link > div').innerText = 'Sauce Labs Bike Light Saber';");
            driver.executeScript("document.querySelector('#item_1_title_link > div').align = 'right';");
            driver.executeScript("document.querySelector('#item_5_title_link > div').innerText = 'Sauce Labs Jacket';");
        }

        driver.executeScript("/*@visual.snapshot*/", "Swag Labs - Product Page - A2Z");

        // capture screen with sort menu shown
        el1 = null;

        el1 = driver.findElement(By.cssSelector("#header_container > div.header_secondary_container > div.right_component > span > select"));
        el1.click();
        driver.executeScript("/*@visual.snapshot*/", "Swag Labs - Product Page with sort menu shown");

        // Sort the items differently
        el1 = null;
        el1 = driver.findElement(By.cssSelector("#header_container > div.header_secondary_container > div.right_component > span > select > option:nth-child(2)"));
        el1.click();
        driver.executeScript("/*@visual.snapshot*/", "Swag Labs - Product Page - Z2A");

        // Sort the items differently - but with a random menu selection
        Random rand = new Random();
        int menuItem = rand.nextInt(4);
        if (menuItem < 1) {
            menuItem = 4;
        }
        el1 = null;
        el1 = driver.findElement((By.cssSelector("#header_container > div.header_secondary_container > div.right_component > span > select > option:nth-child(" + menuItem + ")")));
        el1.click();
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        driver.executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
        driver.executeScript("/*@visual.end*/");
        driver.quit();
    }
}