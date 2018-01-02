package com.baselibrary.utils;

import android.os.Build;
import android.util.Patterns;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 创建时间 : 2017/6/8
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：数据判断类
 */
public class CollectionUtils {

    //判断对象是否为空
    public static <T> boolean isNullOrEmpty(T obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }
    //检测url是否有效
    public static boolean checkURL(String url) {
        boolean value;
        if (Patterns.WEB_URL.matcher(url).matches()) {
            value=true;
            //符合标准
        } else{
            value=false;
            //不符合标准
        }
        return value;
    }
//    public static void deleteData(List list,List list2){
//        for (int i = 0; i < list.size(); i++) {
//            int count = 0;
//
//            for (int j = 0; j < list2.size(); j++) {
//                if (i > 0 && list.get(i).equals(list2.get(j))) {
//                    count++;
//                    break;
//                }
//
//            }
//            if (count == 0 || i == 0) {
//                list2.add(list.get(i));
//            }
//        }
//    }
}
