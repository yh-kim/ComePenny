package com.enterpaper.comepenny.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.util.SetFont;

/**
 * Created by Kim on 2015-09-16.
 */
public class BoothDetailActivity extends ActionBarActivity {
    Toolbar mToolBar;
    ImageView btnBoothBack,btnBoothInfo,btnBoothInfoClose;
    LinearLayout lyBoothInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booth_detail);

        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        //Toolbar 생성
        initToolbar();

        lyBoothInfo = (LinearLayout)findViewById(R.id.booth_info);
        btnBoothBack = (ImageView)findViewById(R.id.btn_booth_back);
        btnBoothInfo = (ImageView)findViewById(R.id.btn_booth_info);
        btnBoothInfoClose = (ImageView)findViewById(R.id.btn_booth_info_close);

        btnBoothInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyBoothInfo.setVisibility(View.VISIBLE);
            }
        });

        btnBoothInfoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyBoothInfo.setVisibility(View.INVISIBLE);

            }
        });

        btnBoothBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initToolbar(){
        //액션바 객체 생성
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //액션바 설정
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        //액션바 숨김
        actionBar.hide();

        //툴바 설정
        mToolBar = (Toolbar) findViewById(R.id.booth_detail_toolbar);
        mToolBar.setContentInsetsAbsolute(0, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}
