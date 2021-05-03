package com.bignerdranch.android.geoquiz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;

public class QuizActivity extends AppCompatActivity {
    public static final String TAG = "QuizActivity", KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private Button mTrueButton, mFalseButton, mResetButton, mCheatButton;
    private ImageButton mNextButton, mPreviousButton;
    private TextView mQuestionTextView;
    private int mCurrentIndex = 0, scoreCounter = 0, cheatCounter = 0;
    private float score = 0;
    private boolean mIsCheater;
    private Question [] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia,true)
    };
    private boolean[] mAnswered = new boolean[mQuestionBank.length];
    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }




    private void updateQuestion(int i){

        if(i > 0 && mCurrentIndex >= 0 && mCurrentIndex <=4)
            mCurrentIndex = (mCurrentIndex + i);
        else if(i < 0 && mCurrentIndex >= 1 && mCurrentIndex <=5)
            mCurrentIndex = (mCurrentIndex + i);

        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        resetButtons();
    }

    @SuppressLint("StringFormatMatches")
    private void checkAnswer(boolean userPressedTue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        String toastString;

        mAnswered[mCurrentIndex] = true;
        if(mIsCheater) {
            if(mCurrentIndex == mQuestionBank.length - 1)
                toastString = String.format(this.getString(R.string.score_toast) , getString(R.string.cheating_toast), score);
            else
                toastString = getString(R.string.cheating_toast);
            makeToastAndShow(toastString);
            return;
        }
        //Checks if at last question
        if(mCurrentIndex == mQuestionBank.length - 1) {
            if (userPressedTue == answerIsTrue) {
                updateScore();
                toastString = String.format(this.getString(R.string.score_toast), getString(R.string.correct_toast), getScore());
            }
            else
                toastString = String.format(this.getString(R.string.score_toast), getString(R.string.incorrect_toast), score);
            makeToastAndShow(toastString);
        }
        else {
            if (userPressedTue == answerIsTrue){
                toastString = getString(R.string.correct_toast);
                updateScore();
            }
            else
                toastString = getString(R.string.incorrect_toast);
            makeToastAndShow(toastString);
        }
    }

    private void updateScore(){
        scoreCounter = scoreCounter + 1;
        float f = (float) scoreCounter/(float) mQuestionBank.length * 100;
        setScore(f);
    }

    private void resetButtons(){
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
        mPreviousButton.setEnabled(true);
        mNextButton.setEnabled(true);
        mCheatButton.setEnabled(true);

        if(cheatCounter >= 3)
            mCheatButton.setEnabled(false);
        if(mAnswered[mCurrentIndex] == true)
            disableButtons();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mResetButton = (Button) findViewById(R.id.reset_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);

        updateQuestion(0);

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
                disableButtons();
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                checkAnswer(false);
                disableButtons();
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = 0;
                scoreCounter = 0;
                score = 0;
                mIsCheater =false;
                cheatCounter = 0;
                mAnswered = new boolean[mQuestionBank.length];
                updateQuestion(0);
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
                cheatCounter++;
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateQuestion(1);
                mIsCheater = false;
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateQuestion(-1);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState: ");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt("KEY_SCORECOUNTER",scoreCounter);
        savedInstanceState.putFloat("KEY_SCORE",score);
        savedInstanceState.putBoolean("KEY_TRUEBUTTON_ENABLED", mTrueButton.isEnabled());
        savedInstanceState.putBoolean("KEY_MISCHEATER",mIsCheater);
        savedInstanceState.putBooleanArray("KEY_MANSWERED", mAnswered);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return;
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data ==null)
                return;
            mIsCheater= CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            updateQuestion(0);
            resetButtons();
            scoreCounter = savedInstanceState.getInt("KEY_SCORECOUNTER", 0);
            score = savedInstanceState.getFloat("KEY_SCORE", 0);
            mIsCheater = savedInstanceState.getBoolean("KEY_MISCHEATER", false);
            mAnswered = savedInstanceState.getBooleanArray("KEY_MANSWERED");

            if (savedInstanceState.getBoolean("KEY_TRUEBUTTON_ENABLED") == false)
                disableButtons();
        }
    }


}
