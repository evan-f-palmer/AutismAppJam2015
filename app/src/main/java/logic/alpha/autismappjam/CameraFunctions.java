package logic.alpha.autismappjam;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
public class CameraFunctions {
    public static final int REQUEST_TAKE_PHOTO = 1;

    private static File getGlobalPicturesDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    private static String generateImageFileNameBasedOnTimeStamp() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName;
    }

    private static File createImageFile(File storageDir)  {
        String imageFileName = generateImageFileNameBasedOnTimeStamp();
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            throw new CouldNotCreateImageFile(imageFileName);
        }
        return image;
    }

    private static boolean isCameraActivityAvailable(Activity currentActivity, Intent takePictureIntent) {
        return takePictureIntent.resolveActivity(currentActivity.getPackageManager()) != null;
    }

    public static boolean isCameraHardwareAvailable(Activity currentActivity) {
        return currentActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static File dispatchTakePictureIntent(Activity currentActivity) {
        File photoFile = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (isCameraActivityAvailable(currentActivity, takePictureIntent)) {
            photoFile = createImageFile(getGlobalPicturesDir());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            currentActivity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
        return photoFile;
    }

    public static void galleryAddPic(Activity currentActivity, File imageFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        currentActivity.sendBroadcast(mediaScanIntent);
    }

    public static void setPic(ImageView imageView, File imageFile) {
        String filePath = "file:" + imageFile.getAbsolutePath();

        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    public static class CouldNotCreateImageFile extends RuntimeException {
        public String fileName;
        CouldNotCreateImageFile(String fileName) {
            this.fileName = fileName;
        }
    };
}
