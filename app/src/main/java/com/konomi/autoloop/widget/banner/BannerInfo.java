package com.konomi.autoloop.widget.banner;

/**
 * 创建者     CJR
 * 创建时间   2016/10/13 16:43
 * 描述	      无限轮播图的bean
 * <p>
 */
public class BannerInfo<T> {
    public T      url;
    public String title;

    public BannerInfo(T url, String title) {
        this.url = url;
        this.title = title;
    }
}
