package graph;

public class Kol extends User{
    private static final int MINFOLLOWER = 50000;

    public Kol(String name, int follower) {
        super(name, follower);
    }

    public static boolean isKol(int follower) {
        return follower >= MINFOLLOWER;
    }
}
