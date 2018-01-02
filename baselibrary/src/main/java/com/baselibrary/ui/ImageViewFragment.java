package com.baselibrary.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.baselibrary.R;
import com.baselibrary.databinding.FragmentImageviewBinding;
import com.baselibrary.ui.base.BaseFragment;
import com.baselibrary.utils.ConvertUtils;
import com.baselibrary.utils.ImageUtils;

/**
 * Created by miao on 2017/6/29.
 */
public class ImageViewFragment extends BaseFragment<FragmentImageviewBinding>{
    private  String url="";
    public static ImageViewFragment newInstance(String item){
        ImageViewFragment fragment=new ImageViewFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url",item);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected void initData(Bundle bundle) {
         url = bundle.getString("url");
        if(url==null){
            url="";
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_imageview;
    }

    @Override
    protected void initView() {
        int screenWidth = ConvertUtils.getWidth(getActivity());
        Bitmap diskBitmap = ImageUtils.getBitmap(url);
        if (diskBitmap!=null){
            //获取Bitmap的宽高
            int width = diskBitmap.getWidth();
            int height = diskBitmap.getHeight();
            //根据Bitmap的宽高和屏幕的宽高重新设置imageview显示的大小
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mBinding.image.getLayoutParams();
            layoutParams.width=screenWidth;
            layoutParams.height=screenWidth*height/width;
            mBinding.image.setLayoutParams(layoutParams);
            mBinding.image.setImage(diskBitmap);
        }
    }
    @Override
    protected void initEvent() {}
    @Override
    protected void loadData(boolean isRefresh) {}

}
