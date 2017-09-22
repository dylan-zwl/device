package com.tapc.platform.ui.activity.run;

import com.tapc.platform.R;
import com.tapc.platform.entity.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Administrator on 2017/8/28.
 */

public class RunCommonActivity extends RunBaseActivity {
    @BindView(R.id.run_banner)
    Banner mBanner;
    private List<Object> mImages;

    @Override
    protected int getContentView() {
        return R.layout.activity_run;
    }

    @Override
    protected void initView() {
        super.initView();
        mTapcApp.setHomeActivity(this.getClass());

        mImages = new ArrayList<Object>();
        mImages.add(R.drawable.bg_advertisement_nomal);
        mImages.add(R.drawable.bg_countdown);
        initImageShow();
    }

    private void initImageShow() {
        //设置mBanner样式
        mBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(mImages);
        //设置mBanner动画效果
//        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当mBanner样式有显示title时）
//        mBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(10000);
        //设置指示器位置（当mBanner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBanner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }
}
