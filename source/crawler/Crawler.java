package crawler;

public class Crawler {
    // Craw data
    public static void crawler() throws InterruptedException {
        String filePath = "resource\\Hashtags.csv";
        // search kol
        SearchKol searchKol = new SearchKol(filePath);
        searchKol.search();
        searchKol.writeToCsv("resource\\ListAccount.csv",
                "resource\\FollowingAccount.csv",
                "resource\\Interaction.csv");
        // find interaction
        CollectInteraction collectInteraction = new CollectInteraction(filePath);
        collectInteraction.buildKolName("resource\\ListAccount.csv", "A");
        collectInteraction.buildKolName("resource\\FollowingAccount.csv", "A");
        collectInteraction.buildKolName("resource\\FollowingAccount.csv", "B");
        collectInteraction.search();
        collectInteraction.writeToCsv("resource\\FollowerCount.csv",
                        "resource\\Interaction.csv");
    }
}
