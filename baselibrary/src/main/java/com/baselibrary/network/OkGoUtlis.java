package com.baselibrary.network;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.baselibrary.network.callback.DialogCallback;
import com.baselibrary.ui.model.ErrorModel;
import com.baselibrary.utils.CollectionUtils;
import com.baselibrary.utils.GsonUtils;

import com.baselibrary.utils.ToastUtils;
import com.google.gson.internal.LinkedTreeMap;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

/**
 * 创建时间 : 2017/12/7
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：网络请求封装工具类
 */
public class OkGoUtlis{
    private static OkGoUtlis okGoUtlis;
    private boolean isok=true;

    public static OkGoUtlis getInstance() {
        if (okGoUtlis == null) {
            synchronized (OkGoUtlis.class) {
                if (okGoUtlis == null) {
                    okGoUtlis = new OkGoUtlis();
                }
            }
        }
        return okGoUtlis;
    }

    /**
     *
     * @param handler 当前handler
     * @param hid  接收数据标识符
     * @param type 请求方式标识
     * @param url 链接
     * @param httpParams 请求参数
     * @param activity 当前上下文
     */
    public void getmData(final Handler handler, final int hid, int type, String url, HttpParams httpParams, final Activity activity){
        switch (type){
            case 0:
                OkGo.get(url)
                        .tag(activity)
                        .params(httpParams)
                        .execute(new DialogCallback<Object>(activity){
                            @Override
                            public void onSuccess(Response<Object> response) {
                                super.onSuccess(response);
                                if (isOk(response))
                                    setOK(handler, hid, response.body());
                                else   showError(response,activity);
                            }
                            @Override
                            public void onError(Response<Object> response) {
                                super.onError(response);
                                showError(response,activity);
                            }
                        });
                return;
            case 1:
                OkGo.post(url)
                        .tag(activity)
                        .params(httpParams)
                        .execute(new DialogCallback<Object>(activity){
                            @Override
                            public void onSuccess(Response<Object> response) {
                                super.onSuccess(response);
                                if (isOk(response))
                                    setOK(handler, hid, response.body());
                                else   showError(response,activity);
                            }
                            @Override
                            public void onError(Response<Object> response) {
                                super.onError(response);
                                showError(response,activity);
                            }
                        });
                return;
            case 2:
                OkGo.delete(url)
                        .tag(activity)
                        .params(httpParams)
                        .execute(new DialogCallback<Object>(activity){
                            @Override
                            public void onSuccess(Response<Object> response) {
                                super.onSuccess(response);
                                if (isOk(response))
                                    setOK(handler, hid, response.body());
                                else   showError(response,activity);
                            }
                            @Override
                            public void onError(Response<Object> response) {
                                super.onError(response);
                                showError(response,activity);
                            }
                        });
                return;
            case 3:
                OkGo.put(url)
                        .tag(activity)
                        .params(httpParams)
                        .execute(new DialogCallback<Object>(activity){
                            @Override
                            public void onSuccess(Response<Object> response) {
                                super.onSuccess(response);
                                if (isOk(response))
                                    setOK(handler, hid, response.body());
                                else   showError(response,activity);
                            }
                            @Override
                            public void onError(Response<Object> response) {
                                super.onError(response);
                                showError(response,activity);
                            }
                        });
                return;
        }
    }

    private void setOK(Handler handler, int hid, Object data){
        Message message=handler.obtainMessage();
        message.obj=data;
        message.arg1=hid;
        handler.sendMessage(message);
    }

    public boolean isOk(Response response){
        return isok ? response.code() >= 200 && response.code() < 300:response.code()>300;
    }

    //显示错误信息弹窗
    public void showError(Response<Object> response,Activity activity){
        if (!CollectionUtils.isNullOrEmpty(response.getRawResponse())) {
            try {
                if (response.code()<=402){
                    LinkedTreeMap<String,Object> maps= (LinkedTreeMap<String, Object>) response.body();
                    if (!CollectionUtils.isNullOrEmpty(maps)){
                      ToastUtils.showShort(maps.get("msg").toString());
                    }
                }else {
                    ErrorModel errorModel=  GsonUtils.GsonToBean(response.getRawResponse().body().string(),ErrorModel.class);
                    if (!CollectionUtils.isNullOrEmpty(errorModel)){
                        ToastUtils.showShort(errorModel.getMsg());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
