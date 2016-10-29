package com.skymxc.mybill.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sky-mxc
 * 账单类型（餐饮，旅游...）
 */
@Table(name = "BillType")
public class BillType  extends Model{

    //名字
    @Column(name = "name")
    private String name;

    //图标名字
    @Column(name = "icon")
    private String icon;

    //进度条颜色
    @Column(name = "color")
    private String color;

    //排序
    @Column(name = "sort")
    private int sort;

    // 支出类型(收入1，支出0)
    @Column(name = "type")
    private int type;

    public BillType() {
    }

    public BillType(String name, String icon, String color, int sort, int type) {
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.sort = sort;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getColor() {
        return color;
    }

    public int getSort() {
        return sort;
    }

    public int getType() {
        return type;
    }
}
