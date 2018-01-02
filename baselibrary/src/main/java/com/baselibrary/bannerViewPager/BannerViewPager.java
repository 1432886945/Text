package com.baselibrary.bannerViewPager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/29.
 */

public class BannerViewPager extends ViewPager{
    private BannerAdapter mAdapter;
    private final int SCROLL_MESSAGE=0x0011;
    //页面轮播切换的间隔时间
    private int mCountTime=3500;
    //自定义的页面切换的速率
    private BnnerScroll mScroller;
    private List<View> mCuonvertView;
    private Activity mActivity;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            setCurrentItem(getCurrentItem()+1);
            //不断的循环执行
            startRoll();
        }
    };

    /**
     * 销毁Handler  停止发送
     */
    @Override
    protected void onDetachedFromWindow() {
        //销毁Handler的生命周期
        mHandler.removeMessages(SCROLL_MESSAGE);
        mHandler=null;
        //解除绑定
        mActivity.getApplication().unregisterActivityLifecycleCallbacks(activtyLeftCycleCall);

        super.onDetachedFromWindow();
    }

    /**
     * 开始轮播
     */
    public void startRoll(){
        //清除消息
        mHandler.removeMessages(SCROLL_MESSAGE);
        // 消息   延迟时间  让用户自定义  有个默认值
        mHandler.sendEmptyMessageDelayed(SCROLL_MESSAGE,mCountTime);
    }
    public BannerViewPager(Context context) {
        this(context,null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity= (Activity) context;
        //改变viewpager切换的速率
        try {
            //反射获取属性
            Field field = ViewPager.class.getDeclaredField("mScroller");
            //设置参数  object第一个代表当前数据在哪个类  第二个参数代表要设置的值
            mScroller=new BnnerScroll(context);
            //设置为强制改变私有属性
            field.setAccessible(true);
            field.set(this,mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCuonvertView=new ArrayList<>();
    }

    /**
     * 设置页面切换的时间
     * @param scrollerDuration
     */
    public void setScrollerDuration(int scrollerDuration){
        mScroller.setScrollDuration(scrollerDuration);
    }
    public void setAdapter(BannerAdapter adapter) {
        //设置viewpager的adapter
        mAdapter=adapter;
        setAdapter(new BannerPagerAdapter());
        //管理activity的生命周期

        mActivity.getApplication().registerActivityLifecycleCallbacks(activtyLeftCycleCall);
    }

    /**
     * 给viewpager设置适配器
     */
    private class BannerPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            //为了实现无线循环
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        /**
         * 创建viewpager条目回调的方法
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            //采用adapter设计模式 为了完全r让用户自定义
            //position  0-->2 的31次方
            View bannerItemView=mAdapter.getView(position%mAdapter.getCount(),getConvertView());
            //添加viewpager中
            container.addView(bannerItemView);
            bannerItemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        mListener.onClick(position%mAdapter.getCount());
                    }
                }
            });
            return bannerItemView;
        }

        /**
         * 销毁条目回调的方法
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
            mCuonvertView.add((View) object);
        }
    }

    /**
     * 获取复用界面
     * @return
     */
    private View getConvertView() {
       for(int i=0;i<mCuonvertView.size();i++){
           //获取没有添加在mCuonvertView中的view
           if(mCuonvertView.get(i).getParent()==null){
                return mCuonvertView.get(i);
           }
       }
        return null;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN://按下
//                //停止轮播
//                mHandler.removeMessages(SCROLL_MESSAGE);
//                break;
//            case MotionEvent.ACTION_UP://抬起
//                //开始轮播
//                mHandler.sendEmptyMessageDelayed(mCountTime,SCROLL_MESSAGE);
//                break;
//        }
//        return true;
//    }

    //管理activity的生命周期
    Application.ActivityLifecycleCallbacks activtyLeftCycleCall=new DefaultActivityLeftCycleCallbacks(){
        @Override
        public void onActivityResumed(Activity activity) {
            super.onActivityResumed(activity);
            //是不是监听的当前的activity的生命周期
            if(activity==mActivity){
            //开始轮播
                mHandler.sendEmptyMessageDelayed(mCountTime,SCROLL_MESSAGE);

            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            super.onActivityPaused(activity);
            if(activity==mActivity){
                //停止轮播
                mHandler.removeMessages(SCROLL_MESSAGE);
            }
        }
    };
    private BannerClickListener mListener;
    public void setOnBannerClick(BannerClickListener listener){
        this.mListener=listener;
    }
}
