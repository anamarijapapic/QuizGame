package org.oss.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SummaryActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    int currentScore;
    int highScore;
    ValueAnimator scoreAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("leaderboard");

        Intent intent = getIntent();
        currentScore = intent.getIntExtra("score", 0);

        getHighScore(mAuth.getCurrentUser().getEmail());

        Button playAgainButton = findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(view -> startActivity(new Intent(SummaryActivity.this, GameActivity.class)));

        Button leaderboardButton = findViewById(R.id.leaderboardButton);
        leaderboardButton.setOnClickListener(view -> startActivity(new Intent(SummaryActivity.this, LeaderboardActivity.class)));

        TextView mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(view -> startActivity(new Intent(SummaryActivity.this, MainActivity.class)));
    }

    private void getHighScore(String email) {
        String username = email.substring(0, email.indexOf('@'));

        mDatabase.child(username).get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                highScore = snapshot.getValue(Integer.class);
            } else {
                highScore = 0;
            }

            updateHighScore(mAuth.getCurrentUser().getEmail());

            // Update UI with highScore
            TextView congratsText = findViewById(R.id.congratulations);
            TextView scoreText = findViewById(R.id.score);
            TextView highScoreText = findViewById(R.id.highScore);

            congratsText.setText(getString(R.string.congratulations, mAuth.getCurrentUser().getEmail()));
            scoreText.setText(getString(R.string.score, currentScore));
            highScoreText.setText(getString(R.string.high_score, highScore));
        }).addOnFailureListener(e -> {
            Log.d("Firebase", "Error getting data", e);
            highScore = 0;
        });
    }

    private void updateHighScore(String email) {
        TextView highScoreText = findViewById(R.id.highScore);

        // Extract username from email
        String username = email.substring(0, email.indexOf('@'));

        // Check if the current score is higher than the existing high score
        if (currentScore > highScore) {
            // Create a ValueAnimator if it hasn't been created yet
            if (scoreAnimator == null) {
                // Initialize the ValueAnimator with the current and target values
                scoreAnimator = ValueAnimator.ofInt(highScore, currentScore);
                scoreAnimator.setDuration(1000); // Set the duration of the animation as needed
                // Add an update listener to handle the interpolated values during animation
                scoreAnimator.addUpdateListener(animation -> {
                    int animatedValue = (int) animation.getAnimatedValue();
                    // Update the highScoreText with the animated value
                    highScoreText.setText(getString(R.string.high_score, animatedValue));
                });
                // Start the animation
                scoreAnimator.start();
            } else {
                // If an animation already exists, set a new target value and start the animation again
                scoreAnimator.setIntValues(highScore, currentScore);
                scoreAnimator.start();
            }

            // Update the actual highScore only if the new score is higher
            highScore = currentScore;
            // Update the high score in the database
            mDatabase.child(username).setValue(highScore);
        }
    }
}