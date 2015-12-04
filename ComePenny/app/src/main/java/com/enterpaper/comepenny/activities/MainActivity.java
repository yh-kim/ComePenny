package com.enterpaper.comepenny.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.tab.ComePennyFragmentPagerAdapter;
import com.enterpaper.comepenny.util.BackPressCloseHandler;
import com.enterpaper.comepenny.util.BaseActivity;
import com.enterpaper.comepenny.util.DataUtil;
import com.enterpaper.comepenny.util.SetFont;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private String SENDER_ID = "536972355978";//프로젝트번호
    private GoogleCloudMessaging gcm;
    private String regid;
    ComePennyFragmentPagerAdapter cpPageAdapter;


    Toolbar mToolBar;
    private BackPressCloseHandler backPressCloseHandler;
    ImageView imgMyInfo, imgSetting;

    static public ViewPager pager;
    PagerSlidingTabStrip tabsStrip;


    @Override
    protected void onStart() {
        super.onStart();

        FlurryAgent.onStartSession(getApplicationContext(), "NGMPMQJKBVTNNJQ3SNKZ");
    }

    @Override
    protected void onStop() {
        super.onStop();

        FlurryAgent.onEndSession(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 플러리
        FlurryAgent.setLogEnabled(false);
        FlurryAgent.init(this, "NGMPMQJKBVTNNJQ3SNKZ");

        // gcm
        gcm = GoogleCloudMessaging.getInstance(this);
        registerInBackground();

        // 액티비티 추가
        new BaseActivity().actList.add(MainActivity.this);

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

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    regid = gcm.register(SENDER_ID);
                    sendRegistrationIdToBackend();
                } catch (IOException ex) {
                }
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void sendRegistrationIdToBackend() {
        // Your implementation here.
        new NetworkaddGCMID().execute("");
    }


    private void initializeLayout() {
        imgMyInfo = (ImageView) findViewById(R.id.img_main_myinfo);
        imgSetting = (ImageView) findViewById(R.id.img_main_setting);
    }

    private void initializeListener() {
        imgMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itMyInfo = new Intent(getApplicationContext(), MyInfoActivity.class);
                startActivity(itMyInfo);
                overridePendingTransition(0, 0);
            }
        });

        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itSetting = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(itSetting);
                overridePendingTransition(0, 0);
            }
        });
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
        mToolBar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolBar.setContentInsetsAbsolute(0, 0);
    }

    private void initializeTab() {
        pager = (ViewPager) this.findViewById(R.id.pager);
        cpPageAdapter = new ComePennyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(cpPageAdapter);

        /* 큰아이콘 탭
        */
        tabsStrip = (PagerSlidingTabStrip) this.findViewById(R.id.tabsStrip);
        tabsStrip.setViewPager(pager);

        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        Toast.makeText(getApplicationContext(), "한 번 더 누르면 앱이 종료됩니다", Toast.LENGTH_SHORT)
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    //GCMId Reg  http연결
    private class NetworkaddGCMID extends AsyncTask<String, String, Integer> {

        // JSON 받아오는 객체
        private JSONObject jObject;

        @Override
        protected Integer doInBackground(String... params) {
            return processing();
        }

        // 서버 연결
        private Integer processing() {
            try {
                HttpClient http_client = new DefaultHttpClient();

                // 요청 후 7초 이내에 응답없으면 timeout 발생
                http_client.getParams().setParameter("http.connection.timeout", 7000);
                // post 방식
                HttpPost http_post = null;

                List<NameValuePair> name_value = new ArrayList<NameValuePair>();

                http_post = new HttpPost("http://54.199.176.234/api/set_gcm_id.php");

                String user_id = DataUtil.getAppPreferences(MainActivity.this, "user_id");

                // 데이터 담음

                name_value.add(new BasicNameValuePair("user_id", user_id + ""));
                name_value.add(new BasicNameValuePair("gcm_id", regid));

                UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(name_value, "UTF-8");
                http_post.setEntity(entityRequest);


                // 서버 전송
                HttpResponse response = http_client.execute(http_post);

                // 받는 부분
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), 8);
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null; ) {
                    builder.append(line).append("\n");
                }

                // json
                jObject = new JSONObject(builder.toString());


                // 0이면 정상, 0이 아니면 오류 발생
                if (jObject.getInt("err") > 0) {
                    return jObject.getInt("err");
                }

            } catch (Exception e) {
                // 오류발생시
                e.printStackTrace();
                return 100;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            // 정상적으로 글쓰기
            if (result == 0) {
                try {
                    jObject.getInt("err");
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_SHORT).show();
            return;
        }


    }


}
