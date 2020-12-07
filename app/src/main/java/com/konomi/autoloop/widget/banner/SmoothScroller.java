package com.konomi.autoloop.widget.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * 创建者     CJR
 * 创建时间   2017/01/21 11:56
 * 描述	      无限轮播图的控制器(自动滑动的时候)
 * <p>
 */
public class SmoothScroller extends Scroller {

    /**滚动持续时间*/
    private int mDuration = 2000;

    public SmoothScroller(Context context) {
        super(context);
    }

    public SmoothScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void bingViewPager(ViewPager viewPager) {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }
}
