package com.enterpaper.comepenny.tab.t2booth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.util.SetFont;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Kim on 2015-07-14.
 */
public class BoothAdapter extends ArrayAdapter<BoothItem> {
    //LayoutInflater -> XML을 동적으로 만들 때 필요
    private LayoutInflater inflater = null;
    //Context -> Activity Class의 객체
    private Context contentContext = null;
    ImageLoader loader;


    public BoothAdapter(Context context, int resource, ArrayList<BoothItem> objects) {
        super(context, resource, objects);

        //context는 함수를 호출한 activiy
        //resource는 row_xxx.xml 의 정보
        this.contentContext = context;
        this.inflater = LayoutInflater.from(context);
        //Image Loader (Application에서 초기화를 다해줌)
        loader = ImageLoader.getInstance();
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .memoryCacheSize(2 * 1024 * 1024) // 2 Mb
//                .denyCacheImageMultipleSizesInMemory()
//                .discCacheFileNameGenerator(new Md5FileNameGenerator())
//                .imageDownloader(new ExtendedImageDownloader(getApplicationContext()))
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .enableLogging() // Not necessary in common
//                .build();
//
//        ImageLoader.getInstance().init(config);


    }


    //ArrayList에 저장되어있는 데이터를 fragment에 넣는 method
    //List 하나마다 getView가 한번 실행된다
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //position -> List번호
        ViewHolder holder;

        //XML 파일이 비어있는 상태라면
        if(convertView == null){
            //layout 설정
            convertView = inflater.inflate(R.layout.row_booth,null);

            //TextView 폰트 지정
            SetFont.setGlobalFont(contentContext, convertView);

            holder = new ViewHolder();

            //row에 있는 정보들을 holder로 가져옴
           // holder.test_layout = (RelativeLayout) convertView.findViewById(R.id.test_layout);
            holder.img = (ImageView) convertView.findViewById(R.id.img_main_company);
           // holder.name = (TextView) convertView.findViewById(R.id.tv_UserId);
            holder.likeNum = (TextView) convertView.findViewById(R.id.txt_boothmain_like);
            //   holder.ideaNum = (TextView) convertView.findViewById(R.id.txt_boothmain_idea);
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);

        }

        holder = (ViewHolder)convertView.getTag();



        BoothItem item = getItem(position);
        holder.tv_name.setText(item.getBooth_name());
        holder.likeNum.setText(item.getLikeNum() + "");
        //image 셋팅(불러옴)
<<<<<<< HEAD
        loader.displayImage("https://s3-ap-northeast-1.amazonaws.com/comepenny/"+item.img_url+".png",holder.img);
=======
        loader.displayImage("https://s3-ap-northeast-1.amazonaws.com/comepenny/booth/"+item.getImg_url()+".png",holder.img);

>>>>>>> 6643cbc44e536b098b42502749aa93037811aee3
        return convertView;

    }


    class ViewHolder {
        RelativeLayout test_layout;
        ImageView img;
        TextView likeNum,ideaNum,tv_name;

    }
}
