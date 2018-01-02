package com.baselibrary.utils;

import android.app.Activity;
import android.content.Context;

import com.baselibrary.R;


/**
 * Activity打开动画
 */
public class ActivityEffectUtil {
    public static final int ENTER_RIGHT_TO_LEFT = 0;
    public static final int OUT_LEFT_TO_RIGHT = 1;
    public static final int ENTER_BOTTOM_TO_TOP = 2;
    public static final int OUT_TOP_TO_BOTTOM = 3;

    /**
     * 设置activity效果
     *
     * @param context，如为null，则默认appmanager.currentActivity();
     * @param effectId
     */
    public static void setActivityEffect(Context context, int effectId) {
        if (context == null) {
            context = AppManager.getInstance().currentActivity();
            if(context==null){
                return;
            }
        }
        switch (effectId) {
            case ENTER_RIGHT_TO_LEFT:
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case OUT_LEFT_TO_RIGHT:
                ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case ENTER_BOTTOM_TO_TOP:
                ((Activity) context).overridePendingTransition(R.anim.push_up_in, R.anim.push_no_move);
                break;
            case OUT_TOP_TO_BOTTOM:
                ((Activity) context).overridePendingTransition(R.anim.push_down_in, R.anim.push_no_move);
                break;
        }
    }
}
