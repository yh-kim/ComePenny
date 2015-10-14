package com.enterpaper.comepenny.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.util.SetFont;

/**
 * Created by Kim on 2015-09-16.
 */
public class WriteActivity extends Activity {
    Toolbar mToolBar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_write);

            //TextView 폰트 지정
            SetFont.setGlobalFont(this, getWindow().getDecorView());

            // layout 생성
            initLayout();

    }

    private void initLayout(){
        //툴바 설정
        mToolBar = (Toolbar) findViewById(R.id.write_toolbar);
        mToolBar.setContentInsetsAbsolute(0, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

}
