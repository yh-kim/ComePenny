package com.enterpaper.comepenny.activity;

import android.app.Activity;
import android.os.Bundle;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.util.SetFont;

/**
 * Created by Kim on 2015-09-16.
 */
public class MyInfoActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_myinfo);

            //TextView 폰트 지정
            SetFont.setGlobalFont(this, getWindow().getDecorView());
    }
}
