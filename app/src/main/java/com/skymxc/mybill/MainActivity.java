package com.skymxc.mybill;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.skymxc.mybill.adapter.BillListAdapter;
import com.skymxc.mybill.adapter.PagerAdapter;
import com.skymxc.mybill.entity.Bill;
import com.skymxc.mybill.fragment.BillFragment;
import com.skymxc.mybill.util.DBUtil;
import com.skymxc.mybill.util.DateUtil;
import com.skymxc.mybill.util.FileUtil;
import com.skymxc.mybill.util.ImageViewPlus;
import com.skymxc.mybill.util.PermissionUtil;
import com.skymxc.mybill.view.MySlidingPaneLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 主窗体
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    
    private static final String TAG = "MainActivity";

    private static final int CAMERA_PERMISSION =0;
    private static final int CAMERA = 2;
    private static final int PHOTO = 3;
    private static final int ZOOM = 4;
    private static final int REQUEST_WRITE_BILL_PEN = 10;

    private MySlidingPaneLayout slidingPane;
    private Toolbar toolbar;
    private NavigationView navigation;
    private ImageViewPlus headImg;
    private PopupWindow window;
    private ActionBar actionBar;
    private TextView navigationTxt;
    private ImageView navigationIco;
    private FloatingActionButton fatAdd;
    private FloatingActionButton fatAddPen;
    private FloatingActionButton fatAddCamera;
    private TextView yearTv;
    private TextView monthTv;
    private TabLayout tabLayout;
    private ViewPager pager;
    private List<Bill> bills;
    private BillListAdapter billListAdapter;
    private List<BillFragment> billFragments;
    private PagerAdapter pagerAdapert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        slidingPane.setEnabled(false);
    }


    private void initView() {
        slidingPane = (MySlidingPaneLayout) findViewById(R.id.sliding);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationTxt = (TextView) findViewById(R.id.navigation_text);
        navigationIco = (ImageView) findViewById(R.id.navigation_ico);
        fatAdd = (FloatingActionButton) findViewById(R.id.add);
        fatAddCamera = (FloatingActionButton) findViewById(R.id.add_camera);
        fatAddPen = (FloatingActionButton) findViewById(R.id.add_pen);
        fatAddCamera.setOnClickListener(this);
        fatAddPen.setOnClickListener(this);
        fatAdd.setOnClickListener(this);
        navigationIco.setOnClickListener(this);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        navigation = (NavigationView) findViewById(R.id.navigation);
        headImg = (ImageViewPlus) navigation.getHeaderView(0).findViewById(R.id.head_image);
        headImg.setOnClickListener(this);
        if (FileUtil.getHeadImage().exists()){
            headImg.setImageBitmap(BitmapFactory.decodeFile(FileUtil.getHeadImage().getPath()));
        }
        navigation.getHeaderView(0).findViewById(R.id.account).setOnClickListener(this);
        navigation.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        slidingPane.setSliderFadeColor(Color.parseColor("#A0000000"));
        slidingPane.setPanelSlideListener(slidLis);

        yearTv = (TextView) findViewById(R.id.date_year);
        yearTv.setText(DateUtil.getCurrentYear()+"年");
        monthTv = (TextView) findViewById(R.id.date_month);
        monthTv.setText(DateUtil.getCurrentMonth()+"月");
        pager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("消费明细"));
        tabLayout.addTab(tabLayout.newTab().setText("分类报表"));
        tabLayout.addTab(tabLayout.newTab().setText("账户统计"));
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        pager.addOnPageChangeListener(onPageChangeLis);
        initFragment();

    }

    /**
     * 初始化Fragment 数据
     */
    private void initFragment() {
        billFragments = new ArrayList<>();
        //账单列表
        bills = DBUtil.getBills(new Date());
        billListAdapter = new BillListAdapter(this, bills);
        BillFragment billListFragment = BillFragment.getInstance(billListAdapter,onBillItemClickLis);
        billFragments.add(billListFragment);
        billFragments.add( BillFragment.getInstance(billListAdapter,onBillItemClickLis));
        billFragments.add( BillFragment.getInstance(billListAdapter,onBillItemClickLis));
        pagerAdapert = new PagerAdapter(getSupportFragmentManager(),billFragments);
        pager.setAdapter(pagerAdapert);

    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        switch (v.getId()){
            case R.id.head_image:
                //选择头像
                showChooseHeadImg(v);
                break;
            case R.id.choose_camera:
                Log.i(TAG, "onClick: choose_camera");
                //   相机拍照 创建头像
                if(window.isShowing()){
                    window.dismiss();
                }
                callCamera();
                break;
            case R.id.choose_photo:
                Log.i(TAG, "onClick: choose_photo");
                //  相册选取图片 创建图片
                if(window.isShowing()){
                    window.dismiss();
                }
                callPhoto();
                break;
            case R.id.account:
                //弹出PopupMenu 退出当前账号显示
                showExit(v);
                break;
            case R.id.navigation_ico:
                //窗口打开与关闭
                if (slidingPane.isOpen()){
                    slidingPane.closePane();
                }else{
                    slidingPane.openPane();
                }
                break;
            case R.id.add:
                //弹出两个球球
                boolean isPop = (v.getTag(R.id.key_is_pop)==null)?false: (boolean) v.getTag(R.id.key_is_pop);
                Log.i(TAG, "onClick: isPop="+isPop);
                if(isPop){
                    dismissPop(v);
                }else{
                    popAdd(v);
                }

                break;
            case R.id.add_camera:
                //通过票据记账
                Log.i(TAG, "onClick: add_camera");
                Toast.makeText(this, "暂不支持此功能", Toast.LENGTH_SHORT).show();
                dismissPop(fatAdd);
                break;
            case R.id.add_pen:
                //通过笔记账
                Log.i(TAG, "onClick: add_pen");
                dismissPop(fatAdd);
                Intent intent = new Intent(this,WriteBillActivity.class);
                startActivityForResult(intent,REQUEST_WRITE_BILL_PEN);
                break;
            case R.id.date_ico:
            case R.id.date_month:
            case R.id.date_year:
                Log.i(TAG, "onClick: 弹出日期Dialog");
                popDateDialog();
                break;
        }
    }

    /**
     * 弹出日期对话框
     */
    private void popDateDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog = new DatePickerDialog(this,R.style.DateDialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.i(TAG, "onDateSet: year="+year+";month="+(month+1));
                yearTv.setText(year+"");
                monthTv.setText((month+1)+"月");
                bills.clear();
                bills.addAll(DBUtil.getBills(DateUtil.getDate(year+"/"+(month+1)+"/"+dayOfMonth)));
                billListAdapter.notifyDataSetChanged();
            }
        },year,month,day);

        dateDialog.show();
        ((ViewGroup)((ViewGroup)dateDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
    }

    /**
     * 关闭记账途径
     */
    private void dismissPop(View v) {
        v.setTag(R.id.key_is_pop,false);
        RotateAnimation rotate = new RotateAnimation(45,0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(500);
        rotate.setFillAfter(true);
        v.startAnimation(rotate);

        //两个球球的关闭动画
        fatAddPen.setTranslationX(0);
        fatAddPen.setTranslationY(0);
        fatAddCamera.setTranslationY(0);


    }

    /**
     * 弹出 记账途径两个
     */
    private void popAdd(View v) {
        v.setTag(R.id.key_is_pop,true);
        RotateAnimation rotate = new RotateAnimation(0,45,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(500);
        rotate.setFillAfter(true);
        v.startAnimation(rotate);

        //记账的两个途径 弹出动画
        PropertyValuesHolder translateXHolder = PropertyValuesHolder.ofFloat("translateX",-75f);
        PropertyValuesHolder translateYHolder = PropertyValuesHolder.ofFloat("translateY",-35f);
        PropertyValuesHolder translateYHolderC = PropertyValuesHolder.ofFloat("translateYC",-75f);
        PropertyValuesHolder rotateHolder = PropertyValuesHolder.ofFloat("rotate",-350,0);

        ValueAnimator moveShow = ValueAnimator.ofPropertyValuesHolder(translateXHolder,translateYHolder,rotateHolder,translateYHolderC);
        moveShow.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
               float translateX= (float) animation.getAnimatedValue("translateX");
               float translateY= (float) animation.getAnimatedValue("translateY");
               float translateYC= (float) animation.getAnimatedValue("translateYC");
                float rotate = (float) animation.getAnimatedValue("rotate");
               fatAddPen.setTranslationX(translateX);
               fatAddPen.setTranslationY(translateY);
                fatAddPen.setRotation(rotate);
                fatAddCamera.setTranslationY(translateYC);
                fatAddCamera.setRotation(rotate);

            }
        });
        moveShow.setDuration(1000);
        moveShow.setInterpolator(new BounceInterpolator());
        moveShow.start();

    }

    /**
     * 图库选取图片权限请求
     */
    private void callPhoto() {
        invokePhoto();
    }

    /**
     * 执行图库选取图片
     */
    private void invokePhoto() {
        Log.i(TAG, "invokePhoto: ");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");

        startActivityForResult(intent,PHOTO);

    }

    /**
     * 调用相机拍照
     */
    private void callCamera() {
        final String[] permissions = {"android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE"};
        //请求权限
        if (PermissionUtil.checkVersion()){
                if (!PermissionUtil.checkPermission(this,permissions)){
                    if (!shouldShowRequestPermissionRationale(permissions[0])||!shouldShowRequestPermissionRationale(permissions[1])){
                        new AlertDialog.Builder(this)
                                .setTitle("权限说明")
                                .setMessage("相机功能需要使用调用相机和内存卡读取的功能，这可能需要您的授权，请在稍后授权后使用")
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(permissions, CAMERA_PERMISSION);
                                        }

                                    }
                                })
                                .setNeutralButton("算了，不拍了",null)
                                .show();
                    }else {
                        //请求权限
                        requestPermissions(permissions, CAMERA_PERMISSION);
                    }
                }else{
                    invokeCamera();
                }
        }else{
            invokeCamera();
        }
    }

    /**
     * 执行拍照
     */
    private void invokeCamera() {
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(FileUtil.getHeadImage()));
            startActivityForResult(intent, CAMERA);
        }else{
            Toast.makeText(this, "检测不到SD卡，无法使用", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开系统图片裁剪功能
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        intent.putExtra("crop",true);
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX",200);
        intent.putExtra("outputY",200);
        intent.putExtra("scale",true); //黑边
        intent.putExtra("scaleUpIfNeeded",true); //黑边
        intent.putExtra("return-data",true);
        intent.putExtra("noFaceDetection",true);
        startActivityForResult(intent,ZOOM);

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
         window = new PopupWindow(view);
        window.setHeight((int) getResources().getDimension(R.dimen.choose_head_img_height));
        window.setWidth((int) getResources().getDimension(R.dimen.choose_head_img_width));
        window.setFocusable(true);
        window.showAsDropDown(v,5,5);
    }

    //导航菜单滑动事件
    private SlidingPaneLayout.PanelSlideListener slidLis =new SlidingPaneLayout.PanelSlideListener() {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
//            Log.i(TAG, "onPanelSlide: offset="+slideOffset*90);
            RotateAnimation rotateAnimation = new RotateAnimation(0,slideOffset*90, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            rotateAnimation.setDuration(100);
            rotateAnimation.setFillAfter(true);
            navigationIco.startAnimation(rotateAnimation);
        }

        @Override
        public void onPanelOpened(View panel) {

        }

        @Override
        public void onPanelClosed(View panel) {

        }
    };

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
            if (!slidingPane.isOpen()) return  false;
            Log.i(TAG, "onNavigationItemSelected: item="+item.getTitle());
            switch (item.getItemId()){
                case R.id.bill:
                    //   去往账单 关闭窗口
                    slidingPane.closePane();
                    navigationTxt.setText("账单");
                    break;
                default:
                    Toast.makeText(MainActivity.this, "此功能暂未开放", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    };

    //选项卡 改变监听
    private  TabLayout.OnTabSelectedListener onTabSelectedListener= new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.i(TAG, "onTabSelected: SelectedTabPosition="+tab.getPosition());
            pager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
    //viewpager 滑动监听
    private ViewPager.OnPageChangeListener onPageChangeLis= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.i(TAG, "onPageSelected: position=" + position);
            tabLayout.getTabAt(position).select();
            if (position == 0) {
                slidingPane.setSlidEnable(true);
            } else {
                slidingPane.setSlidEnable(false);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };

    //账单被点击事件
    private BillFragment.OnItemClickListener onBillItemClickLis = new BillFragment.OnItemClickListener() {
        @Override
        public void onItemClickListener(int position, long id) {
            Log.i(TAG, "onItemClickListener: id="+id);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_PERMISSION:
                if (PermissionChecker.PERMISSION_GRANTED==grantResults[0] && PermissionChecker.PERMISSION_GRANTED==grantResults[1]){
                    //授权成功
                    invokeCamera();
                }else{
                    //失败
                    Toast.makeText(this, "授权失败，无法使用相机功能", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null){
         switch (requestCode){
             case CAMERA:
                 //调用裁剪功能
                 startPhotoZoom(Uri.fromFile(FileUtil.getHeadImage()));
                 break;
             case ZOOM:
                 Bitmap bmp = data.getParcelableExtra("data");
                 headImg.setImageBitmap(bmp);
                 break;
             case PHOTO:
                 String projection=MediaStore.Images.Media.DATA;
              Cursor cursor= getContentResolver().query(data.getData(),new String[]{ projection},null,null,null);
                 if (cursor!=null && cursor.moveToFirst()){
                     String path = cursor.getString(cursor.getColumnIndex(projection));
                     Log.i(TAG, "onActivityResult: photoPath="+path);
                     startPhotoZoom(Uri.fromFile(FileUtil.saveHeadImg(path)));
                 }
                 cursor.close();
                 break;
         }
        }
    }


}
