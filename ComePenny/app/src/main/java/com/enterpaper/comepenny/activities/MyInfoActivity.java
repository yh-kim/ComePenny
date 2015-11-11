package com.enterpaper.comepenny.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kim on 2015-09-16.
 */
public class MyInfoActivity extends ActionBarActivity {
    Toolbar myinfo_toolbar;
    ImageView btn_myinfo_back, img_myinfo_user;
    TextView tv_myinfo_user_mail;
    ScrollView myinfo_scroll;
    static public ViewPager pager_myinfo;
    PagerSlidingTabStrip tabsStrip_myinfo;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private Uri mImageCaptureUri;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        // 액티비티 추가
        new BaseActivity().actList.add(MyInfoActivity.this);


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
                final CharSequence[] items = {"기본이미지", "사진앨범", "카메라"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MyInfoActivity.this);     // 여기서 this는 Activity의 this

// 여기서 부터는 알림창의 속성 설정
                builder.setTitle("프로필 사진 설정")        // 제목 설정
                        .setItems(items, new DialogInterface.OnClickListener() {    // 목록 클릭시 설정
                            public void onClick(DialogInterface dialog, int index) {

                                switch (index) {
                                    case 0:
                                        Toast.makeText(getApplicationContext(), "기본이미지", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Toast.makeText(getApplicationContext(), "사진앨범", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        Toast.makeText(getApplicationContext(), "카메라", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        dialog.cancel();
                                        break;
                                }


                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기


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


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

}

