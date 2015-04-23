package logic.alpha.autismappjam.stickers;

import android.content.Context;
import android.graphics.Bitmap;

import logic.alpha.autismappjam.util.ImageUtils;

/**
 * Created by evan on 4/23/15.
 */
public class GlobalSticker implements Sticker {
    private String filePath;

    public GlobalSticker(String filePath) {
        this.filePath = filePath;
    }

    public Bitmap load(Context context, int width, int height) {
        return ImageUtils.getImageBitmap(filePath, width, height);
    }
}
