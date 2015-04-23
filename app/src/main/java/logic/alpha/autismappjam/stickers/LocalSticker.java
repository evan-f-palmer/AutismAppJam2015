package logic.alpha.autismappjam.stickers;

import android.content.Context;
import android.graphics.Bitmap;

import logic.alpha.autismappjam.util.ImageUtils;

/**
 * Created by evan on 4/23/15.
 */
public class LocalSticker implements Sticker {
    private Integer id;

    public LocalSticker(int id) {
        this.id = id;
    }

    public Bitmap load(Context context, int width, int height) {
        return ImageUtils.getImageBitmap(context, id, width, height);
    }
}
