package com.cjt.stickyview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.FloatEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * 作者: 陈嘉桐 on 2016/7/13
 * 邮箱: 445263848@qq.com.
 */
public class StickyView extends View {
    //没有任何状态
    private static int VIEW_ACTION_STATUS = 0;
    //按下时候的状态
    private static int VIEW_ACTIONDOWN_STATUS = 1;
    //移动时候的状态
    private static int VIEW_ACTIOMOVE_STATUS = 2;
    //断开时候的状态
    private static int VIEW_BREAK_STATUS = 3;
    //当前状态
    private int VIEW_STATUS = VIEW_ACTION_STATUS;

    private boolean isAnim = false;

    private final AccelerateInterpolator aInterpolator = new AccelerateInterpolator(1);

    private Paint mPaint, textPaint;
    private Path mPath;

    private int mWidth, mHeight;
    private float centerX, centerY, radius;

    private float moveX, moveY;
    private boolean isMove = false;

    public StickyView(Context context) {
        this(context, null);
    }

    public StickyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(25);
        textPaint.setTextAlign(Paint.Align.CENTER);

        radius = 50;

        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);

        centerX = mWidth / 2;
        centerY = mHeight / 2;
        Log.i("CJT", "Width=" + mWidth + " Height=" + mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //移动时候的状态
        if (VIEW_STATUS == VIEW_ACTIOMOVE_STATUS) {
            Log.i("CJT", "=========拖动状态=========");
            float length_temp = (float) Math.sqrt(((Math.abs(moveX - centerX) * Math.abs(moveX - centerX)) + (Math.abs(moveY - centerY) * Math.abs(moveY - centerY))));
            float radius_temp;
//                if (radius <= length_temp) {
//                    radius_temp = (radius / length_temp) * radius;
//                } else {
//                    radius_temp = radius/2;
//                }
            radius_temp = radius / 2 < (radius / length_temp) * radius ? radius / 2 : (radius / length_temp) * radius;
            canvas.drawCircle(centerX, centerY, radius_temp, mPaint);
            canvas.drawCircle(moveX, moveY, radius, mPaint);
            canvas.drawLine(centerX, centerY, moveX, moveY, mPaint);
            if (radius_temp / radius < 0.15) {
                Log.i("CJT", "半径比=" + radius_temp / radius);
                VIEW_STATUS = VIEW_BREAK_STATUS;
            }
            float x, y;

            y = ((Math.abs(centerX - moveX) / length_temp) * radius) + moveY;
            x = moveX - ((Math.abs(centerY - moveY) / length_temp) * radius);

            float lengthX = Math.abs(moveX - x);
            float lengthY = Math.abs(y - moveY);
            //            if (moveX < centerX && moveY < centerY) {
            //                canvas.drawLine(moveX - lengthX, moveY + lengthY, moveX + lengthX, moveY - lengthY, mPaint);
            //            } else if (moveX > centerX && moveY < centerY) {
            //                canvas.drawLine(moveX - lengthX, moveY - lengthY, moveX + lengthX, moveY + lengthY, mPaint);
            //            } else if (moveX > centerX && moveY > centerY) {
            //                canvas.drawLine(moveX - lengthX, moveY + lengthY, moveX + lengthX, moveY - lengthY, mPaint);
            //            } else if (moveX < centerX && moveY > centerY) {
            //                canvas.drawLine(moveX - lengthX, moveY - lengthY, moveX + lengthX, moveY + lengthY, mPaint);
            //            }


            float sx, sy;
            sy = ((Math.abs(centerX - moveX) / length_temp) * radius_temp) + centerY;
            sx = centerX - ((Math.abs(centerY - moveY) / length_temp) * radius_temp);
            float lengthsX = Math.abs(centerX - sx);
            float lengthsY = Math.abs(sy - centerY);

            if (moveX < centerX && moveY < centerY) {
                canvas.drawLine(moveX - lengthX, moveY + lengthY, moveX + lengthX, moveY - lengthY, mPaint);
                canvas.drawLine(centerX - lengthsX, centerY + lengthsY, centerX + lengthsX, centerY - lengthsY, mPaint);
            } else if (moveX > centerX && moveY < centerY) {
                canvas.drawLine(moveX - lengthX, moveY - lengthY, moveX + lengthX, moveY + lengthY, mPaint);
                canvas.drawLine(centerX - lengthsX, centerY - lengthsY, centerX + lengthsX, centerY + lengthsY, mPaint);
            } else if (moveX > centerX && moveY > centerY) {
                canvas.drawLine(moveX - lengthX, moveY + lengthY, moveX + lengthX, moveY - lengthY, mPaint);
                canvas.drawLine(centerX - lengthsX, centerY + lengthsY, centerX + lengthsX, centerY - lengthsY, mPaint);
            } else if (moveX < centerX && moveY > centerY) {
                canvas.drawLine(moveX - lengthX, moveY - lengthY, moveX + lengthX, moveY + lengthY, mPaint);
                canvas.drawLine(centerX - lengthsX, centerY - lengthsY, centerX + lengthsX, centerY + lengthsY, mPaint);
            }

            if (moveX < centerX && moveY < centerY) {
                //                canvas.drawLine(moveX - lengthX, moveY + lengthY, centerX - lengthsX, centerY + lengthsY, mPaint);
                //                canvas.drawLine(moveX + lengthX, moveY - lengthY, centerX + lengthsX, centerY - lengthsY, mPaint);

                mPath.reset();
                mPath.moveTo(moveX - lengthX, moveY + lengthY);
                mPath.quadTo((moveX + centerX) / 2, (moveY + centerY) / 2, centerX - lengthsX, centerY + lengthsY);
                mPath.lineTo(centerX + lengthsX, centerY - lengthsY);
                mPath.quadTo((moveX + centerX) / 2, (moveY + centerY) / 2, moveX + lengthX, moveY - lengthY);
                mPath.lineTo(moveX - lengthX, moveY + lengthY);
                mPath.close();
                mPath.setFillType(Path.FillType.WINDING);
                //                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawPath(mPath, mPaint);
                //                mPaint.setStyle(Paint.Style.STROKE);


            } else if (moveX > centerX && moveY < centerY) {
                //                canvas.drawLine(moveX - lengthX, moveY - lengthY, centerX - lengthsX, centerY - lengthsY, mPaint);
                //                canvas.drawLine(moveX + lengthX, moveY + lengthY, centerX + lengthsX, centerY + lengthsY, mPaint);

                mPath.reset();
                mPath.moveTo(moveX - lengthX, moveY - lengthY);
                mPath.quadTo((moveX + centerX) / 2, (moveY + centerY) / 2, centerX - lengthsX, centerY - lengthsY);
                mPath.lineTo(centerX + lengthsX, centerY + lengthsY);
                mPath.quadTo((moveX + centerX) / 2, (moveY + centerY) / 2, moveX + lengthX, moveY + lengthY);
                mPath.lineTo(moveX - lengthX, moveY - lengthY);
                canvas.drawPath(mPath, mPaint);

            } else if (moveX > centerX && moveY > centerY) {
                //                canvas.drawLine(moveX - lengthX, moveY + lengthY, centerX - lengthsX, centerY + lengthsY, mPaint);
                //                canvas.drawLine(moveX + lengthX, moveY - lengthY, centerX + lengthsX, centerY - lengthsY, mPaint);

                mPath.reset();
                mPath.moveTo(moveX - lengthX, moveY + lengthY);
                mPath.quadTo((moveX + centerX) / 2, (moveY + centerY) / 2, centerX - lengthsX, centerY + lengthsY);
                mPath.lineTo(centerX + lengthsX, centerY - lengthsY);
                mPath.quadTo((moveX + centerX) / 2, (moveY + centerY) / 2, moveX + lengthX, moveY - lengthY);
                mPath.lineTo(moveX - lengthX, moveY + lengthY);
                canvas.drawPath(mPath, mPaint);
            } else if (moveX < centerX && moveY > centerY) {
                //                canvas.drawLine(moveX - lengthX, moveY - lengthY, centerX - lengthsX, centerY - lengthsY, mPaint);
                //                canvas.drawLine(moveX + lengthX, moveY + lengthY, centerX + lengthsX, centerY + lengthsY, mPaint);
                mPath.reset();
                mPath.moveTo(moveX - lengthX, moveY - lengthY);
                mPath.quadTo((moveX + centerX) / 2, (moveY + centerY) / 2, centerX - lengthsX, centerY - lengthsY);
                mPath.lineTo(centerX + lengthsX, centerY + lengthsY);
                mPath.quadTo((moveX + centerX) / 2, (moveY + centerY) / 2, moveX + lengthX, moveY + lengthY);
                mPath.lineTo(moveX - lengthX, moveY - lengthY);
                canvas.drawPath(mPath, mPaint);
            }

//            canvas.drawText("99+", moveX, moveY, textPaint);
        } else if (VIEW_STATUS == VIEW_BREAK_STATUS) {//断开时候的状态
            Log.i("CJT", "=========断开状态=========");
            canvas.drawCircle(moveX, moveY, radius, mPaint);
        } else if (VIEW_STATUS == VIEW_ACTION_STATUS) {//初始状态
            Log.i("CJT", "=========初始状态=========");
            canvas.drawCircle(centerX, centerY, radius, mPaint);
//            canvas.drawText("99+", centerX, centerY, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isAnim) {
                    moveX = event.getX();
                    moveY = event.getY();
                    if (moveX >= centerX - radius && moveX <= centerX + radius && moveY >= centerY - radius && centerX <= centerY + radius) {
                        this.VIEW_STATUS = VIEW_ACTIONDOWN_STATUS;
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (VIEW_STATUS == VIEW_ACTIONDOWN_STATUS) {
                    this.VIEW_STATUS = VIEW_ACTIOMOVE_STATUS;
                }
//                Log.i("CJT", "X=" + moveX + " Y=" + moveY);
                isMove = true;
                moveX = event.getX();
                moveY = event.getY();
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                moveX = event.getX();
                moveY = event.getY();
                if (this.VIEW_STATUS == VIEW_ACTIOMOVE_STATUS) {
                    if (!isAnim) {
                        isAnim = true;
                        Log.i("CJT", "移动时候松开");
                        startAnimation((int) moveX, (int) moveY);
                    }
                } else if (this.VIEW_STATUS == VIEW_BREAK_STATUS) {
                    this.VIEW_STATUS = VIEW_ACTION_STATUS;
                    isMove = false;
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setPaintStyle(Paint.Style style) {
        mPaint.setStyle(style);
        invalidate();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    private void startAnimation(int temp_moveX, int temp_moveY) {
        Point startPoint = new Point(temp_moveX, temp_moveY);
        Point endPoint = new Point((int) centerX, (int) centerY);
        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                moveX = point.getX();
                moveY = point.getY();
                Log.i("CJT", moveX + "   ====   " + moveY);
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnim = false;
                VIEW_STATUS = VIEW_ACTION_STATUS;
                invalidate();
            }
        });
        anim.setInterpolator(new SpringInterpolator());
        anim.setDuration(1000);
        anim.start();
    }

    public class PointEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
            float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
            Point point = new Point(x, y);
            return point;
        }

    }

    public class Point {

        private float x;

        private float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

    }
}
