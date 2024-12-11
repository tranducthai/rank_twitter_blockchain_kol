package crawler;

import twitter.DriverConfig;
import csv.*;
import org.openqa.selenium.WebDriver;
import twitter.LoginTwitter;

import java.util.*;


public class CollectInteraction {
    private int totalKol;
    private String hashtagPath;
    private List<String> kols;
    private Map<String, Integer> kolMap; // Đánh dấu xem xuất hiện chưa
    private List<String> followerCount;
    private List<String> interaction;

    public CollectInteraction() {
        totalKol = 0;
        kols = new ArrayList<>();
        kolMap = new HashMap<>();
        followerCount = new ArrayList<>();
        interaction = new ArrayList<>();
    }

    public CollectInteraction(String hashtagPath) {
        totalKol = 0;
        this.hashtagPath = hashtagPath;
        kols = new ArrayList<>();
        kolMap = new HashMap<>();
        followerCount = new ArrayList<>();
        interaction = new ArrayList<>();
    }

    public String getHashtagPath() {
        return this.hashtagPath;
    }

    public void setHashtagPath(String hashtagPath) {
        this.hashtagPath = hashtagPath;
    }

    public int getTotalKol() {
        return this.totalKol;
    }

    // Đánh dấu kol, nếu có rồi thì bỏ quả, nếu chưa thì cho vào list
    public boolean markKolName(String name) {
        if(kolMap.containsKey(name) == true) {
            return false;
        }

        kolMap.put(name, totalKol++);
        kols.add(name);
        return true;
    }

    // Xây dựng 1 list kol dựa vào data thu thập được
    public void buildKolName(String filePath, String column) { // column = A là cột 0, B là cột 1
        totalKol = 0;
        List<String> list = ReadCsv.readCsvToList(filePath, column);
        for(String i : list) {
            markKolName(i);
        }
    }

    // Đi tìm sự tương tác với nhau thoi
    public void search() throws InterruptedException {
        WebDriver driver = DriverConfig.initializeDriver();
        LoginTwitter loginTwitter = new LoginTwitter(driver);
        SearchInteraction search = new SearchInteraction(driver);

        String username = <username>;
        String password = <password>;
        String email = <email>;

        loginTwitter.login(username, password, email);

        List<String> listHashtag = ReadCsv.readCsvToList(hashtagPath, "A");
        search.find(kols, listHashtag, kolMap);
        followerCount = search.getFollowerCount();
        interaction = search.getInteraction();

        loginTwitter.closeBrowser();
    }


    // Ghi ra file kết quả
    public void writeToCsv(String followerCountPath, String interactionPath) {
        for(int i = 0; i + 1 < followerCount.size(); i += 2) {
            String name = followerCount.get(i);
            String follower = followerCount.get(i + 1);
            WriteCsv.writeListToCSV(followerCountPath, new String[]{name, follower});
        }

        for(int i = 0; i + 1 < interaction.size(); i += 2) {
            String nameA = interaction.get(i);
            String nameB = interaction.get(i + 1);
            WriteCsv.writeListToCSV(interactionPath, new String[]{nameA, nameB});
        }
    }
}
