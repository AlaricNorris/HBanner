package com.lake.banner.loader;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.lake.banner.BannerGravity;
import com.lake.banner.R;
import com.lake.banner.uitls.Constants;
import com.lake.banner.uitls.MD5Util;

import java.io.File;

/**
 * 图片代理实现
 */
public class ImageLoader implements ViewLoaderInterface<ImageView> {
    private int gravity = BannerGravity.CENTER;

    public ImageLoader() {
    }

    public ImageLoader(int gravity) {
        this.gravity = gravity;
    }

    @Override
    public ImageView createView(Context context) {
        return new ImageView(context);
    }

    @Override
    public void onPrepared(Context context, Object path, ImageView imageView) {
        try {
            switch (gravity) {
                case BannerGravity.CENTER:
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    break;
                case BannerGravity.FULL_SCREEN:
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    break;
            }

            if (path instanceof Integer) {
                imageView.setImageResource((int) path);
            } else if (path instanceof Uri) {
                String pStr = path.toString();
                String type = pStr.substring(pStr.lastIndexOf("."));
                File file = new File(Constants.DEFAULT_DOWNLOAD_DIR + File.separator + MD5Util.md5(path.toString()) + type);
                if (file.exists()) {
                    Log.e("lake", "onPrepared: isCache");
                    imageView.setImageURI(Uri.fromFile(file));
                } else {
                    Log.e("lake", "onPrepared: noCache");
                    imageView.setImageResource(R.mipmap.defaultvideobg);
                }
            } else {
                imageView.setImageBitmap(BitmapFactory.decodeFile((String) path));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(ImageView imageView) {
        System.gc();
    }
}
