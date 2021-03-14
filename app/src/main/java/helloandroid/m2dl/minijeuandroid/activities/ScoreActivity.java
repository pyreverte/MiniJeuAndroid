package helloandroid.m2dl.minijeuandroid.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import helloandroid.m2dl.minijeuandroid.R;

public class ScoreActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // SharedPreferences
        SharedPreferences sharedPreferences = this.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        int score = sharedPreferences.getInt("last_score", 0);

        TextView textViewScore = findViewById(R.id.score_value);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewScore.setText(String.valueOf(score));
            }
        });

        // Set HighScore
        int highScore = sharedPreferences.getInt("high_score", 0);
        if (score > highScore) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("high_score", score);
            editor.apply();
        }

        // Reset Score
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("last_score", 0);
        editor.apply();

        // Retry Button
        final Button retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toGameActivity();
            }
        });

        // Main Menu Button
        final Button mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toMenuActivity();
            }
        });

    }

    public void toGameActivity() {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }

    public void toMenuActivity() {
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }
}