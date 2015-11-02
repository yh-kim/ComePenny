package com.enterpaper.comepenny.tab.t1idea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.util.SetFont;

import java.util.ArrayList;

/**
 * Created by Kim on 2015-07-14.
 */

public class CommentAdapter extends ArrayAdapter<CommentItem> {
    //LayoutInflater -> XML을 동적으로 만들 때 필요
    private LayoutInflater inflater = null;
    //Context -> Activity Class의 객체
    private Context context = null;

    public CommentAdapter(Context context, int resource, ArrayList<CommentItem> objects) {
        super(context, resource, objects);

        //context는 함수를 호출한 activiy
        //resource는 row_xxx.xml 의 정보
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


    //ArrayList에 저장되어있는 데이터를 fragment에 넣는 method
    //List 하나마다 getView가 한번 실행된다
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //position -> List번호
        ViewHolder holder;

        //XML 파일이 비어있는 상태라면
        if (convertView == null) {
            //layout 설정
            convertView = inflater.inflate(R.layout.row_comment, null);
            //TextView 폰트 지정
            SetFont.setGlobalFont(context, convertView);

            holder = new ViewHolder();

            //row에 있는 정보들을 holder로 가져옴

            convertView.setTag(holder);
        }


        holder = (ViewHolder) convertView.getTag();

        CommentItem item = getItem(position);


        return convertView;
    }

    class ViewHolder {
    }

}
