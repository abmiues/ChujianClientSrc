package com.abmiues.chujian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchCameraActivity extends AppCompatActivity {
    LinearLayout contentView;
    SharedPreferences localdata;
    String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_camera);
        localdata=getSharedPreferences("localdata", Context.MODE_PRIVATE);
        ip=localdata.getString("ip","10.0.2.2");
        contentView= (LinearLayout)findViewById(R.id.contentView);
        ImageButton imgbtn_back= (ImageButton) findViewById(R.id.imgbtn_back);
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchCameraActivity.this.finish();
            }
        });
        drawVideo(getIntent().getExtras().getString("data"));
    }
    public void drawVideo(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                LayoutInflater inflater = (LayoutInflater) SearchCameraActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.item_livevideo, null);
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                ImageView img_seller=(ImageView)view.findViewById(R.id.img_seller);
                ImageView img = (ImageView) view.findViewById(R.id.img_video);//商家头像
                TextView text_name = (TextView) view.findViewById(R.id.text_name);//商家名字
                TextView text_num = (TextView) view.findViewById(R.id.text_num);//观看人数
                text_name.setText(jsonObject.getString("name"));
                final String url=jsonObject.getString("address");
                GetImgByUrl.setUrlImg(img_seller, "http://" + ip + "/ChujianServer/images/" + jsonObject.getString("sellerid") + "/icon.png");
                GetImgByUrl.setUrlImg(img, "http://" + ip + "/ChujianServer/images/" + jsonObject.getString("sellerid") + "/camera.png");
                text_num.setText("0");
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(SearchCameraActivity.this,WebUi.class).putExtra("url",url));
                    }
                });
                contentView.addView(view);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
