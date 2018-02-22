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

import static com.abmiues.chujian.R.id.img_video;

public class SearchSellerActivity extends AppCompatActivity {
    LinearLayout contentView;
    SharedPreferences localdata;
    String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_seller);
        localdata=getSharedPreferences("localdata", Context.MODE_PRIVATE);
        ip=localdata.getString("ip","10.0.2.2");
        contentView= (LinearLayout)findViewById(R.id.contentView);
        ImageButton imgbtn_back= (ImageButton) findViewById(R.id.imgbtn_back);
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchSellerActivity.this.finish();
            }
        });
        drawSeller(getIntent().getExtras().getString("data"));
    }
    public void drawSeller(final String data)
    {
        try {
            JSONArray jsondata=new JSONArray(data);
            for (int i=0;i<jsondata.length();i++)
            {
                final JSONObject jb=jsondata.getJSONObject(i);
                View view=drawSellerItem(jb);
                ImageView img= (ImageView) view.findViewById(R.id.img_seller);//商家头像
                TextView text_name= (TextView) view.findViewById(R.id.name);//商家名字
                TextView text_comment= (TextView) view.findViewById(R.id.text_comment);//评价
                ImageView img_video= (ImageView) view.findViewById(R.id.img_video);//直播状态
                TextView text_menu= (TextView) view.findViewById(R.id.text_menu);//菜系
                text_name.setText((String)jb.get("name"));
                GetImgByUrl.setUrlImg(img,"http://"+ip+"/ChujianServer/images/"+jb.get("sellerid")+"/icon.png");
                img_video.setImageResource((Integer) jb.get("state")==1? R.mipmap.live_orange:R.mipmap.live_black);
                text_comment.setText("店铺评分"+(Integer)jb.get("comment"));
                text_menu.setText("回锅肉、冰淇淋、黄焖鸡。");
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(SearchSellerActivity.this,SellerDetailActivity.class).putExtra("sellerinfo",jb.toString()));
                    }
                });
                contentView.addView(view);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public View drawSellerItem(JSONObject data)
    {
        LayoutInflater inflater = (LayoutInflater)getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_seller, null);
        return view;
    }
}
