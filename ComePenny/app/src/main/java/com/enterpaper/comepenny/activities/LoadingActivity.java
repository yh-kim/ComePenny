package com.enterpaper.comepenny.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.util.DataUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kim on 2015-08-08.
 */
public class LoadingActivity extends Activity {
    private long splashDelay = 400;

    String mData = "", mVer = null;

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getMarketVersionFast();


    }

    public void TimerTask() {
        //Logo를 보여주는 쓰레드 (타이머)
        //2초 후에 run메소드가 실행됨
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                //register값이 저장되있는 회원이 있나 파일에서가져옴
                String register = DataUtil.getAppPreferences(getApplicationContext(), "user_id");

                //회원이 없을때
                if (register.equals("")) {
                    //로그인 activity 이동
                    Intent itLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(itLogin);
                    overridePendingTransition(0, 0);
                }
                //회원가입이 되어있을 때
                else {
                    Intent itMain = new Intent(getApplicationContext(), MainActivity.class);
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

    public String getMarketVersionFast() {
        new NetworkGetVersion().execute("");
        return mVer;
    }

    class NetworkGetVersion extends AsyncTask<String, String, Integer> {
        private String err_msg = "Network error.";

        // JSON에서 받아오는 객체
        private JSONObject jObjects;

        // AsyncTask 실행되는거
        @Override
        protected Integer doInBackground(String... params) {


            try {

                URL mUrl = new URL("https://play.google.com/store/apps/details?id=com.enterpaper.comepenny");
                HttpURLConnection mConnection = (HttpURLConnection) mUrl
                        .openConnection();

                if (mConnection == null) {

                    return null;
                }

                mConnection.setConnectTimeout(5000);
                mConnection.setUseCaches(false);
                mConnection.setDoOutput(true);

                if (mConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader mReader = new BufferedReader(
                            new InputStreamReader(mConnection.getInputStream()));
                    while (true) {
                        String line = mReader.readLine();
                        if (line == null)
                            break;
                        mData += line;
                    }

                    mReader.close();
                }

                mConnection.disconnect();

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            String startToken = "softwareVersion\">";
            String endToken = "<";
            int index = mData.indexOf(startToken);

            if (index == -1) {
                mVer = null;

            } else {
                mVer = mData.substring(index + startToken.length(), index
                        + startToken.length() + 100);
                mVer = mVer.substring(0, mVer.indexOf(endToken)).trim();

            }

            return 0;

        }


        // AsyncTask 실행완료 후에 구동 (Data를 받은것을 Activity에 갱신하는 작업을 하면돼)
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            // 어플 버전 가져오기
            PackageInfo pi = null;
            try {
                pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {

            }
            String appVersion = pi.versionName;

            if (mVer.equals(appVersion)) {
                // Toast.makeText(getApplicationContext(), "최신버전", Toast.LENGTH_SHORT).show();
                TimerTask();


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoadingActivity.this);
                builder.setTitle("버전업데이트")        // 제목 설정
                        .setMessage("새로운 어플리케이션 버전이 출시했습니다.")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("바로설치", new DialogInterface.OnClickListener() {
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.enterpaper.comepenny"));
                                startActivity(intent);
                                //dialog.cancel();
                            }
                        })
                        .setNegativeButton("나중에", new DialogInterface.OnClickListener() {
                            // 취소 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                TimerTask();
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기

            }
            return;


        }

    }
}
