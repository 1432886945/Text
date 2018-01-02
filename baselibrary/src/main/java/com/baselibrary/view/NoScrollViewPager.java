package com.baselibrary.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 创建时间 : 2017/12/25
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：取消滑动的pager
 */
public class NoScrollViewPager  extends ViewPager {
    private float preX;
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }
    //去除页面切换时的滑动翻页效果
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, false);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        boolean res = super.onInterceptTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            preX = event.getX();
        } else {
            if( Math.abs(event.getX() - preX)> 4 ) {
                return true;
            } else {
                preX = event.getX();
            }
        }
        return res;
    }
}
