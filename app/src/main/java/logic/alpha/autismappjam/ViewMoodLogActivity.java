package logic.alpha.autismappjam;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ViewMoodLogActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mood_log);

        List<MoodEntry> moodEntries = loadMoodEntries();
        ViewGroup moodLogs = (ViewGroup)findViewById(R.id.moodLogs);
        addMoodEntriesToView(moodEntries, moodLogs);
    }

    private List<MoodEntry> loadMoodEntries() {
        List<MoodEntry> moodEntries = null;
        try {
            moodEntries = MoodLogger.getMoodEntries(this);
        } catch (MoodLogger.CouldNotLoadFile ex) {
            moodEntries = new ArrayList<MoodEntry>();
        }
        return moodEntries;
    }

    private void addMoodEntriesToView(List<MoodEntry> moodEntries, ViewGroup viewGroup) {
        for(MoodEntry moodEntry : moodEntries) {
            addMoodEntryToView(moodEntry, viewGroup);
        }
    }

    private void addMoodEntryToView(final MoodEntry moodEntry, ViewGroup viewGroup) {
        View moodLayout = LayoutInflater.from(this).inflate(R.layout.mood_layout, null);
        initializeDateText(moodEntry, moodLayout);
        initializeMoodText(moodEntry, moodLayout);
        initializeViewButton(moodEntry, moodLayout);
        viewGroup.addView(moodLayout);
    }

    private void initializeDateText(MoodEntry moodEntry, View moodLayout) {
        TextView dateText   = (TextView) moodLayout.findViewById(R.id.dateText);
        dateText.setText("Date: " + moodEntry.getEntryName());
    }

    private void initializeMoodText(MoodEntry moodEntry, View moodLayout) {
        TextView moodText   = (TextView) moodLayout.findViewById(R.id.moodText);
        moodText.setText("Mood: " + moodEntry.getMood());
    }

    private void initializeViewButton(final MoodEntry moodEntry, View moodLayout) {
        Button viewButton = (Button) moodLayout.findViewById(R.id.viewButton);
        if(!moodEntry.isImageInitialized()) {
            viewButton.setVisibility(View.GONE);
        } else {
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMoodEntry(moodEntry);
                }
            });
        }
    }

    private void openMoodEntry(MoodEntry moodEntry) {

    }
}
