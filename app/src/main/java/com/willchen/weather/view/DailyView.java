package com.willchen.weather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.willchen.weather.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.media.CamcorderProfile.get;
import static com.baidu.location.h.b.f;
import static com.baidu.location.h.b.i;
import static com.baidu.location.h.i.m;

public class DailyView extends View {

    private Paint mHighLinePaint;
    private Paint mLowLinePaint;
    private Paint mTextPaint;
    private Context mContext;
    private int screenWidth;
    private int viewHeight;
    private int bottomHeight, topHeight;
    private int columsWidth;
    private List<Integer> highTemp, lowTemp;
    private int offSetY = -15;          //1°的纵向差量
    private int offSetText = -35;        //文字偏移量
    private int offSetColums = -5;        //相邻两天的距离
    private float mAverage = 0;

    public DailyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DailyView(Context context) {
        super(context);
        init();
    }


    public void setHighTemp(List<Integer> highTemp) {
        this.highTemp = highTemp;
        List<Integer> hightList = new ArrayList<>(highTemp);
        Collections.sort(hightList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        topHeight = hightList.get(0);
//        invalidate();
    }

    public void setLowTemp(List<Integer> lowTemp) {
        this.lowTemp = lowTemp;
        List<Integer> lowList = new ArrayList<>(lowTemp);
        Collections.sort(lowList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        bottomHeight = lowList.get(0);
        int result = 0;
        for (int i = 0; i < highTemp.size(); i++) {
            result += highTemp.get(i);
        }
        for (int i = 0; i < lowTemp.size(); i++) {
            result += lowTemp.get(i);
        }
        mAverage = result / (highTemp.size() + lowTemp.size());
//        requestLayout();
        invalidate();
    }


    private void init() {
        mHighLinePaint = new Paint();
        mHighLinePaint.setColor(getResources().getColor(R.color.white));
        mHighLinePaint.setStrokeWidth(4);
        mHighLinePaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(getResources().getColor(R.color.white));
        mTextPaint.setStrokeWidth(4);       //画笔大小
        mTextPaint.setAntiAlias(true);      //无锯齿
        mTextPaint.setTextSize(40);
        mTextPaint.setTextAlign(Paint.Align.CENTER);


        mLowLinePaint = new Paint();
        mLowLinePaint.setColor(getResources().getColor(R.color.white));
        mLowLinePaint.setStrokeWidth(4);
        mLowLinePaint.setAntiAlias(true);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (lowTemp != null) {
            viewHeight = viewHeight / 2 + (topHeight - bottomHeight) * Math.abs(offSetY) ;
        }
        Log.d("height", "onMeasure: " + viewHeight);
        columsWidth = screenWidth / 5;
        setMeasuredDimension(screenWidth, viewHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (highTemp == null) {
            return;
        }
        float startX = columsWidth / 2;     //中间开始画
        float startY = viewHeight;
        startY /= 2;

        float detay;                    //下一个高度

        for (int i = 0; i < highTemp.size() - 1; i++) {
            mHighLinePaint.setAlpha(0xff);
            detay = startY + (highTemp.get(i + 1) - mAverage) * offSetY;
            float y = startY + (highTemp.get(i) - mAverage) * offSetY;
            canvas.drawCircle(startX, y, 8, mHighLinePaint);     //画一个点
            canvas.drawLine(startX, y, startX + columsWidth, detay, mHighLinePaint); //画线
            canvas.drawText(highTemp.get(i) + "°", startX, y + offSetText, mTextPaint);   //写上温度
            startX = startX + columsWidth;
        }

        float hightY = startY + (highTemp.get(highTemp.size() - 1) - mAverage) * offSetY;
        canvas.drawCircle(startX, hightY, 8, mHighLinePaint);
        canvas.drawText(highTemp.get(highTemp.size() - 1) + "°", startX, hightY + offSetText, mTextPaint);

        if (lowTemp == null) {
            return;
        }
        startX = columsWidth / 2;

        int lowSize = lowTemp.size();
        for (int i = 0; i < lowSize - 1; i++) {
            mLowLinePaint.setAlpha(0xff);
            detay = startY + (mAverage - lowTemp.get(i + 1)) * -offSetY;   //等比例不同点的高度
            float y = startY + (mAverage - lowTemp.get(i)) * -offSetY;
            canvas.drawCircle(startX, y, 8, mLowLinePaint);     //画一个点
            canvas.drawLine(startX, y, startX + columsWidth, detay, mLowLinePaint); //画线
            canvas.drawText(lowTemp.get(i) + "°", startX, y - offSetText * 2, mTextPaint);   //写上温度
            startX = startX + columsWidth;
        }

        float endY = startY + (mAverage - lowTemp.get(lowSize - 1)) * -offSetY;
        canvas.drawCircle(startX, endY, 8, mLowLinePaint);
        canvas.drawText(lowTemp.get(lowSize - 1) + "°", startX, endY - offSetText * 2, mTextPaint);

    }
}
