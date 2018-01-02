package com.baselibrary.ui.listener;

/**
 * 创建时间 : 2017/10/25
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：点击监听
 */
public  interface DialogOnClickListener {
    //弹窗左边按钮点击事件
     void onCancel(String cancel);
    //弹窗右边按钮点击事件
     void onOk(String ok);

}
