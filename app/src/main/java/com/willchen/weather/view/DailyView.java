package com.willchen.weather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.willchen.weather.R;

import java.util.List;

public class DailyView extends View {

    private Paint mHighLinePaint;
    private Paint mLowLinePaint;
    private Paint mTextPaint;
    private Context mContext;
    private int screenWidth;
    private int viewHeight;
    private int columsWidth;
    private List<Integer> highTemp, lowTemp;
    private int offSetY = -15;          //1°的纵向差量


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
        invalidate();
    }

    public void setLowTemp(List<Integer> lowTemp) {
        this.lowTemp = lowTemp;
        invalidate();
    }


    private void init() {
        mHighLinePaint = new Paint();
        mHighLinePaint.setColor(getResources().getColor(R.color.red_dark));
        mHighLinePaint.setStrokeWidth(4);
        mHighLinePaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(getResources().getColor(R.color.tv_weather_daily));
        mTextPaint.setStrokeWidth(4);       //画笔大小
        mTextPaint.setAntiAlias(true);      //无锯齿
        mTextPaint.setTextSize(50);


        mLowLinePaint = new Paint();
        mLowLinePaint.setColor(getResources().getColor(R.color.bg_weather_main));
        mLowLinePaint.setStrokeWidth(4);
        mLowLinePaint.setAntiAlias(true);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        screenWidth = MeasureSpec.getSize(widthMeasureSpec);

        viewHeight =MeasureSpec.getSize(heightMeasureSpec);


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
        float startY = viewHeight / 3;   //
        float detay;

        int offSetText = -30;        //文字偏移量
        int offSetColums = -5;        //相邻两天的距离


        for (int i = 0; i < highTemp.size() - 1; i++) {
            mHighLinePaint.setAlpha(0xff);

            detay = (highTemp.get(i + 1) - highTemp.get(i)) * offSetY;   //等比例不同点的高度

            canvas.drawCircle(startX, startY, 10, mHighLinePaint);     //画一个点

            canvas.drawLine(startX, startY, startX + columsWidth, startY + detay, mHighLinePaint); //画线
            canvas.drawText(highTemp.get(i) + "°", startX - 33, startY + offSetText + offSetColums, mTextPaint);   //写上温度

            startX = startX + columsWidth;
            startY = startY + detay;
        }
        canvas.drawCircle(startX, startY, 10, mHighLinePaint);
        canvas.drawText(highTemp.get(highTemp.size() - 1) + "°", startX - 33, startY + offSetText, mTextPaint);

        if (lowTemp == null) {
            return;
        }
        startX = columsWidth / 2;
        startY = viewHeight / 3 + (lowTemp.get(0) - highTemp.get(0)) * offSetY;


        for (int i = 0; i < lowTemp.size() - 1; i++) {
            mLowLinePaint.setAlpha(0xff);

            detay = (lowTemp.get(i + 1) - lowTemp.get(i)) * offSetY;   //等比例不同点的高度
            canvas.drawCircle(startX, startY, 10, mLowLinePaint);     //画一个点

            canvas.drawLine(startX, startY, startX + columsWidth, startY + detay, mLowLinePaint); //画线
            canvas.drawText(lowTemp.get(i) + "°", startX - 33, startY + 80, mTextPaint);   //写上温度

            startX = startX + columsWidth;
            startY = startY + detay;
        }
        canvas.drawCircle(startX, startY, 10, mLowLinePaint);
        canvas.drawText(lowTemp.get(lowTemp.size() - 1) + "°", startX - 33, startY + 80, mTextPaint);

    }
}
