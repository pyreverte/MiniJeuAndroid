package helloandroid.m2dl.minijeuandroid.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import helloandroid.m2dl.minijeuandroid.R;
import helloandroid.m2dl.minijeuandroid.data.Score;

public class ScoreActivity extends FragmentActivity {

    private SharedPreferences sharedPreferences;

    private int score;
    private int highScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        recordScore();

        resetScore();

        // Retry Button
        final Button retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(v -> toGameActivity());

        // Main Menu Button
        final Button mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(v -> toMenuActivity());
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

    private String getPlayerName() {
        // TODO
        return "John Doe";
    }

    private void recordScore() {
        sharedPreferences = this.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        score = sharedPreferences.getInt("last_score", 0);

        String url = getResources().getString(R.string.database_url);
        FirebaseDatabase database = FirebaseDatabase.getInstance(url);
        DatabaseReference myRef = database.getReference();
        DatabaseReference high_score_ref = database.getReference("high_score");
        ValueEventListener scoreListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                highScore = dataSnapshot.getValue(Integer.class);
                if (score > highScore) {
                    myRef.child("high_score").setValue(score);
                }
                TextView textViewScore = findViewById(R.id.score_value);
                runOnUiThread(() -> textViewScore.setText(String.valueOf(score)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                System.err.println("Failed database access");
            }
        };
        high_score_ref.addValueEventListener(scoreListener);

        // Enregistrement en BD
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM,
                Locale.FRANCE);
        String formattedDate = formatter.format(new Date());
        Score scoreObject = new Score(getPlayerName(), score, formattedDate);
        myRef.child("scores").push().setValue(scoreObject);
    }

    private void resetScore() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("last_score", 0);
        editor.apply();

    }
}