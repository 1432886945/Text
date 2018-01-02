package com.baselibrary.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import com.baselibrary.R;


/**
 * 父类提示框
 */
public class BaseDialog extends Dialog {
    private Context context;
    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        getWindow().setGravity(Gravity.BOTTOM);
        this.context = context;
    }

    public BaseDialog(Context context) {
        this(context, R.style.CustomDialogStyle);
    }

    @Override
    public void show() {
        super.show();
        if(context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = (display.getWidth()); //设置宽度
            getWindow().setAttributes(lp);
        }
    }
}
