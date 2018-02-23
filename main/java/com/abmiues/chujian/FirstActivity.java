package com.abmiues.chujian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.abmiues.Utils.GlobleValue;

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

        //保存全局变量
        GlobleValue.set_globleData(localdata);
        GlobleValue.set_ip(ip);
        GlobleValue.set_host(host);
        GlobleValue.set_userFuncHead("ChujianServer/user/");//设置用户请求方法头
        GlobleValue.set_userid(GlobleValue.get_globleData().getString("userid",""));

        Handler x= new Handler();
        x.postDelayed(new Runnable() {
            @Override
            public void run() {
                HttpRequestUtil.Send("autologin","",new HttpSendCallback() {
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
                });

            }
        },1000);
    }


}
