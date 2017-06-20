package me.rebi.simplechartlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

import java.util.List;

/**
 * 作者: Warm.
 * 日期: 2016/12/24 10:14.
 * 联系: QQ-865741452.
 * 内容: 折线图,柱状图基类,绘制x,y轴
 */
public abstract class BaseLineChart extends BaseChart {
    private static final String TAG = "BaseLineChart";
    /**
     * 值
     */
    protected List<Value> values;

    /**
     * 数据中最大的值,用来和高度做比例
     */
    protected double maxValue;

    /**
     * 是x轴每个点的间距,也是bar的宽度
     */
    protected float xSpace = 100;


    protected float ySpace = 80;

    /**
     * x,y轴画笔
     */
    private Paint linesPaint;

    /**
     * 轴上点的长度
     */
    private float pointLength = 10;
    /**
     * x,y轴名,轴上文字,和内容上方的文字画笔
     */
    protected Paint textPaint;
    /**
     * 轴的颜色
     */
    protected int axisLinesColor = Color.BLACK;
    /**
     * 轴线的宽度
     */
    protected int axisLinesWidth = 2;
    /**
     * x,y轴字体大小
     */
    private int axisTextSize = 20;

    protected float xLineWidth = 200;

    protected String xName = "x轴";
    protected String yName = "y轴";

    /**
     * 左边间隔
     */
    protected float leftSpace = 50;

    /**
     * 下边间隔
     */
    protected float bottomSpace = 50;

    protected int ySpaceValue;

    private Scroller mScroller;

    /**
     * 整个控件的矩形
     */
    private RectF rectF;


    public BaseLineChart(Context context) {
        super(context);
    }

    public BaseLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);

    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseLineChart);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.BaseLineChart_axisLinesWidth) {
                axisLinesWidth = typedArray.getDimensionPixelSize(attr, DisplayUtil.sp2px(context, 2));

            } else if (attr == R.styleable.BaseLineChart_axisTextSize) {
                axisTextSize = typedArray.getDimensionPixelSize(attr, DisplayUtil.sp2px(context, 10));

            } else if (attr == R.styleable.BaseLineChart_pointLength) {
                pointLength = typedArray.getDimensionPixelSize(attr, DisplayUtil.dp2px(context, 5));

            }
        }
        typedArray.recycle();
        leftSpace = pointLength + axisTextSize * 2;
        bottomSpace = pointLength + axisTextSize + DisplayUtil.dp2px(context, 5);

        linesPaint = new Paint();
        linesPaint.setColor(axisLinesColor);
        linesPaint.setStyle(Paint.Style.STROKE);
        linesPaint.setStrokeWidth(axisLinesWidth);
        linesPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(axisLinesColor);
        textPaint.setTextSize(axisTextSize);
        textPaint.setAntiAlias(true);

        mScroller = new Scroller(context);
        rectF = new RectF();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void drawBody(Canvas canvas, float[] v) {
        drawXLine(canvas);
        drawYLine(canvas);
        drawBar(canvas, values, v, ySpace / ySpaceValue);
    }

    protected abstract void drawBar(Canvas canvas, List<Value> values, float[] v, double proportion);

    @Override
    public void initValues(List<Value> values) {
        super.initValues(values);
        this.values = values;
        maxValue = values.get(0).getValue();
        xLineWidth = xSpace * (values.size() + 2);
        rectF.left = 0;
        rectF.top = 0;
        rectF.right = leftSpace + xLineWidth;
        rectF.bottom = mHeight;

        for (int i = 0; i < values.size(); i++) {
            if (i < values.size() - 1) {
                if (maxValue < values.get(i + 1).getValue()) {
                    maxValue = values.get(i + 1).getValue();
                }
            }
            animation((float) values.get(i).getValue(), i, animationTime);
        }
        ySpaceValue = (int) (maxValue / 6);

    }

    /**
     * 绘制x线
     *
     * @param canvas
     */
    private void drawXLine(Canvas canvas) {
        canvas.drawLine(leftSpace, mHeight - bottomSpace, leftSpace + xLineWidth, mHeight - bottomSpace, linesPaint);
        canvas.drawLine(leftSpace + xLineWidth - 20, mHeight - bottomSpace - 10, leftSpace + xLineWidth, mHeight - bottomSpace, linesPaint);
        canvas.drawLine(leftSpace + xLineWidth - 20, mHeight - bottomSpace + 10, leftSpace + xLineWidth, mHeight - bottomSpace, linesPaint);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(xName, leftSpace + xLineWidth - 20, mHeight - bottomSpace - 10, textPaint);
        for (int i = 0; i < values.size(); i++) {
            canvas.drawLine(leftSpace + xSpace * (i + 1), mHeight - bottomSpace + pointLength, leftSpace + xSpace * (i + 1), mHeight - bottomSpace, linesPaint);
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(values.get(i).getStr(), leftSpace + xSpace * (i + 1), mHeight - bottomSpace + pointLength + axisTextSize, textPaint);
        }
    }

    /**
     * 绘制y线
     *
     * @param canvas
     */
    private void drawYLine(Canvas canvas) {
        canvas.drawLine(leftSpace, mHeight - bottomSpace, leftSpace, titleHeight, linesPaint);
        canvas.drawLine(leftSpace - 10, titleHeight + 20, leftSpace, titleHeight, linesPaint);
        canvas.drawLine(leftSpace + 10, titleHeight + 20, leftSpace, titleHeight, linesPaint);
        textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(yName, leftSpace + 15, titleHeight + axisTextSize, textPaint);
        ySpace = (mHeight - titleHeight - bottomSpace - axisTextSize - DisplayUtil.dp2px(context, 3)) / 7;
        for (int i = 1; i < 7; i++) {
            canvas.drawLine(leftSpace - pointLength, (mHeight - bottomSpace) - ySpace * i, leftSpace, (mHeight - bottomSpace) - ySpace * i, linesPaint);
            textPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf(ySpaceValue * (i)), leftSpace - pointLength, (mHeight - bottomSpace) - ySpace * i + axisTextSize / 2, textPaint);
        }

    }

    float trueDownX;

    float downX, XX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                trueDownX = event.getX();
                downX = trueDownX + getScrollX();

                if (rectF.right > mWidth) {
                    return true;
                }

            case MotionEvent.ACTION_MOVE:
                XX = event.getX() - downX;
                scrollTo(-(int) XX, 0);
                break;
            case MotionEvent.ACTION_UP:

                if ((Math.abs(velocityX) > mMinimumVelocity)) {
                    flingX((int) -velocityX);
                } else if (getScrollX() < 0) {
                    startScrollX(getScrollX(), -getScrollX());
                } else if (getScrollX() > (rectF.right - mWidth)) {
                    startScrollX(getScrollX(), (int) (rectF.right - mWidth) - getScrollX());
                }
                Log.d(TAG, "startX=" + getScrollX() + "velocityX=" + velocityX);
                break;
        }


        return false;
    }

    /**
     * 只控制X轴的Scroll
     * @param x
     * @param dx
     */
    private void startScrollX(int x, int dx) {
        mScroller.startScroll(x, 0, dx, 0, 250);
        invalidate();
    }

    /**
     * 只控制X轴的fling
     */
    private void flingX(int velocityX) {
        mScroller.fling(getScrollX(), 0, velocityX, 0, 0, (int) (rectF.right - mWidth), 0, 0);
        Log.d(TAG, "flingX: startX=" + getScrollX() + "velocityX=" + velocityX + "maxX" + (int) (rectF.right - mWidth));
        invalidate();
    }


    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();

        }

    }
}
