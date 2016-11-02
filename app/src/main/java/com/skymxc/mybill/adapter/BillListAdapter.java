package com.skymxc.mybill.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skymxc.mybill.R;
import com.skymxc.mybill.entity.Bill;
import com.skymxc.mybill.entity.BillType;
import com.skymxc.mybill.util.DBUtil;
import com.skymxc.mybill.util.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by sky-mxc
 */
public class BillListAdapter extends BaseAdapter {
    private static final String TAG = "BillListAdapter";
    private Context mContext;
    private LayoutInflater lif;
    private List<Bill> bills;

    public BillListAdapter(Context mContext, List<Bill> bills) {
        this.mContext = mContext;
        this.bills = bills;
        lif = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return bills == null?0:bills.size();
    }

    @Override
    public Bill getItem(int position) {
        return bills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bills.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BillViewHolder holder ;
        if (convertView == null){
            convertView = lif.inflate(R.layout.layout_bill_list_item,null);
            holder = new BillViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (BillViewHolder) convertView.getTag();
        }
        Bill bill = bills.get(position);
        BillType billType = DBUtil.getBillType(bill.getBillTypeId());
        holder.imgBillTypeIcon.setImageResource(mContext.getResources().getIdentifier(billType.getIcon(),"mipmap",mContext.getPackageName()));
        holder.tvBillTypeName.setText(billType.getName());
        //根据 账单类型设置 是 收入还是支出
        if (billType.getType() ==0){
            holder.tvBillNum.setText("-"+bill.getExpense());
        }else{
            holder.tvBillNum.setText("+"+bill.getExpense());
        }
        //设置日期
        holder.tvDate.setText(DateUtil.getDateString(bill.getTime()));
        //查出 这天的消费
        double sum = DBUtil.getSumDay(new Date(bill.getTime()));
        holder.tvRightNum.setText(sum>0?"+"+sum:"-"+sum);
        if(position!=0){    //不是第一个
            Bill beforeBill = bills.get(position-1);
            if (!DateUtil.compareInDay(beforeBill.getTime(),bill.getTime())){
                //不在同一天
                holder.tvDate.setVisibility(View.VISIBLE);
                holder.tvRightNum.setVisibility(View.VISIBLE);
            }else{
                //在同一天
                holder.tvDate.setVisibility(View.GONE);
                holder.tvRightNum.setVisibility(View.GONE);
            }
        }

        return convertView;
    }



    class BillViewHolder {
        TextView tvDate;
        TextView tvRightNum;
        ImageView imgBillTypeIcon;
        TextView tvBillTypeName;
        TextView tvBillNum;
        public BillViewHolder(View v){
                tvDate = (TextView) v.findViewById(R.id.bill_date);
                tvRightNum = (TextView) v.findViewById(R.id.bill_right_num);
                tvBillTypeName = (TextView) v.findViewById(R.id.bill_type_name);
                tvBillNum = (TextView) v.findViewById(R.id.bill_num);
            imgBillTypeIcon = (ImageView) v.findViewById(R.id.bill_type_icon);
        }
    }
}
