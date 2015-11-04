package com.enterpaper.comepenny.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.tab.ComePennyFragmentPagerAdapter;
import com.enterpaper.comepenny.tab.ComePennyMyinfoFragmentPagerAdapter;
import com.enterpaper.comepenny.tab.t1idea.IdeaAdapter;
import com.enterpaper.comepenny.tab.t1idea.IdeaDetailActivity;
import com.enterpaper.comepenny.tab.t1idea.IdeaListItem;
import com.enterpaper.comepenny.util.BackPressCloseHandler;
import com.enterpaper.comepenny.util.BaseActivity;
import com.enterpaper.comepenny.util.DataUtil;
import com.enterpaper.comepenny.util.SetFont;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kim on 2015-09-16.
 */
public class MyInfoActivity extends ActionBarActivity {
    Toolbar myinfo_toolbar;
    private BackPressCloseHandler backPressCloseHandler;
    ImageView btn_myinfo_back, img_myinfo_user;
    TextView tv_myinfo_user_mail;
   // AlertDialog mDialog;
    static public ViewPager pager_myinfo;
    PagerSlidingTabStrip tabsStrip_myinfo;
//    private static final int PICK_FROM_CAMERA = 0;
//     
//    private static final int PICK_FROM_GALLERY = 1;
//     
//    private static final int CROP_FROM_CAMERA = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        // 액티비티 추가
        new BaseActivity().actList.add(MyInfoActivity.this);

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


    private void initializeListener() {
        btn_myinfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_myinfo_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mDialog = createDialog(); 
            }
        });


    }


    private void initializeLayout() {
        img_myinfo_user = (ImageView) findViewById(R.id.img_my_info_user);
        btn_myinfo_back = (ImageView) findViewById(R.id.btn_myinfo_back);
        tv_myinfo_user_mail = (TextView) findViewById(R.id.tv_my_info_user_mail);
        tv_myinfo_user_mail.setText(DataUtil.getAppPreferences(getApplicationContext(), "user_email"));
    }


    private void initializeToolbar() {
        //액션바 객체 생성
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //액션바 설정
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        //액션바 숨김
        actionBar.hide();

        //툴바 설정
        myinfo_toolbar = (Toolbar) findViewById(R.id.myinfo_toolbar);
        myinfo_toolbar.setContentInsetsAbsolute(0, 0);

    }

    private void initializeTab() {
        pager_myinfo = (ViewPager) this.findViewById(R.id.pager_myinfo);
        pager_myinfo.setAdapter(new ComePennyMyinfoFragmentPagerAdapter(getSupportFragmentManager()));

        /* 큰아이콘 탭
        */
        tabsStrip_myinfo = (PagerSlidingTabStrip) this.findViewById(R.id.tabsStrip_myinfo);
        tabsStrip_myinfo.setViewPager(pager_myinfo);


        tabsStrip_myinfo.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


    //dialog
//    private AlertDialog createDialog() {
//        final View innerView = getLayoutInflater().inflate(R.layout.myinfoimg_dialog, null);
//        // TableRow dialogtitle = (TableRow) innerView.findViewById(R.id.dialogtitle); 
//        TableRow row1_gallery = (TableRow) innerView.findViewById(R.id.row1);
//        TableRow row2_camera = (TableRow) innerView.findViewById(R.id.row2); 
//        TableRow row3_basic = (TableRow) innerView.findViewById(R.id.row3);
//        row1_gallery.setOnClickListener(new View.OnClickListener() {
//             
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "dialogtest1", Toast.LENGTH_SHORT).show();  
//                //doTakePhotoAction(); 
//                setDismiss(mDialog); 
//            }
//        });
//        row2_camera.setOnClickListener(new View.OnClickListener() {
//            @Override 
//
//            public void onClick(View v) { 
//                Toast.makeText(getApplicationContext(), "dialogtest2", Toast.LENGTH_SHORT).show(); 
//                //   doTakePhotoAction();
//                setDismiss(mDialog);
//            }
//        });
//        row3_basic.setOnClickListener(new View.OnClickListener() {
//             
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "dialogtest3", Toast.LENGTH_SHORT).show(); 
//                // doTakePhotoAction(); 
//                setDismiss(mDialog); 
//            }
//        });
//        AlertDialog.Builder ab = new AlertDialog.Builder(this); 
//        ab.setView(innerView);
//        ab.setCancelable(true); 
//        Dialog mDialog = ab.create(); 
//        // dialog크기조절 
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//        params.copyFrom(mDialog.getWindow().getAttributes()); //
//        params.width = 800;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT; 
//        mDialog.show();
//        Window window = mDialog.getWindow();
//        window.setAttributes(params); 
//        return ab.create(); 
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PICK_FROM_CAMERA) {
//            Bundle extras = data.getExtras();
//            if (extras != null) {
//                Bitmap photo = extras.getParcelable("data"); 
//                img_myinfo_user.setImageBitmap(photo);
//            }
//        }
//
//        if (requestCode == PICK_FROM_GALLERY)
//
//        {
//            Bundle extras2 = data.getExtras();
//            if (extras2 != null) { 
//                Bitmap photo = extras2.getParcelable("data");
//                img_myinfo_user.setImageBitmap(photo); 
//            } 
//        }
//    }
//
//    // /** //     * 다이얼로그 종료 //     */ //
//    private void setDismiss(AlertDialog dialog) { 
//        if (dialog != null && dialog.isShowing()) { 
//            dialog.dismiss(); 
//        }
//    }

}
