package com.baselibrary.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/6/10.
 * 正方形的FrameLayout容器
 */

public class SquareFrameLayout extends FrameLayout{
    public SquareFrameLayout(Context context) {
        this(context,null);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=width;
        //设置宽高一致
        setMeasuredDimension(width,height);
    }
}
