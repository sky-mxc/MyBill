package com.skymxc.mybill.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sky-mxc
 * 货币类型
 */
@Table(name = "CurrencyType")
public class CurrencyType extends Model{

    //类型名
    @Column(name = "name")
    private String name;

    //简写
    @Column(name = "currency")
    private String currency;

    //排序
    @Column(name = "sort")
    private String sort;

    public CurrencyType() {
    }

    public CurrencyType(String name, String currency, String sort) {
        this.name = name;
        this.currency = currency;
        this.sort = sort;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getName() {

        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public String getSort() {
        return sort;
    }
}
