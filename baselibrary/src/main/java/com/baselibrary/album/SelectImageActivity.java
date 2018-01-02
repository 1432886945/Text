package com.baselibrary.album;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baselibrary.R;
import com.baselibrary.permission.PermissionHelper;
import com.baselibrary.permission.PermissionSuccess;
import com.baselibrary.ui.adapter.SelectImageAdapter;
import com.baselibrary.utils.utlis.AppUtils;
import com.baselibrary.view.recyclerview.DividerGridItemDecoration;


import java.io.File;
import java.util.ArrayList;


public class SelectImageActivity extends AppCompatActivity implements View.OnClickListener{
    //相机
    public static final String EXTRA_SHOW_CAMERA = "EXTRA_SHOW_CAMERA";
    //选择张数
    public static final String EXTRA_SELECT_COUNT = "EXTRA_SELECT_COUNT";
    //原始的图片路径
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "EXTRA_DEFAULT_SELECTED_LIST";
    //选择的模式
    public static final String EXTRA_SELECT_MODE = "EXTRA_SELECT_MODE";
//    //返回选择图片的列表
//    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    //多选
    public static final int MODE_MULTI = 0x0011;
    //单选
    public static final int MODE_SINGLE = 0x0012;
    private static final int LOADER_TYPE = 0x0021;
    private static final int REQUEST_CAMERA = 0x0022;
    private static final int PHOTO_CAMERA = 1002;
//    private static final int PREVIEW_CODE = 1003;
    //单选或多选
    private int mMode = MODE_MULTI;
    //选择图片的张数
    private int mMaxCount = 9;
    //是否显示拍照按钮
    private boolean mShowCamera = true;
    //选择图片的list
    private ArrayList<String> mResultList;
    private ImageView btnBack;
    private RecyclerView imageListRv;
    //预览
    private TextView selectPreview;
    private TextView selectNum;
    private TextView selectFinshi;
    private File mTempFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        btnBack=findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        imageListRv=findViewById(R.id.image_list_rv);
        selectPreview=findViewById(R.id.select_preview);
        selectNum=findViewById(R.id.select_num);
        selectFinshi=findViewById(R.id.select_finshi);
        selectFinshi.setOnClickListener(this);
        initView();
    }


    protected void initView() {
        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_SELECT_MODE, mMode);
        mMaxCount = intent.getIntExtra(EXTRA_SELECT_COUNT, mMaxCount);
        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, mShowCamera);
        mResultList = (ArrayList<String>) intent.getSerializableExtra(EXTRA_DEFAULT_SELECTED_LIST);
        if (mResultList == null) {
            mResultList = new ArrayList<>();
        }
        //初始化本地数据
        initImageList();
        //改变显示
        exchangViewShow(mResultList.size());
    }



    /**
     * 初始化本地数据
     */
    private void initImageList() {
        //获取本地图片  耗时操作
        getLoaderManager().initLoader(LOADER_TYPE, null, mLoaderCallBack);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallBack = new LoaderManager.LoaderCallbacks<Cursor>() {
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID,
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = new CursorLoader(SelectImageActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION, IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR "
                    + IMAGE_PROJECTION[3] + "=? ",
                    new String[]{"image/jpeg", "image/png"},
                    IMAGE_PROJECTION[2] + " DESC");
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            //如果有数据变化
            if (data != null && data.getCount() > 0) {
                ArrayList<String> images = new ArrayList<>();
                //如果需要显示拍照就添加一条空数据
                if (mShowCamera) {
                    images.add("");
                }
                //不断遍历循环
                while (data.moveToNext()) {
                    String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    images.add(path);
                }
                //显示列表数据
                showListData(images);
            }
        }

        /**
         * 判断文件是否存在
         *
         * @param path
         * @return
         */
        private boolean pathExist(String path) {
            if (!TextUtils.isEmpty(path)) {
                return new File(path).exists();
            }
            return false;
        }
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {}
    };


    /**
     * 显示列表数据
     *
     * @param images
     */
    private void showListData(ArrayList<String> images) {
        SelectImageAdapter adapter = new SelectImageAdapter(SelectImageActivity.this, images, mResultList,mMaxCount, R.layout.media_chooser_item);
        imageListRv.setLayoutManager(new GridLayoutManager(SelectImageActivity.this, 4));
        imageListRv.setAdapter(adapter);
        //设置分割线
        imageListRv.addItemDecoration(new DividerGridItemDecoration(SelectImageActivity.this,R.drawable.item_dirver_01));
        adapter.setOnSelectImageListener(number -> exchangViewShow(number));
        //拍照回调
        adapter.setTakePhotoListener(() -> {
            //动态申请权限
            PermissionHelper.with(SelectImageActivity.this).
                    requestPermission(new String[]{Manifest.permission.CAMERA}).
                    requestCode(PHOTO_CAMERA).
                    request();
        });
    }

    /**
     * 拍照这里要做Android6.0以下 Android6.0和Android7.0以上的适配
     */
    @PermissionSuccess(requestCode = PHOTO_CAMERA)
    private void takePhoto(){
        mTempFile=new File(getInnerSDCardPath(), "imagephoto" + SystemClock.currentThreadTimeMillis() + ".jpg");
        if (!mTempFile.getParentFile().exists()){
            mTempFile.getParentFile().mkdirs();
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //Android7.0 com.picchoice替换成自己的包名
            Uri imageUri = FileProvider.getUriForFile(SelectImageActivity.this, AppUtils.getAppProcessName(this), mTempFile);//通过FileProvider创建一个content类型的Uri
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
            startActivityForResult(intent,REQUEST_CAMERA);
        }else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempFile));
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }
    /**
     * 获取内置SD卡路径
     * @return
     */
    public String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.requestPermissionsResult(this,requestCode,permissions,grantResults);
    }
    /**
     * 改变显示
     */
    private void exchangViewShow(int numder) {
        if (numder> 0) {
           selectPreview.setTextColor(Color.parseColor("#FF4081"));
            selectFinshi.setTextColor(Color.parseColor("#FF4081"));
            selectPreview.setEnabled(true);
         selectPreview.setOnClickListener(this);
        } else {
            selectPreview.setTextColor(Color.parseColor("#ffffff"));
            selectFinshi.setTextColor(Color.parseColor("#ffffff"));
            selectPreview.setEnabled(false);
            selectPreview.setOnClickListener(null);
        }
        selectNum.setText(numder + "/" + mMaxCount);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.select_preview) {
            Intent intent = new Intent(SelectImageActivity.this, PreViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("choicePosition", 0);
            bundle.putSerializable("choiceList", mResultList);
            intent.putExtras(bundle);
            startActivity(intent);

        } else if (i == R.id.btn_back) {
            finish();

        } else if (i == R.id.select_finshi) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST, mResultList);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //第一个要把图片添加到集合
        //调用Click方法
        //通知系统本地有图片改变了  下次进来可以找到这张图片
        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_CAMERA){

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mTempFile)));
                mResultList.add(mTempFile.getAbsolutePath());
                setResult();
            }
        }
    }

    /**
     * 设置返回结果
     */
    private void setResult(){
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putSerializable(EXTRA_DEFAULT_SELECTED_LIST,mResultList);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        //关闭当前页面
        finish();
    }
}
