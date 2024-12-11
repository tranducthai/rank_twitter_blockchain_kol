package crawler;

public class Crawler {
    // Thu thập thông tin (khá lâu khoảng vài ngày)
    public static void crawler() throws InterruptedException {
        String filePath = "resource\\Hashtags.csv";
        // Pha thứ nhất : Tìm kol
        SearchKol searchKol = new SearchKol(filePath);
        searchKol.search();
        searchKol.writeToCsv("resource\\ListAccount.csv",
                "resource\\FollowingAccount.csv",
                "resource\\Interaction.csv");
        // Pha thứ 2 : Tìm sự tương tác giữa các kol
        CollectInteraction collectInteraction = new CollectInteraction(filePath);
        collectInteraction.buildKolName("esource\\ListAccount.csv", "A");
        collectInteraction.buildKolName("resource\\FollowingAccount.csv", "A");
        collectInteraction.buildKolName("resource\\FollowingAccount.csv", "B");
        collectInteraction.search();
        collectInteraction.writeToCsv("resource\\FollowerCount.csv",
                        "resource\\Interaction.csv");
    }
}
