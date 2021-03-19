package helloandroid.m2dl.minijeuandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import helloandroid.m2dl.minijeuandroid.R;

public class MenuActivity extends Activity {

    private int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setHighScore();

        final Button newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(v -> toGameActivity());

        final Button quitButton = findViewById(R.id.quit_button);
        quitButton.setOnClickListener(v -> System.exit(0));
    }

    public void toGameActivity() {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    private void setHighScore() {
        String url = getResources().getString(R.string.database_url);
        FirebaseDatabase database = FirebaseDatabase.getInstance(url);
        DatabaseReference high_score_ref = database.getReference("high_score");
        ValueEventListener scoreListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                highScore = dataSnapshot.getValue(Integer.class).intValue();
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