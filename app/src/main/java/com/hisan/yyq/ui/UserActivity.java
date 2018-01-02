package com.hisan.yyq.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import com.baselibrary.album.LQRPhotoSelectUtils;
import com.baselibrary.ui.base.BaseActivity;
import com.baselibrary.ui.listener.PermissionListener;
import com.baselibrary.utils.ImageLoad;
import com.baselibrary.utils.Log;
import com.bumptech.glide.Glide;
import com.hisan.yyq.R;
import com.hisan.yyq.databinding.UserBinding;

import java.io.File;

import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 创建时间 : 2017/12/28
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：头像选择页面
 */
public class UserActivity extends BaseActivity<UserBinding>{
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private PromptDialog promptDialog;
    private static final String[] needContactsPermissions = new String[]{PermissionListener.WRITE, PermissionListener.READ,PermissionListener.CAMERA};


    @Override
    protected int getLayoutId() {
        return R.layout.user;
    }

    @Override
    protected void initView() {
        //创建对象
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, (outputFile, outputUri) -> {
            Glide.with(UserActivity.this).load(outputUri).override(600, 200).fitCenter().into(mBinding.useriamge);
        },false);
        requestPermission();
    }

    @Override
    protected void initEvent() {
        mBinding.useriamge.setOnClickListener(v -> {
            PromptButton cancle = new PromptButton("取消", null);
            cancle.setTextColor(Color.parseColor("#0076ff"));
            promptDialog.showAlertSheet("", true, cancle,
                    new PromptButton("相机", promptButton -> {
                        promptDialog.dismiss();
                        mLqrPhotoSelectUtils.takePhoto();
                    }), new PromptButton("相册", promptButton -> {
                        promptDialog.dismiss();
                        mLqrPhotoSelectUtils.selectPhoto();
                    }));
        });
    }

    @Override
    protected void loadData(boolean isRefresh) {

    }
    private void requestPermission() {
        if (!mayRequestPermission(needContactsPermissions)) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }
    @Override
    public void requestPermissionResult(boolean allowPermission) {
        if (allowPermission) {
            getLoaderManager().initLoader(0, null, this);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }
}
