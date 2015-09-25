package com.enterpaper.comepenny.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Kim on 2015-08-12.
 */
public class BaseActivity extends Activity{
    //onCreate되는 액티비티들 저장
    public static ArrayList<Activity> actList = new ArrayList<Activity>();

    // 저장한 모든 액티비티 종료
    public void closeActivity(){
        for(int i=0; i< actList.size();i++){
            actList.get(i).finish();
        }
    }

}
