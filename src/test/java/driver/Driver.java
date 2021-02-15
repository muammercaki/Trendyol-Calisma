package driver;

import com.thoughtworks.gauge.AfterSuite;
import com.thoughtworks.gauge.BeforeSuite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class Driver {
    protected static Logger logger = LogManager.getLogger(Driver.class);

    public static WebDriver driver;
    public static String baseUrl = "https://www.trendyol.com/";


    @BeforeSuite
    public void initializeDriver() {
        driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
        driver.get(baseUrl);
    }


    @AfterSuite
    public void closeDriver() {
        driver.quit();
    }

}
