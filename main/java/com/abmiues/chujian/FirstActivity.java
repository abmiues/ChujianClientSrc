package com.abmiues.chujian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.abmiues.Utils.GlobleValue;
import com.abmiues.chujian.seller.SellerMainActivity;
import com.abmiues.chujian.user.MainActivity;

public class FirstActivity extends AppCompatActivity {
    String code="";
    SharedPreferences localdata;
    String ip;
    int host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        localdata = getSharedPreferences("localdata", Context.MODE_PRIVATE);
        //保存全局变量
        GlobleValue.set_globleData(localdata);

        //这里先判断不为空是因为实际上不为空的情况会更多，对if-else 来说就不需要再跳到else了，省一次执行指令。设计上，执行概率高的放在if - else if - else 靠前点，概率最低的放在else里。当然是在条件数量相同的前提下。
        if (localdata.getString("ip", null) != null) {
            ip = localdata.getString("ip", "10.0.2.2");
            host = localdata.getInt("host", 0);
        } else//如果是空，表示第一次启动
        {
            ip = "101.132.150.53";
            host = 80;
            //ip="192.168.1.101";
            //ip="172.20.10.5";
            localdata.edit().putString("ip", ip).commit();
            localdata.edit().putInt("host", host).commit();
        }

        //保存全局变量
        GlobleValue.set_ip(ip);
        GlobleValue.set_host(host);
        GlobleValue.set_userid(GlobleValue.get_globleData().getString("userid", ""));

        boolean isUser = localdata.getBoolean("isUser", true);
        if (isUser) {
            GlobleValue.set_isUser(true);
        } else
        {
            GlobleValue.set_isUser(false);
        }

        Handler x= new Handler();
        x.postDelayed(new Runnable() {
            @Override
            public void run() {


                HttpRequestUtil.Send("autologin","",new HttpSendCallback() {
                    @Override
                    public void getdata(String data) {
                        if(data.equals("111"))
                        {
                            startActivity(new Intent(FirstActivity.this,GlobleValue.get_isUser()?MainActivity.class: SellerMainActivity.class));
                            FirstActivity.this.finish();
                        }
                        else
                        {
                            startActivity(new Intent(FirstActivity.this,LoginActivity.class));
                        }
                        FirstActivity.this.finish();
                    }
                });

            }
        },1000);
    }


}
