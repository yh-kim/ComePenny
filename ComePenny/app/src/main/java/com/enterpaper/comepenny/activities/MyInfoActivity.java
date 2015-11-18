package com.enterpaper.comepenny.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.tab.ComePennyMyinfoFragmentPagerAdapter;
import com.enterpaper.comepenny.util.BaseActivity;
import com.enterpaper.comepenny.util.DataUtil;
import com.enterpaper.comepenny.util.SetFont;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kim on 2015-09-16.
 */
public class MyInfoActivity extends ActionBarActivity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    static public ViewPager pager_myinfo;
    Toolbar myinfo_toolbar;
    ImageView btn_myinfo_back, img_myinfo_user;
    TextView tv_myinfo_user_mail;
    PagerSlidingTabStrip tabsStrip_myinfo;
    Uri mImageCaptureUri;
    Bitmap photo = null;
    String content;
    Uri uri;
    String my_id,url;
    ImageLoader loader;
    public static boolean copyFile(File srcFile, File destFile) {
        boolean result = false;
        try {
            InputStream in = new FileInputStream(srcFile);
            try {
                result = copyToFile(in, destFile);
            } finally {
                in.close();
            }
        } catch (IOException e) {
            return false;
        }
        return result;
    }

    private static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            OutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        loader = ImageLoader.getInstance();

        // 액티비티 추가
        new BaseActivity().actList.add(MyInfoActivity.this);


        // Text 폰트 지정
        SetFont.setGlobalFont(this, getWindow().getDecorView());

        // 레이아웃 객체 생성
        initializeLayout();

        initializeListener();

        // Toolbar 생성
        initializeToolbar();

        // 탭 생성
        initializeTab();
    }

    private void initializeListener() {
        btn_myinfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_myinfo_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"기본이미지", "사진앨범", "카메라"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MyInfoActivity.this);     // 여기서 this는 Activity의 this

                // 여기서 부터는 알림창의 속성 설정
                builder.setTitle("프로필 사진 설정").setItems(items, new DialogInterface.OnClickListener() {    // 목록 클릭시 설정
                    public void onClick(DialogInterface dialog, int index) {
                        switch (index) {
                            case 0:

                                doDelPhotoAcition();
                                // Toast.makeText(getApplicationContext(), "기본이미지", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                // Toast.makeText(getApplicationContext(), "사진앨범", Toast.LENGTH_SHORT).show();
                                doTakeAlbumAction();

                                break;
                            case 2:
                                // Toast.makeText(getApplicationContext(), "카메라", Toast.LENGTH_SHORT).show();
                                doTakePhotoAction();
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기
            }
        });
    }

    private void initializeLayout() {

        img_myinfo_user = (ImageView) findViewById(R.id.img_my_info_user);

        try{
            //uri 주소를 String으로 가져옴
            my_id = DataUtil.getAppPreferences(getApplicationContext(), "user_id");
            url = my_id + ".jpg";
            uri = Uri.fromFile(new File(this.getExternalFilesDir(Environment.DIRECTORY_DCIM), url));
            String full_path = uri.getPath();
            Log.i("full path", full_path);

            File file = new File(full_path);
            if(!file.isFile()){
                // 이미지가 sd 카드에 저장돼있지 않을 때 서버에서 가져오는 작업
                new NetworkCheckImg().execute();
            }

            //"/" 를 기준으로 나누어 저장
            String[] s_path = full_path.split("/");

            //실제 사진경로만 뽑아옴
            int index = s_path[0].length() + 1;
            String photo_path = full_path.substring(index, full_path.length());

            Log.i("photo_path", photo_path);
            photo = BitmapFactory.decodeFile(photo_path);

            //사진을 바로쓰지말고 bitmap으로 사이즈를 줄여서 처리하자
            BitmapFactory.Options options = new BitmapFactory.Options();
            for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
                    photo = BitmapFactory.decodeFile(photo_path, options);
                break;
            }
            //이미지뷰에 비트맵을 갖다넣는거야
            img_myinfo_user.setImageBitmap(photo);
        }catch(Exception e) {
        }



        btn_myinfo_back = (ImageView) findViewById(R.id.btn_myinfo_back);
        tv_myinfo_user_mail = (TextView) findViewById(R.id.tv_my_info_user_mail);
        tv_myinfo_user_mail.setText(DataUtil.getAppPreferences(getApplicationContext(), "user_email"));
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
        myinfo_toolbar = (Toolbar) findViewById(R.id.myinfo_toolbar);
        myinfo_toolbar.setContentInsetsAbsolute(0, 0);

    }

    private void initializeTab() {
        pager_myinfo = (ViewPager) this.findViewById(R.id.pager_myinfo);
        pager_myinfo.setAdapter(new ComePennyMyinfoFragmentPagerAdapter(getSupportFragmentManager()));

        /* 큰아이콘 탭
        */
        tabsStrip_myinfo = (PagerSlidingTabStrip) this.findViewById(R.id.tabsStrip_myinfo);
        tabsStrip_myinfo.setViewPager(pager_myinfo);


        tabsStrip_myinfo.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    //기본이미지 함수
    public void  doDelPhotoAcition() {

        try{

            File file = this.getExternalFilesDir(Environment.DIRECTORY_DCIM);
            File[] flist = file.listFiles();
            //Toast.makeText(getApplicationContext(), "imgcnt = " + flist.length, Toast.LENGTH_SHORT).show();
            for(int i = 0 ; i < flist.length ; i++)
            {
                String fname = flist[i].getName();
                if(fname.equals(url))
                {
                    flist[i].delete();

                }

            }
            photo=null;
            new NetworkImgdel().execute("");
            img_myinfo_user.setImageBitmap(null);
        }catch(Exception e){
            //Toast.makeText(getApplicationContext(), "파일 삭제 실패 ", Toast.LENGTH_SHORT).show();
            }

    }


    //사진찍는 함수
    public void doTakePhotoAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mImageCaptureUri = createSaveCropFile();
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    //앨범에서 가져오는 함수
    public void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    //빈파일을 만들어서 위치를 알려줌(사진파일을 담을 파일)
    public Uri createSaveCropFile() {
        //경로

        Log.i("uri", "in");

        //파일명 (현재시간의 밀리 세컨드값)
      //  String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";

        String url = my_id + ".jpg";

        //파일 생성 -> 사진찍은걸 파일로 가지고있어야 전송할 수 있어
        //외장메모리 영역에 파일생성
        uri = Uri.fromFile(new File(this.getExternalFilesDir(Environment.DIRECTORY_DCIM), url));
        Log.i("uri", uri.toString());
        //파일 만든 위치를 uri
        return uri;
    }

    //결과 가져오는 함수
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //결과가 제대로 오지 않으면
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                mImageCaptureUri = data.getData();
                File orignal_file = getImageFile(mImageCaptureUri);
                Log.i("ori", orignal_file.getPath());
                //이 파일을 카피하겠다
                mImageCaptureUri = createSaveCropFile();
                File copy_file = new File(mImageCaptureUri.getPath());
                Log.i("copy_uri", mImageCaptureUri.getPath());
                Log.i("copy_file", copy_file.getPath());
                copyFile(orignal_file, copy_file);
            }
            case PICK_FROM_CAMERA: {
                //카메라 앱이 꺼지면 그 사진을 자르기 할꺼야
                Intent intent = new Intent("com.android.camera.action.CROP");

                //(img경로 crop하라)
                intent.setDataAndType(mImageCaptureUri, "image/*");

                // crop한 이미지를 저장할때 200x200 크기로 저장
//                intent.putExtra("outputX", 136); // crop한 이미지의 x축 크기
//                intent.putExtra("outputY", 136); // crop한 이미지의 y축 크기
                intent.putExtra("aspectX", 1); // crop 박스의 x축 비율
                intent.putExtra("aspectY", 1); // crop 박스의 y축 비율
                intent.putExtra("scale", true);
//                intent.putExtra("return-data", true);
                //crop한 output(img파일)을 다시 그 uri에 덮어씀
                intent.putExtra("output", mImageCaptureUri);

                startActivityForResult(intent, CROP_FROM_CAMERA);
                break;
            }
            case CROP_FROM_CAMERA: {
                //사진을 view시키는거
                //uri 주소를 String으로 가져옴
                String full_path = mImageCaptureUri.getPath();
                Log.i("full path", full_path);

                //"/" 를 기준으로 나누어 저장
                String[] s_path = full_path.split("/");

                //실제 사진경로만 뽑아옴
                int index = s_path[0].length() + 1;
                String photo_path = full_path.substring(index, full_path.length());

                Log.i("photo_path", photo_path);

                photo = BitmapFactory.decodeFile(photo_path);

                new NetworkImgRegister().execute();

                //사진을 바로쓰지말고 bitmap으로 사이즈를 줄여서 처리하자
                BitmapFactory.Options options = new BitmapFactory.Options();
                for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
                    try {
                        photo = BitmapFactory.decodeFile(photo_path, options);
                        break;
                    } catch (OutOfMemoryError outOfMemoryError) {

                    }
                    //이미지뷰에 비트맵을 갖다넣는거야
                }
                img_myinfo_user.setImageBitmap(photo);

                break;
            }
        }
    }

    public File getImageFile(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor mCursor = getContentResolver().query(uri, projection, null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc");

        if (mCursor == null || mCursor.getCount() < 1) {
            return null;
        }

        int column_index = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();

        String path = mCursor.getString(column_index);

        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }

        return new File(path);
    }

    //HTTP연결 Thread 생성 클래스
    private class NetworkImgRegister extends AsyncTask<String, String, Integer> {
        private String err_msg = "Network error.";

        private ProgressDialog dialog;
        //JSON에서 받아오는 객체
        private JSONObject jObject;

        //AsyncTask 실행되는거
        @Override
        protected Integer doInBackground(String... params) {

            return processing();
        }

        //AsyncTask 실행되기 전에 구동
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //기다리라는 dialog 추가
            dialog = ProgressDialog.show(MyInfoActivity.this, "", "잠시만 기다려주세요", true);

        }

        //AsyncTask 실행완료 후에 구동 (Data를 받은것을 Activity에 갱신하는 작업을 하면돼)
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            dialog.cancel();

            if (result == 100) {
                //인터넷 오류
                Toast.makeText(getApplicationContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                return;
            } else if (result > 0) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                return;

            } else if (result == 0) {
                //정상처리
                Toast.makeText(getApplicationContext(), "저장 되었습니다", Toast.LENGTH_SHORT).show();
//                finish();
            }
        }

        private Integer processing() {
            try {
                HttpClient http_client = new DefaultHttpClient();
                //요청한 후 7초 이내에 오지 않으면 timeout 발생하므로 빠져나옴
                http_client.getParams().setParameter("http.connection.timeout", 7000);

                //data를 Post방식으로 보냄
                HttpPost http_post = null;

                List<NameValuePair> name_value = new ArrayList<NameValuePair>();

                http_post = new HttpPost("http://54.199.176.234/api/set_profile.php");
                //web browser랑 호환되게 함
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                //data를 담음
                reqEntity.addPart("user_id", new StringBody(DataUtil.getAppPreferences(MyInfoActivity.this, "user_id"), Charset.defaultCharset()));

                //img 담는거
                if (photo != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                    byte[] data = bos.toByteArray();
                    ByteArrayBody bab = new ByteArrayBody(data, "photo_img.ipeg");
                    reqEntity.addPart("img", bab);
                }else {
                    return 33;
                }

                http_post.setEntity(reqEntity);

                //서버전송
                HttpResponse response = http_client.execute(http_post);


                // 받는 부분
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"), 8);
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null; ) {
                    builder.append(line).append("\n");
                }


                //우리가 사용하는 결과
                jObject = new JSONObject(builder.toString());
//                jObject = new JSONObject(builder.toString().substring(builder.toString().indexOf("{"), builder.toString().lastIndexOf("}") + 1));
                //err가 0이면 정상적인 처리
                //err가 0이 아닐시 오류발생
                if (jObject.getInt("err") > 0) {
                    return jObject.getInt("err");
                }

            } catch (Exception e) {
                //오류발생시
                Log.i(err_msg, e.toString());
                return 100;
            }
            return 0;
        }
    }

    // HTTP 연결 Login Thread 클래서
    private class NetworkImgdel extends AsyncTask<String, String, Integer>{

        // JSON 받아오는 객체
        private JSONObject jObject;

        @Override
        protected Integer doInBackground(String... params) {
            return processing();
        }

        // 서버 연결
        private Integer processing(){
            try {
                HttpClient http_client = new DefaultHttpClient();

                // 요청 후 7초 이내에 응답없으면 timeout 발생
                http_client.getParams().setParameter("http.connection.timeout",7000);
                // post 방식
                HttpPost http_post = null;

                List<NameValuePair> name_value = new ArrayList<NameValuePair>();

                http_post = new HttpPost("http://54.199.176.234/api/delete_img.php");
                String user_id = DataUtil.getAppPreferences(getApplicationContext(),"user_id");
                // 데이터 담음 키,value
                name_value.add(new BasicNameValuePair("user_id", user_id));

                UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(name_value, "UTF-8");
                http_post.setEntity(entityRequest);


                // 서버 전송
                HttpResponse response = http_client.execute(http_post);

                // 받는 부분
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"UTF-8"),8);
                StringBuilder builder = new StringBuilder();
                for(String line = null; (line = reader.readLine()) !=null;){
                    builder.append(line).append("\n");
                }

                // json
                jObject = new JSONObject(builder.toString());
                // callback 오류 뜰 때
//                jObject = new JSONObject(builder.toString().substring(builder.toString().indexOf("{"), builder.toString().lastIndexOf("}") + 1));

                // 0이면 정상, 0이 아니면 오류 발생
                if(jObject.getInt("err") > 0){
                    return jObject.getInt("err");
                }

            } catch (Exception e){
                // 오류발생시
                e.printStackTrace();
                return 100;
            }
            return 0;
        }

        // 값 받는 부분
        @Override
        protected void onPostExecute(Integer result) {
            // 정상적으로 로그인
            if(result == 0){
                return;
            }

            Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_SHORT).show();
            return;
        }

    }


    private class NetworkCheckImg extends AsyncTask<String, String, Integer>{

        // JSON 받아오는 객체
        private JSONObject jObject;

        @Override
        protected Integer doInBackground(String... params) {
            return processing();
        }

        // 서버 연결
        private Integer processing(){
            try {
                HttpClient http_client = new DefaultHttpClient();

                // 요청 후 7초 이내에 응답없으면 timeout 발생
                http_client.getParams().setParameter("http.connection.timeout",7000);
                // post 방식
                HttpPost http_post = null;

                List<NameValuePair> name_value = new ArrayList<NameValuePair>();

                http_post = new HttpPost("http://54.199.176.234/api/check_img.php");
                String user_id = DataUtil.getAppPreferences(getApplicationContext(),"user_id");
                // 데이터 담음 키,value
                name_value.add(new BasicNameValuePair("user_id", user_id));

                UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(name_value, "UTF-8");
                http_post.setEntity(entityRequest);


                // 서버 전송
                HttpResponse response = http_client.execute(http_post);

                // 받는 부분
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"UTF-8"),8);
                StringBuilder builder = new StringBuilder();
                for(String line = null; (line = reader.readLine()) !=null;){
                    builder.append(line).append("\n");
                }

                // json
                jObject = new JSONObject(builder.toString());
                // callback 오류 뜰 때
//                jObject = new JSONObject(builder.toString().substring(builder.toString().indexOf("{"), builder.toString().lastIndexOf("}") + 1));

                // 0이면 정상, 0이 아니면 오류 발생
                if(jObject.getInt("err") > 0){
                    return jObject.getInt("err");
                }

            } catch (Exception e){
                // 오류발생시
                e.printStackTrace();
                return 100;
            }
            return 0;
        }

        // 값 받는 부분
        @Override
        protected void onPostExecute(Integer result) {
            // 정상처리
            if(result == 0){
                try {
                    String image_o = jObject.get("image_o").toString();

                    if(image_o.contains("null")){
                        return;
                    }
                    else{
                        loader.displayImage("https://s3-ap-northeast-1.amazonaws.com/comepenny/" + image_o, img_myinfo_user);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return;
            }

            Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}

