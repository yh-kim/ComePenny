package com.enterpaper.comepenny.tab.t1idea;


/**
 * Created by Kim on 2015-07-14.
 */
public class IdeaListItem {
    private String img;
    private String content;
    private String Email;
    private int ViewCount;
    private int LikeCount;
    private int idea_id;

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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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

    public void setLikeCount(int likeCount) {
        LikeCount = likeCount;
    }

    public int getIdea_id() {
        return idea_id;
    }

    public void setIdea_id(int idea_id) {
        this.idea_id = idea_id;
    }

    public IdeaListItem(String img, String content, String email, int viewCount, int likeCount, int idea_id) {

        this.img = img;
        this.content = content;
        Email = email;
        ViewCount = viewCount;
        LikeCount = likeCount;
        this.idea_id = idea_id;
    }
}
