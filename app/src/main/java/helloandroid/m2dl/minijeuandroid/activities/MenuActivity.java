package helloandroid.m2dl.minijeuandroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.icu.text.TimeZoneFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.UUID;

import helloandroid.m2dl.minijeuandroid.R;
import helloandroid.m2dl.minijeuandroid.data.Score;

public class MenuActivity extends Activity {

    private int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Button newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(v -> toGameActivity());

        Button quitButton = findViewById(R.id.quit_button);
        quitButton.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setHighScore();
    }

    public void toGameActivity() {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }

    private void setHighScore() {
        String url = getResources().getString(R.string.database_url);
        FirebaseDatabase database = FirebaseDatabase.getInstance(url);
        DatabaseReference high_score_ref = database.getReference("high_score");
        ValueEventListener scoreListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                highScore = dataSnapshot.getValue(Integer.class);
                TextView textViewHighScore = findViewById(R.id.high_score_value);
                runOnUiThread(() -> textViewHighScore.setText(String.valueOf(highScore)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                System.err.println("Failed database access");
            }
        };
        high_score_ref.addValueEventListener(scoreListener);
    }
}