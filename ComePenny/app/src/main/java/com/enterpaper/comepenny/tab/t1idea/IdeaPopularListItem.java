package com.enterpaper.comepenny.tab.t1idea;

import android.graphics.Bitmap;

/**
 * Created by kimmiri on 2015. 10. 8..
 */
public class IdeaPopularListItem {
    int booth_id;
    int recycle_image;

    public int getBooth_id() {
        return booth_id;
    }

    public void setBooth_id(int booth_id) {
        this.booth_id = booth_id;
    }

    public int getRecycle_image() {
        return recycle_image;
    }

    public void setRecycle_image(int recycle_image) {
        this.recycle_image = recycle_image;
    }

    public IdeaPopularListItem(int booth_id, int recycle_image) {

        this.booth_id = booth_id;
        this.recycle_image = recycle_image;
    }
}
