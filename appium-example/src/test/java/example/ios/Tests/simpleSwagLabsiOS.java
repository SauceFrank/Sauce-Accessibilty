package example.ios.Tests;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class simpleSwagLabsiOS {

    public String seleniumURI = "@ondemand.eu-central-1.saucelabs.com:443";
    public String username = System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");

    public IOSDriver driver;

    @Test
    public void launchSwagLabs() throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("build", "Andy Simple Swag iOS Test");
        //caps.setCapability("appiumVersion", "1.17.1");
        //caps.setCapability("deviceName", "iPhone_SE_11_3_POC04");
        caps.setCapability("app", "storage:0983320d-5489-4ee7-8fa2-f66511a8b580");

        System.out.println("** creating driver **");
        driver = new IOSDriver(new URL("https://" + username + ":" + accesskey + seleniumURI + "/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        System.out.println("** Swag Splash **");

        Thread.sleep(10000);
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        System.out.println("** setting success flag on Sauce **");
        ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));

        System.out.println("** quitting the driver **");
        driver.quit();
    }
}