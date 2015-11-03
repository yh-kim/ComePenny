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
import java.util.regex.Pattern;

/**
 * Created by Kim on 2015-10-25.
 */
public class SignUpActivity extends Activity{

    ImageView btn_sighup;
    EditText e_signup_email,e_signup_passwd,e_signup_passwd_confirm;
    String email,passwd,passwd_confirm;
    ScrollView svSignUp;
    InputMethodManager keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        initializeLayout();

        initializeListener();
    }

    private void initializeLayout(){
        //스크린키보드
        keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        e_signup_email = (EditText)findViewById(R.id.e_signup_email);
        e_signup_passwd = (EditText)findViewById(R.id.e_signup_passwd);
        e_signup_passwd_confirm = (EditText)findViewById(R.id.e_signup_passwd_confirm);

        svSignUp = (ScrollView)findViewById(R.id.sv_signup);
        svSignUp.setVerticalScrollBarEnabled(false);

        btn_sighup = (ImageView)findViewById(R.id.btn_sighup);
    }

    private void initializeListener(){

        // 텍스트창 누르면 올라가는거
        e_signup_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if( hasFocus == true  && !(e_signup_email.getText().toString().trim().length() > 1)){
                    svSignUp.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            svSignUp.smoothScrollBy(0, 400);
                        }
                    }, 200);
                }

            }
        });

        btn_sighup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = e_signup_email.getText().toString().trim();
                passwd = e_signup_passwd.getText().toString().trim();
                passwd_confirm = e_signup_passwd_confirm.getText().toString().trim();

                if (email.length() == 0) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 이메일 체크
                if(!Pattern.matches("^[0-9a-zA-Z-_\\.]*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$",email)){
                    Toast.makeText(getApplicationContext(), "올바른 이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd.length() == 0) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd.length() < 5) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 너무 짧습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd.length() > 14) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 너무 깁니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd_confirm.length() == 0) {
                    Toast.makeText(getApplicationContext(), "비밀번호 확인을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!passwd.equals(passwd_confirm)) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 서버에 요청
                new NetworkSignUp().execute();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

    /**
     * HTTP 연결 SignUp Thread 생성 클래스
     */
    private class NetworkSignUp extends AsyncTask<String, String, Integer> {
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

                http_post = new HttpPost("http://54.199.176.234/api/signup.php");

                // 데이터 담음
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

        @Override
        protected void onPostExecute(Integer result) {

            // 정상적으로 회원가입
            if(result == 0){
                try {
                    String user_id = jObject.getString("user_id");

                    DataUtil.setAppPreferences(SignUpActivity.this, "user_id", user_id);
                    DataUtil.setAppPreferences(SignUpActivity.this, "user_email", email);

                    Intent itMain = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(itMain);
                    overridePendingTransition(0, 0);
                    new BaseActivity().closeActivity();
                    finish();
                    return;
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

            if(result == 3){
                Toast.makeText(getApplicationContext(), "이메일이 존재합니다", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_SHORT).show();
            return;
        }


    }
}