package org.oss.quizgame.recyclerview;

public class PersonalLeaderboardScore implements Comparable<PersonalLeaderboardScore> {

    private final String timestamp;
    private final int score;

    public PersonalLeaderboardScore(String timestamp, int score) {
        this.timestamp = timestamp;
        this.score = score;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(PersonalLeaderboardScore other) {
        // Compare PersonalLeaderboardScore objects based on timestamps
        return Long.compare(Long.parseLong(this.timestamp), Long.parseLong(other.timestamp));
    }
}