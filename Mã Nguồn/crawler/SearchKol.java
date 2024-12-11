package crawler;

import csv.ReadCsv;
import csv.WriteCsv;
import org.openqa.selenium.WebDriver;
import twitter.DriverConfig;
import twitter.LoginTwitter;

import java.util.ArrayList;
import java.util.List;

public class SearchKol {
    private List<Account> account;
    private String hashtagPath;

    public SearchKol(String hashtagPath) {
        this.account = new ArrayList<>();
        this.hashtagPath = hashtagPath;
    }

    public List<Account> getAccount() {
        return this.account;
    }

    // Tìm thôi ae
    public void search() throws InterruptedException {
        List<String> listHashtag = ReadCsv.readCsvToList(hashtagPath, "A");

        String username = <username>;
        String password = <password>;
        String email = <email>;

        WebDriver driver = DriverConfig.initializeDriver();
        LoginTwitter loginTwitter = new LoginTwitter(driver);
        loginTwitter.login(username, password, email);

        GetAccounts getAccounts = new GetAccounts(driver);

        // Tìm kiếm với mỗi hashtag
        for(int i = 0; i < listHashtag.size(); i++) {
            String str = listHashtag.get(i);
            List<String> accountLinks = getAccounts.searchHashTags(str);
            account.addAll(getAccounts.getAccountsFromLinks(accountLinks));
        }

        loginTwitter.closeBrowser();
    }

    // Ghi ra file kết quả
    public void writeToCsv(String listAccountPath, String followingAccountPath, String interactionPath) {
        for(Account i : account) {
            String name = i.getName();
            WriteCsv.writeListToCSV(listAccountPath, new String[]{name});

            for(String j : i.getFollowingAccount()) {
                WriteCsv.writeListToCSV(followingAccountPath, new String[]{j, name});
                WriteCsv.writeListToCSV(interactionPath, new String[]{j, name});
            }
        }
    }
}