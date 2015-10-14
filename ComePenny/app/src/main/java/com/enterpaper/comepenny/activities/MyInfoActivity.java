package com.enterpaper.comepenny.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.enterpaper.comepenny.tab.t1idea.IdeaPopularAdapter;
import com.enterpaper.comepenny.tab.t1idea.IdeaPopularListItem;
import com.enterpaper.comepenny.util.SetFont;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kim on 2015-09-16.
 */
public class MyInfoActivity extends Activity {

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


        addItemsMyIdea();

        // Adapter 생성
        myadapters = new IdeaAdapter(getApplicationContext(), R.layout.row_idea, mydataList);

        // Adapter와 GirdView를 연결
        lv_mywrite.setAdapter(myadapters);
        myadapters.notifyDataSetChanged();


        lv_mywrite.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
                intent.setClass(getApplicationContext(), IdeaDetailActivity.class);
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

    // 리스트 아이템 추가
    private void addItemsMyIdea() {
        mydataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mydataList.add(new IdeaListItem(1234, "IdeaTitle", "jihoon1234", "1233", "4321"));

        }
    }

    // layout
    private void initLayout() {
        myinfo = (LinearLayout) myinfoview.findViewById(R.id.myinfo);
        img_user = (ImageView) myinfoview.findViewById(R.id.img_user);
        //dialog 호출
        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = createDialog();

            }
        });


        myInfo_divideline = (ImageView) myinfoview.findViewById(R.id.myInfo_divideline);
        tv_id = (TextView) myinfoview.findViewById(R.id.tv_id);
        tv_usermail = (TextView) myinfoview.findViewById(R.id.tv_usermail);


    }

    //dialog
    private AlertDialog createDialog() {
        final View innerView = getLayoutInflater().inflate(R.layout.myinfoimg_dialog, null);
        TableRow dialogtitle = (TableRow)innerView.findViewById(R.id.dialogtitle);
        TableRow row1 = (TableRow) innerView.findViewById(R.id.row1);
        TableRow row2 = (TableRow) innerView.findViewById(R.id.row2);
        TableRow row3 = (TableRow) innerView.findViewById(R.id.row3);


        row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "dialogtest1", Toast.LENGTH_SHORT)
                        .show();

//                doTakePhotoAction();
//                setDismiss(mDialog);
            }
        });
        row2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "dialogtest2", Toast.LENGTH_SHORT)
                        .show();
//                doTakePhotoAction();
//                setDismiss(mDialog);
            }
        });
        row3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "dialogtest3", Toast.LENGTH_SHORT)
                        .show();
//                doTakePhotoAction();
//                setDismiss(mDialog);
            }
        });

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setView(innerView);
        ab.setCancelable(true);
        Dialog mDialog = ab.create();
        //dialog크기조절
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(mDialog.getWindow().getAttributes());
        params.width = 800;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mDialog.show();
        Window window = mDialog.getWindow();
        window.setAttributes(params);

        return ab.create();
    }

    /**
     * 다이얼로그 종료
     */
    private void setDismiss(AlertDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    private void doTakePhotoAction()
    {
        Log.i(TAG, "doTakePhotoAction()");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Crop된 이미지를 저장할 파일의 경로를 생성
        mImageCaptureUri = createSaveCropFile();
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    /**
     * 앨범 호출 하기
     */
    private void doTakeAlbumAction()
    {
        Log.i(TAG, "doTakeAlbumAction()");
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     * Result Code
     */
    @Override


}
