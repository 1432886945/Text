package com.baselibrary.multilist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;

import com.baselibrary.R;
import com.baselibrary.animation.ViewExpandAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Xiangb on 2017/12/4.
 * 功能：
 */

public class ExpandRecyclerHelper {
    private Context context;
    private RecyclerView recyclerView;
    private ExpandAdapter adapter;
    private boolean isMultiSelected = true;
    private List<ExpandBean> dataList = new ArrayList<>();
    private List<ExpandBean> showList = new ArrayList<>();
    private ExpandItemClickListener itemClickListener;
  //  private boolean showMenuSelectView = false;

    public static ExpandRecyclerHelper getInstance(Context context) {
        ExpandRecyclerHelper zxExpandRecyclerHelper = new ExpandRecyclerHelper(context);
        return zxExpandRecyclerHelper;
    }

    public ExpandRecyclerHelper(Context context) {
        this.context = context;
    }

    public ExpandRecyclerHelper withRecycler(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(final RecyclerView recyclerView, final int position, View view) {
                        try {
                            String id = showList.get(position).getId();
                            if (id != null && id.length() > 0) {
                                setShowById(dataList, id);
                                if (showList.get(position).getChildList() == null) {
                                    showList.get(position).setSelected(!showList.get(position).isSelected());
                                    if (itemClickListener != null) {
                                        itemClickListener.onItemClick(showList.get(position));//数据点击事件
                                    }
                                } else {
                                    if (itemClickListener != null) {
                                        itemClickListener.onMenuClick(showList.get(position));//菜单点击事件
                                    }
                                }
                                //将上一个展开的view 收缩
                                if (adapter.mLastViewTag != null) {
                                    View previousView = view.findViewWithTag(adapter.mLastViewTag );
                                    if (previousView != null) {
                                        View childrenView = previousView.findViewById(R.id.down);
                                        if (childrenView != null
                                                && (childrenView.getVisibility() != View.GONE)) {
                                            childrenView.startAnimation(new ViewExpandAnimation(
                                                    childrenView));
                                        }
                                    }
                                }
                                //记录当前item的Tag
                                adapter.mLastViewTag = (ExpandAdapter.MyHolder) view.getTag();
                                //展开当前点击的item
                                View childrenLayout = view.findViewById(R.id.down);
                                ViewExpandAnimation expandAnimation = new ViewExpandAnimation(childrenLayout);
                                expandAnimation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                    }
                                    @Override
                                    public void onAnimationEnd(Animation animation) {
//                                        //item展开结束后，将当前item平滑滚动到顶部
                                        recyclerView.smoothScrollToPosition(0);
                                    }
                                    @Override
                                    public void onAnimationRepeat(Animation animation) {}
                                });

                                childrenLayout.startAnimation(expandAnimation);
                                showList.clear();
                                refresh(dataList);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        return this;
    }

    //刷新
    private void refresh(List<ExpandBean> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            showList.add(dataList.get(i));
            if (dataList.get(i).getChildList() != null && dataList.get(i).getChildList().size() > 0 && dataList.get(i).isShowChild()) {
                refresh(dataList.get(i).getChildList());
            }
        }
    }

    //根据id寻找位置
    private void setShowById(List<ExpandBean> dataList, String id) {
        for (int i = 0; i < dataList.size(); i++) {
            if (id.equals(dataList.get(i).getId())) {
                dataList.get(i).setShowChild(!dataList.get(i).isShowChild());
                //如果关闭item，就关闭它的所有子集
                if (!dataList.get(i).isShowChild()) {
                    closeChild(dataList.get(i));
                }
                return;
            }
            if (dataList.get(i).getChildList() != null && dataList.get(i).getChildList().size() > 0) {
                setShowById(dataList.get(i).getChildList(), id);
            }
        }
    }

    //关闭子集
    private void closeChild(ExpandBean zxExpandBean) {
        if (zxExpandBean.getChildList() == null) {
            return;
        }
        for (int i = 0; i < zxExpandBean.getChildList().size(); i++) {
            zxExpandBean.getChildList().get(i).setShowChild(false);
            if (zxExpandBean.getChildList().get(i).getChildList() != null && zxExpandBean.getChildList().get(i).getChildList().size() > 0) {
                closeChild(zxExpandBean.getChildList().get(i));
            }
        }
    }

    //设置数据
    public ExpandRecyclerHelper setData(List<ExpandBean> dataList) {
        this.dataList = dataList;
        setIndex(this.dataList, 0);
        refresh(dataList);
        return this;
    }

    //循环设置层级
    private void setIndex(List<ExpandBean> dataList, int index) {
       for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setIndex(index);
            String uuid = UUID.randomUUID().toString();
            dataList.get(i).setId(uuid);
            dataList.get(i).setShowChild(false);
            if (dataList.get(i).getChildList() != null && dataList.get(i).getChildList().size() > 0) {
                int item = index + 1;
                setIndex(dataList.get(i).getChildList(), item);
            }
        }
    }

    //是否可以多选
    public ExpandRecyclerHelper multiSelected(boolean isMultiSelected) {
        this.isMultiSelected = isMultiSelected;
        return this;
    }

    //设置item的点击事件（非菜单）
    public ExpandRecyclerHelper setItemClickListener(ExpandItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

//    //是否显示菜单的选择栏
//    public ZXExpandRecyclerHelper showMenuSelectView(boolean showMenuSelectView) {
//        this.showMenuSelectView = showMenuSelectView;
//        return this;
//    }

    //构建
    public void build() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ExpandAdapter(context, showList, isMultiSelected);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
