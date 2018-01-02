package com.baselibrary.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baselibrary.R;

import com.baselibrary.view.SquareImageView;
import com.baselibrary.view.recyclerview.DeleteListener;
import com.baselibrary.view.recyclerview.ItemClickListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miao on 2017/6/29.
 */

public class RequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> mRequestList;
    private int mMaxNumber;
    private Context mContext;
    public  RequestAdapter(Context context,ArrayList<String> requestList,int maxNumber){
        this.mContext=context;
        this.mMaxNumber=maxNumber;
        this.mRequestList = new ArrayList<String>();
        if(mRequestList!=null)
        {
            this.mRequestList.addAll(requestList);
        }
    }
    public void nodfiyData(List<String> list){
        if(list!=null)
        {
            this.mRequestList.clear();
            this.mRequestList.addAll(list);
        }
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_chooser_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        Holder holder = (Holder) viewHolder;
        if(position==mRequestList.size()){
            holder.ivDelete.setVisibility(View.GONE);
            int resourceId = R.drawable.icon_addpic_unfocused;
            //显示图片 利用Glide centerCrop()
            Glide.with(mContext).load(resourceId).centerCrop().into(holder.image);
            if(position==mMaxNumber){
                holder.image.setVisibility(View.GONE);
            }
        }else{
            holder.ivDelete.setVisibility(View.VISIBLE);
            //显示图片 利用Glide centerCrop()
            Glide.with(mContext).load(mRequestList.get(position)).centerCrop().into(holder.image);
        }
        //条目点击事件
        if(mItemClickListener!=null){
            holder.itemView.setOnClickListener(v -> {
                if(position==mMaxNumber){
                    return;
                }
                if(mRequestList.size()==position){
                    mItemClickListener.onItemClick("",position);
                }else {
                    mItemClickListener.onItemClick(mRequestList.get(position), position);
                }
            });
        }
        if(mItemDeleteListener!=null){
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemDeleteListener.onDeleteClick(mRequestList.get(position), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mRequestList.size()+1;
    }
    class Holder extends RecyclerView.ViewHolder{
        SquareImageView image;
        ImageView ivDelete;

        public Holder(View itemView) {
            super(itemView);
            image = (SquareImageView) itemView.findViewById(R.id.image_show);
            ivDelete= (ImageView) itemView.findViewById(R.id.iv_delete);
        }
    }
    //使用接口回调点击事件
    private ItemClickListener mItemClickListener;
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener=itemClickListener;
    }
    //使用接口回调点击事件
    private DeleteListener mItemDeleteListener;
    public void setOnDeleteClickListener(DeleteListener itemDeleteListener){
        this.mItemDeleteListener=itemDeleteListener;
    }
}
