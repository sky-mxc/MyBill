package com.skymxc.mybill;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.skymxc.mybill.util.ImageViewPlus;

/**
 * 主窗体
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    
    private static final String TAG = "MainActivity";

    private SlidingPaneLayout slidingPane;
    private Toolbar toolbar;
    private NavigationView navigation;
    private ImageViewPlus headImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView() {
        slidingPane = (SlidingPaneLayout) findViewById(R.id.sliding);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigation = (NavigationView) findViewById(R.id.navigation);
        headImg = (ImageViewPlus) navigation.getHeaderView(0).findViewById(R.id.head_image);
        headImg.setOnClickListener(this);
        navigation.getHeaderView(0).findViewById(R.id.account).setOnClickListener(this);
        navigation.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        setSupportActionBar(toolbar);
        slidingPane.setSliderFadeColor(Color.parseColor("#A0000000"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_image:
                showChooseHeadImg(v);
                break;
            case R.id.choose_camera:
                Log.i(TAG, "onClick: choose_camera");
                // TODO: 2016/10/25  相机拍照 创建头像 
                break;
            case R.id.choose_photo:
                Log.i(TAG, "onClick: choose_photo");
                // TODO: 2016/10/25  相册选取图片 创建图片 
                break;
            case R.id.account:
                showExit(v);
                break;
        }
    }

    /**
     * 弹出 PopupMenu 显示退出菜单项
     * @param v 账号 TextView
     */
    private void showExit(View v) {
        Log.i(TAG, "showExit: account");
        PopupMenu exit = new PopupMenu(this,v);
        exit.inflate(R.menu.menu_navigation_exit);
        exit.setOnMenuItemClickListener(onMenuItemClickListener);
        exit.show();
    }

    /**
     * 弹出PopupWindow 选择创建头像的方式
     * @param v 头像ImageView
     */
    private void showChooseHeadImg(View v) {
        Log.i(TAG, "showChooseHeadImg: ");
        View view = getLayoutInflater().inflate(R.layout.show_choose_headimg,null);
        view.findViewById(R.id.choose_camera).setOnClickListener(this);
        view.findViewById(R.id.choose_photo).setOnClickListener(this);
        PopupWindow window = new PopupWindow(view);
        window.setHeight((int) getResources().getDimension(R.dimen.choose_head_img_height));
        window.setWidth((int) getResources().getDimension(R.dimen.choose_head_img_width));
        window.setFocusable(true);
        window.showAsDropDown(v,5,5);
    }

    //PopupMenu 菜单项点击事件
    private PopupMenu.OnMenuItemClickListener onMenuItemClickListener =new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Log.i(TAG, "onMenuItemClick: item="+item.getTitle());
            switch (item.getItemId()){
                case R.id.exit:
                    // TODO: 2016/10/25  退出当前账号
                    break;
            }
            return true;
        }
    };
    
    //导航菜单项点击事件
    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.i(TAG, "onNavigationItemSelected: item="+item.getTitle());
            switch (item.getItemId()){
                case R.id.bill:
                    //   去往账单 关闭窗口
                    slidingPane.closePane();
                    break;
                default:
                    Toast.makeText(MainActivity.this, "此功能暂未开放", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    };
}
