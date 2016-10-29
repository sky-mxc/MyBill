package com.skymxc.mybill.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sky-mxc
 * 支付方式(微信，现金。。。)
 */
@Table(name = "PayType")
public class PayType extends Model {

    //支付类型
    @Column(name = "name")
    private String name;

    //图标
    @Column(name = "icon")
    private String icon;

    //收支类型 (支出0，收入1)
    @Column(name = "type")
    private int type;

    public PayType() {
    }

    public PayType(String name, String icon, int type) {
        this.name = name;
        this.icon = icon;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public int getType() {
        return type;
    }
}
