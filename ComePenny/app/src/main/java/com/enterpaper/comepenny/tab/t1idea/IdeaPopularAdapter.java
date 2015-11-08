package com.enterpaper.comepenny.tab.t1idea;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.tab.t2booth.BoothDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by kimmiri on 2015. 10. 8..
 */
public class IdeaPopularAdapter extends RecyclerView.Adapter<IdeaPopularAdapter.ViewHolder> {
    Context context;
    List<IdeaPopularListItem> items;
    int item_layout;
    ImageLoader loader;

    public IdeaPopularAdapter(Context context, List<IdeaPopularListItem> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
        loader = ImageLoader.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_idea_popular, null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final IdeaPopularListItem item = items.get(position);
      //  Drawable drawable = context.getResources().getDrawable(item.getRecycle_image());
        //holder.recycle_image.setBackground(drawable);
       loader.displayImage("https://s3-ap-northeast-1.amazonaws.com/comepenny"+item.img_url+".png",holder.recycle_image);
       // loader.displayImage("https://s3-ap-northeast-1.amazonaws.com/comepenny/love.png",holder.recycle_image);

        holder.tv_name.setText(item.getBooth_name());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Activity activity = (Activity) context;
                    Intent intent = new Intent(context, BoothDetailActivity.class);

                     intent.putExtra("booth_id", item.getBooth_id());

                    context.startActivity(intent);
                    activity.overridePendingTransition(0, 0);

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView recycle_image;
        CardView cardview;
        TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            recycle_image = (ImageView) itemView.findViewById(R.id.recycle_image);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
        }






    }
}