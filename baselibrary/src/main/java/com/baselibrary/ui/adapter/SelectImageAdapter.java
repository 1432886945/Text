package com.baselibrary.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baselibrary.R;

import com.baselibrary.ui.listener.SelectImageListener;
import com.baselibrary.view.SquareImageView;
import com.baselibrary.ui.listener.TakePhotoListener;
import com.baselibrary.view.recyclerview.RecyclerCommonAdapter;
import com.baselibrary.view.recyclerview.ViewHolder;
import com.bumptech.glide.Glide;


import java.util.List;

/**
 * Created by Administrator on 2017/6/10.
 */

public class SelectImageAdapter extends RecyclerCommonAdapter<String> {
    private Context mContext;
    private List<String> imageList;
    private int mMaxCount;

    public SelectImageAdapter(Context context, List<String> data, List<String> choice, int maxCount, int layoutId) {
        super(context, data, layoutId);
        this.mContext = context;
        this.imageList = choice;
        this.mMaxCount = maxCount;
    }

    @Override
    protected void bindData(ViewHolder holder, final String mPath, final int position) {
        if (TextUtils.isEmpty(mPath)) {
            //显示拍照
            holder.setViewVisibility(R.id.camear_ll, View.VISIBLE);
            holder.setViewVisibility(R.id.media_selected_indicator, View.INVISIBLE);
            holder.setViewVisibility(R.id.image_show, View.INVISIBLE);
            //拍照
            holder.getView(R.id.camear_ll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //调用拍照
                    if(takePhotoListener!=null){
                        takePhotoListener.takePhoto();
                    }
                }
            });
        } else {
            //显示图片
            holder.setViewVisibility(R.id.camear_ll, View.INVISIBLE);
            holder.setViewVisibility(R.id.media_selected_indicator, View.VISIBLE);
            holder.setViewVisibility(R.id.image_show, View.VISIBLE);
            SquareImageView image = holder.getView(R.id.image_show);
            //显示图片 利用Glide centerCrop()
            Glide.with(mContext).load(mPath).centerCrop().into(image);
            ImageView selectImageView = holder.getView(R.id.media_selected_indicator);
            if (imageList.contains(mPath)) {
                //设置勾选图片
                selectImageView.setSelected(true);
            } else {
                selectImageView.setSelected(false);
            }
            //给条目添加点击事件
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //没有就加入集合
                    if (!imageList.contains(mPath)) {
                        if (imageList.size() >= mMaxCount) {
                            Toast.makeText(mContext, "最多只能选择" + mMaxCount + "张图片", Toast.LENGTH_LONG).show();
                            return;
                        }
                        imageList.add(mPath);
                    } else {
                        //有就移除
                        imageList.remove(mPath);
                    }
                    //刷新数据
                    notifyDataSetChanged();
                    //通知显示布局
                    if (selectImageListener != null) {
                        selectImageListener.select(imageList.size());
                    }
                }
            });
        }
    }

    private SelectImageListener selectImageListener;

    public void setOnSelectImageListener(SelectImageListener listener) {
        this.selectImageListener = listener;
    }
    private TakePhotoListener takePhotoListener;

    public void setTakePhotoListener(TakePhotoListener listener) {
        this.takePhotoListener = listener;
    }
}
