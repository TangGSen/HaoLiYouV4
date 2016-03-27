package com.sen.haoliyou.tools;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sen.haoliyou.R;
import com.sen.haoliyou.imgloader.CircleBitmapDisplayer;

/**
 * 网络图片加载配置类
 *
 */
public class ImageLoadOptions {




    /**
     * 默认设置
     *
     *
     * @return
     */
    public static DisplayImageOptions getBannerImageOptions() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();

        return displayImageOptions;
    }


    /**
     * 这个评论的
     */
 public static DisplayImageOptions getCommentImageOptions() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.mipmap.defuser_head)
                .showImageForEmptyUri(R.mipmap.defuser_head)
                .showImageOnFail(R.mipmap.defuser_head)
                .displayer(new FadeInBitmapDisplayer(500))
                .displayer(new CircleBitmapDisplayer())
                .build();

        return displayImageOptions;
    }
/**
     * 这个评论的
     */
 public static DisplayImageOptions getStudyImageOptions() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.mipmap.coursedefault)
                .showImageForEmptyUri(R.mipmap.coursedefault)
                .showImageOnFail(R.mipmap.coursedefault)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();

        return displayImageOptions;
    }




}
