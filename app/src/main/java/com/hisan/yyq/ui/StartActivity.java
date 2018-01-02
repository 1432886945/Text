package com.hisan.yyq.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import com.baselibrary.ordercart.manycart.ManyCartActivity;
import com.baselibrary.ordercart.singlecart.SinglecartActivity;
import com.hisan.yyq.R;
import com.king.thread.nevercrash.NeverCrash;

/**
 * 创建时间 : 2017/12/5
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：无
 */
public class StartActivity extends AppCompatActivity {
    private Button user,album,multlist,network,wheel,dwon,pop,cart,cart1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        user=findViewById(R.id.user);
        album=findViewById(R.id.album);
        multlist=findViewById(R.id.multlist);
        network=findViewById(R.id.network);
        wheel=findViewById(R.id.wheel);
        dwon=findViewById(R.id.down);
        pop=findViewById(R.id.pop);
        cart=findViewById(R.id.cart);
        cart1=findViewById(R.id.cart1);
        NeverCrash.init((t, e) -> Log.d("xxx", "系统异常："+e.getMessage()));
        user.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, UserActivity.class);
            startActivity(intent);
        });
        album.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        });
        network.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, NetWorkActivity.class);
            startActivity(intent);
        });
        multlist.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, RecyclerActivity.class);
            startActivity(intent);
        });
        wheel.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, WheelActivity.class);
            startActivity(intent);
        });
        dwon.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, DownActivity.class);
            startActivity(intent);
        });
        pop.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, PopWindowActivity.class);
            startActivity(intent);
        });
        cart.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, ManyCartActivity.class);
            startActivity(intent);
        });
        cart1.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, SinglecartActivity.class);
            startActivity(intent);
        });
    }
}

