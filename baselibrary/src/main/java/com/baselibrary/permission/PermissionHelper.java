package com.baselibrary.permission;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 * 反射和注解的方式
 */

public class PermissionHelper {
    private Object mObject;
    private int mRequestCode;
    private String[] mPermisson;

    private PermissionHelper(Object object) {
        this.mObject = object;
    }

    //以什么样的方式传参数  传什么样的参数
    //Objec Fragment or Activity  int 请求码  需要请求的权限  String[]
    public static void requestPermisson(Activity activity, int requestCode, String[] permissons) {
        PermissionHelper.with(activity).requestCode(requestCode).requestPermission(permissons).request();
    }

    //链式的方式传递
    //传activity
    public static PermissionHelper with(Activity activity) {
        return new PermissionHelper(activity);
    }

    //传fragment
    public static PermissionHelper with(Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    //传FragmentActivity
    public static PermissionHelper with(FragmentActivity activity) {
        return new PermissionHelper(activity);
    }

    //传请求码
    public PermissionHelper requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    //传请求权限
    public PermissionHelper requestPermission(String... permisson) {
        this.mPermisson = permisson;
        return this;
    }

    /**
     * 真正判断和发起权限请求
     */
    public void request() {
        //首先判断当前的版本是不是6.0和6.0以上
        if (!PermissionUtils.isOverMarshmallow()) {
            //如果不是6.0以上，  反射获取执行方法
            PermissionUtils.executeSuccess(mObject,mRequestCode);
            return;
        }
        //如果是6.0以上那么首先需要判断是否授予权限
        List<String> deniedPermission=PermissionUtils.getDeniedPermission(mObject,mPermisson);
        //如果授予了 反射获取执行方法
        if(deniedPermission.size()==0){
            //全部都是授予过权限的
            PermissionUtils.executeSuccess(mObject,mRequestCode);
        }else{
            //如果没有授予 那么我们就申请
            ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject),
                    deniedPermission.toArray(new String[deniedPermission.size()]),
                    mRequestCode);
        }
    }
    /**
     * 处理权限申请回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void requestPermissionsResult(Object mObject,int requestCode, String[] permissions, int[] grantResults) {
        //再次获取是否授予的权限
        List<String> deniedPermission=PermissionUtils.getDeniedPermission(mObject,permissions);
        if(deniedPermission.size()==0){
            //这些权限全部同一
            PermissionUtils.executeSuccess(mObject,requestCode);
        }else{
            //申请中权限中有用户不同意的
            PermissionUtils.executeFailMethod(mObject,requestCode);
        }
    }
}
