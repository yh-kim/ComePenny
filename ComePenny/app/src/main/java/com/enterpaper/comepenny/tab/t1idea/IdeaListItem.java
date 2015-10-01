package com.enterpaper.comepenny.tab.t1idea;


import android.graphics.Bitmap;

/**
 * Created by Kim on 2015-07-14.
 */
public class IdeaListItem {
    private Bitmap img;
    private String title;
    private String UserId;
    private String ViewCount;
    private String LikeCount;

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return UserId;
    }

    public String getViewCount() {
        return ViewCount;
    }
    public String getLikeCount() {
        return LikeCount;
    }

    public IdeaListItem(int img, String title, String userId, String viewCount, String likeCount) {

        //this.img = img;
        this.title = title;
        this.UserId = userId;
        this.ViewCount = viewCount;
        this.LikeCount = likeCount;
    }
}
