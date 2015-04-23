package logic.alpha.autismappjam.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by evan on 4/15/15.
 */
public class CameraUtils {
    public static final int REQUEST_TAKE_PHOTO = 1;

    private static boolean isCameraActivityAvailable(Activity currentActivity, Intent takePictureIntent) {
        return takePictureIntent.resolveActivity(currentActivity.getPackageManager()) != null;
    }

    public static boolean isCameraHardwareAvailable(Activity currentActivity) {
        return currentActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static void dispatchTakePictureIntent(Activity currentActivity, File imageFile) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(!isCameraActivityAvailable(currentActivity, takePictureIntent)) {
            throw new CameraActivityNotAvailable();
        }

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        currentActivity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

    public static class CameraActivityNotAvailable extends RuntimeException {}
}