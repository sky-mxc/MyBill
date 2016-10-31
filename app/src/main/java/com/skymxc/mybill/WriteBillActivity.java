package com.skymxc.mybill;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.Toast;

import com.skymxc.mybill.adapter.BillTypeAdapter;
import com.skymxc.mybill.entity.Bill;
import com.skymxc.mybill.entity.BillType;
import com.skymxc.mybill.entity.CurrencyType;
import com.skymxc.mybill.entity.PayType;
import com.skymxc.mybill.util.DBUtil;
import com.skymxc.mybill.util.DateUtil;

import java.util.Date;
import java.util.List;

public class WriteBillActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "WriteBillActivity" ;
    private static final int REQUET_REMARK =10;
    private Toolbar toolbar;
    private ListView payTypeLv;
    private List<PayType> payTypes;
    private Button btPayType;
    private Button btBillDate;
    private TextView tvBillType;
    private RecyclerView rvBillType;
    private List<BillType> billTypes;
    private List<CurrencyType> currencyTypes;
    private BillTypeAdapter billTypeAdapter;
    private ImageView imgChooseBillType;
    private TextView tvBillNum;
    private TextView tvCurrency;
    private boolean point =true;
    private boolean numEnable =true;
    private CurrencyAdapter currencyAdapter;
    private Bill bill;

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
        //收入1 || 支出0
        tvBillType = (TextView) findViewById(R.id.change_bill_type);
        tvBillType.setTag(0);
        rvBillType = (RecyclerView) findViewById(R.id.recycler_bill_type);

        int rvHeight = rvBillType.getMeasuredHeight();
        int itemHeight = getResources().getDimensionPixelOffset(R.dimen.bill_type_icon_height);
        int spanCount = rvHeight % itemHeight ==0 ? rvHeight/itemHeight:rvHeight/itemHeight+1;
        Log.i(TAG, "initView: rvHeight="+rvHeight+";itemHeight="+itemHeight+";spanCount="+spanCount);
        GridLayoutManager glm = new GridLayoutManager(this,4,GridLayoutManager.HORIZONTAL,false);
        rvBillType.setLayoutManager(glm);
        rvBillType.setItemAnimator(new DefaultItemAnimator());
        billTypes = DBUtil.getBillTypes((int)tvBillType.getTag());
        billTypeAdapter = new BillTypeAdapter(this, billTypes);
        rvBillType.setAdapter(billTypeAdapter);
        billTypeAdapter.setOnClickListener(onItemClickListener);
        imgChooseBillType = (ImageView) findViewById(R.id.choose_bill_type);
        imgChooseBillType.setTag(billTypes.get(0));

        //数目
        tvBillNum = (TextView) findViewById(R.id.bill_num);

        //货币类型
        tvCurrency = (TextView) findViewById(R.id.currency_type);
        tvCurrency.setOnClickListener(this);
        currencyTypes = DBUtil.getCureencyTypes();
        tvCurrency.setTag(currencyTypes.get(0));
        tvCurrency.setText(currencyTypes.get(0).getCurrency());
    }


    /**
     * 切换化账单类型
     */
    private void changeBillTypeData(){
       billTypes = DBUtil.getBillTypes((int)tvBillType.getTag());
        Log.i(TAG, "changeBillTypeData: size="+billTypes.size());
        billTypeAdapter.changeData(billTypes);
        chooseBillType(billTypes.get(0));
    }

    /**
     * 选中 账单类型
     */
    public void chooseBillType(BillType type){
        imgChooseBillType.setTag(type);
        imgChooseBillType.setImageResource(getResources().getIdentifier(type.getIcon(),"mipmap",getPackageName()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_type:
                //选择收支方式
                showChoosePayType();
                break;
            case R.id.bill_date:
                //选择记账日期
                chooseWriteDate();
                break;
            case R.id.change_bill_type:
                //切换支出或者收入
                changeBillType(v);
                break;
            case R.id.close_write_bill:
                setResult(RESULT_CANCELED);
                break;
            case R.id.currency_type:
                Log.i(TAG, "onClick: 切换货币类型");
                changeCurrency();
                break;
            case R.id.bill_save:
                Log.i(TAG, "onClick: 保存");
                saveBill();
                break;

        }

    }

    /**
     * 保存 bill
     */
    private void saveBill() {
        getBill();
        if (bill.getExpense() == 0){
            Toast.makeText(this, "金额不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        bill.save();
        Toast.makeText(this, "保存成功，返回咯", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 切换货币类型
     */
    private void changeCurrency() {
        currencyAdapter = new CurrencyAdapter();
        ListView lv = new ListView(this);
        lv.setAdapter(currencyAdapter);
        lv.setDividerHeight(0);
        new AlertDialog.Builder(this,R.style.alertStyle)
                .setTitle("货币类型")
                .setView(lv)
                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onClick: cancel");
                        tvCurrency.setTag(currencyTypes.get(0));
                        tvCurrency.setText(currencyTypes.get(0).getCurrency());
                    }
                })
                .setNegativeButton("OK", null)

                .show();

    }

    /**
     * 弹出 对话框选择 记账 日期
     */
    private void chooseWriteDate() {
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
    }

    /**
     * 切换 收入 || 支出
     * @param v
     */
    private void changeBillType(View v) {
        PopupMenu popChangeBillType = new PopupMenu(this,v);
        popChangeBillType.inflate(R.menu.menu_change_bill_type);
        popChangeBillType.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i(TAG, "onMenuItemClick: 支出or收入 ="+item.getTitle());
                tvBillType.setText(item.getTitle());
                switch (item.getItemId()){
                    case R.id.bill_income:
                        tvBillType.setTag(1);
                        break;
                    case R.id.bill_pay:
                        tvBillType.setTag(0);
                        break;
                }
                changeBillTypeData();
                return true;
            }
        });
        popChangeBillType.show();
    }

    /**d
     * 弹出选择支付方式
     */
    private void showChoosePayType() {
        payTypeLv = new ListView(this);

        payTypeLv.setAdapter(new PayTypeAdapter());
        new AlertDialog.Builder(this,R.style.alertStyle)
                .setTitle("支付方式")
                .setView(payTypeLv)
                .setNegativeButton("确定",null).show();
    }

    /**
     * 计算机单击事件
     * @param v
     */
    public void onCalcClick(View v){

        switch (v.getId()){
            case R.id.calc_clear:
                point=true;     //小数点后可写入
                numEnable = true; //num 可以写入
                tvBillNum.setText("0.00");
                break;
            case R.id.calc_point:
                point=false;
                break;
            case R.id.calc_del:
                String currentNum = tvBillNum.getText().toString();
                point=true;     //小数点后可写入
                numEnable = true; //num 可以写入
               if (currentNum.equals("0.00")) return;
                //拿到小数点之后的数字
                String afterPoint = currentNum.substring(currentNum.indexOf(".")+1);
                //拿到小数点之前的数字
                String beforePoint = currentNum.substring(0,currentNum.indexOf("."));
                double afterPointNum = getIntFormart(afterPoint);
                Log.i(TAG, "onCalcClick: afterPointNum="+afterPointNum);
                if (afterPointNum>0){   //删除 小数点之后的字符
                    if (afterPointNum%10==0){//删除 小数点 后的第一位
                        tvBillNum.setText(beforePoint+".00");
                    }else{//删除小数点后的 第二位
                        tvBillNum.setText(beforePoint+"."+(int)afterPointNum/10+"0");
                    }
                }else{ //删除小数点之前的字符
                    double beforePointNum = getIntFormart(beforePoint);
                    if (beforePointNum>=10) {
                        String newNum = beforePoint.substring(0, beforePoint.length() - 1);
                        tvBillNum.setText(newNum + ".00");
                    }else{
                        tvBillNum.setText("0.00");
                    }
                }
                break;
            default:
                if (!numEnable) return;
                Log.i(TAG, "onCalcClick: text=" + ((TextView) v).getText());
                String addNumStr = ((TextView) v).getText().toString();
                String oldNumStr =tvBillNum.getText().toString();
                String afterNumStr =oldNumStr.substring(oldNumStr.indexOf(".")+1);
                double afterNum = getIntFormart(afterNumStr);
                Log.i(TAG, "onCalcClick: afterNum="+afterNum);

                    if (!oldNumStr.equals("0.00")) {
                            //拿到 . 之前的字符
                            String oldNum = oldNumStr.substring(0, oldNumStr.indexOf("."));
                        if (point) {
                            //写入小数点之前
                            if (oldNum.length()<12) {
                                tvBillNum.setText(oldNum + addNumStr + ".00");
                            }
                        }else{  //写入小数点之后
                            //小数点之后的字符
                            if (afterNum>=10){
                                //最后一个字符可用 ，写入小数点后的第二位
                                tvBillNum.setText(oldNumStr.substring(0,tvBillNum.getText().length()-1)+addNumStr);
                                numEnable=false;//不可写入了
                            }else{
                                //最后一个字符不可用 写入小数点后的第一位
                                tvBillNum.setText(oldNum+"."+addNumStr+"0");
                            }
                        }
                    } else {
                        tvBillNum.setText(addNumStr + ".00");
                    }

                break;
        }
        if (tvBillNum.length()>=9){
            tvBillNum.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        }else{
            tvBillNum.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        }

    }

    /**
     * 字符串转为 int
     * @param s
     * @return
     */
    private double getIntFormart(String s){
        return  Double.parseDouble(s);
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
                //  前往备注activity
                Log.i(TAG, "onOptionsItemSelected: ");
               BillInfoActivity.toWriteRemarkActivity(this,getBill(),REQUET_REMARK,10);
                break;
        }
        return true;
    }

    /**
     * 获取当前bill 对象
     * @return
     */
    private Bill getBill() {
        if (bill==null){
          bill = new Bill();
        }
        //时间
        bill.setTime(((Date)  btBillDate.getTag()).getTime());
        //支付方式
        bill.setPayTypeId(((PayType)btPayType.getTag()).getId());
        //数目
        double num = getIntFormart(tvBillNum.getText().toString());
        bill.setExpense(num);
        //货币
        CurrencyType currencyType = (CurrencyType) tvCurrency.getTag();
        bill.setCurrencyTypeId(currencyType.getId());
        //账单类型
        BillType billType = (BillType) imgChooseBillType.getTag();
        bill.setBillTypeId(billType.getId());

        return  bill;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        PayType pay = (PayType) buttonView.getTag();
        btPayType.setText(pay.getName());
        btPayType.setTag(pay);
        ((PayTypeAdapter)payTypeLv.getAdapter()).notifyDataSetChanged();
    }

    /**
     * 货币类型选择
     */
    private CompoundButton.OnCheckedChangeListener onCheckedChangeLis = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            tvCurrency.setTag(buttonView.getTag());
            tvCurrency.setText(((CurrencyType)buttonView.getTag()).getCurrency());
            currencyAdapter.notifyDataSetChanged();
        }
    };
    /**
     * 账单类型选择
     */
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvBillType.getChildAdapterPosition(v);
            Log.i(TAG, "onItemClickListener: position="+position+";name="+billTypes.get(position).getName());
            chooseBillType(billTypes.get(position));
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK  && data != null){
            switch (requestCode){
                case REQUET_REMARK:
                    bill = data.getParcelableExtra("bill");
                    break;
            }

        }
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

    class CurrencyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return currencyTypes.size();
        }

        @Override
        public CurrencyType getItem(int position) {
            return currencyTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return currencyTypes.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CurrencyViewHolder holder ;
            if (convertView ==null){
                convertView = getLayoutInflater().inflate(R.layout.layout_currency_item,null);
                holder = new CurrencyViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (CurrencyViewHolder) convertView.getTag();
            }
            CurrencyType currency = currencyTypes.get(position);
            holder.tvName.setText(currency.getName());
            holder.tvCurrency.setText(currency.getCurrency());
            holder.check.setOnCheckedChangeListener(null);
            CurrencyType currentC =(CurrencyType)tvCurrency.getTag();
            if (currency.getId()==currentC.getId()){
                holder.check.setChecked(true);
            }else{
                holder.check.setChecked(false);
            }
            holder.check.setOnCheckedChangeListener(onCheckedChangeLis);
            holder.check.setTag(currency);
            return convertView;
        }
    }

    class CurrencyViewHolder {
        TextView tvName;
        TextView tvCurrency;
        CheckBox check;
        public CurrencyViewHolder(View v){
            tvName = (TextView) v.findViewById(R.id.currency_name);
            tvCurrency = (TextView) v.findViewById(R.id.currency_currency);
            check = (CheckBox)  v.findViewById(R.id.currency_check);
        }
    }

}
