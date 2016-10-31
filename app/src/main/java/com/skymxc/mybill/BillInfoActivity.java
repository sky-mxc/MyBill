package com.skymxc.mybill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.skymxc.mybill.entity.Bill;
import com.skymxc.mybill.entity.BillType;
import com.skymxc.mybill.entity.PayType;
import com.skymxc.mybill.util.DBUtil;
import com.skymxc.mybill.util.DateUtil;

/**
 * Created by sky-mxc
 */
public class BillInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "BillInfoActivity";
    private Bill bill;
    private ImageView imgBillTypeIcon;
    private TextView tvBillTypeName;
    private TextView tvBillDate;
    private TextView tvBillPay;
    private TextView tvBillNum;
    private EditText etRemark;
    private ImageView imgCamera;
    private Toolbar toolBar;
    private static int from;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent()!=null){
            bill = getIntent().getParcelableExtra("bill");
        }
        setContentView(R.layout.activity_write_remark);
        initView();
        initData();
    }

    /**
     * 初始化View
      */
    private void initView(){
        imgBillTypeIcon = (ImageView) findViewById(R.id.bill_type_icon);
        tvBillTypeName = (TextView) findViewById(R.id.bill_type_name);
        tvBillDate = (TextView) findViewById(R.id.bill_date);
        tvBillPay = (TextView) findViewById(R.id.bill_pay_type);
        tvBillNum = (TextView) findViewById(R.id.bill_num);
        etRemark = (EditText) findViewById(R.id.remark);
        imgCamera = (ImageView) findViewById(R.id.bill_remark_camera);
        imgCamera.setOnClickListener(this);
        toolBar = (Toolbar) findViewById(R.id.tool_bal);
        setSupportActionBar(toolBar);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP|ActionBar.DISPLAY_SHOW_TITLE);
        toolBar.setNavigationOnClickListener(this);
        if (from == 10) {
            getSupportActionBar().setTitle("添加备注");
            toolBar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24px);
        }
    }

    private void initData() {
        BillType billType = DBUtil.getBillType(bill.getBillTypeId());
        imgBillTypeIcon.setImageResource(getResources().getIdentifier(billType.getIcon(),"mipmap",getPackageName()));
        tvBillTypeName.setText(billType.getName());
        String billDate = DateUtil.getDateString(bill.getTime());
        tvBillDate.setText(billDate);
        PayType payType = DBUtil.getPayType(bill.getPayTypeId());
        tvBillPay.setText(payType.getName());
        tvBillNum.setText(bill.getExpense()+"");
        etRemark.setText(bill.getRemarks());
    }


    /**
     * 跳转到 备注页面
     * @param activity 谁跳的
     * @param bill 账单对象
     * @param requestCode 请求码
     */
    public static void toWriteRemarkActivity(Activity activity, Bill bill,int requestCode,int fromActivity){
        Intent intent = new Intent(activity,BillInfoActivity.class);
        intent.putExtra("bill",bill);
        activity.startActivityForResult(intent,requestCode);
        from = fromActivity;
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: id="+v.getId());
        switch (v.getId()){
            case R.id.bill_remark_camera:
                break;
            default:
                String remark= etRemark.getText().toString();
                bill.setRemarks(remark);
                if (from == 10){
                  //回到 write
                    returnWrite();
                }
                finish();
                break;

        }
    }

    /**
     * 回到 write
     */
    private void returnWrite() {
        Intent intent = new Intent(this,WriteBillActivity.class);
        intent.putExtra("bill",bill);
        setResult(RESULT_OK,intent);
    }

    @Override
    public void onBackPressed() {
        String remark= etRemark.getText().toString();
        bill.setRemarks(remark);
        super.onBackPressed();
    }
}
