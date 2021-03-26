import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SwagLabsVisualDemo {
    protected WebDriver driver;
    public Boolean result;
    public static JavascriptExecutor js;
    boolean createVisualDiffs = true;
    //boolean createVisualDiffs = true;
    String myViewportSize = "414x896";
    //String myViewportSize = "1024x768";
    //String myViewportSize = "1280x1024";
    //String myViewportSize = "1920x1200";

    @BeforeMethod
    public void setup(Method method) throws MalformedURLException {
        String username = System.getenv("SAUCE_USERNAME");
        String accessKey = System.getenv("SAUCE_ACCESS_KEY");
        String visualApiKey = System.getenv("SAUCE_VISUAL_API_KEY");
        String methodName = method.getName();

        ChromeOptions chromeOpts = new ChromeOptions();
        chromeOpts.setExperimentalOption("w3c", true);

        MutableCapabilities sauceVisual = new MutableCapabilities();
        sauceVisual.setCapability("apiKey", visualApiKey);
        sauceVisual.setCapability("projectName", "VisualBeta-" + myViewportSize);
        sauceVisual.setCapability("viewportSize", myViewportSize);

        MutableCapabilities sauceOpts = new MutableCapabilities();
        sauceOpts.setCapability("name", methodName);
        sauceOpts.setCapability("build", "SwagLabsVisualDemo");
        sauceOpts.setCapability("seleniumVersion", "3.141.59");
        sauceOpts.setCapability("username", username);
        sauceOpts.setCapability("accessKey", accessKey);
        sauceOpts.setCapability("tags", "Sauce-Visual");

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(ChromeOptions.CAPABILITY, chromeOpts);
        caps.setCapability("sauce:options", sauceOpts);
        caps.setCapability("browserName", "chrome");
        caps.setCapability("browserVersion", "latest");
        caps.setCapability("platformName", "windows 10");
        caps.setCapability("sauce:visual", sauceVisual);

        //String sauceUrl = "https://ondemand.saucelabs.com:443/wd/hub";
        String sauceUrl = "https://hub.screener.io:443/wd/hub";
        URL url = new URL(sauceUrl);
        driver = new RemoteWebDriver(url, caps);
        js = (JavascriptExecutor) driver;
        js.executeScript("/*@visual.init*/", "Init");
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }

    @Test
    public void SwagLabsVisualTest() throws AssertionError, InterruptedException {
        driver.get("https://www.saucedemo.com");
        String getTitle = driver.getTitle();
        // Test Login Page on Entry
        js.executeScript("/*@visual.snapshot*/", "Swag Labs - Login Page");
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
        js.executeScript("/*@visual.snapshot*/", "Swag Labs - Failed Login");

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
            js.executeScript("document.querySelector('#item_4_title_link > div').style.color = 'black';");
            js.executeScript("document.querySelector('#item_0_title_link > div').innerText = 'Sauce Labs Bike Light Saber';");
            js.executeScript("document.querySelector('#item_1_title_link > div').align = 'right';");
            js.executeScript("document.querySelector('#item_5_title_link > div').innerText = 'Sauce Labs Jacket';");
        }

        js.executeScript("/*@visual.snapshot*/", "Swag Labs - Product Page - A2Z");

        // capture screen with sort menu shown
        el1 = null;
        el1 = driver.findElement(By.cssSelector("#inventory_filter_container > select"));
        el1.click();
        js.executeScript("/*@visual.snapshot*/", "Swag Labs - Product Page with sort menu shown");

        // Sort the items differently
        el1 = null;
        el1 = driver.findElement(By.cssSelector("#inventory_filter_container > select > option:nth-child(2)"));
        el1.click();
        js.executeScript("/*@visual.snapshot*/", "Swag Labs - Product Page - Z2A");

        // Sort the items differently - but with a random menu selection
        Random rand = new Random();
        int menuItem = rand.nextInt(4);
        if (menuItem < 1) {
            menuItem = 4;
        }
        el1 = null;
        el1 = driver.findElement(By.cssSelector("#inventory_filter_container > select > option:nth-child(" + menuItem + ")"));
        el1.click();

        //optionally mess with some a style
        el1 = null;
        el1 = driver.findElement(By.cssSelector("#inventory_container > div > div:nth-child(1) > div.pricebar > button"));
        js.executeScript("/*@visual.snapshot*/", "Swag Labs - Product Page - Random Order");

    }

    @AfterMethod
    public void teardown(ITestResult result) {
        ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
        js.executeScript("/*@visual.end*/");
        driver.quit();
    }
}