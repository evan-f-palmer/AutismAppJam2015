package logic.alpha.autismappjam.stickers;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by evan on 4/23/15.
 */
public interface Sticker extends Serializable {
    public Bitmap load(Context context, int width, int height);
}
