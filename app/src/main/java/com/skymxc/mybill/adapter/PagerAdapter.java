package com.skymxc.mybill.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.skymxc.mybill.fragment.BillFragment;

import java.util.List;

/**
 * Created by sky-mxc
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "PagerAdapter";
    private List<BillFragment> fragments;
    public PagerAdapter(FragmentManager fm,List<BillFragment> billFragments) {
        super(fm);
        this.fragments=billFragments;
    }

    @Override
    public BillFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null?0:fragments.size();
    }
}
