package com.jypdev.multiimageeditview.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by JY-park on 16. 6. 10..
 */
public class ImageItem {

    private Bitmap bitmap;
    private Matrix matrix;
    private Paint paint;
    private float width;
    private float height;
    private float x=0;
    private float y=0;

    public ImageItem(Bitmap bitmap) {
        this(bitmap,new Matrix());
    }
    public ImageItem(Bitmap bitmap, float x, float y){

        this.x=x;
        this.y=y;
        float[] value = new float[9];
        Matrix matrix = new Matrix();
        matrix.getValues(value);
        value[2]=x;
        value[5]=y;
        matrix.setValues(value);

        this.bitmap = bitmap;
        this.matrix = matrix;
        this.paint = new Paint();
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();

    }

    public ImageItem(Bitmap bitmap, Matrix matrix) {
       this(bitmap,matrix,new Paint());
    }

    public ImageItem(Bitmap bitmap, Matrix matrix, Paint paint) {
        this.bitmap = bitmap;
        this.matrix = matrix;
        this.paint = paint;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public Paint getPaint() {
        return paint;
    }

}
