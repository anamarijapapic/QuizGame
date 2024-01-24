package org.oss.quizgame.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import org.oss.quizgame.R;

import java.util.ArrayList;

public class LeaderboardScoreAdapter extends RecyclerView.Adapter<LeaderboardScoreViewHolder> {

    private final ArrayList<LeaderboardScore> localDataSet;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public LeaderboardScoreAdapter(ArrayList<LeaderboardScore> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public LeaderboardScoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.leaderboard_score, viewGroup, false);

        return new LeaderboardScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardScoreViewHolder viewHolder, int position) {
        LeaderboardScore item = localDataSet.get(position);
        String username = item.getUsername();
        int score = item.getScore();

        viewHolder.getPlace().setText(String.valueOf(position + 1));
        viewHolder.getUsername().setText(username);
        viewHolder.getScore().setText(String.valueOf(score));

        String email = mAuth.getCurrentUser().getEmail();
        if (email.substring(0, email.indexOf('@')).equals(username)) {
            viewHolder.itemView.setBackgroundColor(
                    ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.grey)
            );
        }
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}