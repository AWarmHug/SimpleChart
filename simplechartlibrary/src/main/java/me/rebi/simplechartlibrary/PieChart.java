package me.rebi.simplechartlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import java.util.List;

/**
 * 作者: Warm.
 * 日期: 2016/12/23 13:20.
 * 联系: QQ-865741452.
 * 内容:饼状图
 */
public class PieChart extends BaseChart {

    /**
     * 值
     */
    protected List<Value> values;


    private float[]  angle;

    private Paint inCirclePaint;

    private Paint halfTranPaint;

    /**
     * 画扇形
     */
    private Paint arcPaint;
    /**
     * 默认开始角度未-90
     */
    private int startAngle = -90;

    /**
     * 1:逆时针
     * 0:顺时针
     */
    private int type = 1;




    private float contentSize = 20;
    private int contentColor = Color.argb(255, 81, 81, 81);


    public PieChart(Context context) {
        this(context, null, 0);
    }

    public PieChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieChart);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.PieChart_contentSize) {
                contentSize = typedArray.getDimensionPixelOffset(attr, DisplayUtil.sp2px(context, 14));

            } else if (attr == R.styleable.PieChart_contentColor) {
                contentColor = typedArray.getColor(attr, Color.argb(255, 81, 81, 81));

            } else if (attr == R.styleable.PieChart_startAngle) {
                startAngle = typedArray.getInt(attr, 0);

            } else if (attr == R.styleable.PieChart_type) {
                type = typedArray.getInt(attr, 0);

            }
        }
        typedArray.recycle();

        Log.i("***", String.valueOf(contentSize));


        /**
         * 内圆绘制笔,颜色写死 为白色
         */
        inCirclePaint = new Paint();
        inCirclePaint.setAntiAlias(true);
        inCirclePaint.setStyle(Paint.Style.FILL);
        inCirclePaint.setColor(Color.WHITE);

        halfTranPaint = new Paint();
        halfTranPaint.setAntiAlias(true);
        halfTranPaint.setStyle(Paint.Style.FILL);
        halfTranPaint.setColor(Color.argb(123, 0, 0, 0));

        arcPaint = new Paint();
    }




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void drawBody(Canvas canvas, float[] v) {
        drawPie(canvas,v);
    }


    private float arcRadius;

    private void drawPie(Canvas canvas, float[] v) {
        arcRadius = Math.min(mWidth / 4, (mHeight - titleHeight) / 2);

        RectF rectF = new RectF();
        rectF.left = (mWidth / 4) - arcRadius;
        rectF.right = rectF.left + arcRadius * 2;
        rectF.top = (mHeight + titleHeight) / 2 - arcRadius;
        rectF.bottom = rectF.top + arcRadius * 2;

        float start = 0;
        for (int i = 0; i < values.size(); i++) {

            arcPaint.setColor(values.get(i).getColor());
            arcPaint.setAntiAlias(true);
            arcPaint.setStyle(Paint.Style.FILL);
            if (i == 0) {

                start = 0 + startAngle;
            } else {

                start += angle[i - 1];

            }
            canvas.drawArc(rectF, start, v[i], true, arcPaint);
            canvas.drawCircle(mWidth / 2 + DisplayUtil.dp2px(context, 30), titleHeight + ((mHeight - titleHeight) / values.size() * (i + 1)) - contentSize / 2 + DisplayUtil.dp2px(context, 2) - (mHeight - titleHeight) / values.size() / 2, contentSize / 2, arcPaint);
            arcPaint.setColor(contentColor);
            arcPaint.setTextSize(contentSize);
            canvas.drawText(values.get(i).getStr(), mWidth / 2 + DisplayUtil.dp2px(context, 60), titleHeight + ((mHeight - titleHeight) / values.size() * (i + 1)) - (mHeight - titleHeight) / values.size() / 2, arcPaint);
        }
        canvas.drawCircle(mWidth / 4, (mHeight + titleHeight) / 2, arcRadius / 3 + arcRadius / 8, halfTranPaint);
        canvas.drawCircle(mWidth / 4, (mHeight + titleHeight) / 2, arcRadius / 3, inCirclePaint);
    }

    @Override
    public void initValues(List<Value> values) {
        super.initValues(values);
        this.values=values;
        angle = new float[values.size()];
        double total = 0;
        for (int i = 0; i < values.size(); i++) {
            total += values.get(i).getValue();
        }
        for (int i = 0; i < values.size(); i++) {
            if (type == 1) {
                angle[i] = -(float) (values.get(i).getValue() / total * 360f);
            } else {
                angle[i] = (float) (values.get(i).getValue() / total * 360f);
            }
            animation(angle[i], i,animationTime);
        }
    }

    //    @Override
//    public void setValues(List<Value> values) {
//        this.values = values;
//        angle = new float[values.size()];
//        double total = 0;
//        for (int i = 0; i < values.size(); i++) {
//            total += values.get(i).getValue();
//        }
//        for (int i = 0; i < values.size(); i++) {
//            if (type == 1) {
//                angle[i] = -(float) (values.get(i).getValue() / total * 360f);
//            } else {
//                angle[i] = (float) (values.get(i).getValue() / total * 360f);
//            }
//            animation(angle[i], i,animationTime);
//        }
//    }



    public float[] getAngle() {
        return angle;
    }

    public void setAngle(float[] angle) {
        this.angle = angle;
        chartInvalidate(false);
    }

    public Paint getArcPaint() {
        return arcPaint;
    }

    public void setArcPaint(Paint arcPaint) {
        this.arcPaint = arcPaint;
        chartInvalidate(false);

    }

    public float getArcRadius() {
        return arcRadius;
    }

    public void setArcRadius(float arcRadius) {
        this.arcRadius = arcRadius;
        chartInvalidate(false);

    }

    public int getContentColor() {
        return contentColor;
    }

    public void setContentColor(int contentColor) {
        this.contentColor = contentColor;

    }

    public float getContentSize() {
        return contentSize;
    }

    public void setContentSize(float contentSize) {
        this.contentSize = contentSize;

    }

    public Paint getHalfTranPaint() {
        return halfTranPaint;
    }

    public void setHalfTranPaint(Paint halfTranPaint) {
        this.halfTranPaint = halfTranPaint;

    }

    public Paint getInCirclePaint() {
        return inCirclePaint;
    }

    public void setInCirclePaint(Paint inCirclePaint) {
        this.inCirclePaint = inCirclePaint;

    }

    public int getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAnimationTime() {
        return animationTime;
    }

    public void setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
    }




}
