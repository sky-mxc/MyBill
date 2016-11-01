package com.skymxc.mybill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.skymxc.mybill.R;

/**
 * Created by sky-mxc
 */
public class BillFragment extends ListFragment {
    private static final String TAG = "BillFragment";
    private View view;
    //数据适配器
    private BaseAdapter adapter ;
    //点击项的监听
    private OnItemClickListener onItemClickLis;

    /**
     * 不建议使用 建议同过 getInstance ()获取
     * 如果使用此方法获取实例 必须另行设置adapter
     */
    public BillFragment(){
        setArguments(new Bundle());
    }

    

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置适配器
        if (adapter!=null){
            setListAdapter(adapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view ==null){
            view = inflater.inflate(R.layout.layout_fragment_bill,null);
        }
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (onItemClickLis !=null)
            onItemClickLis.onItemClickListener(position,id);
    }

    /**
     * 获取 Fragment 实例
     * @param adapter 数据适配器
     * @param itemClickLis item 点击事件
     * @return
     */
    public static  BillFragment getInstance(BaseAdapter adapter,OnItemClickListener itemClickLis){
        BillFragment fragment = new BillFragment();
        fragment.setAdapter(adapter);
        fragment.setOnItemClickLis(itemClickLis);
        return  fragment;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void setOnItemClickLis(OnItemClickListener onItemClickLis) {
        this.onItemClickLis = onItemClickLis;
    }

    /**
     * fragment中的 item 项被点击监听
     */
    public interface  OnItemClickListener{
        /**
         * item 项点击监听
         * @param position 被点击项的 position
         * @param id 被点击项 的 id
         */
        void onItemClickListener(int position,long id);
    }
}
