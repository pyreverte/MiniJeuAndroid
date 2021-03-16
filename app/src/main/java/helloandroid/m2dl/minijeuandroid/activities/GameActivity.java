package helloandroid.m2dl.minijeuandroid.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

import helloandroid.m2dl.minijeuandroid.GameView;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = this.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        editor.putInt("screen_height", height);
        editor.putInt("screen_width", width);
        editor.apply();

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GameView(this, sharedPreferences, this));
    }

    public void toScoreActivity() {
        Intent i = new Intent(this, ScoreActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }
}