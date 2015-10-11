package com.enterpaper.comepenny.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.util.SetFont;

/**
 * Created by Kim on 2015-09-16.
 */
public class SettingActivity extends Activity {
    ImageView btn_setting_back;
    LinearLayout btn_logout, btn_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());
        ImageView btn_setting_back = (ImageView) findViewById(R.id.btn_setting_back);
        LinearLayout btn_logout = (LinearLayout) findViewById(R.id.btn_logout);
        LinearLayout btn_version = (LinearLayout) findViewById(R.id.btn_version);


        btn_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {finish();}
        });


    }
}
