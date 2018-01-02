package com.baselibrary.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * 创建时间 : 2017/12/5
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：图片压缩工具
 */
public class CompressionUtils {
    private static ProgressDialog progressDialog;
    public static void setData(final Handler handler, final Activity activity, final ArrayList<String> mImageList){
        final List<File> files=new ArrayList<>();
        progressDialog = ProgressDialog.show(activity, null, "图片压缩中...");
        Flowable.just(mImageList)
                .observeOn(Schedulers.io())
                .map(list -> {
                    // 同步方法直接返回压缩后的文件
                    return Luban.with(activity).load(list).get();
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    for (File file : list) {
                        files.add(file);
                    }
                    Message message=handler.obtainMessage();
                    message.arg1=1;
                    message.obj=files;
                    handler.sendMessage(message);
                    progressDialog.dismiss();
                });
    }
}
