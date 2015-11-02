package com.enterpaper.comepenny.tab.t1idea;


/**
 * Created by Kim on 2015-07-14.
 */
public class IdeaListItem {
    private String img;
    private String content;
    private String UserId;
    private int ViewCount;
    private int LikeCount;
    private int idea_id;

    public int getIdea_id() {
        return idea_id;
    }

    public void setIdea_id(int idea_id) {
        this.idea_id = idea_id;
    }


    public IdeaListItem(int id, String img, String content, String userId, int viewCount, int likeCount) {
        idea_id = id;
        this.img = img;
        this.content = content;
        UserId = userId;
        ViewCount = viewCount;
        LikeCount = likeCount;
    }

    public IdeaListItem(String img, String content, String userId, int viewCount, int likeCount) {
        this.img = img;
        this.content = content;
        UserId = userId;
        ViewCount = viewCount;
        LikeCount = likeCount;
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

    public int getLikeCount() {
        return LikeCount;
    }

    public void setLikeCount(int liekeCount) {
        LikeCount = liekeCount;
    }
}
