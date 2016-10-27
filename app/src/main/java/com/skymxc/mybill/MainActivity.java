package com.skymxc.mybill;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.skymxc.mybill.util.FileUtil;
import com.skymxc.mybill.util.ImageViewPlus;
import com.skymxc.mybill.util.PermissionUtil;

import java.io.File;

/**
 * 主窗体
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    
    private static final String TAG = "MainActivity";

    private static final int CAMERA_PERMISSION =0;
    private static final int PHOTO_PERMISSION =1;
    private static final int CAMERA = 2;
    private static final int PHOTO = 3;
    private static final int ZOOM = 4;

    private SlidingPaneLayout slidingPane;
    private Toolbar toolbar;
    private NavigationView navigation;
    private ImageViewPlus headImg;
    private PopupWindow window;
    private File mfile;


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
                //   相机拍照 创建头像
                if(window.isShowing()){
                    window.dismiss();
                }
                callCamera();
                break;
            case R.id.choose_photo:
                Log.i(TAG, "onClick: choose_photo");
                // TODO: 2016/10/25  相册选取图片 创建图片
                if(window.isShowing()){
                    window.dismiss();
                }
                callPhoto();
                break;
            case R.id.account:
                showExit(v);
                break;
        }
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
