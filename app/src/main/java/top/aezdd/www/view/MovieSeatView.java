package top.aezdd.www.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import top.aezdd.www.ant_movies.R;


/**
 * Created by jianzhou.liu on 2019/1/3.
 */

public class MovieSeatView extends View{

    private final static String TAG = "ljz";
    private Matrix seatMatrix;
    private Matrix rawNumMatrix;
    private Matrix screenIconMatrix;

    private Bitmap seatSelectedIconBitmap;
    private Bitmap seatSelectableIconBitmap;
    private Bitmap seatSoldIconBitmap;

    private Paint mPaint;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;

    /*
    * View @width  @height
    * */
    private float seatWidth;
    private float seatHeight;
    /*
    * isScaling 是否正在缩放
    * */
    private boolean isScaling = false;

    /*
    * isFirstScale 是否手指按在屏幕上首次缩放
    * */
    private boolean isFirstScale = true;

    /*
    * scaleFocusX  缩放中心点 X值
    * scaleFocusY 缩放中心点 Y值
    * */
    private float scaleFocusX;
    private float scaleFocusY;
    /*
    * X， Y 初始缩放值
    * */
    private float defaultScaleX = 1f;
    private float defaultScaleY = 1f;
    /*
    * 座位之间横向和纵向的间距 seatSpaceX ,seatSpaceY
    * */
    private float seatSpaceX;
    private float seatSpaceY;

    private boolean isDrawFirstRaw = false;
    /*
    * seatData 座位布局二维数组
    * */
    private String[][] seatData;
    /*
    * 座位的四种类型：可选，已选，已售，空
    * */
    public final static Integer SEAT_TYPE_SELECTABLE = 1;
    public final static Integer SEAT_TYPE_SELECTED = 2;
    public final static Integer SEAT_TYPE_SOLD = 3;
    public final static Integer SEAT_TYPE_EMPTY = 4;

    /*
    * ChooseSeatListener 初始化回调接口
    * */
    public ChooseSeatListener chooseSeatListener;

    public MovieSeatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MovieSeatView(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawSeat(canvas);
        onDrawRawNum(canvas);


    }
    private void onDrawScreenIcon(Canvas canvas){

    }
    Matrix tempSeatMatrix = new Matrix();
    private void onDrawSeat(Canvas canvas){
        Log.d(TAG,"start draw Seat");
        canvas.save();
        /*绘制座位背景颜色*/
        /*
        mPaint.setColor(0xFFFFFFFF);
        mPaint.setStyle(Paint.Style.FILL);
        //canvas.translate();
        Path seatBackPath = new Path();
        seatBackPath.addRect(new RectF(0f,0f,screenWidth,600),Path.Direction.CCW);
        canvas.drawPath(seatBackPath,mPaint);
        canvas.save();*/
        /*绘制座位*/
        canvas.translate(seatSpaceX + rawNumWidth,seatSpaceY);
        float zoom = getSeatMatrixScaleX();
        Log.d(TAG,"seatData.length = " + seatData.length);
        for(int i = 0; i<seatData.length;i++){
            //绘制顶部的间隔
            float top = i * seatSelectableIconBitmap.getHeight() * defaultScaleY * zoom + i * seatSpaceY * zoom + getSeatMatrixTranslateY();
            float bottom = top + seatSelectableIconBitmap.getHeight() * defaultScaleY * zoom;
            //Log.d(TAG, "top " + top + " bottom " + bottom);
            if(bottom < - seatSpaceY*zoom || top > getHeight()){
                //Log.d(TAG,"bottom < -seatSpaceY * zoom");
                continue;
            }
            for(int j = 0;j<seatData[i].length;j++){
                //绘制左边的间隔
                float left = j * seatSelectableIconBitmap.getWidth() * defaultScaleX *zoom + j * seatSpaceX *zoom + getSeatMatrixTranslateX();
                float right = left + seatSelectableIconBitmap.getWidth() * defaultScaleX * zoom;
                //Log.d(TAG, "left " + left + " right " + right);
                if(right < - seatSpaceX*zoom || left > getWidth()-rawNumWidth){
                    continue;
                }
                tempSeatMatrix.setTranslate(left,top);
                //tempSeatMatrix.postTranslate(getSeatMatrixTranslateX(),getSeatMatrixTranslateY());
                tempSeatMatrix.postScale(defaultScaleX,defaultScaleY,left,top);
                tempSeatMatrix.postScale(getSeatMatrixScaleX(),getSeatMatrixScaleY(),left,top);
                if(getSeatType(seatData[i][j]) == SEAT_TYPE_SELECTABLE) {
                    canvas.drawBitmap(seatSelectableIconBitmap, tempSeatMatrix, null);
                }else if(getSeatType(seatData[i][j]) == SEAT_TYPE_SELECTED){
                    canvas.drawBitmap(seatSelectedIconBitmap, tempSeatMatrix, null);
                }else if(getSeatType(seatData[i][j]) == SEAT_TYPE_SOLD){
                    canvas.drawBitmap(seatSoldIconBitmap,tempSeatMatrix,null);
                }else if(getSeatType(seatData[i][j]) ==  SEAT_TYPE_EMPTY){
                    //canvas.drawBitmap(seatSoldIconBitmap,seatMatrix,null);
                    continue;
                }
            }
        }
        canvas.restore();
    }
    /*
    * rawNumWidth  座位行号条形view的宽度
    * rawNumHeight 座位行号条形view的高度
    * */
    private float rawNumWidth;
    private float rawNumHeight;
    private float rawNumSize;

    private void onDrawRawNum(Canvas canvas){
        canvas.save();
        canvas.translate(seatSpaceX,seatSpaceY);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(rawNumWidth/3*2);
        float zoom = getSeatMatrixScaleX();

        for(int i = 1; i<= seatData.length; i++){
            float top = i * seatSelectableIconBitmap.getHeight() * defaultScaleY * zoom + i * seatSpaceY  * zoom + getSeatMatrixTranslateY();
            rawNumHeight = top;
            float left;
            if(i<10){
                left = rawNumWidth/8;
            }else{
                left = 0;
            }
            canvas.drawText(i + "",left,top-(seatSpaceY  * zoom) - (seatSelectableIconBitmap.getHeight() * defaultScaleY * zoom/4),mPaint);
        }
        canvas.restore();
        canvas.save();

        canvas.translate(seatSpaceX/2,seatSpaceY/2);

        int translateY = (int)getSeatMatrixTranslateY();
        float scaleY = getSeatMatrixScaleY();

        mPaint.setColor(Color.GRAY);
        mPaint.setAlpha(100);
        mPaint.setStyle(Paint.Style.FILL);
        //Path rawNumLine = new Path();
        RectF rectF = new RectF(0,translateY,rawNumWidth,rawNumHeight);
        canvas.drawRoundRect(rectF,rawNumWidth/2,rawNumWidth/2,mPaint);

        canvas.restore();
    }

    /*
    * pointCount 手指按在屏幕的数量
    * */
    private int pointCount;

    private void init(){
        seatSpaceX = dip2Px(5);
        seatSpaceY = dip2Px(10);
        rawNumWidth = dip2Px(20);
        seatMatrix = new Matrix();
        rawNumMatrix = new Matrix();
        screenIconMatrix = new Matrix();
        seatSelectableIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cinema_seat_selectable_small);
        seatSelectedIconBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.cinema_seat_selected_small);
        seatSoldIconBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.cinema_seat_sold_small);
        mPaint = new Paint();
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }
            @Override
            public void onShowPress(MotionEvent e) {

            }
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                float x = e.getX() - rawNumWidth -seatSpaceX;
                float y = e.getY() - seatSpaceY;
                choose:
                for(int i = 0;i<seatData.length;i++){
                    for(int j = 0;j<seatData[i].length;j++){
                        float seatStartX = j*(seatSelectableIconBitmap.getWidth() + seatSpaceX) *getSeatMatrixScaleX() + getSeatMatrixTranslateX();
                        float seatEndX = seatStartX + seatSelectableIconBitmap.getWidth()*getSeatMatrixScaleX();

                        float seatStartY = i * (seatSelectableIconBitmap.getHeight() + seatSpaceY) * getSeatMatrixScaleY() + getSeatMatrixTranslateY();
                        float seatEndY = seatStartY + seatSelectableIconBitmap.getHeight() * getSeatMatrixScaleY();

                        if(x >= seatStartX && x <= seatEndX && y >= seatStartY && y <= seatEndY){
                            Log.d(TAG,"this is a seat icon");
                            if(getSeatType(seatData[i][j])==SEAT_TYPE_SELECTABLE){
                                Log.d(TAG,"seat selectable");
                                if(chooseSeatList.size() == 6){
                                    Toast.makeText(getContext(),"给别人也留几个座位吧！",Toast.LENGTH_SHORT).show();
                                    break choose;
                                }
                                seatData[i][j] = "*";
                                ArrayList<Integer> seatValue = new ArrayList<Integer>();
                                seatValue.add(i);
                                seatValue.add(j);
                                chooseSeatList.add(seatValue);
                                Log.d(TAG,"already choose seat size is " + chooseSeatList.size());
                                invalidate();
                                chooseSeatListener.chooseSeatResult(chooseSeatList);
                                break choose;
                            }

                            if(getSeatType(seatData[i][j])==SEAT_TYPE_SELECTED){
                                Log.d(TAG,"seat selected");
                                for(int t = 0; t<chooseSeatList.size();t++){
                                    if(chooseSeatList.get(t).get(0)==i && chooseSeatList.get(t).get(1) == j){
                                        chooseSeatList.remove(t);
                                        seatData[i][j] = "1";
                                        invalidate();
                                        chooseSeatListener.chooseSeatResult(chooseSeatList);
                                        break ;
                                    }
                                }
                            }
                            Log.d(TAG,"already choose seat size is " + chooseSeatList.size());
                            break choose;
                        }
                    }
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d(TAG,"pointCount " + pointCount);
                if(!isScaling && pointCount == 1){
                    seatMatrix.postTranslate(-distanceX,-distanceY);
                    invalidate();
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                isScaling = true;
                if(isFirstScale){
                    scaleFocusX = detector.getFocusX();
                    scaleFocusY = detector.getFocusY();
                    isFirstScale = false;
                }

                float scaleFactory = detector.getScaleFactor();
                if(getSeatMatrixScaleX() * scaleFactory >3){
                    scaleFactory = 3 / getSeatMatrixScaleX();
                }
                if(getSeatMatrixScaleX() * scaleFactory < 0.5){
                    scaleFactory = 0.5f / getSeatMatrixScaleX();
                }
                Log.d(TAG,"scaleFactory = " + scaleFactory + " focusX = " + scaleFocusX + " focusY = " + scaleFocusY);
                seatMatrix.postScale(scaleFactory,scaleFactory,0,0);
                invalidate();
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                isScaling = false;
                isFirstScale = true;
            }
        });
    }
    //存储选择座位的坐标值
    List<ArrayList<Integer>> chooseSeatList = new ArrayList<ArrayList<Integer>>();

    float downX;
    float downY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        pointCount = event.getPointerCount();
        float focusX = event.getX();
        float focusY = event.getY();

        int action  = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                downX = focusX;
                downY = focusY;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                autoScale();

                if(Math.abs(focusX-downX)>10 && Math.abs(focusY-downY)>10){
                    autoScroll();
                }
                break;
        }

        return true;
    }


    float[] tempValue = new float[9];
    private float getSeatMatrixScaleX(){
        seatMatrix.getValues(tempValue);
        return tempValue[Matrix.MSCALE_X];
    }

    private float getSeatMatrixScaleY(){
        seatMatrix.getValues(tempValue);
        return tempValue[Matrix.MSCALE_Y];
    }

    private float getSeatMatrixTranslateX(){
        seatMatrix.getValues(tempValue);
        return tempValue[Matrix.MTRANS_X];
    }

    private float getSeatMatrixTranslateY(){
        seatMatrix.getValues(tempValue);
        return tempValue[Matrix.MTRANS_Y];
    }

    public void setData(String[][] value){
        seatData = value;
        //根据传入的data计算当前seat 实际大小
        if(value!=null && value.length>0){
            if(value[0].length>0){
                seatWidth = value[0].length * (seatSelectableIconBitmap.getWidth() + seatSpaceX);
                seatHeight = value.length * (seatSelectableIconBitmap.getHeight() + seatSpaceY);
            }
        }

    }

    public float dip2Px(float value){
        Log.d(TAG,"density = " + getResources().getDisplayMetrics().density);
        return getResources().getDisplayMetrics().density * value;
    }

    private int getSeatType(String value){
        switch(value){
            case "1":
                return SEAT_TYPE_SELECTABLE;
            case "0":
                return SEAT_TYPE_SOLD;
            case " ":
                return SEAT_TYPE_EMPTY;
            case "*":
                return SEAT_TYPE_SELECTED;
        }
        return SEAT_TYPE_EMPTY;
    }
    /**
     * 自动回弹
     * 整个大小不超过控件大小的时候:
     * 往左边滑动,自动回弹到行号右边
     * 往右边滑动,自动回弹到右边
     * 往上,下滑动,自动回弹到顶部
     * <p>
     * 整个大小超过控件大小的时候:
     * 往左侧滑动,回弹到最右边,往右侧滑回弹到最左边
     * 往上滑动,回弹到底部,往下滑动回弹到顶部
     */
    private void autoScroll() {
        float currentSeatWidth = seatWidth * getSeatMatrixScaleX();
        float currentSeatHeight = seatHeight * getSeatMatrixScaleY();
        float moveYLength = 0;
        float moveXLength = 0;

        //处理左右滑动的情况
        if (currentSeatWidth < getWidth()-rawNumWidth) {
            moveXLength = -getSeatMatrixTranslateX() + seatSpaceX;
        } else {
            if (getSeatMatrixTranslateX() < 0 && getSeatMatrixTranslateX() + currentSeatWidth > getWidth()) {

            } else {
                //往左侧滑动
                if (getSeatMatrixTranslateX() + currentSeatWidth < getWidth()) {
                    moveXLength = getWidth() - (getSeatMatrixTranslateX() + currentSeatWidth) -rawNumWidth;
                } else {
                    //右侧滑动
                    moveXLength = -getSeatMatrixTranslateX();
                }
            }
        }

        float startYPosition = getSeatMatrixTranslateY() + seatSpaceY * getSeatMatrixScaleY() ;

        //处理上下滑动
        if (currentSeatHeight < getHeight()) {
                moveYLength = -getSeatMatrixTranslateY();
        } else {
            if (getSeatMatrixTranslateY() < 0 && getSeatMatrixTranslateY() + currentSeatHeight > getHeight()) {

            } else {
                //往上滑动
                if (getSeatMatrixTranslateY() + currentSeatHeight < getHeight()) {
                    moveYLength = getHeight() - (getSeatMatrixTranslateY() + currentSeatHeight);
                } else {
                    //往下滑动
                    moveYLength = - getSeatMatrixTranslateY();
                }
            }
        }

        Point start = new Point();
        start.x = (int) getSeatMatrixTranslateX();
        start.y = (int) getSeatMatrixTranslateY();

        Point end = new Point();
        end.x = (int) (start.x + moveXLength);
        end.y = (int) (start.y + moveYLength);

        scrollAnimator(start, end);

    }
    private void move(Point p) {
        float x = p.x - getSeatMatrixTranslateX();
        float y = p.y - getSeatMatrixTranslateY();
        seatMatrix.postTranslate(x, y);
        invalidate();
    }
    /*
    * 自动回弹（确保座位不能太小或太大）
    * */
    private void autoScale(){
        float scale = getSeatMatrixScaleX();
        if(scale < 0.8f){
            scaleAnimator(scale,1f);
        }else if(scale > 2.5f){
            scaleAnimator(scale,2.0f);
        }
    }

    private void scrollAnimator(Point startScroll,Point endScroll){
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new MoveEvaluator(), startScroll, endScroll);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point p = (Point) animation.getAnimatedValue();
                move(p);
            }
        });
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }

    class MoveEvaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            int x = (int) (startPoint.x + fraction * (endPoint.x - startPoint.x));
            int y = (int) (startPoint.y + fraction * (endPoint.y - startPoint.y));
            return new Point(x, y);
        }
    }

    private void scaleAnimator(float startScale, float endScale){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startScale,endScale);
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scaleValue = (Float)animation.getAnimatedValue();
                Log.d(TAG,"scaleValue :" + scaleValue);
                float scale = scaleValue/getSeatMatrixScaleX();
                seatMatrix.postScale(scale,scale,0,0);
                invalidate();
            }
        });
        valueAnimator.start();

    }

    public interface ChooseSeatListener{
        void chooseSeatResult(List<ArrayList<Integer>> chooseList);
    }

    public void setChooseSeatListener(ChooseSeatListener chooseSeatListener){
        this.chooseSeatListener = chooseSeatListener;
    }
}
