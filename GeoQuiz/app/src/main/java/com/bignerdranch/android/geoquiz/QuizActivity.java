package com.bignerdranch.android.geoquiz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.sax.TextElementListener;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity {
    public static final String TAG = "QuizActivity", KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0,
                            REQUEST_RESULTS_SUMMARY = 1;
    private Question [] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia,true) };
    private Button mTrueButton, mFalseButton, mResetButton, mCheatButton, mResultsButton;
    private ProgressBar mProgressBar;
    private ImageButton mNextButton, mPreviousButton;
    private TextView mQuestionTextView, mCheatTextView;
    private int mCurrentIndex = 0, mScoreCounter = 0, mCheatCounter = 0;
    private float mScore = 0, mProgress = 0;
    private int mQuestionsAnswered = 0;
    private boolean[] mIsCheater = new boolean[mQuestionBank.length];
    private boolean[] mAnswered = new boolean[mQuestionBank.length];


    private void updateQuestion(int i){

        if(i > 0 && mCurrentIndex >= 0 && mCurrentIndex <=4)
            mCurrentIndex = (mCurrentIndex + i);
        else if(i < 0 && mCurrentIndex >= 1 && mCurrentIndex <=5)
            mCurrentIndex = (mCurrentIndex + i);

        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        resetButtons();
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        String toastString;

        mQuestionsAnswered++;
        mAnswered[mCurrentIndex] = true;
        mProgress++;
        setmProgressBar();
        //dealing with a cheater
        if(mIsCheater[mCurrentIndex]) {
            if (userPressedTrue == answerIsTrue)
                updateScore();
            if(mProgress == mQuestionBank.length)
                toastString = String.format(this.getString(R.string.score_toast) , getString(R.string.cheating_toast), mScore);
            else
                toastString = getString(R.string.cheating_toast);
            makeToastAndShow(toastString);
            updateCheatTextView();
            return;
        }
        //Checks if at last question
        if(mProgress == mQuestionBank.length) {
            if (userPressedTrue == answerIsTrue) {
                updateScore();
                toastString = String.format(this.getString(R.string.score_toast), getString(R.string.correct_toast), mScore);
            }
            else
                toastString = String.format(this.getString(R.string.score_toast), getString(R.string.incorrect_toast), mScore);
            makeToastAndShow(toastString);
        }
        else {
            if (userPressedTrue == answerIsTrue){
                toastString = getString(R.string.correct_toast);
                updateScore();
            }
            else
                toastString = getString(R.string.incorrect_toast);
            makeToastAndShow(toastString);
        }
    }

    private void updateScore(){
        mScoreCounter += 1;
        mScore= (float) mScoreCounter/(float) mQuestionBank.length * 100;
    }

    private void updateCheatTextView(){
        mCheatCounter = 0;
        for(int i=0; i<mIsCheater.length; i++) {
            if (mIsCheater[i] == true)
                mCheatCounter++;
        }
        mCheatTextView.setText(getString(R.string.cheat_textview,3 - mCheatCounter));
    }

    private void resetButtons(){
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
        mPreviousButton.setEnabled(true);
        mNextButton.setEnabled(true);
        mCheatButton.setEnabled(true);

        if(mCheatCounter >= 3 && mIsCheater[mCurrentIndex] == false)
            mCheatButton.setEnabled(false);
        else
            mCheatButton.setEnabled(true);
        updateCheatTextView();
        if(mAnswered[mCurrentIndex] == true) {
            disableButtons();
            mCheatButton.setEnabled(false);
        }
        if(mCurrentIndex == 0)
            mPreviousButton.setEnabled(false);
        else if (mCurrentIndex == mQuestionBank.length - 1)
            mNextButton.setEnabled(false);

    }

    private void disableButtons(){
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    private void makeToastAndShow(String s){
        Toast toast;
        toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0,0);
        toast.show();
    }

    private void setmProgressBar(){
        mProgressBar.setProgress((int) ((mProgress)/(float)mQuestionBank.length *100));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return;
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data ==null)
                return;
            mIsCheater[mCurrentIndex] = CheatActivity.wasAnswerShown(data);
            updateCheatTextView();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Question.shuffleQuestions(mQuestionBank);
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mCheatTextView = (TextView) findViewById(R.id.cheat_textview);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mResetButton = (Button) findViewById(R.id.reset_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mResultsButton = (Button) findViewById(R.id.result_summary_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);

        updateQuestion(0);
        resetButtons();
        setmProgressBar();
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuestion(1);
            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                checkAnswer(true);
                resetButtons();
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                checkAnswer(false);
                resetButtons();

            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = 0;
                mScoreCounter = 0;
                mScore = 0;
                mQuestionsAnswered = 0;
                mProgress = 0;
                mIsCheater = new boolean[mQuestionBank.length];
                mCheatCounter = 0;
                mAnswered = new boolean[mQuestionBank.length];
                Question.shuffleQuestions(mQuestionBank);
                updateQuestion(0);
                setmProgressBar();
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateQuestion(1);
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateQuestion(-1);
            }
        });

        mResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ResultSummary.newIntent(QuizActivity.this,
                                                        mQuestionsAnswered,
                                                        mQuestionBank.length,
                                                        mScore,
                                                        mCheatCounter);
                startActivityForResult(intent,REQUEST_RESULTS_SUMMARY);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState: ");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt("KEY_SCORECOUNTER",mScoreCounter);
        savedInstanceState.putFloat("KEY_SCORE",mScore);
        savedInstanceState.putBoolean("KEY_TRUEBUTTON_ENABLED", mTrueButton.isEnabled());
        savedInstanceState.putBooleanArray("KEY_MISCHEATER",mIsCheater);
        savedInstanceState.putBooleanArray("KEY_MANSWERED", mAnswered);
        savedInstanceState.putInt("KEY_MQUESTIONSANSWERED", mQuestionsAnswered);
        savedInstanceState.putInt("KEY_CHEATCOUNTER", mCheatCounter);
        savedInstanceState.putFloat("KEY_MPROGRESS",mProgress);
        savedInstanceState.putSerializable("KEY_MQUESTIONBANK", mQuestionBank);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null) {
            mQuestionBank = (Question[]) savedInstanceState.getSerializable("KEY_MQUESTIONBANK");
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            updateQuestion(0);
            mScoreCounter = savedInstanceState.getInt("KEY_SCORECOUNTER", 0);
            mScore = savedInstanceState.getFloat("KEY_SCORE", 0);
            mIsCheater = savedInstanceState.getBooleanArray("KEY_MISCHEATER");
            mAnswered = savedInstanceState.getBooleanArray("KEY_MANSWERED");
            mQuestionsAnswered = savedInstanceState.getInt("KEY_MQUESTIONSANSWERED");
            mCheatCounter = savedInstanceState.getInt("KEY_CHEATCOUNTER");
            mProgress = savedInstanceState.getFloat("KEY_MPROGRESS");
            resetButtons();
            if (savedInstanceState.getBoolean("KEY_TRUEBUTTON_ENABLED") == false)
                disableButtons();
        }
    }


}
