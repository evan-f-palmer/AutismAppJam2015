package logic.alpha.autismappjam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;

public class CameraOrDrawingActivity extends Activity {
    public final static String FILE_MESSAGE = "file_message";

    private File imageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_or_drawing);

        configureCameraButton();
        configureDrawFromScratchButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraFunctions.REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                CameraFunctions.galleryAddPic(this, imageFile);
                startDrawingApp(imageFile.getPath());
            }
        }
    }

    private void startDrawingApp(String filePath) {
        Intent intent = new Intent(CameraOrDrawingActivity.this, DrawingActivity.class);
        intent.putExtra(FILE_MESSAGE, filePath);
        startActivity(intent);
    }

    private void configureCameraButton() {
        final Button cameraButton = (Button) findViewById(R.id.cameraButton);
        if(CameraFunctions.isCameraHardwareAvailable(this)) {
            setOnClickedListenerForCameraButton(cameraButton);
        } else {
            removeCameraButton(cameraButton);
        }
    }

    private void configureDrawFromScratchButton() {
        final Button drawButton = (Button) findViewById(R.id.drawButton);
        drawButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startDrawingApp(null);
            }
        });
    }

    private void setOnClickedListenerForCameraButton(Button cameraButton) {
        cameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            imageFile = CameraFunctions.dispatchTakePictureIntent(CameraOrDrawingActivity.this);
            }
        });
    }

    private void removeCameraButton(Button cameraButton) {
        System.err.println("Error: No camera Available");
        ViewGroup layout = (ViewGroup) cameraButton.getParent();
        if(layout != null) {
            layout.removeView(cameraButton);
        }
    }
}