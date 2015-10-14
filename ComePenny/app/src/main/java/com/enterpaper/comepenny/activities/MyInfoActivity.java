package com.enterpaper.comepenny.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.tab.t1idea.IdeaAdapter;
import com.enterpaper.comepenny.tab.t1idea.IdeaDetailActivity;
import com.enterpaper.comepenny.tab.t1idea.IdeaListItem;
import com.enterpaper.comepenny.util.SetFont;

import java.util.ArrayList;

/**
 * Created by Kim on 2015-09-16.
 */
public class MyInfoActivity extends Activity {
    Toolbar mToolBar;

    View myinfoview;
    TextView tv_id, tv_usermail;
    ImageView btn_myinfo_back;
    ListView lv_mywrite;
    ImageView img_user, myInfo_divideline;
    private Intent intent = new Intent();
    IdeaAdapter myadapters;
    ArrayList<IdeaListItem> mydataList = new ArrayList<>();
    LinearLayout myinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        btn_myinfo_back = (ImageView)findViewById(R.id.btn_myinfo_back);
        btn_myinfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_mywrite = (ListView)findViewById(R.id.lv_mywrite);


        //헤더 생성
        myinfoview = getLayoutInflater().inflate(R.layout.activity_myinfo_header, null, false);
        myinfo = (LinearLayout) myinfoview.findViewById(R.id.myinfo);

        // 레이아웃 객체 생성
        initLayout();


        //헤더설정, 헤더부분 리스트뷰리스너작동막기
        lv_mywrite.addHeaderView(myinfoview,myadapters,false);


        addItemsMyIdea();

        // Adapter 생성
        myadapters = new IdeaAdapter(getApplicationContext(), R.layout.row_idea, mydataList);

        // Adapter와 GirdView를 연결
        lv_mywrite.setAdapter(myadapters);
        myadapters.notifyDataSetChanged();


        lv_mywrite.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
                intent.setClass(getApplicationContext(), IdeaDetailActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }

        });

        lv_mywrite.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        return;
    }

    // 리스트 아이템 추가
    private void addItemsMyIdea() {
        mydataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mydataList.add(new IdeaListItem(1234, "IdeaTitle", "jihoon1234", "1233", "4321"));

        }
    }

    // layout
    private void initLayout() {
        //툴바 설정
        mToolBar = (Toolbar) findViewById(R.id.myinfo_toolbar);
        mToolBar.setContentInsetsAbsolute(0, 0);

        myinfo = (LinearLayout) myinfoview.findViewById(R.id.myinfo);
        img_user = (ImageView) myinfoview.findViewById(R.id.img_user);
        myInfo_divideline = (ImageView) myinfoview.findViewById(R.id.myInfo_divideline);
        tv_id = (TextView) myinfoview.findViewById(R.id.tv_id);
        tv_usermail = (TextView) myinfoview.findViewById(R.id.tv_usermail);


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
