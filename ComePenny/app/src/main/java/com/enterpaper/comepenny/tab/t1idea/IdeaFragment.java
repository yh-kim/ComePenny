package com.enterpaper.comepenny.tab.t1idea;

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
import android.widget.TextView;
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
    TextView tv_name;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    IdeaAdapter adapters;
    IdeaPopularAdapter adapter;
    ArrayList<IdeaListItem> dataList = new ArrayList<>();
    LinearLayout recycler_info;
    List<IdeaPopularListItem> items = new ArrayList<>();
    LinearLayoutManager layoutmanager;
    private Intent intent = new Intent();
    String img_url;

    public static Fragment newInstance() {
        Fragment fragment = new IdeaFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }




    //다른 activity에 갔다가 돌아왔을때 실행되는 코드, onCreate()실행되고 뭐 실행되고 뭐실행되고 실행되는게 onResume()
    public void onResume() {
        super.onResume();

        //초기화 & 쓰레드 실행
        initializationList();

    }

    //Initlist (초기화 메소드)
    public void initializationList() {
        //초기화
        is_scroll = true;
        offset = 0;
        dataList.clear();

        //쓰레드 실행

        new NetworkGetMainIdeaList().execute("");
        return;
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
        tv_name = (TextView)popular_view.findViewById(R.id.tv_name);

        // 헤더레이아웃 객체 생성
        initializeLayout();


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


        initializeListener();

        return rootView;
    }


    // headerlayout
    private void initializeLayout() {
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

    private void initializeListener() {
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
                fab.attachToListView(lvMainIdea);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    //서버로부터 받아온 List개수를 count
                    //지금까지 받아온 개수를 offset
                    if (count != 0 && offset % row_cnt == 0) {
                        if (is_scroll) {
                            //스크롤 멈추게 하는거
                            is_scroll = false;
                            new NetworkGetMainIdeaList().execute("");
                        }
                    }

            }
        });
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
                Log.i("Network popluar Data", jObject.toString());

                // JSON에서 받은 객체를 가지고 List에 뿌려줘야해
                // jObject에서 데이터를 뽑아내자
                try {
                    JSONArray ret_arr = jObject.getJSONArray("ret");
                    for (int index = 0; index < ret_arr.length(); index++) {
                        JSONObject obj = ret_arr.getJSONObject(index);

                        int booth_id = obj.getInt("id");
                       img_url = booth_id+"";
                        String booth_name = obj.getString("name");


                        //Item 객체로 만들어야함
                        IdeaPopularListItem item = new IdeaPopularListItem(booth_id, R.drawable.ex11,img_url,booth_name);

                        //Item 객체를 ArrayList에 넣는다
                        items.add(item);

                        // Adapter에게 데이터를 넣었으니 갱신하라고 알려줌
                        adapter.notifyDataSetChanged();
                    }


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

                http_post = new HttpPost("http://54.199.176.234/api/get_booth_list.php");

                name_value.add(new BasicNameValuePair("booth_is_main", "1"));


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
                        int booth_id = obj_boothIdeas.getInt("booth_id");
                        img_url = booth_id+"";
                        int idea_id = obj_boothIdeas.getInt("id");
                        String content = obj_boothIdeas.getString("content");
                        int hit = obj_boothIdeas.getInt("hit");
                        int like_num = obj_boothIdeas.getInt("like_num");
                        String getemail = obj_boothIdeas.getString("email");

                        byte[] mailarray = getemail.getBytes();
                        String email_view = new String(mailarray,0,3);
                        String hide_email = email_view +"*****";

                        // Item 객체로 만들어야함
                        IdeaListItem items = new IdeaListItem(img_url, content, hide_email, hit, like_num, idea_id);

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

                //서버에 보낼 데이터
                // data를 담음
                name_value.add(new BasicNameValuePair("offset", offset + ""));


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
