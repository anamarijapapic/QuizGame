package org.oss.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView logout;
    FirebaseAuth mAuth;
    Button startGame;
    Button viewLeaderboard;
    Button viewPersonalLeaderboard;
    Button viewPersonalLeaderboardTopScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Successful logout", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        startGame = findViewById(R.id.startGameButton);
        startGame.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, GameActivity.class)));

        viewLeaderboard = findViewById(R.id.leaderboardButton);
        viewLeaderboard.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LeaderboardActivity.class)));

        viewPersonalLeaderboard = findViewById(R.id.personalLeaderboardButton);
        viewPersonalLeaderboard.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, PersonalLeaderboardActivity.class)));

        viewPersonalLeaderboardTopScores = findViewById(R.id.personalLeaderboardTopScoresButton);
        viewPersonalLeaderboardTopScores.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, PersonalLeaderboardTopScoresActivity.class)));
    }
}