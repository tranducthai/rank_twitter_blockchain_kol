package graph;

public class User {
    private String name;
    private int follower;

    public User(String name, int follower) {
        this.name = name;
        this.follower = follower;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFollower() {
        return this.follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }
}
