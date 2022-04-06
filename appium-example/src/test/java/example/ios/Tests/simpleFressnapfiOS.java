package example.ios.Tests;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By.*;

import java.net.URL;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

public class simpleFressnapfiOS {

    public String seleniumURI = "@ondemand.eu-central-1.saucelabs.com:443";
    public String username = System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");

    public IOSDriver driver;

    @Test
    public void launchSwagLabs() throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("build", "Andy Fressnapf iOS Test");
        //caps.setCapability("appiumVersion", "1.17.1");
        //caps.setCapability("deviceName", "iPhone_SE_11_3_POC04");
        caps.setCapability("platformVersion", "14.8");
        caps.setCapability("bundleId", "com.fressnapf.app.stage");
        caps.setCapability("app", "storage:a568a013-a443-4b83-931c-d16fa1a1f917");
        caps.setCapability("autoAcceptAlerts", true);

        System.out.println("** creating driver **");
        driver = new IOSDriver(new URL("https://" + username + ":" + accesskey + seleniumURI + "/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

        //WebElement el1 = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\\\"Alle akzeptieren\\\"]\""));
        WebElement el1 = driver.findElement(By.xpath("//XCUIElementTypeButton[@name='Alle akzeptieren']"));
        el1.click();
        el1 = null;
        el1 = driver.findElement(By.xpath("//XCUIElementTypeButton[@name='Bei Fressnapf registrieren']"));
        el1.click();
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