package example.android.Tests;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class simpleSwagLabsAndroid {

    public String seleniumURI = "@ondemand.eu-central-1.saucelabs.com:443";
    public String username = System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");

    public AndroidDriver driver;

    @Test
    public void launchSwagLabs() throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "9");
        //caps.setCapability("deviceName", "Samsung Galaxy S9 WQHD GoogleAPI Emulator");
        caps.setCapability("appiumVersion", "1.17.1");
        caps.setCapability("browserName", "");
        caps.setCapability("deviceOrientation", "portrait");
        //caps.setCapability("build", "Andy Simple Swag Android Test");
        caps.setCapability("app", "storage:21f9558c-044f-48e3-8fb0-7c54dfb3141d");

        System.out.println("** creating driver **");
        driver = new AndroidDriver(new URL("https://" + username + ":" + accesskey + seleniumURI + "/wd/hub"), caps);
        System.out.println("** driver created **");
        //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

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