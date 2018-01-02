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

import com.baselibrary.utils.Log;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.exception.StorageException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 * 修订历史：
 * ================================================
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {
    private Type type;
    private Class<T> clazz;
    public T mData;
    public T mResponse;

    public JsonCallback(Type type){
        this.type = type;
    }

    public JsonCallback(){
        this.clazz = clazz;
    }


    /**
     * 拿到响应后，将数据转换成需要的格式，子线程中执行，可以是耗时操作
     * @param response 需要转换的对象
     * @return 转换后的结果
     * @throws Exception 转换过程发生的异常
     */
    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null){
            return null;
        }
        mData = null;
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(body.charStream());
        if(type != null){
            mData = gson.fromJson(jsonReader,type);
        } else if(clazz != null){
            mData = gson.fromJson(jsonReader,clazz);
        }else {
            Type genType = getClass().getGenericSuperclass();
            Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            mData = gson.fromJson(jsonReader, type);
        }
        return mData;
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        super.onError(response);
        Throwable exception = response.getException();
        if(exception != null){
            exception.printStackTrace();
        }
        mResponse = (T) response.getRawResponse();
        if(exception instanceof UnknownHostException || exception instanceof ConnectException){
        Log.d("网络连接失败，请检查网络");
        }else if(exception instanceof SocketTimeoutException){
            Log.d("网络请求超时");
        }else if(exception instanceof HttpException){
            Log.d("网络端响应码404或者505了，请联系服务器开发人员");
        }else if(exception instanceof StorageException){
            Log.d("SD卡不存在或者没有权限");
        }else if(exception instanceof IllegalStateException){
            String message = exception.getMessage();
            Log.d(message);
        }
    }
    //子类不需要去设置response.body()了，只需要拿到mResponse直接用就好了
    @Override
    public void onSuccess(com.lzy.okgo.model.Response<T> response) {
        mResponse = response.body();
    }


}
