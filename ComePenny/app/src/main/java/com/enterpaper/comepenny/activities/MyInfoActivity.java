package com.enterpaper.comepenny.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.tab.t1idea.IdeaAdapter;
import com.enterpaper.comepenny.tab.t1idea.IdeaListItem;

import com.enterpaper.comepenny.tab.t1idea.IdeaDetailActivity;
import com.enterpaper.comepenny.tab.t1idea.IdeaPopularAdapter;
import com.enterpaper.comepenny.tab.t1idea.IdeaPopularListItem;

import com.enterpaper.comepenny.util.DataUtil;

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
 * Created by Kim on 2015-09-16.
 */
public class MyInfoActivity extends Activity {
    int row_cnt = 8;
    int count = 0;
    int offset = 0;
    boolean is_scroll = true;

    View myinfoview;
    TextView tv_id, tv_usermail, mDialogTitleView;
    ImageView btn_myinfo_back, mTitleImageView;
    ListView lv_mywrite;
    ImageView img_user, myInfo_divideline;
    private Intent intent = new Intent();
    IdeaAdapter myadapters;
    ArrayList<IdeaListItem> mydataList = new ArrayList<>();
    LinearLayout myinfo, mTitleViewGroup;
    AlertDialog mDialog;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int CROP_FROM_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        //TextView 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        btn_myinfo_back = (ImageView) findViewById(R.id.btn_myinfo_back);
        btn_myinfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_mywrite = (ListView) findViewById(R.id.lv_mywrite);


        //헤더 생성
        myinfoview = getLayoutInflater().inflate(R.layout.activity_myinfo_header, null, false);
        myinfo = (LinearLayout) myinfoview.findViewById(R.id.myinfo);

        // 레이아웃 객체 생성
        initLayout();


        //헤더설정, 헤더부분 리스트뷰리스너작동막기
        lv_mywrite.addHeaderView(myinfoview, myadapters, false);
        // Adapter 생성
        myadapters = new IdeaAdapter(getApplicationContext(), R.layout.row_idea, mydataList);

        // Adapter와 GirdView를 연결
        lv_mywrite.setAdapter(myadapters);
        myadapters.notifyDataSetChanged();

        new NetworkGetMylikeIdeaList().execute("");

        lv_mywrite.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.setClass(getApplicationContext(), IdeaDetailActivity.class);
                intent.putExtra("idea_id", mydataList.get(position - 1).getIdea_id());
                startActivity(intent);
               overridePendingTransition(0, 0);
            }

        });





        lv_mywrite.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });



        return;
    }

    // mylikeidealist HTTP연결 Thread 생성 클래스
    class NetworkGetMylikeIdeaList extends AsyncTask<String, String, Integer> {
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
                        String user_id = obj_boothIdeas.getString("email");


                        // Item 객체로 만들어야함
                        IdeaListItem items = new IdeaListItem("img", content, user_id, hit, like_num,idea_id);

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
                String user_id = DataUtil.getAppPreferences(getApplicationContext(),"user_id");

//                        //서버에 보낼 데이터
                // data를 담음
                name_value.add(new BasicNameValuePair("offset", offset + ""));
                name_value.add(new BasicNameValuePair("user_id", user_id));
//                        // 받아올개수 row_cnt 는 int형이니까 뒤에 ""를 붙이면 String이 되겠지
//                        name_value.add(new BasicNameValuePair("row_cnt", row_cnt + ""));
                        // 데이터를 받아올 시작점
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






    // layout
    private void initLayout() {
        myinfo = (LinearLayout) myinfoview.findViewById(R.id.myinfo);
        img_user = (ImageView) myinfoview.findViewById(R.id.img_user);
        //dialog 호출
//        img_user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog = createDialog();
//
//            }
//        });


        myInfo_divideline = (ImageView) myinfoview.findViewById(R.id.myInfo_divideline);
        tv_id = (TextView) myinfoview.findViewById(R.id.tv_id);
        tv_usermail = (TextView) myinfoview.findViewById(R.id.tv_usermail);
        tv_usermail.setText(DataUtil.getAppPreferences(getApplicationContext(),"user_email"));

    }


//    //dialog
//    private AlertDialog createDialog() {
//        final View innerView = getLayoutInflater().inflate(R.layout.myinfoimg_dialog, null);
//        TableRow dialogtitle = (TableRow) innerView.findViewById(R.id.dialogtitle);
//        TableRow row1_gallery = (TableRow) innerView.findViewById(R.id.row1);
//        TableRow row2_camera = (TableRow) innerView.findViewById(R.id.row2);
//        TableRow row3_basic = (TableRow) innerView.findViewById(R.id.row3);
//
//
//        row1_gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(getApplicationContext(), "dialogtest1", Toast.LENGTH_SHORT)
//                        .show();
//
//
////                doTakePhotoAction();
////                setDismiss(mDialog);
//            }
//        });
//        row2_camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(getApplicationContext(), "dialogtest2", Toast.LENGTH_SHORT)
//                        .show();
//
////                doTakePhotoAction();
////                setDismiss(mDialog);
//            }
//        });
//        row3_basic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(getApplicationContext(), "dialogtest3", Toast.LENGTH_SHORT)
//                        .show();
////                doTakePhotoAction();
////                setDismiss(mDialog);
//            }
//        });
//
//        AlertDialog.Builder ab = new AlertDialog.Builder(this);
//        ab.setView(innerView);
//        ab.setCancelable(true);
//        Dialog mDialog = ab.create();
//        //dialog크기조절
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//        params.copyFrom(mDialog.getWindow().getAttributes());
//        params.width = 800;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//        mDialog.show();
//        Window window = mDialog.getWindow();
//        window.setAttributes(params);
//
//        return ab.create();
//    }
//
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PICK_FROM_CAMERA) {
//            Bundle extras = data.getExtras();
//            if (extras != null) {
//                Bitmap photo = extras.getParcelable("data");
//                img_user.setImageBitmap(photo);
//            }
//        }
//        if (requestCode == PICK_FROM_GALLERY) {
//            Bundle extras2 = data.getExtras();
//            if (extras2 != null) {
//                Bitmap photo = extras2.getParcelable("data");
//                img_user.setImageBitmap(photo);
//            }
//        }
//    }
//
//    /**
//     * 다이얼로그 종료
//     */
//    private void setDismiss(AlertDialog dialog) {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
//    }
//

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

}
