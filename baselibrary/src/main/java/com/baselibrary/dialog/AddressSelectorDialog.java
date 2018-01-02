package com.baselibrary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.baselibrary.R;
import com.baselibrary.ui.model.RegionJson;
import com.baselibrary.utils.GsonUtils;
import com.baselibrary.utils.Log;
import com.baselibrary.utils.utlis.FileUtils;
import com.baselibrary.view.WheelView;
import com.lzh.easythread.Callback;
import com.lzh.easythread.EasyThread;

import java.util.ArrayList;
import java.util.List;


/**
 * 创建时间 : 2017/12/23
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：城市弹窗+滚轮
 */
public class AddressSelectorDialog extends AddressDialog implements View.OnClickListener {
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Context mContext;
    private TextView tvCancel;
    private TextView tvConfirm;
    EasyThread executor = null;

    public AddressSelectorDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public AddressSelectorDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_address);
        setUpViews();
        setUpListener();
    }

    private void setUpViews() {
        mViewProvince =findViewById(R.id.id_province);
        mViewCity = findViewById(R.id.id_city);
        mViewDistrict = findViewById(R.id.id_district);
        tvCancel = findViewById(R.id.btn_cancel);
        tvConfirm =findViewById(R.id.btn_confirm);
        initProvinceSelectView();
    }

    private void setUpListener() {
        // 取消和确定
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel) {
            if (null != listener) {
                listener.onCanceled();
            }
        } else if (i == R.id.btn_confirm) {
            if (null != listener) {
                listener.onConfirmed(datas,mCurrentProvinceName, mCurrentCityName, mCurrentDistrictName);
            }
        }
    }

    private void initProvinceSelectView() {
        mViewProvince.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                String provinceText = mProvinceList.get(id);
                if (!mCurrentProvinceName.equals(provinceText)) {
                    mCurrentProvinceName = provinceText;
                    ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProvinceName);
                    mViewCity.resetData(mCityData);
                    mViewCity.setDefault(0);
                    mCurrentCityName = mCityData.get(0);
                    ArrayList<String> mDistrictData = mAreaDatasMap.get(mCurrentCityName);
                    mViewDistrict.resetData(mDistrictData);
                    mViewDistrict.setDefault(0);
                    mCurrentDistrictName = mDistrictData.get(0);
                }
            }
            @Override
            public void selecting(int id, String text) {}
        });

        mViewCity.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProvinceName);
                String cityText = mCityData.get(id);
                if (!mCurrentCityName.equals(cityText)) {
                    mCurrentCityName = cityText;
                    ArrayList<String> mCountyData = mAreaDatasMap.get(mCurrentCityName);
                    mViewDistrict.resetData(mCountyData);
                    mViewDistrict.setDefault(0);
                    mCurrentDistrictName = mCountyData.get(0);
                }
            }
            @Override
            public void selecting(int id, String text) {}
        });

        mViewDistrict.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                ArrayList<String> mDistrictData = mAreaDatasMap.get(mCurrentCityName);
                String districtText = mDistrictData.get(id);
                if (!mCurrentDistrictName.equals(districtText)) {
                    mCurrentDistrictName = districtText;
                }
            }
            @Override
            public void selecting(int id, String text) {}
        });
        getData();
    }

    private void getData() {
                executor = EasyThread.Builder
                .fixed(2)
                .priority(Thread.MAX_PRIORITY)
                .name("thread name")
                .build();
        executor.name("city").callback(new ThreadCallback())
                .execute(() -> {
                    // 读取数据
                    initProvinceDatas(mContext);
                });
    }

        private class ThreadCallback implements Callback {
        @Override
        public void onError(Thread thread, Throwable t) {
            Log.d( String.format("线程%s运行出现异常，异常信息为：%s", thread, t.getMessage()));
        }

        @Override
        public void onCompleted(Thread thread) {
            setDefaultData();
        }

        @Override
        public void onStart(Thread thread) {}
    }

    private void setDefaultData() {
        mViewProvince.setData(mProvinceList);
        mViewProvince.setDefault(0);
        mCurrentProvinceName = mProvinceList.get(0);
        ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProvinceName);
        mViewCity.setData(mCityData);
        mViewCity.setDefault(0);
        mCurrentCityName = mCityData.get(0);
        ArrayList<String> mDistrictData = mAreaDatasMap.get(mCurrentCityName);
        mViewDistrict.setData(mDistrictData);
        mViewDistrict.setDefault(0);
        mCurrentDistrictName = mDistrictData.get(0);
    }
    public WheelView getmViewProvince() {
        return mViewProvince;
    }
    public WheelView getmViewCity() {
        return mViewCity;
    }
    public WheelView getmViewDistrict() {
        return mViewDistrict;
    }
}
