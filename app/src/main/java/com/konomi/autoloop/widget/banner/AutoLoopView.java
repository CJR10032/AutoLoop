package com.konomi.autoloop.widget.banner;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.konomi.autoloop.R;

import java.util.ArrayList;

/**
 * 创建者     CJR
 * 创建时间   2016/10/13 16:32
 * 描述	     无限轮播图
 */
public class AutoLoopView extends RelativeLayout {

    /**装ImageView的集合*/
    public final ArrayList<ImageView> mImageViewList = new ArrayList<>();

    /**轮播速度*/
    private int mLoopSpeed = 4000;
    /**自动轮播时动画的持续时间*/
    private int mLoopDuration = 3000;

    /**ViewPage控件*/
    private ViewPager mViewPager;
    /**显示标题的TextView*/
    private TextView mTvTitle;
    /**移动指示器的容器*/
    private FrameLayout mFlDotContainerr;
    /**固定指示器的容器*/
    private LinearLayout mLlDotContainer;

    /**轮播图的适配器*/
    private NewsPagerAdapter mNewsPagerAdapter;

    private PagerChangeListener mPagerChangeListener;

    /**轮播图信息*/
    private ArrayList<BannerInfo> mBannerInfos;
    /**当前选择的条目*/
    private TextView mDotZero;
    /**当前选择的条目*/
    private TextView mDotOne;
    private int dotMargin = dip2px(/*8*/5);
    private int dotWidth  = dip2px(18.91f);
    private int dotHeight = dip2px(3.64f);
    /*private int dotWidth  = dip2px(8);
    private int dotHeight = dip2px(8);*/

    /**被选中指示器的总位移长度*/
    private int mTotalDistance;
    /**被选中指示器圆点是否跟随ViewPager移动*/
    private boolean mFlowFlag = true;

    /**图片加载的监听*/
    private OnImageLoadListener mOnImageLoadListener;
    /**轮播图的点击监听*/
    private OnBannerClickListener mOnBannerClickListener;


    /**轮播的Runnable*/
    private final Runnable mLoopRunnable = new Runnable() {

        @Override
        public void run() {
            int position = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(position + 1);
            startLoop();
        }
    };

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public AutoLoopView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param attrs   属性
     */
    public AutoLoopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //  初始化
        initializeData();
    }

    /**
     * 初始化
     */
    public void initializeData() {
        //  初始化控件
        initView();
        //  初始化数据
        initData();
        //  初始化监听
        initListener();
    }



    /**
     * 设置轮播速度(每隔loopSpeed毫秒自动播一次)
     *
     * @param loopSpeed 轮播速度(ms)
     * @return 当前对象, 形成链式调用
     */
    public AutoLoopView setLoopSpeed(int loopSpeed) {
        this.mLoopSpeed = loopSpeed;
        return this;
    }

    /**
     * 设置自动轮播时动画的持续时间 (ms)
     *
     * @param loopDuration 自动轮播时动画的持续时间(ms)
     * @return 当前对象, 形成链式调用
     */
    public AutoLoopView setLoopDuration(int loopDuration) {
        this.mLoopDuration = loopDuration;
        return this;
    }

    /**
     * 设置轮播图图片加载的监听
     *
     * @param listener 图片加载监听
     * @return 当前对象, 形成链式调用
     */
    public AutoLoopView setOnImageLoadListener(OnImageLoadListener listener) {
        mOnImageLoadListener = listener;
        return this;
    }

    /**
     * 设置轮播图图片的点击监听
     *
     * @param listener 图片点击监听
     * @return 当前对象, 形成链式调用
     */
    public AutoLoopView setOnBannerClickListener(OnBannerClickListener listener) {
        mOnBannerClickListener = listener;
        return this;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_autoloop, this);
        mViewPager = findViewById(R.id.vp);
        mTvTitle = findViewById(R.id.tvTitle);
        mFlDotContainerr = findViewById(R.id.flDotContaine);
        mLlDotContainer = findViewById(R.id.llDotContainer);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        mNewsPagerAdapter = new NewsPagerAdapter();
        mViewPager.setAdapter(mNewsPagerAdapter);
        mPagerChangeListener = new PagerChangeListener();

        SmoothScroller smoothScroller = new SmoothScroller(getContext());
        smoothScroller.setDuration(mLoopDuration);
        smoothScroller.bingViewPager(mViewPager);
    }

    /**
     * 初始化监听
     */
    public void initListener() {
        mViewPager.addOnPageChangeListener(mPagerChangeListener);
    }

    /**
     * 传入图片的信息
     *
     * @param bannerInfos BannerInfo
     */
    public void setLoopData(ArrayList<BannerInfo> bannerInfos) {
        if (bannerInfos != null && bannerInfos.size() > 0) {
            this.mBannerInfos = bannerInfos;

            if (mOnImageLoadListener != null) {
                //  清空图片集合
                mImageViewList.clear();
                //  广告数量
                for (int i = 0, end = bannerInfos.size(); i < end; i++) {
                    //  获取轮播图信息
                    BannerInfo bannerInfo = bannerInfos.get(i);
                    //  创建图片
                    ImageView imageView = new ImageView(getContext());
                    //  添加到图片里诶包
                    mImageViewList.add(imageView);
                    //  回调图片加载事件
                    mOnImageLoadListener.onImageLoad(imageView, bannerInfo.url);
                    //  记录图片索引
                    final int index = i;

                    //  设置图片点击监听
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnBannerClickListener != null)
                                //  点击监听的回调
                                mOnBannerClickListener.onBannerClick(index,
                                        mBannerInfos.get(index));
                        }
                    });
                }
                if (bannerInfos.size() > 1) {
                    //  图片超过1张
                    for (int i = 0; i < 2; i++) {
                        BannerInfo bannerInfo = bannerInfos.get(i);
                        ImageView imageView = new ImageView(getContext());
                        mImageViewList.add(imageView);
                        mOnImageLoadListener.onImageLoad(imageView, bannerInfo.url);
                        final int index = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mOnBannerClickListener != null){
                                    mOnBannerClickListener.onBannerClick(index,
                                            mBannerInfos.get(index));
                                }
                            }
                        });
                    }
                }
            } else {
                throw new NullPointerException("AutoLoopView OnBannerEventListener " +
                        "is not initialize,Be sure to initialize the onLoadImageView");
            }
        } else {
            throw new NullPointerException("AutoLoopView mBannerInfos " +
                    "is null or mBannerInfos.size() isEmpty");
        }

        //  先把指示器移除
        removeIndicator();
        if (bannerInfos.size() > 1) {
            initIndicator();
            initCurIndicator();
            mLlDotContainer.getViewTreeObserver()
                    .addOnPreDrawListener(new IndicatorPreDrawListener());
        }

        mNewsPagerAdapter.notifyDataSetChanged();
        mViewPager.setOffscreenPageLimit(bannerInfos.size() + 1);
        int currentValue = bannerInfos.size();
        mViewPager.setCurrentItem(currentValue, false);
        startLoop();
    }

    private void initIndicator() {
        int len = mBannerInfos.size();
        for (int i = 0; i < len; i++) {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(dotWidth, dotHeight);
            if (i != len - 1) {
                params.setMargins(0, 0, dotMargin, 0);
            } else {
                params.setMargins(0, 0, 0, 0);
            }
            textView.setLayoutParams(params);
            //  设置默认的背景颜色
            textView.setBackgroundResource(R.drawable.indicator_normal_background);
            mLlDotContainer.addView(textView);
        }
    }

    private void initCurIndicator() {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(dotWidth, dotHeight);
        mDotZero = new TextView(getContext());
        mDotZero.setGravity(Gravity.CENTER);
        //设置选中的背景颜色
        mDotZero.setBackgroundResource(R.drawable.indicator_selected_background);
        mFlDotContainerr.addView(mDotZero, params);
        mDotOne = new TextView(getContext());
        mDotOne.setGravity(Gravity.CENTER);
        //设置选中的背景颜色
        mDotOne.setBackgroundResource(R.drawable.indicator_selected_background);
        mFlDotContainerr.addView(mDotOne, params);
        ViewCompat.setTranslationX(mDotOne, -dotWidth);
    }

    private void removeIndicator() {
        mLlDotContainer.removeAllViews();
        mFlDotContainerr.removeView(mDotZero);
        mFlDotContainerr.removeView(mDotOne);
    }

    /**
     * 当AutoLoopNewsView从window离开时调用
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoop();
    }

    /**
     * 当AutoLoopNewsViwe添加到window的时候调用
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startLoop();
    }

    /**
     * 开始循环
     */
    public void startLoop() {
        if (mBannerInfos == null || mBannerInfos.size() < 2 && mLoopSpeed > 0) {
            // 只有一张图片的时候也不需要轮播
            stopLoop();
            return;
        }
        postDelayed(mLoopRunnable, mLoopSpeed);
    }

    /**
     * 通过removeCallback停止循环
     */
    public void stopLoop() {
        removeCallbacks(mLoopRunnable);
    }


    class PagerChangeListener implements OnPageChangeListener {

        /**
         * 当页面发生滚动时候的回调
         *
         * @param position             滚动的页面的索引
         * @param positionOffset       宽度的比例(滚动偏移的像素占ViewPager宽度的比例)
         * @param positionOffsetPixels 页面滚动偏移量的像素点的个数
         */
        @Override
        public void onPageScrolled(int position,
                                   float positionOffset, int positionOffsetPixels) {
            if (mBannerInfos != null && !mBannerInfos.isEmpty()) {
                int size = mBannerInfos.size();
                if (positionOffset == 0 && size > 1) {
                    if (position == 0) {
                        mViewPager.setCurrentItem(size, false);
                    } else if (position == mNewsPagerAdapter.getCount() - 1) {
                        mViewPager.setCurrentItem(1, false);
                    }
                }

                //  设置标题
                mTvTitle.setText(mBannerInfos.get(position % mBannerInfos.size()).title);
            }

            if (mBannerInfos != null && mBannerInfos.size() > 1) {
                float percent = ((position % mBannerInfos.size()) + positionOffset) / (
                        mBannerInfos.size() - 1);
                float path = percent * mTotalDistance;
                if (mFlowFlag) {
                    ViewCompat.setTranslationX(mDotZero, path);
                    ViewCompat.setTranslationX(mDotOne,
                            path - mTotalDistance - dotMargin - dotWidth);
                }
            }
        }

        /**
         * 当页面选中时发生回调
         *
         * @param position 当前选中的页面的索引
         */
        @Override
        public void onPageSelected(int position) {
            if (!mFlowFlag && mBannerInfos.size() > 1) {
                float percent = (position % mBannerInfos.size() * 1.0f)
                        / (mBannerInfos.size() - 1);
                float path = percent * mTotalDistance;
                ViewCompat.setTranslationX(mDotZero, path);
            }
        }

        /**
         * 当页面滚动状态发生变化的回调
         *
         * public static final int SCROLL_STATE_IDLE = 0; 空闲状态
         * public static final int SCROLL_STATE_DRAGGING = 1; 拖拽状态
         * public static final int SCROLL_STATE_SETTLING = 2;
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            // 设置状态(手指松开到空闲状态的过程), 只有空闲状态的时候才会开启自动轮播
            if (ViewPager.SCROLL_STATE_IDLE == state) {
                startLoop();
            } else {
                stopLoop();
            }
        }
    }

    /**
     * dp转像素
     *
     * @param dip dp的值
     * @return 像素的值
     */
    public int dip2px(float dip) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }


    /**
     * ViewPager的adapter
     */
    class NewsPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mBannerInfos == null) {
                return 0;
            }
            return mBannerInfos.size() < 2 ?
                    mBannerInfos.size() : mBannerInfos.size() + 2;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        /**
         * 页面是否被标记
         *
         * @param view   页面的view
         * @param object 页面的标记
         * @return 是否这个view被标记了, 在需要用到view的时候, 会先判断view是不是被创建了(通过标记),
         * 如果是创建过的, 就直接用以前的
         * ,不会创建 如果是没创建的,就会调用instantiateItem方法创建并打上标记
         * 总结为一句话就是:true就不去创建,使用缓存;false就去创建
         */
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        /**
         * 初始化页面 类似于ListView的adapter中的getView
         *
         * @param container 看过源码之后发现,在这里就是ViewPager,容器
         * @param position  加载的页面的索引
         * @return 返回页面的标记
         */
        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImageViewList.get(position);
            container.removeView(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * OnPreDrawListener
     */
    private class IndicatorPreDrawListener
            implements ViewTreeObserver.OnPreDrawListener {

        @Override
        public boolean onPreDraw() {
            Rect firstRect = new Rect();
            mLlDotContainer.getChildAt(0).getGlobalVisibleRect(firstRect);

            Rect lastRect = new Rect();
            mLlDotContainer.getChildAt(mBannerInfos.size() - 1).
                    getGlobalVisibleRect(lastRect);

            mTotalDistance = lastRect.left - firstRect.left;

            mLlDotContainer.getViewTreeObserver().removeOnPreDrawListener(this);

            return false;
        }
    }

    /**
     * 图片加载的监听, 交由外部调用图片加载库加载
     */
    public interface OnImageLoadListener {
        /**
         * image load
         *
         * @param imageView ImageView
         * @param imgUri String    可以为一个文件路径、uri或者url
         *                  Uri   uri类型
         *                  File  文件
         *                  Integer   资源Id,R.drawable.xxx或者R.mipmap.xxx
         *                  byte[]    类型
         *                  T 自定义类型
         */
        void onImageLoad(ImageView imageView, Object imgUri);
    }

    /**
     * 轮播图被点击的监听
     */
    public interface OnBannerClickListener {

        /**
         * 轮播图被点击的监听
         *
         * @param index   轮播图的索引
         * @param banner  轮播图的数据
         */
        void onBannerClick(int index, BannerInfo banner);
    }

}

