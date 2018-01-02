package com.baselibrary.dialog;

import android.content.Context;

import com.baselibrary.ui.model.RegionJson;
import com.baselibrary.utils.GsonUtils;
import com.baselibrary.utils.utlis.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建时间 : 2017/12/23
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：城市弹窗
 */
public class AddressDialog extends BaseDialog {

    /**
     * 省
     */
    protected ArrayList<String> mProvinceList=new ArrayList<>();
    /**
     * key - 省 value - 市
     */
    protected Map<String,ArrayList<String>> mCitisDatasMap = new HashMap<String, ArrayList<String>>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, ArrayList<String>> mAreaDatasMap = new HashMap<String, ArrayList<String>>();
    /**
     * 省市区模型
     */
    protected List<RegionJson> datas=new ArrayList<>();
    /**
     * 当前省份
     */
    protected String mCurrentProvinceName;
    /**
     * 当前城市
     */
    protected String mCurrentCityName;
    /**
     * 当前区
     */
    protected String mCurrentDistrictName = "";
    public OnAddressChangedListener listener;

    public AddressDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public AddressDialog(Context context) {
        super(context);
    }
    /**
     * 初始化数据
     */

    protected void initProvinceDatas(Context context) {
        //读取文件
        String str = FileUtils.readAssetsFile(context,"china.json");
        datas = GsonUtils.jsonToList(str,RegionJson.class);
        initAddress(datas);
    }
    private void initAddress(List<RegionJson> datas) {
        for(RegionJson data : datas) {
            //省
            mProvinceList.add(data.name);
            ArrayList<String> mCitysList = new ArrayList<>();
            for(RegionJson.ChildEntity city : data.children) {
                //市
                mCitysList.add(city.name);
                ArrayList<String> mAreaList = new ArrayList<>();
                for(RegionJson.ChildEntity.ChildEntity2 area : city.children) {
                    //区
                    mAreaList.add(area.name);
                }
                //市-区对该
                mAreaDatasMap.put(city.name,mAreaList);
            }
            //省支市对应
            mCitisDatasMap.put(data.name,mCitysList);
        }
    }
    public interface OnAddressChangedListener {
        void onCanceled();

        void onConfirmed(List<RegionJson> datas, String currentProvinceName, String currentCityName, String currentDistrictName);
    }

    public void setOnAddressChangedListener(OnAddressChangedListener listener) {
        this.listener = listener;
    }
}
