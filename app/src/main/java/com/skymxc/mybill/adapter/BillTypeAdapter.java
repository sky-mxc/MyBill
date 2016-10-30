package com.skymxc.mybill.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skymxc.mybill.R;
import com.skymxc.mybill.entity.BillType;

import java.util.List;

/**
 * Created by sky-mxc
 */
public class BillTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BillTypeAdapter";
    private LayoutInflater lif;
    private List<BillType> billTypes;
    private View.OnClickListener onClickListener;
    private Context mContext;


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public BillTypeAdapter(Context context, List<BillType> billTypes) {
        this.billTypes = billTypes;
        this.mContext = context;
        lif = LayoutInflater.from(context);
    }

    @Override
    public BillTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BillTypeViewHolder(lif.inflate(R.layout.layout_bill_type_item,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BillType billType = billTypes.get(position);
       if (holder instanceof BillTypeViewHolder){
           BillTypeViewHolder mHolder = (BillTypeViewHolder) holder;
           mHolder.icon.setImageResource(mContext.getResources().getIdentifier(billType.getIcon(),"mipmap",mContext.getPackageName()));
           mHolder.name.setText(billType.getName());
       }
    }

    @Override
    public int getItemCount() {
        return billTypes==null?0:billTypes.size();
    }

    public void changeData(List<BillType> bills){
        this.billTypes=null;
        this.billTypes = bills;
        notifyDataSetChanged();
    }

    public BillType getItem(int position){
        return  billTypes.get(position);
    }

    class BillTypeViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView name;

        public BillTypeViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.bill_type_icon);
            name = (TextView) itemView.findViewById(R.id.bill_type_name);
            this.itemView.setOnClickListener(onClickListener);
        }
    }
}
