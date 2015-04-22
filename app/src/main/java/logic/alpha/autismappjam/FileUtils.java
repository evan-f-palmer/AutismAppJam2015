package logic.alpha.autismappjam;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by evan on 4/21/15.
 */
public class FileUtils {
    public static File getGlobalPicturesDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
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
}
