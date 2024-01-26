package org.oss.quizgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.oss.quizgame.api.ApiResponse;
import org.oss.quizgame.api.QuestionRepository;
import org.oss.quizgame.api.QuestionService;

import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameActivity extends AppCompatActivity {

    private QuestionRepository[] questions;
    private TextView timeText;
    private TextView scoreText;
    private TextView questionText;
    private Button trueButton, falseButton;
    private AtomicInteger score;
    private int questionNumber;
    private boolean isCallInterrupted = false;
    private CountDownTimer countDownTimer;
    private long remainingTimeMillis; // To store remaining time during interruptions

    private static final String KEY_SCORE = "score";
    private static final String KEY_QUESTION_NUMBER = "questionNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (savedInstanceState != null) {
            // Restore the game state when the activity is recreated
            score = new AtomicInteger(savedInstanceState.getInt(KEY_SCORE));
            questionNumber = savedInstanceState.getInt(KEY_QUESTION_NUMBER);
            isCallInterrupted = savedInstanceState.getBoolean("isCallInterrupted");
            remainingTimeMillis = savedInstanceState.getLong("remainingTimeMillis");
        } else {
            score = new AtomicInteger();
            questionNumber = 0;
            remainingTimeMillis = 60000; // Default 1 minute if not interrupted
        }

        if (!isCallInterrupted) {
            // Only retrieve questions from the API if not interrupted by a call
            retrieveQuestions();
        } else {
            // If interrupted, resume the game with the saved state
            resumeGame();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // Save the game state when the activity is destroyed
        outState.putInt(KEY_SCORE, score.get());
        outState.putInt(KEY_QUESTION_NUMBER, questionNumber);
        outState.putBoolean("isCallInterrupted", isCallInterrupted);
        outState.putLong("remainingTimeMillis", remainingTimeMillis);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause the game when the activity is paused (e.g., on a phone call)
        isCallInterrupted = true;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume the game when the activity is resumed after a pause (e.g., after a phone call)
        if (isCallInterrupted) {
            isCallInterrupted = false;
            resumeGame();
        }
    }

    private void retrieveQuestions() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuestionService service = retrofit.create(QuestionService.class);
        Call<ApiResponse> call = service.getQuestions();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                ApiResponse apiResponse = response.body();
                assert apiResponse != null;

                questions = apiResponse.getResults();

                startGame();
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.e("Error", "Failed to retrieve questions", t);
            }
        });
    }

    private void resumeGame() {
        // Recreate the game loop with a new countdown timer
        countDownTimer = new CountDownTimer(remainingTimeMillis, 1000) { // Use remaining time
            final MediaPlayer mp = MediaPlayer.create(GameActivity.this, R.raw.clock_timer);

            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeMillis = millisUntilFinished; // Update remaining time
                timeText.setText(getString(R.string.time, millisUntilFinished / 1000));
                mp.start();
                displayQuestion();
            }

            @Override
            public void onFinish() {
                mp.stop();
                Intent intent = new Intent(GameActivity.this, SummaryActivity.class);
                intent.putExtra("score", score.get());
                startActivity(intent);
            }
        }.start();
    }

    private void startGame() {
        timeText = findViewById(R.id.remainingTime);
        scoreText = findViewById(R.id.score);
        questionText = findViewById(R.id.questionText);
        trueButton = findViewById(R.id.trueButton);
        falseButton = findViewById(R.id.falseButton);

        scoreText.setText(getString(R.string.score, score.get()));

        setupButtonListeners();

        // Start the game loop with a countdown timer
        // When the game loop is finished, show game summary
        countDownTimer = new CountDownTimer(remainingTimeMillis, 1000) { // Use remaining time
            final MediaPlayer mp = MediaPlayer.create(GameActivity.this, R.raw.clock_timer);

            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeMillis = millisUntilFinished; // Update remaining time
                timeText.setText(getString(R.string.time, millisUntilFinished / 1000));
                mp.start();
                displayQuestion();
            }

            @Override
            public void onFinish() {
                mp.stop();
                Intent intent = new Intent(GameActivity.this, SummaryActivity.class);
                intent.putExtra("score", score.get());
                startActivity(intent);
            }
        }.start();
    }

    private void setupButtonListeners() {
        trueButton.setOnClickListener(view -> handleAnswer("True"));
        falseButton.setOnClickListener(view -> handleAnswer("False"));
    }

    private void displayQuestion() {
        if (questionNumber < questions.length) {
            QuestionRepository question = questions[questionNumber];

            // Decode HTML entities in the question text
            String decodedQuestion = Html.fromHtml(question.getQuestion(), Html.FROM_HTML_MODE_LEGACY).toString();

            questionText.setText(decodedQuestion);
        }
    }

    private void handleAnswer(String selectedAnswer) {
        int points = 100;
        MediaPlayer mp;

        if (questionNumber < questions.length) {
            QuestionRepository question = questions[questionNumber];

            if (question.getCorrectAnswer().equals(selectedAnswer)) {
                mp = MediaPlayer.create(this, R.raw.right_answer);
                mp.start();
                score.getAndAdd(points);
            } else if (score.get() > 0) {
                mp = MediaPlayer.create(this, R.raw.wrong_answer);
                mp.start();
                score.getAndAdd(-points);
            } else {
                mp = MediaPlayer.create(this, R.raw.wrong_answer);
                mp.start();
            }

            scoreText.setText(getString(R.string.score, score.get()));

            questionNumber++;

            displayQuestion();
        }
    }
}