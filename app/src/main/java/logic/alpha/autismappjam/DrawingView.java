package logic.alpha.autismappjam;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by evan on 4/15/15.
 */
public class DrawingView  extends View {
    private enum Mode {
        draw,
        erase,
        sticker
    };

    private Bitmap sticker;
    private Mode mode;
    private float brushSize, lastBrushSize;

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
        sticker   = null;
        mode = Mode.draw;
        setupDrawing();
    }

    public void setModeDraw() {
        mode = Mode.draw;
    }

    public void setModeErase() {
        mode = Mode.erase;
    }

    public void setModePlaceSticker(Bitmap sticker) {
        mode = Mode.sticker;
        this.sticker = sticker;
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
        } catch(Exception ex) {
            destroyDrawingCache();
            System.err.println("Could not save to " + absolutePathToFile);
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
        switch (mode) {
            case draw:    drawMode(event); break;
            case erase:   eraseMode(event); break;
            case sticker: stickerMode(event); break;
        }

        // Cause the onDraw method to be called
        invalidate();
        return true;
    }

    public void setBrushSize(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void destroy() {
        if(imageBitmap != null) {
            imageBitmap.recycle();
        }
        canvasBitmap.recycle();
    }

    private void drawMode(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX + 1, touchY + 1);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
        }
    }

    private void eraseMode(MotionEvent event) {

    }

    private void stickerMode(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            drawCanvas.drawBitmap(sticker, touchX - sticker.getWidth() / 2, touchY - sticker.getHeight() / 2, null);
        }
    }

    private void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();

        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;

        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setStrokeWidth(brushSize);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

//    private void loadImage(int targetW, int targetH) {
//        imageBitmap = ImageUtils.getImageBitmap(imageFile, targetW, targetH);
//    }

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