package com.enterpaper.comepenny.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.enterpaper.comepenny.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kimmiri on 2015. 11. 7..
 */

public class Capture extends Activity {
    private LinearLayout layout = null;
    // 저장파일의 경로입니다. Environment.getExternalStorageDirectory().getAbsolutePath()는 sd카드의 경로입니다.
   // FileOutputStream fos;


   File saveDirectory = Environment.getExternalStorageDirectory();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_detail_header);

        layout = (LinearLayout) findViewById(R.id.layout_capture);
        View view_ = layout.getRootView();
        view_.setDrawingCacheEnabled(true);
        // 화면 캡쳐
        capture(view_.getDrawingCache(), "Capture");
    }

    private void capture(Bitmap bitmap, String fileName) {
       File path = saveDirectory;


        File file = new File(path, fileName + ".jpg");
        if (!path.canWrite()) {
            Toast.makeText(this, path.getPath() + "에 " + fileName + "파일저장에 실패 했습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Toast.makeText(this, path.getPath() + "에 " + fileName + "파일이 저장 되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
