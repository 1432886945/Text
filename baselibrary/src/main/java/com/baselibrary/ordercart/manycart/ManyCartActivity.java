package com.baselibrary.ordercart.manycart;

import android.view.View;

import com.baselibrary.R;
import com.baselibrary.databinding.OrderCartBinding;
import com.baselibrary.network.OkGoUtlis;
import com.baselibrary.ui.base.BaseActivity;
import com.baselibrary.utils.GsonUtils;
import com.baselibrary.utils.ListUtils;
import com.baselibrary.utils.Log;
import com.baselibrary.utils.ToastUtils;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 创建时间 : 2017/12/26
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：多家商铺购物车
 */
public class ManyCartActivity extends BaseActivity<OrderCartBinding> {
    private ShopcartExpandableListViewAdapter adapter;
    private List<GoodsBean> shopBeanList;
    //商铺总数
    private int ZONGSHU = 0;
    //总价
    private double ZONGJINE = 0.0;
    //购物车id
    private List<String> gidlist=new LinkedList<>();
    //添加、减少商品标识
    private String msg="";
    private boolean isedit=true;//显示是否在编辑
    @Override
    protected int getLayoutId() {
        return R.layout.order_cart;
    }

    @Override
    protected void initView() {
        mHelper.showLoadingView();
        show_Hide_ModuleTitle("购物车");
        hideTopRightText("编辑");
        title_right_text.setTextColor(getResources().getColor(R.color.red));
        mBinding.btnPay.setBackgroundColor(getResources().getColor(R.color.order));
    }

    @Override
    protected void initEvent() {
        mBinding.cballChoose.setOnClickListener(v -> {
            for (int i = 0; i < shopBeanList.size(); i++) {
                shopBeanList.get(i).setChoosed(mBinding.cballChoose.isChecked());
                GoodsBean group = shopBeanList.get(i);
                for (int j = 0; j < group.getList().size(); j++) {
                    List<GoodsBean.ListBean> listBean=group.getList();
                    listBean.get(j).setIs_choose(mBinding.cballChoose.isChecked());
                }
            }
            adapter.notifyDataSetChanged();
            calculate();
        });
        getRight_relative_layout().setOnClickListener(v -> {
            if (isedit){
                title_right_text.setText("完成");
                mBinding.orderCartPliner.setVisibility(View.INVISIBLE);
                mBinding.btnPay.setBackgroundColor(getResources().getColor(R.color.red));
                isedit=false;
                mBinding.btnPay.setText("删除");
            }else{
                title_right_text.setText("编辑");
                mBinding.btnPay.setBackgroundColor(getResources().getColor(R.color.order));
                mBinding.orderCartPliner.setVisibility(View.VISIBLE);
                if (ZONGSHU<=0){
                    ZONGSHU=0;
                }
                calculate();
                mBinding.tvtotalMoney.setText("");
                mBinding.btnPay.setText("结算");
                isedit=true;
            }
        });
        mBinding.btnPay.setOnClickListener(v -> {
            String btname=mBinding.btnPay.getText().toString();
            if(ZONGJINE>0){
                if (btname.equals("删除")){
                    ToastUtils.showShort("删除");

                }else {
                    ToastUtils.showShort("下单");
                }
            }else {
                ToastUtils.showShort("当前暂无选择商品");
            }
        });
    }

    @Override
    protected void loadData(boolean isRefresh) {
        shopBeanList=new LinkedList<>();
        HttpParams httpParams=new HttpParams();
        httpParams.put("uid",441);
        httpParams.put("token","bd0ccb296a4115ea3de5e375645b305c");
        OkGoUtlis.getInstance().getmData(handler,0,0,"http://www.shehuizhue.com/app/mall/getshopcart",httpParams,this);
    }

    @Override
    public void getData(int id, Object ndata) {
        switch (id){
            case 0:
                try {
                    String mdata= GsonUtils.GsonString(ndata);
                    JSONObject response=new JSONObject(mdata);
                    String  status = response.getString("status");
                    if (status.equals("1.0")){
                        JSONArray data=response.getJSONArray("data");
                        if (data.length()>0){
                            JSONArray josn = null;
                            GoodsBean goodsBean=new GoodsBean();
                            List<GoodsBean.ListBean> listBeens=new ArrayList<>();
                            for (int i=0;i<data.length();i++){
                                JSONObject jdata=data.getJSONObject(i);
                                String storename=jdata.getString("storename");
                                goodsBean.setStorename(storename);
                                for (int j=0;j<jdata.length();j++){
                                    josn=jdata.getJSONArray("list");
                                }
                                for (int a=0;a<josn.length();a++){
                                    JSONObject object=josn.getJSONObject(a);
                                    GoodsBean.ListBean  listBean = GsonUtils.GsonToBean(object.toString(),GoodsBean.ListBean.class);
                                    listBeens.add(listBean);
                                }
                            }
                            goodsBean.setList(listBeens);
                            shopBeanList.add(goodsBean);

                            adapter=new ShopcartExpandableListViewAdapter(shopBeanList,this);
                            mBinding.expandablelistview.setAdapter(adapter);
                            for (int i = 0; i < adapter.getGroupCount(); i++) {
                                mBinding.expandablelistview.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
                            }
                            //ExpandableListView不会折叠
                            mBinding.expandablelistview.setOnGroupClickListener((expandableListView, view, i, l) -> true);
                            //增加,减少
                            adapter.setModifyCountInterface(new ShopcartExpandableListViewAdapter.ModifyCountInterface() {
                                @Override
                                public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked, int goods_id, String goods_attr) {
                                    if(goods_id!=0) {
                                        gidlist.add(goods_id+"");
                                    }
                                    if (goods_id>0){
                                        UpDateCart(0,1,goods_id, "+", goods_attr);
                                    }
                                }
                                @Override
                                public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked, int gids, String goods_attr) {
                                    if (gidlist.size()>0) ListUtils.deleteid(gidlist,gids+"");
                                    UpDateCart(1,1,gids,"-",goods_attr);
                                }
                            });
                            adapter.setCheckInterface(new ShopcartExpandableListViewAdapter.CheckInterface() {
                                @Override
                                public void checkGroup(int groupPosition, boolean isChecked) {
                                    for (int i = 0; i < shopBeanList.size(); i++) {
                                        shopBeanList.get(i).setChoosed(isChecked);
                                        GoodsBean group = shopBeanList.get(i);
                                        for (int j = 0; j < group.getList().size(); j++) {
                                            List<GoodsBean.ListBean> listBean=group.getList();
                                            listBean.get(j).setIs_choose(isChecked);
                                        }
                                    }
                                    if (isAllCheck()) mBinding.cballChoose.setChecked(true);
                                    else mBinding.cballChoose.setChecked(false);
                                    adapter.notifyDataSetChanged();
                                    calculate();
                                }

                                @Override
                                public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
                                    boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
                                    GoodsBean group = shopBeanList.get(groupPosition);
                                    for (int j = 0; j < group.getList().size(); j++) {
                                        List<GoodsBean.ListBean> listBean=group.getList();
                                        for (int i = 0; i < listBean.size(); i++) {
                                            if (listBean.get(i).getChecked() != isChecked) {
                                                allChildSameState = false;
                                                break;
                                            }
                                        }
                                    }

                                    if (allChildSameState) {
                                        group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
                                    } else {
                                        group.setChoosed(false);// 否则，组元素一律设置为未选中状态
                                    }

                                    if (isAllCheck())
                                        mBinding.cballChoose.setChecked(true);
                                    else
                                        mBinding.cballChoose.setChecked(false);
                                    adapter.notifyDataSetChanged();
                                    calculate();
                                }
                            });
                            mHelper.showDataView();
                        }else{
                            mHelper.showEmptyView();
                        }
                    }else{
                        mHelper.showErrorView();
                    }
                } catch (Exception e) {}
                break;
            case 1:
                if (msg.equals("-")){
                    ZONGSHU--;
                    if (ZONGSHU<=0){
                        ZONGSHU=0;
                    }
                }
                mBinding.cballChoose.setChecked(false);
                mBinding.tvtotalMoney.setText("￥0");
                mBinding.btnPay.setText("结算");
                loadData(true);
                break;
        }
    }

    /**
     * 判断是否都被未被选中
     */
    private boolean isAllCheck() {
        for (GoodsBean group : shopBeanList) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }
    /**
     * 统计操作
     */
    private void calculate() {
        ZONGJINE = 0.0;
        ZONGSHU = 0;
        for (int i = 0; i <shopBeanList .size(); i++) {
            GoodsBean group = shopBeanList.get(i);
            for (int j = 0; j < group.getList().size(); j++) {
                GoodsBean.ListBean listBean = group.getList().get(j);
                if (listBean.getChecked()) {
                    if(listBean.getId()!=0) {
                        gidlist.add(listBean.getId()+"");
                    }
                    ZONGSHU++;
                    ZONGJINE += listBean.getGoodsTotalPrice();
                }else {
                    Log.d(gidlist.size());
                    if (gidlist.size()>0) {
                        ListUtils.deleteid(gidlist,listBean.getId()+"");
                    }
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

    @Override
    public void requestPermissionResult(boolean allowPermission) {}
    /**
     * 更新购物车
     * @param
     * @param msg
     *
     */
    private void UpDateCart(int type, int number, int gid, final String msg, final String attrs) {
        this.msg=msg;
        HttpParams httpParams=new HttpParams();
        httpParams.put("uid",441);
        httpParams.put("token","bd0ccb296a4115ea3de5e375645b305c");
        httpParams.put("type",type);
        httpParams.put("goods_id",gid);
        httpParams.put("goods_attr",attrs);
        httpParams.put("number",number);
        OkGoUtlis.getInstance().getmData(handler,1,0,"http://www.shehuizhue.com/app/mall/updateshopcart",httpParams,this);
    }
}
