package com.baselibrary.ui.listener;

import android.Manifest;

/**
 * 创建时间 : 2017/12/16
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：android项目主要权限
 */
public interface PermissionListener {
    //读写权限
    String READ= Manifest.permission.READ_EXTERNAL_STORAGE;
    String WRITE= Manifest.permission.WRITE_EXTERNAL_STORAGE;
    //相机权限
    String CAMERA=Manifest.permission.CAMERA;
    //麦克风
    String RECORD_AUDIO=Manifest.permission.RECORD_AUDIO;
    //定位权限
    String ACCESS_COARSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
    //读取联系人权限
    String CONTACTS=Manifest.permission.READ_CONTACTS;
    //发送短信权限
    String SMS=Manifest.permission.SEND_SMS;
    //拨打电话权限
    String PHONE= Manifest.permission.CALL_PHONE;
    //网络权限
    String INTERNET=Manifest.permission.INTERNET;
}
