package com.hisan.yyq.ui;
import com.baselibrary.down.DownUtils;
import com.baselibrary.ui.base.BaseActivity;
import com.hisan.yyq.R;
import com.hisan.yyq.databinding.DownBinding;

/**
 * 创建时间 : 2017/12/23
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：下载页面
 */
public class DownActivity extends BaseActivity<DownBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.down;
    }

    @Override
    protected void initView() {
        show_Hide_ModuleTitle("下载");
        mBinding.down.setOnClickListener(v -> {
            DownUtils.DownApk(this,"http://freeride.oss-cn-qingdao.aliyuncs.com/install/1.2.4.apk");
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData(boolean isRefresh) {

    }

    @Override
    public void requestPermissionResult(boolean allowPermission) {

    }

    /*private int getData(int a){
        int temp = 1;
        for(int i = 1;i<=a;i++){
            temp = temp*i;
        }
        return temp;
    }

    private int getD(int a){

        return a == 0 ? 1 a * getD(a - 1);
    }*/
}
