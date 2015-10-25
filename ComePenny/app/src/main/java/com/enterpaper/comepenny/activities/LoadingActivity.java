package com.enterpaper.comepenny.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.util.DataUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kim on 2015-08-08.
 */
public class LoadingActivity extends Activity {
    private long splashDelay = 400;

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //Logo를 보여주는 쓰레드 (타이머)
        //2초 후에 run메소드가 실행됨
        TimerTask task = new TimerTask(){

            @Override
            public void run() {
                //register값이 저장되있는 회원이 있나 파일에서가져옴
                String register = DataUtil.getAppPreferences(getApplicationContext(), "user_id");

                //회원이 없을때
                if(register.equals("")){
                    //로그인 activity 이동
                    Intent itLogin = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(itLogin);
                    overridePendingTransition(0, 0);
                }
                //회원가입이 되어있을 때
                else{
                    Intent itMain = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(itMain);
                    overridePendingTransition(0, 0);
                }

                finish();
            }

        };

        //Timer 객체 생성
        Timer timer = new Timer();
        //몇초 후에 실행해라(splashDelay 초 후에 task를 실행해라)
        timer.schedule(task, splashDelay);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

}
