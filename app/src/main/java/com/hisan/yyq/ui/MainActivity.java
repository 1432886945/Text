package com.hisan.yyq.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;

import com.baselibrary.album.PreViewActivity;
import com.baselibrary.album.SelectImageActivity;
import com.baselibrary.permission.PermissionHelper;
import com.baselibrary.ui.adapter.RequestAdapter;
import com.baselibrary.ui.base.BaseActivity;
import com.baselibrary.ui.listener.PermissionListener;
import com.baselibrary.utils.CollectionUtils;
import com.baselibrary.utils.CompressionUtils;
import com.baselibrary.utils.utlis.AppUtils;
import com.hisan.yyq.R;
import com.hisan.yyq.album.ImageSelector;
import com.hisan.yyq.databinding.ActivityMainBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private final int SELECT_IMAGE_REQUEST = 0x0011;
    private ArrayList<String> mImageList = new ArrayList<>();
    private ArrayList<String> mRuquestList = new ArrayList<>();
    private RequestAdapter adapter;
    static final String[] needContactsPermissions = new String[]{PermissionListener.WRITE, PermissionListener.READ};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        show_Hide_ModuleTitle("自定义图片选择");
        adapter = new RequestAdapter(this, mImageList, 9);
        mBinding.rv.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        mBinding.rv.setAdapter(adapter);
        requestPermission();
    }

    @Override
    protected void initEvent() {
        adapter.setOnDeleteClickListener((obj, position) -> removeList(position));
        mBinding.onclick.setOnClickListener(v -> tvCompress());
        adapter.setOnItemClickListener((obj, position) -> {
            if (mImageList.size() == position) {
                toSelectImageActivity();
            } else {
                //跳转至删除或者预览页面
                Intent intent = new Intent(MainActivity.this, PreViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("choicePosition", position);
                bundle.putSerializable("choiceList", mImageList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData(boolean isRefresh) {}



    /**
     * 删除图片
     *
     * @param position
     */
    private void removeList(int position) {
        mImageList.remove(position);
        adapter.nodfiyData(mImageList);
    }
    private void toSelectImageActivity() {
        ImageSelector.Companion
                .create()
                .multi()
                .count(9)
                .showCamera(true)
                .origin(mImageList)
                .start(this, SELECT_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_REQUEST && data != null) {
                mImageList = (ArrayList<String>) data.getSerializableExtra(SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST);
                  adapter.nodfiyData(mImageList);
            }
        }
    }

    /**
     * 压缩点击事件
     */
    public void tvCompress() {
        if (mRuquestList.size() != 0) {
            mRuquestList.clear();
        }
        CompressionUtils.setData(handler,this,mImageList);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.requestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void getData(int id, Object data) {
        if (!CollectionUtils.isNullOrEmpty(data)) {
            List<File> files = (List<File>) data;
            for (String path : mImageList) {
                int[] originSize = computeSize(path);
                String originArg = String.format(Locale.CHINA, "原图参数：%d*%d, %dk", originSize[0], originSize[1], new File(path).length() >> 10);
                showToast(originArg);
            }
            for (File file1 : files) {
                int[] thumbSize = computeSize(file1.getAbsolutePath());
                String thumbArg = String.format(Locale.CHINA, "压缩后参数：%d*%d, %dk", thumbSize[0], thumbSize[1], file1.length() >> 10);
                showToast(thumbArg);
            }
        }
    }



    private int[] computeSize(String srcImg) {
        int[] size = new int[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(srcImg, options);
        size[0] = options.outWidth;
        size[1] = options.outHeight;
        return size;
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
