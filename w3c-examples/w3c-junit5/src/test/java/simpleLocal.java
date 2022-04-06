import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//comment the above line and uncomment below line to use Chrome
import org.openqa.selenium.chrome.ChromeDriver;

public class simpleLocal {

    public static void main(String[] args) throws InterruptedException {
        // declaration and instantiation of objects/variables
        //System.setProperty("webdriver.gecko.driver","/Users/andywyatt/seleniumDrivers/geckodriver");
        System.setProperty("webdriver.chrome.driver", "/Users/andywyatt/seleniumDrivers/chromedriver");
        //WebDriver driver = new FirefoxDriver();
        WebDriver driver = new ChromeDriver();
        //comment the above 2 lines and uncomment below 2 lines to use Chrome
        //System.setProperty("webdriver.chrome.driver","G:\\chromedriver.exe");
        //WebDriver driver = new ChromeDriver();

        String baseUrl = "https://www.ba.com";
        String expectedTitle = "British Airways | Book Flights, Holidays, City Breaks & Check In Online";
        String actualTitle = "";

        // launch Browser and direct it to the Base URL
        driver.get(baseUrl);

        // get the actual value of the title
        actualTitle = driver.getTitle();
        System.out.println("page title: " + actualTitle);
        Thread.sleep(10000);

        /*
         * compare the actual title of the page with the expected one and print
         * the result as "Passed" or "Failed"
         */
        if (actualTitle.contentEquals(expectedTitle)) {
            System.out.println("Test Passed!");
        } else {
            System.out.println("Test Failed");
        }

        //close Fire fox
        driver.close();

    }

}