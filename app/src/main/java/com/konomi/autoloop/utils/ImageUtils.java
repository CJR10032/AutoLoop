package com.konomi.autoloop.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.konomi.autoloop.R;
import com.squareup.picasso.Picasso;

/**
 * 创建者     CJR
 * 创建时间   2017/2/7 13:54
 * 描述	      用来加载图片的utils , 这个是demo用的
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述
 */
public class ImageUtils {
    public static void glideLoadPic(Context context, ImageView imageView, Object imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .crossFade()
                .into(imageView);
    }

    public static void picassoLoadPic(Context context, ImageView imageView, String imgUrl) {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context)
                .load(imgUrl)
                        //                .resize(200, 150) //说明：为图片重新定义大小
                        //                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

    public static void picassoLoadPic(Context context, ImageView imageView, int imgRes) {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context)
                .load(imgRes)
                        //                .resize(200, 150) //说明：为图片重新定义大小
                        //                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

    public static void normalLoadPic(ImageView imageView, int imgRe) {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(imgRe);
    }
}
