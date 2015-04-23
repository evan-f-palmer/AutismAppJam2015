package logic.alpha.autismappjam.stickers;

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
import java.util.LinkedList;
import java.util.List;

import logic.alpha.autismappjam.R;
import logic.alpha.autismappjam.util.FileUtils;

/**
 * Created by evan on 4/23/15.
 */
public class Stickers {
    public static final String STICKERS_FILE = "stickers.txt";
    public static final String BACKUP_STICKERS_FILE = "backup_stickers.txt";


    private List<Sticker> unlockedStickers;
    private List<Sticker> lockedStickers;

    public Stickers(Context context) {
        try {
            loadStickers(context, STICKERS_FILE);
        } catch (CouldNotLoadFile ex) {
            try {
                loadStickers(context, BACKUP_STICKERS_FILE);
            } catch (CouldNotLoadFile ex2) {
                loadDefaults();
            }
        }
    }

    public void unlockSticker(Sticker sticker) {
        unlockedStickers.add(sticker);
        lockedStickers.remove(sticker);
    }

    public void addSticker(Sticker sticker) {
        lockedStickers.add(sticker);
    }

    public List<Sticker> getUnlockedStickers() {
        return unlockedStickers;
    }

    public List<Sticker> getLockedStickers() {
        return lockedStickers;
    }

    //TODO make generic object in/output class
    public void saveStickers(Context context) {
        backupFile();

        try {
            FileOutputStream fos = context.openFileOutput(STICKERS_FILE, Context.MODE_PRIVATE);
            OutputStream buffer = new BufferedOutputStream(fos);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(unlockedStickers);
            output.writeObject(lockedStickers);
            output.close();
            buffer.close();
            fos.close();
        } catch (Exception ex) {
            throw new CouldNotSaveFile();
        }
    }

    private void backupFile() {
        try {
            FileUtils.copy(new File(STICKERS_FILE), new File(BACKUP_STICKERS_FILE));
        } catch (IOException e) {
            System.err.println("Could not backup stickers file");
        }
    }

    private void loadStickers(Context context, String fileName) {
        try {
            InputStream fis = context.openFileInput(fileName);
            InputStream buffer = new BufferedInputStream(fis);
            ObjectInput input = new ObjectInputStream(buffer);
            unlockedStickers = (List<Sticker>)input.readObject();
            lockedStickers = (List<Sticker>)input.readObject();
            input.close();
            buffer.close();
            fis.close();
        } catch (Exception ex) {
            throw new CouldNotLoadFile();
        }
    }

    private void loadDefaults() {
        unlockedStickers = new LinkedList<Sticker>();
        lockedStickers   = new LinkedList<Sticker>();

        setDefaultUnlocked();
        setDefaultLocked();
    }

    private void setDefaultUnlocked() {
        unlockedStickers.add(new LocalSticker(R.drawable.camera));
        unlockedStickers.add(new LocalSticker(R.drawable.brush));
    }

    private void setDefaultLocked() {
        lockedStickers.add(new LocalSticker(R.drawable.happy));
        lockedStickers.add(new LocalSticker(R.drawable.sad));
    }

    public static class CouldNotLoadFile extends RuntimeException {}
    public static class CouldNotSaveFile extends RuntimeException {}
}
