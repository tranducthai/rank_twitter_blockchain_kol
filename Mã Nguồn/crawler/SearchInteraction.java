package crawler;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import twitter.TwitterDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchInteraction extends TwitterDriver {
    private List<String> followerCount; // chẵn là tên, lẻ là số fl
    private List<String> interaction; // lẻ cmt/repost chẵn

    public SearchInteraction(WebDriver driver) {
        super(driver);
        this.followerCount = new ArrayList<>();
        this.interaction = new ArrayList<>();
    }

    public List<String> getFollowerCount() {
        return this.followerCount;
    }

    public List<String> getInteraction() {
        return this.interaction;
    }

    // Hàm check nick twitter có liên quan đến blockchain hay ko
    private boolean containsHashtag(String text, List<String> hashtags) {
        return hashtags.stream().anyMatch(text::contains);
    }
    // Truy cập vào trang của kol
    public void searchAndOpenKol(String kol) throws InterruptedException {
        // Truy cập vào link của kol
        driver.navigate().to("https://x.com/" + kol);
        driver.navigate().refresh();
    }
    // Hàm tạo node tìm kiếm các nick twitter có tương tác vs nhau
    public void checkKolandFollowing(String kol, List<String> Hashtags, Map<String, Integer> kolMap) throws InterruptedException {
        try {
            int countFl = AccountUtil.countFollower(wait);
            if (countFl < 50000) {
                return;
            }

            followerCount.add(kol);
            followerCount.add(Integer.toString(countFl));

            WebElement Kolreplies = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='/" + kol + "/with_replies']")));
            Kolreplies.click();
            Thread.sleep(2000);

            int scroll = 0;
            while (scroll < 20) {
                // Cuộn để load thêm
                js.executeScript("window.scrollBy(0, 500);");
                Thread.sleep(2000);

                // Tìm danh sách các cellInnerDiv
                List<WebElement> cellElements = driver.findElements(By.xpath("//div[@data-testid='cellInnerDiv']"));
                if (cellElements.size() > 2 * scroll + 1) {
                    for (int i = 2 * scroll; i <= 2 * scroll + 1; i++) {
                        WebElement cell = cellElements.get(i);
                        String cellText = cell.getText().toLowerCase();

                        // Kiểm tra chứa hashtag
                        if (containsHashtag(cellText, Hashtags)) {
                            // tìm xem kol có reposted bài của kol khác ko
                            if (cellText.contains("reposted")) {
                                // Cập nhật regex để tìm username bắt đầu bằng '@'
                                Pattern pattern = Pattern.compile("@[\\w.]+"); // Regex tìm "@username"
                                Matcher matcher = pattern.matcher(cellText);

                                if (matcher.find()) {
                                    String originalAccount = matcher.group(); // Lấy tài khoản gốc bao gồm '@'
                                    // Loại bỏ '@' để chỉ lấy username
                                    String usernameWithoutAt = originalAccount.substring(1); // Bỏ ký tự đầu tiên '@'
                                    // Nếu ký tự cuối là endline thì xóa đi
                                    if (!usernameWithoutAt.isEmpty() && usernameWithoutAt.charAt(usernameWithoutAt.length() - 1) == '\n') {
                                        usernameWithoutAt = usernameWithoutAt.substring(0, usernameWithoutAt.length() - 1);
                                    }
                
                                    // Chỉ xét những kol trong list
                                    if (kolMap.containsKey(usernameWithoutAt) == true) {
                                        interaction.add(usernameWithoutAt);
                                        interaction.add(kol);
                                    }
                                }
                            }
                                
                            // Tìm xem kol tương tác vs kol nào(cmt/repost)
                            else {
                                List<WebElement> userNameElements = cell.findElements(By.xpath(".//*[@data-testid='User-Name']"));

                                for (WebElement userNameElement : userNameElements) {
                                    String fullUserName = userNameElement.getText();
                                    // Kiểm tra và tách phần username
                                    if (fullUserName.contains("@")) {
                                        // Tách phần sau '@' và loại bỏ phần sau dấu phân cách (khoảng trắng, dấu chấm hoặc ký tự '·')
                                        String userName = fullUserName.split("@")[1].split("[ .·]")[0]; // Tách trước khoảng trắng, dấu chấm hoặc ký tự '·'
                                        
                                        // So sánh với Kol sau khi chuyển cả 2 thành chữ thường (hoặc chữ hoa)
                                        if (!userName.trim().equalsIgnoreCase(kol.trim())) { // Kiểm tra khác với Kol, xử lý khoảng trắng thừa
                                            if (!userName.isEmpty() && userName.charAt(userName.length() - 1) == '\n') {
                                                userName = userName.substring(0, userName.length() - 1);
                                            }
                                            if (kolMap.containsKey(userName) == true) {
                                                interaction.add(userName);
                                                interaction.add(kol);
                                            }
                                            break; // Dừng sau khi tìm thấy tài khoản đầu tiên khác Kol
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                scroll++;
            }
        } catch(NoSuchElementException e) {
            System.out.println("Không tìm thấy phần tử: " + e.getMessage());
        } catch (TimeoutException e) {
            System.out.println("Lag quá ae ơi");
        } catch (StaleElementReferenceException e){
            Thread.currentThread().interrupt();
            Thread.sleep(2000);
        }
    }

    // hàm để vào các trang của kol
    public void find(List<String> kols, List<String> Hashtags, Map<String, Integer> kolMap) throws InterruptedException {
        int sleepSearchKol = 0;
        for (String kol : kols) {
            sleepSearchKol++;
            // Khoảng 22 người thì nghỉ ~13p
            if(sleepSearchKol % 22 == 0)
                Thread.sleep(800000);
            searchAndOpenKol(kol);
            checkKolandFollowing(kol, Hashtags, kolMap);
            WebElement element = driver.findElement(By.xpath("//a[@aria-label='Home' and @href='/home']"));
            element.click();
        }
    }
}
