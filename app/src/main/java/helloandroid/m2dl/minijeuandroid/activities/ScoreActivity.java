package helloandroid.m2dl.minijeuandroid.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import helloandroid.m2dl.minijeuandroid.R;

public class ScoreActivity extends Activity {

    private TextView textViewScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
    }

    @Override
    protected void onStart() {
        super.onStart();
        textViewScore = findViewById(R.id.scoreValue);

        // SharedPreferences
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        textViewScore.setText(sharedPreferences.getInt("last_score", 0));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("last_score", 0);
        editor.apply();

        final Button retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toGameActivity();
            }
        });

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