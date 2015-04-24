package logic.alpha.autismappjam;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

import logic.alpha.autismappjam.mood.MoodEntry;
import logic.alpha.autismappjam.mood.MoodLogger;
import logic.alpha.autismappjam.stickers.Sticker;
import logic.alpha.autismappjam.stickers.Stickers;
import logic.alpha.autismappjam.util.FileUtils;
import logic.alpha.autismappjam.util.ImageUtils;

public class DrawingActivity extends Activity {

    private DrawingView drawingView;
    private ImageButton currentPaint;
    private MoodEntry moodEntry;
    private float smallBrush, mediumBrush, largeBrush;
    private Stickers stickers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        drawingView = (DrawingView)findViewById(R.id.drawingView);

        setCurrentPaintToFirstPaint();
        loadImageIfAvailable();

        moodEntry = (MoodEntry)getIntent().getSerializableExtra(CameraOrDrawingActivity.NEW_MOOD);

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        stickers = new Stickers(this);
    }

    public void brushClicked(View view) {
        drawingView.setModeDraw();
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Brush size:");
        brushDialog.setContentView(R.layout.brush_chooser);

        ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawingView.setBrushSize(smallBrush);
//                drawingView.setLastBrushSize(smallBrush);
                brushDialog.dismiss();
            }
        });

        ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawingView.setBrushSize(mediumBrush);
//                drawingView.setLastBrushSize(mediumBrush);
                brushDialog.dismiss();
            }
        });

        ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawingView.setBrushSize(largeBrush);
//                drawingView.setLastBrushSize(largeBrush);
                brushDialog.dismiss();
            }
        });
        brushDialog.show();
    }

    public void eraserClicked(View view) {
        drawingView.setModeErase();
    }

    public void stickerClicked(View view) {
        final Dialog stickerDialog = new Dialog(this);
        stickerDialog.setTitle("Pick a sticker:");
        stickerDialog.setContentView(R.layout.sticker_chooser);
        ViewGroup stickerView = (ViewGroup)stickerDialog.findViewById(R.id.stickerView);
        for(Sticker sticker : stickers.getUnlockedStickers()) {
            ImageButton button = new ImageButton(this);
            final Bitmap stickerBm = sticker.load(this, 100, 100);
            button.setImageBitmap(stickerBm);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawingView.setModePlaceSticker(stickerBm);
                    stickerDialog.dismiss();
                }
            });
            stickerView.addView(button);
        }
        stickerDialog.show();
    }

    public void saveClicked(View view) {
        saveImage();
        startShareReminderActivity();
    }

    public void paintClicked(View view){
        drawingView.setModeDraw();
        if(currentPaint != view){
            releaseCurrentPaintButton();
            selectPaintButton((ImageButton) view);
        }
    }

    private Bitmap getSticker() {
        return ImageUtils.getImageBitmap(this, R.drawable.camera, 100, 100);
    }

    private void saveImage() {
        Toast.makeText(this, "Saving", Toast.LENGTH_SHORT).show();

        if(moodEntry.isImageFileAbsolutePathSet()) {
            new File(moodEntry.getImageFileAbsolutePath()).delete();
        }
        moodEntry.createImageFile(FileUtils.getLocalPicturesDir(this));

        drawingView.saveDrawing(this, moodEntry.getImageFileAbsolutePath());
        moodEntry.setImageToInitialized();
        MoodLogger.saveMoodEntry(this, moodEntry);

        drawingView.destroy();
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    private void startShareReminderActivity() {
        Intent intent = new Intent(this, ShareReminderActivity.class);
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