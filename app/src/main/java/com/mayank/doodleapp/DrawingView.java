package com.mayank.doodleapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DrawingView extends View{

    int count = -1;
    ArrayList<Path> pathList;
    ArrayList<Integer> colorList;

    Context mContext;
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    String currColor;
    private Bitmap canvasBitmap;

    private StorageReference mStorageRef;

    private boolean erase=false;

    public void setErase(boolean isErase){


        erase=isErase;
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
    }


    public void redoBrush()
    {

        Log.d("count", "redoBrush: "+count+"pathSize"+pathList.size());

        drawCanvas.drawColor(Color.WHITE);
            if(count<(pathList.size()-1))
            {
                count++;
            }

        for(int i = 0;i<=count;i++)
        {

            drawPaint.setColor(colorList.get(i));
//            setColor(colorList.get(count));
            drawCanvas.drawPath(pathList.get(i), drawPaint);
        }
        Log.d("count", "redoBrush: "+count);
        invalidate();

//        Log.d("brush", "undoBrush: "+pathList.get(0));
    }

    public void undoBrush()
    {

        drawCanvas.drawColor(Color.WHITE);

        for(int i = 0;i<count;i++)
        {

                drawPaint.setColor(colorList.get(i));
            drawCanvas.drawPath(pathList.get(i), drawPaint);
        }
        if(count>=0)
        {
            count--;
        }
        invalidate();
  }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                if(erase) {
                    drawPath.lineTo(touchX, touchY);
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                    drawPath.moveTo(touchX, touchY);
                }
                else{
                drawPath.lineTo(touchX, touchY);}
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                if(!erase)
                {
                    Path path = new Path(drawPath);
                    count++;
                    pathList.add(count, path);
                    colorList.add(count, paintColor);
                }
                drawPath.reset();


                break;
            default:
                return false;
        }

        invalidate();
        return true;
//        return super.onTouchEvent(event);


    }

    private void setupDrawing(){

            drawPath = new Path();
            drawPaint = new Paint();
            drawPaint.setColor(paintColor);
            drawPaint.setAntiAlias(true);
            drawPaint.setStrokeWidth(20);
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setStrokeJoin(Paint.Join.ROUND);
            drawPaint.setStrokeCap(Paint.Cap.ROUND);
            canvasPaint = new Paint(Paint.DITHER_FLAG);

        }

        public DrawingView(Context context, AttributeSet attrs){
            super(context, attrs);
            mContext = context;
            pathList = new ArrayList<>();
            colorList = new ArrayList<Integer>();
            setupDrawing();
        }
    public void setColor(String newColor){
//set color
        invalidate();
//        currColor = newColor;
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

}
