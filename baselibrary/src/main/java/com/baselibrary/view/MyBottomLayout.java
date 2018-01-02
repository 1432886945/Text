package com.baselibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselibrary.R;


/**
 * 创建时间 : 2017/8/2
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：自定义底部按钮
 */
public class MyBottomLayout extends LinearLayout implements View.OnTouchListener {
    private Context context;
    private RelativeLayout shopping;//首页
    private RelativeLayout module;//商场
    private RelativeLayout order_cart;//我的
    private ICallbackListener iCallbackListener = null;

    public MyBottomLayout(Context context) {
        super(context);
        this.context=context;
        initview();
    }

    public MyBottomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initview();
    }

    public MyBottomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initview();
    }

    //初始化
    private void initview() {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_bottom,this);
        findView(view);
        initData();
    }

    //初始化数据
    private void initData() {
        setResidAndColor(0);
    }

    //初始化按钮
    private void findView(View view) {
        shopping= (RelativeLayout) view.findViewById(R.id.shoppingLayout);
        module= (RelativeLayout) view.findViewById(R.id.moduleLayout);
        order_cart= (RelativeLayout) view.findViewById(R.id.order_cartLayout);
        shopping.setOnTouchListener(this);
        module.setOnTouchListener(this);
        order_cart.setOnTouchListener(this);
    }


    /**
     * 设置名称和颜色
     * @param
     */
    public void setResidAndColor(int i) {
//        switch (i) {
//            case 0:
//                changeDataItem(setResid(new int[]{1, 0, 0, 0, 0}),
//                        new int[]{1, 0, 0, 0, 0});
//                break;
//            case 1:
//                changeDataItem(setResid(new int[]{0, 1, 0, 0, 0}),
//                        new int[]{0, 1, 0, 0, 0});
//                break;
//            case 2:
//                changeDataItem(setResid(new int[]{0, 0, 1, 0, 0}),
//                        new int[]{0, 0, 1, 0, 0});
//                break;
//        }
    }

    /**
     * 把所有的数据整合一起进行抽取
     */
    private void changeDataItem(int[] resid, int[] color) {
        initDataItem(shopping, resid[0], "首页", color[0]);
        initDataItem(module, resid[1], "行程", color[1]);

        initDataItem(order_cart, resid[2], "我的", color[2]);
    }


    /**
     * 初始化数据的抽取方法
     * @param resid
     * @param name
     * @param color
     */
    private void initDataItem(View view, int resid, String name, int color) {
        view.findViewById(R.id.tabImg).setBackgroundResource(resid);
        TextView tv = (TextView) view.findViewById(R.id.tabText);
        tv.setText(name);
        tv.setTextColor( (color == 1) ? getResources().getColor(R.color.order): getResources().getColor(R.color.home_titel));
    }


//    public int[] setResid(int[] resid) {
//        int ressHopping =  (resid[0] == 1) ?  R.mipmap.home_acitve : R.mipmap.home;
//        int resModule =  (resid[1] == 1) ?  R.mipmap.trip_active : R.mipmap.trip;
//        int resoRder =  (resid[2] == 1) ?  R.mipmap.my_active : R.mipmap.my;
//        return new int[] {ressHopping, resModule, resoRder};
//    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int i = view.getId();
        if (i == R.id.shoppingLayout) {
            setResidAndColor(0);
        } else if (i == R.id.moduleLayout) {
            setResidAndColor(1);
        } else if (i == R.id.order_cartLayout) {
            setResidAndColor(2);
        }
        //这里加入了一个接口方法，留给ViewPager去实现
        //功能是点击item后viewPager也会跟着变
        iCallbackListener.click(view.getId());
        return false;
    }

    //自定义接口文件，click方法由调用处实现，功能是完成viewpager的滑动
    public interface ICallbackListener {
        void click(int id);
    }

    //初始化接口，由需要实现activity（MainActivity）调用
    //通过findviewbyid获取MyBottomLayout，进行调用
    public void setOnCallbackListener(ICallbackListener iCallbackListener) {
        this.iCallbackListener = iCallbackListener;
    }
}
