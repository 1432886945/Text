package com.baselibrary.network;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.baselibrary.utils.Log;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

/**
 * 创建时间 : 2017/12/7
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：OkGO初始化工具
 */
public class KUtilLibs {
    private static Context appContext;

    /**
     * 初始化库
     *
     * @param isDebug 是否打印日志
     * @param TAG     日志TAG
     * @param context application
     */
    public static void init(@NonNull boolean isDebug, @NonNull String TAG, @NonNull Application context, HttpBuild.Build httpBuild) {

        if (TAG == null || context == null) throw new RuntimeException("KUtilLibs 初始化参数均不能为空");
        appContext = context.getApplicationContext();
        if (isDebug) Log.init(TAG, true);//开启日志打印
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (HttpBuild.httpInterceptor != null) {
            builder.addInterceptor(HttpBuild.httpInterceptor);
        } else {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(TAG);
            //log打印级别，决定了log显示的详细程度
            loggingInterceptor.setPrintLevel(isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            //log颜色级别，决定了log在控制台显示的颜色
            loggingInterceptor.setColorLevel(Level.INFO);
            builder.addInterceptor(loggingInterceptor);
        }
        builder.connectTimeout(HttpBuild.timeOut, TimeUnit.SECONDS);
        builder.readTimeout(HttpBuild.timeOut, TimeUnit.SECONDS);
        builder.writeTimeout(HttpBuild.timeOut, TimeUnit.SECONDS);
        CookieJar cookieJar = null;
        switch (HttpBuild.cookieStore) {
            case SPCookieStore:
                cookieJar = new CookieJarImpl(new SPCookieStore(appContext));
                break;
            case DBCookieStore:
                cookieJar = new CookieJarImpl(new DBCookieStore(appContext));
                break;
            case MemoryCookieStore:
                cookieJar = new CookieJarImpl(new MemoryCookieStore());
                break;
        }
        if (cookieJar != null)
            builder.cookieJar(cookieJar);
        OkGo.getInstance().init(context).setOkHttpClient(builder.build());
    }

}
