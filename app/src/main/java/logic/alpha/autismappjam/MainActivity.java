package logic.alpha.autismappjam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class MainActivity extends Activity {
    private File imageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void drawButtonClicked(View view) {
        startActivity(new Intent(this, CameraOrDrawingActivity.class));
    }

    public void moodButtonClicked(View view) {
        startActivity(new Intent(this, MoodSelectActivity.class));
    }

    public void viewMoodsButtonClicked(View view) {
        startActivity(new Intent(this, ViewMoodLogActivity.class));
    }
}