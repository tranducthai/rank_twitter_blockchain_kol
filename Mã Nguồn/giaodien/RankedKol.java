package giaodien;

public class RankedKol {
	private String name;
	private float score;
	private int rank;
	private int followers;

	public RankedKol(String name,int followers,float score) {
		this.name = name;
		this.score = score;
		this.followers = followers;
	}

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
}
