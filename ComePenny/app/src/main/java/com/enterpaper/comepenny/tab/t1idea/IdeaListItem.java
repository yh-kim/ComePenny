package com.enterpaper.comepenny.tab.t1idea;


import android.graphics.Bitmap;

/**
 * Created by Kim on 2015-07-14.
 */
public class IdeaListItem {
    private String img;
    private String title;
    private String UserId;
    private String ViewCount;
    private String LikeCount;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getViewCount() {
        return ViewCount;
    }

    public void setViewCount(String viewCount) {
        ViewCount = viewCount;
    }

    public String getLikeCount() {
        return LikeCount;
    }

    public void setLikeCount(String likeCount) {
        LikeCount = likeCount;
    }

    public IdeaListItem(String img, String title, String userId, String viewCount, String likeCount) {
        this.img = img;

        this.title = title;
        UserId = userId;
        ViewCount = viewCount;
        LikeCount = likeCount;
    }
}
