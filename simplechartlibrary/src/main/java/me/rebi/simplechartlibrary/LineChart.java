package me.rebi.simplechartlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;

import java.util.List;

/**
 * 作者: Warm.
 * 日期: 2016/12/25 21:58.
 * 联系: QQ-865741452.
 * 内容:
 */
public class LineChart extends BaseLineChart {
    private Paint mPaint;
    private Paint pointPaint;
    /**
     * 实心
     * 空心
     */
    private boolean full;
    private int lineColor = Color.BLACK;
    private int linesSize=2;

    /**
     * 圆点的颜色和半径
     */
    private int pointColor=0;
    private int pointSize=0;


    Path path;

    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initV(context, attrs, defStyleAttr);
    }

    private void initV(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineChart);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.LineChart_full) {
                full = typedArray.getBoolean(attr, false);

            } else if (attr == R.styleable.LineChart_linesColor) {
                lineColor = typedArray.getColor(attr, Color.BLACK);

            } else if (attr == R.styleable.LineChart_linesSize) {
                linesSize = typedArray.getDimensionPixelSize(attr, DisplayUtil.dp2px(context, 1));

            } else if (attr == R.styleable.LineChart_pointColor) {
                pointColor = typedArray.getColor(attr, Color.BLACK);

            } else if (attr == R.styleable.LineChart_pointSize) {
                pointSize = typedArray.getDimensionPixelSize(attr, DisplayUtil.dp2px(context, 1));

            }
        }
        typedArray.recycle();

        Log.i("****", String.valueOf(full));

        mPaint = new Paint();
        mPaint.setColor(lineColor);
        mPaint.setAntiAlias(true);

        if (pointColor==0){
            pointColor=lineColor;
        }
        if (pointSize==0){
            pointSize=linesSize*2;
        }
        Log.i("****linesSize", String.valueOf(linesSize));

        Log.i("****pointSize", String.valueOf(pointSize));
        pointPaint=new Paint();
        pointPaint.setColor(pointColor);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setAntiAlias(true);

        path=new Path();

    }


    @Override
    protected void drawBar(Canvas canvas, List<Value> values, float[] v, double proportion) {
        path.moveTo(leftSpace, mHeight - bottomSpace);
        for (int i = 0; i < values.size(); i++) {

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(linesSize);
            mPaint.setColor(lineColor);
            if (i == 0) {
                canvas.drawLine(leftSpace, mHeight - bottomSpace, leftSpace + xSpace * (i + 1), (float) (mHeight - bottomSpace - proportion * v[i]), mPaint);
            } else {
                canvas.drawLine(leftSpace + xSpace * (i), (float) (mHeight - bottomSpace - proportion * v[i - 1]), leftSpace + xSpace * (i + 1), (float) (mHeight - bottomSpace - proportion * v[i]), mPaint);
            }
            path.lineTo(leftSpace + xSpace * (i + 1), (float) (mHeight - bottomSpace - proportion * v[i]));
        }
        path.lineTo(leftSpace + xSpace * (values.size()), mHeight - bottomSpace);
        mPaint.setAlpha(123);
        if(full) {
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, mPaint);
        }

        for (int i = 0; i < values.size(); i++) {

            pointPaint.setColor(values.get(i).getColor());
            canvas.drawCircle(leftSpace + xSpace * (i + 1), (float) (mHeight - bottomSpace - proportion * v[i]), pointSize, pointPaint);
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(String.valueOf(values.get(i).getValue()), leftSpace + xSpace * (i + 1), (float) (mHeight - bottomSpace - proportion * v[i]) - pointSize-DisplayUtil.dp2px(context, 3), textPaint);

        }

    }
}
