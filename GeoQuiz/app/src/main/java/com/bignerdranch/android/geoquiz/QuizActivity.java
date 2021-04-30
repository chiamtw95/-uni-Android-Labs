package com.bignerdranch.android.geoquiz;

import android.content.res.Resources;
import android.media.Image;
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
    public static final String TAG = "QuizActivity";
    public static final String KEY_INDEX = "index";
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private int mCurrentIndex = 0;
    private int scoreCounter = 0;
    private int score;

    private Question [] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia,true)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState != null)
            mCurrentIndex =savedInstanceState.getInt(KEY_INDEX, 0);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        updateQuestion(0);
        resetButtons();
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
    }

    private void updateQuestion(int i){

        if(i > 0 && mCurrentIndex >= 0 && mCurrentIndex <=4){
            mCurrentIndex = (mCurrentIndex + i);
            resetButtons();
        }
        else if(i >0 && mCurrentIndex >= 1 && mCurrentIndex <=5){
            mCurrentIndex = (mCurrentIndex + i);
            resetButtons();
        }
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        String scoreString;
        Toast toast = null;
        Resources res = getResources();

        if (mCurrentIndex == mQuestionBank.length - 1) {
            if (userPressedTue == answerIsTrue) {
                updateScore();
                scoreString = String.format(res.getString(R.string.score_toast), getString(R.string.correct_toast), score);
            }
            else
//                scoreString = getString(R.string.score_toast, getString(R.string.incorrect_toast), finalScore);
                scoreString = String.format(res.getString(R.string.score_toast), getString(R.string.incorrect_toast), score);
            toast = Toast.makeText(this, scoreString, Toast.LENGTH_SHORT);
        }
        else {
            if (userPressedTue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                updateScore();
            }
            else
                messageResId = R.string.incorrect_toast;
            toast = Toast.makeText(this, messageResId,Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.TOP, 0,0);
        toast.show();
    }

    private void updateScore(){
        scoreCounter = scoreCounter + 1;
        score = (scoreCounter/mQuestionBank.length) * 100;
    }

    private void resetButtons(){
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
        if(mCurrentIndex == 0 )
            mPreviousButton.setEnabled(false);
        if (mCurrentIndex == mQuestionBank.length - 1)
            mNextButton.setEnabled(false);
    }

    private void disableButtons(){
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState: ");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }


}
