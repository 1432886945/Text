package com.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 尺寸适配工具
 *
 * @author Dev03
 */
public class ConvertUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context mContext, float pxValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context mContext, float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 返回一个字符串在当前手机中所占的宽度（dp）
     *
     * @param mContext
     * @param str被测量的字符串
     * @return
     */
    public static float getStringWidth(Context mContext, String str) {
        Paint paint = new Paint();
        return px2dip(mContext, paint.measureText(str));
    }

    /**
     * 返回一个控件所占用手机的宽度
     *
     * @param view
     * @return
     */
    public static int getViewWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredWidth();
    }
    /**
     * 获取控件的高度
     */
    public static int getViewHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }


    /**
     * 将文字填充到一行TextView中，如果所有文字组成的宽度超过了TextView的宽度则显示一部分，后面以...结尾
     *
     * @param mContext
     * @param wholeStr
     * @param textview
     * @return
     */
    public static String getStrForTextview(Context mContext, String wholeStr, TextView textview) {
        float strWidth = getStringWidth(mContext, wholeStr);
        int textviewWidth = getViewWidth(textview);
        if (strWidth > textviewWidth) {
            for (int i = 10; i < wholeStr.length(); i++) {
                String copyStr = String.copyValueOf(wholeStr.toCharArray(), 0, i);
                strWidth = getStringWidth(mContext, copyStr);
                if (strWidth >= textviewWidth) {
                    return copyStr + "...";
                }
            }
        }
        return wholeStr;
    }

    /**
     * 获取屏幕的宽度
     *
     * @param mContext
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getWidth(Context mContext) {
        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获取屏幕的高度
     *
     * @param mContext
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getHeight(Context mContext) {
        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 返回顶部状态栏高度
     *
     * @param mContext
     * @return
     */
    public static int getTop(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     *
     * @param mContext
     * @return
     */
    public static int getNavigationBar(Context mContext) {
        Resources resources = mContext.getResources();
        int rid = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid > 0) {
            if (resources.getBoolean(rid)) {
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                return resources.getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    /**
     * 获取设备的物理尺寸
     *
     * @param mContext
     * @return
     */
    public static double getScreenSizeOfDevice(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        double width = dm.widthPixels / dm.xdpi;
        double height = dm.heightPixels / dm.ydpi;
        double x = Math.pow(width, 2);
        double y = Math.pow(height, 2);
        return Math.sqrt(x + y);
    }

    /***
     * 获取屏幕密度
     * @param context
     * @return
     */
    public static float getDensity(Context context)
    {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }
    /*
    * 将时间戳转换为时间
    */
    public static String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    //时间格式转换
    public static String timeChange(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String str = format1.format(date);
        return str;
    }

    public static long timeDifference(String nowtime, String endtime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = 0;
        try {
            //系统时间转化为Date形式
            Date dstart = format.parse(nowtime);
            //活动结束时间转化为Date形式
            Date dend = format.parse(endtime);
            //算出时间差，用ms表示
            diff = dend.getTime() - dstart.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //返回时间差
        return diff;
    }

}
