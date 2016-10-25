package com.skymxc.mybill.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.skymxc.mybill.R;

/**
 * Created by sky-mxc
 * 圆形图片
 */
public class ImageViewPlus extends ImageView {
    private static final String TAG = "ImageViewPlus";

    //外圆的宽度
    private int outCircleWidth;

    //外圆的颜色
    private int outCircleColor = Color.GREEN;

    //画笔
    private Paint paint;

    //view的宽度和高度
    private int viewWidth;
    private int viewHeight;

    private Bitmap image;

    public ImageViewPlus(Context context) {
        this(context,null);
    }

    public ImageViewPlus(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ImageViewPlus(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        initAttrs(context,attrs,defStyleAttr);
    }

    /**
     * 初始化属性
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttrs(Context context,AttributeSet attrs,int defStyleAttr){
        TypedArray array =null;
        if(attrs!=null){
            array = context.obtainStyledAttributes(attrs, R.styleable.ImageViewPlus);
            int len = array.length();
            for(int i =0;i<len;i++){
                int attr = array.getIndex(i);
                switch (attr){
                    case R.styleable.ImageViewPlus_outCircleColor:  //获取到外圆的颜色
                        this.outCircleColor= array.getColor(attr,Color.GREEN);
                        break;
                    case R.styleable.ImageViewPlus_outCircleWidth:  //获取到外圆的半径
                        this.outCircleWidth = (int) array.getDimension(attr,5);
                        break;
                }
            }
            paint = new Paint();
            paint.setColor(outCircleColor);
            paint.setAntiAlias(true);
            array.recycle();

        }

    }


    /**
     * view 的测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measure(widthMeasureSpec);
        int height = measure(heightMeasureSpec);

        viewWidth = width - outCircleWidth * 2;
        viewHeight = height - outCircleWidth * 2;

        //调用该方法 将测量后的宽和高设置进去，完成测量工作
        setMeasuredDimension(width,height);
    }

    /**
     * 测量宽和高
     * @param widthMeasureSpec
     * @return
     */
    private int measure(int widthMeasureSpec) {
        int result = 0;
        //从MeasureSpec对象中提取出来具体的测量模式和大小
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            //测量的模式，精确
            result = size;
        } else {
            result = viewWidth;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //加载图片
        loadImage();

        if (image!=null){
            //拿到最小的值当做半斤
            int size = Math.min(viewWidth,viewHeight);

            int circleCenter =size/2;

            image = Bitmap.createScaledBitmap(image,size,size,false);

            //画圆
            canvas.drawCircle(circleCenter+outCircleWidth,circleCenter+outCircleWidth,circleCenter+outCircleWidth,paint);

            canvas.drawBitmap(createCircleBitmap(image,size),outCircleWidth,outCircleWidth,null);

        }
    }

    /**
     * 创建给圆形的Bitmap
     * @param image 传入的image
     * @param size 圆形半径
     * @return 绘制好的圆形Bitmap
     */
    private Bitmap createCircleBitmap(Bitmap image,int size){
        Bitmap bmp = null;

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        bmp = Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmp);
        //画一个和图片大小相等的画布
        canvas.drawCircle(size/2,size/2,size/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //绘制Bitmap
        canvas.drawBitmap(image,0,0,paint);
        return  bmp;
    }

    /**
     * 加载 Bitmap
     */
    private void  loadImage (){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();
        if (bitmapDrawable!=null){
            image= bitmapDrawable.getBitmap();
        }
    }


    public void setOutCircleWidth(int outCircleWidth) {
        this.outCircleWidth = outCircleWidth;
    }

    public void setOutCircleColor(int outCircleColor) {
        this.outCircleColor = outCircleColor;
    }
}
