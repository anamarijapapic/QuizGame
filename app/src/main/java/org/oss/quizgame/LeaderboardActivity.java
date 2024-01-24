package org.oss.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.oss.quizgame.recyclerview.LeaderboardScore;
import org.oss.quizgame.recyclerview.LeaderboardScoreAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ArrayList<LeaderboardScore> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("leaderboard");

        LeaderboardScoreAdapter customAdapter = new LeaderboardScoreAdapter(results);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    ArrayList<LeaderboardScore> leaderboardScores = new ArrayList<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String userId = child.getKey();
                        int score = child.getValue(Integer.class);
                        leaderboardScores.add(new LeaderboardScore(userId, score));
                    }

                    // Sort the userScores list in descending order based on scores
                    leaderboardScores.sort(Collections.reverseOrder());

                    // Keep only the top 10 results (or less if there are fewer than 10)
                    results.clear();  // Clear the existing list
                    results.addAll(leaderboardScores.subList(0, Math.min(10, leaderboardScores.size())));

                    // Update the customAdapter with the top 10 results
                    customAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(view -> finish());

        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(LeaderboardActivity.this, "Successful logout", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LeaderboardActivity.this, LoginActivity.class));
        });
    }
}