package com.baselibrary.bannerViewPager;

import android.view.View;

/**
 * Created by Administrator on 2017/5/29.
 */

public abstract class BannerAdapter {
    /**
     * 根据位置获取viewpager里面的子view
     * @param position
     * @return
     */
    public abstract View getView(int position,View convertView);

    /**
     * 获取轮播的数量
     * @return
     */
    public abstract int getCount();

    /**
     * 根据位置获取广告为的描述
     * @param position
     * @return
     */
    public String getBannerDesc(int position){
        return "";
    };
}
