package com.hisan.yyq.ui;
import com.baselibrary.dialog.AddressDialog;
import com.baselibrary.dialog.AddressSelectorDialog;
import com.baselibrary.ui.base.BaseActivity;
import com.baselibrary.ui.model.RegionJson;
import com.hisan.yyq.R;
import com.hisan.yyq.databinding.WheelBinding;

import java.util.List;

/**
 * 创建时间 : 2017/12/22
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：滚轮页面
 */
public class WheelActivity extends BaseActivity<WheelBinding>{
    private AddressSelectorDialog addressSelectorDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.wheel;
    }

    @Override
    protected void initView() {
        show_Hide_ModuleTitle("城市联动");
        if (addressSelectorDialog == null) {
            addressSelectorDialog = new AddressSelectorDialog(this);
        }
        addressSelectorDialog.setOnAddressChangedListener(new AddressDialog.OnAddressChangedListener() {
            @Override
            public void onCanceled() {
                addressSelectorDialog.dismiss();
            }
            @Override
            public void onConfirmed(List<RegionJson> datas, String currentProvinceName, String currentCityName, String currentDistrictName) {
                int pid=datas.get(addressSelectorDialog.getmViewProvince().getSelected()).id;
                int cid=datas.get(addressSelectorDialog.getmViewProvince().getSelected()).children.get(addressSelectorDialog.getmViewCity().getSelected()).id;
                int aid=datas.get(addressSelectorDialog.getmViewProvince().getSelected()).
                        children.get(addressSelectorDialog.getmViewCity().getSelected()).
                        children.get(addressSelectorDialog.getmViewDistrict().getSelected()).id;
                mBinding.name.setText(currentProvinceName +"-"+currentCityName +"-"+currentDistrictName+" 城市Id:"+pid+":"+cid+":"+aid);
                addressSelectorDialog.dismiss();
            }
        });
    }

    @Override
    protected void initEvent() {
        mBinding.city.setOnClickListener(v -> addressSelectorDialog.show());
    }

    @Override
    protected void loadData(boolean isRefresh) {}

    @Override
    public void requestPermissionResult(boolean allowPermission) {}
}
