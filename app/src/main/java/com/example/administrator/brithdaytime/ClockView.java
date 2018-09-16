package com.example.administrator.brithdaytime;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;


/**
 * Created by R on 2018/8/11.
 */

public class ClockView extends View {
    public final int START_CLOCK=1000;
    //文字画笔对象
    private Paint mTextPaint;
    //圆， 指针，刻度画笔
    private Paint mPaint;

    public float mRadius;
    public int mCircleColor;
    public float mCircleWidth;
    public float mTextSize;
    public int mTextColor;
    public int mBigScaleColor;
    public int mMiddleScaleColor;
    public int mSmallScaleColor;
    public int mHourHandColor;
    public int mMinuteHandColor;
    public int mSecondHandColor;
    public float mHourHandWidth;
    public float mMinuteHandWidth;
    public float mSecondHandWidth;
    public int mWidth;
    public int mHeight;
    public ClockView(Context context) {
        this(context,null);
    }
    public ClockView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public ClockView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.ClockView);
        mRadius = typedArray.getDimension(R.styleable.ClockView_mRadius,400);
        mCircleColor = typedArray.getColor(R.styleable.ClockView_mCircleColor, Color.WHITE);
        mCircleWidth = typedArray.getDimension(R.styleable.ClockView_mCircleWidth,20);
        mTextSize = typedArray.getDimension(R.styleable.ClockView_mTextSize,70);
        mTextColor = typedArray.getColor(R.styleable.ClockView_mTextColor,Color.DKGRAY);
        mBigScaleColor = typedArray.getColor(R.styleable.ClockView_mBigSclaleColor,Color.BLACK);
        mMiddleScaleColor = typedArray.getColor(R.styleable.ClockView_mMiddleCaleColor,Color.RED);
        mSmallScaleColor= typedArray.getColor(R.styleable.ClockView_mSmallScaleColor,Color.BLACK);
        mHourHandColor= typedArray.getColor(R.styleable.ClockView_mHourHandColor,Color.BLACK);
        mMinuteHandColor= typedArray.getColor(R.styleable.ClockView_mMinuteHandColor,Color.BLACK);
        mSecondHandColor= typedArray.getColor(R.styleable.ClockView_mSecondHandColor,Color.BLACK);
        mHourHandWidth = typedArray.getDimension(R.styleable.ClockView_mHourHandWidth,20);
        mMinuteHandWidth = typedArray.getDimension(R.styleable.ClockView_mMinuteHandWidth,10);
        mSecondHandWidth = typedArray.getDimension(R.styleable.ClockView_mSecondHandWidth,5);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec),measureSize(heightMeasureSpec));
    }
    private int measureSize(int mMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(mMeasureSpec);
        int size = MeasureSpec.getSize(mMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 400;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return  result;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
        mHeight = getMeasuredHeight() -getPaddingBottom()-getPaddingTop();
//        drawCircle(canvas);
        drawScale(canvas);
        drawPointer(canvas);
        handler.sendEmptyMessageDelayed(START_CLOCK,1000);

    }

//    private void drawCircle(Canvas canvas){
//        mPaint.setStrokeWidth(mCircleWidth);
//        mPaint.setColor(mCircleColor);
//        mPaint.setStyle(Paint.Style.FILL);
//        canvas.drawCircle(mWidth/2,mHeight/2,mRadius,mPaint);
//    }
    //短线和数字
    private void drawScale(Canvas canvas){
        for(int i=0; i<60; i++){
            if(i==0||i==15||i==30||i==45){
                mPaint.setStrokeWidth(6);
                mPaint.setColor(mBigScaleColor);
                canvas.drawLine(mWidth/2,mHeight/2-mWidth/2+mCircleWidth/2, mWidth/2
                        ,mHeight/2-mWidth/2+mCircleWidth/2+60,mPaint);
                String scaleTv = String.valueOf(i==0?12:i/5);
//                mPaint.setTextSize(30);
//                canvas.drawText(scaleTv,mWidth/2-mTextPaint.measureText(scaleTv)/2,mHeight/2-mWidth/2+mCircleWidth/2+95,mPaint);
            }else if(i==5||i==10||i==20||i==25||i==35||i==40||i==50||i==55){
                mPaint.setStrokeWidth(4);//圆环宽度
                mPaint.setColor(mMiddleScaleColor);
                canvas.drawLine(mWidth/2,mHeight/2-mWidth/2+mCircleWidth/2, mWidth/2
                        ,mHeight/2-mWidth/2+mCircleWidth/2+40,mPaint);
//                String scaleTv = String.valueOf(i/5);
//                canvas.drawText(scaleTv,mWidth/2-mTextPaint.measureText(scaleTv)/2,mHeight/2-mWidth/2+mCircleWidth/2+75,mPaint);
            }else {
                mPaint.setStrokeWidth(2);
                mPaint.setColor(mSecondHandColor);

                canvas.drawLine(mWidth/2,mHeight/2-mWidth/2+mCircleWidth/2,mWidth/2
                        ,mHeight/2-mWidth/2+mCircleWidth+30,mPaint);
            }
            canvas.rotate(6,mWidth/2,mHeight/2);
        }
    }
    //时针、分针、秒针、中间的小圆圈
    private void drawPointer(Canvas canvas){
        Calendar mCalendar=Calendar.getInstance();
        //获取当前小时数
        int hours = mCalendar.get(Calendar.HOUR);
        //获取当前分钟数
        int minutes = mCalendar.get(Calendar.MINUTE);
        //获取当前秒数
        int seconds=mCalendar.get(Calendar.SECOND);

        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //绘制时针
        canvas.save();
        mPaint.setColor(mHourHandColor);
        mPaint.setStrokeWidth(mHourHandWidth);
        //这里计算时针需要旋转的角度 实现原理是计算出一共多少分钟除以60计算出真实的小时数（带有小数，为了更加准确计算度数），已知12小时是360度，现在求出了实际小时数比例求出角度
        Float hoursAngle = (hours * 60 + minutes) / 60f / 12f  * 360;
        canvas.rotate(hoursAngle, mWidth / 2, mHeight / 2);
        canvas.drawLine(mWidth / 2, mHeight / 2 - mWidth/2f*0.5f, mWidth / 2, mHeight / 2 +  mWidth/2f*0.15f, mPaint);
        canvas.restore();


        //绘制分针
        canvas.save();
        mPaint.setColor(mMinuteHandColor);
        mPaint.setStrokeWidth(mMinuteHandWidth);
        //这里计算分针需要旋转的角度  60分钟360度，求出实际分钟数所占的度数
        Float minutesAngle = (minutes*60+seconds) / 60f/ 60f * 360;
        canvas.rotate(minutesAngle, mWidth / 2, mHeight / 2);
        canvas.drawLine(mWidth / 2, mHeight / 2 -  mWidth/2f*0.7f, mWidth / 2, mHeight / 2 +  mWidth/2f*0.15f, mPaint);
        canvas.restore();

        //绘制中间的圆圈
        canvas.save();
        mPaint.setColor(mSecondHandColor);
        mPaint.setStrokeWidth(mSecondHandWidth);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth/2,mHeight/2,20,mPaint);
        canvas.restore();


        //绘制秒针
        canvas.save();
        mPaint.setColor(mSecondHandColor);
        mPaint.setStrokeWidth(mSecondHandWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        //这里计算秒针需要旋转的角度  60秒360度，求出实际秒数所占的度数
        Float secondAngle = seconds/60f*360;
        canvas.rotate(secondAngle, mWidth / 2, mHeight / 2);
        canvas.drawLine(mWidth / 2, mHeight / 2 -  mWidth/2f*0.8f, mWidth / 2, mHeight / 2 +  mWidth/2f*0.2f, mPaint);
        canvas.restore();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case START_CLOCK:
                    invalidate();
                    handler.sendEmptyMessageDelayed(START_CLOCK,1000);
                    break;
            }
        }
    };
}
