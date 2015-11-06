package com.enterpaper.comepenny.tab.t2booth;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.tab.t1idea.IdeaPopularListItem;
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
 * Created by Kim on 2015-09-26.
 */
public class BoothFragment extends Fragment {
    int row_cnt = 8;
    int count = 0;
    int offset = 0;
    boolean is_scroll = true;

    View rootView;
    GridView main_list;
    // Adapter
    BoothAdapter adapter;
    // ArrayList
    ArrayList<BoothItem> arr_list = new ArrayList<BoothItem>();

    public static Fragment newInstance() {
        Fragment fragment = new BoothFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_booth,container,false);

        // frgment 폰트 설정
        SetFont.setGlobalFont(rootView.getContext(), rootView);

        initializeLayout();

        initializeList();

        initializeListener();

        return rootView;
    }

    private void initializeLayout(){
        main_list = (GridView)rootView.findViewById(R.id.gv_main_booth);

        // Adapter 생성
        adapter = new BoothAdapter(rootView.getContext(), R.layout.row_booth, arr_list);

        // Adapter와 GirdView를 연결
        main_list.setAdapter(adapter);
    }

    private void initializeListener(){
        main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent company = new Intent(rootView.getContext(), BoothDetailActivity.class);
                company.putExtra("booth_id", arr_list.get(position).getBooth_id());
                startActivity(company);

                getActivity().overridePendingTransition(0, 0);
            }
        });

        main_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }


    //다른 activity에 갔다가 돌아왔을때 실행되는 코드, onCreate()실행되고 뭐 실행되고 뭐실행되고 실행되는게 onResume()
    public void onResume() {
        super.onResume();

        //초기화 & 쓰레드 실행
//        initializeList();

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

//
                        // Item 객체로 만들어야함
                        BoothItem item = new BoothItem(R.drawable.ex1,"name",booth_id,ideaNum,likeNum);

                        // Item 객체를 ArrayList에 넣는다
                        arr_list.add(item);


                        // Adapter에게 데이터를 넣었으니 갱신하라고 알려줌
                        adapter.notifyDataSetChanged();
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
                Toast.makeText(getContext(), "Error",
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
