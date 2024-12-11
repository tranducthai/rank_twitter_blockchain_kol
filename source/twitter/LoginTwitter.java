package twitter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginTwitter extends TwitterDriver{
    public LoginTwitter(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password, String email) throws InterruptedException {
        // Vô trang twitter nè
        driver.navigate().to("https://x.com");

        // Cuộn xuống cuối trang(bị che mất không click được)
        wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

         // Chờ nút "Sign in" và nhấn vào
         WebElement signin = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sign in']")));
         signin.click();
         Thread.sleep(5000);

         // Nhập tên người dùng
         WebElement userInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='text']")));
         userInput.sendKeys(username);

         // Nhấn nút "Next"
         WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Next']")));
         nextButton.click();

         try {
             // Nhập password
             WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='password']")));
             passwordInput.sendKeys(password);
             WebElement login = driver.findElement(By.xpath("//span[text()='Log in']"));
             login.click();
             Thread.sleep(5000);
         } catch (Exception e1) {
             // Nếu không tìm thấy, nhập email
             WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='text']")));
             emailInput.sendKeys(email);
             nextButton = driver.findElement(By.xpath("//span[text()='Next']"));
             nextButton.click();

             // Nhập password
             WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='password']")));
             passwordInput.sendKeys(password);
             WebElement login = driver.findElement(By.xpath("//span[text()='Log in']"));
             login.click();
         }
    }
}
