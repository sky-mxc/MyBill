package com.skymxc.mybill.util;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sky-mxc
 * 文件帮助类
 */
public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final String headImgPath ="/mybill/header.jpg";

    /**
     * 将文件保存为 头像
     * @param path
     */
    public static File saveHeadImg(String path) {
        File file = getHeadImage();

        try {
            if (!file.exists()){
                file.createNewFile();
            }
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            int len ;
            byte[] b = new byte[1024];
            while ((len=bis.read(b))!=-1){
                bos.write(b,0,len);
            }
            bos.flush();
            bos.close();
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 返回头像
     * 使用时不保证文件是否存在,但是文件夹是真正存在的；
     * @return
     */
    public static File getHeadImage(){
        File file = new File(Environment.getExternalStorageDirectory(), "mybill");
        if (!file.exists()) {
            file.mkdir();
        }
       File mfile = new File(file, "header.jpg");

        return  mfile;
    }

}
