package com.enterpaper.comepenny.tab.t1idea;


/**
 * Created by Kim on 2015-07-14.
 */
public class IdeaListItem {
    private String img;
    private String content;
    private String UserId;
    private int ViewCount;
    private int LiekeCount;
    private int idea_id;

    public int getIdea_id() {
        return idea_id;
    }

    public void setIdea_id(int idea_id) {
        this.idea_id = idea_id;
    }


    public IdeaListItem(int id, String img, String content, String userId, int viewCount, int liekeCount) {
        idea_id = id;
        this.img = img;
        this.content = content;
        UserId = userId;
        ViewCount = viewCount;
        LiekeCount = liekeCount;
    }

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
