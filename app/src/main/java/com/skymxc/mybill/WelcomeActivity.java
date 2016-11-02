package com.skymxc.mybill;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.skymxc.mybill.util.DBUtil;
import com.skymxc.mybill.util.DateUtil;
import com.skymxc.mybill.util.SDFUtil;

/**
 * Created by sky-mxc
 * 欢迎界面
 */
public class WelcomeActivity extends AppCompatActivity {
    private LinearLayout root;
    private boolean ok = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        getSupportActionBar().hide();
        root = (LinearLayout) findViewById(R.id.root);


        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                toMain();
            }
        },1000*2);
        if (SDFUtil.isFirst(this)){
            DBUtil.initData(this);

        }
        toMain();

    }

    private void toMain(){
        if (ok){
            Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            ok=true;
        }
    }
}
