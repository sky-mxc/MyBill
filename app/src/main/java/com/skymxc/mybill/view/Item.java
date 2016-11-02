package com.skymxc.mybill.view;

/**
 * Created by sky-mxc
 */
public class Item {
    private static final String TAG = "Item";

    public void setTitle(String title) {
        this.title = title;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public static String getTAG() {

        return TAG;
    }

    public String getTitle() {
        return title;
    }


    public String getName() {
        return name;
    }

    public String getScale() {
        return scale;
    }

    public int getProgress() {
        return progress;
    }

    public double getNum() {
        return num;
    }
    private long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {

        return id;
    }

    private  String title;
    private  String icon;
    private String name;
    private String scale;
    private int progress;
    private double num;
    private int type;
    private int color ;
    private int viewType;

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {

        return color;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {

        return icon;
    }

    public int getType() {
        return type;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {

        return viewType;
    }
}
