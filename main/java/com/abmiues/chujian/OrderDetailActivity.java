package com.abmiues.chujian;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.data;
import static com.abmiues.chujian.R.id.contentView;
import static com.abmiues.chujian.R.id.text_name;
import static com.abmiues.chujian.R.id.text_price;
import static com.abmiues.chujian.R.id.text_time;

public class OrderDetailActivity extends AppCompatActivity {
String ip;
    SharedPreferences localdata;
    int orderid;
    LinearLayout contentView;
    String sellerid;
    int host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        localdata=getSharedPreferences("localdata",Context.MODE_PRIVATE);
        ip=localdata.getString("ip","10.0.2.2");
        host=localdata.getInt("host",80);
        ImageView img= (ImageView) findViewById(R.id.img_seller);//商家头像

        TextView text_name= (TextView) findViewById(R.id.sellername);//商家名字
        TextView text_time= (TextView) findViewById(R.id.text_time);//时间
        TextView text_remark= (TextView)findViewById(R.id.text_remark);//备注
        TextView text_priceall= (TextView) findViewById(R.id.text_priceall);//价格
        contentView= (LinearLayout) findViewById(R.id.contentView);
        ImageButton imgbtn_back= (ImageButton) findViewById(R.id.imgbtn_back);
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailActivity.this.finish();
            }
        });
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(getIntent().getExtras().getString("data"));
                String time = jsonObject.getString("time");
                sellerid = jsonObject.getString("sellerid");
                double price = jsonObject.getDouble("price");
                String remark = jsonObject.getString("remark");
                String sellername = jsonObject.getString("sellername");
                orderid = jsonObject.getInt("orderid");
                text_name.setText(sellername);
                text_time.setText(time);
                GetImgByUrl.setUrlImg(img,"http://"+ip+"/ChujianServer/images/"+sellerid+"/icon.png");
                text_priceall.setText("¥"+price);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        createOrderlist();
    }
    public void createOrderlist()
    {
        new HttpRequestUtil("http://"+ip+":"+host+"/ChujianServer/user/getorderdetail","orderid="+orderid,new HttpSendCallback() {
            @Override
            public void getdata(String data) {
                if(data.equals(""))
                    Toast.makeText(OrderDetailActivity.this,"数据为空",Toast.LENGTH_LONG).show();
                else
                {

                    try {
                        JSONArray jsonArray=new JSONArray(data);
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            int foodid=jsonObject.getInt("foodid");
                            int num=jsonObject.getInt("num");
                            int orderid=jsonObject.getInt("orderid");
                            String remark=jsonObject.getString("remark");
                            String sellername=jsonObject.getString("sellername");
                            String foodname=jsonObject.getString("foodname");
                            double price=jsonObject.getDouble("price");
                            createItem( sellerid, foodname, foodid, num, price);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        },OrderDetailActivity.this).execute();
    }
    public void createItem(String sellerid,String name,int foodid,int num,double price)
    {
        LayoutInflater inflater = (LayoutInflater)OrderDetailActivity.this.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_order, null);
        ImageView img= (ImageView) view.findViewById(R.id.img_food);//食物图片
        TextView text_name= (TextView) view.findViewById(R.id.name);//食物名字
        TextView text_remark= (TextView) view.findViewById(R.id.text_remark);//口味
        TextView text_price= (TextView) view.findViewById(R.id.text_price);//价格
        TextView text_num= (TextView) view.findViewById(R.id.text_num);
        TextView text_time=(TextView) view.findViewById(R.id.text_time);
        text_time.setVisibility(View.GONE);
        text_name.setText(name);
        text_num.setText(String.valueOf(num));
        GetImgByUrl.setUrlImg(img,"http://"+ip+"/ChujianServer/images/"+sellerid+"/"+foodid+".png");
        text_price.setText("¥"+price);
        contentView.addView(view);
    }
}
