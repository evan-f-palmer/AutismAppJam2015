package logic.alpha.autismappjam;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;

public class CameraOrDrawingActivity extends Activity {
    private File imageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_or_drawing);

        configureCameraButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraFunctions.REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                CameraFunctions.galleryAddPic(this, imageFile);
            }
        }
    }

    private void configureCameraButton() {
        final Button cameraButton = (Button) findViewById(R.id.button);
        if(CameraFunctions.isCameraHardwareAvailable(this)) {
            setOnClickedListenerForCameraButton(cameraButton);
        } else {
            removeCameraButton(cameraButton);
        }
    }

    private void setOnClickedListenerForCameraButton(Button cameraButton) {
        final Activity currentActivity = this;
        cameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imageFile = CameraFunctions.dispatchTakePictureIntent(currentActivity);
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
