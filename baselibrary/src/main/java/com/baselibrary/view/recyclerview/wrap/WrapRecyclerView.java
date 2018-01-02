package com.baselibrary.view.recyclerview.wrap;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;


/**
 * 自定义RecyclerView
 */
public class WrapRecyclerView extends RecyclerView {
    private WrapRecyclerAdapter mAdapter;
    //观察者模式
    private AdapterDataObserver mAdapterDataObserver=new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            mAdapter.notifyItemRangeChanged(positionStart,itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            mAdapter.notifyItemRangeChanged(positionStart,itemCount,payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            mAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            mAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };
    public WrapRecyclerView(Context context) {
        this(context,null);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof WrapRecyclerAdapter) {
            mAdapter= (WrapRecyclerAdapter) adapter;
        }else{
            mAdapter=new WrapRecyclerAdapter(adapter);
            adapter.registerAdapterDataObserver(mAdapterDataObserver);
        }
        //删除的问题是因为列表的数据改变了，但是WrapRecyclerAdapter并没有修改所以需要关联  观察者模式
        super.setAdapter(mAdapter);
    }
    // 添加头部
    public void addHeaderView(View view) {
        if(mAdapter!=null){
            mAdapter.addHeaderView(view);
        }
    }

    // 添加底部
    public void addFooterView(View view) {
        if(mAdapter!=null){
            mAdapter.addFooterView(view);
        }
    }

    // 移除头部
    public void removeHeaderView(View view) {
        if(mAdapter!=null){
            mAdapter.removeHeaderView(view);
        }
    }

    // 移除底部
    public void removeFooterView(View view) {
        if(mAdapter!=null){
            mAdapter.removeFooterView(view);
        }
    }
}
