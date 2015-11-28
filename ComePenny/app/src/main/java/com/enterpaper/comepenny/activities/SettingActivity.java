package com.enterpaper.comepenny.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.util.BaseActivity;
import com.enterpaper.comepenny.util.DataUtil;
import com.enterpaper.comepenny.util.SetFont;
import com.flurry.android.FlurryAgent;

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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kim on 2015-09-16.
 */
public class SettingActivity extends Activity {
    Toolbar mToolBar;
    ImageView btn_setting_back;
    LinearLayout btn_logout, btn_version;
    TextView tvVersion;

    private void addLog(){
        FlurryAgent.logEvent("Setting_view");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        addLog();

        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        // layout 생성
        initializeLayout();

        // 어플 버전 가져오기
        PackageInfo pi = null;

        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {

        }
        String appVersion = pi.versionName;

        tvVersion.setText(appVersion);

        initializeListener();
    }

    private void initializeLayout() {
        //툴바 설정
        mToolBar = (Toolbar) findViewById(R.id.setting_toolbar);
        mToolBar.setContentInsetsAbsolute(0, 0);

        btn_setting_back = (ImageView) findViewById(R.id.btn_setting_back);
        btn_logout = (LinearLayout) findViewById(R.id.btn_logout);
        btn_version = (LinearLayout) findViewById(R.id.btn_version);
        tvVersion = (TextView)findViewById(R.id.tv_setting_version);
    }

    private void initializeListener() {
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NetworkdelGCMID().execute("");

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


    //GCMId Del  http연결
    private class NetworkdelGCMID extends AsyncTask<String, String, Integer> {

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

                String user_id = DataUtil.getAppPreferences(SettingActivity.this, "user_id");

                // 데이터 담음

                name_value.add(new BasicNameValuePair("user_id", user_id + ""));

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
                    FlurryAgent.logEvent("logout");
                    jObject.getInt("err");

                    DataUtil.setAppPreferences(SettingActivity.this, "user_id", null);
                    new BaseActivity().closeActivity();
                    Intent itLoad = new Intent(getApplicationContext(), LoadingActivity.class);
                    startActivity(itLoad);
                    overridePendingTransition(0, 0);
                    finish();


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
