package crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountUtil {
	// Chuyển followerCountText thành số
	public static int convertToNumber(String str) {
		if (str.endsWith("K")) {
			return (int)(Double.parseDouble(str.replace("K", "")) * 1000);
		} else if (str.endsWith("M")) {
			return (int)(Double.parseDouble(str.replace("M", "")) * 1000000);
		} else if (str.endsWith("B")) {
			return (int)(Double.parseDouble(str.replace("B", "")) * 1000000000);
		} else {
			str = str.replace(",", "");
			return Integer.parseInt(str);
		}
	}

	// Đếm số follower
	public static int countFollower(WebDriverWait wait) {
		WebElement followerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href*='followers'] span.css-1jxf684")));
		String followerCountText = followerElement.getText();
		int followerCount = AccountUtil.convertToNumber(followerCountText);
		return followerCount;
	}

	// Tách tên khỏi link : "...../name"
	public static String getNameFromLink(String link) {
		int n = link.length();
		int pos = 0;
		for(int i = n - 1; i >= 0; i--) {
			if (link.charAt(i) == '/') {
				pos = i;
				break;
			}
		}
		return link.substring(pos + 1);
	}
}