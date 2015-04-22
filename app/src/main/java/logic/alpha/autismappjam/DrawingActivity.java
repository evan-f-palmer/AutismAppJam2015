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
import android.widget.Toast;

import java.io.File;

import logic.alpha.autismappjam.R;

public class DrawingActivity extends Activity {

    private DrawingView drawingView;
    private ImageButton currentPaint;
    private MoodEntry moodEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        drawingView = (DrawingView)findViewById(R.id.drawingView);

        setCurrentPaintToFirstPaint();
        loadImageIfAvailable();

        moodEntry = (MoodEntry)getIntent().getSerializableExtra(CameraOrDrawingActivity.NEW_MOOD);
    }

    public void brushClicked(View view) {

    }

    public void eraserClicked(View view) {

    }

    public void saveClicked(View view) {
        saveImage();
        startMainMenuActivity();
    }

    public void paintClicked(View view){
        if(currentPaint != view){
            releaseCurrentPaintButton();
            selectPaintButton((ImageButton) view);
        }
    }

    private void saveImage() {
        Toast.makeText(this, "Saving", Toast.LENGTH_SHORT).show();

        if(!moodEntry.isImageFileAbsolutePathSet()) {
            moodEntry.createImageFile(FileUtils.getGlobalPicturesDir());
        }

        drawingView.saveDrawing(this, moodEntry.getImageFileAbsolutePath());
        moodEntry.setImageToInitialized();
        MoodLogger.saveMoodEntry(this, moodEntry);

        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    private void startMainMenuActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
            drawingView.setImageFile(imageFile);
        }
    }
}