package org.oss.quizgame.api;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class QuestionRepository {
    @SerializedName("type")
    @Expose
    private final String type;
    @SerializedName("difficulty")
    @Expose
    private final String difficulty;
    @SerializedName("category")
    @Expose
    private final String category;
    @SerializedName("question")
    @Expose
    private final String question;
    @SerializedName("correct_answer")
    @Expose
    private final String correctAnswer;
    @SerializedName("incorrect_answers")
    @Expose
    private final String[] incorrectAnswers;

    public QuestionRepository(String type, String difficulty, String category, String question, String correct_answer, String[] incorrect_answers) {
        this.type = type;
        this.difficulty = difficulty;
        this.category = category;
        this.question = question;
        this.correctAnswer = correct_answer;
        this.incorrectAnswers = incorrect_answers;
    }

    @NonNull
    @Override
    public String toString() {
        return "QuestionRepository{" +
                "type='" + type + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", category='" + category + '\'' +
                ", question='" + question + '\'' +
                ", correct_answer='" + correctAnswer + '\'' +
                ", incorrect_answers='" + Arrays.toString(incorrectAnswers) + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getIncorrectAnswers() {
        return incorrectAnswers;
    }
}
