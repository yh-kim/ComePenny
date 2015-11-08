package com.enterpaper.comepenny.tab.t2booth;

/**
 * Created by Kim on 2015-07-19.
 */
public class BoothItem {
    String img_url;
    private String booth_name;
    private int booth_id;
    private int likeNum;
    private int ideaNum;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getBooth_name() {
        return booth_name;
    }

    public void setBooth_name(String booth_name) {
        this.booth_name = booth_name;
    }

    public int getBooth_id() {
        return booth_id;
    }

    public void setBooth_id(int booth_id) {
        this.booth_id = booth_id;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getIdeaNum() {
        return ideaNum;
    }

    public void setIdeaNum(int ideaNum) {
        this.ideaNum = ideaNum;
    }

    public BoothItem(String img_url, String booth_name, int booth_id, int likeNum, int ideaNum) {
        this.img_url = img_url;
        this.booth_name = booth_name;
        this.booth_id = booth_id;
        this.likeNum = likeNum;
        this.ideaNum = ideaNum;
    }
}
