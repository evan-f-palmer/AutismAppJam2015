package logic.alpha.autismappjam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.File;

import logic.alpha.autismappjam.mood.MoodEntry;
import logic.alpha.autismappjam.util.CameraUtils;
import logic.alpha.autismappjam.util.FileUtils;

public class CameraOrDrawingActivity extends Activity {
    public final static String FILE_MESSAGE = "file_message";
    public final static String NEW_MOOD = MoodSelectActivity.NEW_MOOD;

    private File imageFile = null;
    private MoodEntry moodEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_or_drawing);

        initMoodEntry();
        configureCameraButton();
    }

    public void drawFromScratchButtonClicked(View view) {
        startDrawingApp(null);
    }

    public void cameraButtonClicked(View view) {
        imageFile = moodEntry.createImageFile(FileUtils.getGlobalDocsDir());
        CameraUtils.dispatchTakePictureIntent(CameraOrDrawingActivity.this, imageFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraUtils.REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                moodEntry.setImageToInitialized();
                startDrawingApp(imageFile.getPath());
            }
        }
    }

    private void initMoodEntry() {
        moodEntry = (MoodEntry)getIntent().getSerializableExtra(MoodSelectActivity.NEW_MOOD);
        if(moodEntry == null) {
            moodEntry = new MoodEntry();
        }
    }

    private void startDrawingApp(String imageToOpen) {
        Intent intent = new Intent(CameraOrDrawingActivity.this, DrawingActivity.class);
        intent.putExtra(NEW_MOOD, moodEntry);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(FILE_MESSAGE, imageToOpen);
        startActivity(intent);
    }

    private void configureCameraButton() {
        if (!CameraUtils.isCameraHardwareAvailable(this)) {
            final ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraButton);
            removeCameraButton(cameraButton);
        }
    }

    private void removeCameraButton(ImageButton cameraButton) {
        System.err.println("Error: No camera Available");
        ViewGroup layout = (ViewGroup) cameraButton.getParent();
        if(layout != null) {
            layout.removeView(cameraButton);
        }
    }
}