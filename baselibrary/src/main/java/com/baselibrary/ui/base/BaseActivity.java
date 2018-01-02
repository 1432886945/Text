package com.baselibrary.ui.base;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselibrary.R;
import com.baselibrary.errorview.VaryViewHelper;
import com.baselibrary.refresh.view.SuperRefreshLayout;
import com.baselibrary.ui.listener.DataListener;
import com.baselibrary.ui.listener.DialogOnClickListener;
import com.baselibrary.utils.AppManager;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.view.AlertDialog;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity<VB extends ViewDataBinding> extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>,DialogOnClickListener,DataListener {
	//权限返回码
	private static final int PERMISSON_REQUESTCODE = 0;
	static final String[] PROJECTION = new String[]{ContactsContract.Data._ID,
			ContactsContract.Data.DISPLAY_NAME};
	String mCurFilter;
	protected VB mBinding;
	public AppManager appManger;
	private boolean isok=true;
	public AlertDialog mDialog=null;
	private AlertDialog.Builder builder;
	//头部导航栏
	private RelativeLayout relTitleBar;
	//左边布局
	private RelativeLayout left_relative_layout;
	private ImageView left_icon_title;
	private Button left_btn;
	//中间布局
	public RelativeLayout mid_relative_layout;
	public TextView middle_text;
	public TextView middle_sub_text;

	public RelativeLayout getRight_relative_layout() {
		return right_relative_layout;
	}

	//右边布局
	public RelativeLayout right_relative_layout;
	public TextView title_right_text;
	public ImageView titl_right_image_view;
	//内容显示布局
	public FrameLayout body;

	/**
	 * 界面状态切换 对象
	 */
	protected VaryViewHelper mHelper;
	public AlertDialog getmDialog(String msg,String cancel,String ok) {
		builder = new AlertDialog.Builder(this);
		if (mDialog==null){
			synchronized (AlertDialog.class){
				builder.setMessage(msg)
						.setTopImage(R.mipmap.certification_prompt)
						.setNegativeButton(cancel, (dialog, which) ->   onCancel(cancel))
						.setPositiveButton(ok, (dialog, which) -> {
							onOk(ok);
						});
			}
			mDialog = builder.create();
		}
		return mDialog;
	}

	public Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			getData(msg.arg1,msg.obj);
		}
	};
	//自定义接口回调
	@Override
	public void getData(int id, Object data) {}
	//自定义弹窗确定（默认确定）点击监听
	@Override
	public void onOk(String ok) {
		if (mDialog!=null&&mDialog.isShowing())mDialog.dismiss();
		if (ok.equals("设置")){
			startAppSettings();
		}
	}
	//自定义弹窗取消（默认取消）点击监听
	@Override
	public void onCancel(String cancel) {
		if (cancel.equals("取消")){
			if (mDialog!=null&&mDialog.isShowing())mDialog.dismiss();
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		initData(savedInstanceState);
		appManger=AppManager.getInstance();
		appManger.addActivity(this);
		//竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mHelper = new VaryViewHelper.Builder()  //初始化界面状态管理器 ，需要自己写几类布局
				.setDataView(findViewById(R.id.body))
				.setLoadingView(LayoutInflater.from(this).inflate(R.layout.layout_loadingview,
						null))//加载页，无实际逻辑处理
				.setEmptyView(LayoutInflater.from(this).inflate(R.layout.layout_emptyview,
						null))//空页面，无实际逻辑处理
				.setErrorView(LayoutInflater.from(this).inflate(R.layout.layout_errorview, null))//错误页面
				.setRefreshListener(v -> {
					mHelper.showLoadingView();
					loadData(true);
				})
				.build();

		body =findViewById(R.id.body);
		relTitleBar=findViewById(R.id.relTitleBar);
		left_relative_layout=findViewById(R.id.left_relative_layout);
		left_icon_title=findViewById(R.id.left_icon_title);
		left_btn=findViewById(R.id.left_btn);
		mid_relative_layout=findViewById(R.id.mid_relative_layout);
		middle_text=findViewById(R.id.middle_text);
		middle_sub_text=findViewById(R.id.middle_sub_text);
		right_relative_layout=findViewById(R.id.right_relative_layout);
		title_right_text=findViewById(R.id.title_right_text);
		titl_right_image_view=findViewById(R.id.title_right_image_view);

		View view = LayoutInflater.from(this).inflate( getLayoutId(),
				null);
		body.addView(view,0);
		showLeftButton(true);
		mBinding = DataBindingUtil.inflate(getLayoutInflater(), getLayoutId(), body, true);
		initView();//加载ui
		initEvent();//处理事件
		loadData(true);//加载数据
	}
	public   void initData(Bundle bundle){}
	//返回布局
	protected abstract int getLayoutId();
	protected abstract void initView();
	protected abstract void initEvent();
	protected abstract void loadData(boolean isRefresh);
	//权限判断
	public abstract void requestPermissionResult(boolean allowPermission);


	/**
	 * @param permissions
	 * @since 2.5.0
	 */
	private void checkPermissions(String... permissions) {
		List<String> needRequestPermissonList = findDeniedPermissions(permissions);
		if (null != needRequestPermissonList
				&& needRequestPermissonList.size() > 0) {
			ActivityCompat.requestPermissions( this,
					needRequestPermissonList.toArray(
							new String[needRequestPermissonList.size()]),
					PERMISSON_REQUESTCODE);
		}
	}
	/**
	 * 获取权限集中需要申请权限的列表
	 *
	 * @param permissions
	 * @return
	 * @since 2.5.0
	 */
	private List<String> findDeniedPermissions(String[] permissions) {
		List<String> needRequestPermissonList = new ArrayList<String>();
		for (String perm : permissions) {
			if (ContextCompat.checkSelfPermission(this,
					perm) != PackageManager.PERMISSION_GRANTED
					|| ActivityCompat.shouldShowRequestPermissionRationale(
					this, perm)) {
				needRequestPermissonList.add(perm);
			}
		}
		return needRequestPermissonList;
	}


	/**
	 * 检测是否说有的权限都已经授权
	 *
	 * @param grantResults
	 * @return
	 * @since 2.5.0
	 */
	private boolean verifyPermissions(int[] grantResults) {
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}


	public boolean mayRequestPermission(String[] permissions) {
		boolean mayPermission = false;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}
		for (String req : permissions) {
			if (checkSelfPermission(req) != PackageManager.PERMISSION_GRANTED) {
				mayPermission = false;
				break;
			} else {
				mayPermission = true;
			}
		}
		if (mayPermission) {
			return true;
		}
		for (String req : permissions) {
			if (shouldShowRequestPermissionRationale(req)) {
				showMissingPermissionDialog();
			}
		}
		checkPermissions(permissions);

		return false;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == PERMISSON_REQUESTCODE) {
			if (verifyPermissions(grantResults)) {
				requestPermissionResult(true);
			} else {
				requestPermissionResult(false);
				showMissingPermissionDialog();
			}
		}
	}

	/**
	 * 显示提示信息
	 *
	 * @since 2.5.0
	 */
	public void showMissingPermissionDialog() {
		getmDialog("当前应用缺少必要权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限。","取消","设置").show();

	}

	/**
	 * 启动应用的设置
	 *
	 * @since 2.5.0
	 */
	private void startAppSettings() {
		Intent intent = new Intent(
				Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivity(intent);
	}
	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		Uri baseUri;
		if (mCurFilter != null) {
			baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI
					, Uri.encode(mCurFilter));
		} else {
			baseUri = ContactsContract.Contacts.CONTENT_URI;
		}
		String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
				+ ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
				+ ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";
		CursorLoader loader = new CursorLoader(this, baseUri,
				PROJECTION, select, null, ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		return loader;
	}



	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//        if (cursor == null) {
//            return;
//        }
//        while (cursor.moveToNext()) {
//            String[] names = cursor.getColumnNames();
//            for (String str : names) {
//                String contacts = cursor.getString(cursor.getColumnIndex(str));
//            }
//        }
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {}

	public void showToast(String msg){
		ToastUtils.showShort(msg);
	}

	//设置顶部导航栏背景
	private void setrelTitleBar(int color){

		relTitleBar.setBackgroundColor(color);
	}
	//显示左边按钮
	protected  void showLeftButton(boolean shut_Down,int...value){
		if (shut_Down){
			//默认返回图标
			left_icon_title.setImageResource(R.mipmap.back);
		}else {
			//默认取消图标
			//topLeftImg.setImageResource(R.mipmap.back);
		}
		//区分不同的关闭动画
		left_relative_layout.setOnClickListener(v -> finishActivity(shut_Down));

	}
	//隐藏/显示头部文字
	protected void show_Hide_ModuleTitle(String name) {
		Log.v("xxx","调用");
		middle_text.setText(name);
	}
	//隐藏/显示右边文字
	protected void hideTopRightText(String name) {
		title_right_text.setText(name);
	}
	//隐藏/显示右边图标
	protected void setModuleTitleImg(int resId,Object...value) {
		if (value==null) titl_right_image_view.setVisibility(View.VISIBLE);
		else  titl_right_image_view.setVisibility(View.GONE);
		titl_right_image_view.setImageResource(resId);
	}



	public void finishActivity(boolean isshow) {
		appManger.finishActivity(BaseActivity.this,isshow);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		OkGo.getInstance().cancelTag(this);
		handler.removeCallbacksAndMessages(null);
	}
}
