package com.baselibrary.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/28.
 * RecyclerView通用ViewHolder
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    //用于缓存已找的界面
    private SparseArray<View> mView;
    public ViewHolder(View itemView) {
        super(itemView);
        mView=new SparseArray<>();
    }
    public <T extends View> T getView(int viewId){
        //对已有的view做缓存
        View view=mView.get(viewId);
        //使用缓存的方式减少findViewById的次数
        if(view==null){
            view=itemView.findViewById(viewId);
            mView.put(viewId,view);
        }
        return (T) view;
    }
    //通用的功能进行封装  设置文本 设置条目点击事件  设置图片
    public ViewHolder setText(int viewId , CharSequence text){
        TextView view = getView(viewId);
        view.setText(""+text);
        //希望可以链式调用
        return this;
    }

    /**
     *设置本地图片
     * @param viewId
     * @param resId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int resId){
        ImageView iv=getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    /**
     * 设置控件显示或隐藏
     * @param viewId
     * @param resId
     * @return
     */
    public ViewHolder setViewVisibility(int viewId, int resId){
        View view = getView(viewId);
        view.setVisibility(resId);
        return this;
    }
    /**
     * 加载图片资源路径
     * @param viewId
     * @param imageLoader
     * @return
     */
    public ViewHolder setImagePath(int viewId, HolderImageLoader imageLoader){
        ImageView iv=getView(viewId);
        imageLoader.loadImage(iv,imageLoader.getPath());
        return this;
    }
    public abstract static class HolderImageLoader{
        private String mPath;
        public HolderImageLoader(String path){
            this.mPath=path;
        }

        /**
         * 需要去复写这个方法加载图片
         * @param iv
         * @param path
         */
        public abstract void loadImage(ImageView iv, String path);
        public String getPath(){
            return mPath;
        }
    }
}
