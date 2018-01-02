package com.baselibrary.utils;

import java.util.Iterator;
import java.util.List;

/**
 * 创建时间 : 2017/6/20
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：集合处理工具类
 */
public class ListUtils {
    private ListUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    //把list拼接成字符串
    public static String formatStr(List<String> list) {
        String result = "";
        if(list.size() > 0) {
            for (int i=0;i<list.size();i++){
                if(list.get(i) != null){
                    result += list.get(i) + ",";
                }
            }
            result = result.substring(0,result.length()-1);
        }
        return result;
    }

    //删除list中指定元素
    public static void deleteid(List list, String gid){
        Iterator<String> sListIterator = list.iterator();
        while(sListIterator.hasNext()){
            String e = sListIterator.next();
            if(e.equals(gid)){
                sListIterator.remove();
            }
        }
    }

}
