package test.camelot;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class CamelotVisualDemo {
    protected WebDriver driver;
    public Boolean result;
    public static JavascriptExecutor js;
    boolean createVisualDiffs = false;
    //boolean createVisualDiffs = true;
    //String myViewportSize = "414x896";
    //String myViewportSize = "1024x768";
    //String myViewportSize = "1280x1024";
    String myViewportSize = "1920x1200";

    @BeforeMethod
    public void setup(Method method) throws MalformedURLException {
        String username = System.getenv("SAUCE_USERNAME");
        String accessKey = System.getenv("SAUCE_ACCESS_KEY");
        String visualApiKey = System.getenv("SAUCE_VISUAL_API_KEY");
        String methodName = method.getName();

        String demoPrefix = "Camelot";
        String buildName = demoPrefix + " Visual Demo";

        ChromeOptions chromeOpts = new ChromeOptions();
        chromeOpts.setExperimentalOption("w3c", true);

        MutableCapabilities sauceVisual = new MutableCapabilities();
        sauceVisual.setCapability("apiKey", visualApiKey);
        sauceVisual.setCapability("projectName", demoPrefix + " Homepage-" + myViewportSize);
        sauceVisual.setCapability("viewportSize", myViewportSize);

        MutableCapabilities sauceOpts = new MutableCapabilities();
        sauceOpts.setCapability("name", methodName);
        sauceOpts.setCapability("build", buildName);
        sauceOpts.setCapability("seleniumVersion", "3.141.59");
        sauceOpts.setCapability("username", username);
        sauceOpts.setCapability("accessKey", accessKey);
        sauceOpts.setCapability("tags", demoPrefix + "-Visual");

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
    public void CamelotVisualTest() throws AssertionError, InterruptedException {
        driver.get("https://www.national-lottery.co.uk/");
        String getTitle = driver.getTitle();
        // Test Login Page on Entry
        js.executeScript("/*@visual.snapshot*/", "National Lottery - Home Page");
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
        js.executeScript("/*@visual.end*/");
        driver.quit();
    }
}