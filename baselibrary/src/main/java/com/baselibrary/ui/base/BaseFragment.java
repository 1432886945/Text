package com.baselibrary.ui.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 创建时间 : 2017/12/20
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：Fragment基类
 */
public abstract class BaseFragment<VB extends ViewDataBinding>  extends Fragment {
    protected VB mBinding;
    /**
     * 贴附的activity
     */
    protected FragmentActivity mActivity;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding(container, inflater);
        initData(getArguments());
        mIsPrepare = true;
        initView();
        initEvent();
        onLazyLoad();
        return mBinding.getRoot();
    }
    protected VB getDataBinding(ViewGroup container, LayoutInflater inflater) {
        return DataBindingUtil.inflate(inflater, this.getLayoutId(), container, false);
    }
    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initEvent();
    protected abstract void loadData(boolean isRefresh);

    /**
     * 接收到的从其他地方传递过来的参数
     * @param arguments
     */
    protected abstract void initData(Bundle arguments);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mIsVisible = isVisibleToUser;
        if (isVisibleToUser){
            onVisibleToUser();
        }
    }

    /**
     * 用户可见时执行的操作
     */
    protected void onVisibleToUser(){
        if (mIsPrepare && mIsVisible) {
            onLazyLoad();
        }
    }

    /**
     * 懒加载，仅当用户可见切view初始化结束后才会执行
     */
    protected void onLazyLoad() {
        loadData(true);
    }
}
