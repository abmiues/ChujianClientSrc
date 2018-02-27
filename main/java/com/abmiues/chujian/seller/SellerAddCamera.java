package com.abmiues.chujian.seller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.abmiues.chujian.HttpRequestUtil;
import com.abmiues.chujian.HttpSendCallback;
import com.abmiues.chujian.R;

public class SellerAddCamera extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_camera);
        ImageButton imgbtn_back= (ImageButton) findViewById(R.id.imgbtn_back);
        Button btnAdd=(Button)findViewById(R.id.btn_addCamera);
        final EditText editName=(EditText)findViewById(R.id.Edit_cameraName);
        final EditText editAddr=(EditText)findViewById(R.id.Edit_cameraAddr);
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerAddCamera.this.finish();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=editName.getText().toString();
                String addr=editAddr.getText().toString();
                if(name.equals(""))
                    Toast.makeText(SellerAddCamera.this,"请输入摄像头名称",Toast.LENGTH_SHORT).show();
                if(addr.equals(""))
                    Toast.makeText(SellerAddCamera.this,"请输入摄像头地址",Toast.LENGTH_SHORT).show();
                String params="address="+addr+"&camname="+name;
                HttpRequestUtil.Send("addcamera", params, new HttpSendCallback() {
                    @Override
                    public void getdata(String data) {
                        Toast.makeText(SellerAddCamera.this,"添加成功",Toast.LENGTH_SHORT).show();
                        setResult(111,getIntent());
                        SellerAddCamera.this.finish();
                    }
                });
            }
        });
    }
}
