package com.yixia.cavas;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by zhaoxiaopo on 2018/1/13.
 */
public class QuestionnaireProgress extends View {

    /**
     * view的宽高，xml必须设置的值也可以不用设置使用默认值，
     */
    private int mWidth, mHeight;

    /**
     * 默认宽高
     */
    private int mDefaultWidth, mDefaultHeight;

    /**
     * 默认背景色，可以在xml中定义
     *
     * @see {@link R.styleable#QuestionnaireStyle_default_color}
     */
    private int mBackgroundColor;
    /**
     * 答案选择的背景色，可以在xml中定义
     *
     * @see {@link R.styleable#QuestionnaireStyle_answer_color}
     */
    private int mAnswerColor;

    /**
     * 答案进度条
     */
    private RectF mProgressRectF;
    /**
     * 默认背景
     */
    private RectF mBackgroundRectF;

    private Paint mProgressPaint;
    private Path mProgressPath;

    /**
     * 进度最大值
     */
    private int mMaxValue = 100;
    /**
     * 当前进度值
     */
    private float mCurrentValue;
    /**
     * 进度百分比
     */
    private float mPercent;

    public QuestionnaireProgress(Context context) {
        super(context);
        initValue(context, null, 0);
    }

    public QuestionnaireProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initValue(context, attrs, 0);
    }

    public QuestionnaireProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initValue(context, attrs, defStyleAttr);
    }

    private void initValue(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuestionnaireStyle, defStyleAttr, 0);
        mDefaultWidth = typedArray.getDimensionPixelSize(R.styleable.QuestionnaireStyle_default_width, 600);
        mDefaultHeight = typedArray.getDimensionPixelSize(R.styleable.QuestionnaireStyle_default_height, 100);
        mAnswerColor = typedArray.getColor(R.styleable.QuestionnaireStyle_answer_color, getResources().getColor(R.color.colorPrimary));
        mBackgroundColor = typedArray.getColor(R.styleable.QuestionnaireStyle_default_color, getResources().getColor(R.color.colorAccent));
        typedArray.recycle();

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setColor(mAnswerColor);

        mBackgroundRectF = new RectF();
        mProgressRectF = new RectF();

        mProgressPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        if (MeasureSpec.EXACTLY == widthSpecMode && MeasureSpec.EXACTLY == heightSpecMode) {
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
        } else {
            mWidth = mDefaultWidth;
            mHeight = mDefaultHeight;
            setMeasuredDimension(mDefaultWidth, mDefaultHeight);
        }

        mBackgroundRectF.left = getPaddingLeft();
        mBackgroundRectF.top = getPaddingTop();
        mBackgroundRectF.right = mWidth - getPaddingRight();
        mBackgroundRectF.bottom = mHeight - getPaddingBottom();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //要想保证是圆角，只要保证圆心是高的1／2就可以了
        //先使用clip函数限定画布区域，然后处理进度
        mProgressPath.addRoundRect(mBackgroundRectF, mHeight / 2, mHeight / 2, Path.Direction.CW);
        canvas.clipPath(mProgressPath);
        canvas.drawColor(mBackgroundColor);
//        canvas.drawRoundRect(mBackgroundRectF, mHeight / 2, mHeight / 2, mBgProgresspaint);
        canvas.drawRect(mProgressRectF, mProgressPaint);
    }

    public void setCurrentValue(float currentValue) {
        mCurrentValue = currentValue;
        mPercent = mCurrentValue / mMaxValue;
        mProgressRectF = new RectF(mBackgroundRectF.left,
                mBackgroundRectF.top, mBackgroundRectF.width() * mPercent, mBackgroundRectF.bottom);
        invalidate();
    }
}
