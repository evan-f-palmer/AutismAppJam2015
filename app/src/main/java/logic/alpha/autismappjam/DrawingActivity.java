package logic.alpha.autismappjam;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.File;

import logic.alpha.autismappjam.R;

public class DrawingActivity extends Activity {

    private DrawingView drawingView;
    private ImageButton currentPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        drawingView = (DrawingView)findViewById(R.id.drawingView);

        setCurrentPaintToFirstPaint();
        loadImageIfAvailable();
    }

    public void paintClicked(View view){
        if(currentPaint != view){
            releaseCurrentPaintButton();
            selectPaintButton((ImageButton) view);
        }
    }

    private void setCurrentPaintToFirstPaint() {
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        selectPaintButton((ImageButton)paintLayout.getChildAt(0));
    }

    private void releaseCurrentPaintButton() {
        currentPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
    }

    private void selectPaintButton(ImageButton paintButton) {
        currentPaint = paintButton;
        currentPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        String color = currentPaint.getTag().toString();
        drawingView.setColor(color);
    }

    private void loadImageIfAvailable() {
        Intent intent = getIntent();
        String imageFile = intent.getStringExtra(CameraOrDrawingActivity.FILE_MESSAGE);

        if(imageFile != null) {
            System.out.println("Image file " + imageFile);
            drawingView.setImageFile(imageFile);
        }
    }
}