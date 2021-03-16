package helloandroid.m2dl.minijeuandroid.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Score {

    public String username;
    public int score;
    public String date;

    public Score() {

    }

    public Score(String username, int score, String date) {
        this.username = username;
        this.score = score;
        this.date = date;
    }
}
