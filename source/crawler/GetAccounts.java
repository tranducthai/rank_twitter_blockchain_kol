package crawler;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import twitter.TwitterDriver;

public class GetAccounts extends TwitterDriver {
	public GetAccounts(WebDriver driver) {
		super(driver);
	}

    // Tìm account với hashtag @.....
	public List<String> searchHashTags(String hashtag) {
		WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search']")));
        searchInput.click();   
        
        // Nhập hashtag vào thanh tìm kiếm
        searchInput.sendKeys(hashtag);
        searchInput.sendKeys(Keys.ENTER);
 
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Timeline: Search timeline']")));
        
        //Click vào nút People
        WebElement peopleButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='People']")));
        peopleButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Timeline: Search timeline']")));

        List<String> accountLinks = new ArrayList<>();
        int target = 20;
        while (accountLinks.size() < target) {
            // Lấy lại các phần tử tài khoản mỗi lần cuộn trang
            List<WebElement> accountElements = driver.findElements(By.cssSelector("div[data-testid='cellInnerDiv']"));
            	            
            for (int i = 0; i < accountElements.size(); i++) {
                WebElement accountElement = accountElements.get(i);               
                // Tìm phần tử <a> bên trong CellInnerDiv để lấy href
                WebElement linkElement = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(accountElement, By.tagName("a")));
                String link = linkElement.getAttribute("href");
                accountLinks.add(link);
                System.out.println("Account : " + AccountUtil.getNameFromLink(link));
            }
                              
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)"); // Cuộn

            try {
                Thread.sleep(2000);
            } catch (NoSuchElementException e) {
                System.out.println("Bị lỗi ở hashtag : " + hashtag);
            } catch (InterruptedException e) {
                System.out.println("Chương trình bị gián đoạn");
                e.printStackTrace();
                Thread.currentThread().interrupt(); // Khôi phục trạng thái gián đoạn ?
            }
        }

        return accountLinks;     
	}	

    // Lấy ra acc và following của acc
	public List<Account> getAccountsFromLinks(List<String> accountLinks) {
	   List<Account> accounts = new ArrayList<>();
       List<String> followingOfAccount = new ArrayList<>();

       int target = 20;
	   int minFollower = 50000;

	   for(int j = 0; j < Math.min(accountLinks.size(),target); j++) {
           String link = accountLinks.get(j);
           driver.navigate().to(link);  // Truy cập vô link

           try {
               int followerCount = AccountUtil.countFollower(wait); // Số flers

               if(followerCount < minFollower) continue;
               System.out.println("Link: " + link + " has " +  followerCount + " followers.");
                           
               // Chuyển đến trang người theo dõi
               WebElement followerLinkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href*='followers']")));
               String linkfl = followerLinkElement.getAttribute("href");
               driver.navigate().to(linkfl);

               // Chuyển đến trang người đang theo dõi
               WebElement followingButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Following']")));
               followingButton.click();
               
               // Chờ tải danh sách người đang theo dõi
               wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[aria-label='Timeline: Following']")));
               List<WebElement> followingElements = driver.findElements(By.cssSelector("div[data-testid='cellInnerDiv']"));

               for(int i = 0; i < followingElements.size(); i++) {
                   WebElement following = followingElements.get(i);
                   // Tìm phần tử con chứa tên (sử dụng selector của span hoặc div có thể chứa tên)
                   WebElement linkFollowing = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(following, By.tagName("a")));
                   String fllink = linkFollowing.getAttribute("href");
                   String name = AccountUtil.getNameFromLink(fllink);
                   System.out.println("Following " + (i + 1) + ": " + name);
               }
                          
               accounts.add(new Account(AccountUtil.getNameFromLink(link), followerCount, new ArrayList<>(followingOfAccount)));

           } catch (NoSuchElementException e) {
               System.out.println("Không tìm thấy số followers hoặc link người follow trong link :" + link);
           } catch (Exception e) {
               System.out.println("Chương trình xảy ra lỗi : " + e.getMessage());
           }
           
           followingOfAccount.clear();
       }
		
		return accounts;
	} 
	
}