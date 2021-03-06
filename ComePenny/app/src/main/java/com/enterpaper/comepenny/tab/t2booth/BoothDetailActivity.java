package com.enterpaper.comepenny.tab.t2booth;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.activities.WriteActivity;
import com.enterpaper.comepenny.tab.t1idea.IdeaAdapter;
import com.enterpaper.comepenny.tab.t1idea.IdeaDetailActivity;
import com.enterpaper.comepenny.tab.t1idea.IdeaListItem;
import com.enterpaper.comepenny.util.DataUtil;
import com.enterpaper.comepenny.util.SetFont;
import com.flurry.android.FlurryAgent;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import com.nostra13.universalimageloader.core.ImageLoader;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kim on 2015-09-16.
 */
public class BoothDetailActivity extends ActionBarActivity {
    String userId;
    int selectedItem;
    FloatingActionButton fab;
    int booth_id;
    int row_cnt = 6;
    int count = 0;
    int offset = 0;
    String img_url,booth_name;
    boolean is_scroll = true;
    ListView lvBoothDetailIdea;
    TextView tv_logo_name, booth_main_idea, booth_explanation;
    ImageLoader loader = ImageLoader.getInstance();
    IdeaAdapter adapters;
    ArrayList<IdeaListItem> dataList = new ArrayList<>();

    String name[] = {"게임","공부","도전","독서","애니","예술","브랜드","사랑",
            "스포츠","시간","여행","영화","오글오글","음악","이별","인생","종교","창업",
            "취업","친구","희망","기타"};

    Toolbar mToolBar;
    ImageView btnBoothBack, btnBoothInfo, btnBoothInfoClose,img_booth;
    LinearLayout lyBoothInfo;
    View header;

    private void addLog(){
        Map<String, String> boothParams = new HashMap<String, String>();
        boothParams.put("booth_name", name[booth_id-1]);

        FlurryAgent.logEvent("Booth_view", boothParams, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booth_detail);

        //boothFragment에서 intent할때 보낸 값 받기
        Intent intent = getIntent();
        booth_id = intent.getExtras().getInt("booth_id");

        // 로그저장
        addLog();

        userId = DataUtil.getAppPreferences(getApplicationContext(), "user_id");
        //리스트부분
        lvBoothDetailIdea = (ListView)findViewById(R.id.lv_booth_detail_idea);
        // 리스트 헤더 부분
        header = getLayoutInflater().inflate(R.layout.activity_booth_detail_header, null, false);
        // header.selec
        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());
        SetFont.setGlobalFont(header.getContext(), header);

        //Toolbar 생성
        initializeToolbar();

        // 레이아웃 객체 생성
        initializeLayout();

        initializationList();

        // 헤더 설정, 헤더에 리스트뷰리스너막음 + position-1
        lvBoothDetailIdea.addHeaderView(header, adapters, false);

        // Adapter 생성
        adapters = new IdeaAdapter(this, R.layout.row_idea, dataList);

        // Adapter와 GirdView를 연결
        lvBoothDetailIdea.setAdapter(adapters);


        lvBoothDetailIdea.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = position - 1;

                Intent booth_ideas = new Intent(getApplicationContext(), IdeaDetailActivity.class);
                booth_ideas.putExtra("idea_id", dataList.get(position-1).getIdea_id());//헤더를 position0으로인식하기때문
                booth_ideas.putExtra("email",dataList.get(position-1).getEmail());
                startActivityForResult(booth_ideas, 0);
                overridePendingTransition(0, 0);
            }

        });


        lvBoothDetailIdea.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(booth_id == 7 && !userId.equals("0")){
                    // 브랜드 카테고리인데 일반 유저라면
                    // 숨기기
                    fab.hide();

                    if ((firstVisibleItem + visibleItemCount) > totalItemCount - 2) {
                        //서버로부터 받아온 List개수를 count
                        //지금까지 받아온 개수를 offset
                        if (count != 0 && offset > 3 && offset % 6 == 0) {
                            if (is_scroll) {
                                //스크롤 멈추게 하는거
                                is_scroll = false;
                                new NetworkGetBoothIdeaList().execute("");
                            }
                        }
                    }
                }else{
                    fab.attachToListView(lvBoothDetailIdea, new ScrollDirectionListener() {
                        @Override
                        public void onScrollDown() {

                        }

                        @Override
                        public void onScrollUp() {

                        }
                    }, listListener);
                }
            }
        });




//        btnBoothInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lyBoothInfo.setVisibility(View.VISIBLE);
//            }
//        });
//
//        btnBoothInfoClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lyBoothInfo.setVisibility(View.INVISIBLE);
//
//            }
//        });

        btnBoothBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    AbsListView.OnScrollListener listListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if ((firstVisibleItem + visibleItemCount) == totalItemCount - 2) {
                //서버로부터 받아온 List개수를 count
                //지금까지 받아온 개수를 offset
                if (count != 0 && offset > 3 && offset % 6 == 0) {
                    if (is_scroll) {
                        //스크롤 멈추게 하는거
                        is_scroll = false;
                        new NetworkGetBoothIdeaList().execute("");
                    }
                }
            }
        }
    };

    private void initializeToolbar() {
        //액션바 객체 생성
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //액션바 설정
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        //액션바 숨김
        actionBar.hide();

        //툴바 설정
        mToolBar = (Toolbar) findViewById(R.id.booth_detail_toolbar);
        mToolBar.setContentInsetsAbsolute(0, 0);
    }


    // layout
    private void initializeLayout() {


        tv_logo_name = (TextView) findViewById(R.id.tv_logo_name);
        // booth_explanation = (TextView) header.findViewById(R.id.booth_explanation);
        booth_main_idea = (TextView) header.findViewById(R.id.booth_main_idea);
        // lyBoothInfo = (LinearLayout) header.findViewById(R.id.booth_info);
        btnBoothBack = (ImageView) findViewById(R.id.btn_booth_back);
        // btnBoothInfo = (ImageView) header.findViewById(R.id.btn_booth_info);
        // btnBoothInfoClose = (ImageView) header.findViewById(R.id.btn_booth_info_close);
        lvBoothDetailIdea = (ListView) findViewById(R.id.lv_booth_detail_idea);
        img_booth = (ImageView)header.findViewById(R.id.img_booth);

        fab = (FloatingActionButton) findViewById(R.id.fab_booth);
        fab.attachToListView(lvBoothDetailIdea);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(booth_id == 7 && !userId.equals("0")){
                    return;
                }

                Intent itWrite = new Intent(getApplicationContext(), WriteActivity.class);
                itWrite.putExtra("booth_id", booth_id);
                startActivityForResult(itWrite, 3);
                overridePendingTransition(0, 0);
            }
        });

    }

    //취소버튼 눌렀을 때
    @Override
    public void onBackPressed() {
//        if (lyBoothInfo.getVisibility() == View.VISIBLE) {
//            lyBoothInfo.setVisibility(View.INVISIBLE);
//            return;
//        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        FlurryAgent.endTimedEvent("Booth_view");
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (dataList.size() == 0) {
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

                IdeaListItem backItem = dataList.get(selectedItem);
                backItem.setContent(backContent);
                backItem.setViewCount(backView);
                backItem.setCommentCount(backComment);
                backItem.setLikeCount(backLike);

                adapters.notifyDataSetChanged();
                break;

            // 삭제된 상황 (아이템 지우기)
            case 2:
                dataList.remove(selectedItem);
                adapters.notifyDataSetChanged();
                break;

            // 글 쓰기 했을 때
            case 3:
                initializationList();
                break;
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
    private void initializationList() {
        //초기화
        is_scroll = true;
        offset = 0;
        dataList.clear();

        //쓰레드 실행

        new NetworkGetBoothIdeaList().execute("");
        new NetworkGetBoothinfo().execute("");
        return;
    }


    //헤더정보가져오기 - HTTP연결 Thread 생성 클래스
    class NetworkGetBoothinfo extends AsyncTask<String, String, Integer> {
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
                // jObject에서 데이터를 뽑아내자
                try {

                    String logo_name = jObject.get("name").toString();
                    tv_logo_name.setText(logo_name);
                    //  String explanation = jObject.getString("explanation");
                    // booth_explanation.setText(explanation);
                    String idea_num = jObject.getString("idea_num");
                    booth_main_idea.setText(idea_num);
                    img_url = booth_id+"";

                    loader.displayImage("https://s3-ap-northeast-1.amazonaws.com/comepenny/booth/"+img_url+".png",img_booth);



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
                        "http://54.199.176.234/api/get_booth_info.php");

                //서버에 보낼 데이터
                // data를 담음
                name_value.add(new BasicNameValuePair("booth_id", booth_id + ""));

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

    // boothideas list HTTP연결 Thread 생성 클래스
    class NetworkGetBoothIdeaList extends AsyncTask<String, String, Integer> {
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
                        "http://54.199.176.234/api/get_idea_list.php");

//                        //서버에 보낼 데이터
                // data를 담음
                name_value.add(new BasicNameValuePair("booth_id", booth_id + ""));
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