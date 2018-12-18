package com.yixia.cavas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Triangle view
 */
public class TriangleView extends LinearLayout {

    private Paint mPaint;
    private Point[] mPoints;
    private Path mPath;

    public TriangleView(Context context) {
        super(context);
        init(context, null);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#ff00ff"));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);

        mPoints = new Point[3];
        mPath = new Path();

        for (int i = 0; i < mPoints.length; i++) {
            mPoints[i] = new Point();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        if (MeasureSpec.EXACTLY == widthSpecMode && MeasureSpec.EXACTLY == heightSpecMode) {
        } else {
            setMeasuredDimension(150, 50);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final Path path = mPath;
        final Paint paint = mPaint;

        int w = getWidth();
        int h = getHeight();

        // setting color
        mPoints[0].x = trimWidthPadding(0);
        mPoints[0].y = trimHeightPadding(0);

        mPoints[1].x = trimWidthPadding(w);
        mPoints[1].y = trimHeightPadding(0);

        mPoints[2].x = trimWidthPadding(w / 2);
        mPoints[2].y = trimHeightPadding(h);

        path.reset();
        path.moveTo(mPoints[0].x, mPoints[0].y);
        path.lineTo(mPoints[1].x, mPoints[1].y);
        path.lineTo(mPoints[2].x, mPoints[2].y);
        path.lineTo(mPoints[0].x, mPoints[0].y);
        path.close();

        canvas.drawPath(path, paint);
    }


    protected final int trimWidthPadding(int value) {
        return trim(getPaddingLeft(), getWidth() - getPaddingRight(), value);
    }

    protected final int trimHeightPadding(int value) {
        return trim(getPaddingTop(), getHeight() - getPaddingBottom(), value);
    }

    final int trim(int min, int max, int value) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }
}