package helloandroid.m2dl.minijeuandroid.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Score {

    public String username;
    public int score;
    public Date date;

    public Score() {

    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public Date getDate() {
        return date;
    }

    public Score(String username, int score, Date date) {
        this.username = username;
        this.score = score;
        this.date = date;
    }
}
