package com.enterpaper.comepenny.tab.t1idea;


import android.graphics.Bitmap;

/**
 * Created by Kim on 2015-07-14.
 */
public class IdeaListItem {
    private String img;
    private String content;
    private String UserId;
    private int ViewCount;
    private int LiekeCount;

    public IdeaListItem(String img, String content, String userId, int viewCount, int liekeCount) {
        this.img = img;
        this.content = content;
        UserId = userId;
        ViewCount = viewCount;
        LiekeCount = liekeCount;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getViewCount() {
        return ViewCount;
    }

    public void setViewCount(int viewCount) {
        ViewCount = viewCount;
    }

    public int getLiekeCount() {
        return LiekeCount;
    }

    public void setLiekeCount(int liekeCount) {
        LiekeCount = liekeCount;
    }
}
