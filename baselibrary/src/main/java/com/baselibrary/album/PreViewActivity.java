package com.baselibrary.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baselibrary.R;
import com.baselibrary.ui.ImageViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miao on 2017/6/29.
 * 预览界面
 */
public class PreViewActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnBack;
    private ViewPager viewpager;
    private List<String> choiceList;
    private DeleteAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        viewpager=findViewById(R.id.viewpager);
        btnBack=findViewById(R.id.btn_back);
        choiceList = (List<String>) getIntent().getSerializableExtra("choiceList");
        if(choiceList==null){
            choiceList=new ArrayList<>();
        }

        adapter=new DeleteAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);

        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_back) {
            finish();
        }
    }
    class DeleteAdapter extends FragmentPagerAdapter {
        public DeleteAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return ImageViewFragment.newInstance(choiceList.get(position));
        }
        @Override
        public int getCount() {
            return choiceList.size();
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}
