package logic.alpha.autismappjam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import logic.alpha.autismappjam.mood.MoodEntry;
import logic.alpha.autismappjam.mood.MoodLogger;


public class MoodSelectActivity extends Activity {
    public static final String NEW_MOOD = "newMood";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_select);
    }

    public void happyButtonClicked(View view) {
        logMoodAndStartStickerUnlock("happy");
    }

    public void sadButtonClicked(View view) {
        logMoodAndStartStickerUnlock("sad");
    }

    private void logMoodAndStartStickerUnlock(String mood) {
        MoodEntry newMood = MoodLogger.createMoodEntry(mood);
        Intent intent = new Intent(this, StickerUnlockActivity.class);
        intent.putExtra(NEW_MOOD, newMood);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
