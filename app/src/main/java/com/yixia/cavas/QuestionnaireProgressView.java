package com.yixia.cavas;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 答题进度条样式
 *
 * Created by zhaoxiaopo on 2018/1/13.
 */
public class QuestionnaireProgressView extends View {

    /**
     * 样式列表
     */
    public enum QuestionProgressState {
        SElECT, ANSWER, ANSWER_ERROR, DEFAULT
    }

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
     * @see {@link R.styleable#QuestionnaireStyle_background_color}
     */
    private int mBackgroundColor;
    /**
     * 答案选择的背景色，可以在xml中定义
     *
     * @see {@link R.styleable#QuestionnaireStyle_answer_color}
     */
    private int mAnswerColor;
    /**
     * 选择错误的背景色，可以在xml中定义
     *
     * @see {@link R.styleable#QuestionnaireStyle_answer_error_color}
     */
    private int mAnswerErrorColor;
    /**
     * 用户选择答题的颜色
     *
     * @see {@link R.styleable#QuestionnaireStyle_select_color}
     */
    private int mSelectColor;

    /**
     * 自定义字体大小
     *
     * @see {@link R.styleable#QuestionnaireStyle_text_size}
     */
    private float mTextValueSize;

    /**
     * 自定义字体颜色
     *
     * @see {@link R.styleable#QuestionnaireStyle_text_color}
     */
    private int mTextColor;

    private int mTextLeftMargin;
    private int mTextRightMargin;

    /**
     * 答案进度条
     */
    private RectF mProgressRectF;
    /**
     * 默认背景
     */
    private RectF mBackgroundRectF;

    private Paint mProgressPaint;
    private Paint mTextValuePaint;
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

    /**
     * 绘制的文本内容
     */
    private String mTextValue, mTextValue2;
    /**
     * 绘制的字体宽度和高度
     */
    private Rect mTextRectF, mTextRectF2;

    /**
     * 该进度条的状态
     */
    private QuestionProgressState mProgressState = QuestionProgressState.DEFAULT;

    public QuestionnaireProgressView(Context context) {
        super(context);
        initValue(context, null, 0);
    }

    public QuestionnaireProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initValue(context, attrs, 0);
    }

    public QuestionnaireProgressView(Context context, @Nullable AttributeSet attrs,
                                     int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initValue(context, attrs, defStyleAttr);
    }

    private void initValue(Context context, AttributeSet attrs, int defStyleAttr) {
        //初始化属性列表
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.QuestionnaireStyle, defStyleAttr, 0);
        mDefaultWidth = typedArray.getDimensionPixelSize(
                R.styleable.QuestionnaireStyle_default_width, 600);
        mDefaultHeight = typedArray.getDimensionPixelSize(
                R.styleable.QuestionnaireStyle_default_height, 100);
        mAnswerColor = typedArray.getColor(R.styleable.QuestionnaireStyle_answer_color,
                getResources().getColor(R.color.questionnaire_answer_color));
        mAnswerErrorColor = typedArray.getColor(R.styleable.QuestionnaireStyle_answer_error_color,
                getResources().getColor(R.color.questionnaire_answer_error_color));
        mSelectColor = typedArray.getColor(R.styleable.QuestionnaireStyle_select_color,
                getResources().getColor(R.color.questionnaire_select_color));
        mBackgroundColor = typedArray.getColor(R.styleable.QuestionnaireStyle_background_color,
                Color.WHITE);

        mTextColor = typedArray.getColor(R.styleable.QuestionnaireStyle_text_color,
                getResources().getColor(R.color.questionnaire_default_text_color));
        mTextValueSize = typedArray.getDimension(R.styleable.QuestionnaireStyle_text_size, 15);
        mTextLeftMargin = typedArray.getDimensionPixelSize(R.styleable.QuestionnaireStyle_text_left_margin, 0);
        mTextRightMargin = typedArray.getDimensionPixelSize(R.styleable.QuestionnaireStyle_text_right_margin, 0);
        typedArray.recycle();

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextValuePaint = new Paint();
        mTextValuePaint.setAntiAlias(true);
        mTextValuePaint.setColor(mTextColor);
        mTextValuePaint.setTextSize(mTextValueSize);

        mBackgroundRectF = new RectF();
        mProgressRectF = new RectF();
        mTextRectF = new Rect();
        mTextRectF2 = new Rect();

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
        mProgressPath.addRoundRect(mBackgroundRectF, mHeight / 2, mHeight / 2,
                Path.Direction.CW);
        canvas.clipPath(mProgressPath);
        if (QuestionProgressState.ANSWER == mProgressState) {
            canvas.drawColor(mBackgroundColor);
            mProgressPaint.setColor(mAnswerColor);
            canvas.drawRect(mProgressRectF, mProgressPaint);
            if (!TextUtils.isEmpty(mTextValue)) {
                canvas.drawText(mTextValue, mTextLeftMargin, mBackgroundRectF.height() / 2
                        + mTextRectF.height() / 2, mTextValuePaint);
            }
            if (!TextUtils.isEmpty(mTextValue2)) {
                canvas.drawText(mTextValue2, mBackgroundRectF.width() - mTextRightMargin - mTextRectF2.width(),
                        mBackgroundRectF.height() / 2 + mTextRectF2.height() / 2, mTextValuePaint);
            }
        } else if (QuestionProgressState.ANSWER_ERROR == mProgressState) {
            canvas.drawColor(mBackgroundColor);
            mProgressPaint.setColor(mAnswerErrorColor);
            canvas.drawRect(mProgressRectF, mProgressPaint);
            if (!TextUtils.isEmpty(mTextValue)) {
                canvas.drawText(mTextValue, mTextLeftMargin, mBackgroundRectF.height() / 2
                        + mTextRectF.height() / 2, mTextValuePaint);
            }
            if (!TextUtils.isEmpty(mTextValue2)) {
                canvas.drawText(mTextValue2, mBackgroundRectF.width() - mTextRightMargin - mTextRectF2.width(),
                        mBackgroundRectF.height() / 2 + mTextRectF2.height() / 2, mTextValuePaint);
            }
        } else if (QuestionProgressState.SElECT == mProgressState){
            canvas.drawColor(mSelectColor);
            if (!TextUtils.isEmpty(mTextValue)) {
                canvas.drawText(mTextValue, mTextLeftMargin, mBackgroundRectF.height() / 2
                        + mTextRectF.height() / 2, mTextValuePaint);
            }
            if (!TextUtils.isEmpty(mTextValue2)) {
                canvas.drawText(mTextValue2, mBackgroundRectF.width() - mTextRightMargin - mTextRectF2.width(),
                        mBackgroundRectF.height() / 2 + mTextRectF2.height() / 2, mTextValuePaint);
            }
        } else if (QuestionProgressState.DEFAULT == mProgressState){
            canvas.drawColor(mBackgroundColor);
            if (!TextUtils.isEmpty(mTextValue)) {
                canvas.drawText(mTextValue, mTextLeftMargin, mBackgroundRectF.height() / 2
                        + mTextRectF.height() / 2, mTextValuePaint);
            }
            if (!TextUtils.isEmpty(mTextValue2)) {
                canvas.drawText(mTextValue2, mBackgroundRectF.width() - mTextRightMargin - mTextRectF2.width(),
                        mBackgroundRectF.height() / 2 + mTextRectF2.height() / 2, mTextValuePaint);
            }
        }
    }

    /**
     * 此方法需要等待布局加载完成后设置，否则{@link #mBackgroundRectF}的宽高==0
     *
     * @param currentValue 设置进度值，默认选择样式{@link QuestionProgressState#DEFAULT}不需要设置，
     * @param textValue 设置左侧显示文本内容
     * @param textValue2 设置右侧显示文本内容
     * @param progressState 设置进度条样式
     */
    public void setCurrentValue(float currentValue, String textValue, String textValue2, QuestionProgressState progressState) {
        mProgressState = progressState;
        if (!TextUtils.isEmpty(textValue)) {
            mTextValue = textValue;
            mTextValuePaint.getTextBounds(mTextValue, 0, mTextValue.length(), mTextRectF);
        }

        if (!TextUtils.isEmpty(textValue2)) {
            mTextValue2 = textValue2;
            mTextValuePaint.getTextBounds(mTextValue2, 0, mTextValue2.length(), mTextRectF2);
        }

        if (QuestionProgressState.SElECT != mProgressState) {
            mCurrentValue = currentValue;
            mPercent = mCurrentValue / mMaxValue;
            mProgressRectF = new RectF(mBackgroundRectF.left, mBackgroundRectF.top,
                    mBackgroundRectF.width() * mPercent, mBackgroundRectF.bottom);
        }
        invalidate();
    }
}
