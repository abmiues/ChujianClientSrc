package com.abmiues.chujian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {
    String code="";
    SharedPreferences localdata;
    String ip;
    int host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        localdata=getSharedPreferences("localdata", Context.MODE_PRIVATE);
        if(localdata.getString("ip",null)!=null)
        {
            ip=localdata.getString("ip","10.0.2.2");
            host=localdata.getInt("host",0);
        }
        else
        {
            ip="101.132.150.53";
            host=80;
            //ip="192.168.1.101";
            //ip="172.20.10.5";
            localdata.edit().putString("ip",ip).commit();
            localdata.edit().putInt("host",host).commit();
        }

        Handler x= new Handler();
        x.postDelayed(new Runnable() {
            @Override
            public void run() {
                String url="http://"+ip+":"+host+"/ChujianServer/user/autologin";
                //startActivity(new Intent(FirstActivity.this,PlayerActivity.class).putExtra("url","http://open.ys7.com/openlive/ebbb77823054487a837e2908f92bb3ee.m3u8"));
                new HttpRequestUtil(url, "", new HttpSendCallback() {
                    @Override
                    public void getdata(String data) {
                        if(data.equals("111"))
                        {
                            startActivity(new Intent(FirstActivity.this,MainActivity.class));
                            FirstActivity.this.finish();
                        }
                        else
                            startActivity(new Intent(FirstActivity.this,LoginActivity.class));
                            FirstActivity.this.finish();
                    }
                },getApplicationContext()).execute();

            }
        },1000);
        /*ezaccessToken = EZOpenSDK.getInstance().getEZAccessToken();
        if(ezaccessToken!=null) {
            String accessToken = ezaccessToken.getAccessToken();
            if (accessToken!=null && !accessToken.equals(""))
            {
                Toast.makeText(getApplicationContext(),"获取成功",Toast.LENGTH_SHORT).show();
                getCamera();
            }
            else
                Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                registerKey();
        }*/
    }


}
