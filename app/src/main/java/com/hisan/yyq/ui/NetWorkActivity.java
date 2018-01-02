package com.hisan.yyq.ui;


import com.baselibrary.network.OkGoUtlis;
import com.baselibrary.ui.base.BaseActivity;
import com.baselibrary.ui.listener.PermissionListener;
import com.baselibrary.utils.GsonUtils;
import com.hisan.yyq.R;
import com.hisan.yyq.databinding.NetworkBinding;
import com.lzy.okgo.model.HttpParams;
import java.util.List;

/**
 * 创建时间 : 2017/12/5
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：自定义网络
 */
public class NetWorkActivity extends BaseActivity<NetworkBinding> {
    static final String[] needContactsPermissions = new String[]{PermissionListener.INTERNET};

    @Override
    protected int getLayoutId() {
        return R.layout.network;
    }
    @Override
    protected void initView() {
        show_Hide_ModuleTitle("自定义网络框架");
        requestPermission();
    }

    @Override
    protected void initEvent() {
        mBinding.get.setOnClickListener(v -> OkGoUtlis.getInstance().getmData(handler,0,0,"http://freeride.0951yh.com/app/v1/data/city",null,NetWorkActivity.this));
        mBinding.post.setOnClickListener(v -> {
            HttpParams httpParams=new HttpParams();
            httpParams.put("mobile","13095605272");
            httpParams.put("mobile","123456");
            OkGoUtlis.getInstance().getmData(handler,1,1,"http://freeride.0951yh.com/app/v1/login",httpParams,NetWorkActivity.this);
        });
        mBinding.delete.setOnClickListener(v -> OkGoUtlis.getInstance().getmData(handler,2,2,"http://freeride.0951yh.com/app/v1/passenger/delete",null,NetWorkActivity.this));
        mBinding.put.setOnClickListener(v -> OkGoUtlis.getInstance().getmData(handler,3,3,"http://freeride.0951yh.com/app/v1/trip/booking/cancel",null,NetWorkActivity.this));
    }

    @Override
    protected void loadData(boolean isRefresh) {}



    @Override
    public void getData(int id, Object data) {
        switch (id){
            case 0:
                String udata= GsonUtils.GsonString(data);
                List<CityModel >cityModels= GsonUtils.jsonToList(udata,CityModel.class);
                for (CityModel cityModel:cityModels){
                    showToast(cityModel.toString());
                }
                return;
            case 1:
                showToast("登入成功:"+data);
                return;
            case 2:
                showToast("删除成功:"+data);
                return;
            case 3:
                showToast("put成功:"+data);
                return;
        }
    }


    private void requestPermission() {
        if (!mayRequestPermission(needContactsPermissions)) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }
    @Override
    public void requestPermissionResult(boolean allowPermission) {
        if (allowPermission) {
            getLoaderManager().initLoader(0, null, this);
        }
    }


}
