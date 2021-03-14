package helloandroid.m2dl.minijeuandroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.TimeZoneFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import helloandroid.m2dl.minijeuandroid.R;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setHighScore();

        final Button newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toGameActivity();
            }
        });

        Button quitButton = findViewById(R.id.quit_button);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

    public void toGameActivity() {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }

    private void setHighScore() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        int highScore = sharedPreferences.getInt("high_score", 0);
        TextView textViewHighScore = findViewById(R.id.high_score_value);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewHighScore.setText(String.valueOf(highScore));
            }
        });

    }
}