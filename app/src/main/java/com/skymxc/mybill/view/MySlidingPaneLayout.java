package com.skymxc.mybill.view;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by sky-mxc
 */
public class MySlidingPaneLayout extends SlidingPaneLayout {
    private static final String TAG = "MySlidingPaneLayout";

    //滑动是否可用 m默认是可用的
    private boolean slidEnable =true ;

    public MySlidingPaneLayout(Context context) {
        super(context);
    }

    public MySlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
//


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "onInterceptTouchEvent: ");
        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                if (!slidEnable) {
                    Log.i(TAG, "onInterceptTouchEvent: 拦截");
                    return false;
                }
                break;
        }
        Log.i(TAG, "onInterceptTouchEvent: 不拦截");
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 滑动是否可用
     * @param slidEnable
     */
    public void setSlidEnable(boolean slidEnable) {
        this.slidEnable = slidEnable;
        Log.i(TAG, "setSlidEnable: 设置滑动 可用否"+slidEnable);
    }
}
