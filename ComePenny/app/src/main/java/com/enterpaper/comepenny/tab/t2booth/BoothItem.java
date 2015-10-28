package com.enterpaper.comepenny.tab.t2booth;

/**
 * Created by Kim on 2015-07-19.
 */
public class BoothItem {
    private String boothImg;
    private String name;
    private int booth_id;
    private int likeNum;
    private int ideaNum;

    public BoothItem(String boothImg, String name, int booth_id, int likeNum, int ideaNum) {
        this.boothImg = boothImg;
        this.name = name;
        this.booth_id = booth_id;
        this.likeNum = likeNum;
        this.ideaNum = ideaNum;

    }

    public String getBoothImg() {
        return boothImg;
    }

    public void setBoothImg(String boothImg) {
        this.boothImg = boothImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
