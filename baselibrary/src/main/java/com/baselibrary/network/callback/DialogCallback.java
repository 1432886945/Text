/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baselibrary.network.callback;

import android.app.Activity;
import android.widget.Toast;

import com.baselibrary.network.NetworkUtils;
import com.baselibrary.view.LoadDialog;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：对于网络请求是否需要弹出进度对话框
 * 修订历史：
 * ================================================
 */
public class DialogCallback<T> extends JsonCallback<T> {
    LoadDialog dialog=null;

    private Activity activity;

    public LoadDialog getInstance(Activity activity) {
        if (dialog == null) {
            synchronized (LoadDialog.class) {
                dialog=new LoadDialog(activity);
                dialog.ShowText(); //是否显示加载中字体（默认不显示）
                dialog.SetText("努力加载中…");  //自定义字体
                dialog.show();
            }
        }
        return dialog;
    }

    public DialogCallback(Activity activity) {
        super();
        this.activity=activity;
        getInstance(activity);
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        if (!NetworkUtils.isNetConnected(activity)){
            Toast.makeText(activity,"亲，你断网了啊！",Toast.LENGTH_SHORT).show();
            // 一定要调用这个方法才会生效
            onFinish();
            return;
        }

        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onFinish() {
     //   网络请求结束后关闭对话框
        if (dialog != null ) {
            dialog.dismiss();
        }
    }


    @Override
    public void onError(Response<T> response) {
        super.onError(response);
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
