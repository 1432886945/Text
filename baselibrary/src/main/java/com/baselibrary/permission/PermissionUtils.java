package com.baselibrary.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理权限请求的工具类
 * Created by Administrator on 2017/5/19.
 */

public class PermissionUtils {
    private static final String TAG="PermissionUtils";
    private PermissionUtils(){
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断其是不是6.0以上的版本
     * @return
     */
    public static boolean isOverMarshmallow(){
        return Build.VERSION.SDK_INT>=Build.VERSION_CODES.M;
    }

    /**
     * 执行成功的方法
     * @param mOhject
     * @param requestCode
     */
    public static void executeSuccess(Object mOhject, int requestCode) {
        //获取class中所有的方法
        Method[] methods = mOhject.getClass().getDeclaredMethods();
        for(Method method:methods){
            System.out.println("--------"+method);
            //获取该方法上有没有打这个成功的标记
            PermissionSuccess successMethod = method.getAnnotation(PermissionSuccess.class);
            if(successMethod!=null){
                //代表该方法打了标记
                //遍历找到打了标记的方法并且我们的请求码要一致
                int methodCode = successMethod.requestCode();
                if(methodCode==requestCode){
                    //这个就是我们要找的成功的方法
                    //反射执行该方法
                    executeMethod(mOhject,method);
                }
            }
        }
    }

    /**
     * 反射执行该方法
     * @param mOhject
     * @param method
     */
    private static void executeMethod(Object mOhject,Method method) {
        //反射执行方法  第一个参数是该方法是属于哪个类  第二个参数是传参数
        try {
            method.setAccessible(true);//运行执行私有方法
            method.invoke(mOhject,new Object[]{});//反射执行方法
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取没有授予的权限
     * @param mObject
     * @param mPermisson
     * @return  没有授予过的权限
     */
    public static List<String> getDeniedPermission(Object mObject, String[] mPermisson) {
        List<String> deniedPermission=new ArrayList<String>();
        for(String requestPermission:mPermisson){
            //把没有授予过的权限加入到集合中
            if(ContextCompat.checkSelfPermission(getActivity(mObject),requestPermission)
                    == PackageManager.PERMISSION_DENIED){
                deniedPermission.add(requestPermission);
            }
        }
        return deniedPermission;
    }

    /**
     * 获取Activity
     * @param mObject
     * @return
     */
    public static Activity getActivity(Object mObject) {
        if(mObject instanceof Activity){
            return (Activity)mObject;
        }
        if(mObject instanceof Fragment){
            return ((Fragment) mObject).getActivity();
        }
        return null;
    }

    /**
     * 执行失败的方法
     * @param mObject
     * @param requestCode
     */
    public static void executeFailMethod(Object mObject, int requestCode) {
        //获取class中所有的方法
        Method[] methods = mObject.getClass().getDeclaredMethods();
        for(Method method:methods){
            System.out.println("--------"+method);
            //获取该方法上有没有打这个失败的标记
            PermissionFail failMethod = method.getAnnotation(PermissionFail.class);
            if(failMethod!=null){
                //代表该方法打了标记
                //遍历找到打了标记的方法并且我们的请求码要一致
                int methodCode = failMethod.requestCode();
                if(methodCode==requestCode){
                    //这个就是我们要找的成功的方法
                    //反射执行该方法
                    executeMethod(mObject,method);
                }
            }
        }
    }
}
