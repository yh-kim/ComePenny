package com.enterpaper.comepenny.tab.t1idea;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.enterpaper.comepenny.R;
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

public class IdeaDetailActivity extends ActionBarActivity {
    private ScrollView scrollView_mainidea_detail;
    Toolbar mToolBar;
    ImageView  btn_ideaback;
    ListView lvIdeaDetailComment;
    ImageButton btn_pick;
    TextView tv_logo_name, tv_Writer, tv_view, tv_like, tv_ideaoriginal;
    int pick_boolean = 0;
    View header;
    int idea_id;
    String email;

    CommentAdapter adapters;
    ArrayList<CommentItem> arr_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_detail);

        //idea_id받기
        Intent itReceive = getIntent();
        idea_id = itReceive.getExtras().getInt("idea_id");
        email = itReceive.getExtras().getString("email");

        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        //Toolbar 생성
        initToolbar();

        // 레이아웃 객체 생성
        initLayout();

        // 헤더 설정
        lvIdeaDetailComment.addHeaderView(header);

        // Adapter 생성
        adapters = new CommentAdapter(getApplicationContext(), R.layout.row_comment, arr_list);

        // Adapter와 GirdView를 연결
        lvIdeaDetailComment.setAdapter(adapters);

        for(int i=0;i <10; i++){
            arr_list.add(new CommentItem());
        }
        adapters.notifyDataSetChanged();

        // 액션 리스너 생성
        initializeAction();

        new NetworkGetIdeainfo().execute();

    }

    // layout
    private void initLayout(){
        // 리스트 헤더 부분
        header = getLayoutInflater().inflate(R.layout.activity_idea_detail_header, null, false);

        scrollView_mainidea_detail = (ScrollView)header.findViewById(R.id.scrollView_mainidea_detail);
        btn_pick = (ImageButton) header.findViewById(R.id.btn_pick);
        tv_Writer = (TextView) header.findViewById(R.id.tv_Writer);
        tv_view = (TextView) header.findViewById(R.id.tv_view);
        tv_like = (TextView) header.findViewById(R.id.tv_like);
        tv_ideaoriginal = (TextView) header.findViewById(R.id.tv_ideaoriginal);

        // 리스트부분
        lvIdeaDetailComment = (ListView)findViewById(R.id.lv_idea_detail_comments);
        btn_ideaback = (ImageView) findViewById(R.id.btn_ideaback);
        tv_logo_name = (TextView) findViewById(R.id.tv_logo_name);

    }

    private void initToolbar() {
        //액션바 객체 생성
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //액션바 설정
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        //액션바 숨김
        actionBar.hide();

        //툴바 설정
        mToolBar = (Toolbar) findViewById(R.id.idea_detail_toolbar);
        mToolBar.setContentInsetsAbsolute(0, 0);
    }

    private void initializeAction(){
        btn_ideaback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pick_boolean == 0) {
                    btn_pick.setBackgroundResource(R.drawable.detail_pickbutton_after);
                    Toast.makeText(getApplicationContext(), "like", Toast.LENGTH_SHORT)
                            .show();
                    pick_boolean = 1;
                    new NetworkGetlike().execute();


                } else {
                    btn_pick.setBackgroundResource(R.drawable.detail_pickbutton_before);
                    Toast.makeText(getApplicationContext(), "unlike", Toast.LENGTH_SHORT)
                            .show();
                    pick_boolean = 0;
                    new NetworkGetlike().execute();
                }
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


    // 아이디어 헤더정보가져오기 - HTTP연결 Thread 생성 클래스
    class NetworkGetIdeainfo extends AsyncTask<String, String, Integer> {
        private String err_msg = "Network error.";

        // JSON에서 받아오는 객체
        private JSONObject jObject;

        // AsyncTask 실행되는거
        @Override
        protected Integer doInBackground(String... params) {

            return processing();
        }

        private Integer processing() {
            try {
                HttpClient http_client = new DefaultHttpClient();
                // 요청한 후 7초 이내에 오지 않으면 timeout 발생하므로 빠져나옴
                http_client.getParams().setParameter("http.connection.timeout",
                        7000);

                // data를 Post방식으로 보냄
                HttpPost http_post = null;

                List<NameValuePair> name_value = new ArrayList<NameValuePair>();

                http_post = new HttpPost(
                        "http://54.199.176.234/api/get_idea_info.php");

                //서버에 보낼 데이터
                // data를 담음
                name_value.add(new BasicNameValuePair("idea_id", idea_id + ""));
                name_value.add(new BasicNameValuePair("user_id", DataUtil.getAppPreferences(getApplicationContext(),"user_id")));

                UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(
                        name_value, "UTF-8");
                http_post.setEntity(entityRequest);

                // 실행
                HttpResponse response = http_client.execute(http_post);

                // 받는 부분
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"), 8);
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null; ) {
                    builder.append(line).append("\n");
                }

                // 우리가 사용하는 결과
//                jObject = new JSONObject(builder.toString());
                jObject = new JSONObject(builder.toString().substring(builder.toString().indexOf("{"), builder.toString().lastIndexOf("}") + 1));

                // err가 0이면 정상적인 처리
                // err가 0이 아닐시 오류발생
                if (jObject.getInt("err") > 0) {
                    return jObject.getInt("err");
                }
            } catch (Exception e) {
                // 오류발생시
                Log.i(err_msg, e.toString());
                return 100;
            }
            return 0;
        }

        // AsyncTask 실행완료 후에 구동 (Data를 받은것을 Activity에 갱신하는 작업을 하면돼)
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            // 지금 코드에서는 result가 0이면 정상적인 상황
            if (result == 0) {
                Log.i("Network Data", jObject.toString());

                // jObject에서 데이터를 뽑아내자
                try {
                    String booth_name = jObject.get("name").toString();
                    String content = jObject.get("content").toString();
                   // String email = jObject.get("email").toString();
                    int hit = jObject.getInt("hit");
                    int like_num = jObject.getInt("like_num");
                    int like = jObject.getInt("like");



                    tv_Writer.setText(email);
                   tv_logo_name.setText(booth_name);
                    tv_ideaoriginal.setText(content);
                    tv_view.setText(hit+"");
                    tv_like.setText(like_num+"");

                    if(like == 1){
                        pick_boolean=1;
                        btn_pick.setBackgroundResource(R.drawable.detail_pickbutton_after);
                    }
                    else{
                        pick_boolean=0;
                        btn_pick.setBackgroundResource(R.drawable.detail_pickbutton_before);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
            // Error 상황
            else {
                Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    //like정보가져오기 - HTTP연결 Thread 생성 클래스
    class NetworkGetlike extends AsyncTask<String, String, Integer> {
        private String err_msg = "Network error.";

        // JSON에서 받아오는 객체
        private JSONObject jObject;

        // AsyncTask 실행되는거
        @Override
        protected Integer doInBackground(String... params) {

            return processing();
        }

        private Integer processing() {
            try {
                HttpClient http_client = new DefaultHttpClient();
                // 요청한 후 7초 이내에 오지 않으면 timeout 발생하므로 빠져나옴
                http_client.getParams().setParameter("http.connection.timeout",
                        7000);

                // data를 Post방식으로 보냄
                HttpPost http_post = null;

                List<NameValuePair> name_value = new ArrayList<NameValuePair>();

                http_post = new HttpPost(
                        "http://54.199.176.234/api/like.php");

                //서버에 보낼 데이터
                // data를 담음
                name_value.add(new BasicNameValuePair("idea_id", idea_id + ""));
                name_value.add(new BasicNameValuePair("user_id", DataUtil.getAppPreferences(getApplicationContext(),"user_id")));
                if(pick_boolean==1) {
                    name_value.add(new BasicNameValuePair("is_like", 0+""));
                }else{
                    name_value.add(new BasicNameValuePair("is_like", 1+""));
                }

                UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(
                        name_value, "UTF-8");
                http_post.setEntity(entityRequest);

                // 실행
                HttpResponse response = http_client.execute(http_post);

                // 받는 부분
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"), 8);
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null; ) {
                    builder.append(line).append("\n");
                }

                // 우리가 사용하는 결과
//                jObject = new JSONObject(builder.toString());
                jObject = new JSONObject(builder.toString().substring(builder.toString().indexOf("{"), builder.toString().lastIndexOf("}") + 1));

                // err가 0이면 정상적인 처리
                // err가 0이 아닐시 오류발생
                if (jObject.getInt("err") > 0) {
                    return jObject.getInt("err");
                }
            } catch (Exception e) {
                // 오류발생시
                Log.i(err_msg, e.toString());
                return 100;
            }
            return 0;
        }

        // AsyncTask 실행완료 후에 구동 (Data를 받은것을 Activity에 갱신하는 작업을 하면돼)
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            // 지금 코드에서는 result가 0이면 정상적인 상황
            if (result == 0) {
                Log.i("Network Data", jObject.toString());

                // jObject에서 데이터를 뽑아내자

                return;
            }
            // Error 상황
            else {
                Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}
