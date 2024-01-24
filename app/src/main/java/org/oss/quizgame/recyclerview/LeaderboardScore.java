package org.oss.quizgame.recyclerview;

public class LeaderboardScore implements Comparable<LeaderboardScore> {

    private final String username;
    private final int score;

    public LeaderboardScore(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(LeaderboardScore other) {
        // Compare UserScore objects based on scores
        return Integer.compare(this.score, other.score);
    }
}