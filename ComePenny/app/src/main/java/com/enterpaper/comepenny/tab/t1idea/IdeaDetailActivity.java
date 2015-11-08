package com.enterpaper.comepenny.tab.t1idea;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.activities.LoadingActivity;
import com.enterpaper.comepenny.activities.MainActivity;
import com.enterpaper.comepenny.util.BaseActivity;
import com.enterpaper.comepenny.util.Capture;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class IdeaDetailActivity extends ActionBarActivity {
    int row_cnt = 6;
    int count = 0;
    int offset = 0;
    boolean is_scroll = true;
    //  private String msg, reg_Time, regTime_str;
    InputMethodManager keyboard;
    private ScrollView scrollView_mainidea_detail;
    Toolbar mToolBar;
    ImageView btn_ideaback,btn_share;
    ListView lvIdeaDetailComment;
    ImageButton btn_pick;
    EditText Edit_reple;
    TextView tv_logo_name, tv_Writer, tv_view, tv_like, tv_ideaoriginal, tv_commentcount, tv_time, Btn_reple, btn_del;
    int pick_boolean = 0;
    View header;
    int idea_id;
    String email, content;
    AlertDialog mDialog;
    CommentAdapter adapters;
    ArrayList<CommentItem> arr_list = new ArrayList<>();
    Activity Capture;

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
        initializeToolbar();

        // 레이아웃 객체 생성
        initializeLayout();

        // 헤더 설정
        lvIdeaDetailComment.addHeaderView(header);

        // Adapter 생성
        adapters = new CommentAdapter(getApplicationContext(), R.layout.row_comment, arr_list);

        // Adapter와 GirdView를 연결
        lvIdeaDetailComment.setAdapter(adapters);
        adapters.notifyDataSetChanged();

        // 액션 리스너 생성
        initializeListener();

        new NetworkGetIdeainfo().execute();
        new NetworkGetCommentList().execute();

    }


    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    public static String formatTimeString(String str) throws ParseException {

        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        java.util.Date date = format.parse(str);

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
// sec
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
// min
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
// hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
// day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
// day
            msg = (diffTime) + "달 전";
        } else {
            msg = str;
        }
        return msg;
    }


    // layout
    private void initializeLayout() {
        //스크린키보드
        keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // 리스트 헤더 부분
        header = getLayoutInflater().inflate(R.layout.activity_idea_detail_header, null, false);

        scrollView_mainidea_detail = (ScrollView) header.findViewById(R.id.scrollView_mainidea_detail);
        btn_pick = (ImageButton) header.findViewById(R.id.btn_pick);
        tv_Writer = (TextView) header.findViewById(R.id.tv_Writer);
        tv_view = (TextView) header.findViewById(R.id.tv_view);
        tv_like = (TextView) header.findViewById(R.id.tv_like);
        tv_time = (TextView) header.findViewById(R.id.tv_time);
        btn_del = (TextView) header.findViewById(R.id.btn_del);
        tv_ideaoriginal = (TextView) header.findViewById(R.id.tv_ideaoriginal);
        tv_commentcount = (TextView) header.findViewById(R.id.tv_comment_view);
        Btn_reple = (TextView) header.findViewById(R.id.Btn_reple);

        // 리스트부분
        lvIdeaDetailComment = (ListView) findViewById(R.id.lv_idea_detail_comments);
        btn_ideaback = (ImageView) findViewById(R.id.btn_ideaback);
        btn_share = (ImageView)findViewById(R.id.btn_share);
        tv_logo_name = (TextView) findViewById(R.id.tv_logo_name);
        Edit_reple = (EditText) header.findViewById(R.id.Edit_reple);

    }

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
        mToolBar = (Toolbar) findViewById(R.id.idea_detail_toolbar);
        mToolBar.setContentInsetsAbsolute(0, 0);
    }

    private void initializeListener() {
        btn_ideaback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                Capture();

            }
        });
        Btn_reple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = Edit_reple.getText().toString().trim();

                if (content.length() == 0) {
                    Toast.makeText(getApplicationContext(), "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 서버에 저장
                new NetworkaddComment().execute();

            }
        });
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog = createDialog();
            }
        });
        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pick_boolean == 0) {
                    btn_pick.setBackgroundResource(R.drawable.detail_pickbutton_after);
                    pick_boolean = 1;
                    new NetworkGetlike().execute();


                } else {
                    btn_pick.setBackgroundResource(R.drawable.detail_pickbutton_before);
                    pick_boolean = 0;
                    new NetworkGetlike().execute();
                }

            }
        });


        lvIdeaDetailComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

        });

        lvIdeaDetailComment.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //서버로부터 받아온 List개수를 count
                //지금까지 받아온 개수를 offset
                if (count != 0 && offset % row_cnt == 0) {
                    if (is_scroll) {
                        //스크롤 멈추게 하는거
                        is_scroll = false;
                        new NetworkGetCommentList().execute("");
                    }
                }
            }
        });

    }
    //dialog
    private AlertDialog createDialog() {
        final View innerView = getLayoutInflater().inflate(R.layout.idea_dialog, null);
        TableRow row1 = (TableRow) innerView.findViewById(R.id.row1);
        TableRow row2 = (TableRow) innerView.findViewById(R.id.row2);
        TableRow row3 = (TableRow) innerView.findViewById(R.id.row3);
        row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new NetworkIdeaRewrite().execute();


            }
        });
        row2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NetworkIdeaDel().execute();

                Toast.makeText(IdeaDetailActivity.this, "idea를 삭제하겠습니다", Toast.LENGTH_SHORT).show();
                new BaseActivity().closeActivity();
                Intent itLoad = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(itLoad);
                finish();
                overridePendingTransition(0, 0);


            }
        });
        row3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();

            }
        });
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setView(innerView);
        ab.setCancelable(true);
        Dialog mDialog = ab.create();
        //dialog크기조절

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(mDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setAttributes(params);
        return ab.create();
    }


    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(0, 0);
    }


    public void Capture() {
        new Capture();

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
                name_value.add(new BasicNameValuePair("user_id", DataUtil.getAppPreferences(getApplicationContext(), "user_id")));


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
                Log.i("Network IdeaHeader Data", jObject.toString());


                // jObject에서 데이터를 뽑아내자
                try {
                    String idea_user_id = jObject.get("user_id").toString();
                    String booth_name = jObject.get("name").toString();
                    String content = jObject.get("content").toString();
                    int hit = jObject.getInt("hit");
                    int like_num = jObject.getInt("like_num");
                    int like = jObject.getInt("like");
                    int comment_num = jObject.getInt("comment_num");

                    //서버에서 date받아와서 formatTimeString이용해서 값 변환
                    String reg_Time = jObject.getString("date");
                    String time = formatTimeString(reg_Time);


                    // tv_Writer.setText(email);

                    String getemail = email;

                    byte[] mailarray = getemail.getBytes();
                    String email_view = new String(mailarray, 0, 3);
                    String hide_email = email_view + "*****";

                    tv_Writer.setText(hide_email);
                    tv_logo_name.setText(booth_name);
                    tv_ideaoriginal.setText(content);
                    tv_view.setText(hit + "");
                    tv_like.setText(like_num + "");
                    tv_time.setText(time);
                    tv_commentcount.setText(comment_num + "");

                    if (like == 1) {
                        pick_boolean = 1;
                        btn_pick.setBackgroundResource(R.drawable.detail_pickbutton_after);
                    } else {
                        pick_boolean = 0;
                        btn_pick.setBackgroundResource(R.drawable.detail_pickbutton_before);
                    }
                    String user_id = DataUtil.getAppPreferences(getApplicationContext(), "user_id");
                    // 글을 쓴 사람이거나 관리자이면
                    if (user_id.equals(idea_user_id) || user_id.equals("0")) {
                        btn_del.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
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
                name_value.add(new BasicNameValuePair("user_id", DataUtil.getAppPreferences(getApplicationContext(), "user_id")));
                if (pick_boolean == 1) {
                    name_value.add(new BasicNameValuePair("is_like", 0 + ""));
                } else {
                    name_value.add(new BasicNameValuePair("is_like", 1 + ""));
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
                jObject = new JSONObject(builder.toString().substring(builder.toString().indexOf("{"), builder.toString().lastIndexOf("}") + 1));

                // err가 0이면 정상적인 처리,err가 0이 아닐시 오류발생
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
                int like_num = 0;
                try {
                    like_num = jObject.getInt("like_num");
                    tv_like.setText(like_num + "");
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

    // 댓글 list HTTP연결 Thread 생성 클래스
    class NetworkGetCommentList extends AsyncTask<String, String, Integer> {
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
                Log.i("Network like Data", jObjects.toString());

                // JSON에서 받은 객체를 가지고 List에 뿌려줘야해
                // jObject에서 데이터를 뽑아내자
                try {
                    // 가져오는 값의 개수를 가져옴
                    count = jObjects.getInt("cnt");
                    offset = offset + count;


                    JSONArray ret_arr = jObjects.getJSONArray("ret");
                    for (int index = 0; index < ret_arr.length(); index++) {
                        JSONObject obj_boothIdeas = ret_arr.getJSONObject(index);

                        String content = obj_boothIdeas.getString("comment");
                        String getemail = obj_boothIdeas.getString("email");


                        byte[] mailarray = getemail.getBytes();
                        String email_view = new String(mailarray, 0, 3);
                        // int email_length = mailarray.length;
                        String hide_email = email_view + "*****";


                        //서버에서 date받아와서 formatTimeString이용해서 값 변환
                        String reg_Time = obj_boothIdeas.getString("date");
                        String comment_time = formatTimeString(reg_Time);


                        // Item 객체로 만들어야함
                        CommentItem items = new CommentItem("img", content, hide_email, comment_time);

                        // Item 객체를 ArrayList에 넣는다
                        //                      arr_list.add(items);
                        arr_list.add(0, items);
                    }

                    // Adapter에게 데이터를 넣었으니 갱신하라고 알려줌
                    adapters.notifyDataSetChanged();

                    // scroll 할 수 있게함
                    is_scroll = true;

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
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
                        "http://54.199.176.234/api/get_comment_list.php");

//                        //서버에 보낼 데이터
                // data를 담음
                name_value.add(new BasicNameValuePair("offset", offset + ""));
                name_value.add(new BasicNameValuePair("idea_id", idea_id + ""));


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


    //
    private class NetworkaddComment extends AsyncTask<String, String, Integer> {
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

                http_post = new HttpPost("http://54.199.176.234/api/write_comment.php");
                String comment = Edit_reple.getText().toString().trim();
                String user_id = DataUtil.getAppPreferences(IdeaDetailActivity.this, "user_id");

                // 데이터 담음
                name_value.add(new BasicNameValuePair("idea_id", idea_id + ""));
                name_value.add(new BasicNameValuePair("comment", comment + ""));
                name_value.add(new BasicNameValuePair("user_id", user_id + ""));

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

                    Edit_reple.setText("");

                    jObject.getInt("err");

                    //arr_list.clear();
                    new NetworkGetCommentList().execute();

                    lvIdeaDetailComment.smoothScrollToPosition(0);


                    int comment_num = jObject.getInt("comment_num");


                    tv_commentcount.setText(comment_num + "");


                    //키보드숨기기
                    keyboard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_SHORT).show();
            return;
        }


    }


    //아이디어 삭제
    private class NetworkIdeaDel extends AsyncTask<String, String, Integer> {
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

                http_post = new HttpPost("http://54.199.176.234/api/delete_idea.php");

                // 데이터 담음
                name_value.add(new BasicNameValuePair("idea_id", idea_id + ""));

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


                    jObject.getInt("err");



                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_SHORT).show();
            return;
        }


    }
//    ///아이디어 수정

    private class NetworkIdeaRewrite extends AsyncTask<String, String, Integer> {
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

                http_post = new HttpPost("http://54.199.176.234/api/modify_idea.php");

                // 데이터 담음
                name_value.add(new BasicNameValuePair("idea_id", idea_id + ""));
                name_value.add(new BasicNameValuePair("content",content));

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


                    jObject.getInt("err");


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
