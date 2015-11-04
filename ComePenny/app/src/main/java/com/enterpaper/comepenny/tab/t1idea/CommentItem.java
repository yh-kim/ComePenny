package com.enterpaper.comepenny.tab.t1idea;

/**
 * Created by Kim on 2015-11-02.
 */
public class CommentItem {

        private String img;
        private String comment_content;
        private String Email;
        private String comment_time;
        private int idea_id;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public int getIdea_id() {
        return idea_id;
    }

    public void setIdea_id(int idea_id) {
        this.idea_id = idea_id;
    }

    public CommentItem(String img, String comment_content, String email, String comment_time, int idea_id) {

        this.img = img;
        this.comment_content = comment_content;
        Email = email;
        this.comment_time = comment_time;
        this.idea_id = idea_id;
    }
}
