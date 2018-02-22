package com.abmiues.chujian;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.model.ImageVideoModelLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.bitmap;
import static android.R.attr.path;
import static android.R.attr.scaleType;

/**
 * Created by Administrator on 2017/2/21.
 */

public class GetImgByUrl {


    public static void  setUrlImg(final ImageView view, final String url)
    {
        setUrlImg(view,url,false,1);
    }
    public static void  setUrlImg(final ImageView view, final String url,double scale)
    {
        setUrlImg(view,url,false,scale);
    }
    public static void  setUrlImg(final ImageView view, final String url,boolean needFull)
    {
        setUrlImg(view,url,needFull,1);
    }
     public static void setUrlImg(final ImageView view, final String url, final boolean needFull, final double scale)
     {


           /*class PicHandler extends Handler {

             @Override
             public void handleMessage(Message msg) {
                 Drawable myimg = (Drawable)msg.obj;
                 if(myimg!=null)
                 view.setImageDrawable(myimg);
             }
         }
         final PicHandler pic_hdl = new PicHandler();
         new Thread(new Runnable() {
             @Override
             public void run() {
                 InputStream is = null;
                 Drawable d = null;
                 try {
                     is = (InputStream) new URL(url).getContent();
                     d = Drawable.createFromStream(is, "src");
                     d.setBounds(0, 0, 100, 100);
                     is.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 Message msg = pic_hdl.obtainMessage();
                 msg.what = 0;
                 msg.obj = d;
                 pic_hdl.sendMessage(msg);
                 //return img;
             }
         }).start();*/
         
         
         class PicHandler extends Handler {

             @Override
             public void handleMessage(Message msg) {
                 Bitmap myimg = (Bitmap)msg.obj;
                 ViewGroup.LayoutParams params=view.getLayoutParams();
                 if(myimg!=null)
                 {
                     params.height=myimg.getHeight();
                     if(needFull==true){
                         params.width= ViewGroup.LayoutParams.MATCH_PARENT;
                         int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                         int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                         view.measure(w, h);
                         int height =view.getMeasuredHeight();
                         int width =view.getMeasuredWidth();
                         params.height= width*myimg.getHeight()*4/5/myimg.getWidth();
                         view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                     }
                     else
                     params.width= (int) (myimg.getWidth()*scale);

                     params.height=(int) (params.height*scale);
                     savePicture(myimg,url);
                     view.setLayoutParams(params);
                    view.setImageBitmap(myimg);}
             }
         }
         final PicHandler pic_hdl = new PicHandler();
         new Thread(new Runnable() {
             @Override
             public void run() {
                 Bitmap img = null;
                 int index=url.indexOf("ChujianServer");
                 String path=url.substring(index);
                 String pictureName = "/mnt/sdcard/chujian/" + path;
                 int last=pictureName.lastIndexOf("/");
                 path=pictureName.substring(0,last);
                 File file = new File(pictureName);
                 if(!file.exists())
                 {
                     try {
                         URL picurl = new URL(url);
                         // 获得连接
                         HttpURLConnection conn = (HttpURLConnection)picurl.openConnection();
                         conn.setConnectTimeout(6000);//设置超时
                         conn.setDoInput(true);
                         conn.setUseCaches(true);//不缓存
                         conn.connect();
                         InputStream is = conn.getInputStream();//获得图片的数据流
                         img = BitmapFactory.decodeStream(is);
                         is.close();
                     } catch (Exception e) {
                         e.printStackTrace();
                         img=null;
                     }
                 }
                 else
                 {
                     img = BitmapFactory.decodeFile(pictureName);
                 }
                 Message msg = pic_hdl.obtainMessage();
                 msg.what = 0;
                 msg.obj = img;
                 pic_hdl.sendMessage(msg);
                 //return img;
             }
         }).start();

     }
    public static void savePicture(Bitmap bitmap ,String url)
    {
        int index=url.indexOf("ChujianServer");
        String path=url.substring(index);
        String pictureName = "/mnt/sdcard/chujian/" + path;
        int last=pictureName.lastIndexOf("/");
        path=pictureName.substring(0,last);
        File file = new File(pictureName);
        if(!file.exists()){
            if(!file.getParentFile().exists())
            {
                if(!file.getParentFile().mkdirs()) {//TODO 目录创建失败
                }
            }
            FileOutputStream out;
            try
            {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


}
