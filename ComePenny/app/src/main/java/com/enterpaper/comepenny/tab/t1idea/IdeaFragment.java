package com.enterpaper.comepenny.tab.t1idea;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.activities.WriteBoothActivity;
import com.enterpaper.comepenny.util.SetFont;
import com.melnykov.fab.FloatingActionButton;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
public class IdeaFragment extends Fragment {
    int row_cnt = 6;
    int count = 0;
    int offset = 0;
    boolean is_scroll = true;
    View rootView, popular_view;
    ListView lvMainIdea;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    private Intent intent = new Intent();
    IdeaAdapter adapters;
    IdeaPopularAdapter adapter;
    ArrayList<IdeaListItem> dataList = new ArrayList<>();
    LinearLayout recycler_info;
    List<IdeaPopularListItem> items = new ArrayList<>();
    LinearLayoutManager layoutmanager;

    public static Fragment newInstance() {
        Fragment fragment = new IdeaFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_idea, container, false);

        // frgment 폰트 설정
        SetFont.setGlobalFont(rootView.getContext(), rootView);

        lvMainIdea = (ListView) rootView.findViewById(R.id.lv_main_idea);


        //헤더 생성
        popular_view = inflater.inflate(R.layout.fragment_idea_header, null, false);
        recycler_info = (LinearLayout) popular_view.findViewById(R.id.recycler_info);
        recyclerView = (RecyclerView) popular_view.findViewById(R.id.recyclerview);

        // 헤더레이아웃 객체 생성
        inithearLayout();


        //헤더설정
        lvMainIdea.addHeaderView(popular_view);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.attachToListView(lvMainIdea);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itWrite = new Intent(rootView.getContext(), WriteBoothActivity.class);
                startActivity(itWrite);
                getActivity().overridePendingTransition(0, 0);
            }
        });


        // Adapter 생성
        adapters = new IdeaAdapter(rootView.getContext(), R.layout.row_idea, dataList);

        // Adapter와 GirdView를 연결
        lvMainIdea.setAdapter(adapters);
        adapters.notifyDataSetChanged();//값이 변경됨을 알려줌
        new NetworkGetMainIdeaList().execute("");
        lvMainIdea.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.setClass(rootView.getContext(), IdeaDetailActivity.class);
                intent.putExtra("idea_id", dataList.get(position - 1).getIdea_id());
                intent.putExtra("email", dataList.get(position - 1).getEmail());
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }

        });

        lvMainIdea.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                fab.attachToListView(lvMainIdea);


            }
        });

        return rootView;
    }


    // headerlayout
    private void inithearLayout() {
        recycler_info = (LinearLayout) popular_view.findViewById(R.id.recycler_info);
        recyclerView = (RecyclerView) popular_view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(popular_view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        items = new ArrayList<>();
        adapter = new IdeaPopularAdapter(popular_view.getContext(), items, R.layout.row_idea_popular);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        new NetworkGetPopularBoothList().execute("");
    }


    // popular boothlist HTTP연결 Thread 생성 클래스
    class NetworkGetPopularBoothList extends AsyncTask<String, String, Integer> {
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


                        // Item 객체로 만들어야함
                        IdeaPopularListItem item = new IdeaPopularListItem(booth_id, R.drawable.ex1);

                        // Item 객체를 ArrayList에 넣는다
                        items.add(item);

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
                for (String line = null; (line = reader.readLine()) != null; ) {
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

    // main ideabooth list HTTP연결 Thread 생성 클래스
    class NetworkGetMainIdeaList extends AsyncTask<String, String, Integer> {
        private String err_msg = "Network error.";

        // JSON에서 받아오는 객체
        private JSONObject jObjects;

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
                Log.i("Network Data", jObjects.toString());

                // JSON에서 받은 객체를 가지고 List에 뿌려줘야해
                // jObject에서 데이터를 뽑아내자
                try {
                    // 가져오는 값의 개수를 가져옴
                    count = jObjects.getInt("cnt");
                    offset = offset + count;
                    JSONArray ret_arr = jObjects.getJSONArray("ret");
                    for (int index = 0; index < ret_arr.length(); index++) {
                        JSONObject obj_boothIdeas = ret_arr.getJSONObject(index);

                        int idea_id = obj_boothIdeas.getInt("id");
                        String content = obj_boothIdeas.getString("content");
                        int hit = obj_boothIdeas.getInt("hit");
                        int like_num = obj_boothIdeas.getInt("like_num");
                        String email = obj_boothIdeas.getString("email");


                        // Item 객체로 만들어야함
                        IdeaListItem items = new IdeaListItem("img", content, email, hit, like_num, idea_id);

                        // Item 객체를 ArrayList에 넣는다
                        dataList.add(items);


                        // Adapter에게 데이터를 넣었으니 갱신하라고 알려줌
                        adapters.notifyDataSetChanged();
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
                        "http://54.199.176.234/api/get_idea_list.php");


                // data를 담음//서버에 보낼 데이터
                // 데이터를 받아올 시작점
                name_value.add(new BasicNameValuePair("offset", offset + ""));
//                        // 받아올개수 row_cnt 는 int형이니까 뒤에 ""를 붙이면 String이 되겠지
                name_value.add(new BasicNameValuePair("row_cnt", row_cnt + ""));
//
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
                jObjects = new JSONObject(builder.toString());

                // err가 0이면 정상적인 처리
                // err가 0이 아닐시 오류발생
                if (jObjects.getInt("err") > 0) {
                    return jObjects.getInt("err");
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
