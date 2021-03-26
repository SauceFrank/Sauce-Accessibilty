package example.android.Tests;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class mapRunner {

    public String seleniumURI = "@ondemand.eu-central-1.saucelabs.com:443";
    public String username = System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");

    public AndroidDriver driver;

    @Test
    public void walk2Tesco() throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("browserName", "");
        caps.setCapability("build", "AW Map Runner - Android");
        //caps.setCapability("deviceName", "Google_Pixel_real");
        //caps.setCapability("appPackage","com.google.android.apps.maps");
        caps.setCapability("appActivity","com.google.android.maps.MapsActivity");
        caps.setCapability("deviceOrientation", "portrait");

        System.out.println("** creating driver **");
        driver = new AndroidDriver(new URL("https://" + username + ":" + accesskey + seleniumURI + "/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        System.out.println("** on journey... **");
        //caps.setCapability("appPackage","com.google.android.apps.maps");


        Thread.sleep(10000);
        makeJourney(driver);
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        System.out.println("** setting success flag on Sauce **");
        ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));

        System.out.println("** quitting the driver **");
        driver.quit();
    }

    private void sleep(int interval) throws InterruptedException {
        if (interval==0) {
            interval = 1000;
        }
            Thread.sleep(interval);
    }

    private void makeJourney(AndroidDriver driver) throws IOException, ParserConfigurationException, SAXException, InterruptedException {
        File inputFile = new File("resources/HPW2Tesco.gpx");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("trkpt");

        Double latitude;
        Double longitude;
        Double altitude = 12.0;

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nProcessing node: " + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                latitude = Double.valueOf(eElement.getAttribute("lat"));
                longitude = Double.valueOf(eElement.getAttribute("lon"));
                System.out.println("latitude : " + latitude + " , Longitude : " + longitude);
                driver.setLocation(new Location(latitude, longitude, altitude));
                if (temp==0) {
                    System.out.println("\nsetting initial location and launching maps");
                    driver.activateApp("com.google.android.apps.maps");
                    sleep(10000);
                } else {
                    //sleep(10000);
                }
            }
        }
    }
}