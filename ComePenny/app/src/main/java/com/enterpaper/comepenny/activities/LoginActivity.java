package com.enterpaper.comepenny.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.enterpaper.comepenny.R;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kim on 2015-10-25.
 */
public class LoginActivity extends Activity {
    ImageView btnLogin;
    TextView tvSignUp;
    EditText e_log_email;
    EditText e_log_passwd;
    String email;
    String passwd;
    ScrollView svLogin;
    InputMethodManager keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        initializeLayout();

        initializeListener();

    }

    private void initializeLayout(){
        //스크린키보드
        keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        btnLogin = (ImageView)findViewById(R.id.btn_login);
        tvSignUp = (TextView)findViewById(R.id.tv_SignUp);

        e_log_email = (EditText)findViewById(R.id.e_log_email);
        e_log_passwd = (EditText)findViewById(R.id.e_log_passwd);

        svLogin = (ScrollView)findViewById(R.id.sv_login);
        svLogin.setVerticalScrollBarEnabled(false);
    }

    private void initializeListener(){
        // 텍스트창 누르면 올라가는거
        e_log_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus == true && !(e_log_email.getText().toString().trim().length() > 1)) {
                    svLogin.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            svLogin.smoothScrollBy(0, 400);
                        }
                    }, 200);
                }

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = e_log_email.getText().toString().trim();
                passwd = e_log_passwd.getText().toString().trim();

                if (email.length() == 0) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd.length() == 0) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                //서버로 데이터 보냄
                onLogin();

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(itSignUp);
                overridePendingTransition(0, 0);
                new BaseActivity().actList.add(LoginActivity.this);
            }
        });
    }

    private void onLogin(){
        new NetworkLogin().execute();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }



    // HTTP 연결 Login Thread 클래서
    private class NetworkLogin extends AsyncTask<String, String, Integer>{

        // JSON 받아오는 객체
        private JSONObject jObject;

        @Override
        protected Integer doInBackground(String... params) {
            return processing();
        }

        // 서버 연결
        private Integer processing(){
            try {
                HttpClient http_client = new DefaultHttpClient();

                // 요청 후 7초 이내에 응답없으면 timeout 발생
                http_client.getParams().setParameter("http.connection.timeout",7000);
                // post 방식
                HttpPost http_post = null;

                List<NameValuePair> name_value = new ArrayList<NameValuePair>();

                http_post = new HttpPost("http://54.199.176.234/api/login.php");

                // 데이터 담음 키,value
                name_value.add(new BasicNameValuePair("email", email));
                name_value.add(new BasicNameValuePair("passwd", passwd));

                UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(name_value, "UTF-8");
                http_post.setEntity(entityRequest);


                // 서버 전송
                HttpResponse response = http_client.execute(http_post);

                // 받는 부분
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"UTF-8"),8);
                StringBuilder builder = new StringBuilder();
                for(String line = null; (line = reader.readLine()) !=null;){
                    builder.append(line).append("\n");
                }

                // json
                jObject = new JSONObject(builder.toString());
                // callback 오류 뜰 때
//                jObject = new JSONObject(builder.toString().substring(builder.toString().indexOf("{"), builder.toString().lastIndexOf("}") + 1));

                // 0이면 정상, 0이 아니면 오류 발생
                if(jObject.getInt("err") > 0){
                    return jObject.getInt("err");
                }

            } catch (Exception e){
                // 오류발생시
                e.printStackTrace();
                return 100;
            }
            return 0;
        }

        // 값 받는 부분
        @Override
        protected void onPostExecute(Integer result) {
            // 정상적으로 로그인
            if(result == 0){
                try {
                    String user_id = jObject.getString("user_id");
                    String user_email = jObject.getString("user_email");
                    DataUtil.setAppPreferences(LoginActivity.this, "user_id", user_id);
                    DataUtil.setAppPreferences(LoginActivity.this, "user_email", user_email);

                    Intent itMain = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(itMain);
                    overridePendingTransition(0, 0);
                    finish();
                    return;
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

            if(result == 3){
                Toast.makeText(getApplicationContext(), "이메일이 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            if(result == 4){
                Toast.makeText(getApplicationContext(), "비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_SHORT).show();
            return;
        }

    }


    }


