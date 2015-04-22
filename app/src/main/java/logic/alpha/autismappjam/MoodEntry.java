package logic.alpha.autismappjam;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by evan on 4/21/15.
 */
public class MoodEntry implements Serializable {
    public static final String NOT_AVAILABLE = "N/A";

    private Date date;
    private String entryName;
    private String mood;
    private String imageFileName;
    private String imageFileAbsolutePath;
    private boolean isImageInitialized;

    public MoodEntry() {
        date                  = new Date();
        entryName             = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
        mood                  = NOT_AVAILABLE;
        imageFileName         = NOT_AVAILABLE;
        imageFileAbsolutePath = NOT_AVAILABLE;
        isImageInitialized    = false;
    }

    public String getEntryName() {
        return entryName;
    }

    public String getMood() {
        return mood;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public String getImageFileAbsolutePath() {
        return imageFileAbsolutePath;
    }

    public boolean isMoodSet() {
        return !mood.equals(NOT_AVAILABLE);
    }

    public boolean isImageFileNameSet() {
        return !imageFileName.equals(NOT_AVAILABLE);
    }

    public boolean isImageFileAbsolutePathSet() {
        return !imageFileAbsolutePath.equals(NOT_AVAILABLE);
    }

    public boolean isImageInitialized() {
        return isImageInitialized;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public void setImageToInitialized() {
        isImageInitialized = true;
    }

    public File createImageFile(File storageDir) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        imageFileName = "JPEG_" + timestamp + "_";

        File imageFile = FileUtils.createFile(imageFileName, ".jpg", storageDir);
        imageFileAbsolutePath = imageFile.getAbsolutePath();
        return imageFile;
    }
}
