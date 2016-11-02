package com.skymxc.mybill.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skymxc.mybill.R;
import com.skymxc.mybill.view.Item;

import java.util.List;

/**
 * Created by sky-mxc
 */
public class ReportAdpater extends BaseAdapter {
    private static final String TAG = "ReportAdpater";
    private Context mContext;
    private LayoutInflater lif;
    private List<Item> items;

    public ReportAdpater(Context mContext, List<Item> items) {
        this.mContext = mContext;
        this.items = items;
        lif = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return items == null?0:items.size();
    }

    @Override
    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReportViewHolder holder ;
        if (convertView == null){
            convertView = lif.inflate(R.layout.layout_report_item,null);
            holder = new ReportViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ReportViewHolder) convertView.getTag();
        }
        Item item = items.get(position);
        holder.imgIcon.setImageResource(mContext.getResources().getIdentifier(item.getIcon(),"mipmap",mContext.getPackageName()));
        holder.tvName.setText(item.getName());
        holder.tvScale.setText(item.getScale()+"%");
        holder.tvTitle.setText(item.getTitle());
        holder.pb.setProgress(item.getProgress());

        LayerDrawable drawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.shape_progress_bg);
        ClipDrawable cd = (ClipDrawable) mContext.getResources().getDrawable(R.drawable.progress);
        cd.setColorFilter(item.getColor(), PorterDuff.Mode.SRC_OVER);
        drawable.setDrawableByLayerId(android.R.id.progress,cd);
        holder.pb.setProgressDrawable(drawable);
        if (item.getType()==1){
            holder.tvNum.setText("+"+item.getNum());
        }else{
            holder.tvNum.setText("-"+item.getNum());
        }
        if (position!=0){
            if (item.getType()!=items.get(position-1).getType()){
                holder.tvTitle.setVisibility(View.VISIBLE);
            }else{
                holder.tvTitle.setVisibility(View.GONE);
            }
        }else{
            holder.tvTitle.setVisibility(View.VISIBLE);
        }

        if (item.getViewType()!=1){
            holder.tvScale.setVisibility(View.GONE);
            holder.pb.setVisibility(View.GONE);
        }else{
            holder.tvScale.setVisibility(View.VISIBLE);
            holder.pb.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ReportViewHolder {
        TextView tvTitle;
        ImageView imgIcon;
        TextView tvName;
        TextView tvScale;
        ProgressBar pb;
        TextView tvNum;
        public ReportViewHolder(View v){
            tvName = (TextView) v.findViewById(R.id.name);
            tvNum = (TextView) v.findViewById(R.id.num );
            tvScale = (TextView) v.findViewById(R.id.scale);
            tvTitle = (TextView) v.findViewById(R.id.title);
            pb = (ProgressBar) v.findViewById(R.id.progress);
            imgIcon = (ImageView) v.findViewById(R.id.icon);
        }
    }
}
