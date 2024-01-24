package org.oss.quizgame.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.oss.quizgame.R;

public class LeaderboardScoreViewHolder extends RecyclerView.ViewHolder {

    private final TextView place;
    private final TextView username;
    private final TextView score;

    public LeaderboardScoreViewHolder(View view) {
        super(view);

        place = view.findViewById(R.id.place);
        username = view.findViewById(R.id.username);
        score = view.findViewById(R.id.score);
    }

    public TextView getPlace() {
        return place;
    }

    public TextView getUsername() {
        return username;
    }

    public TextView getScore() {
        return score;
    }
}
