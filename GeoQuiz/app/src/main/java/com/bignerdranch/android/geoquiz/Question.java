package com.bignerdranch.android.geoquiz;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

public class Question implements Serializable {
    private int mTextResId;
    private boolean mAnswerTrue;

    public Question(int textResId, boolean answerTrue){
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public static void shuffleQuestions(Question[] mQuestionBank){
        Collections.shuffle(Arrays.asList(mQuestionBank));
    }


}
