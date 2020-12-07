package com.konomi.autoloop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.konomi.autoloop.utils.ImageUtils;
import com.konomi.autoloop.widget.banner.AutoLoopView;
import com.konomi.autoloop.widget.banner.BannerInfo;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private Toast        mToast;
    private AutoLoopView mAutoLoopView;
    private FrameLayout  mContainerView;

    ArrayList         imgs;
    ArrayList<String> titles;

    public void doToast(String str) {
        if (mToast == null) {
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        } else {
            mToast.setText(str);
            mToast.show();
        }
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public ArrayList<BannerInfo> initData() {
        ArrayList<BannerInfo> bannerInfos = new ArrayList<>();
        imgs = new ArrayList();
        imgs.add(R.drawable.a0);
        //        imgs.add(R.drawable.a1);
        //        imgs.add(R.drawable.a2);
        //        imgs.add(R.drawable.a3);
        imgs.add(R.drawable.a4);

        titles = new ArrayList<>();
        titles.add("折纸鸢一");
        //        titles.add("黑长直");
        //        titles.add("新垣绫濑");
        //        titles.add("五河琴里");
        titles.add("双马尾");

        addNetPic();

        int len = imgs.size();
        for (int i = 0; i < len; i++) {
            bannerInfos.add(new BannerInfo<>(imgs.get(i), titles.get(i)));
        }
        return bannerInfos;
    }

    public void addNetPic() {
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1487055176&di=4f81eb37d9a4f0154af8c553eec7c3b0&imgtype=jpg&er=1&src=http%3A%2F%2Fi1.hdslb.com%2Fvideo%2F9a%2F9a3c440c51376b515c07752b2c4beefb.jpg");
        titles.add("圣子降临");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486460971076&di=3616a59860d6cb8130dfa2916d204989&imgtype=0&src=http%3A%2F%2Fi-7.vcimg.com%2Ftrim%2F58dcd73ebbfb673c72b5fe97e5d409c651253%2Ftrim.jpg");
        titles.add("桐乃");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486461048912&di=7c13ec9f48d1c4bdfc64def876cf3d8e&imgtype=0&src=http%3A%2F%2Fp2.image.hiapk.com%2Fuploads%2Fallimg%2F121130%2F23-121130113146.jpg");
        titles.add("ましろ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(mAutoLoopView);
        setContentView(R.layout.activity_main);

       // mAutoLoopView = new AutoLoopView(this);
        mAutoLoopView = findViewById(R.id.loopView);
        //TODO 设置LoopViewPager参数
        mAutoLoopView.setLoopSpeed(3000);//轮播的速度(毫秒)
        mAutoLoopView.setLoopDuration(1600);//滑动的速率(毫秒)

        //TODO 初始化
        //  mAutoLoopView.initializeData();

        /*bannerInfos.add(new BannerInfo<Integer>(R.drawable.a0, "第一张图片"));
        //        bannerInfos.add(new BannerInfo<String>("http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1309/13/c6/25630915_1379054108962.jpg", "第二张图片"));
        bannerInfos.add(new BannerInfo<Integer>(R.drawable.a1, "第二张图片"));
        bannerInfos.add(new BannerInfo<Integer>(R.drawable.a2, "第三张图片"));
        bannerInfos.add(new BannerInfo<Integer>(R.drawable.a3, "第四张图片"));
        bannerInfos.add(new BannerInfo<Integer>(R.drawable.a4, "第五张图片"));*/

        //TODO 准备数据
        final ArrayList<BannerInfo> bannerInfos = initData();

        //TODO 设置监听
        mAutoLoopView.setOnImageLoadListener(new AutoLoopView.OnImageLoadListener() {
            @Override
            public void onImageLoad(ImageView imageView, Object imgUri) {
                ImageUtils.glideLoadPic(MainActivity.this, imageView, imgUri);
                 /*if (img instanceof String) {
                     ImageUtils.picassoLoadPic(this, imageView, (String) imgUri);
                 } else if (img instanceof Integer) {
                     ImageUtils.picassoLoadPic(this, imageView, (Integer) imgUri);
                 }*/
            }
        });

        //  点击监听
        mAutoLoopView.setOnBannerClickListener(new AutoLoopView.OnBannerClickListener() {
            @Override
            public void onBannerClick(int index, BannerInfo banner) {
                doToast("点中的轮播图index : " + index);
            }
        });

        //TODO 设置数据
        mAutoLoopView.setLoopData(bannerInfos);

        mContainerView = (FrameLayout) findViewById(R.id.container);
        //  mContainerView.addView(mAutoLoopView);


        findViewById(R.id.btn_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<BannerInfo> datas = new ArrayList<>();
                datas.add(new BannerInfo<Integer>(R.drawable.a4, "第五张图片"));
                mAutoLoopView.setLoopData(datas);
            }
        });

        findViewById(R.id.btn_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<BannerInfo> datas = new ArrayList<>();
                datas.addAll(bannerInfos);
                datas.addAll(bannerInfos);
                mAutoLoopView.setLoopData(datas);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAutoLoopView != null) {
            mAutoLoopView.startLoop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAutoLoopView != null) {
            mAutoLoopView.stopLoop();
        }
        cancelToast();
    }
}
