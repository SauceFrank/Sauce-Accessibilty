package example.ios.Tests;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.ios.IOSDriver;
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

    public IOSDriver driver;

    @Test
    public void walk2Tesco() throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("browserName", "Safari");
        caps.setCapability("platformVersion", "12.2");
        caps.setCapability("deviceName", "iPhone 7 Simulator");
        caps.setCapability("deviceOrientation", "Portrait");
        caps.setCapability("build", "AW Map Runner");
        //caps.setCapability("appiumVersion", "1.17.0");

        System.out.println("** creating driver **");
        driver = new IOSDriver(new URL("https://" + username + ":" + accesskey + seleniumURI + "/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);


        System.out.println("** navigating to Home screen **");
        driver.executeScript("mobile: pressButton", ImmutableMap.of("name", "home"));

        //System.out.println("** navigating to Settings **");
        //driver.activateApp("Settings");

        System.out.println("** activating Maps - default location **");
        driver.activateApp("com.apple.Maps");

        System.out.println("** on journey... **");
        makeJourney(driver);
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        System.out.println("** setting success flag on Sauce **");
        ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));

        System.out.println("** quitting the driver **");
        driver.quit();
    }

    private void makeJourney(IOSDriver driver) throws IOException, ParserConfigurationException, SAXException {
        File inputFile = new File("resources/HPW2Tesco.gpx");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("trkpt");

        Double latitude = null;
        Double longitude = null;
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
            }
        }
    }
}