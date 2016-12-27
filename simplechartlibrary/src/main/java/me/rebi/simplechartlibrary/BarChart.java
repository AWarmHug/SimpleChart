package me.rebi.simplechartlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import java.util.List;

/**
 * 作者: Warm.
 * 日期: 2016/12/24 12:10.
 * 联系: QQ-865741452.
 * 内容:
 */
public class BarChart extends BaseLineChart {

    /**
     * 画bar的笔
     */
    private Paint mPaint;


    public BarChart(Context context) {
        this(context,null);
    }

    public BarChart(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initV(context, attrs, defStyleAttr);
    }

    private void initV(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void drawBar(Canvas canvas, List<Value> values, float[] v, double proportion) {
//       double proportion= ySpace/ ySpaceValue;
        for (int i=0;i<values.size();i++){
//            double proportion = v[i] / maxValue;
//            double proportion=linesSpaceValue/values.get(i).getValue();
            RectF rectF = new RectF();
            rectF.left=leftSpace+ xSpace /2+ xSpace *i;
            rectF.right=rectF.left+ xSpace;
            rectF.top= (float) (mHeight-bottomSpace-proportion*v[i]);
            rectF.bottom=mHeight-bottomSpace;
            mPaint.setColor(values.get(i).getColor());
            canvas.drawRect(rectF,mPaint);
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(String.valueOf(values.get(i).getValue()),rectF.left+ xSpace /2,rectF.top-DisplayUtil.dp2px(context,3), textPaint);
        }
    }
}
