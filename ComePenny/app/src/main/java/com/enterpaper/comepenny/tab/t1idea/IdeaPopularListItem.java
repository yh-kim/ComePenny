package com.enterpaper.comepenny.tab.t1idea;

import android.graphics.Bitmap;

/**
 * Created by kimmiri on 2015. 10. 8..
 */
public class IdeaPopularListItem {

    int recycle_image;
    String recycle_title;

    public int getRecycle_image() {
        return recycle_image;
    }

    public void setRecycle_image(int recycle_image) {
        this.recycle_image = recycle_image;
    }

    public String getRecycle_title() {
        return recycle_title;
    }

    public void setRecycle_title(String recycle_title) {
        this.recycle_title = recycle_title;
    }

    public IdeaPopularListItem(int recycle_image, String recycle_title) {

        this.recycle_image = recycle_image;
        this.recycle_title = recycle_title;
    }
}
