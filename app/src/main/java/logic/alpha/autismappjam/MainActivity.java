package logic.alpha.autismappjam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends Activity {
    private File imageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureDrawButton();
    }

    private void configureDrawButton() {
        final Activity currentActivity = this;
        final Button drawButton = (Button) findViewById(R.id.cameraButton);
        drawButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(currentActivity, CameraOrDrawingActivity.class));
            }
        });
    }
}