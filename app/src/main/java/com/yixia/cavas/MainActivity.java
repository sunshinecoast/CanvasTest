package com.yixia.cavas;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    QuestionnaireProgressView mQuestionnaireProgress1;
    QuestionnaireProgressView mQuestionnaireProgress2;
    QuestionnaireProgressView mQuestionnaireProgress3;

    private int mValue = 1;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//            mQuestionnaireProgress1.setCurrentValue(mValue++, QuestionnaireProgressView.QuestionProgressState.ANSWER);
//            if (mValue <= 100) {
//                mHandler.sendEmptyMessageDelayed(0, 10);
//            }
            mQuestionnaireProgress1.setCurrentValue(50, "1231231",
                    QuestionnaireProgressView.QuestionProgressState.ANSWER);
            mQuestionnaireProgress3.setCurrentValue(30, "23123123123",
                    QuestionnaireProgressView.QuestionProgressState.ANSWER_ERROR);
            mQuestionnaireProgress2.setCurrentValue(0, "你好",
                    QuestionnaireProgressView.QuestionProgressState.SElECT);
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionnaireProgress1 = findViewById(R.id.questionnaire_progress1);
        mQuestionnaireProgress2 = findViewById(R.id.questionnaire_progress2);
        mQuestionnaireProgress3 = findViewById(R.id.questionnaire_progress3);
//        mQuestionnaireProgress.setCurrentValue(20);
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }
}
