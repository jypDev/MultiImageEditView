package com.jypdev.muitlimageeditview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import com.jypdev.muitlimageeditview.model.ImageItem;

import java.util.ArrayList;

/**
 * Created by JY-park on 16. 6. 8..
 */
public class MatrixDrawView extends View {

    // 디버깅 정보
    private static final String TAG = "MatrixDrawView";

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ROTATE = 2;
    private static final int REMOVE = 3;
    private int mode = NONE;

    private boolean isInit = false;
    private Paint paint;

    private ArrayList<ImageItem> arrayList = new ArrayList<>();
    private ImageItem currentItem;
    private ImageItem lastItem;

    //Drag
    private PointF start = new PointF();
    private float pointX = 0;
    private float pointY = 0;

    //Matrix
    private float[] currentValue = new float[9];

    //Rotate
    private float currentAngle;
    private float lastAngle;

    //Detector
    private ScaleGestureDetector gestureDetector;
    private DragDetector dragDetector;
    private RotationGestureDetector rotationDetector;

    public MatrixDrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MatrixDrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatrixDrawView(Context context) {
        this(context, null);
    }

    //public method
    public void setRotateMode(){
        if (lastItem != null) {
            converteBitmap(lastItem);
        }
        mode = ROTATE;
    }

    public void setRemoveMode() {
        mode = REMOVE;
    }

    public void addImageItem(ImageItem item){
        arrayList.add(item);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isInit == false) {
            init();
            isInit = true;
        }
    }


    protected void init() {
        paint = new Paint();
        paint.setAntiAlias(true);  //모서리 부드럽게
        paint.setDither(true); //단말의 색표현력이 떨어질때 낮게 표현
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xFF000000);
        paint.setPathEffect(new DashPathEffect(new float[]{7, 4}, 2));
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(2);

        gestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        dragDetector = new DragDetector();
        rotationDetector = new RotationGestureDetector();
    }

    private void converteBitmap(ImageItem item) {
        Bitmap tempBitmap = Bitmap.createBitmap(item.getBitmap(), 0, 0, item.getBitmap().getWidth(), item.getBitmap().getHeight(), item.getMatrix(), false);
        item.getBitmap().recycle();
        arrayList.remove(item);

        ImageItem imageItem = new ImageItem(tempBitmap,item.getX(),item.getY());
        arrayList.add(imageItem);
        lastItem = imageItem;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (ImageItem item : arrayList) {
            canvas.drawBitmap(item.getBitmap(), item.getMatrix(), item.getPaint());  //image

            if(item.isSelected()) {
                canvas.drawRect(item.getX(), item.getY(), item.getX() + item.getWidth(), item.getY() + item.getHeight(), paint);  //image rect outline
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) { //item select
            if (mode != ROTATE) {
                currentItem = getCurrentItem(x, y);
                mode = DRAG;
                lastItem = currentItem;
            }
        }

        if (mode == DRAG) {
            if (currentItem != null) {
                gestureDetector.onTouchEvent(event);
                dragDetector.onTouchEvent(event);
                matrixTurning(currentItem, currentValue);
                invalidate();
            }
        } else if (mode == ROTATE) {
            if (lastItem != null) {
                rotationDetector.onTouchEvent(event);
                invalidate();
            }else{
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getContext(), "마지막선택 이미지가 없습니다. 다시 선택하고 mode 클릭하세요", Toast.LENGTH_SHORT).show();
                }
            }
        }

        switch(event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP: {

            }
            case MotionEvent.ACTION_POINTER_UP: {
                mode = NONE;
                if(currentItem!=null) {
                    currentItem.setSelected(false);
                    currentItem = null;
                }
                currentValue = null;
            }
            break;
        }


        return true;
    }


    private ImageItem getCurrentItem(int x, int y) {
        ImageItem imageItem = null;
        for (ImageItem item : arrayList) {
            float[] matrixValue = new float[9];
            item.getMatrix().getValues(matrixValue);
            if (matrixValue[2] < x && (matrixValue[2] + item.getWidth()) > x) {
                if (matrixValue[5] < y && (matrixValue[5] + item.getHeight()) > y) {
                    currentValue = matrixValue;
                    imageItem = item;
                }
            }
        }
        if (imageItem != null) {
            imageItem.setSelected(true);
            arrayList.remove(imageItem);

            if (mode == REMOVE) {
                //삭제모드일땐 다시 추가하지 않음.
                imageItem = null;
                invalidate();
            } else {
                //삭제모드가 아닐때 아이템을 다시 추가하여 순서를 맨뒤로 추가함
                arrayList.add(imageItem);
            }
        }
        return imageItem;
    }


    //    /**
//     * 이미지 핏
//     */
//    public void setImagePit(){
//
//        // 매트릭스 값
//        float[] value = new float[9];
//        this.matrix.getValues(value);
//
//        // 뷰 크기
//        int width = this.getWidth();
//        int height = this.getHeight();
//
//        // 이미지 크기
//        Drawable d = this.getDrawable();
//        if (d == null)  return;
//        int imageWidth = d.getIntrinsicWidth();
//        int imageHeight = d.getIntrinsicHeight();
//        int scaleWidth = (int) (imageWidth * value[0]);
//        int scaleHeight = (int) (imageHeight * value[4]);
//
//        // 이미지가 바깥으로 나가지 않도록.
//
//        value[2] = 0;
//        value[5] = 0;
//
//        if (imageWidth > width || imageHeight > height){
//            int target = WIDTH;
//            if (imageWidth < imageHeight) target = HEIGHT;
//
//            if (target == WIDTH) value[0] = value[4] = (float)width / imageWidth;
//            if (target == HEIGHT) value[0] = value[4] = (float)height / imageHeight;
//
//            scaleWidth = (int) (imageWidth * value[0]);
//            scaleHeight = (int) (imageHeight * value[4]);
//
//            if (scaleWidth > width) value[0] = value[4] = (float)width / imageWidth;
//            if (scaleHeight > height) value[0] = value[4] = (float)height / imageHeight;
//        }
//
//        // 그리고 가운데 위치하도록 한다.
//        scaleWidth = (int) (imageWidth * value[0]);
//        scaleHeight = (int) (imageHeight * value[4]);
//        if (scaleWidth < width){
//            value[2] = (float) width / 2 - (float)scaleWidth / 2;
//        }
//        if (scaleHeight < height){
//            value[5] = (float) height / 2 - (float)scaleHeight / 2;
//        }
//
//        matrix.setValues(value);
//
//        setImageMatrix(matrix);
//    }
    private void matrixTurning(ImageItem item, float[] savedValue) {
        // Matrix
        float[] value = new float[9];
        item.getMatrix().getValues(value);

        // View size
        int width = getWidth();
        int height = getHeight();

        // Image size
        int imageWidth = item.getBitmap().getWidth();
        int imageHeight = item.getBitmap().getHeight();
        int scaleWidth = (int) (imageWidth * savedValue[0]);
        int scaleHeight = (int) (imageHeight * savedValue[4]);

        // Do not outside Image
        if (savedValue[2] > width - scaleWidth) savedValue[2] = width - scaleWidth;
        if (savedValue[5] > height - scaleHeight) savedValue[5] = height - scaleHeight;
        if (savedValue[2] < 0) savedValue[2] = 0;
        if (savedValue[5] < 0) savedValue[5] = 0;

        item.setX((int) savedValue[2]);
        item.setY((int) savedValue[5]);
        item.setWidth(scaleWidth);
        item.setHeight(scaleHeight);
        item.getMatrix().setValues(savedValue);
    }



    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = currentValue[0];

            scale *= detector.getScaleFactor();
            if (scale < 0.3f)
                scale = 0.3f;
            if (scale > 5.0f)
                scale = 5.0f;

            currentValue[0] = currentValue[4] = scale;
            return true;
        }
    }

    private class DragDetector {
        public void onTouchEvent(MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {
                    pointX = currentValue[2];
                    pointY = currentValue[5];
                    start.set(event.getX(), event.getY());
                }
                break;
                case MotionEvent.ACTION_MOVE: {
                    currentValue[2] = pointX + (event.getX() - start.x);//x
                    currentValue[5] = pointY + (event.getY() - start.y);//y
                }
                break;
            }
        }
    }

    private class RotationGestureDetector {
        private static final int INVALID_POINTER_ID = -1;
        private float fX, fY, sX, sY;
        private int ptrID1, ptrID2;
        private float mAngle;

        public RotationGestureDetector() {
            ptrID1 = INVALID_POINTER_ID;
            ptrID2 = INVALID_POINTER_ID;
        }

        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    ptrID1 = event.getPointerId(event.getActionIndex());
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    ptrID2 = event.getPointerId(event.getActionIndex());
                    sX = event.getX(event.findPointerIndex(ptrID1));
                    sY = event.getY(event.findPointerIndex(ptrID1));
                    fX = event.getX(event.findPointerIndex(ptrID2));
                    fY = event.getY(event.findPointerIndex(ptrID2));
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {
                        float nfX, nfY, nsX, nsY;
                        nsX = event.getX(event.findPointerIndex(ptrID1));
                        nsY = event.getY(event.findPointerIndex(ptrID1));
                        nfX = event.getX(event.findPointerIndex(ptrID2));
                        nfY = event.getY(event.findPointerIndex(ptrID2));

                        mAngle = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY);
                        currentAngle = mAngle + lastAngle;

                        if (lastItem != null) {
                            Matrix matrix = lastItem.getMatrix();
                            matrix.setRotate(currentAngle, lastItem.getWidth() / 2, lastItem.getHeight() / 2);
                            matrix.postTranslate(lastItem.getX(),lastItem.getY());
                            lastItem.setMatrix(matrix);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    ptrID1 = INVALID_POINTER_ID;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    lastAngle = currentAngle;

                    if(lastItem!=null) {    //to bitmap
                        Bitmap bitmap = getRotateBitmap(lastItem.getBitmap(), lastItem.getMatrix());
                        arrayList.add(new ImageItem(bitmap, lastItem.getX(), lastItem.getY()));
                        arrayList.remove(lastItem);
                        lastItem = null;
                    }

                    ptrID2 = INVALID_POINTER_ID;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    ptrID1 = INVALID_POINTER_ID;
                    ptrID2 = INVALID_POINTER_ID;
                    break;
            }
            return true;
        }

        private Bitmap getRotateBitmap(Bitmap bitmap, Matrix matrix) {
            Bitmap tempBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            bitmap.recycle();
            bitmap = null;
            return tempBitmap;
        }

        private float angleBetweenLines(float fX, float fY, float sX, float sY, float nfX, float nfY, float nsX, float nsY) {
            float angle1 = (float) Math.atan2((fY - sY), (fX - sX));
            float angle2 = (float) Math.atan2((nfY - nsY), (nfX - nsX));

            float angle = ((float) Math.toDegrees(angle1 - angle2)) % 360;
            if (angle < -180.f) angle += 360.0f;
            if (angle > 180.f) angle -= 360.0f;
            return -angle;
        }
    }
}
