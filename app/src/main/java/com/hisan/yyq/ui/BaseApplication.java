package com.hisan.yyq.ui;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.baselibrary.network.HttpBuild;
import com.baselibrary.network.KUtilLibs;
import com.baselibrary.utils.FontsOverride;
import com.baselibrary.utils.Log;
import com.baselibrary.utils.utlis.Utils;


/**
 * 创建时间 : 2017/12/5
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：无
 */
public class BaseApplication extends Application {
    private static BaseApplication instance;
    /**
     * 实例化Application，单例模式
     *
     * @return 返回单例的Application对象
     */
    public synchronized static BaseApplication getInstance() {
        synchronized (BaseApplication.class){
            if (instance == null)
                instance = new BaseApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        Log.init("xxx",true);
        KUtilLibs.init(true, "xxx", this, new HttpBuild.Build(null, 10, HttpBuild.CookieType.MemoryCookieStore));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
