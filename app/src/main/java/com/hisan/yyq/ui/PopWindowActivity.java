package com.hisan.yyq.ui;
import com.baselibrary.dropdown.FilterUrl;
import com.baselibrary.dropdown.interfaces.OnFilterDoneListener;
import com.baselibrary.ui.base.BaseActivity;

import com.baselibrary.utils.ToastUtils;
import com.hisan.yyq.R;
import com.hisan.yyq.databinding.PopwindowBinding;

/**
 * 创建时间 : 2017/12/23
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：顶部三级弹窗
 */
public class PopWindowActivity extends BaseActivity<PopwindowBinding> implements OnFilterDoneListener {
    @Override
    protected int getLayoutId() {
        return R.layout.popwindow;
    }

    @Override
    protected void initView() {
        String[] titleList = new String[]{"第一个", "第二个", "第三个", "第四个"};
        mBinding.dropDownMenu.setMenuAdapter(new DropMenuAdapter(this, titleList, this));
    }

    @Override
    protected void initEvent() {}

    @Override
    protected void loadData(boolean isRefresh) {}

    @Override
    public void requestPermissionResult(boolean allowPermission) {}

    @Override
    public void onFilterDone(int position, String positionTitle, String urlValue) {
        if (position != 3) {
            mBinding.dropDownMenu.setPositionIndicatorText(FilterUrl.instance().position, FilterUrl.instance().positionTitle);
        }
        mBinding.dropDownMenu.close();
        ToastUtils.showShort(FilterUrl.instance().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FilterUrl.instance().clear();
    }
}
