package com.skymxc.mybill.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sky-mxc
 * 账单
 */
@Table(name = "Bill")
public class Bill extends Model {

    //账单时间
    @Column(name = "time")
   private long time;
    //钱 数目
    @Column(name = "expense")
   private double expense;
   //账单类型（餐饮，旅游..）
    @Column(name = "billTypeId")
    private long billTypeId;
   //备注
    @Column(name = "remarks")
    private String remarks;
   //货币类型
    @Column(name = "currencyTypeId")
      private long currencyTypeId;
   //付费方式 （微信，支付宝。。）
    @Column(name = "payTypeId")
    private long payTypeId;

    public Bill(long time, double expense, long billTypeId, String remarks, long currencyTypeId, long payTypeId) {
        this.time = time;
        this.expense = expense;
        this.billTypeId = billTypeId;
        this.remarks = remarks;
        this.currencyTypeId = currencyTypeId;
        this.payTypeId = payTypeId;
    }

    public Bill() {
        super();

    }



    public void setTime(long time) {
        this.time = time;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public void setBillTypeId(long billTypeId) {
        this.billTypeId = billTypeId;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setCurrencyTypeId(long currencyTypeId) {
        this.currencyTypeId = currencyTypeId;
    }

    public void setPayTypeId(long payTypeId) {
        this.payTypeId = payTypeId;
    }

    public long getTime() {

        return time;
    }

    public double getExpense() {
        return expense;
    }

    public long getBillTypeId() {
        return billTypeId;
    }

    public String getRemarks() {
        return remarks;
    }

    public long getCurrencyTypeId() {
        return currencyTypeId;
    }

    public long getPayTypeId() {
        return payTypeId;
    }
}
