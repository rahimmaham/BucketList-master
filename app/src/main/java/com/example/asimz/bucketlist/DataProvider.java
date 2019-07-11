package com.example.asimz.bucketlist;

public class DataProvider {

    private String  title;
    private String desc;
    private String points;

    public DataProvider(String title, String desc, String points) {
        this.title = title;
        this.desc = desc;
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPoints() {
        return points;
    }


    public void setPoints(String points) {
        this.points = points;

    }
}
