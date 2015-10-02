package com.enterpaper.comepenny.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.tab.t1idea.IdeaAdapter;
import com.enterpaper.comepenny.tab.t1idea.IdeaListItem;
import com.enterpaper.comepenny.util.SetFont;

import java.util.ArrayList;

/**
 * Created by Kim on 2015-09-16.
 */
public class BoothDetailActivity extends ActionBarActivity {

    ListView lvBoothDetailIdea;

    IdeaAdapter adapters;
    ArrayList<IdeaListItem> dataList = new ArrayList<>();

    Toolbar mToolBar;
    ImageView btnBoothBack,btnBoothInfo,btnBoothInfoClose;
    LinearLayout lyBoothInfo;
    View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booth_detail);

        // 리스트 헤더 부분
        header = getLayoutInflater().inflate(R.layout.activity_booth_detail_header,null,false);

        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        //Toolbar 생성
        initToolbar();

        // 레이아웃 객체 생성
        initLayout();

        // 헤더 설정
        lvBoothDetailIdea.addHeaderView(header);

        addItemsidea();

        // Adapter 생성
        adapters = new IdeaAdapter(getApplicationContext(), R.layout.row_idea, dataList);

        // Adapter와 GirdView를 연결
        lvBoothDetailIdea.setAdapter(adapters);

        adapters.notifyDataSetChanged();

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

    // 리스트 아이템 추가
    private void addItemsidea() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add(new IdeaListItem(1234, "IdeaTitle", "jihoon1234", "1233", "4321"));
        }
    }

    // layout
    private void initLayout(){
        lyBoothInfo = (LinearLayout)header.findViewById(R.id.booth_info);
        btnBoothBack = (ImageView)findViewById(R.id.btn_booth_back);
        btnBoothInfo = (ImageView)header.findViewById(R.id.btn_booth_info);
        btnBoothInfoClose = (ImageView)header.findViewById(R.id.btn_booth_info_close);
        lvBoothDetailIdea = (ListView) findViewById(R.id.lv_booth_detail_idea);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}
