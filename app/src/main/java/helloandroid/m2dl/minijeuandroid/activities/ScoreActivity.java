package helloandroid.m2dl.minijeuandroid.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import helloandroid.m2dl.minijeuandroid.R;
import helloandroid.m2dl.minijeuandroid.models.Score;

import static helloandroid.m2dl.minijeuandroid.R.string.no_internet_connection;

public class ScoreActivity extends FragmentActivity {

    private SharedPreferences sharedPreferences;

    private int score;
    private int highScore;
    private boolean alreadySaved;
    private EditText editTextUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        editTextUserName = findViewById(R.id.username_value);

        this.alreadySaved = false;

        // Save score on DB
        sharedPreferences = this.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        score = sharedPreferences.getInt("last_score", 0);

        TextView textViewScore = findViewById(R.id.score_value);
        runOnUiThread(() -> textViewScore.setText(String.valueOf(score)));

        // SaveScore Button
        final Button saveScoreButton = findViewById(R.id.saveScoreButton);
        saveScoreButton.setOnClickListener(v -> saveScore());

        // Retry Button
        final Button retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(v -> toGameActivity());

        // Main Menu Button
        final Button mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(v -> toMenuActivity());
    }

    private void saveScore() {
        editTextUserName.clearFocus();
        // Check if connected to internet
        if (isConnected() && !alreadySaved) {
            // Get EditText value
            String userName = getUserName();

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
            Score scoreObject = new Score(userName, score, formattedDate);
            myRef.child("scores").push().setValue(scoreObject);
            this.alreadySaved = true;
        } else if (alreadySaved) {
            // Modal
            Snackbar.make(findViewById(R.id.score_label), getResources().getText(R.string.score_already_saved), Snackbar.LENGTH_SHORT).show();
        } else {
            // Modal
            Snackbar.make(findViewById(R.id.score_label), getResources().getText(no_internet_connection), Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean result = false;
        if (activeNetwork != null) {
            result = activeNetwork.isConnectedOrConnecting();
        }
        return result;
    }

    public void toGameActivity() {
        startActivity(new Intent(this, GameActivity.class));
        finish();
    }

    public void toMenuActivity() {
        finish();
    }

    private String getUserName() {
        if (editTextUserName != null) {
            return editTextUserName.getText().toString();
        }
        return "John Doe";
    }
}