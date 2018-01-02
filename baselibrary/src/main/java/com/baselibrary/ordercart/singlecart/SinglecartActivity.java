package com.baselibrary.ordercart.singlecart;

import android.support.v7.widget.LinearLayoutManager;

import com.baselibrary.R;
import com.baselibrary.databinding.SinglecartBinding;
import com.baselibrary.databinding.SinglecartItemBinding;
import com.baselibrary.network.OkGoUtlis;
import com.baselibrary.ordercart.manycart.GoodsBean;
import com.baselibrary.ui.base.BaseActivity;
import com.baselibrary.utils.CollectionUtils;
import com.baselibrary.utils.GsonUtils;
import com.baselibrary.utils.ImageLoad;
import com.baselibrary.utils.ListUtils;
import com.baselibrary.utils.Log;
import com.baselibrary.utils.ToastUtils;
import com.lzy.okgo.model.HttpParams;
import com.mcxtzhang.commonadapter.databinding.rv.BaseBindingAdapter;
import com.mcxtzhang.commonadapter.databinding.rv.BaseBindingVH;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间 : 2017/12/26
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：单家商铺购物车
 */
public class SinglecartActivity extends BaseActivity<SinglecartBinding>{
    private BaseBindingAdapter mAdapter;
    private List<CartBean> cartBeans;
    private double ZONGJINE = 0.0;
    private SinglecartItemBinding binding;

    private List<String> gidlist=new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.singlecart;
    }

    @Override
    protected void initView() {
        show_Hide_ModuleTitle("单商铺商城");
        mBinding.rv.setLayoutManager(new LinearLayoutManager(this));
        hideTopRightText("编辑");
        title_right_text.setTextColor(getResources().getColor(R.color.red));
    }

    @Override
    protected void initEvent() {
        mBinding.cartChoose.setOnClickListener(v -> {
            for (int i = 0; i < cartBeans.size(); i++) {
                cartBeans.get(i).setIs_choose(mBinding.cartChoose.isChecked());
            }
            mAdapter.notifyDataSetChanged();
            calculate();
        });

    }

    @Override
    protected void loadData(boolean isRefresh) {
        HttpParams httpParams=new HttpParams();
        httpParams.put("access_token","93943e205836324085c10f49ac8fa8fb");
        OkGoUtlis.getInstance().getmData(handler,0,0," http://api.zry8181.com/api/v1/shopcartlist",httpParams,this);
    }

    @Override
    public void requestPermissionResult(boolean allowPermission) {}

    @Override
    public void getData(int id, Object mdata) {
        if (!CollectionUtils.isNullOrEmpty(mdata)){
            String data=GsonUtils.GsonString(mdata);
            cartBeans= GsonUtils.jsonToList(data,CartBean.class);
            mAdapter=new BaseBindingAdapter<CartBean, SinglecartItemBinding>(this,cartBeans,R.layout.singlecart_item) {
                @Override
                public void onBindViewHolder(BaseBindingVH<SinglecartItemBinding> holder, int position) {
                    super.onBindViewHolder(holder, position);
                    binding = holder.getBinding();
                    CartBean cartBean = cartBeans.get(position);
                    new ImageLoad(getApplicationContext()).loadUrlImage( cartBean.getThumbnail(), binding.ivgoods);
                    holder.getBinding().setPosition(holder.getLayoutPosition());
                }
            };
            //设置Item点击事件
            mAdapter.setItemPresenter(new SingleItemPresenter());
            mBinding.rv.setAdapter(mAdapter);
        }
    }

    /**
     * Item点击事件P
     */
    public class SingleItemPresenter {
        public void onItemClick(CartBean data, int position) {
            ToastUtils.showShort("item点击");

        }
        //add监听
        public void onAddClick(CartBean data, int position) {
            ToastUtils.showShort("添加");
        }
        //delete监听
        public void onDelClick(CartBean data, int position) {
            ToastUtils.showShort("删除");
        }
        //单选监听
        public void onCheckBox(CartBean data, int position) {
            //添加
            if (!data.getChecked()) {
                data.setIs_choose(true);
                //减少
            } else {
                data.setIs_choose(false);
            }
            calculate();
            mBinding.cartChoose.setChecked(isAllCheck());
        }
    }
    /**
     * 判断是否都被未被选中
     */
    private boolean isAllCheck() {
        for (CartBean group : cartBeans) {
            if (!group.is_choose())
                return false;
        }
        return true;
    }
    /**
     * 统计操作
     */
    private void calculate() {
        ZONGJINE = 0.0;
        for (int i = 0; i <cartBeans .size(); i++) {
            CartBean group = cartBeans.get(i);
            if (group.getChecked()) {
                if(group.getId()!=0) {
                    gidlist.add(group.getId()+"");
                }
                ZONGJINE += group.getGoodsTotalPrice();
            }else {
                Log.d(gidlist.size());
                if (gidlist.size()>0) {
                    ListUtils.deleteid(gidlist,group.getId()+"");
                }
            }
        }
        String btname=mBinding.btnPay.getText().toString();
        if (btname.equals("删除")){
            mBinding.btnPay.setText("删除");
        }else{
            mBinding.btnPay.setText("结算");
            mBinding.tvtotalMoney.setText("￥"+String.format("%.2f", ZONGJINE));
        };
    }


}
