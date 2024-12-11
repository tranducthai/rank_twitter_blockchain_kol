package twitter;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class TwitterDriver {
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected JavascriptExecutor js;

	public TwitterDriver(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		this.js = (JavascriptExecutor) driver;
	}

	public void closeBrowser() {
		if(this.driver == null) {
			System.out.println("Chưa chạy trình duyệt");
		}
		else {
			this.driver.quit();
			System.out.println("Chạy xong rồi nhé các mom :))");
		}
	}
}