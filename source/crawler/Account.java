package crawler;

import java.util.List;

public class Account {
    private String name;
    private int followers;
    private List<String> followingAccount;

    public Account(String name, int followers, List<String> followingAccount) {
        this.name = name;
        this.followers = followers;
        this.followingAccount = followingAccount;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFollowers() {
        return this.followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public List<String> getFollowingAccount() {
        return this.followingAccount;
    }

    public void setFollowingAccount(List<String> followingAccount) {
        this.followingAccount = followingAccount;
    }
}
