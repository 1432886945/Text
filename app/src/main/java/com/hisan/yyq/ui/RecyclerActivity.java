package com.hisan.yyq.ui;




import com.baselibrary.multilist.ExpandBean;
import com.baselibrary.multilist.ExpandRecyclerHelper;
import com.baselibrary.ui.base.BaseActivity;
import com.hisan.yyq.R;
import com.hisan.yyq.databinding.RecyclerBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * 三级列表
 */
public class RecyclerActivity extends BaseActivity<RecyclerBinding> {
    private List<ExpandBean> zxExpandBeans=new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.recycler;
    }

    @Override
    protected void initView() {
        show_Hide_ModuleTitle("三级列表");
        for (int i=0;i<5;i++){
            ExpandBean zxExpandBean1=new ExpandBean(null,i+"");
            List<ExpandBean> datalist1=new ArrayList<>();
            for (int j=0;j<3;j++){
                ExpandBean zxExpandBean2=new ExpandBean(null,j+"");
                List<ExpandBean> datalist2=new ArrayList<>();
                for (int k=0;k<3;k++){
                    ExpandBean zxExpandBean3=new ExpandBean(null,k+"");
                    datalist2.add(zxExpandBean3);
                }
                zxExpandBean2.setChildList(datalist2);
                datalist1.add(zxExpandBean2);
            }
            zxExpandBean1.setChildList(datalist1);
            zxExpandBeans.add(zxExpandBean1);
        }
        ExpandRecyclerHelper.getInstance(this)
                .withRecycler(mBinding.recy)
                .setData(zxExpandBeans)
                .multiSelected(true)
                .build();
    }


    @Override
    protected void initEvent() {}

    @Override
    protected void loadData(boolean isRefresh) {}

    @Override
    public void requestPermissionResult(boolean allowPermission) {}
}
