package com.enterpaper.comepenny.tab.t1idea;


/**
 * Created by Kim on 2015-07-14.
 */
public class MenuListItem {
    private int img;
    private String title;


    public MenuListItem(int img, String title){
        this.img = img;
        this.title = title;
    }
    public MenuListItem(String title){
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
