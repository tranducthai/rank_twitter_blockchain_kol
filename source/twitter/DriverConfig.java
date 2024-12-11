package twitter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class DriverConfig {
	public static WebDriver initializeDriver() {
        System.setProperty("webdriver.edge.driver", "resource\\msedgedriver.exe");
        return new EdgeDriver();
    }
}
