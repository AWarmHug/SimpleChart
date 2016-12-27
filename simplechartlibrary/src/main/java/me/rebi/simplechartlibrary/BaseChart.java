package me.rebi.simplechartlibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * 作者: Warm.
 * 日期: 2016/12/23 14:42.
 * 联系: QQ-865741452.
 * 内容:基类,测量,绘制标题,动画设置
 */
public abstract class BaseChart extends View {

    protected Context context;

    /**
     * 控件宽高
     */
    protected int viewWidth,viewHeight;


    /**
     * title
     */
    protected String title;
    protected int titleColor = Color.BLACK;
    protected float titleSize = 40;
    protected Paint titlePaint;

    private float[] v;

    /**
     * 动画执行时间
     */
    protected int animationTime = 1000;


    /**
     * 真实可以绘制的宽高,除去padding
     */
    protected int mWidth,mHeight;

    protected float titleHeight;


    public BaseChart(Context context) {
        super(context);
    }

    public BaseChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseChart);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.BaseChart_animationTime) {
                animationTime = typedArray.getInt(attr, 1000);

            } else if (attr == R.styleable.BaseChart_titleName) {
                title = typedArray.getString(attr);

            } else if (attr == R.styleable.BaseChart_titleSize) {
                titleSize = typedArray.getDimensionPixelSize(attr, DisplayUtil.sp2px(context, 16));

            } else if (attr == R.styleable.BaseChart_titleColor) {
                titleColor = typedArray.getColor(attr, DisplayUtil.sp2px(context, Color.BLACK));

            }
        }
        typedArray.recycle();

        /**
         * 标题绘制笔
         */
        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setColor(titleColor);
        titlePaint.setTextSize(titleSize);
        titlePaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(measureWidthSpec(widthMeasureSpec), measureHeightSpec(heightMeasureSpec));

    }

    private int measureWidthSpec(int spec) {
        int result = DisplayUtil.dp2px(context, 300);
        int mode = MeasureSpec.getMode(spec);
        int size = MeasureSpec.getSize(spec);
        switch (mode) {
            case MeasureSpec.AT_MOST:
                result = Math.min(result, size);
                break;
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        return result;
    }

    private int measureHeightSpec(int spec) {
        int result = DisplayUtil.dp2px(context, 200);
        int mode = MeasureSpec.getMode(spec);
        int size = MeasureSpec.getSize(spec);
        switch (mode) {
            case MeasureSpec.AT_MOST:
                result = Math.min(result, size);
                break;
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth=w;
        viewHeight=h;
        mWidth=viewWidth-getPaddingLeft()-getPaddingRight();
        mHeight=viewHeight-getPaddingTop()-getPaddingBottom();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (title != null) {
            titleHeight=titleSize+DisplayUtil.dp2px(context,10);
            drawTitle(canvas);
        } else {
            titleSize = 0;
        }
        drawBody(canvas,v);
    }

    protected void drawTitle(Canvas canvas) {
        canvas.drawText(title, mWidth / 2, titleSize, titlePaint);

    }

    public abstract void drawBody(Canvas canvas, float[] v);

    public void initValues(List<Value> values){
        if (values!=null) {
            v = new float[values.size()];
        }else {

        }
    }

    /**
     * 属性动画
     * @param value    传入的值
     * @param position 对应的位置
     */
    protected void animation(float value, final int position,int animationTime) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,value);
        valueAnimator.setDuration(animationTime);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v[position] = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }




    public void chartInvalidate(boolean post){
        if (post){
            postInvalidate();
        }else {
            invalidate();
        }
    }


}
