package com.example.stepview.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.example.stepview.R;

import java.util.ArrayList;

public class StepView extends View {
    private Paint bgPaint;
    private Paint mCircleInnerPaint;
    private Paint mCircleOutPaint;
    private Paint mFontPaint;
    private ArrayList<String> contents;
    private int mLineHeight;
    private int mRadius;
    private int mOutRadius;
    private int mMarginTop;
    private int mItemWidth;
    private int mStepCurrentIndex;
    private int mFontSize;
    private int mFontColor;
    private int mBgColor;
    private int mLineSelectedColor;
    private int mTextHeight;
    private int mInnerColor;

    public StepView(Context context) {
        super(context);
    }

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (context == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.step_view);
        mLineHeight = typedArray.getDimensionPixelSize(R.styleable.step_view_step_lineHeight, 20);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.step_view_step_radius, 10);
        mOutRadius = mRadius + typedArray.getDimensionPixelSize(R.styleable.step_view_step_out_radius, 8);
        mMarginTop = typedArray.getDimensionPixelSize(R.styleable.step_view_step_marginTop, 30);
        mFontSize = typedArray.getDimensionPixelSize(R.styleable.step_view_step_textSize, 30);
        mFontColor = typedArray.getColor(R.styleable.step_view_step_textColor, Color.BLACK);
        mBgColor = typedArray.getColor(R.styleable.step_view_step_bgColor, Color.GRAY);
        mLineSelectedColor = typedArray.getColor(R.styleable.step_view_step_line_selected_color, Color.RED);
        mInnerColor = typedArray.getColor(R.styleable.step_view_step_inner_circleColor, Color.WHITE);
        init();
    }

    private void init() {
        mCircleOutPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleOutPaint.setStyle(Paint.Style.FILL);
        mCircleOutPaint.setColor(mLineSelectedColor);
        mCircleOutPaint.setStrokeWidth(mLineHeight);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(mBgColor);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setStrokeWidth(mLineHeight);

        mCircleInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleInnerPaint.setColor(mInnerColor);
        mCircleInnerPaint.setStyle(Paint.Style.FILL);

        mFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFontPaint.setColor(mFontColor);
        mFontPaint.setStyle(Paint.Style.FILL);
        mFontPaint.setTextSize(mFontSize);
        mFontPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void setData(ArrayList<String> contents) {
        if (contents == null) {
            this.contents = new ArrayList<>();
        } else {
            this.contents = contents;
        }
    }

    public void setIndex(int index) {
        index = index - 1;
        if (index > contents.size() - 1) {
            mStepCurrentIndex = contents.size() - 1;
        } else if (index < 0) {
            mStepCurrentIndex = 0;
        } else {
            mStepCurrentIndex = index;
        }
        // 重绘
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (contents != null && contents.size() > 0) {
            mItemWidth = (getWidth() - mRadius * 2) / (contents.size() - 1);
            canvas.drawLine(mOutRadius, mOutRadius * 2, getWidth() - mOutRadius * 2, mOutRadius * 2, bgPaint);
            for (int i = 0; i < contents.size(); i++) {
                if (i < mStepCurrentIndex) {
                    mCircleOutPaint.setColor(mLineSelectedColor);
                    canvas.drawLine(mOutRadius + mItemWidth * (i + 1), mOutRadius * 2, i * mItemWidth - mOutRadius, mOutRadius * 2, mCircleOutPaint);
                    canvas.drawCircle(mOutRadius + mItemWidth * i, mOutRadius * 2, mOutRadius, mCircleOutPaint);
                    canvas.drawCircle(mOutRadius + mItemWidth * i, mOutRadius * 2, mRadius, mCircleInnerPaint);
                } else if (i == mStepCurrentIndex) {
                    mCircleOutPaint.setColor(mLineSelectedColor);
                    int circleStartX = mOutRadius + mItemWidth * i;
                    int lineStopX = i * mItemWidth + mItemWidth / 2;
                    if (i == 0) {
                        circleStartX = 2 * mOutRadius;
                    } else if (contents.size() - 1 == i) {
                        circleStartX = mItemWidth * i - mOutRadius;
                        lineStopX = i * mItemWidth;
                    }
                    canvas.drawLine(circleStartX - mItemWidth / 2  - mRadius * 2, mOutRadius * 2, lineStopX, mOutRadius * 2, mCircleOutPaint);
                    canvas.drawCircle(circleStartX, mOutRadius * 2, (float) (mOutRadius * 2), mCircleOutPaint);
                    canvas.drawCircle(circleStartX, mOutRadius * 2, (float) (mRadius * 2), mCircleInnerPaint);
                } else {
                    if (i == contents.size() - 1) {
                        mCircleOutPaint.setColor(mBgColor);
                        canvas.drawCircle(mItemWidth * i, mOutRadius * 2, mOutRadius, mCircleOutPaint);
                        canvas.drawCircle(mItemWidth * i, mOutRadius * 2, mRadius, mCircleInnerPaint);
                    } else {
                        mCircleOutPaint.setColor(mBgColor);
                        canvas.drawCircle(mOutRadius + mItemWidth * i, mOutRadius * 2, mOutRadius, mCircleOutPaint);
                        canvas.drawCircle(mOutRadius + mItemWidth * i, mOutRadius * 2, mRadius, mCircleInnerPaint);
                    }
                }

                //绘制文字
                Rect rect = new Rect();
                //内容
                mFontPaint.getTextBounds(contents.get(i), 0, contents.get(i).length(), rect);
                int textWidth = rect.width();
                mTextHeight = rect.height();
                float startX = 0f;
                if (i == 0) {
                    startX = mRadius + mItemWidth * i - mRadius;
                } else if (i == contents.size() - 1) {
                    startX = (mRadius + mItemWidth * i) - textWidth;
                } else {
                    startX = (mRadius + mItemWidth * i) - textWidth / 2;
                }
                canvas.drawText(contents.get(i), startX
                        , (float) (mOutRadius * 2.5) + (mTextHeight) + (mMarginTop * 2), mFontPaint);
            }
        }
    }

    /**
     * 重新计算view的高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //自定义视图高度 = bar圆高度 + 文字的上间距 + 文本内容高度
        int height = (mOutRadius * 2) + (mMarginTop * 2) + (mFontSize * 2);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }
}
