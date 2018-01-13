package com.yixia.cavas;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    QuestionnaireProgress mQuestionnaireProgress;
    private int mValue = 1;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mQuestionnaireProgress.setCurrentValue(mValue++);
            if (mValue <= 100) {
                mHandler.sendEmptyMessageDelayed(0, 10);
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionnaireProgress = findViewById(R.id.questionnaire_progress);
//        mQuestionnaireProgress.setCurrentValue(20);
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }
}
