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

import org.oss.quizgame.recyclerview.PersonalLeaderboardScore;
import org.oss.quizgame.recyclerview.PersonalLeaderboardScoreAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class PersonalLeaderboardActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ArrayList<PersonalLeaderboardScore> results = new ArrayList<>();
    TextView personalLeaderboardTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_leaderboard);

        mAuth = FirebaseAuth.getInstance();

        String email = mAuth.getCurrentUser().getEmail();
        String username = email.substring(0, email.indexOf('@'));

        personalLeaderboardTitle = findViewById(R.id.personalLeaderboardTitle);
        personalLeaderboardTitle.setText(getString(R.string.personal_leaderboard_title_username, username));

        mDatabase = FirebaseDatabase.getInstance().getReference("personal_leaderboard/" + username);

        PersonalLeaderboardScoreAdapter customAdapter = new PersonalLeaderboardScoreAdapter(results);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    ArrayList<PersonalLeaderboardScore> personalLeaderboardScores = new ArrayList<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String timestamp = child.getKey();
                        int score = child.getValue(Integer.class);
                        personalLeaderboardScores.add(new PersonalLeaderboardScore(timestamp, score));
                    }

                    // Sort the userScores list in descending order based on scores
                    personalLeaderboardScores.sort(Collections.reverseOrder());

                    // Add the sorted list to the results list
                    results.clear();
                    results.addAll(personalLeaderboardScores);

                    customAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PersonalLeaderboardActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(view -> finish());

        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(PersonalLeaderboardActivity.this, "Successful logout", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PersonalLeaderboardActivity.this, LoginActivity.class));
        });
    }
}