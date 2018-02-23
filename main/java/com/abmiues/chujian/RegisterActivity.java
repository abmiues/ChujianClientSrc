package com.abmiues.chujian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText edit_phone;
    EditText edit_checkcode;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edit_phone= (EditText) findViewById(R.id.edit_phone);
        edit_checkcode= (EditText) findViewById(R.id.edit_checkcode);
        Button btn_getcode= (Button) findViewById(R.id.btn_check);
        Button btn_register= (Button) findViewById(R.id.btn_register);
        btn_getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone=edit_phone.getText().toString();
                 HttpRequestUtil.Send("getcheckcode", phone, new HttpSendCallback() {
                    @Override
                    public void getdata(String data) {

                    }
                });
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phone!=null && !phone.equals(""))
                {startActivity(new Intent(RegisterActivity.this,SetPwdActivity.class).putExtra("phone",phone));
                RegisterActivity.this.finish();}
                else
                    Toast.makeText(RegisterActivity.this,"请先获取验证码",Toast.LENGTH_LONG).show();
            }
        });

    }
}
