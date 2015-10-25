package com.enterpaper.comepenny.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.enterpaper.comepenny.util.SetFont;

/**
 * Created by Kim on 2015-10-25.
 */
public class LoginActivity extends Activity {
    ImageView btnLogin;
    TextView tvSignUp;
    EditText e_log_email;
    EditText e_log_passwd;
    String user_email;
    String user_passwd;
    ScrollView svLogin;
    InputMethodManager keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        initLayout();
    }

    private void initLayout(){
        //스크린키보드
        keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        btnLogin = (ImageView)findViewById(R.id.btn_login);
        tvSignUp = (TextView)findViewById(R.id.tv_SignUp);

        e_log_email = (EditText)findViewById(R.id.e_log_email);
        e_log_passwd = (EditText)findViewById(R.id.e_log_passwd);

        svLogin = (ScrollView)findViewById(R.id.sv_login);
        svLogin.setVerticalScrollBarEnabled(false);

        // 텍스트창 누르면 올라가는거
        e_log_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if( hasFocus == true && !(e_log_email.getText().toString().trim().length() > 1)){
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
                user_email = e_log_email.getText().toString().trim();
                user_passwd = e_log_passwd.getText().toString().trim();

                if (user_email.length() == 0) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (user_passwd.length() == 0) {
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
        Intent itMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(itMain);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
