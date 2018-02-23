package com.abmiues.chujian;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetPwdActivity extends AppCompatActivity {
    EditText edit_pwd;
    EditText edit_checkpwd;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pwd);
        phone=getIntent().getExtras().getString("phone");
        edit_pwd= (EditText) findViewById(R.id.edit_pwd);
        edit_checkpwd= (EditText) findViewById(R.id.edit_checkpwd);
        Button btn_register= (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd=edit_pwd.getText().toString();
                if(pwd!=null && !pwd.equals(""))
                     HttpRequestUtil.Send("register", "userid="+phone+"&pwd="+pwd+"&tel="+phone+"&name="+phone, new HttpSendCallback() {
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
                    });
                else
                    Toast.makeText(SetPwdActivity.this,"请输入密码",Toast.LENGTH_LONG).show();

            }
        });

    }
}