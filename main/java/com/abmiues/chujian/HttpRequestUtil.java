package com.abmiues.chujian;


import android.os.AsyncTask;

import com.abmiues.Utils.GlobleValue;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by George-Xu on 2017/1/9.
 * 网络访问类，初始化传入地址，参数，回调函数
 */

public class HttpRequestUtil
{
    /**
     * 传入方法名，参数，回调方法，访问主服务器地址
     * @param funcName
     * @param params
     * @param callback
     */
    public static void Send(String funcName,String params,HttpSendCallback callback)
    {
        Send(GlobleValue.get_ip(),GlobleValue.get_host(),GlobleValue.get_userFuncHead()+funcName,params,callback);
    }

    /**
     * 手动传入ip,端口，方法地址访问，用于访问其他服务器。
     * @param ip
     * @param host
     * @param url
     * @param params
     * @param callback
     */
    public static void Send (String ip,int host,String url, String params, HttpSendCallback callback)
    {
        String urlstr="http://"+ ip+":"+host+"/"+url;
        new MyAsyncTask(urlstr,params,callback).execute();
    }

    /**
     * 本地测试方法，不进行网络请求
     * @param callback
     * @param islocal
     */
    public static void Send(HttpSendCallback callback)
    {
        new MyAsyncTask(callback).execute();
    }

}

class MyAsyncTask extends AsyncTask<Void, Void, String>{
    private  HttpSendCallback callback;
    private String urlstr;
    private String params;
    private  boolean islocal;
    private String result="";
    public  MyAsyncTask (String urlstr, String params, HttpSendCallback callback)
    {
        this.callback=callback;
        this.urlstr=urlstr;
        this.params=params;
        this.islocal=false;
    }
    public MyAsyncTask(HttpSendCallback callback)
    {
        this.callback=callback;
        this.islocal=true;
    }
    @Override
    protected String doInBackground(Void... param) {
        URL url= null;
        if(this.islocal)
            return "111";
        try {
            url = new URL(urlstr);
            HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
            //设置参数
            httpConn.setDoOutput(true);   //需要输出
            httpConn.setDoInput(true);   //需要输入
            httpConn.setUseCaches(false);  //不允许缓存
            httpConn.setRequestMethod("POST");   //设置POST方式连接
            httpConn.setInstanceFollowRedirects(false);
            //设置请求属性
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpConn.setRequestProperty("Charset", "UTF-8");
            String sessionid=GlobleValue.get_globleData().getString("sessionid","");
            if(!sessionid.equals("")){
                httpConn.setRequestProperty("cookie", sessionid);
            }
            //连接,也可以不用connect，使用下面的httpConn.getOutputStream()会自动connect
            httpConn.connect();
            //建立输入流，向指向的URL传入参数

            DataOutputStream dos=new DataOutputStream(httpConn.getOutputStream());
            dos.write(params.getBytes());
            dos.flush();
            dos.close();
            //获得响应状态
            String cookieval = httpConn.getHeaderField("set-cookie");//获取session
            if(cookieval != null) {//当服务器不存在发送过去的session时，此值不为空
                String sessionid2 = cookieval.substring(0, cookieval.indexOf(";"));
                if(!sessionid2.equals(sessionid))
                {
                    GlobleValue.get_globleData().edit().putString("sessionid",sessionid2).commit();
                    return "010";
                }
            }
            int resultCode=httpConn.getResponseCode();
            if(HttpURLConnection.HTTP_OK==resultCode) {
                StringBuffer sb= new StringBuffer();
                String readLine = new String();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine);
                }
                responseReader.close();
                result=sb.toString();

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    @Override
    protected void onPostExecute(String result) {
        callback.getdata(result);
    }
}



