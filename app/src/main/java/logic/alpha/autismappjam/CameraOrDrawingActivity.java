package logic.alpha.autismappjam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

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
        imageFile = moodEntry.createImageFile(FileUtils.getGlobalPicturesDir());
        CameraFunctions.dispatchTakePictureIntent(CameraOrDrawingActivity.this, imageFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraFunctions.REQUEST_TAKE_PHOTO) {
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
        intent.putExtra(FILE_MESSAGE, imageToOpen);
        startActivity(intent);
    }

    private void configureCameraButton() {
        if (!CameraFunctions.isCameraHardwareAvailable(this)) {
            final Button cameraButton = (Button) findViewById(R.id.cameraButton);
            removeCameraButton(cameraButton);
        }
    }

    private void removeCameraButton(Button cameraButton) {
        System.err.println("Error: No camera Available");
        ViewGroup layout = (ViewGroup) cameraButton.getParent();
        if(layout != null) {
            layout.removeView(cameraButton);
        }
    }
}