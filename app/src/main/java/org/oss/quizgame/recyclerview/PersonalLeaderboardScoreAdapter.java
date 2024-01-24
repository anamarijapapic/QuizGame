package org.oss.quizgame.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.oss.quizgame.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PersonalLeaderboardScoreAdapter extends RecyclerView.Adapter<PersonalLeaderboardScoreViewHolder> {

    private final ArrayList<PersonalLeaderboardScore> localDataSet;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public PersonalLeaderboardScoreAdapter(ArrayList<PersonalLeaderboardScore> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public PersonalLeaderboardScoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.personal_leaderboard_score, viewGroup, false);

        return new PersonalLeaderboardScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalLeaderboardScoreViewHolder viewHolder, int position) {
        PersonalLeaderboardScore item = localDataSet.get(position);

        String timestamp = item.getTimestamp();
        Date date = new Date(Long.parseLong(timestamp));
        String datetime = sdf.format(date);
        int score = item.getScore();

        viewHolder.getPlace().setText(String.valueOf(position + 1));
        viewHolder.getDatetime().setText(datetime);
        viewHolder.getScore().setText(String.valueOf(score));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
