package helloandroid.m2dl.minijeuandroid.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import helloandroid.m2dl.minijeuandroid.GameView;

public class GameActivity extends Activity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SharedPreferences
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Création des délimitations
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        editor.putInt("screen_height", displayMetrics.heightPixels);
        editor.putInt("screen_width", displayMetrics.widthPixels);
        editor.apply();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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