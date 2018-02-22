package com.abmiues.chujian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetPwdActivity extends AppCompatActivity {
    EditText edit_pwd;
    EditText edit_checkpwd;
    SharedPreferences localdata;
    String ip;
    String phone;
    int host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pwd);
        phone=getIntent().getExtras().getString("phone");
        localdata=getSharedPreferences("localdata", Context.MODE_PRIVATE);
        ip=localdata.getString("ip","10.0.2.2");
        host=localdata.getInt("host",80);
        edit_pwd= (EditText) findViewById(R.id.edit_pwd);
        edit_checkpwd= (EditText) findViewById(R.id.edit_checkpwd);
        Button btn_register= (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd=edit_pwd.getText().toString();
                if(pwd!=null && !pwd.equals(""))
                    new HttpRequestUtil("http://" + ip+":"+host + "/ChujianServer/user/register", "userid="+phone+"&pwd="+pwd+"&tel="+phone+"&name="+phone, new HttpSendCallback() {
                        @Override
                        public void getdata(String data) {
                            if(data.equals("111"))
                            {Toast.makeText(SetPwdActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                               // startActivity(new Intent(SetPwdActivity.this,LoginActivity.class));
                                SetPwdActivity.this.finish();
                            }
                            else if(data.equals("100"))
                                Toast.makeText(SetPwdActivity.this,"账号已存在",Toast.LENGTH_LONG).show();
                        }
                    },SetPwdActivity.this).execute();
                else
                    Toast.makeText(SetPwdActivity.this,"请输入密码",Toast.LENGTH_LONG).show();

            }
        });

    }
}