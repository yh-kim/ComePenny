package com.enterpaper.comepenny.activities;

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
import android.widget.ListView;
import android.widget.Toast;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.tab.t1idea.IdeaAdapter;
import com.enterpaper.comepenny.tab.t1idea.IdeaDetailActivity;
import com.enterpaper.comepenny.tab.t1idea.IdeaListItem;
import com.enterpaper.comepenny.util.DataUtil;
import com.enterpaper.comepenny.util.SetFont;

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

public class MyinfoWriteFragment extends Fragment {
    int selectedItem;
    int row_cnt = 6;
    int count = 0;
    int offset = 0;
    boolean is_scroll = false;
    View rootView;
    ListView lv_mywrite;
    IdeaAdapter myadapters;
    String booth_name;
    ArrayList<IdeaListItem> mydataList = new ArrayList<>();
    String name[] = {"게임","공부","도전","독서","애니","예술","브랜드","사랑",
            "스포츠","시간","여행","영화","오글오글","음악","이별","인생","종교","창업",
            "취업","친구","희망","기타"};

    private Intent intent = new Intent();

    public static Fragment newInstance() {
        Fragment fragment = new MyinfoWriteFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_myinfo_write_fragment, container, false);

        // frgment 폰트 설정
        SetFont.setGlobalFont(rootView.getContext(), rootView);

        lv_mywrite = (ListView) rootView.findViewById(R.id.lv_mywrite);
        // Adapter 생성 
        myadapters = new IdeaAdapter(getContext(), R.layout.row_idea, mydataList);
        initializeListener();
        // Adapter와 GirdView를 연결 
        lv_mywrite.setAdapter(myadapters);
        myadapters.notifyDataSetChanged();

        initializationList();


        return rootView;

    }

    private void initializeListener() {
        lv_mywrite.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = position;
                intent.setClass(rootView.getContext(), IdeaDetailActivity.class);
                intent.putExtra("idea_id", mydataList.get(position).getIdea_id());
                intent.putExtra("email", mydataList.get(position).getEmail());
                startActivityForResult(intent, 0);
                getActivity().overridePendingTransition(0, 0);
            }

        });

        lv_mywrite.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) > totalItemCount - 2) {
                    //서버로부터 받아온 List개수를 count
                    //지금까지 받아온 개수를 offset
                    if (count != 0 && offset > 4 && offset % row_cnt == 0) {
                        if (is_scroll) {
                            //스크롤 멈추게 하는거
                            is_scroll = false;
                            new NetworkGetMyWriteList().execute("");
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mydataList.size() == 0) {
            initializationList();
            return;
        }
        switch (resultCode){
            // 일반적 상황 (조회수, 좋아요수, 댓글수, 컨텐츠 업데이트)
            case 1:
                String backContent = data.getStringExtra("backContent");
                int backView = data.getIntExtra("backView", 0);
                int backComment = data.getIntExtra("backComment", 0);
                int backLike = data.getIntExtra("backLike",0);

                IdeaListItem backItem = mydataList.get(selectedItem);
                backItem.setContent(backContent);
                backItem.setViewCount(backView);
                backItem.setCommentCount(backComment);
                backItem.setLikeCount(backLike);

                myadapters.notifyDataSetChanged();
                break;

            // 삭제된 상황 (아이템 지우기)
            case 2:
                mydataList.remove(selectedItem);
                myadapters.notifyDataSetChanged();
                break;
        }
    }


    // mylike list HTTP연결 Thread 생성 클래스
    class NetworkGetMyWriteList extends AsyncTask<String, String, Integer> {
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
                        int comment_num = obj_boothIdeas.getInt("comment_num");
                        int like_num = obj_boothIdeas.getInt("like_num");
                        int booth_id = obj_boothIdeas.getInt("booth_id");
                        String img_url = booth_id+"";
                        String getemail = obj_boothIdeas.getString("email");

                        booth_name = name[booth_id - 1];

                        // Item 객체로 만들어야함
                        IdeaListItem items = new IdeaListItem(img_url, content, getemail,booth_name, hit,comment_num, like_num, idea_id);

                        // Item 객체를 ArrayList에 넣는다
                        mydataList.add(items);


                        // Adapter에게 데이터를 넣었으니 갱신하라고 알려줌
                        myadapters.notifyDataSetChanged();
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
                String user_id = DataUtil.getAppPreferences(getContext(), "user_id");

                //서버에 보낼 데이터
                // data를 담음
                name_value.add(new BasicNameValuePair("user_id", user_id));
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
    //다른 activity에 갔다가 돌아왔을때 실행되는 코드, onCreate()실행되고 뭐 실행되고 뭐실행되고 실행되는게 onResume()
    /*
    public void onResume() {
        super.onResume();

        //초기화 & 쓰레드 실행
        initializationList();

    }
    */

    //Initlist (초기화 메소드)
    public void initializationList() {
        //초기화
        is_scroll = true;
        offset = 0;
        mydataList.clear();

        //쓰레드 실행

        new NetworkGetMyWriteList().execute("");
        return;
    }



}