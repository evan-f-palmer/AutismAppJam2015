package logic.alpha.autismappjam.mood;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import logic.alpha.autismappjam.util.FileUtils;

/**
 * Created by evan on 4/20/15.
 */
public class MoodLogger {

    public static final String MAIN_FILE_NAME   = "mood_entries";
    public static final String BACKUP_FILE_NAME = "backup_mood_entries";


    public static MoodEntry createMoodEntry() {
        MoodEntry newMood = new MoodEntry();
        return newMood;
    }

    public static MoodEntry createMoodEntry(String mood) {
        MoodEntry newMood = new MoodEntry();
        newMood.setMood(mood);
        return newMood;
    }

    public static List<MoodEntry> getMoodEntries(Context context) {
        List<MoodEntry> moodEntries = null;
        try {
            moodEntries = getMoodEntries(context, MAIN_FILE_NAME);
        } catch (Exception ex) {
            try {
                moodEntries = getMoodEntries(context, BACKUP_FILE_NAME);
            } catch (Exception ex2) {
                throw new CouldNotLoadFile();
            }
        }

        return moodEntries;
    }

    private static List<MoodEntry> getMoodEntries(Context context, String fileName) {
        List<MoodEntry> moodEntries = null;
        try {
            InputStream fis = context.openFileInput(fileName);
            InputStream buffer = new BufferedInputStream(fis);
            ObjectInput input = new ObjectInputStream(buffer);
            moodEntries = (List<MoodEntry>)input.readObject();
            input.close();
            buffer.close();
            fis.close();
        } catch (Exception ex) {
            throw new CouldNotLoadFile();
        }

        return moodEntries;
    }

    private static void deleteMoodEntryFromList(List<MoodEntry> moodEntries, MoodEntry moodEntryToDelete) {
        for(MoodEntry moodEntry : moodEntries) {
            if(moodEntry.getEntryName().equals(moodEntryToDelete.getEntryName())) {
                moodEntryToDelete = moodEntry;
                break;
            }
        }

        moodEntries.remove(moodEntryToDelete);
    }

    public static void deleteMoodEntry(Context context, MoodEntry moodEntry) {
        List<MoodEntry> moodEntries = MoodLogger.getMoodEntries(context);
        System.out.println("Size before delete: " + moodEntries.size());
        deleteMoodEntryFromList(moodEntries, moodEntry);
        System.out.println("Size after delete: " + moodEntries.size());
        saveMoodEntries(context, moodEntries);
    }

    public static void saveMoodEntry(Context context, MoodEntry moodEntry) {
        backupMoodEntries();
        List<MoodEntry> moodEntries = getMoodEntriesOrBlankListOnError(context);
        moodEntries.add(moodEntry);
        saveMoodEntries(context, moodEntries);
    }

    private static void backupMoodEntries() {
        try {
            FileUtils.copy(new File(MAIN_FILE_NAME), new File(BACKUP_FILE_NAME));
        } catch (IOException e) {
            System.err.println("Could not backup mood file");
        }
    }

    private static List<MoodEntry> getMoodEntriesOrBlankListOnError(Context context) {
        List<MoodEntry> moodEntries = null;
        try {
            moodEntries = MoodLogger.getMoodEntries(context);
        } catch (CouldNotLoadFile e) {
            moodEntries = new ArrayList<MoodEntry>();
        }
        return moodEntries;
    }

    private static void saveMoodEntries(Context context, List<MoodEntry> moodEntries) {
        try {
            FileOutputStream fos = context.openFileOutput(MAIN_FILE_NAME, Context.MODE_PRIVATE);
            OutputStream buffer = new BufferedOutputStream(fos);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(moodEntries);
            output.close();
            buffer.close();
            fos.close();
        } catch (Exception ex) {
            throw new CouldNotSaveFile();
        }
    }

    public static class CouldNotLoadFile extends RuntimeException {}
    public static class CouldNotSaveFile extends RuntimeException {}
}
