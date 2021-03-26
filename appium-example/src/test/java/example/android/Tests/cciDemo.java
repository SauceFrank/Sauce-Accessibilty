package example.android.Tests;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class cciDemo {

    public String seleniumURI = "@ondemand.eu-central-1.saucelabs.com:443";
    public String username = System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");

    public AndroidDriver driver;

    @Test
    public void launchCCIApp() throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("app", "storage:958dfd65-e14f-4c3f-b545-908f4063d5f2");
        //caps.setCapability("app", "storage:cci.apk");
        caps.setCapability("browserName", "");
        caps.setCapability("deviceOrientation", "portrait");

        System.out.println("** creating driver **");
        driver = new AndroidDriver(new URL("https://" + username + ":" + accesskey + seleniumURI + "/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        System.out.println("** App Launched **");

        System.out.println("** Entering Userid **");
        WebElement el1 = driver.findElement(By.xpath("(//android.widget.EditText)[1]"));
        el1.sendKeys("ebest.preseller@cci.com.tr.sf.test1");
        el1 =  null;

        System.out.println("** Entering Password **");
        el1 = driver.findElement(By.xpath("(//android.widget.EditText)[2]"));
        el1.sendKeys("asdf1234");
        el1 = null;

        System.out.println("** Clicking Log In button **");
        el1 = driver.findElement(By.xpath("//android.widget.Button[1]"));
        el1.click();
        

    }

    @AfterMethod
    public void teardown(ITestResult result) {
        System.out.println("** setting success flag on Sauce **");
        ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));

        System.out.println("** quitting the driver **");
        driver.quit();
    }
}