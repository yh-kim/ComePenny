package com.enterpaper.comepenny.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.util.SetFont;

public class IdeaDetailActivity extends ActionBarActivity {
    private ScrollView scrollView_mainidea_detail;
    Toolbar mToolBar;
    ImageView Img_company, btn_ideaback;
    ImageButton btn_pick;
    TextView tv_logo_name, tv_ideatitle, tv_Writer, tv_view, tv_like, tv_ideaoriginal,btn_modify, btn_delete;
    boolean pick_boolean = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_detail);

        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        //Toolbar 생성
        initToolbar();


        // 레이아웃 객체 생성
        initLayout();

        btn_ideaback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pick_boolean == true) {
                    btn_pick.setBackgroundResource(R.drawable.detail_pickbutton_after);
                    Toast.makeText(getApplicationContext(), "idea를 pick하겠습니다", Toast.LENGTH_SHORT)
                            .show();
                    pick_boolean = false;

                }else{
                    btn_pick.setBackgroundResource(R.drawable.detail_pickbutton_before);
                    Toast.makeText(getApplicationContext(), "pick 취소하겠습니다", Toast.LENGTH_SHORT)
                            .show();
                    pick_boolean = true;
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // layout
    private void initLayout(){
        scrollView_mainidea_detail = (ScrollView)findViewById(R.id.scrollView_mainidea_detail);
        Img_company = (ImageView) findViewById(R.id.Img_company);
        btn_ideaback = (ImageView) findViewById(R.id.btn_ideaback);
        btn_pick = (ImageButton) findViewById(R.id.btn_pick);
        btn_modify = (TextView) findViewById(R.id.btn_modify);
        btn_delete = (TextView) findViewById(R.id.btn_delete);
        tv_logo_name = (TextView) findViewById(R.id.tv_logo_name);
        tv_ideatitle = (TextView) findViewById(R.id.tv_ideatitle);
        tv_Writer = (TextView) findViewById(R.id.tv_Writer);
        tv_view = (TextView) findViewById(R.id.tv_view);
        tv_like = (TextView) findViewById(R.id.tv_like);
        tv_ideaoriginal = (TextView) findViewById(R.id.tv_ideaoriginal);
    }

    private void initToolbar() {
        //액션바 객체 생성
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //액션바 설정
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        //액션바 숨김
        actionBar.hide();

        //툴바 설정
        mToolBar = (Toolbar) findViewById(R.id.mainidea_toolbar);
        mToolBar.setContentInsetsAbsolute(0, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

}
