package org.oss.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SummaryActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);

        TextView congratsText = findViewById(R.id.congratulations);
        TextView scoreText = findViewById(R.id.score);
        TextView highScoreText = findViewById(R.id.highScore);

        congratsText.setText(getString(R.string.congratulations, mAuth.getCurrentUser().getEmail()));
        scoreText.setText(getString(R.string.score, score));
        highScoreText.setText(getString(R.string.high_score, score));

        Button playAgainButton = findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(view -> startActivity(new Intent(SummaryActivity.this, GameActivity.class)));
    }
}