package logic.alpha.autismappjam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import logic.alpha.autismappjam.stickers.Sticker;
import logic.alpha.autismappjam.stickers.Stickers;

public class StickerUnlockActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_unlock);

        Sticker newSticker = unlockSticker();
        displaySticker(newSticker);
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

    private Sticker unlockSticker() {
        Sticker newSticker = null;
        Stickers stickers = new Stickers(this);;
        List<Sticker> lockedStickers =  stickers.getLockedStickers();
        if(!lockedStickers.isEmpty()) {
            newSticker = lockedStickers.get((int)(Math.random() * lockedStickers.size()));
            stickers.unlockSticker(newSticker);
            stickers.saveStickers(this);
        }
        return newSticker;
    }

    private void displaySticker(Sticker newSticker) {
        if(newSticker != null) {
            ImageView stickerView = (ImageView)findViewById(R.id.stickerView);
            stickerView.setImageBitmap(newSticker.load(this, 100, 100));
        }
    }
}