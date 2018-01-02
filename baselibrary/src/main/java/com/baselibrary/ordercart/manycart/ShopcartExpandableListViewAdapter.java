package com.baselibrary.ordercart.manycart;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.baselibrary.R;
import com.baselibrary.utils.CollectionUtils;
import com.baselibrary.utils.ImageLoad;
import com.baselibrary.utils.ToastUtils;
import java.util.List;

/**
 * 多选购物车适配器
 */
public class ShopcartExpandableListViewAdapter extends BaseExpandableListAdapter {
    private List<GoodsBean> groups;
    private Context context;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private ImageLoad load;
    /**
     * 构造函数
     *
     * @param groups   组元素列表
     * @param context
     */
    public ShopcartExpandableListViewAdapter(List<GoodsBean> groups, Context context) {
        super();
        this.groups = groups;
        this.context = context;
        load=new ImageLoad(context);
    }

    //设置数据
    public void setData(List<GoodsBean> groups){
        this.groups = groups;
        notifyDataSetChanged();
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }


    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public int getGroupCount() {
        return !CollectionUtils.isNullOrEmpty(groups)?groups.size():0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return groups.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder gholder;
        if (convertView == null) {
            gholder = new GroupHolder();
            convertView = View.inflate(context, R.layout.shopname_item, null);
            gholder.cb_checkStoreAll =convertView.findViewById(R.id.cbshop);
            gholder.tv_storeName =convertView.findViewById(R.id.shopname);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupHolder) convertView.getTag();
        }
        final GoodsBean group = (GoodsBean) getGroup(groupPosition);
        if (group != null) {
            gholder.tv_storeName.setText(group.getStorename());
            gholder.cb_checkStoreAll.setOnClickListener(v -> {
                group.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());// 暴露组选接口
            });
            gholder.cb_checkStoreAll.setChecked(group.isChoosed());
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder cholder;
        if (convertView == null) {
            cholder = new ChildHolder();
            convertView = View.inflate(context, R.layout.shop_main, null);
            cholder.cb_product =convertView.findViewById(R.id.cbgoods);
            cholder.iv_shopcar_product =convertView.findViewById(R.id.ivgoods);
            cholder.tvgoodsconent = convertView.findViewById(R.id.tvgoodsconent);
            cholder.tvgoodsconent.setVisibility(View.VISIBLE);
            cholder.tv_product_name =convertView.findViewById(R.id.tvgoodsname);
            cholder.tv_product_price =convertView.findViewById(R.id.tvprice);
            cholder.iv_increase = convertView.findViewById(R.id.btn_add);
            cholder.iv_decrease = convertView.findViewById(R.id.btn_reduce);
            cholder.tv_product_number = convertView.findViewById(R.id.tv_goods_num);
            convertView.setTag(cholder);
        } else {
            cholder = (ChildHolder) convertView.getTag();
        }
        final GoodsBean.ListBean product = (GoodsBean.ListBean) getChild(groupPosition, childPosition);

        if (product != null) {
            load.loadCircleImage(product.getGoods_thumbnail(),cholder.iv_shopcar_product);
            cholder.tv_product_name.setText(product.getGoods_name());
            cholder.tv_product_price.setText("¥" + product.getCursprice());
            cholder.tv_product_number.setText(""+product.getNumber());
            cholder.tvgoodsconent.setText(product.getGoods_attr());
            cholder.cb_product.setChecked(product.getChecked());
            cholder.cb_product.setOnClickListener(v -> {
                product.setIs_choose(((CheckBox) v).isChecked());
                cholder.cb_product.setChecked(((CheckBox) v).isChecked());
                checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());// 暴露子选接口
            });
            }
        cholder.iv_increase.setOnClickListener(v -> {
             int num = product.getNumber();
            if (num<=45){
                modifyCountInterface.doIncrease(groupPosition, childPosition, cholder.tv_product_number, cholder.cb_product.isChecked(),product.getGoods_id(),product.getGoods_attr());// 暴露增加接口
            }else{
                ToastUtils.showShort("已超过最大商品数！");
            }
        });
        final int num = product.getNumber();
        cholder.iv_decrease.setOnClickListener(v -> {
            if (num <= 1) {
                ToastUtils.showShort("亲，宝贝不能再少了！");
            } else {
                modifyCountInterface.doDecrease(groupPosition, childPosition, cholder.tv_product_number, cholder.cb_product.isChecked(),product.getGoods_id(),product.getGoods_attr());// 暴露删减接口
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 组元素绑定器
     */
    private class GroupHolder {
        CheckBox cb_checkStoreAll;
        TextView tv_storeName;
    }

    /**
     * 子元素绑定器
     */
    private class ChildHolder {
        CheckBox cb_product;
        ImageView iv_shopcar_product;
        TextView tv_product_name;
        TextView tv_product_price;
        TextView iv_increase;
        TextView tv_product_number;
        TextView iv_decrease;
        TextView tvgoodsconent;
    }

    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        public void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        public void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    public interface DeleteInterface {


        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         */
         void delete(int groupPosition, int childPosition);
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *  @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         * @param goods_id
         * @param goods_attr
         */
         void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked, int goods_id, String goods_attr);

        /**
         * 删减操作
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         * @param gids
         * @param goods_attr
         */
         void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked, int gids, String goods_attr);
    }

}
