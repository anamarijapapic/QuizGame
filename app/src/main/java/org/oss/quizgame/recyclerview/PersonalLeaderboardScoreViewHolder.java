package org.oss.quizgame.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.oss.quizgame.R;

public class PersonalLeaderboardScoreViewHolder extends RecyclerView.ViewHolder {

    private final TextView place;
    private final TextView datetime;
    private final TextView score;

    public PersonalLeaderboardScoreViewHolder(View view) {
        super(view);

        place = view.findViewById(R.id.place);
        datetime = view.findViewById(R.id.datetime);
        score = view.findViewById(R.id.score);
    }

    public TextView getPlace() {
        return place;
    }

    public TextView getDatetime() {
        return datetime;
    }

    public TextView getScore() {
        return score;
    }
}
