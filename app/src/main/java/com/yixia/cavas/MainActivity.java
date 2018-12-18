package com.yixia.cavas;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ImageSpan;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.DBHelper;
import database.DBValues;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {

    private static final String STICKY_LABEL = "置顶";

    QuestionnaireProgressView mQuestionnaireProgress1;
    QuestionnaireProgressView mQuestionnaireProgress2;
    QuestionnaireProgressView mQuestionnaireProgress3;

    private TextView mLabelTextview;
    private TextView mLabelTextview1;
    private TextView mLabelTextview2;
    private TextView mLabelTextview3;
    private TextView mLabelTextview4;
    private Fade mFade;
    private LinearLayout mMainLayout;

    private int mValue = 1;

    private LinearLayout mChoiceLayout;

    private View view1, view2;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mQuestionnaireProgress1.setCurrentValue( "12月4号", "1231231");
            mQuestionnaireProgress3.setCurrentValue("12342342", "1231231");
            mQuestionnaireProgress2.setCurrentValue("你好", "1231231");
            return true;
        }
    });

    private IMyAidlInterface mIMyAidlInterface;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
//            mIMyAidlInterface.basicTypes()
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new FrescoConfig().initFrescoConfig(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mQuestionnaireProgress1 = findViewById(R.id.questionnaire_progress1);
//        mQuestionnaireProgress2 = findViewById(R.id.questionnaire_progress2);
//        mQuestionnaireProgress3 = findViewById(R.id.questionnaire_progress3);
//        mQuestionnaireProgress.setCurrentValue(20);
//        mHandler.sendEmptyMessageDelayed(0, 1000);

        mMainLayout = findViewById(R.id.main_layout);

        mChoiceLayout = findViewById(R.id.choice_layout);
//        view1 = findViewById(R.id.view1);
//        view2 = findViewById(R.id.view2);
        mLabelTextview = new TextView(this);
        mLabelTextview.setText("Label text sample");

        mFade = new Fade(Fade.IN);
        mFade.setDuration(1000);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("condition" , "index : " + mChoiceViewIndex + "   condition : " + mChoosedCondition);
                TransitionManager.beginDelayedTransition(mMainLayout, new ChangeBounds());
                mMainLayout.getChildAt(0).setVisibility(mMainLayout.getChildAt(0).getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//                startActivity(new Intent(MainActivity.this, SecondActivity.class));

                FrescoCacheUtil.getBitmap(
                        "https://alcdn.img.xiaoka.tv/20180709/775/fd2/348727802/775fd2223e81db395d5726f4fde3a45c.jpg",
                        30, 30,
                        new FrescoCacheUtil.IFetchCallback() {
                            @Override
                            public void loadSucess(@Nullable Bitmap bitmap) {
                                if (bitmap == null) {
                                    Log.i("zhaoxiaopo", "FrescoCacheUtil----------=============== null--------------");
                                } else {
                                    Log.i("zhaoxiaopo", "FrescoCacheUtil------------------------" + bitmap);
                                }
                            }

                            @Override
                            public void loadFailure() {

                            }
                        });

                try {
                    PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
                    Log.i("zhaoxiaopo", getPackageName());
                    for (int i = 0; i < packageInfo.signatures.length; i ++) {
                        Log.i("zhaoxiaopo", packageInfo.signatures[i].hashCode() + "");
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                Intent captureIntent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    MediaProjectionManager mMediaProjectionManager = (MediaProjectionManager) getSystemService(android.content.Context.MEDIA_PROJECTION_SERVICE);
                    captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
                    startActivityForResult(captureIntent, 43534);
                }
            }
        });


        ((SimpleDraweeView) findViewById(R.id.imageview)).setImageURI("https://alcdn.img.xiaoka.tv/20180529/3cc/ebd/0/3ccebd0a7c49b8666ea2e9c035f119a7.jpg");

//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startAnim();
//            }
//        },200 );
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//        Request request = new Request.Builder().url("http://wwww.baidu.com").build();
//        Call call = okHttpClient.newCall(request);
//        try {
//            Response response = call.execute();
//            System.out.println(response.body().string());
//            response.body().close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    System.out.println(response.body().string());
//                    response.body().close();
//                }
//            });

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < 1000; i ++) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBValues.ValueColumns.COLUMN_NAME_USER_NAME, "zhao" + i);
                    contentValues.put(DBValues.ValueColumns.COLUMN_NAME_USER_AGE, i);
                    contentValues.put(DBValues.ValueColumns.COLUMN_NAME_USER_SEX, i);
                    database.insert(DBValues.ValueColumns.TABLE_NAME, null, contentValues);
                }
                Log.i("database", "time1 : " + (System.currentTimeMillis() - startTime) + "");
                dbHelper.close();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                long startTime = System.currentTimeMillis();
                database.beginTransaction();
                for (int i = 0; i < 10000; i ++) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBValues.ValueColumns.COLUMN_NAME_USER_NAME, "zhao" + i);
                    contentValues.put(DBValues.ValueColumns.COLUMN_NAME_USER_AGE, i);
                    contentValues.put(DBValues.ValueColumns.COLUMN_NAME_USER_SEX, i);
                    database.insert(DBValues.ValueColumns.TABLE_NAME, null, contentValues);
                }
                database.setTransactionSuccessful();
                database.endTransaction();
                Log.i("database", "time2 : " + (System.currentTimeMillis() - startTime) + "");
                dbHelper.close();
            }
        });

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                SQLiteDatabase database = dbHelper.getWritableDatabase();
//                StringBuilder stringBuilder = new StringBuilder();
                long startTime = System.currentTimeMillis();
                database.beginTransaction();
                for (int i = 0; i < 10000; i ++) {
//                    stringBuilder.append("insert into ")
//                            .append(DBValues.ValueColumns.TABLE_NAME)
//                            .append(" values ('zhao")
//                            .append(i)
//                            .append("', ")
//                            .append(i)
//                            .append(", ")
//                            .append(i)
//                            .append(")");
                    String stringBuilder = "insert into " + DBValues.ValueColumns.TABLE_NAME + " values ('zhao" + i
                            + "', " + i + ", " + i + ")";
                    database.execSQL(stringBuilder.toString());
//                    stringBuilder.delete(0 ,stringBuilder.length());
                }
                database.endTransaction();
                Log.i("database", "time3 : " + (System.currentTimeMillis() - startTime) + "");
                dbHelper.close();
            }
        });

        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < 1000; i ++) {
                    String insert = "insert into " + DBValues.ValueColumns.TABLE_NAME + " values ('zhao" + i
                            + "', " + i + ", " + i + ")";
                    database.execSQL(insert);
                }
                Log.i("database", "time4 : " + (System.currentTimeMillis() - startTime) + "");
                dbHelper.close();
            }
        });

//        try {
//            thread1.start();
//            thread1.join();
//            thread2.start();
//            thread2.join();
//            thread3.start();
//            thread3.join();
//            thread4.start();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        TextView textView = findViewById(R.id.fwaefawefawef);
//        SpannableString spannableString = new SpannableString("置顶置顶");
//        spannableString.setSpan(new ImageSpan(this, getLabelBitmap(getApplicationContext())),
//                0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textView.setText(spannableString);

        List<RedCondition> redConditions = new ArrayList<>();
        redConditions.add(new RedCondition("开通真爱粉", 1));
        redConditions.add(new RedCondition("立即送礼", 2));
        redConditions.add(new RedCondition("立即分享", 3));
        redConditions.add(new RedCondition("关注主播", 4));
        initChoiceGroup(redConditions);

        textView.setTextSize(18);
        SpannableString string = new SpannableString("道具卡" + " x" + "45");
        string.setSpan(new AbsoluteSizeSpan(dip2px(getApplicationContext(), 15)), 3, 3 + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        string.setSpan(new AbsoluteSizeSpan(dip2px(getApplicationContext(), 19)), 3 + 2, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(string);

        ClipboardManager clipboardManager =
                (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData != null && clipData.getDescription().hasMimeType(
                    ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                String text = clipData.getItemAt(0).getText().toString();
                if (!TextUtils.isEmpty(text) && text.length() > 3) {
                    String head = text.substring(0, 3);
                   Log.i("zhaoxiaopo", text);
                }
            }
        }
    }

    public static int dip2px(Context context, float dipValue) {
        float var2 = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * var2 + 0.5F);
    }

    private void startAnim() {
        AnimatorSet animSet = new AnimatorSet();
        ObjectAnimator translateFront = ObjectAnimator.ofFloat(view2, "translationY", -view2.getBottom() + 200);
        ObjectAnimator translateBack = ObjectAnimator.ofFloat(view1, "translationY", view1.getBottom() - view1.getTop() - 200);
        animSet.play(translateFront).with(translateBack);
        animSet.start();
    }

    private Bitmap getLabelBitmap(Context context) {
        Rect rect = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
        paint.getTextBounds(STICKY_LABEL, 0, 2, rect);

        Bitmap bitmap = Bitmap.createBitmap(100, 70, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);

        //绘制圆角矩形
        paint.reset();
        paint.setAntiAlias(true);
        paint.setShader(new LinearGradient(0, bitmap.getHeight() / 2, bitmap.getWidth(), bitmap.getHeight() / 2, parseColor("#FF2448"), parseColor("#FF7529"), Shader.TileMode.CLAMP));
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        canvas.drawRoundRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), bitmap.getHeight() / 2, bitmap.getHeight() / 2,  paint);

        //绘制文字
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
        canvas.drawText(STICKY_LABEL, 0, bitmap.getHeight() / 2 + rect.height() / 2, paint);
        return bitmap;
    }

    private int parseColor(String color) {
        int result = Color.parseColor("#ffffff");
        if (TextUtils.isEmpty(color)) {
            return result;
        }
        try {
            result = Color.parseColor(color);
        } catch (IllegalArgumentException e) {

        }
        return result;
    }

    private int mChoosedCondition = 0;
    private int mChoiceViewIndex = 0;

    private void initChoiceGroup(List<RedCondition> redConditions) {
        for (int i = 0; i < redConditions.size(); i++) {
            final RedCondition condition = redConditions.get(i);

            FrameLayout linearLayout = new FrameLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            linearLayout.setLayoutParams(layoutParams);

            CheckBox radioButton = new CheckBox(this);
            radioButton.setCompoundDrawablePadding(5);
            radioButton.setTextSize(12);
            radioButton.setTextColor(Color.WHITE);

            Drawable drawable = this.getResources().getDrawable(R.drawable.selector_newly_red_condition_choice);
            radioButton.setButtonDrawable(drawable);
            radioButton.setPadding(20, 0,0,0);
//            radioButton.setCompoundDrawables(drawable, null, null, null);
            radioButton.setText(condition.getConditionName());
            FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams1.gravity = Gravity.CENTER;
            radioButton.setLayoutParams(layoutParams1);
            linearLayout.addView(radioButton);

            final int finalI = i;
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mChoiceViewIndex != finalI) {
                        ((CheckBox) ((ViewGroup) mChoiceLayout.getChildAt(
                                mChoiceViewIndex)).getChildAt(0)).setChecked(false);
                    }
                    if (isChecked) {
                        mChoiceViewIndex = finalI;
                        mChoosedCondition = condition.getConditionType();
                    }
                }
            });
            mChoiceLayout.addView(linearLayout);
        }
    }
}
