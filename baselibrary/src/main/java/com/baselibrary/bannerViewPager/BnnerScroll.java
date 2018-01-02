package com.baselibrary.bannerViewPager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by Administrator on 2017/5/29.
 * 改变viewpager切换的速率
 */

public class BnnerScroll extends Scroller{
    //动画持续的时间
    private int mScrollDuration=1050;

    /**
     * 设置切换页面持续的时间
     * @param scrollDuration
     */
    public void setScrollDuration(int scrollDuration){
        this.mScrollDuration=scrollDuration;
    }
    public BnnerScroll(Context context) {
        super(context);
    }

    public BnnerScroll(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BnnerScroll(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);

    }
}
