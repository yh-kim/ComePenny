package com.enterpaper.comepenny.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.tab.ComePennyFragmentPagerAdapter;
import com.enterpaper.comepenny.util.BackPressCloseHandler;
import com.enterpaper.comepenny.util.BaseActivity;
import com.enterpaper.comepenny.util.SetFont;


public class MainActivity extends ActionBarActivity {
    Toolbar mToolBar;
    private BackPressCloseHandler backPressCloseHandler;
    ImageView imgMyInfo,imgSetting;

    static public ViewPager pager;
    PagerSlidingTabStrip tabsStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 액티비티 추가
        new BaseActivity().actList.add(MainActivity.this);

        // 취소버튼 눌렀을 때 핸들러
        backPressCloseHandler = new BackPressCloseHandler(this);

        // Text 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        // 레이아웃 객체 생성
        initializeLayout();

        initializeListener();

        // Toolbar 생성
        initializeToolbar();

        // 탭 생성
        initializeTab();
    }


    private void initializeLayout(){
        imgMyInfo = (ImageView)findViewById(R.id.img_main_myinfo);
        imgSetting = (ImageView)findViewById(R.id.img_main_setting);
    }

    private void initializeListener(){
        imgMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itMyInfo = new Intent(getApplicationContext(), MyInfoActivity.class);
                startActivity(itMyInfo);
                overridePendingTransition(0,0);
            }
        });

        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itSetting = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(itSetting);
                overridePendingTransition(0,0);
            }
        });
    }


    private void initializeToolbar(){
        //액션바 객체 생성
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //액션바 설정
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        //액션바 숨김
        actionBar.hide();

        //툴바 설정
        mToolBar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolBar.setContentInsetsAbsolute(0, 0);
    }

    private void initializeTab(){
        pager = (ViewPager) this.findViewById(R.id.pager);
        pager.setAdapter(new ComePennyFragmentPagerAdapter(getSupportFragmentManager()));

        /* 큰아이콘 탭
        */
        tabsStrip = (PagerSlidingTabStrip)this.findViewById(R.id.tabsStrip);
        tabsStrip.setViewPager(pager);



        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });

    }

    //취소버튼 눌렀을 때
    @Override
    public void onBackPressed() {
        //핸들러 작동
        backPressCloseHandler.onBackPressed();
        Toast.makeText(getApplicationContext(), "한 번 더 누르면 앱이 종료됩니다", Toast.LENGTH_SHORT)
                .show();
    }
}
