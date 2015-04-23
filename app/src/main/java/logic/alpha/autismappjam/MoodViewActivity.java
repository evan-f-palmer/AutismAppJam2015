package logic.alpha.autismappjam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import logic.alpha.autismappjam.mood.MoodEntry;
import logic.alpha.autismappjam.mood.MoodLogger;
import logic.alpha.autismappjam.util.FileUtils;
import logic.alpha.autismappjam.util.ImageUtils;


public class MoodViewActivity extends Activity {

    MoodEntry moodEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_view);
        moodEntry = (MoodEntry)getIntent().getSerializableExtra(ViewMoodLogActivity.MOOD_TO_VIEW);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        initializeText();
        initializeImage();
    }

    public void onSaveClicked(View view) {
        try {
            File imageFile = copyImageToPicturesFolder();
            ImageUtils.galleryAddPic(this, imageFile);
            Toast.makeText(this, "Saved Entry", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDeleteClicked(View view) {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle("Delete Entry");
        deleteDialog.setMessage("Are you sure you want to delete this entry?");
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                deleteMoodEntry();
            }
        });
        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        deleteDialog.show();
    }

    private void deleteMoodEntry() {
        MoodLogger.deleteMoodEntry(this, moodEntry);
        File imageFile = new File(moodEntry.getImageFileAbsolutePath());
        imageFile.delete();
        Toast.makeText(this, "Deleted Entry", Toast.LENGTH_SHORT).show();
        finish();
        return;
    }

    private void initializeText() {
        TextView dateText = (TextView)findViewById(R.id.dateText);
        TextView moodText = (TextView)findViewById(R.id.moodText);
        dateText.setText("Date:\n" + moodEntry.getEntryName());
        moodText.setText("Mood: " + moodEntry.getMood());
    }

    private void initializeImage() {
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        Bitmap bm = ImageUtils.getImageBitmap(moodEntry.getImageFileAbsolutePath(),
                                              imageView.getWidth(), imageView.getHeight());
        imageView.setImageBitmap(bm);
    }

    private File copyImageToPicturesFolder() throws IOException {
        File src  = new File(moodEntry.getImageFileAbsolutePath());
        File dest = FileUtils.createFile(moodEntry.getImageFileName(), ".jpg", FileUtils.getGlobalPicturesDir());
        FileUtils.copy(src, dest);
        return dest;
    }
}
