package com.enterpaper.comepenny.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.util.BaseActivity;
import com.enterpaper.comepenny.util.DataUtil;
import com.enterpaper.comepenny.util.SetFont;

/**
 * Created by Kim on 2015-09-16.
 */
public class SettingActivity extends Activity {
    Toolbar mToolBar;
    ImageView btn_setting_back;
    LinearLayout btn_logout, btn_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        // layout 생성
        initializeLayout();

        initializeListener();
    }

    private void initializeLayout(){
        //툴바 설정
        mToolBar = (Toolbar) findViewById(R.id.setting_toolbar);
        mToolBar.setContentInsetsAbsolute(0, 0);

        btn_setting_back = (ImageView) findViewById(R.id.btn_setting_back);
        btn_logout = (LinearLayout) findViewById(R.id.btn_logout);
        btn_version = (LinearLayout) findViewById(R.id.btn_version);
    }

    private void initializeListener(){
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtil.setAppPreferences(SettingActivity.this, "user_id", null);
                new BaseActivity().closeActivity();
                Intent itLoad = new Intent(getApplicationContext(),LoadingActivity.class);
                startActivity(itLoad);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        btn_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
