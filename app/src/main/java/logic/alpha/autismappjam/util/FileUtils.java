package logic.alpha.autismappjam.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import logic.alpha.autismappjam.mood.MoodEntry;

/**
 * Created by evan on 4/21/15.
 */
public class FileUtils {
    public static final String LOCAL_PICTURES_DIR = "pictures";

    public static File getGlobalPicturesDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    public static File getGlobalDocsDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    public static File getLocalPicturesDir(Context context) {
        ContextWrapper contextWrapper = new ContextWrapper(context.getApplicationContext());
        return contextWrapper.getDir(LOCAL_PICTURES_DIR, Context.MODE_PRIVATE);
    }

    public static File createFile(String fileName, String fileExtension, File storageDir)  {
        File image = null;
        try {
            image = File.createTempFile(fileName, fileExtension, storageDir);
        } catch (IOException e) {
            throw new CouldNotCreateFile(fileName);
        }
        return image;
    }

    public static class CouldNotCreateFile extends RuntimeException {
        public String fileName;
        CouldNotCreateFile(String fileName) {
            this.fileName = fileName;
        }
    };

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static ObjectInput openObjectInput(Context context, String fileName) {
        try {
            InputStream fis = context.openFileInput(fileName);;
            InputStream buffer = new BufferedInputStream(fis);
            fis.close();
            buffer.close();
            return new ObjectInputStream(buffer);
        } catch (Exception ex) {
            throw new CouldNotLoadFile();
        }
    }

    public static void saveLocalFile(Context context, String fileName, Object... objects) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStream buffer = new BufferedOutputStream(fos);
            ObjectOutput output = new ObjectOutputStream(buffer);
            for(Object object : objects) {
                output.writeObject(object);
            }
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
