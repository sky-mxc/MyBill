package com.skymxc.mybill;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.skymxc.mybill.entity.PayType;
import com.skymxc.mybill.util.DBUtil;
import com.skymxc.mybill.util.DateUtil;

import java.util.Date;
import java.util.List;

public class WriteBillActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "WriteBillActivity" ;
    private Toolbar toolbar;
    private ListView payTypeLv;
    private List<PayType> payTypes;
    private Button btPayType;
    private Button btBillDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_bill);
        initView();
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btPayType = (Button) findViewById(R.id.pay_type);
        payTypes = DBUtil.getPayTypes();
        //默认 支付方式
        btPayType.setText(payTypes.get(0).getName());
        btPayType.setTag(payTypes.get(0));
        //默认当前日期
        btBillDate = (Button) findViewById(R.id.bill_date);
        String currentDate = DateUtil.getStringFormart(new Date());
        btBillDate.setText(currentDate);
        btBillDate.setTag(new Date());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_type:
                showChoosePayType();
                break;
            case R.id.bill_date:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String chooseDate = year+"/"+(month+1)+"/"+dayOfMonth;
                        Log.i(TAG, "onDateSet:chooseDate="+chooseDate);
                       Date date = DateUtil.getDateFormart(chooseDate);
                        btBillDate.setTag(date);
                        btBillDate.setText(DateUtil.getStringFormart(date));

                    }
                }, DateUtil.getCurrentYear(),DateUtil.getCurrentMonth(),DateUtil.getCurrentDay())
                        .show();
                break;
        }

    }

    /**
     * 弹出选择支付方式
     */
    private void showChoosePayType() {
        payTypeLv = new ListView(this);

        payTypeLv.setAdapter(new PayTypeAdapter());
        new AlertDialog.Builder(this)
                .setTitle("支付方式")
                .setView(payTypeLv)
                .setNegativeButton("确定",null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_bill_to_remark,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.to_remark:
                // TODO: 2016/10/30 前往备注activity 
                break;
        }
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        PayType pay = (PayType) buttonView.getTag();
        btPayType.setText(pay.getName());
        btPayType.setTag(pay);
        ((PayTypeAdapter)payTypeLv.getAdapter()).notifyDataSetChanged();
    }

    class  PayTypeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return payTypes==null?0:payTypes.size();
        }

        @Override
        public PayType getItem(int position) {
            return payTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return payTypes.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PayTypeViewHolder holder ;
            if (convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.layout_choose_pay_type,null);
                holder = new PayTypeViewHolder(convertView);
                convertView.setTag(holder);
            }else {
                holder= (PayTypeViewHolder) convertView.getTag();
            }
            PayType pay = payTypes.get(position);
            holder.payName.setText(pay.getName());
            holder.payIcon.setImageResource(getResources().getIdentifier(pay.getIcon(),"mipmap",getPackageName()));
            holder.payCheck.setOnCheckedChangeListener(null);
            if (pay.getId().equals(((PayType)(btPayType.getTag())).getId())){
                holder.payCheck.setChecked(true);
            }else{
                holder.payCheck.setChecked(false);
            }
            holder.payCheck.setTag(pay);
            holder.payCheck.setOnCheckedChangeListener(WriteBillActivity.this);
            return convertView;
        }
    }

    class  PayTypeViewHolder {
        ImageView payIcon;
        TextView payName;
        CheckBox payCheck;
        public PayTypeViewHolder(View v){
            payIcon = (ImageView) v.findViewById(R.id.pay_icon);
            payName = (TextView) v.findViewById(R.id.pay_name);
            payCheck = (CheckBox) v.findViewById(R.id.pay_check);
        }
    }
}
