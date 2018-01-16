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
            mQuestionnaireProgress1.setCurrentValue( "12月4号", "1231231");
            mQuestionnaireProgress3.setCurrentValue("12342342", "1231231");
            mQuestionnaireProgress2.setCurrentValue("你好", "1231231");
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
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }
}
