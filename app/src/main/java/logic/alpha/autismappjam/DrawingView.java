package logic.alpha.autismappjam;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.Image;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by evan on 4/15/15.
 */
public class DrawingView  extends View {
    private String imageFile;
    private Bitmap imageBitmap;

    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = 0xFF660000;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        imageFile = null;

        setupDrawing();
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public void setColor(String newColor){
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void saveDrawing(Activity currentActivity, String absolutePathToFile) {
        setDrawingCacheEnabled(true);
        try {
            FileOutputStream out = new FileOutputStream(absolutePathToFile);
            getDrawingCache().compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            CameraFunctions.galleryAddPic(currentActivity, new File(absolutePathToFile));
        } catch(Exception ex) {
            destroyDrawingCache();
            System.err.println("could not save to " + absolutePathToFile);
            ex.printStackTrace();
            throw new CouldNotSave();
        }
        destroyDrawingCache();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);

        if(imageFile == null) {
        } else {
            loadImage(w, h);
            drawCanvas.drawBitmap(imageBitmap, 0, 0, new Paint(Paint.DITHER_FLAG));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: drawPath.moveTo(touchX, touchY);
                                          drawPath.lineTo(touchX, touchY); break;
            case MotionEvent.ACTION_MOVE: drawPath.lineTo(touchX, touchY); break;
            case MotionEvent.ACTION_UP:   drawCanvas.drawPath(drawPath, drawPaint);
                                          drawPath.reset();                break;
            default:                      return false;
        }

        // Cause the onDraw method to be called
        invalidate();
        return true;
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

    private void loadImage(int targetW, int targetH) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        //Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        imageBitmap = BitmapFactory.decodeFile(imageFile, bmOptions);
    }

    public class CouldNotSave extends RuntimeException {}
}