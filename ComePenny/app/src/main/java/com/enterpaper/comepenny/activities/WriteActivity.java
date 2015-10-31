package com.enterpaper.comepenny.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.tab.t1idea.IdeaDetailActivity;
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
 * Created by Kim on 2015-09-16.
 */
public class WriteActivity extends Activity {
    Toolbar mToolBar;
    int booth_id;
    ImageView btn_write_back, btn_write_write;
    EditText edit_content;
    String content, user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        //writebooth에서 intent할때 보낸 값 받기
        Intent intent = getIntent();
        booth_id = intent.getExtras().getInt("booth_id");

        btn_write_back = (ImageView) findViewById(R.id.btn_write_back);
        btn_write_write = (ImageView) findViewById(R.id.btn_write_write);
        edit_content = (EditText) findViewById(R.id.edit_content);

        user_id = DataUtil.getAppPreferences(getApplicationContext(), "user_id");
        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        // layout 생성
        initLayout();

        btn_write_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_write_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = edit_content.getText().toString().trim();

                if (content.length() == 0) {
                    Toast.makeText(getApplicationContext(), "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                onSignUp();
            }
        });


    }

    private void onSignUp() {
        new NetworkWriteSignUp().execute();

    }

    private void initLayout() {
        //툴바 설정
        mToolBar = (Toolbar) findViewById(R.id.write_toolbar);
        mToolBar.setContentInsetsAbsolute(0, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


    private class NetworkWriteSignUp extends AsyncTask<String, String, Integer> {
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

                http_post = new HttpPost("http://54.199.176.234/api/write_idea.php");

                // 데이터 담음
                name_value.add(new BasicNameValuePair("content", content));
                name_value.add(new BasicNameValuePair("booth_id", booth_id + ""));
                name_value.add(new BasicNameValuePair("user_id", user_id));

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
                    //idea_id받기
//                    String idea_id = jObject.getString("idea_id");
                    jObject.getInt("err");



                    // 여기서인텐트하기
                    Intent itIdeaDetail = new Intent(getApplicationContext(), IdeaDetailActivity.class);
//                    itMain.putExtra("idea_id",idea_id);//booth_id를어떻게넘겨줄지
                    startActivity(itIdeaDetail);
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
