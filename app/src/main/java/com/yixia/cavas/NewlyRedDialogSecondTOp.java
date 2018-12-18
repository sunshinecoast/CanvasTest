package com.yixia.cavas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhaoxiaopo on 2018/7/11.
 */
public class NewlyRedDialogSecondTOp extends View {
    private Paint mPaint = new Paint();
    private RectF mRect = new RectF();
    private RectF mRectClipPath = new RectF();

    private Path mPath = new Path();
    private LinearGradient mLinearGradient;
    private int mColorStart, mColorCenter, mColorEnd;

    private int mDip15;

    public NewlyRedDialogSecondTOp(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mPaint.setAntiAlias(true);
        mColorStart = context.getResources().getColor(R.color.newly_red_dialog_list_top_color_start);
        mColorCenter = context.getResources().getColor(R.color.newly_red_dialog_list_top_color_start1);
        mColorEnd = context.getResources().getColor(R.color.newly_red_dialog_list_top_color_end);

        mDip15 = 30;
    }

    public NewlyRedDialogSecondTOp(Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NewlyRedDialogSecondTOp(Context context, @Nullable AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NewlyRedDialogSecondTOp(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mRect.left = -100;
        mRect.right = getMeasuredWidth() + 100;
        mRect.top = -(getMeasuredHeight() * 3 / 2);
        mRect.bottom = getMeasuredHeight();

        mRectClipPath.left = 0;
        mRectClipPath.right = getMeasuredWidth();
        mRectClipPath.top = 0;
        mRectClipPath.bottom = getMeasuredHeight();

        if (mLinearGradient == null) {
            mLinearGradient = new LinearGradient(0, 0, mRect.width(),
                    mRect.height(), new int[]{mColorStart, mColorCenter, mColorEnd, mColorEnd}, null,
                    Shader.TileMode.CLAMP);
        }
        mPaint.setShader(mLinearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.addRoundRect(mRectClipPath, mDip15, mDip15, Path.Direction.CCW);
        canvas.clipPath(mPath);
        canvas.drawOval(mRect, mPaint);
    }
}
