package com.bignerdranch.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.bignerdranch.android.geoquiz.R.string.results_score;

public class ResultSummary extends AppCompatActivity {

    private TextView mQuestionsAnsweredTextView, mFinalScoreTextView, mNumberofCheatsTextView;
    private int length, cheats;
    private float score;
    private static final String
        EXTRA_NQUESTIONS = "com.bignerdranch.android.geoquiz.size",
        EXTRA_SCORE_FINAL = "com.bignerdranch.android.geoquiz.score",
        EXTRA_NCHEATS = "com.bignerdranch.android.geoquiz.cheatCounter";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_summary);


        mQuestionsAnsweredTextView = (TextView) findViewById(R.id.results_question_answered);
        mFinalScoreTextView = (TextView) findViewById(R.id.results_score);
        mNumberofCheatsTextView = (TextView) findViewById(R.id.results_cheats);

        length = getIntent().getIntExtra(EXTRA_NQUESTIONS,0);
        cheats = getIntent().getIntExtra(EXTRA_NCHEATS,0);
        score = getIntent().getFloatExtra(EXTRA_SCORE_FINAL,0);


        mFinalScoreTextView.setText(this.getString(results_score, score));
        mQuestionsAnsweredTextView.setText(this.getString(R.string.results_questions_answered, length));
        mNumberofCheatsTextView.setText(this.getString(R.string.results_cheats,cheats));
    }

    public static Intent newIntent(Context packageContext, int length, float score, int nCheats) {
        Intent intent = new Intent(packageContext, ResultSummary.class);
        intent.putExtra(EXTRA_NQUESTIONS, length);
        intent.putExtra(EXTRA_SCORE_FINAL, score);
        intent.putExtra(EXTRA_NCHEATS, nCheats);
        return intent;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(EXTRA_NCHEATS, length);
        savedInstanceState.putInt(EXTRA_NQUESTIONS,cheats);
        savedInstanceState.putFloat(EXTRA_SCORE_FINAL,score);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            length = savedInstanceState.getInt(EXTRA_NQUESTIONS, 0);
            score = savedInstanceState.getFloat(EXTRA_SCORE_FINAL, 0);
            cheats = savedInstanceState.getInt(EXTRA_NCHEATS, 0);
        }
    }

}
