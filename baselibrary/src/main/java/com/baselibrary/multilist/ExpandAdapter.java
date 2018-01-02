package com.baselibrary.multilist;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.baselibrary.R;
import com.baselibrary.utils.HiddenAnimUtils;

import java.util.List;

/**
 * Created by Xiangb on 2017/12/4.
 * 功能：
 */

public class ExpandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ExpandBean> showList;
    private Context context;
    private boolean isMultiSelected = true;
    public MyHolder mLastViewTag = null;
    public ExpandAdapter(Context context, List<ExpandBean> showList, boolean isMultiSelected) {
        this.context = context;
        this.showList = showList;
        this.isMultiSelected = isMultiSelected;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        ExpandBean expandBean= showList.get(position);
        myHolder.tvInfo.setText(expandBean.getItemText());
        ViewGroup.LayoutParams params = myHolder.viewIndex.getLayoutParams();
       params.width = dip2px(context,30 * expandBean.getIndex());
        myHolder.viewIndex.setLayoutParams(params);
        if (expandBean.getChildList() == null) {
            myHolder.ivArrow.setVisibility(View.INVISIBLE);
        } else if (expandBean.isShowChild()) {
            myHolder.ivArrow.setVisibility(View.VISIBLE);
            myHolder.ivArrow.setBackground(ContextCompat.getDrawable(context, R.mipmap.arrow_open));
        } else {
            myHolder.ivArrow.setVisibility(View.VISIBLE);
            myHolder.ivArrow.setBackground(ContextCompat.getDrawable(context, R.mipmap.arrow_close));
        }
        if (!isMultiSelected) {
            myHolder.ivSelect.setVisibility(View.INVISIBLE);
        } else if (expandBean.getChildList() != null) {
            myHolder.ivSelect.setVisibility(View.INVISIBLE);
        } else if (expandBean.isSelected()) {
            myHolder.ivSelect.setVisibility(View.VISIBLE);
            myHolder.ivSelect.setBackground(ContextCompat.getDrawable(context, R.mipmap.select));
        } else {
            myHolder.ivSelect.setVisibility(View.VISIBLE);
            myHolder.ivSelect.setBackground(ContextCompat.getDrawable(context, R.mipmap.not_select));
        }
    }

    @Override
    public int getItemCount() {
        return showList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tvInfo;
        private View viewIndex;
        private ImageView ivArrow, ivSelect;
        private LinearLayout down;

        public MyHolder(View itemView) {
            super(itemView);
            tvInfo = itemView.findViewById(R.id.tv_info);
            viewIndex = itemView.findViewById(R.id.view_index);
            ivArrow = itemView.findViewById(R.id.iv_arrow);
            ivSelect = itemView.findViewById(R.id.iv_select);
            down=itemView.findViewById(R.id.down);
        }
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
