package example.ios.Tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class simpleWebUP {

    public String seleniumURI = "@ondemand.eu-central-1.saucelabs.com:443";
    public String username = System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");
    public AppiumDriver driver;

    @Test
    public void navigateHome() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();

        // If running against iOS
        /*
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "13");
        caps.setCapability("build", "Simple Selenium Test");
        caps.setCapability("appiumVersion", "1.17.1");
        System.out.println("** creating driver **");
        driver = new IOSDriver(new URL("https://" + username + ":" + accesskey + seleniumURI + "/wd/hub"), caps);
        //*/

        // If running against Android
        ///*
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "10");
        caps.setCapability("deviceName", "Samsung Galaxy .*");
        caps.setCapability("build", "Simple Selenium Test");
        caps.setCapability("appiumVersion", "1.17.1");
        System.out.println("** creating driver **");
        driver =  new AndroidDriver(new URL("https://" + username + ":" + accesskey + seleniumURI + "/wd/hub"), caps);
        //*/

        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        System.out.println("** navigating to Home URL **");
        driver.get("https://www.saucelabs.com");

    }

    @AfterMethod
    public void teardown(ITestResult result) {
        System.out.println("** setting success flag on Sauce **");
        ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));

        System.out.println("** quitting the driver **");
        driver.quit();
    }
}