package logic.alpha.autismappjam;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import logic.alpha.autismappjam.R;

public class StickerUnlockActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_unlock);
    }

    public void cameraOrDrawingClicked(View view) {
        Intent intent = new Intent(this, CameraOrDrawingActivity.class);
        intent.putExtra(MoodSelectActivity.NEW_MOOD, getIntent().getSerializableExtra(MoodSelectActivity.NEW_MOOD));
        startActivity(intent);
    }

    public void backToMainMenuClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}