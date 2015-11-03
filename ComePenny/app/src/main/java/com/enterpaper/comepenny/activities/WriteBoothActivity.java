package com.enterpaper.comepenny.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.tab.t2booth.BoothItem;
import com.enterpaper.comepenny.tab.t2booth.WriteBoothAdapter;
import com.enterpaper.comepenny.util.SetFont;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimmiri on 2015. 10. 30..
 */
public class WriteBoothActivity extends ActionBarActivity{
    int row_cnt = 8;
    int count = 0;
    int offset = 0;
    boolean is_scroll = true;

    ImageView btn_select_back;
    Toolbar mToolBar;
    GridView booth_list;

    // Adapter
    WriteBoothAdapter adapter_sel_booth;
    // ArrayList
    ArrayList<BoothItem> arr_list = new ArrayList<BoothItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_booth_select);

        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        // layout 생성
        initializeLayout();

        initializeList();

        initializeListener();
    }

    //Initlist (초기화 메소드)
    public void initializeList(){
        //초기화
        is_scroll = true;
        offset = 0;
        arr_list.clear();

        //쓰레드 실행
        new NetworkGetBoothList().execute("");
        return;
    }


    private void initializeLayout(){
        //액션바 객체 생성
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //액션바 설정
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        //액션바 숨김
        actionBar.hide();

        //툴바 설정
        mToolBar = (Toolbar) findViewById(R.id.write_select_toolbar);
        mToolBar.setContentInsetsAbsolute(0, 0);

        booth_list = (GridView)findViewById(R.id.gv_select_booth);
        btn_select_back =(ImageView)findViewById(R.id.btn_select_back);

        // Adapter 생성
        adapter_sel_booth = new WriteBoothAdapter(getApplicationContext(), R.layout.write_row_booth, arr_list);

        // Adapter와 GirdView를 연결
        booth_list.setAdapter(adapter_sel_booth);

        adapter_sel_booth.notifyDataSetChanged();
    }

    private void initializeListener(){
        booth_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent company = new Intent(getApplicationContext(), WriteActivity.class);
                company.putExtra("booth_id", arr_list.get(position).getBooth_id());
                startActivity(company);
                overridePendingTransition(0, 0);
                finish();


            }
        });

        booth_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    //서버로부터 받아온 List개수를 count
                    //지금까지 받아온 개수를 offset
                    if (count != 0 && offset % row_cnt == 0) {
                        if (is_scroll) {
                            //스크롤 멈추게 하는거
                            is_scroll = false;
                            new NetworkGetBoothList().execute("");
                        }
                    }
                }


            }
        });

        btn_select_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(0, 0);

            }
        });
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    // HTTP연결 Thread 생성 클래스
    class NetworkGetBoothList extends AsyncTask<String, String, Integer> {
        private String err_msg = "Network error.";

        // JSON에서 받아오는 객체
        private JSONObject jObject;

        // AsyncTask 실행되는거
        @Override
        protected Integer doInBackground(String... params) {

            return processing();
        }


        // AsyncTask 실행완료 후에 구동 (Data를 받은것을 Activity에 갱신하는 작업을 하면돼)
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            // 지금 코드에서는 result가 0이면 정상적인 상황
            if (result == 0) {
                Log.i("Network Data", jObject.toString());

                // JSON에서 받은 객체를 가지고 List에 뿌려줘야해
                // jObject에서 데이터를 뽑아내자
                try {
                    // 가져오는 값의 개수를 가져옴
                    count = jObject.getInt("cnt");
                    offset = offset + count;
                    JSONArray ret_arr = jObject.getJSONArray("ret");
                    for (int index = 0; index < ret_arr.length(); index++) {
                        JSONObject obj = ret_arr.getJSONObject(index);

                        int booth_id = obj.getInt("id");
                        int ideaNum = obj.getInt("idea_num");
                        int likeNum =obj.getInt("like_num");

                        // Item 객체로 만들어야함
                        BoothItem item = new BoothItem("img","name",booth_id,ideaNum,likeNum);

                        // Item 객체를 ArrayList에 넣는다
                        arr_list.add(item);

                        // Adapter에게 데이터를 넣었으니 갱신하라고 알려줌
                        adapter_sel_booth.notifyDataSetChanged();
                    }

                    // scroll 할 수 있게함
                    is_scroll = true;

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
                        "http://54.199.176.234/api/get_booth_list.php");

//                        //서버에 보낼 데이터
//                        // data를 담음
//                        name_value.add(new BasicNameValuePair("booth_id", id));
//                        // 받아올개수 row_cnt 는 int형이니까 뒤에 ""를 붙이면 String이 되겠지
//                        name_value.add(new BasicNameValuePair("row_cnt", row_cnt + ""));
//                        // 데이터를 받아올 시작점
//                        name_value.add(new BasicNameValuePair("offset", offset + ""));
//
//                        UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(
//                                name_value, "UTF-8");
//                        http_post.setEntity(entityRequest);

                // 실행
                HttpResponse response = http_client.execute(http_post);

                // 받는 부분
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"), 8);
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null;) {
                    builder.append(line).append("\n");
                }

                // 우리가 사용하는 결과
                jObject = new JSONObject(builder.toString());

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

    }

}
